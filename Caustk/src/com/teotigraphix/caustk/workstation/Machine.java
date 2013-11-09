////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.workstation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class Machine extends CaustkComponent {

    private transient IRack rack;

    IRack getRack() {
        return rack;
    }

    private transient ICaustkFactory factory;

    private transient Patch pendingPatch;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private RackSet rackSet;

    @Tag(1010)
    private int machineIndex = -1;

    @Tag(102)
    private MachineType machineType;

    @Tag(103)
    private String machineName;

    @Tag(104)
    private RackTone rackTone;

    @Tag(105)
    private Patch patch;

    @Tag(106)
    private Map<Integer, Phrase> phrases = new HashMap<Integer, Phrase>();

    @Tag(107)
    private Map<Integer, SequencerPattern> patterns = new HashMap<Integer, SequencerPattern>();

    @Tag(108)
    private int currentBank;

    @Tag(109)
    private int currentPattern;

    @Tag(110)
    private Map<Integer, Integer> bankEditor = new HashMap<Integer, Integer>();

    @Tag(111)
    private boolean enabled = false;

    @Tag(112)
    private boolean muted = false;

    @Tag(113)
    private boolean selected = false;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return machineName;
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    public final RackSet _getRackSet() {
        return rackSet;
    }

    //----------------------------------
    // machineIndex
    //----------------------------------

    public int getMachineIndex() {
        return machineIndex;
    }

    void setMachineIndex(int value) {
        machineIndex = value;
    }

    //----------------------------------
    // machineType
    //----------------------------------

    public MachineType getMachineType() {
        return machineType;
    }

    //----------------------------------
    // machineName
    //----------------------------------

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String value) {
        machineName = value;
        RackMessage.MACHINE_NAME.send(getRack(), machineIndex, machineName);
    }

    //----------------------------------
    // rackTone
    //----------------------------------

    public RackTone getRackTone() {
        return rackTone;
    }

    //----------------------------------
    // patch
    //----------------------------------

    public Patch getPatch() {
        return patch;
    }

    //----------------------------------
    // phrases
    //----------------------------------

    public Map<Integer, Phrase> getPhrases() {
        return phrases;
    }

    /**
     * Returns the {@link Phrase} using the {@link #getCurrentBank()} and
     * {@link #getCurrentPattern()}.
     */
    public Phrase getPhrase() {
        return getPhrase(currentBank, currentPattern);
    }

    /**
     * Returns a {@link Phrase} at the bank and pattern index, <code>null</code>
     * if the machine has no note data assigned at the specific pattern.
     * 
     * @param bankIndex The bank index.
     * @param patternIndex The pattern index.
     */
    public Phrase findPhrase(int bankIndex, int patternIndex) {
        int index = PatternUtils.getIndex(bankIndex, patternIndex);
        return phrases.get(index);
    }

    /**
     * Returns a {@link Phrase} at the bank and pattern index, if the
     * {@link Phrase} has not been created, this method will create one at the
     * bank and pattern index.
     * 
     * @param bankIndex The bank index.
     * @param patternIndex The pattern index.
     */
    public Phrase getPhrase(int bankIndex, int patternIndex) {
        int index = PatternUtils.getIndex(bankIndex, patternIndex);
        Phrase phrase = phrases.get(index);
        if (phrase == null) {
            phrase = factory.createPhrase(this, bankIndex, patternIndex);
            phrases.put(index, phrase);
        }
        return phrase;
    }

    //----------------------------------
    // patterns
    //----------------------------------

    public Map<Integer, SequencerPattern> getPatterns() {
        return patterns;
    }

    //----------------------------------
    // currentBank
    //----------------------------------

    public int getCurrentBank() {
        return currentBank;
    }

    /**
     * @param value
     * @see OnTrackBankChange
     */
    public void setCurrentBank(int value) {
        if (value == currentBank)
            return;
        currentBank = value;
        getRackTone().getPatternSequencer().setSelectedBank(currentBank);
        getRack().getDispatcher().trigger(new OnMachineChange(this, MachineChangeKind.Bank));
    }

    //----------------------------------
    // currentPattern
    //----------------------------------

    public int getCurrentPattern() {
        return currentPattern;
    }

    /**
     * @param value
     * @see OnTrackPatternChange
     */
    public void setCurrentPattern(int value) {
        if (value == currentPattern)
            return;
        currentPattern = value;
        bankEditor.put(currentBank, currentPattern);
        getRackTone().getPatternSequencer().setSelectedPattern(currentPattern);
        getRack().getDispatcher().trigger(new OnMachineChange(this, MachineChangeKind.Pattern));
    }

    public int getEditPattern() {
        if (!bankEditor.containsKey(currentBank))
            return 0;
        return bankEditor.get(currentBank);
    }

    /**
     * @param bank
     * @param pattern
     * @see OnTrackBankChange
     * @see OnTrackPatternChange
     */
    public void setCurrentBankPattern(int bank, int pattern) {
        setCurrentBank(bank);
        setCurrentPattern(pattern);
    }

    //----------------------------------
    // enabled
    //----------------------------------

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean value) {
        if (value == enabled)
            return;
        enabled = value;
        // firePropertyChange(TonePropertyKind.ENABLED, mEnabled);
    }

    //----------------------------------
    // muted
    //----------------------------------

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean value) {
        if (value == muted)
            return;
        muted = value;
        // firePropertyChange(TonePropertyKind.MUTE, mMuted);
        getMixer().setMute(muted);
    }

    //----------------------------------
    // selected
    //----------------------------------

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean value) {
        if (value == selected)
            return;
        selected = value;
        // firePropertyChange(TonePropertyKind.SELECTED, mSelected);
    }

    //----------------------------------
    // mixer
    //----------------------------------

    public final MachineMixer getMixer() {
        return patch.getMixer();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Machine() {
    }

    Machine(ComponentInfo info, int machineIndex, MachineType machineType, String machineName) {
        setInfo(info);
        this.machineIndex = machineIndex;
        this.machineType = machineType;
        this.machineName = machineName;
    }

    Machine(ComponentInfo info, RackSet rackSet, int index, MachineType machineType,
            String machineName) {
        setInfo(info);
        this.rackSet = rackSet;
        this.machineIndex = index;
        this.machineType = machineType;
        this.machineName = machineName;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void addPattern(int bankIndex, int patternIndex, int startBeat, int endBeat) {
        SequencerPattern pattern = new SequencerPattern(this);
        pattern.setBankPattern(bankIndex, patternIndex);
        pattern.setLocation(startBeat, endBeat);
        patterns.put(startBeat, pattern);
        System.out.println("Add pattern:" + pattern.toString());
    }

    public SequencerPattern removePattern(int bankIndex, int patternIndex, int startBeat,
            int endBeat) {
        SequencerPattern pattern = patterns.remove(startBeat);
        return pattern;
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                // set the rack and factory from this context
                factory = context.getFactory();
                rack = factory.getRack();

                // when machineType is null, the Machine is a placeholder until the type is set
                // in this case we do not create it yet, but we need the factory and
                // rack refs set above so they exist if it is assigned this session
                if (machineType == null)
                    return;

                if (rackTone != null)
                    throw new IllegalStateException("Tone exists in machine");

                ToneDescriptor descriptor = new ToneDescriptor(machineIndex, machineName,
                        MachineType.fromString(machineType.getType()));
                try {
                    rackTone = factory.createRackTone(this, descriptor);
                    rackTone.create(context);
                } catch (CausticException e) {
                    throw e;
                }
                if (pendingPatch == null) {
                    patch = factory.createPatch(this);
                    patch.create(context);
                } else {
                    patch = pendingPatch;
                    patch.setMachine(this);
                    patch.update(context);
                    pendingPatch = null;
                }
                break;

            case Load:
                factory = context.getFactory();
                rack = factory.getRack();

                create(context);

                //        if (populateTone) {
                //            loadTone(factory);
                //        }
                try {
                    loadPatch(context);
                } catch (IOException e) {
                    throw new CausticException(e);
                }
                loadPhrases(context);
                break;

            case Update:
                if (rackTone == null && machineType != null)
                    throw new IllegalStateException("RackTone cannot be null during update()");

                // set the rack and factory from this context
                factory = context.getFactory();
                rack = factory.getRack();

                // the case of the unassigned Machine(type) in an AudioTrack
                if (machineType != null) {
                    updateTone(context);
                    updatePatch(context);
                    updatePhrases(context);
                }

                break;

            case Restore:
                break;

            case Connect:
                factory = context.getFactory();
                rack = context.getRack();
                rackTone.setRack(rack);
                break;

            case Disconnect:
                patterns.clear();
                phrases.clear();
                rack = null;
                rackSet = null;
                break;
        }
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onSave() {
        // XXX TEMP HACK
        if (rackTone != null)
            rackTone.onSave();
        patch.onSave();
        for (Phrase phrase : phrases.values()) {
            phrase.onSave();
        }
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    /**
     * Loads the machine's {@link Patch} from the native machine's preset values
     * and mixer channel.
     * 
     * @param factory The library factory.
     * @throws IOException
     * @throws CausticException
     */
    private void loadPatch(ICaustkApplicationContext context) throws IOException, CausticException {
        // patch = context.getFactory().createPatch(this);
        patch.load(context);
    }

    /**
     * Loads the machine's {@link Phrase}s that currently exist in the native
     * rack.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    private void loadPhrases(ICaustkApplicationContext context) throws CausticException {
        final IRack rack = context.getRack();
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(rack,
                machineIndex);
        if (patterns != null) {
            createPatternsFromLoadOperation(context, patterns);
        }
    }

    private void createPatternsFromLoadOperation(ICaustkApplicationContext context, String patterns)
            throws CausticException {
        for (String patternName : patterns.split(" ")) {
            int bankIndex = PatternUtils.toBank(patternName);
            int patternIndex = PatternUtils.toPattern(patternName);

            Phrase caustkPhrase = context.getFactory().createPhrase(this, bankIndex, patternIndex);
            phrases.put(caustkPhrase.getIndex(), caustkPhrase);
            caustkPhrase.load(context);
        }
    }

    private void updateTone(ICaustkApplicationContext context) throws CausticException {
        rackTone.update(context);
    }

    private void updatePatch(ICaustkApplicationContext context) throws CausticException {
        patch.update(context);
    }

    private void updatePhrases(ICaustkApplicationContext context) throws CausticException {
        int currentBank = getCurrentBank();
        int currentPattern = getCurrentPattern();
        for (Phrase caustkPhrase : phrases.values()) {
            PatternSequencerMessage.BANK.send(getRack(), machineIndex, caustkPhrase.getBankIndex());
            PatternSequencerMessage.PATTERN.send(getRack(), machineIndex,
                    caustkPhrase.getPatternIndex());
            caustkPhrase.update(context);
        }
        PatternSequencerMessage.BANK.send(getRack(), machineIndex, currentBank);
        PatternSequencerMessage.PATTERN.send(getRack(), machineIndex, currentPattern);
    }

    @SuppressWarnings("unused")
    private void loadTone(CaustkFactory factory) throws CausticException {
        final IRack rack = factory.getRack();

        if (rack.getRackSet().hasMachine(machineIndex))
            throw new IllegalStateException("Tone exists in ISoundSource at index:" + machineIndex);

        ToneDescriptor descriptor = new ToneDescriptor(machineIndex, machineName,
                MachineType.fromString(machineType.getType()));
        rackTone = factory.createRackTone(this, descriptor);
        if (rackTone == null)
            throw new CausticException("Failed to create " + descriptor.toString());
    }

    public void initializeMachine(Patch patch) throws CausticException {
        machineType = patch.getMachineType();
        pendingPatch = patch;
        // pendingPatch will be used as the Machine's patch and will have it's
        // update() called instead of create()
        create(factory.createContext());
    }

    @Override
    public String toString() {
        return "[Machine(" + machineIndex + ", " + machineType.getType() + ", '" + machineName
                + "')]";
    }

    public enum MachineChangeKind {
        Bank,

        Pattern;
    }

    /**
     * @author Michael Schmalle
     * @see IRack#getDispatcher()
     */
    public static class OnMachineChange {

        private Machine machine;

        public Machine getMachine() {
            return machine;
        }

        private MachineChangeKind kind;

        public MachineChangeKind getKind() {
            return kind;
        }

        public OnMachineChange(Machine machine, MachineChangeKind kind) {
            this.machine = machine;
            this.kind = kind;
        }
    }

}

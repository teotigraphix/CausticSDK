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

package com.teotigraphix.caustk.live;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.components.MixerChannel;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class Machine implements ICaustkComponent, IRackSerializer {

    private transient IRack rack;

    private transient ICaustkFactory factory;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private ComponentInfo info;

    @Tag(101)
    private RackTone rackTone;

    @Tag(102)
    private RackSet rackSet;

    @Tag(103)
    private int machineIndex = -1;

    @Tag(104)
    private MachineType machineType;

    @Tag(105)
    private String machineName;

    @Tag(110)
    private int currentBank;

    @Tag(111)
    private int currentPattern;

    @Tag(112)
    private Patch patch;

    @Tag(113)
    private Map<Integer, Phrase> phrases = new HashMap<Integer, Phrase>();

    @Tag(114)
    private Map<Integer, SequencerPattern> patterns = new HashMap<Integer, SequencerPattern>();

    @Tag(115)
    private Map<Integer, Integer> bankEditor = new HashMap<Integer, Integer>();

    @Tag(120)
    private boolean enabled = false;

    @Tag(121)
    private boolean muted = false;

    @Tag(122)
    private boolean selected = false;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public ComponentInfo getInfo() {
        return info;
    }

    //----------------------------------
    // rackTone
    //----------------------------------

    public RackTone getRackTone() {
        return rackTone;
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    public final RackSet _getRackSet() {
        return rackSet;
    }

    IRack getRack() {
        return rack;
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
    // mixer
    //----------------------------------

    public final MixerChannel getMixer() {
        return rackTone.getMixer();
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

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Machine() {
    }

    Machine(ComponentInfo info, int machineIndex, MachineType machineType, String machineName) {
        this.info = info;
        this.machineIndex = machineIndex;
        this.machineType = machineType;
        this.machineName = machineName;
    }

    Machine(ComponentInfo info, RackSet rackSet, int index, MachineType machineType,
            String machineName) {
        this.info = info;
        this.rackSet = rackSet;
        this.machineIndex = index;
        this.machineType = machineType;
        this.machineName = machineName;
    }

    /**
     * Creates the underlying {@link RackTone} instance for the machine using
     * the {@link #getMachineIndex()}, {@link #getMachineName()} and
     * {@link #getMachineType()}.
     * 
     * @throws CausticException Error creating Tone
     */
    @Override
    public void create(ICaustkApplicationContext context) throws CausticException {
        if (rackTone != null)
            throw new IllegalStateException("Tone exists in machine");

        // set the rack and factory from this context
        factory = context.getFactory();
        rack = factory.getRack();

        ToneDescriptor descriptor = new ToneDescriptor(machineIndex, machineName,
                MachineType.fromString(machineType.getType()));
        try {
            rackTone = factory.createRackTone(this, descriptor);
            rackTone.create(context);
        } catch (CausticException e) {
            throw e;
        }

        patch = factory.createPatch(this);
        patch.create(null);
    }

    @Override
    public void update(ICaustkApplicationContext context) {

        // create the Tone
        updateTone(context);

        // assign the patch
        updatePatch(context);

        // create the phrases
        updatePhrases(context);
    }

    private void updateTone(ICaustkApplicationContext context) {
        ToneDescriptor descriptor = new ToneDescriptor(machineIndex, machineName,
                MachineType.fromString(machineType.getType()));
        try {
            rackTone = factory.createRackTone(this, descriptor);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    private void updatePatch(ICaustkApplicationContext context) {
        patch.update(context);
    }

    private void updatePhrases(ICaustkApplicationContext context) {
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

    /**
     * Loads the machine's patch and phrases from the native machine at
     * {@link #getMachineIndex()} in the current native rack.
     * <p>
     * Calling this method will create a {@link RackTone} in the rack's
     * {@link ISoundSource} if populateTone is true.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    // XXX Figure out if this matters in the Phase?
    @Override
    public void load(ICaustkApplicationContext context) throws CausticException {
        //        setRackSet(context.getRackSet());

        //        if (populateTone) {
        //            loadTone(factory);
        //        }
        try {
            loadPatch(context);
        } catch (IOException e) {
            throw new CausticException(e);
        }
        loadPhrases(context);
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

    @Override
    public void restore() {
    }

    /**
     * Loads the machine's {@link Patch} from the mative machine's preset values
     * and mixer channel.
     * 
     * @param factory The library factory.
     * @throws IOException
     * @throws CausticException
     */
    public void loadPatch(ICaustkApplicationContext context) throws IOException, CausticException {
        patch = context.getFactory().createPatch(this);
        patch.load(context);
    }

    /**
     * Loads the machine's {@link Phrase}s that currently exist in the native
     * rack.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    public void loadPhrases(ICaustkApplicationContext context) throws CausticException {
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
    public String toString() {
        return "[Machine(" + machineIndex + ", " + machineType.getType() + ", " + machineName
                + ")]";
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

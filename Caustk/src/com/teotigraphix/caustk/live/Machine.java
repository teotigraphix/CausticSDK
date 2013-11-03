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
import com.teotigraphix.caustk.controller.ICaustkFactory;
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.controller.core.CaustkFactory;
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

    /**
     * This reference allows fast calls to the native Rack, this instance will
     * always be the same reference as rackSet.getRack();
     * <p>
     * This instance is set in the constructor with rackSet passed and is also
     * set in the load(CaustkFactory) method.
     */
    //private transient IRack rack;

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
    private int index = -1;

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

    public final RackSet getRackSet() {
        return rackSet;
    }

    ICaustkFactory getFactory() {
        return rackSet.getFactory();
    }

    public final IRack getRack() {
        return rackSet.getRack();
    }

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return index;
    }

    void setIndex(int value) {
        index = value;
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
        RackMessage.MACHINE_NAME.send(getRack(), index, machineName);
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
            phrase = rackSet.getFactory().createPhrase(this, bankIndex, patternIndex);
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

    Machine(ComponentInfo info, MachineType machineType, String machineName) {
        this.info = info;
        this.machineType = machineType;
        this.machineName = machineName;
    }

    Machine(ComponentInfo info, int index, MachineType machineType, String machineName) {
        this.info = info;
        this.index = index;
        this.machineType = machineType;
        this.machineName = machineName;
    }

    Machine(ComponentInfo info, RackSet rackSet, int index, MachineType machineType,
            String machineName) {
        this.info = info;
        this.rackSet = rackSet;
        this.index = index;
        this.machineType = machineType;
        this.machineName = machineName;
    }

    /**
     * Creates the underlying {@link RackTone} instance for the machine using
     * the {@link #getIndex()}, {@link #getMachineName()} and
     * {@link #getMachineType()}.
     * 
     * @throws CausticException Error creating Tone
     */
    @Override
    public void create() throws CausticException {
        if (rackTone != null)
            throw new IllegalStateException("Tone exists in machine");

        ToneDescriptor descriptor = new ToneDescriptor(index, machineName,
                MachineType.fromString(machineType.getType()));
        try {
            rackTone = rackSet.getFactory().createTone(this, descriptor);
        } catch (CausticException e) {
            throw e;
        }

        patch = rackSet.getFactory().createPatch(this);
        patch.create();
    }

    @Override
    public void restore() {

    }

    @Override
    public void update() {
        // create the Tone
        updateTone();

        // assign the patch
        updatePatch();

        // create the phrases
        updatePhrases();
    }

    private void updateTone() {
        ToneDescriptor descriptor = new ToneDescriptor(index, machineName,
                MachineType.fromString(machineType.getType()));
        try {
            rackTone = rackSet.getFactory().createTone(this, descriptor);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    private void updatePatch() {
        patch.update();
    }

    private void updatePhrases() {
        int currentBank = getCurrentBank();
        int currentPattern = getCurrentPattern();
        for (Phrase caustkPhrase : phrases.values()) {
            PatternSequencerMessage.BANK.send(getRack(), index, caustkPhrase.getBankIndex());
            PatternSequencerMessage.PATTERN.send(getRack(), index, caustkPhrase.getPatternIndex());
            caustkPhrase.update();
        }
        PatternSequencerMessage.BANK.send(getRack(), index, currentBank);
        PatternSequencerMessage.PATTERN.send(getRack(), index, currentPattern);
    }

    /**
     * Loads the machine's patch and phrases from the native machine at
     * {@link #getIndex()} in the current native rack.
     * <p>
     * Calling this method will create a {@link RackTone} in the rack's
     * {@link ISoundSource} if populateTone is true.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    // XXX Figure out if this matters in the Phase?
    @Override
    public void load(IRackContext context) throws CausticException {
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

        if (rack.getRackSet().hasMachine(index))
            throw new IllegalStateException("Tone exists in ISoundSource at index:" + index);

        ToneDescriptor descriptor = new ToneDescriptor(index, machineName,
                MachineType.fromString(machineType.getType()));
        rackTone = factory.createTone(this, descriptor);
        if (rackTone == null)
            throw new CausticException("Failed to create " + descriptor.toString());
    }

    /**
     * Loads the machine's {@link Patch} from the mative machine's preset values
     * and mixer channel.
     * 
     * @param factory The library factory.
     * @throws IOException
     * @throws CausticException
     */
    public void loadPatch(IRackContext context) throws IOException, CausticException {
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
    public void loadPhrases(IRackContext context) throws CausticException {
        final IRack rack = context.getRack();
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(rack, index);
        if (patterns != null) {
            createPatternsFromLoadOperation(context, patterns);
        }
    }

    private void createPatternsFromLoadOperation(IRackContext context, String patterns)
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
        return "[Machine(" + index + ", " + machineType.getType() + ", " + machineName + ")]";
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

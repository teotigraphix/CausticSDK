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

package com.teotigraphix.caustk.machine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.IRackAware;
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.controller.core.CaustkFactory;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.ToneType;
import com.teotigraphix.caustk.utils.PatternUtils;

public class Machine implements ICaustkComponent, IRackAware, IRackSerializer {

    /*
     * The tone is only set when the LiveMachine is actually assigned to a channel
     * in the LiveScene.
     */
    private transient Tone tone;

    private transient IRack rack;

    @Override
    public IRack getRack() {
        return rack;
    }

    @Override
    public void setRack(IRack rack) {
        this.rack = rack;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(3)
    private int index = -1;

    @Tag(4)
    private MachineType machineType;

    @Tag(5)
    private String machineName;

    @Tag(10)
    private int currentBank;

    @Tag(11)
    private int currentPattern;

    @Tag(12)
    private Patch patch;

    @Tag(13)
    private Map<Integer, Phrase> phrases = new HashMap<Integer, Phrase>();

    @Tag(14)
    private Map<Integer, SequencerPattern> patterns = new HashMap<Integer, SequencerPattern>();

    @Tag(15)
    private Map<Integer, Integer> bankEditor = new HashMap<Integer, Integer>();

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
        RackMessage.MACHINE_NAME.send(rack, index, machineName);
    }

    //----------------------------------
    // patch
    //----------------------------------

    public Patch getPatch() {
        return patch;
    }

    public final MixerPreset getMixer() {
        return patch.getMixerPreset();
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
     * Returns a {@link Phrase} at the bank and pattern index,
     * <code>null</code> if the machine has no note data assigned at the
     * specific pattern.
     * 
     * @param bankIndex The bank index.
     * @param patternIndex The pattern index.
     */
    public Phrase getPhrase(int bankIndex, int patternIndex) {
        int index = PatternUtils.getIndex(bankIndex, patternIndex);
        return phrases.get(index);
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
        getTone().getPatternSequencer().setSelectedBank(currentBank);
        //        getDispatcher().trigger(new OnTrackChange(TrackChangeKind.Bank, this));
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
        getTone().getPatternSequencer().setSelectedPattern(currentPattern);
        //        getDispatcher().trigger(new OnTrackChange(TrackChangeKind.Pattern, this));
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

    public Tone getTone() {
        return tone;
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
                ToneType.fromString(machineType.getType()));
        try {
            tone = getRack().getFactory().createTone(descriptor);
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
            PatternSequencerMessage.BANK.send(rack, index, caustkPhrase.getBankIndex());
            PatternSequencerMessage.PATTERN.send(rack, index, caustkPhrase.getPatternIndex());
            caustkPhrase.update();
        }
        PatternSequencerMessage.BANK.send(rack, index, currentBank);
        PatternSequencerMessage.PATTERN.send(rack, index, currentPattern);
    }

    /**
     * Loads the machine's patch and phrases from the native machine at
     * {@link #getIndex()} in the current native rack.
     * <p>
     * Calling this method will create a {@link Tone} in the rack's
     * {@link ISoundSource} if populateTone is true.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    // XXX Figure out if this matters in the Phase?
    @Override
    public void load(IRackContext context) throws CausticException {
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

        if (rack.getScene().hasMachine(index))
            throw new IllegalStateException("Tone exists in ISoundSource at index:" + index);

        ToneDescriptor descriptor = new ToneDescriptor(index, machineName,
                ToneType.fromString(machineType.getType()));
        tone = factory.createTone(descriptor);
        if (tone == null)
            throw new CausticException("Failed to create " + descriptor.toString());
    }

    /**
     * Loads the machine's {@link Patch} from the mative machine's preset
     * values and mixer channel.
     * 
     * @param factory The library factory.
     * @throws IOException
     * @throws CausticException
     */
    public void loadPatch(IRackContext context) throws IOException, CausticException {
        patch = context.getFactory().createPatch(this);
        patch.load(context);
        if (tone != null)
            tone.setDefaultPatchId(patch.getInfo().getId());
    }

    /**
     * Loads the machine's {@link Phrase}s that currently exist in the
     * native rack.
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

            Phrase caustkPhrase = context.getFactory().createPhrase(this, bankIndex,
                    patternIndex);
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
}
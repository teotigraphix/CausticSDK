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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.ISoundSource;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.ToneType;
import com.teotigraphix.caustk.utils.PatternUtils;

public class CaustkMachine implements ICaustkComponent {

    /*
     * The tone is only set when the LiveMachine is actually assigned to a channel
     * in the LiveScene.
     */
    private transient Tone tone;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private String name;

    @Tag(2)
    private File file;

    @Tag(3)
    private int index;

    @Tag(4)
    private MachineType machineType;

    @Tag(10)
    private int currentBank;

    @Tag(11)
    private int currentPattern;

    @Tag(12)
    private CaustkPatch patch;

    @Tag(13)
    private Map<Integer, CaustkPhrase> phrases = new HashMap<Integer, CaustkPhrase>();

    @Tag(14)
    private Map<Integer, Integer> bankEditor = new HashMap<Integer, Integer>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    @Override
    public UUID getId() {
        return id;
    }

    //----------------------------------
    // name
    //----------------------------------

    @Override
    public String getName() {
        return name;
    }

    //----------------------------------
    // file
    //----------------------------------

    @Override
    public File getFile() {
        return file;
    }

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return index;
    }

    //----------------------------------
    // machineType
    //----------------------------------

    public MachineType getMachineType() {
        return machineType;
    }

    //----------------------------------
    // patch
    //----------------------------------

    public CaustkPatch getPatch() {
        return patch;
    }

    public final MixerPreset getMixer() {
        return patch.getMixerPreset();
    }

    //----------------------------------
    // phrases
    //----------------------------------

    public Map<Integer, CaustkPhrase> getPhrases() {
        return phrases;
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
    CaustkMachine() {
    }

    CaustkMachine(UUID id, MachineType machineType) {
        this.id = id;
        this.machineType = machineType;
    }

    CaustkMachine(UUID id, MachineType machineType, int index, String name) {
        this.id = id;
        this.machineType = machineType;
        this.index = index;
        this.name = name;
    }

    /**
     * Loads the machine's patch and phrases from the native machine at
     * {@link #getIndex()} in the current native rack.
     * <p>
     * Calling this method will create a {@link Tone} in the rack's
     * {@link ISoundSource} if populateTone is true.
     * 
     * @param factory The library factory.
     * @throws IOException
     * @throws CausticException
     */
    public void load(CaustkLibraryFactory factory, boolean populateTone) throws IOException,
            CausticException {
        if (populateTone) {
            loadTone(factory);
        }
        loadPatch(factory);
        loadPhrases(factory);
    }

    private void loadTone(CaustkLibraryFactory factory) throws CausticException {
        final IRack rack = factory.getRack();
        ISoundSource soundSource = rack.getSoundSource();

        if (soundSource.hasTone(index))
            throw new IllegalStateException("Tone exists in ISoundSource at index:" + index);

        ToneDescriptor descriptor = new ToneDescriptor(index, name, ToneType.fromString(machineType
                .getType()));
        tone = rack.createTone(descriptor);
        if (tone == null)
            throw new CausticException("Failed to create " + descriptor.toString());
    }

    /**
     * Loads the machine's {@link CaustkPatch} from the mative machine's preset
     * values and mixer channel.
     * 
     * @param factory The library factory.
     * @throws IOException
     * @throws CausticException
     */
    public void loadPatch(CaustkLibraryFactory factory) throws IOException, CausticException {
        patch = factory.createPatch(this);
        patch.load(factory);
        if (tone != null)
            tone.setDefaultPatchId(patch.getId());
    }

    /**
     * Loads the machine's {@link CaustkPhrase}s that currently exist in the
     * native rack.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    public void loadPhrases(CaustkLibraryFactory factory) throws CausticException {
        final IRack rack = factory.getRack();
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(rack, index);
        if (patterns != null) {
            createPatternsFromLoadOperation(factory, patterns);
        }
    }

    private void createPatternsFromLoadOperation(CaustkLibraryFactory factory, String patterns)
            throws CausticException {
        for (String patternName : patterns.split(" ")) {
            int bankIndex = PatternUtils.toBank(patternName);
            int patternIndex = PatternUtils.toPattern(patternName);

            CaustkPhrase caustkPhrase = factory.createPhrase(this, bankIndex, patternIndex);
            phrases.put(caustkPhrase.getIndex(), caustkPhrase);
            caustkPhrase.load(factory);
        }
    }
}

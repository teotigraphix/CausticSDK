////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.part;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.IPatch;
import com.teotigraphix.caustic.part.PatchData;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.common.IMemento;

/*

Saves;
  - All mixer channel settings for the IMachine
  - 2 Effects if defined in the effects rack and their settings
  - The ITone data, (enabled, muted, selected) <- that probably doesn't make sense
  - The machine settings
    - this needs to be custom because we don't want the sequencer

<?xml version="1.0" encoding="UTF-8"?>
<patch id="A001" presetBank="SYSTEM" name="Cool Bassline">

	<mixer id="part1" eq_bass="0.0" eq_mid="0.0" eq_high="0.0" 
					  volume="0.0" 
					  delay_send="0.42" reverb_send="0.0" 
					  pan="-1.0"
					  stereo_width="0.0" 
					  mute="0" solo="0"/>
	
	<effects index="0">
		<effect type="4" index="0" depth="8" jitter="0.0" rate="0.0" wet="0.0"/>
		<effect type="5" index="1" depth="0.9" feedback="0.42" rate="0.4" wet="0.5"/>
	</effects>
	
	<tone id="part1" type="subsynth" enabled="0" index="0" muted="0" selected="0">
		<data/>
		<machine id="part1" index="0" type="subsynth">
			<sequencer/>
			<synth polyphony="4"/>
			<volume out="1.0" attack="0.0" decay="0.0" release="0.0" sustain="1.0"/>
			<filter cutoff="1.0" resonance="0.0" attack="0.0" decay="0.0" release="1.5" sustain="1.0" track="0.0" type="0"/>
			<osc1 bend="0.0" fm="0.0" mix="0.5" waveform="0"/>
			<osc2 cents="0" octave="0" phase="0.0" semis="0" waveform="0"/>
			<lfo1 rate="1" depth="0.0" target="0" waveform="0"/>
			<lfo2 rate="1" depth="0.0" target="0"/>
		</machine>
	</tone>
</patch>

*/

/*

path = /sdcard/myapp/patches/trance/lead-bass.xml - SubSynth

- IPart.loadPatch(path) or setPatch(XMLSubSynthPatch) > loadPath(XMLSubSynthPatch.getFile())


What is a Patch?

- At it's most basic state, it's a ZIP archive OR XML file that has 
  a manifest and assets.

A patch saves;

!!! remember there is no idea of "index" becasue when a patch is saved its
    just a bunch of settings that will be used to restore() a new machine

- machine id
- display name
- preset bank
- data object

- saves the effects applied to the machine

*/

/**
 * The default implementation of the {@link IPatch} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Patch implements IPatch {

    //--------------------------------------------------------------------------
    // 
    //  IRackAware API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  rack
    //----------------------------------

    private IRack mRack;

    @Override
    public IRack getRack() {
        return mRack;
    }

    @Override
    public void setRack(IRack value) {
        mRack = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  IPreset API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  id
    //----------------------------------

    private String mId;

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setId(String value) {
        mId = value;
    }

    //----------------------------------
    //  index
    //----------------------------------

    private int mIndex;

    @Override
    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int value) {
        mIndex = value;
    }

    //----------------------------------
    //  name
    //----------------------------------

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String value) {
        mName = value;
    }

    //----------------------------------
    //  bank
    //----------------------------------

    private String mBank;

    @Override
    public String getPresetBank() {
        return mBank;
    }

    @Override
    public void setPresetBank(String value) {
        mBank = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  IPatch API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  part
    //----------------------------------

    private IPart mPart;

    @Override
    public IPart getPart() {
        return mPart;
    }

    public void setPart(IPart value) {
        mPart = value;
        if (mMemento != null) {
            // this will get called in ISoundGenerator.loadPatch()
            // time to energize
            paste(mMemento);
        }
    }

    //----------------------------------
    //  data
    //----------------------------------

    private PatchData mData;

    @Override
    public PatchData getData() {
        return mData;
    }

    @Override
    public void setData(PatchData value) {
        mData = value;
    }

    //----------------------------------
    //  memento
    //----------------------------------

    private IMemento mMemento;

    @Override
    public IMemento getMemento() {
        return mMemento;
    }

    @Override
    public void setMemento(IMemento value) {
        mMemento = value;
        if (mMemento != null) {
            // if we keep the memento from the load around, it could
            // be used to easily restore the Patch back to its original
            // state.
            pasteInitial(mMemento);
            if (mPart != null) {
                paste(mMemento);
            }
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public Patch() {
    }

    //--------------------------------------------------------------------------
    // 
    //  IPatch API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public boolean revert() {
        if (mMemento != null) {
            // need to call this becasue the memento is active but we 
            // want the original values
            pasteInitial(mMemento);
            paste(mMemento);
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    // 
    //  IPersist API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {

        // - id; The unique string identifier within the PatchLibrary
        memento.putString(PartConstants.ATT_ID, getId());
        // - name; The non unique human readable name
        memento.putString(PartConstants.ATT_NAME, getId());
        // - presetBank; The bank within the PatchLibrary
        memento.putString(PartConstants.ATT_PRESET_BANK, getPresetBank());

        memento.putString("type", getPart().getTone().getType().toString());

        // - data; PatchData
        if (mData != null) {
            mData.copy(memento.createChild(PartConstants.TAG_DATA));
        }

        IMachine machine = getMachine();
        // - MixerPanel
        //   - Bass, Mid, High, Delay, Reverb, Pan, Width, Volume
        mRack.getMixerPanel().copyChannel(machine, memento.createChild(PartConstants.TAG_MIXER));
        // - EffectRack
        //   - slot1, slot2 ( effect.copy() )
        mRack.getEffectsRack().copyChannel(machine, memento.createChild(PartConstants.TAG_EFFECTS));

        // - tone; The tone's full state memento
        mPart.copyTone(memento);
    }

    IMachine getMachine() {
        return mRack.getMachine(mPart.getIndex());
    }

    void pasteInitial(IMemento memento) {
        setId(memento.getString(PartConstants.ATT_ID));
        setName(memento.getString(PartConstants.ATT_NAME));
        setPresetBank(memento.getString(PartConstants.ATT_PRESET_BANK));

        if (mData != null) {
            mData.paste(memento.getChild(PartConstants.TAG_DATA));
        }
    }

    @Override
    public void paste(IMemento memento) {
        if (mMemento == null) {
            pasteInitial(memento);
        }
        IMachine machine = getMachine();
        mRack.getMixerPanel().pasteChannel(machine, memento.getChild(PartConstants.TAG_MIXER));
        mRack.getEffectsRack().pasteChannel(machine, memento.getChild(PartConstants.TAG_EFFECTS));

        mPart.pasteTone(memento);

    }

}

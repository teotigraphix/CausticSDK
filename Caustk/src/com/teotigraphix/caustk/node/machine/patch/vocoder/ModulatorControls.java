////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine.patch.vocoder;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.teotigraphix.caustk.core.osc.VocoderMessage;
import com.teotigraphix.caustk.core.osc.VocoderMessage.CarrierOscWaveform;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;

/**
 * The {@link VocoderMachine#getControls()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ModulatorControls extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private TreeMap<Integer, Float> character = new TreeMap<Integer, Float>();

    private boolean sendNotes;

    private CarrierOscWaveform waveform;

    private float unison;

    private float sub;

    private float noise;

    private float slew;

    private float hfBypass;

    private float dry;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // character
    //----------------------------------

    /**
     * @param index Frequency index (0..7).
     * @see VocoderMessage#CCONTROL
     */
    public float getCharactorControl(int index) {
        return character.get(index);
    }

    public float queryCharactorControl(int index) {
        return VocoderMessage.QUERY_CCONTROL.send(getRack(), getMachineIndex(), index);
    }

    /**
     * Control the balance of frequencies in the resulting vocoded carrier
     * sound.
     * 
     * @param index Frequency index (0..7).
     * @param gain The gain amount, 0 being no gain adjustment (-1.0..1.0).
     * @see VocoderMessage#CCONTROL
     */
    public void setCharactorControl(int index, float gain) {
        character.put(index, gain);
        VocoderMessage.CCONTROL.send(getRack(), getMachineIndex(), index, gain);
    }

    //----------------------------------
    // sendNotes
    //----------------------------------

    /**
     * @see VocoderMessage#SND_NOTES
     */
    public boolean isSendNotes() {
        return sendNotes;
    }

    public boolean querySendNotes() {
        return VocoderMessage.SND_NOTES.query(getRack(), getMachineIndex()) == 0f ? false : true;
    }

    /**
     * @param sendNotes (true|false)
     * @see VocoderMessage#SND_NOTES
     */
    public void setSendNotes(boolean sendNotes) {
        if (sendNotes == this.sendNotes)
            return;
        this.sendNotes = sendNotes;
        VocoderMessage.SND_NOTES.send(getRack(), getMachineIndex(), sendNotes ? 1 : 0);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    /**
     * @see VocoderMessage#WAVEFORM
     */
    public CarrierOscWaveform getWaveform() {
        return waveform;
    }

    public CarrierOscWaveform queryWaveform() {
        return CarrierOscWaveform.fromInt((int)VocoderMessage.WAVEFORM.query(getRack(),
                getMachineIndex()));
    }

    /**
     * @param waveform CarrierOscWaveform
     * @see VocoderMessage#WAVEFORM
     */
    public void setWaveform(CarrierOscWaveform waveform) {
        if (waveform == this.waveform)
            return;
        this.waveform = waveform;
        VocoderMessage.WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
    }

    //----------------------------------
    // unison
    //----------------------------------

    /**
     * @see VocoderMessage#UNISON
     */
    public float getUnison() {
        return unison;
    }

    public float queryUnison() {
        return VocoderMessage.UNISON.query(getRack(), getMachineIndex());
    }

    /**
     * @param unison (0.0..1.0)
     * @see VocoderMessage#UNISON
     */
    public void setUnison(float unison) {
        if (unison == this.unison)
            return;
        if (unison < 0f || unison > 1f)
            throw newRangeException(VocoderMessage.UNISON, "0.0..1.0", unison);
        this.unison = unison;
        VocoderMessage.UNISON.send(getRack(), getMachineIndex(), unison);
    }

    //----------------------------------
    // sub
    //----------------------------------

    /**
     * @see VocoderMessage#SUB
     */
    public float getSub() {
        return sub;
    }

    public float querySub() {
        return VocoderMessage.SUB.query(getRack(), getMachineIndex());
    }

    /**
     * @param sub (0.0..1.0)
     * @see VocoderMessage#SUB
     */
    public void setSub(float sub) {
        if (sub == this.sub)
            return;
        if (sub < 0f || sub > 1f)
            throw newRangeException(VocoderMessage.SUB, "0.0..1.0", sub);
        this.sub = sub;
        VocoderMessage.SUB.send(getRack(), getMachineIndex(), sub);
    }

    //----------------------------------
    // noise
    //----------------------------------

    /**
     * @see VocoderMessage#NOISE
     */
    public float getNoise() {
        return noise;
    }

    public float queryNosie() {
        return VocoderMessage.NOISE.query(getRack(), getMachineIndex());
    }

    /**
     * @param noise (0.0..1.0)
     * @see VocoderMessage#NOISE
     */
    public void setNoise(float noise) {
        if (noise == this.noise)
            return;
        if (noise < 0f || noise > 1f)
            throw newRangeException(VocoderMessage.NOISE, "0.0..1.0", noise);
        this.noise = noise;
        VocoderMessage.NOISE.send(getRack(), getMachineIndex(), noise);
    }

    //----------------------------------
    // slew
    //----------------------------------

    /**
     * @see VocoderMessage#SLEW
     */
    public float getSlew() {
        return slew;
    }

    public float querySlew() {
        return VocoderMessage.SLEW.query(getRack(), getMachineIndex());
    }

    /**
     * @param slew (0.0..0.4)
     * @see VocoderMessage#SLEW
     */
    public void setSlew(float slew) {
        if (slew == this.slew)
            return;
        if (slew < 0f || slew > 0.4f)
            throw newRangeException(VocoderMessage.SLEW, "0.0..0.4", slew);
        this.slew = slew;
        VocoderMessage.SLEW.send(getRack(), getMachineIndex(), slew);
    }

    //----------------------------------
    // hfBypass
    //----------------------------------

    /**
     * @see VocoderMessage#HF_BYPASS
     */
    public float getHFBypass() {
        return hfBypass;
    }

    public float queryHFBypass() {
        return VocoderMessage.HF_BYPASS.query(getRack(), getMachineIndex());
    }

    /**
     * @param hfBypass (0.0..1.0)
     * @see VocoderMessage#HF_BYPASS
     */
    public void setHFBypass(float hfBypass) {
        if (hfBypass == this.hfBypass)
            return;
        if (hfBypass < 0f || hfBypass > 1f)
            throw newRangeException(VocoderMessage.HF_BYPASS, "0.0..1.0", hfBypass);
        this.hfBypass = hfBypass;
        VocoderMessage.HF_BYPASS.send(getRack(), getMachineIndex(), hfBypass);
    }

    //----------------------------------
    // dry
    //----------------------------------

    /**
     * @see VocoderMessage#DRY
     */
    public float getDry() {
        return dry;
    }

    public float queryDry() {
        return VocoderMessage.DRY.query(getRack(), getMachineIndex());
    }

    /**
     * @param dry (0.0..1.0)
     * @see VocoderMessage#DRY
     */
    public void setDry(float dry) {
        if (dry == this.dry)
            return;
        if (dry < 0f || dry > 1f)
            throw newRangeException(VocoderMessage.DRY, "0.0..1.0", dry);
        this.dry = dry;
        VocoderMessage.DRY.send(getRack(), getMachineIndex(), dry);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ModulatorControls() {
    }

    public ModulatorControls(int machineIndex) {
        super(machineIndex);
    }

    public ModulatorControls(MachineNode machineNode) {
        super(machineNode);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        for (Entry<Integer, Float> entry : character.entrySet()) {
            VocoderMessage.CCONTROL.send(getRack(), getMachineIndex(), entry.getKey(),
                    entry.getValue());
        }
        VocoderMessage.DRY.send(getRack(), getMachineIndex(), dry);
        VocoderMessage.HF_BYPASS.send(getRack(), getMachineIndex(), hfBypass);
        VocoderMessage.NOISE.send(getRack(), getMachineIndex(), noise);
        VocoderMessage.SND_NOTES.send(getRack(), getMachineIndex(), sendNotes ? 1 : 0);
        VocoderMessage.SLEW.send(getRack(), getMachineIndex(), slew);
        VocoderMessage.SUB.send(getRack(), getMachineIndex(), sub);
        VocoderMessage.UNISON.send(getRack(), getMachineIndex(), unison);
        VocoderMessage.WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
    }

    @Override
    protected void restoreComponents() {
        for (int i = 0; i < 8; i++) {
            setCharactorControl(i, queryCharactorControl(i));
        }
        setDry(queryDry());
        setHFBypass(queryHFBypass());
        setNoise(queryNosie());
        setSendNotes(querySendNotes());
        setSlew(querySlew());
        setSub(querySub());
        setUnison(queryUnison());
        setWaveform(queryWaveform());
    }
}

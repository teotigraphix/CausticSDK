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

import java.io.File;

import com.teotigraphix.caustk.core.osc.VocoderMessage;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;

/**
 * The {@link VocoderMachine#getModulator()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class VocoderModulator extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private int previewIndex;

    private int carrierSource;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // previewIndex
    //----------------------------------

    /**
     * @see VocoderMessage#PREVIEW_MODULATOR
     */
    public int getPreviewIndex() {
        return previewIndex;
    }

    public int queryPreviewIndex() {
        return (int)VocoderMessage.PREVIEW_MODULATOR.query(getRack(), getMachineIndex());
    }

    /**
     * @param previewIndex (0..6)
     * @see VocoderMessage#PREVIEW_MODULATOR
     */
    public void setPreviewIndex(int previewIndex) {
        if (previewIndex == this.previewIndex)
            return;
        if (previewIndex < 0 || previewIndex > 6)
            throw newRangeException(VocoderMessage.PREVIEW_MODULATOR, "0..6", previewIndex);
        this.previewIndex = previewIndex;
        VocoderMessage.PREVIEW_MODULATOR.send(getRack(), getMachineIndex(), previewIndex);
    }

    /**
     * Clear modulator : will clear all modulator information for the selected
     * modulator.
     * 
     * @param index (0,1,2,3,4,5)
     */
    public void clearModulator(int index) {
        VocoderMessage.CLEAR_MODULATOR.send(getRack(), getMachineIndex(), index);
    }

    /**
     * Select between 6 different modulation sources. The selected modulator
     * will be the one to play when notes on the preview keyboard are played.
     * 
     * @param index
     * @param machineIndex
     */
    public void setModulator(int index, int machineIndex) {
        VocoderMessage.MODULATOR.send(getRack(), getMachineIndex(), index, machineIndex);
    }

    public void setModulator(int index, File wavFile) {
        VocoderMessage.MODULATOR.send(getRack(), getMachineIndex(), index,
                wavFile.getAbsolutePath());
    }

    /**
     * A return value of -1 indicates it's using a WAV file and the sample name
     * will be copied to the response. If it's a machine, the return value
     * indicates the slot# and the response is not touched.
     */
    public String queryModulator() {
        return VocoderMessage.MODULATOR.queryString(getRack(), getMachineIndex());
    }

    /**
     * Carrier selector: Select between the internal oscillator or any other
     * machine in your rack as the carrier signal to be vocoded.
     * 
     * @param carrierSource (-1,0..13) -1 uses internal, 0..13 for targeting
     *            machine slots. It's an error to reference itself or an empty
     *            slot.
     */
    public void setCarrierSource(int carrierSource) {
        if (carrierSource == this.carrierSource)
            return;
        VocoderMessage.CARRIER_SOURCE.send(getRack(), getMachineIndex(), carrierSource);
    }

    public int queryCarrierSource(int carrierSource) {
        return (int)VocoderMessage.CARRIER_SOURCE.send(getRack(), getMachineIndex(), carrierSource);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public VocoderModulator() {
    }

    public VocoderModulator(int machineIndex) {
        super(machineIndex);
    }

    public VocoderModulator(MachineNode machineNode) {
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
    }

    @Override
    protected void restoreComponents() {
        for (int i = 0; i < 6; i++) {
            setCarrierSource(queryCarrierSource(i));
        }
        setPreviewIndex(queryPreviewIndex());
    }
}

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

package com.teotigraphix.caustk.node.machine.patch.organ;

import com.teotigraphix.caustk.core.osc.OrganMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.OrganMachine;

/**
 * The {@link OrganMachine} leslie component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LeslieComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private int[] drawbar;

    private float percTone;

    private float percDecay;

    private float leslieRate;

    private float leslieDepth;

    private float drive;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // drawbar
    //----------------------------------

    /**
     * @param index The drawbar index (0..7).
     * @see OrganMessage#DRAWBAR
     */
    public float getDrawBar(int index) {
        return drawbar[index];
    }

    public float queryDrawBar(int index) {
        return OrganMessage.DRAWBAR.query(getRack(), getMachineIndex(), index);
    }

    /**
     * Sets a drawbar amplitude at the specified index.
     * 
     * @param index The drawbar index (0..7).
     * @param amplitude (0.0..1.0)
     * @see OrganMessage#DRAWBAR
     */
    public void setDrawBar(int index, float amplitude) {
        if (amplitude == drawbar[index])
            return;
        if (amplitude < 0 || amplitude > 7)
            throw newRangeException("drawbar_index", "0..7", index);
        if (amplitude < 0f || amplitude > 1.0f)
            throw newRangeException(OrganMessage.DRAWBAR, "0..1.0", amplitude);
        OrganMessage.DRAWBAR.send(getRack(), getMachineIndex(), index, amplitude);
    }

    //----------------------------------
    // percTone
    //----------------------------------

    /**
     * @see OrganMessage#PERC_TONE
     */
    public float getPercTone() {
        return percTone;
    }

    public float queryPercTone() {
        return OrganMessage.PERC_TONE.query(getRack(), getMachineIndex());
    }

    /**
     * @param percTone (0.0..1.0)
     * @see OrganMessage#PERC_TONE
     */
    public void setPercTone(float percTone) {
        if (percTone == this.percTone)
            return;
        if (percTone < 0f || percTone > 1.0f)
            throw newRangeException(OrganMessage.PERC_TONE, "0..1.0", percTone);
        this.percTone = percTone;
        OrganMessage.PERC_TONE.send(getRack(), getMachineIndex(), percTone);
    }

    //----------------------------------
    // percDecay
    //----------------------------------

    /**
     * @see OrganMessage#PERC_DECAY
     */
    public float getPercDecay() {
        return percDecay;
    }

    public float queryPercDecay() {
        return OrganMessage.PERC_DECAY.query(getRack(), getMachineIndex());
    }

    /**
     * @param percDecay (0.0..0.5)
     * @see OrganMessage#PERC_DECAY
     */
    public void setPercDecay(float percDecay) {
        if (percDecay == this.percDecay)
            return;
        if (percDecay < 0f || percDecay > 0.5f)
            throw newRangeException(OrganMessage.PERC_DECAY, "0..0.5", percDecay);
        this.percDecay = percDecay;
        OrganMessage.PERC_DECAY.send(getRack(), getMachineIndex(), percDecay);
    }

    //----------------------------------
    // leslieRate
    //----------------------------------

    /**
     * @see OrganMessage#LESLIE_RATE
     */
    public float getLeslieRate() {
        return leslieRate;
    }

    public float queryLeslieRate() {
        return OrganMessage.LESLIE_RATE.query(getRack(), getMachineIndex());
    }

    /**
     * @param leslieRate (0.0..0.7)
     * @see OrganMessage#LESLIE_RATE
     */
    public void setLeslieRate(float leslieRate) {
        if (leslieRate == this.leslieRate)
            return;
        if (leslieRate < 0f || leslieRate > 0.7f)
            throw newRangeException(OrganMessage.LESLIE_RATE, "0..0.7", leslieRate);
        this.leslieRate = leslieRate;
        OrganMessage.LESLIE_RATE.send(getRack(), getMachineIndex(), leslieRate);
    }

    //----------------------------------
    // leslieDepth
    //----------------------------------

    /**
     * @see OrganMessage#LESLIE_DEPTH
     */
    public float getLeslieDepth() {
        return leslieDepth;
    }

    public float queryLeslieDepth() {
        return OrganMessage.LESLIE_DEPTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param leslieDepth (0.0..1.0)
     * @see OrganMessage#LESLIE_DEPTH
     */
    public void setLeslieDepth(float leslieDepth) {
        if (leslieDepth == this.leslieDepth)
            return;
        if (leslieDepth < 0f || leslieDepth > 1.0f)
            throw newRangeException("leslie_depth", "0..1.0", leslieDepth);
        this.leslieDepth = leslieDepth;
        OrganMessage.LESLIE_DEPTH.send(getRack(), getMachineIndex(), leslieDepth);
    }

    //----------------------------------
    // drive
    //----------------------------------

    /**
     * @see OrganMessage#DRIVE
     */
    public float getDrive() {
        return drive;
    }

    public float queryDrive() {
        return OrganMessage.DRIVE.query(getRack(), getMachineIndex());
    }

    /**
     * @param drive (0.0..0.4)
     * @see OrganMessage#DRIVE
     */
    public void setDrive(float drive) {
        if (drive == this.drive)
            return;
        if (drive < 0f || drive > 0.4f)
            throw newRangeException("drive", "0..0.4", drive);
        this.drive = drive;
        OrganMessage.DRIVE.send(getRack(), getMachineIndex(), drive);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LeslieComponent() {
    }

    public LeslieComponent(MachineNode machineNode) {
        super(machineNode);
        drawbar = new int[8];
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
        for (int i = 0; i < 8; i++) {
            OrganMessage.DRAWBAR.send(getRack(), getMachineIndex(), i, drawbar[i]);
        }
        OrganMessage.PERC_TONE.send(getRack(), getMachineIndex(), percTone);
        OrganMessage.PERC_DECAY.send(getRack(), getMachineIndex(), percDecay);
        OrganMessage.LESLIE_RATE.send(getRack(), getMachineIndex(), leslieRate);
        OrganMessage.LESLIE_DEPTH.send(getRack(), getMachineIndex(), leslieDepth);
        OrganMessage.DRIVE.send(getRack(), getMachineIndex(), drive);
    }

    @Override
    protected void restoreComponents() {
        for (int i = 0; i < 8; i++) {
            setDrawBar(i, queryDrawBar(i));
        }
        setDrive(queryDrive());
        setLeslieDepth(queryLeslieDepth());
        setLeslieRate(queryLeslieRate());
        setPercDecay(queryPercDecay());
        setPercTone(queryPercTone());
    }
}

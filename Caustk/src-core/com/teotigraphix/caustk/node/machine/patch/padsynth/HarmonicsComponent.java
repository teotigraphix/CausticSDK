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

package com.teotigraphix.caustk.node.machine.patch.padsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PadSynthMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;

/**
 * The {@link PadSynthMachine} harmonics component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see PadSynthMachine#getHarmonics()
 */
public class HarmonicsComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float[][] table;

    @Tag(101)
    private float[] width;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // harmonics
    //----------------------------------

    /**
     * Returns a harmonic value for the table at the specified index (0,1).
     * 
     * @param tableIndex The harmonic table index (0,1).
     * @param index The harmonic index (0..23).
     */
    public float getHarmonic(int tableIndex, int index) {
        return table[tableIndex][index];
    }

    /**
     * Queries the native machine for the harmonic at the specified index (0,1).
     * 
     * @param tableIndex The harmonic table index (0,1).
     * @param index The harmonic index (0..23).
     * @see PadSynthMessage#QUERY_HARMONICS
     */
    public float queryHarmonic(int tableIndex, int index) {
        return PadSynthMessage.QUERY_HARMONICS
                .send(getRack(), getMachineIndex(), tableIndex, index);
    }

    /**
     * Sets a harmonic at the specified index (0,1).
     * 
     * @param tableIndex The harmonic table index (0,1).
     * @param index The harmonic index (0..23).
     * @param amplitude The harmonic amplitude (0.0..1.0).
     * @see PadSynthMessage#HARMONICS
     */
    public void setHarmonic(int tableIndex, int index, float amplitude) {
        if (getHarmonic(tableIndex, index) == amplitude)
            return;
        if (amplitude < 0f || amplitude > 1f)
            throw newRangeException(PadSynthMessage.HARMONICS, "0..1", amplitude);
        table[tableIndex][index] = amplitude;
        PadSynthMessage.HARMONICS.send(getRack(), getMachineIndex(), tableIndex, index, amplitude);
    }

    //----------------------------------
    // width
    //----------------------------------

    /**
     * Returns the width for the specific harmonic table index.
     * 
     * @param tableIndex The harmonic table index (0,1).
     */
    public float getWidth(int tableIndex) {
        return width[tableIndex];
    }

    /**
     * Queries the native machine for the width at the specified table index.
     * 
     * @param tableIndex The harmonic table index (0,1).
     * @see PadSynthMessage#QUERY_WIDTH
     */
    public float queryWidth(int tableIndex) {
        return PadSynthMessage.QUERY_WIDTH.send(getRack(), getMachineIndex(), tableIndex);
    }

    /**
     * Sets the width for the specific harmonic table index.
     * 
     * @param tableIndex The harmonic table index (0,1).
     * @param value The width value (0.0..1.0)
     */
    public void setWidth(int tableIndex, float value) {
        if (getWidth(tableIndex) == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("width", "0..1", value);
        width[tableIndex] = value;
        PadSynthMessage.WIDTH.send(getRack(), getMachineIndex(), tableIndex, value);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public HarmonicsComponent() {
    }

    public HarmonicsComponent(MachineNode machineNode) {
        super(machineNode);
        createTables();
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
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 24; j++) {
                float harmonic = table[i][j];
                setHarmonic(i, j, harmonic);
            }
        }
        setWidth(0, width[0]);
        setWidth(1, width[1]);
    }

    @Override
    protected void restoreComponents() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 24; j++) {
                float harmonic = queryHarmonic(i, j);
                table[i][j] = harmonic;
            }
        }
        setWidth(0, queryWidth(0));
        setWidth(1, queryWidth(1));
    }

    //--------------------------------------------------------------------------
    // Private:: Methods
    //--------------------------------------------------------------------------

    private void createTables() {
        table = new float[2][24];
        table[0] = new float[24];
        table[1] = new float[24];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 24; j++) {
                table[i][j] = 0f;
            }
        }
        width = new float[2];
        width[0] = 0.5f;
        width[1] = 0.5f;
    }
}

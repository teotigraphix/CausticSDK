
package com.teotigraphix.caustk.core.components.padsynth;

import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.osc.PadSynthMessage;

public class HarmonicsComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    // Public API
    //--------------------------------------------------------------------------

    //----------------------------------
    // harmonics
    //----------------------------------

    private float[][] table;

    private void createTables() {
        table[0] = new float[24];
        table[1] = new float[24];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 24; j++) {
                table[i][j] = 0f;
            }
        }
    }

    public float getHarmonic(int tableIndex, int index) {
        return table[tableIndex][index];
    }

    float getHarmonic(int tableIndex, int index, boolean restore) {
        return PadSynthMessage.HARMONICS.query(getEngine(), getToneIndex(), tableIndex, index);
    }

    public void setHarmonic(int tableIndex, int index, float value) {
        if (getHarmonic(tableIndex, index) == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("harmonics", "0..1", value);
        table[tableIndex][index] = value;
        PadSynthMessage.HARMONICS.send(getEngine(), getToneIndex(), tableIndex, index, value);
    }

    //----------------------------------
    // width
    //----------------------------------

    private float[] width;

    public float getWidth(int tableIndex) {
        return width[tableIndex];
    }

    float getWidth(int tableIndex, boolean restore) {
        return PadSynthMessage.WIDTH.query(getEngine(), getToneIndex(), tableIndex);
    }

    public void setWidth(int tableIndex, float value) {
        if (getWidth(tableIndex) == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("width", "0..1", value);
        width[tableIndex] = value;
        PadSynthMessage.WIDTH.send(getEngine(), getToneIndex(), tableIndex, value);
    }

    public HarmonicsComponent() {
        createTables();
        width = new float[2];
    }

    @Override
    public void restore() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 24; j++) {
                table[i][j] = getHarmonic(i, j, true);
            }
        }
    }

}

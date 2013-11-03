
package com.teotigraphix.caustk.rack.tone.components.padsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PadSynthMessage;
import com.teotigraphix.caustk.rack.tone.RackToneComponent;

public class HarmonicsComponent extends RackToneComponent {

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

    private void createTables() {
        table = new float[2][24];
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
        return PadSynthMessage.QUERY_HARMONICS.send(getEngine(), getToneIndex(), tableIndex, index);
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

    public float getWidth(int tableIndex) {
        return width[tableIndex];
    }

    float getWidth(int tableIndex, boolean restore) {
        return PadSynthMessage.QUERY_WIDTH.send(getEngine(), getToneIndex(), tableIndex);
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
                float harmonic = getHarmonic(i, j, true);
                table[i][j] = harmonic;
            }
        }
        getWidth(0, true);
        getWidth(1, true);
    }

}

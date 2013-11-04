
package com.teotigraphix.caustk.rack.tone.components.organ;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.OrganMessage;
import com.teotigraphix.caustk.rack.tone.RackToneComponent;

public class LeslieComponent extends RackToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    protected int drawbar;

    @Tag(101)
    protected float percTone;

    @Tag(102)
    protected float percDecay;

    @Tag(103)
    protected float leslieRate;

    @Tag(104)
    protected float leslieDepth;

    @Tag(105)
    protected float drive;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // drawbar
    //----------------------------------

    public float getDrawBar(int index) {
        return drawbar;
    }

    float getDrawBar(int index, boolean restore) {
        return OrganMessage.DRAWBAR.query(getEngine(), getToneIndex(), index);
    }

    public void setDrawBar(int index, float value) {
        if (value < 0f || value > 1.0f)
            throw newRangeException("drawbar", "0..1.0", value);
        OrganMessage.DRAWBAR.send(getEngine(), getToneIndex(), index, value);
    }

    //----------------------------------
    // percTone
    //----------------------------------

    public float getPercTone() {
        return percTone;
    }

    float getPercTone(boolean restore) {
        return OrganMessage.PERC_TONE.query(getEngine(), getToneIndex());
    }

    public void setPercTone(float value) {
        if (value == percTone)
            return;
        if (value < 0f || value > 1.0f)
            throw newRangeException("perc_tone", "0..1.0", value);
        percTone = value;
        OrganMessage.PERC_TONE.send(getEngine(), getToneIndex(), percTone);
    }

    //----------------------------------
    // percDecay
    //----------------------------------

    public float getPercDecay() {
        return percDecay;
    }

    float getPercDecay(boolean restore) {
        return OrganMessage.PERC_DECAY.query(getEngine(), getToneIndex());
    }

    public void setPercDecay(float value) {
        if (value == percDecay)
            return;
        if (value < 0f || value > 0.5f)
            throw newRangeException("perc_decay", "0..0.5", value);
        percDecay = value;
        OrganMessage.PERC_DECAY.send(getEngine(), getToneIndex(), percDecay);
    }

    //----------------------------------
    // leslieRate
    //----------------------------------

    public float getLeslieRate() {
        return leslieRate;
    }

    float getLeslieRate(boolean restore) {
        return OrganMessage.LESLIE_RATE.query(getEngine(), getToneIndex());
    }

    public void setLeslieRate(float value) {
        if (value == leslieRate)
            return;
        if (value < 0f || value > 0.7f)
            throw newRangeException("leslie_rate", "0..0.7", value);
        leslieRate = value;
        OrganMessage.LESLIE_RATE.send(getEngine(), getToneIndex(), leslieRate);
    }

    //----------------------------------
    // leslieDepth
    //----------------------------------

    public float getLeslieDepth() {
        return leslieDepth;
    }

    float getLeslieDepth(boolean restore) {
        return OrganMessage.LESLIE_DEPTH.query(getEngine(), getToneIndex());
    }

    public void setLeslieDepth(float value) {
        if (value == leslieDepth)
            return;
        if (value < 0f || value > 1.0f)
            throw newRangeException("leslie_depth", "0..1.0", value);
        leslieDepth = value;
        OrganMessage.LESLIE_DEPTH.send(getEngine(), getToneIndex(), leslieDepth);
    }

    //----------------------------------
    // drive
    //----------------------------------

    public float getDrive() {
        return drive;
    }

    float getDrive(boolean restore) {
        return OrganMessage.DRIVE.query(getEngine(), getToneIndex());
    }

    public void setDrive(float value) {
        if (value == drive)
            return;
        if (value < 0f || value > 1.0f)
            throw newRangeException("drive", "0..1.0", value);
        drive = value;
        OrganMessage.DRIVE.send(getEngine(), getToneIndex(), drive);
    }

    public LeslieComponent() {
    }

    @Override
    protected void restoreComponents() {
        setDrive(getDrive(true));
        setLeslieDepth(getLeslieDepth(true));
        setLeslieRate(getLeslieRate(true));
        setPercDecay(getPercDecay(true));
        setPercTone(getPercTone(true));
    }

}

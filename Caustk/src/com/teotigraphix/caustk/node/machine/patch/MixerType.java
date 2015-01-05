
package com.teotigraphix.caustk.node.machine.patch;

public enum MixerType {

    Volume(0), Pan(1), Bass(2), Mid(3), High(4), Reverb(5), Delay(6), Width(7);

    private int index;

    public int getIndex() {
        return index;
    }

    MixerType(int index) {
        this.index = index;
    }

    public static MixerType fromInt(int index) {
        for (MixerType type : values()) {
            if (type.getIndex() == index)
                return type;
        }
        return null;
    }
}

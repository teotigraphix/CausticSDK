
package com.teotigraphix.caustk.sound.effect;

public enum EffectType {

    /**
     * Adds distortion to the machine's signal.
     */
    DISTORTION(2),

    /**
     * Compresses the machine's signal.
     */
    COMPRESSOR(3),

    BITCRUSHER(4),

    FLANGER(5),

    PHASER(6),

    CHORUS(7),

    AUTOWAH(8),

    PARAMETRICEQ(9);

    private int value;

    EffectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EffectType fromInt(Integer type) {
        for (EffectType result : values()) {
            if (result.getValue() == type)
                return result;
        }
        return null;
    }

}

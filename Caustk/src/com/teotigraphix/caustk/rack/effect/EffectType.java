
package com.teotigraphix.caustk.rack.effect;

/**
 * @author Michael Schmalle
 */
public enum EffectType {

    /**
     * Adds distortion to the machine's signal.
     */
    Distortion(2),

    /**
     * Compresses the machine's signal.
     */
    Compressor(3),

    Bitcrusher(4),

    Flanger(5),

    Phaser(6),

    Chorus(7),

    Autowah(8),

    ParametricEQ(9);

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

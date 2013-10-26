
package com.teotigraphix.caustk.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.effect.EffectUtils;

public class CaustkEffect {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private int index = -1;

    @Tag(1)
    private EffectType effectType;

    @Tag(2)
    private CaustkPatch patch;

    @Tag(3)
    private IEffect effect;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * The slot this effect is held in the {@link LivePatch}, -1 if not assigned
     * to a {@link LivePatch}.
     */
    public int getIndex() {
        return index;
    }

    void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // effectType
    //----------------------------------

    public EffectType getEffectType() {
        return effectType;
    }

    //----------------------------------
    // effect
    //----------------------------------

    public IEffect getEffect() {
        return effect;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkEffect() {
    }

    public CaustkEffect(int index, EffectType effectType) {
        this.index = index;
        this.effectType = effectType;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void restore(CaustkPatch caustkPatch) {
        effect = EffectUtils.create(effectType, index, caustkPatch.getMachine().getIndex());
    }
}

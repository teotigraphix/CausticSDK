
package com.teotigraphix.caustk.groove.importer;

import com.teotigraphix.caustk.node.effect.EffectType;

public class CausticEffectType {

    private int index;

    private EffectType type;

    public int getIndex() {
        return index;
    }

    public EffectType getType() {
        return type;
    }

    public boolean hasType() {
        return type != null;
    }

    public CausticEffectType(int index, EffectType type) {
        this.index = index;
        this.type = type;
    }
}

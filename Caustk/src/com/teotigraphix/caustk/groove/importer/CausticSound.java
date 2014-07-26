
package com.teotigraphix.caustk.groove.importer;

public class CausticSound extends CausticItem {

    private String name;

    private String displayName;

    private int index;

    private CausticEffect effect;

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getIndex() {
        return index;
    }

    public CausticEffect getEffect() {
        return effect;
    }

    public CausticSound(int index, String displayName, String effectName) {
        this.index = index;
        this.displayName = displayName;
        effect = new CausticEffect(index, effectName);
    }
}

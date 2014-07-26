
package com.teotigraphix.caustk.groove.manifest;

import com.teotigraphix.caustk.groove.LibraryBank;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectType;

public class LibraryEffectManifest extends LibraryItemManifest {

    private EffectType type0;

    private EffectType type1;

    public EffectType getType0() {
        return type0;
    }

    public EffectType getType1() {
        return type1;
    }

    public LibraryEffectManifest(String name, EffectNode efffect0, EffectNode efffect1) {
        super(name);
        if (efffect0 != null)
            this.type0 = efffect0.getType();
        if (efffect1 != null)
            this.type1 = efffect1.getType();
    }

    public LibraryEffectManifest(String name, LibraryBank libraryBank) {
        super(name, libraryBank);
    }

}

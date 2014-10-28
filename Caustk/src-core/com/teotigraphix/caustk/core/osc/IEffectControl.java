
package com.teotigraphix.caustk.core.osc;

public interface IEffectControl extends IOSCControl {
    String getControl();

    EffectControlKind getKind();
}

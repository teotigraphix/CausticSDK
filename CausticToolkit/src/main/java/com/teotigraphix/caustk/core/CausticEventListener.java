
package com.teotigraphix.caustk.core;

public interface CausticEventListener {
    public abstract void OnBeatChanged(int nNewBeat);

    public abstract void OnMeasureChanged(int nNewMeasure);
}

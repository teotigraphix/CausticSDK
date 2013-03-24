package com.singlecellsoftware.causticcore;

public interface CausticEventListener
{
    public abstract void OnBeatChanged(int nNewBeat);

    public abstract void OnMeasureChanged(int nNewMeasure);
}

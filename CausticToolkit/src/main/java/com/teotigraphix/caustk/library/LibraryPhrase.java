
package com.teotigraphix.caustk.library;

import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;

public class LibraryPhrase extends LibraryItem {

    //----------------------------------
    //  bankIndex
    //----------------------------------

    private int bankIndex;

    public final int getBankIndex() {
        return bankIndex;
    }

    public final void setBankIndex(int value) {
        bankIndex = value;
    }

    //----------------------------------
    //  patternIndex
    //----------------------------------

    private int patternIndex;

    public final int getPatternIndex() {
        return patternIndex;
    }

    public final void setPatternIndex(int value) {
        patternIndex = value;
    }

    private Resolution resolution;

    public final Resolution getResolution() {
        return resolution;
    }

    public final void setResolution(Resolution value) {
        resolution = value;
    }

    //----------------------------------
    //  dispatcher
    //----------------------------------

    private int length;

    public final int getLength() {
        return length;
    }

    public final void setLength(int value) {
        length = value;
    }

    //----------------------------------
    //  dispatcher
    //----------------------------------

    private String swing;

    public final String getSwing() {
        return swing;
    }

    public final void setSwing(String value) {
        swing = value;
    }

    //----------------------------------
    //  dispatcher
    //----------------------------------

    private String noteData;

    public final String getNoteData() {
        return noteData;
    }

    public final void setNoteData(String value) {
        noteData = value;
    }

    //----------------------------------
    //  dispatcher
    //----------------------------------

    private MachineType machineType;

    public final MachineType getMachineType() {
        return machineType;
    }

    public final void setMachineType(MachineType value) {
        machineType = value;
    }

    //----------------------------------
    //  tempo
    //----------------------------------

    private float tempo;

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float value) {
        tempo = value;
    }

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    public LibraryPhrase() {
    }

    @Override
    public String toString() {
        return getMetadataInfo().getName() + ":" + getMetadataInfo().getTags().toString();
    }
}

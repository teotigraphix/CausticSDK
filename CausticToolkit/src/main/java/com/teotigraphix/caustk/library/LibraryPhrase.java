package com.teotigraphix.caustk.library;

import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;

public class LibraryPhrase extends LibraryItem {

    private Resolution resolution;
    
    private int length;
    
    private String swing;
    
    private String noteData;
    
    private MachineType machineType;
    
    private float tempo;
    
    public final Resolution getResolution() {
        return resolution;
    }

    public final void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public final int getLength() {
        return length;
    }

    public final void setLength(int length) {
        this.length = length;
    }

    public final String getSwing() {
        return swing;
    }

    public final void setSwing(String swing) {
        this.swing = swing;
    }

    public final String getNoteData() {
        return noteData;
    }

    public final void setNoteData(String noteData) {
        this.noteData = noteData;
    }

    public final MachineType getMachineType() {
        return machineType;
    }

    public final void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    public LibraryPhrase() {
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

}

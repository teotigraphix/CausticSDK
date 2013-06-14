
package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.StringReader;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.core.XMLMemento;
import com.teotigraphix.caustic.machine.MachineType;

/**
 * presetFile -
 * <code>/root/libraries/myLibrary/patches/d34sd9..sd23.bassline</code>
 */
public class LibraryPatch extends LibraryItem {

    //----------------------------------
    // machineType
    //----------------------------------

    private MachineType machineType;

    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    //----------------------------------
    // data
    //----------------------------------

    private String data;

    /**
     * Returns the String {@link IMemento} data that was saved from the current
     * machine.
     */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    //----------------------------------
    // presetFile
    //----------------------------------

    public File getPresetFile() {
        return new File(getId().toString());
    }

    //----------------------------------
    // memento
    //----------------------------------

    /**
     * Returns the {@link IMemento} using the {@link #getData()} settings.
     */
    public IMemento getMemento() {
        return XMLMemento.createReadRoot(new StringReader(getData()));
    }

    public LibraryPatch() {
    }

}

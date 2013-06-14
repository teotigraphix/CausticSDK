
package com.teotigraphix.caustk.library.vo;

import java.io.StringReader;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.core.XMLMemento;

public abstract class MementoInfo {
    
    /**
     * Returns the String {@link IMemento} data that was saved from the current
     * rack scene.
     */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data;

    /**
     * Returns the {@link IMemento} using the {@link #getData()} settings.
     */
    public IMemento getMemento() {
        return XMLMemento.createReadRoot(new StringReader(getData()));
    }

    public MementoInfo() {
    }

}

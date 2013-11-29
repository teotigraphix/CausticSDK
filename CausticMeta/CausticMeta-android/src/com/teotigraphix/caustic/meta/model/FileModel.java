
package com.teotigraphix.caustic.meta.model;

import java.io.File;
import java.io.FileNotFoundException;

import com.teotigraphix.caustk.core.CausticFile;

public class FileModel {

    CausticFile causticFile;

    private OnFileModelChangeListener listener;

    public CausticFile getCausticFile() {
        return causticFile;
    }

    public void setFile(File value) throws FileNotFoundException {
        causticFile = new CausticFile(value);
        listener.onFileChange(causticFile);
    }

    public FileModel() {
    }

    public void setOnFileModelChangeListener(OnFileModelChangeListener l) {
        listener = l;
    }

    public static interface OnFileModelChangeListener {
        void onFileChange(CausticFile causticFile);
    }

}

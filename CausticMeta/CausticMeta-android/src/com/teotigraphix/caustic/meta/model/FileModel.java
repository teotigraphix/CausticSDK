
package com.teotigraphix.caustic.meta.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticFile;
import com.teotigraphix.caustk.core.CaustkActivity;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class FileModel {

    CausticFile causticFile;

    private OnFileModelChangeListener listener;

    private CaustkActivity activity;

    public CausticFile getCausticFile() {
        return causticFile;
    }

    public void setFile(File value) throws FileNotFoundException {
        causticFile = new CausticFile(value);
        listener.onFileChange(causticFile);
    }

    public FileModel(CaustkActivity activity) {
        this.activity = activity;
    }

    public void setOnFileModelChangeListener(OnFileModelChangeListener l) {
        listener = l;
    }

    public static interface OnFileModelChangeListener {
        void onFileChange(CausticFile causticFile);

        void onReset();
    }

    public void reset() {
        causticFile = null;
        listener.onReset();
    }

    public File saveSong(String name) {
        RackMessage.SAVE_SONG.send(activity.getGenerator(), name);
        return RuntimeUtils.getCausticSongFile(name);
    }

    public File saveSongAs(File file) throws IOException {
        File song = saveSong(file.getName().replace(".caustic", ""));
        FileUtils.copyFileToDirectory(song, file.getParentFile());
        song.delete();
        return file;
    }
}

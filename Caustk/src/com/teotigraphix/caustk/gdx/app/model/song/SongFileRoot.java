
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.google.common.io.Files;

public class SongFileRoot {

    @Tag(0)
    private File file;

    @Tag(1)
    private Collection<SongFile> files = new ArrayList<SongFile>();

    @Tag(2)
    private boolean checked;

    public File getFile() {
        return file;
    }

    public Collection<SongFile> getFiles() {
        return files;
    }

    /**
     * Whether the item is checked in the UI.
     */
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return Files.getNameWithoutExtension(file.getAbsolutePath());
    }

    SongFileRoot() {
    }

    public SongFileRoot(File file) {
        this.file = file;
    }

}

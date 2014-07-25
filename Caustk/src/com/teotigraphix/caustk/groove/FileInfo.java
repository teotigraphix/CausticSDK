
package com.teotigraphix.caustk.groove;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class FileInfo {

    @Tag(0)
    private File file;

    public File getFile() {
        return file;
    }

    public LibraryItemFormat getFormat() {
        return LibraryItemFormat.fromString(FilenameUtils.getExtension(file.getName()));
    }

    public long getModified() {
        return file.lastModified();
    }

    public String toModifiedString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        return sdf.format(file.lastModified());
    }

    public long getSize() {
        return FileUtils.sizeOf(file);
    }

    public String getType() {
        return getFormat().getExtension();
    }

    public String getName() {
        return FilenameUtils.getBaseName(file.getName());
    }

    public FileInfo(File file) {
        this.file = file;
    }

}

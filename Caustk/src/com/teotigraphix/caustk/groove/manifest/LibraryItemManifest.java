
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;

public class LibraryItemManifest {

    @Tag(0)
    private String displayName;

    @Tag(1)
    private File archiveFile;

    @Tag(2)
    private String relativePath;

    //    private LibraryBank libraryBank;

    //    public LibraryBank getLibraryBank() {
    //        return libraryBank;
    //    }

    public String getDisplayName() {
        return displayName;
    }

    public File getArchiveFile() {
        return archiveFile;
    }

    public String getRelativePath() {
        return relativePath;
    }

    //--------------------------------------------------------------------------

    public LibraryItemFormat getFormat() {
        return LibraryItemFormat.fromString(FilenameUtils.getExtension(archiveFile.getName()));
    }

    public long getModified() {
        return archiveFile.lastModified();
    }

    public String toModifiedString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        return sdf.format(archiveFile.lastModified());
    }

    public long getSize() {
        return FileUtils.sizeOf(archiveFile);
    }

    public String getExtension() {
        return getFormat().getExtension();
    }

    public String getFileName() {
        return FilenameUtils.getBaseName(archiveFile.getName());
    }

    public LibraryItemManifest(String displayName, File archiveFile, String relativePath) {
        this.displayName = displayName;
        this.archiveFile = archiveFile;
        this.relativePath = relativePath;
    }

    //    public LibraryItemManifest(String name, LibraryBank libraryBank) {
    //        this(name);
    //        this.libraryBank = libraryBank;
    //    }

}

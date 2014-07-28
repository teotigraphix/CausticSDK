
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.library.GrooveLibrary;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.node.ICaustkNode;
import com.teotigraphix.caustk.node.Library;

public class LibraryItemManifest {

    @Tag(0)
    private UUID id;

    @Tag(1)
    private LibraryItemFormat format;

    @Tag(3)
    private File archiveFile;

    @Tag(2)
    private String displayName;

    @Tag(4)
    private String relativePath;

    private String author;

    private String description;

    private Date created;

    private Date modified;

    private List<String> tags;

    private boolean selected;

    public UUID getId() {
        return id;
    }

    public final LibraryItemFormat getFormat() {
        return format;
    }

    //    protected void setFormat(LibraryItemFormat format) {
    //        this.format = format;
    //    }

    //    private LibraryBank libraryBank;

    //    public LibraryBank getLibraryBank() {
    //        return libraryBank;
    //    }

    //----------------------------------
    //  displayName
    //----------------------------------

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Whether the item has an explicit name.
     * 
     * @see #getDisplayName()
     */
    public boolean hasDisplayName() {
        return displayName != null && !displayName.equals("Untitled");
    }

    /**
     * Sets the display name of the item.
     * 
     * @param name A String display name.
     */
    public final void setName(String displayName) {
        this.displayName = displayName;
    }

    //----------------------------------
    //  archiveFile
    //----------------------------------

    /**
     * Returns the relative path from the owning {@link LibraryProduct}.
     * <p>
     * The path could be something like; <code>Kits/Drums/Trance/909.ggrp</code>
     * <p>
     * <strong>Do not</strong> use getAbsolutePath() on this File, use
     * {@link GrooveLibrary#resolveLocation(ICaustkDefinition)} instead to
     * resolve the directory correctly.
     */
    public File getArchiveFile() {
        return archiveFile;
    }

    /**
     * Returns the full relative path from the Library's base;
     * <code>/root/MyApp/Libraries/MyLib/[Effect/Distortion/SubDir/MyDistorion.effect]</code>
     * <p>
     * This property is non <code>null</code> when the component is added to the
     * {@link Library#add(ICaustkNode)}, or {@link #setFile(File)} is explicitly
     * called with a relative path value.
     */
    public String getRelativePath() {
        return relativePath;
    }

    //--------------------------------------------------------------------------
    //  Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    //  author
    //----------------------------------

    /**
     * The author information.
     */
    public final String getAuthor() {
        return author;
    }

    /**
     * Sets the author information.
     * 
     * @param author A String value.
     */
    public final void setAuthor(String author) {
        this.author = author;
    }

    //----------------------------------
    //  description
    //----------------------------------

    /**
     * The description information.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description information.
     * 
     * @param description A String value.
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    //----------------------------------
    //  created
    //----------------------------------

    /**
     * The Date created.
     */
    public final Date getCreated() {
        return created;
    }

    /**
     * Sets the created data.
     * 
     * @param created A creation Date.
     */
    public final void setCreated(Date created) {
        this.created = created;
    }

    //----------------------------------
    //  modified
    //----------------------------------

    /**
     * The data last modified.
     */
    public final Date getModifiedDate() {
        return modified;
    }

    /**
     * Sets the modified data.
     * 
     * @param modified A creation Date.
     */
    public final void setModified(Date modified) {
        this.modified = modified;
    }

    //----------------------------------
    //  tags
    //----------------------------------

    /**
     * Returns a List of String tags added to this information object for
     * searching.
     */
    public final List<String> getTags() {
        if (tags == null)
            tags = new ArrayList<String>();
        return tags;
    }

    /**
     * Sets a List of String tags describing the node's information.
     * 
     * @param tags The String tag List.
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Returns a single String with the tags joined together with a space ' '.
     */
    public String getTagsString() {
        return join(tags, " ");
    }

    /**
     * Sets a tag String which is split into individual tags.
     * 
     * @param value The tag String separated with spaces ' '.
     */
    public void setTagsString(String value) {
        tags = Arrays.asList(value.split(" "));
    }

    //----------------------------------
    //  selected
    //----------------------------------

    /**
     * Whether this information is selected.
     * <p>
     * The context determines what this value actually means.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets this node information selected.
     * 
     * @param selected Selects or de-selects the node information.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //--------------------------------------------------------------------------

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

    public LibraryItemManifest(LibraryItemFormat format, String displayName, File archiveFile,
            String relativePath) {
        this.format = format;
        this.displayName = displayName;
        this.archiveFile = archiveFile;
        this.relativePath = relativePath;
        this.id = UUID.randomUUID();
        setCreated(new Date());
        setModified(new Date());
    }

    //    public LibraryItemManifest(String name, LibraryBank libraryBank) {
    //        this(name);
    //        this.libraryBank = libraryBank;
    //    }

    //--------------------------------------------------------------------------
    //  Methods
    //--------------------------------------------------------------------------

    /**
     * Returns whether the info has tags.
     */
    public boolean hasTags() {
        return tags != null && tags.size() > 0;
    }

    /**
     * Add a tag to the node information.
     * 
     * @param tag A String tag with no spaces.
     */
    public void addTag(String tag) {
        getTags().add(tag);
    }

    static String join(Collection<String> list, String delimiter) {
        final StringBuffer buffer = new StringBuffer();
        Iterator<String> i = list.iterator();
        while (i.hasNext()) {
            buffer.append(i.next());
            if (i.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

}

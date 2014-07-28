////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.groove.manifest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.node.ICaustkNode;
import com.teotigraphix.caustk.node.Library;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryItemManifest {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private UUID productId;

    @Tag(2)
    private LibraryItemFormat format;

    @Tag(3)
    private String name;

    @Tag(4)
    private String displayName;

    @Tag(5)
    private String relativePath;

    @Tag(6)
    private String author;

    @Tag(7)
    private String description;

    @Tag(8)
    private Date created;

    @Tag(9)
    private Date modified;

    @Tag(10)
    private List<String> tags;

    @Tag(11)
    private Boolean selected;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    //  id
    //----------------------------------

    public UUID getId() {
        return id;
    }

    //----------------------------------
    //  productId
    //----------------------------------

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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
    //  name
    //----------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Whether the item has an explicit name.
     * 
     * @see #getName()
     */
    public boolean hasName() {
        return name != null && !name.equals("Untitled");
    }

    //----------------------------------
    //  displayName
    //----------------------------------

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name of the item.
     * 
     * @param name A String display name.
     */
    public final void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    //----------------------------------
    //  relativePath
    //----------------------------------

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

    //    public long getModified() {
    //        return archiveFile.lastModified();
    //    }
    //
    //    public String toModifiedString() {
    //        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
    //        return sdf.format(archiveFile.lastModified());
    //    }
    //
    //    public long getSize() {
    //        return FileUtils.sizeOf(archiveFile);
    //    }

    public String getExtension() {
        return getFormat().getExtension();
    }

    //    public String getFileName() {
    //        return FilenameUtils.getBaseName(archiveFile.getName());
    //    }

    //--------------------------------------------------------------------------
    //  Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization.
     */
    LibraryItemManifest() {
    }

    public LibraryItemManifest(LibraryItemFormat format, String name, String relativePath) {
        this.format = format;
        this.name = name;
        this.relativePath = relativePath;
        this.id = UUID.randomUUID();
        setCreated(new Date());
        setModified(new Date());
    }

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

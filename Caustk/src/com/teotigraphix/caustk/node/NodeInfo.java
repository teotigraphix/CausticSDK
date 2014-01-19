////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * The {@link NodeBase} metadata information object.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class NodeInfo {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private UUID id;

    private NodeType type;

    private File file;

    private String path;

    private String name;

    private String author;

    private String description;

    private Date created;

    private Date modified;

    private List<String> tags;

    private boolean selected;

    //----------------------------------
    //  id
    //----------------------------------

    /**
     * Returns the unique id of the component.
     * <p>
     * <strong>Assigned only at construction.</strong>
     */
    public UUID getId() {
        return id;
    }

    //----------------------------------
    //  componentType
    //----------------------------------

    /**
     * Returns the type of Caustk node.
     */
    public NodeType getType() {
        return type;
    }

    //----------------------------------
    //  name
    //----------------------------------

    /**
     * Whether the node has an explicit name.
     * 
     * @see #getName()
     */
    public boolean hasName() {
        return name != null && !name.equals("Untitled");
    }

    /**
     * Returns the display name of the name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the display name of the node.
     * 
     * @param name A String display name.
     */
    public final void setName(String name) {
        this.name = name;
    }

    //----------------------------------
    //  file
    //----------------------------------

    /**
     * Returns the relative path from the owning {@link Library}.
     * <p>
     * The path could be something like;
     * <code>Machine/SubSynth/Trance/FM Synth Setup.cmc</code>
     * <p>
     * <strong>Do not</strong> use getAbsolutePath() on this File, use
     * {@link Library#resolveLocation(ICaustkDefinition)} instead to resolve the
     * directory correctly.
     */
    public File getFile() {
        return file;
    }

    public void setFile(File value) {
        file = value;
    }

    /**
     * Returns the relative path within the {@link DefinitionType}'s sub
     * directory.
     */
    public String getRelativePath() {
        if (file == null)
            return "";
        return file.getPath();
    }

    //----------------------------------
    //  path
    //----------------------------------

    /**
     * Returns the full relative path from the Library's base;
     * <code>/root/MyApp/Libraries/MyLib/[Effect/Distortion/SubDir/MyDistorion.cef]</code>
     * <p>
     * This property is set when the component is added to the
     * {@link Library#add(ICaustkDefinition)}.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the base component path including file name and extension.
     * <p>
     * Used for mapping and finding components when a library is deserialized
     * from a manifest.
     * 
     * @param path The path.
     */
    void setPath(String path) {
        this.path = path;
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
    public final Date getModified() {
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
    //  Constructor
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    NodeInfo() {
    }

    /**
     * Creates a {@link NodeBase} information object with {@link UUID} and
     * {@link NodeType}.
     * 
     * @param id The unique identifier.
     * @param type The {@link NodeType}.
     */
    NodeInfo(UUID id, NodeType type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Creates a {@link NodeBase} information object with {@link UUID},
     * {@link NodeType}, file location and custom display name.
     * 
     * @param id The unique identifier.
     * @param type The {@link NodeType}.
     * @param relativeFile A relative path of this node located within a
     *            Library.
     * @param name The node's display name.
     */
    NodeInfo(UUID id, NodeType type, File relativeFile, String name) {
        this.id = id;
        this.type = type;
        this.file = relativeFile;
        this.name = name;
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

    @Override
    public String toString() {
        return "[NoteInfo(" + type.name() + ", " + name + ", " + file + ")]";
    }
}

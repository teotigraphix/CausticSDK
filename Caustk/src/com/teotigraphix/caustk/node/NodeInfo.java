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
     * <code>Machine/SubSynth/Trance/FM Synth Setup.machine</code>
     * <p>
     * <strong>Do not</strong> use getAbsolutePath() on this File, use
     * {@link Library#resolveLocation(ICaustkDefinition)} instead to resolve the
     * directory correctly.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the relative file location of this node.
     * 
     * @param file The non absolute file.
     * @throws IllegalStateException File must not be absolute
     */
    public void setFile(File file) {
        if (file.isAbsolute())
            throw new IllegalStateException("File must not be absolute");
        this.file = file;
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
        if (file == null)
            return null;
        return file.getPath();
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((modified == null) ? 0 : modified.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (selected ? 1231 : 1237);
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeInfo other = (NodeInfo)obj;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (created.compareTo(other.created) == 0)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (modified == null) {
            if (other.modified != null)
                return false;
        } else if (modified.compareTo(other.modified) == 0)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (selected != other.selected)
            return false;
        if (tags == null) {
            if (other.tags != null)
                return false;
        } else if (!tags.equals(other.tags))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[NodeInfo(" + type.name() + ", " + name + ", " + file + ")]";
    }
}

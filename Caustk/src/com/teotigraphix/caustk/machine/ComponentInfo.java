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

package com.teotigraphix.caustk.machine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/**
 * @author Michael Schmalle
 */
public class ComponentInfo {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private ComponentType type;

    @Tag(2)
    private File file;

    @Tag(10)
    private String name;

    @Tag(11)
    private String author;

    @Tag(12)
    private String description;

    @Tag(13)
    private Date created;

    @Tag(14)
    private Date modified;

    @Tag(15)
    private List<String> tags;

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
     * Returns the type of Caustk component.
     */
    public ComponentType getType() {
        return type;
    }

    //----------------------------------
    //  name
    //----------------------------------

    public boolean hasName() {
        return name != null && !name.equals("Untitled");
    }

    /**
     * Returns the display name of the component.
     */
    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        name = value;
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
     * {@link Library#resolveLocation(ICaustkComponent)} instead to
     * resolve the directory correctly.
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the relative path within the {@link ComponentType}'s sub
     * directory.
     */
    public String getPath() {
        return file.getPath();
    }

    //--------------------------------------------------------------------------
    //  Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    //  author
    //----------------------------------

    public final String getAuthor() {
        return author;
    }

    public final void setAuthor(String value) {
        author = value;
    }

    //----------------------------------
    //  description
    //----------------------------------

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String value) {
        description = value;
    }

    //----------------------------------
    //  created
    //----------------------------------

    public final Date getCreated() {
        return created;
    }

    public final void setCreated(Date value) {
        created = value;
    }

    //----------------------------------
    //  modified
    //----------------------------------

    public final Date getModified() {
        return modified;
    }

    public final void setModified(Date value) {
        modified = value;
    }

    //----------------------------------
    //  tags
    //----------------------------------

    public final List<String> getTags() {
        if (tags == null)
            tags = new ArrayList<String>();
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTagsString() {
        return join(tags, " ");
    }

    public void setTagsString(String value) {
        tags = Arrays.asList(value.split(" "));
    }

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    ComponentInfo() {
    }

    ComponentInfo(UUID id, ComponentType type) {
        this.id = id;
        this.type = type;
    }

    ComponentInfo(UUID id, ComponentType type, File relativeFile, String name) {
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

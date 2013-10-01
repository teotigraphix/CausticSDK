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

package com.teotigraphix.caustk.library.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MetadataInfo {

    //--------------------------------------------------------------------------
    //  Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    //  name
    //----------------------------------

    private String name = "Untitled";

    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        name = value;
    }

    //----------------------------------
    //  author
    //----------------------------------

    private String author = "Unamed";

    public final String getAuthor() {
        return author;
    }

    public final void setAuthor(String value) {
        author = value;
    }

    //----------------------------------
    //  description
    //----------------------------------

    private String description = "";

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String value) {
        description = value;
    }

    //----------------------------------
    //  created
    //----------------------------------

    private Date created;

    public final Date getCreated() {
        return created;
    }

    public final void setCreated(Date value) {
        created = value;
    }

    //----------------------------------
    //  modified
    //----------------------------------

    private Date modified;

    public final Date getModified() {
        return modified;
    }

    public final void setModified(Date value) {
        modified = value;
    }

    //----------------------------------
    //  tags
    //----------------------------------

    private List<String> tags = new ArrayList<String>();

    public List<String> getTags() {
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

    public MetadataInfo() {
    }

    //--------------------------------------------------------------------------
    //  Methods
    //--------------------------------------------------------------------------

    public void addTag(String tag) {
        tags.add(tag);
    }

    public static String join(Collection<String> s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
}

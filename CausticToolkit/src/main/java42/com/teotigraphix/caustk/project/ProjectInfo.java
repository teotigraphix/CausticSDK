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

package com.teotigraphix.caustk.project;

import java.util.Date;

public class ProjectInfo {

    //----------------------------------
    // name
    //----------------------------------

    private String name;

    /**
     * Returns the project's human readable name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the project's human readable name.
     * 
     * @param value The name.
     */
    public final void setName(String value) {
        name = value;
    }

    //----------------------------------
    // author
    //----------------------------------

    private String author;

    /**
     * Returns the project's author.
     */
    public final String getAuthor() {
        return author;
    }

    public final void setAuthor(String value) {
        author = value;
    }

    //----------------------------------
    // description
    //----------------------------------

    private String description;

    /**
     * Returns the project's description.
     */
    public final String getDescription() {
        return description;
    }

    public final void setDescription(String value) {
        description = value;
    }

    //----------------------------------
    // created
    //----------------------------------

    private Date created;

    /**
     * Returns the date the project was created.
     */
    public final Date getCreated() {
        return created;
    }

    public final void setCreated(Date value) {
        created = value;
    }

    //----------------------------------
    // modified
    //----------------------------------

    private Date modified;

    /**
     * Returns the date the project was modified.
     */
    public final Date getModified() {
        return modified;
    }

    public final void setModified(Date value) {
        modified = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectInfo() {
    }

}

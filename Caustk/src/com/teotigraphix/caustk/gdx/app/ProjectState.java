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

package com.teotigraphix.caustk.gdx.app;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/**
 * The internal state for the {@link IApplicationModel} held within the project.
 * <p>
 * Each {@link com.teotigraphix.caustk.gdx.app.Project} owns an instance of this
 * and will be serialized with the project.
 */
public abstract class ProjectState {

    //--------------------------------------------------------------------------
    // Serialization
    //--------------------------------------------------------------------------

    @Tag(0)
    private Project project;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // project
    //----------------------------------

    public Project getProject() {
        return project;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    protected ProjectState() {
    }

    public ProjectState(Project project) {
        this.project = project;
    }

    //--------------------------------------------------------------------------
    // Public Callback :: Methods
    //--------------------------------------------------------------------------

    /**
     * Called before {@link #create()} and {@link #load()}.
     * 
     * @see com.teotigraphix.caustk.gdx.app.Project#onInitialize()
     */
    public void initialize() {
    }

    /**
     * Called when a Project is created for the first time or has no rack
     * bytes(project specific).
     * 
     * @see com.teotigraphix.caustk.gdx.app.Project#onCreate()
     */
    public void create() {
    }

    /**
     * Called when deserializing a project from disk.
     * 
     * @see com.teotigraphix.caustk.gdx.app.Project#onLoad()
     */
    public void load() {
    }

}

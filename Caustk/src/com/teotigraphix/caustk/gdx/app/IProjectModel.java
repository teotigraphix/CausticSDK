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

/**
 * The {@link IApplicationModel} loads and sets the {@link #getProject()}.
 */
public interface IProjectModel {

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // properties
    //----------------------------------

    /**
     * The serialized primitive project value api.
     * <p>
     * Any String, boolean, int, float or long can be saved per project in this
     * instance.
     */
    ProjectProperties getProperties();

    //----------------------------------
    // project
    //----------------------------------

    /**
     * Returns the current project impl loaded in the application.
     */
    <T extends Project> T getProject();

    //----------------------------------
    // machineAPI
    //----------------------------------

    /**
     * The {@link com.teotigraphix.caustk.node.machine.MachineNode} API.
     */
    ProjectModelMachineAPI getMachineAPI();

    //--------------------------------------------------------------------------
    // Methods
    //--------------------------------------------------------------------------

    //void restore(ProjectState state);

    public static class ProjectModelEvent {

        public static enum Kind {
            MachineSelectionChange;
        }

        private Kind kind;

        private IProjectModel model;

        public Kind getKind() {
            return kind;
        }

        public IProjectModel getModel() {
            return model;
        }

        public ProjectModelEvent(Kind kind, IProjectModel model) {
            this.kind = kind;
            this.model = model;
        }
    }
}

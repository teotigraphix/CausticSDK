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

import java.io.File;
import java.io.IOException;

public interface IApplicationModel extends IApplicationComponent {

    /**
     * Whether the application is dirty.
     */
    boolean isDirty();

    /**
     * Sets the application state dirty.
     * 
     * @param dirty Whether the application is dirty.
     */
    void setDirty(boolean dirty);

    ApplicationPreferences getApplicationPreferences();

    void newProject(File file) throws IOException;

    void loadProject(File file) throws IOException;

    File saveProjectAs(String projectName) throws IOException;

    File exportProject(File file, ApplicationExportType exportType) throws IOException;;

    void save();

    void dispose();

    public static enum ApplicationExportType {

        Caustic,

        Project;
    }

    public static enum ApplicationModelEventKind {
        IsDirtyChange
    }

    public static class ApplicationModelEvent {

        private IApplicationModel applicationModel;

        private ApplicationModelEventKind kind;

        public IApplicationModel getApplicationModel() {
            return applicationModel;
        }

        public ApplicationModelEventKind getKind() {
            return kind;
        }

        public ApplicationModelEvent(IApplicationModel applicationModel,
                ApplicationModelEventKind kind) {
            this.applicationModel = applicationModel;
            this.kind = kind;
        }
    }

}

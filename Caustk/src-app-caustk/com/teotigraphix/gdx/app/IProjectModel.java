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

package com.teotigraphix.gdx.app;

import java.util.Collection;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.controller.ViewBase;
import com.teotigraphix.gdx.groove.ui.components.SceneViewChildData;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

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

    ProjectProperties getProperties();

    //----------------------------------
    // project
    //----------------------------------

    <T extends Project> T getProject();

    ProjectModelMachineAPI getMachineAPI();

    //----------------------------------
    // SceneViews
    //----------------------------------

    Array<ButtonBarItem> getSceneButtons();

    void setSceneButtons(Array<ButtonBarItem> buttons);

    Array<SceneViewChildData> getSceneViews();

    void setSceneViews(Array<SceneViewChildData> views);

    int getSceneViewIndex();

    void setSceneViewIndex(int viewIndex);

    //----------------------------------
    // Views
    //----------------------------------

    Collection<ViewBase> getViews();

    ViewBase getViewByIndex(int viewIndex);

    ViewBase getSelectedView();

    void setSelectedViewId(int viewid);

    int getViewIndex();

    void setViewIndex(int viewIndex);

    Array<ButtonBarItem> getViewButtons();

    void setViewButtons(Array<ButtonBarItem> buttons);

    //--------------------------------------------------------------------------
    // Methods
    //--------------------------------------------------------------------------

    void restore(ProjectState state);

    public static enum ProjectModelEventKind {
        SceneViewChange, ViewChange, MachineSelectionChange;
    }

    public static class ProjectModelEvent {
        private ProjectModelEventKind kind;

        private IProjectModel model;

        public ProjectModelEventKind getKind() {
            return kind;
        }

        public IProjectModel getModel() {
            return model;
        }

        public ProjectModelEvent(ProjectModelEventKind kind, IProjectModel model) {
            this.kind = kind;
            this.model = model;
        }
    }

}

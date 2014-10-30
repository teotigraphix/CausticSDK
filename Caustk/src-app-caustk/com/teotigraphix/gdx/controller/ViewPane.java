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

package com.teotigraphix.gdx.controller;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.gdx.app.ProjectState;

/**
 * A view pane is a child of the {@link IViewManager} and saves it's state on
 * the project.
 * <p>
 * A {@link ViewPane} will wrap more complex ui logic that exists between a UI
 * component (Pane) and it's Behavior (PaneBehavior).
 */
public class ViewPane {

    // subclass tags start at 50

    @Tag(0)
    private String id;

    // the RUNTIME index, non serialized
    private int index;

    private ProjectState state;

    private IViewManager viewManager;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public ProjectState getState() {
        return state;
    }

    public IViewManager getViewManager() {
        return viewManager;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    protected ViewPane() {
    }

    public ViewPane(String id) {
        this.id = id;
    }

    /**
     * Attaches the manager to the view.
     * 
     * @param viewManager
     */
    public void attachTo(IViewManager viewManager, ProjectState state) {
        this.viewManager = viewManager;
        this.state = state;
    }

    /**
     * Called when the pane is activated/enabled and visible.
     */
    public void onActivate() {
    }

    /**
     * Called when the pane is deactivated/disabled and invisible.
     */
    public void onDeactivate() {
    }

    protected void post(Object event) {
        CaustkRuntime.getInstance().getApplication().getEventBus().post(event);
    }

}

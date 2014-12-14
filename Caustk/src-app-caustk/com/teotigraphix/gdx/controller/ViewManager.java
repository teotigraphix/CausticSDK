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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.groove.session.SceneManager;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.gdx.app.ApplicationComponent;
import com.teotigraphix.gdx.app.CaustkApplication;
import com.teotigraphix.gdx.app.IProjectModel;
import com.teotigraphix.gdx.app.ProjectState;
import com.teotigraphix.gdx.controller.view.AbstractDisplay;

@Singleton
public abstract class ViewManager extends ApplicationComponent implements IViewManager {

    @Inject
    private IProjectModel projectModel;

    private AbstractDisplay display;

    private AbstractDisplay subDisplay;

    protected boolean redrawEnabled = true;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // projectModel
    //----------------------------------

    public IProjectModel getProjectModel() {
        return projectModel;
    }

    //----------------------------------
    // redrawEnabled
    //----------------------------------

    public final boolean isRedrawEnabled() {
        return redrawEnabled;
    }

    public final void setRedrawEnabled(boolean redrawEnabled) {
        this.redrawEnabled = redrawEnabled;
        if (this.redrawEnabled) {
            // flush since it was halted
            onRedrawAll();
        }
    }

    protected abstract void onRedrawAll();

    //----------------------------------
    // display
    //----------------------------------

    @Override
    public AbstractDisplay getDisplay() {
        return display;
    }

    protected void setDisplay(AbstractDisplay display) {
        this.display = display;
    }

    //----------------------------------
    // subDisplay
    //----------------------------------

    @Override
    public AbstractDisplay getSubDisplay() {
        return subDisplay;
    }

    protected void setSubDisplay(AbstractDisplay subDisplay) {
        this.subDisplay = subDisplay;
    }

    // TODO remove!
    @Override
    public PatternNode getSelectedMachinePattern() {
        return projectModel.getMachineAPI().getSelectedMachinePattern();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ViewManager() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void flush(boolean clear) {

        //        if (clear) {
        //            getPads().clear();
        //        }

        //        ViewBase selectedView = getSelectedView();
        //        if (selectedView != null)
        //            selectedView.updateArrows();

        if (getDisplay() != null) {
            getDisplay().flush();
            getSubDisplay().flush();
        }

        //        if (getPads() != null) {
        //            getPads().flush();
        //        }
    }

    public void flush() {
        flush(false);
    }

    /**
     * Called the last starup frame after behaviors have been created.
     * 
     * @see ApplicationStates#startUI()
     * @see ViewManagerStartUIEvent
     */
    public void onStartUI() {
        getEventBus().post(new ViewManagerStartUIEvent());
    }

    /**
     * {@link CaustkApplication#startScene()}
     * 
     * @see SceneManager#reset()
     * @see SceneManager#setScene(getInitialScene());
     * @see ViewManagerReStartUIEvent
     */
    public void onRestartUI() {
        getEventBus().post(new ViewManagerReStartUIEvent());
    }

    /**
     * Called when projects are loaded and the ui needs a clean redraw.
     * 
     * @see ViewManagerRedrawUIEvent
     */
    public void onRedraw(Object kind) {
        if (redrawEnabled)
            getEventBus().post(new ViewManagerRedrawUIEvent(kind));
    }

    public abstract void restore(ProjectState state);
}

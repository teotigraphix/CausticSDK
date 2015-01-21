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

package com.teotigraphix.caustk.gdx.app.ui;

import com.google.inject.Inject;
import com.teotigraphix.caustk.gdx.app.IApplication;
import com.teotigraphix.caustk.gdx.app.IProjectModel;
import com.teotigraphix.caustk.gdx.app.ProjectModel;
import com.teotigraphix.caustk.gdx.app.controller.IViewManager;
import com.teotigraphix.caustk.gdx.app.controller.ViewManager;
import com.teotigraphix.caustk.gdx.groove.ui.IContainerMap;
import com.teotigraphix.caustk.gdx.groove.ui.factory.UIFactory;

public abstract class CaustkScene extends Scene implements ICaustkScene {

    //--------------------------------------------------------------------------
    // Inject
    //--------------------------------------------------------------------------

    @Inject
    private UIFactory factory;

    @Inject
    private IProjectModel projectModel;

    @Inject
    private IContainerMap containerMap;

    @Inject
    private IViewManager viewManager;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // factory
    //----------------------------------

    @Override
    public UIFactory getFactory() {
        return factory;
    }

    //----------------------------------
    // projectModel
    //----------------------------------

    @Override
    public IProjectModel getProjectModel() {
        return projectModel;
    }

    //----------------------------------
    // viewManager
    //----------------------------------

    @Override
    public IViewManager getViewManager() {
        return viewManager;
    }

    //--------------------------------------------------------------------------
    // Public :: Properties
    //--------------------------------------------------------------------------

    protected IContainerMap getContainerMap() {
        return containerMap;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CaustkScene() {
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void initialize(IApplication application) {
        super.initialize(application);
        getFactory().createDefaults(getSkin());
        containerMap.register(this);
        containerMap.setScene(this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ((ViewManager)getViewManager()).render();
    }

    @Override
    public void start() {
        super.start();
        ((ProjectModel)getProjectModel()).restore(projectModel.getProject().getState());
    }

    @Override
    public void onPreCalculate(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onPreCalculate(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onBeatChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onPreSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onPreSixteenthChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onSixteenthChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onThirtysecondChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onThirtysecondChange(measure, beat, sixteenth, thirtysecond);
        }
    }
}

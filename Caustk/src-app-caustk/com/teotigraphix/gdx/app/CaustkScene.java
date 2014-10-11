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

import com.google.inject.Inject;
import com.teotigraphix.gdx.groove.ui.IContainerMap;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;
import com.teotigraphix.gdx.groove.ui.model.IUIModel;

public abstract class CaustkScene extends Scene implements ICaustkScene {

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private UIFactory factory;

    @Inject
    private IUIModel model;

    @Inject
    private IContainerMap containerMap;

    @Override
    public UIFactory getFactory() {
        return factory;
    }

    @Override
    public IUIModel getModel() {
        return model;
    }

    protected IApplicationModel getApplicationModel() {
        return applicationModel;
    }

    protected IContainerMap getContainerMap() {
        return containerMap;
    }

    public CaustkScene() {
    }

    @Override
    public void initialize(IApplication application) {
        super.initialize(application);
        containerMap.register(this);
        containerMap.setScene(this);
    }

    @Override
    public void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onBeatChange(measure, beat, sixteenth, thirtysecond);
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

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

package com.teotigraphix.gdx.groove.ui;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.CaustkScene;

@Singleton
public class ContainerMap implements IContainerMap {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private HashMap<IContainerKind, WidgetGroup> map = new HashMap<IContainerKind, WidgetGroup>();

    private EventBus eventBus;

    private CaustkScene scene;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // scene
    //----------------------------------

    @Override
    public void setScene(CaustkScene scene) {
        this.scene = scene;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ContainerMap() {
        eventBus = new EventBus();
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public WidgetGroup get(IContainerKind key) {
        return map.get(key);
    }

    @Override
    public void put(IContainerKind key, WidgetGroup group) {
        map.put(key, group);
    }

    @Override
    public void register(Object object) {
        eventBus.register(object);
    }

    @Override
    public void addActor(IContainerKind kind, WidgetGroup actor) {
        Rectangle bounds = kind.getBounds();
        scene.getRoot().addActor(actor);
        actor.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        put(kind, actor);
    }

}

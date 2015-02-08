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

package com.teotigraphix.caustk.groove.behavior;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.Subscribe;
import com.teotigraphix.caustk.gdx.app.controller.IViewManager.ViewManagerEvent;
import com.teotigraphix.caustk.gdx.app.ui.CaustkBehavior;

public abstract class GrooveBehavior extends CaustkBehavior {

    public GrooveBehavior() {
    }

    @Subscribe
    public void onViewManagerEvent(final ViewManagerEvent event) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (getScene() != null) {
                    redrawView(event.getKind());
                } else {
                    // XXX This happens when a Scene is disposed and there are
                    // still Runnable getting executed on the next frame AFTER
                    // the scene has been deleted
                    //err(getClass().getName(), event.getKind()
                    //        + "No Scene, Failed in GrooveBehavior.onViewManagerEvent("
                    //        + "getScene() == null)");
                }
            }
        });
    }

    /**
     * Override in subclass and make kind concrete enum type.
     * 
     * @param kind the project's redraw enum for all its behaviors.
     */
    protected abstract void redrawView(Object kind);
}

////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.libgdx.controller;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.libgdx.screen.IScreen;

public class ScreenMediator extends CaustkMediator {

    private List<ScreenMediatorChild> subMediators = new ArrayList<ScreenMediatorChild>();

    protected void addMediator(ScreenMediatorChild child) {
        subMediators.add(child);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ScreenMediator() {
    }

    //--------------------------------------------------------------------------
    // LifeCycle
    //--------------------------------------------------------------------------

    public void onInitialize(IScreen screen) {
    }

    protected void createChildren(IScreen screen, Table parent) {
        for (ScreenMediatorChild mediator : subMediators) {
            mediator.onCreate(screen, parent);
        }
    }

    /**
     * Call {@link #createChildren(IScreen, Table)} from the
     * {@link #onCreate(IScreen)} method in sub classes.
     * 
     * @param screen
     */
    public void onCreate(IScreen screen) {
    }

    public void onAttach(IScreen screen) {
        for (ScreenMediatorChild child : subMediators) {
            child.onAttach(screen);
        }
    }

    public void onShow(IScreen screen) {
        for (ScreenMediatorChild child : subMediators) {
            child.onShow(screen);
        }
    }

    public void onHide(IScreen screen) {
        for (ScreenMediatorChild child : subMediators) {
            child.onHide(screen);
        }
    }

    public void onResume(IScreen screen) {
        for (ScreenMediatorChild child : subMediators) {
            child.onResume(screen);
        }
    }

    public void onPause(IScreen screen) {
        for (ScreenMediatorChild child : subMediators) {
            child.onPause(screen);
        }
    }

    public void onDispose(IScreen screen) {
        for (ScreenMediatorChild child : subMediators) {
            child.onDispose(screen);
        }
    }

    public void onDetach(IScreen screen) {
        for (ScreenMediatorChild child : subMediators) {
            child.onDetach(screen);
        }
    }
}

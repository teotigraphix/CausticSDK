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

package com.teotigraphix.gdx.app;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.gdx.GdxScreen;
import com.teotigraphix.gdx.IGdxScreen;

/**
 * The {@link ScreenMediator} is a view mediator that draws and handles UI
 * events from custom components.
 * <p>
 * The mediator is also capable of handling child mediators that mediate
 * specific parts of a complicated view.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ScreenMediator extends CaustkMediator {

    protected List<ScreenMediatorChild> children = new ArrayList<ScreenMediatorChild>();

    private IGdxScreen screen;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // screen
    //----------------------------------

    /**
     * Returns the mediator's owning {@link IGdxScreen}.
     */
    public IGdxScreen getScreen() {
        return screen;
    }

    /**
     * Sets the {@link IGdxScreen} owning screen.
     * 
     * @param screen The mediator's owner.
     * @see #onScreenChange(IGdxScreen)
     */
    public void setScreen(IGdxScreen screen) {
        this.screen = screen;
        onScreenChange(screen);
    }

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // skin
    //----------------------------------

    /**
     * Returns the screen's {@link Skin}.
     */
    protected Skin getSkin() {
        return screen.getSkin();
    }

    //----------------------------------
    // stage
    //----------------------------------

    /**
     * Returns the screen's {@link Stage}.
     */
    protected Stage getStage() {
        return screen.getStage();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Creates a {@link ScreenMediator}.
     */
    public ScreenMediator() {
    }

    //--------------------------------------------------------------------------
    // LifeCycle
    //--------------------------------------------------------------------------

    /**
     * Called once during {@link GdxScreen#create()}, before {@link #onCreate()}
     * .
     * <p>
     * Add all global/model event listeners.
     */
    @Override
    public void onAttach() {
        for (ScreenMediatorChild child : children) {
            child.onAttach();
        }
    }

    /**
     * Called once during {@link GdxScreen#create()}, after {@link #onAttach()}.
     * <p>
     * Create all user interface components that are attached to the
     * {@link #getStage()}.
     * <p>
     * Call {@link #createChildren(Table)} from the {@link #onCreate(IScreen)}
     * method in sub classes if this mediator is using child tables with
     * mediators.
     */
    public void onCreate() {
    }

    /**
     * Called during {@link IGdxScreen#show()}.
     */
    public void onShow() {
        for (ScreenMediatorChild child : children) {
            child.onShow();
        }
    }

    /**
     * Called during {@link IGdxScreen#hide()}.
     */
    public void onHide() {
        for (ScreenMediatorChild child : children) {
            child.onHide();
        }
    }

    /**
     * Called during {@link IGdxScreen#resume()}.
     */
    public void onResume() {
        for (ScreenMediatorChild child : children) {
            child.onResume();
        }
    }

    /**
     * Called during {@link IGdxScreen#pause()}.
     */
    public void onPause() {
        for (ScreenMediatorChild child : children) {
            child.onPause();
        }
    }

    /**
     * Called during {@link IGdxScreen#dispose()}.
     * <p>
     * Called before {@link #onDispose()}.
     */
    public void onDetach() {
        for (ScreenMediatorChild child : children) {
            child.onDetach();
        }
    }

    /**
     * Called during {@link IGdxScreen#dispose()}.
     * <p>
     * Called after {@link #onDetach()}.
     */
    public void onDispose() {
        for (ScreenMediatorChild child : children) {
            child.onDispose();
        }
        screen = null;
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Called when {@link #setScreen(IGdxScreen)}'s value has changed.
     * 
     * @param screen The new {@link IGdxScreen}.
     */
    protected void onScreenChange(IGdxScreen screen) {
    }

    /**
     * Adds a child mediator and sets its parent to this mediator.
     * 
     * @param child The {@link ScreenMediatorChild}.
     */
    protected void addChild(ScreenMediatorChild child) {
        child.setParent(this);
        children.add(child);
    }

    /**
     * Call in a subclass that uses {@link ScreenMediatorChild} composites, to
     * pass them their {@link Table} parent during creation.
     * 
     * @param parent The parent {@link Table} instance that will hold the child
     *            mediator's ui components.
     */
    protected void createChildren(Table parent) {
        for (ScreenMediatorChild mediator : children) {
            mediator.onCreate(parent);
        }
    }
}

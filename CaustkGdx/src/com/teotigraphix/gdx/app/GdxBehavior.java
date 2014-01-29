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

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.gdx.GdxScene;
import com.teotigraphix.gdx.IGdxScene;

/**
 * The {@link GdxBehavior} is a view behavior that draws and handles UI events
 * from custom components.
 * <p>
 * The behavior is also capable of handling child behaviors that mediate
 * specific parts of a complicated view.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class GdxBehavior extends GdxComponent implements IGdxBehavior {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private GdxBehavior parent;

    private List<IGdxBehavior> children = new ArrayList<IGdxBehavior>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // parent
    //----------------------------------

    /**
     * Returns the parent {@link GdxBehavior} of this child behavior.
     * <p>
     * If the parent is <code>null</code>, this behavior is rooted to the
     * {@link IGdxScene}.
     */
    public GdxBehavior getParent() {
        return parent;
    }

    void setParent(GdxBehavior parent) {
        this.parent = parent;
        setScene(parent.getScene());
        onParentChanged(parent);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Creates a {@link GdxBehavior}.
     */
    public GdxBehavior() {
    }

    //--------------------------------------------------------------------------
    // LifeCycle
    //--------------------------------------------------------------------------

    protected void onParentChanged(GdxBehavior parent) {
    }

    /**
     * Called once during {@link GdxScene#create()}, before {@link #onStart()} .
     * <p>
     * Add all global/model event listeners.
     */
    @Override
    public void onAwake() {
        for (IGdxBehavior child : children) {
            child.onAwake();
        }
    }

    /**
     * Called once during {@link GdxScene#create()}, after {@link #onAttach()}.
     * <p>
     * Create all user interface components that are attached to the
     * {@link #getStage()}.
     * <p>
     * Call {@link #createChildren(Table)} from the {@link #onCreate(IScreen)}
     * method in sub classes if this behavior is using child tables with
     * behaviors.
     */
    @Override
    public void onStart() {
        for (IGdxBehavior child : children) {
            child.onStart();
        }
    }

    @Override
    public void onUpdate() {
        for (IGdxBehavior child : children) {
            child.onUpdate();
        }
    }

    @Override
    public void onReset() {
        for (IGdxBehavior child : children) {
            child.onReset();
        }
    }

    /**
     * Called during {@link IGdxScene#show()}.
     */
    @Override
    public void onShow() {
        for (IGdxBehavior child : children) {
            child.onShow();
        }
    }

    /**
     * Called during {@link IGdxScene#hide()}.
     */
    @Override
    public void onHide() {
        for (IGdxBehavior child : children) {
            child.onHide();
        }
    }

    /**
     * Called during {@link IGdxScene#resume()}.
     */
    @Override
    public void onEnable() {
        for (IGdxBehavior child : children) {
            child.onEnable();
        }
    }

    /**
     * Called during {@link IGdxScene#pause()}, or {@link IGdxScene#dispose()}.
     */
    @Override
    public void onDisable() {
        for (IGdxBehavior child : children) {
            child.onDisable();
        }
    }

    /**
     * Called during {@link IGdxScene#dispose()}.
     * <p>
     * Called after {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        for (IGdxBehavior child : children) {
            child.onDestroy();
        }
        setScene(null);
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds a child behavior and sets its parent to this behavior.
     * 
     * @param child The {@link GdxBehaviorChild}.
     */
    protected void addComponent(IGdxBehavior child) {
        ((GdxBehavior)child).setParent(this);
        children.add(child);
    }

    //    /**
    //     * Call in a subclass that uses {@link GdxBehaviorChild} composites, to pass
    //     * them their {@link Table} parent during creation.
    //     * 
    //     * @param parent The parent {@link Table} instance that will hold the child
    //     *            behavior's ui components.
    //     */
    //    protected void createChildren(Table parent) {
    //        for (IGdxBehavior behavior : children) {
    //            // XXX Fix, shouldn't have cast
    //            ((GdxBehavior)behavior).onCreate(parent);
    //        }
    //    }
}

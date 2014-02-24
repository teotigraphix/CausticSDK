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

/**
 * The {@link Behavior} is a view behavior that draws and handles UI events from
 * custom components.
 * <p>
 * The behavior is also capable of handling child behaviors that mediate
 * specific parts of a complicated view.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class Behavior extends SceneComponent implements ISceneBehavior {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Behavior parent;

    private final List<ISceneBehavior> children = new ArrayList<ISceneBehavior>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // parent
    //----------------------------------

    /**
     * Returns the parent {@link Behavior} of this child behavior.
     * <p>
     * If the parent is <code>null</code>, this behavior is rooted to the
     * {@link IScene}.
     */
    public Behavior getParent() {
        return parent;
    }

    void setParent(Behavior parent) {
        this.parent = parent;
        setApplication(parent.getApplication());
        setScene(parent.getScene());
        onParentChanged(parent);
    }

    @Override
    public void setScene(IScene scene) {
        super.setScene(scene);
        attachChildren();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Creates a {@link Behavior}.
     */
    public Behavior() {
    }

    //--------------------------------------------------------------------------
    // LifeCycle
    //--------------------------------------------------------------------------

    protected abstract void attachChildren();

    protected void onParentChanged(Behavior parent) {
    }

    /**
     * Called once during {@link Scene#create()}, before {@link #onStart()} .
     * <p>
     * Add all global/model event listeners.
     */
    @Override
    public void onAwake() {
        for (ISceneBehavior child : children) {
            child.onAwake();
        }
    }

    /**
     * Called once during {@link Scene#create()}, after {@link #onAwake()}.
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
        for (ISceneBehavior child : children) {
            child.onStart();
        }
    }

    @Override
    public void onUpdate() {
        for (ISceneBehavior child : children) {
            child.onUpdate();
        }
    }

    @Override
    public void onResize() {
        for (ISceneBehavior child : children) {
            child.onResize();
        }
    }

    @Override
    public void onReset() {
        for (ISceneBehavior child : children) {
            child.onReset();
        }
    }

    /**
     * Called during {@link IScene#show()}.
     */
    @Override
    public void onShow() {
        for (ISceneBehavior child : children) {
            child.onShow();
        }
    }

    /**
     * Called during {@link IScene#hide()}.
     */
    @Override
    public void onHide() {
        for (ISceneBehavior child : children) {
            child.onHide();
        }
    }

    /**
     * Called during {@link IScene#resume()}.
     */
    @Override
    public void onEnable() {
        for (ISceneBehavior child : children) {
            child.onEnable();
        }
    }

    /**
     * Called during {@link IScene#pause()}, or {@link IScene#dispose()}.
     */
    @Override
    public void onDisable() {
        for (ISceneBehavior child : children) {
            child.onDisable();
        }
    }

    /**
     * Called during {@link IScene#dispose()}.
     * <p>
     * Called after {@link #onDestroy()}.
     */
    @Override
    public void onDestroy() {
        for (ISceneBehavior child : children) {
            child.onDestroy();
        }
        setScene(null);
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds a child behavior and sets its parent to this behavior.
     * <p>
     * 
     * @param child The {@link ISceneBehavior}.
     */
    protected void addComponent(ISceneBehavior child) {
        ((Behavior)child).setParent(this);
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

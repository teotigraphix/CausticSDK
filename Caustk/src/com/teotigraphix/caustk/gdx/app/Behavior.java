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

package com.teotigraphix.caustk.gdx.app;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

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

    public List<ISceneBehavior> getChildren() {
        return children;
    }

    private boolean visible = false;

    private boolean enabled = false;

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
     * {@link com.teotigraphix.caustk.gdx.app.IScene}.
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
    protected void onSceneChange(IScene screen) {
        super.onSceneChange(screen);
        attachChildren();
    }

    //----------------------------------
    // visible
    //----------------------------------

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible == this.visible)
            return;

        this.visible = visible;
        if (visible)
            onShow();
        else
            onHide();
    }

    //----------------------------------
    // enabled
    //----------------------------------

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled)
            return;

        this.enabled = enabled;
        if (enabled)
            onEnable();
        else
            onDisable();
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

    /**
     * Called during a scene change.
     */
    protected void attachChildren() {
    }

    protected void onParentChanged(Behavior parent) {
    }

    /**
     * Called once during {@link com.teotigraphix.caustk.gdx.app.Scene#create()}
     * , before {@link #onStart()} .
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
     * Called once during {@link com.teotigraphix.caustk.gdx.app.Scene#create()}
     * , after {@link #onAwake()}.
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
     * Called during {@link #setVisible(boolean)}.
     */
    @Override
    public void onShow() {
        for (ISceneBehavior child : children) {
            child.onShow();
        }
    }

    /**
     * Called during {@link #setVisible(boolean)}.
     */
    @Override
    public void onHide() {
        for (ISceneBehavior child : children) {
            child.onHide();
        }
    }

    /**
     * Called during {@link #setEnabled(boolean)}.
     */
    @Override
    public void onEnable() {
        for (ISceneBehavior child : children) {
            child.onEnable();
        }
    }

    /**
     * Called during {@link #setEnabled(boolean)}.
     */
    @Override
    public void onDisable() {
        for (ISceneBehavior child : children) {
            child.onDisable();
        }
    }

    /**
     * Called during {@link com.teotigraphix.caustk.gdx.app.IScene#pause()}, or
     * {@link com.teotigraphix.caustk.gdx.app.IScene#dispose()}.
     */
    @Override
    public void onPause() {
        for (ISceneBehavior child : children) {
            child.onPause();
        }
    }

    /**
     * Called during {@link com.teotigraphix.caustk.gdx.app.IScene#resume()}.
     */
    @Override
    public void onResume() {
        for (ISceneBehavior child : children) {
            child.onResume();
        }
    }

    /**
     * Called during {@link com.teotigraphix.caustk.gdx.app.IScene#dispose()}.
     * <p>
     * Called after {@link #onDestroy()}.
     */
    @Override
    public void onDestroy() {
        for (ISceneBehavior child : children) {
            child.onDestroy();
        }
    }

    // called right after onDestroy()
    @Override
    public void dispose() {
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

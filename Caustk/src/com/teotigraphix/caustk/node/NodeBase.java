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

package com.teotigraphix.caustk.node;

import com.teotigraphix.caustk.core.CaustkRack;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.core.osc.IOSCControl;
import com.teotigraphix.caustk.utils.ExceptionUtils;

/**
 * The base node for all caustk node types.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class NodeBase implements ICaustkNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private NodeInfo info = null;

    private String label = null;

    private Object icon = null;

    private Object color = null;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public NodeInfo getInfo() {
        return info;
    }

    /**
     * Sets the node's information metadata.
     * 
     * @param info The {@link NodeInfo} that describes this node.
     */
    public void setInfo(NodeInfo info) {
        this.info = info;
    }

    //----------------------------------
    // label
    //----------------------------------

    /**
     * The node's display label, this human readable name would be different
     * than a machine readable name.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the human readable display label.
     * 
     * @param label The display label.
     * @see NodeLabelEvent
     */
    public void setLabel(String label) {
        if (this.label.equals(label))
            return;
        this.label = label;
        post(new NodeLabelEvent(this, label));
    }

    //----------------------------------
    // icon
    //----------------------------------

    /**
     * The node's iconic visual display.
     */
    public Object getIcon() {
        return icon;
    }

    /**
     * Sets the node's iconic visual display.
     * <p>
     * Usually a skin part name or relative url to an image file, could also be
     * a tag identifier in an icon library.
     * 
     * @param icon The new icon.
     * @see NodeIconEvent
     */
    public void setIcon(Object icon) {
        if (icon == this.icon)
            return;
        this.icon = icon;
        post(new NodeIconEvent(this, icon));
    }

    //----------------------------------
    // color
    //----------------------------------

    /**
     * The node's RGB color assignment.
     * 
     * @return A Color object based on the application framework's
     *         implementation.
     */
    public Object getColor() {
        return color;
    }

    /**
     * Sets the color of the node.
     * 
     * @param color The color.
     * @see NodeColorEvent
     */
    public void setColor(Object color) {
        if (color == this.color)
            return;
        this.color = color;
        post(new NodeColorEvent(this, color));
    }

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the session {@link CaustkRack}.
     */
    protected final CaustkRack getRack() {
        return CaustkRuntime.getInstance().getRack();
    }

    /**
     * Returns the session {@link CaustkFactory}.
     */
    protected final CaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }

    /**
     * Returns the session {@link ICaustkLogger}.
     */
    protected final ICaustkLogger getLogger() {
        return CaustkRuntime.getInstance().getLogger();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public NodeBase() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * @see #createComponents()
     */
    @Override
    public final void create() {
        createComponents();
    }

    /**
     * @see #destroyComponents()
     */
    @Override
    public void destroy() {
        destroyComponents();
    }

    /**
     * @see #updateComponents()
     */
    @Override
    public final void update() {
        updateComponents();
    }

    /**
     * @see #restoreComponents()
     */
    @Override
    public final void restore() {
        restoreComponents();
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Create all node composite components.
     */
    protected abstract void createComponents();

    /**
     * Destroy all node composite components.
     */
    protected abstract void destroyComponents();

    /**
     * Update all node composite components.
     */
    protected abstract void updateComponents();

    /**
     * Restore all node composite components.
     */
    protected abstract void restoreComponents();

    /**
     * Posts a {@link NodeEvent} event to all subscribers of the
     * {@link CaustkRack#getEventBus()}.
     * 
     * @param event The {@link NodeEvent}.
     */
    protected void post(NodeEvent event) {
        getRack().getEventBus().post(event);
    }

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param control The OSC control involved.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param message The {@link CausticMessage} that caused the exception.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected RuntimeException newRangeException(CausticMessage message, String range, float value) {
        return ExceptionUtils.newRangeException(message.toString(), range, value);
    }

    /**
     * Base class for all {@link NodeBase} events posted through the
     * {@link CaustkRack#getEventBus()}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     * @see CaustkRack#getEventBus()
     */
    public static abstract class NodeEvent {

        private NodeBase target;

        private IOSCControl control;

        /**
         * Returns the {@link NodeBase} target that posted the event.
         */
        public final NodeBase getTarget() {
            return target;
        }

        /**
         * The {@link IOSCControl} OSC control that was sent to the native audio
         * core.
         */
        public IOSCControl getControl() {
            return control;
        }

        /**
         * Creates a {@link NodeEvent}.
         * 
         * @param target The {@link NodeBase} target that posted the event.
         */
        public NodeEvent(NodeBase target) {
            this.target = target;
        }

        public NodeEvent(NodeBase target, IOSCControl control) {
            this.target = target;
            this.control = control;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see NodeBase#setLabel(String)
     */
    public static class NodeLabelEvent extends NodeEvent {

        private String label;

        public String getLabel() {
            return label;
        }

        public NodeLabelEvent(NodeBase target, String label) {
            super(target);
            this.label = label;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see NodeBase#setIcon(Object)
     */
    public static class NodeIconEvent extends NodeEvent {

        private Object icon;

        public Object getIcon() {
            return icon;
        }

        public NodeIconEvent(NodeBase target, Object icon) {
            super(target);
            this.icon = icon;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see NodeBase#setColor(Object)
     */
    public static class NodeColorEvent extends NodeEvent {

        private Object color;

        public Object getColor() {
            return color;
        }

        public NodeColorEvent(NodeBase target, Object color) {
            super(target);
            this.color = color;
        }
    }
}

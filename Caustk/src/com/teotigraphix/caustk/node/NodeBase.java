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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkFactory;
import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeColorEvent;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeIconEvent;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeLabelEvent;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeSelectedEvent;
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

    @Tag(0)
    private NodeMetaData data = null;

    @Tag(1)
    private String label;

    @Tag(2)
    private Object icon;

    @Tag(3)
    private int color;

    @Tag(4)
    private boolean selected;

    //----------------------------------
    // data
    //----------------------------------

    protected void setData(NodeMetaData data) {
        this.data = data;
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // label
    //----------------------------------

    @Override
    public String getLabel() {
        if (data != null)
            return data.getLabel();
        return label;
    }

    /**
     * Sets the human readable display label.
     * 
     * @param label The display label.
     * @see NodeLabelEvent
     */
    public void setLabel(String label) {
        if (data != null) {
            final String oldLabel = data.getLabel();
            if (oldLabel != null && oldLabel.equals(label))
                return;
            data.setLabel(label);
        } else {
            this.label = label;
        }
        post(new NodeLabelEvent(this, label));
    }

    //----------------------------------
    // icon
    //----------------------------------

    /**
     * The node's iconic visual display.
     */
    public Object getIcon() {
        if (data != null)
            return data.getIcon();
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
        if (data != null) {
            if (icon == data.getIcon())
                return;
            data.setIcon(icon);
        } else {
            this.icon = icon;
        }
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
    public int getColor() {
        if (data != null)
            return data.getColor();
        return color;
    }

    /**
     * Sets the color of the node.
     * 
     * @param color The color.
     * @see NodeColorEvent
     */
    public void setColor(int color) {
        if (data != null) {
            if (color == data.getColor())
                return;
            data.setColor(color);
        } else {
            this.color = color;
        }
        post(new NodeColorEvent(this, color));
    }

    //----------------------------------
    // color
    //----------------------------------

    /**
     * Whether this node is selected.
     */
    public boolean isSelected() {
        if (data != null)
            return data.isSelected();
        return selected;
    }

    /**
     * Sets the selected state of this node.
     * 
     * @param selected The selected state.
     */
    public void setSelected(boolean selected) {
        if (data != null) {
            if (selected == data.isSelected())
                return;
            data.setSelected(selected);
        } else {
            this.selected = selected;
        }
        post(new NodeSelectedEvent(this, selected));
    }

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the session {@link com.teotigraphix.caustk.core.ICaustkRack}.
     */
    protected final ICaustkRack getRack() {
        return CaustkRuntime.getInstance().getRack();
    }

    /**
     * Returns the session {@link com.teotigraphix.caustk.core.ICaustkFactory}.
     */
    protected final ICaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }

    /**
     * Returns the session {@link com.teotigraphix.caustk.core.ICaustkLogger}.
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
    protected NodeBase() {
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
     * {@link com.teotigraphix.caustk.core.CaustkRack#getEventBus()}.
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
     * @param message The
     *            {@link com.teotigraphix.caustk.core.osc.CausticMessage} that
     *            caused the exception.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected RuntimeException newRangeException(CausticMessage message, String range, float value) {
        return ExceptionUtils.newRangeException(message.toString(), range, value);
    }
}

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

    protected Integer index = null;

    private NodeInfo info = null;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the index within the context of the node.
     * <p>
     * Some index values will point to machine index.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

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

        private CausticMessage message;

        /**
         * Returns the {@link NodeBase} target that posted the event.
         */
        public final NodeBase getTarget() {
            return target;
        }

        /**
         * The {@link CausticMessage} OSC message that was sent to the native
         * audio core.
         */
        public CausticMessage getMessage() {
            return message;
        }

        /**
         * Creates a {@link NodeEvent}.
         * 
         * @param target The {@link NodeBase} target that posted the event.
         */
        public NodeEvent(NodeBase target) {
            this.target = target;
        }

        public NodeEvent(NodeBase target, CausticMessage message) {
            this.target = target;
            this.message = message;
        }
    }
}

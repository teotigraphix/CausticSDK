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

import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.caustk.rack.RackProvider;
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

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the {@link RackProvider}'s {@link Rack} instance.
     */
    protected final Rack getRack() {
        return RackProvider.getRack();
    }

    /**
     * Returns the session {@link ICaustkLogger}.
     */
    protected final ICaustkLogger getLogger() {
        return RackProvider.getRack().getLogger();
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
}

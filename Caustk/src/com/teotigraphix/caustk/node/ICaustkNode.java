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

/**
 * The base node API for all caustk node types in the <code>nodes</code>
 * package.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see NodeType
 */
public interface ICaustkNode {

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the node's information metadata.
     * <p>
     * Can be <code>null</code>, the node will not serialize this field if
     * <code>null</code>.
     */
    NodeInfo getInfo();

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates all node components.
     * 
     * @see #createComponents()
     */
    void create();

    /**
     * Destroys all node components.
     * 
     * @see #destroyComponents()
     */
    void destroy();

    /**
     * Updates all node components.
     * 
     * @see #updateComponents()
     */
    void update();

    /**
     * Resotres all node components.
     * 
     * @see #restoreComponents()
     */
    void restore();
}

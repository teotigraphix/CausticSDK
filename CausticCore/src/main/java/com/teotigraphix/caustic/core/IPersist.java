////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.core;

/**
 * The {@link IPersist} API allows components to be serializable and provide a
 * means to pass mementos to children to load/save specific state.
 * <p>
 * The parent persistable always passes the tag to the child memento.
 * <p>
 * In {@link #copy(IMemento)}, the parent would say;
 * 
 * <pre>
 * child.copy(memento.createChild(&quot;child&quot;));
 * </pre>
 * <p>
 * In {@link #paste(IMemento)}, the parent would say;
 * 
 * <pre>
 * child.paste(memento.getChild(&quot;child&quot;));
 * </pre>
 * <p>
 * It is very important to keep this consistent. The child is aware of it's tag
 * name through the {@link IMemento#getName()}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPersist {
    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Copies the state of a persistable object into the passed {@link IMemento}
     * .
     * 
     * @param memento The {@link IMemento} the parent has given it's child to
     *            copy persistable data into.
     */
    void copy(IMemento memento);

    /**
     * Pastes the state of a persistable object from the passed {@link IMemento}
     * .
     * 
     * @param memento The {@link IMemento} the parent has given it's child to
     *            paste persistable data from.
     */
    void paste(IMemento memento);
}

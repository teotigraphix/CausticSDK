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

package com.teotigraphix.caustk.controller.core;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICausticLogger;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.controller.IRackComponent;

/**
 * Default implementation of the {@link IRackComponent} API.
 * 
 * @author Michael Schmalle
 */
public class RackComponent implements IRackComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private IRack rack;

    //--------------------------------------------------------------------------
    // IRackComponent API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public final IRack getRack() {
        return rack;
    }

    protected final ICausticLogger getLogger() {
        return rack.getLogger();
    }

    protected final IDispatcher getGlobalDispatcher() {
        return rack.getGlobalDispatcher();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public RackComponent() {
    }

    public RackComponent(IRack rack) {
        this.rack = rack;
    }

    //--------------------------------------------------------------------------
    // IRestore API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
    }

    //--------------------------------------------------------------------------
    // IRackComponent API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public void registerObservers() {
    }

    @Override
    public void unregisterObservers() {
    }
}

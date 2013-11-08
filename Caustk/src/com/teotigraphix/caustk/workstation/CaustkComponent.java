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

package com.teotigraphix.caustk.workstation;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;

/**
 * @author Michael Schmalle
 */
public abstract class CaustkComponent implements ICaustkComponent, IRackSerializer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public final ComponentInfo getInfo() {
        return info;
    }

    void setInfo(ComponentInfo info) {
        this.info = info;
    }

    @Override
    public abstract String getDefaultName();

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    public CaustkComponent() {
    }

    @Override
    public final void create(ICaustkApplicationContext context) throws CausticException {
        componentPhaseChange(context, ComponentPhase.Create);
    }

    @Override
    public final void load(ICaustkApplicationContext context) throws CausticException {
        componentPhaseChange(context, ComponentPhase.Load);
    }

    @Override
    public final void update(ICaustkApplicationContext context) {
        try {
            componentPhaseChange(context, ComponentPhase.Update);
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public final void restore() {
        try {
            componentPhaseChange(null, ComponentPhase.Restore);
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public final void disconnect() {
        try {
            componentPhaseChange(null, ComponentPhase.Disconnect);
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public enum ComponentPhase {
        Create,

        Load,

        Update,

        Restore,

        Disconnect
    }

    protected abstract void componentPhaseChange(ICaustkApplicationContext context,
            ComponentPhase phase) throws CausticException;

    @Override
    public void onLoad() {
    }

    @Override
    public void onSave() {
    }

}

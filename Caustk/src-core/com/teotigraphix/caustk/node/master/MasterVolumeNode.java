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

package com.teotigraphix.caustk.node.master;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage.MasterMixerControl;

/**
 * The master volume insert node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MasterVolumeNode extends MasterChildNode {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    @Tag(100)
    private float out = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // out
    //----------------------------------

    /**
     * @see MasterMixerMessage#VOLUME
     */
    public float getOut() {
        return out;
    }

    public float queryOut() {
        return MasterMixerMessage.VOLUME.query(getRack());
    }

    /**
     * @param out (0..2)
     * @see MasterMixerMessage#VOLUME
     */
    public void setOut(float out) {
        if (out == this.out)
            return;
        if (out < 0f || out > 2f)
            throw newRangeException(MasterMixerMessage.VOLUME, "0..2", out);
        this.out = out;
        MasterMixerMessage.VOLUME.send(getRack(), out);
        post(MasterMixerControl.Volume, out);
    }

    @Override
    CausticMessage getBypassMessage() {
        return MasterMixerMessage.VOLUME_BYPASS;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MasterVolumeNode() {
    }

    public MasterVolumeNode(MasterNode masterNode) {
        super(masterNode);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        MasterMixerMessage.VOLUME.send(getRack(), out);
    }

    @Override
    protected void restoreComponents() {
        setOut(queryOut());
    }
}

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
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.master.MasterNode.MasterNodeChangeEvent;

/**
 * The base node for all master node types.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MasterChildNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    CausticMessage getBypassMessage() {
        return null;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private MasterNode masterNode;

    @Tag(51)
    private boolean bypass = false;

    //----------------------------------
    // masterNode
    //----------------------------------

    public final MasterNode getMasterNode() {
        return masterNode;
    }

    //----------------------------------
    // bypass
    //----------------------------------

    public boolean isBypass() {
        return bypass;
    }

    boolean isBypass(boolean restore) {
        return getBypassMessage().query(getRack()) == 1 ? true : false;
    }

    public void setBypass(boolean value) {
        if (bypass == value)
            return;
        bypass = value;
        CausticMessage message = getBypassMessage();
        message.send(getRack(), value ? 1 : 0);
        MasterMixerControl control = null;
        if (message == MasterMixerMessage.DELAY_BYPASS)
            control = MasterMixerControl.DelayBypass;
        else if (message == MasterMixerMessage.REVERB_BYPASS)
            control = MasterMixerControl.ReverbBypass;
        else if (message == MasterMixerMessage.LIMITER_BYPASS)
            control = MasterMixerControl.LimiterBypass;
        post(control, value ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MasterChildNode() {
    }

    public MasterChildNode(MasterNode masterNode) {
        this.masterNode = masterNode;
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        getBypassMessage().send(getRack(), bypass ? 1 : 0);
    }

    @Override
    protected void restoreComponents() {
        setBypass(isBypass(true));
    }

    protected void post(MasterMixerControl control, float value) {
        post(new MasterNodeChangeEvent(this, control, value));
    }
}

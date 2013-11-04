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

package com.teotigraphix.caustk.rack.mixer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;

/**
 * @author Michael Schmalle
 */
public class MasterVolume extends RackMasterComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float out = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // out
    //----------------------------------

    public float getOut() {
        return out;
    }

    float getOut(boolean restore) {
        return MasterMixerMessage.VOLUME.query(getRack());
    }

    public void setOut(float value) {
        if (out == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("volume", "0..2", value);
        out = value;
        MasterMixerMessage.VOLUME.send(getRack(), value);
        //        fireChange(MasterMixerChangeKind.Volume, volume);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterVolume() {
        bypassMessage = MasterMixerMessage.VOLUME_BYPASS;
    }

    //--------------------------------------------------------------------------
    // IRackSerializer API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
        super.restore();
        // XXX OSC BUG        setOut(getOut(true));
    }

    @Override
    public void update(ICaustkApplicationContext context) {
        super.update(context);
        MasterMixerMessage.VOLUME.send(getRack(), out);
    }
}

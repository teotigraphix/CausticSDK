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

package com.teotigraphix.caustk.rack.tone.components.bassline;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.FilterMessage;
import com.teotigraphix.caustk.rack.tone.components.FilterComponentBase;

public class FilterComponent extends FilterComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float decay = 0f;

    @Tag(101)
    private float envMod = 0.99f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // decay
    //----------------------------------

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return FilterMessage.FILTER_DECAY.query(getEngine(), getToneIndex());
    }

    public void setDecay(float value) {
        if (value == decay)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("filter_decay", "0..1", value);
        decay = value;
        FilterMessage.FILTER_DECAY.send(getEngine(), getToneIndex(), decay);
    }

    //----------------------------------
    // envMod
    //----------------------------------

    public float getEnvMod() {
        return envMod;
    }

    float getEnvMod(boolean restore) {
        return FilterMessage.FILTER_ENVMOD.query(getEngine(), getToneIndex());
    }

    public void setEnvMod(float value) {
        if (value == envMod)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("filter_envmod", "0..1", value);
        envMod = value;
        FilterMessage.FILTER_ENVMOD.send(getEngine(), getToneIndex(), envMod);
    }

    public FilterComponent() {
    }

    @Override
    public void restore() {
        super.restore();
        setDecay(getDecay(true));
        setEnvMod(getEnvMod(true));
    }
}

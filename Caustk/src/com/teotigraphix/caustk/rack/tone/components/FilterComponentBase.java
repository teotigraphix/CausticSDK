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

package com.teotigraphix.caustk.rack.tone.components;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.FilterMessage;
import com.teotigraphix.caustk.rack.tone.ToneComponent;

public class FilterComponentBase extends ToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    protected float cutoff = 1.0f;

    @Tag(51)
    protected float resonance = 0f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    public float getCutoff() {
        return cutoff;
    }

    float getCutoff(boolean restore) {
        return FilterMessage.FILTER_CUTOFF.query(getEngine(), getToneIndex());
    }

    public void setCutoff(float value) {
        if (value == cutoff)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(FilterMessage.FILTER_CUTOFF.toString(), "0..1", value);
        cutoff = value;
        FilterMessage.FILTER_CUTOFF.send(getEngine(), getToneIndex(), cutoff);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    public float getResonance() {
        return resonance;
    }

    float getResonance(boolean restore) {
        return FilterMessage.FILTER_RESONANCE.query(getEngine(), getToneIndex());
    }

    public void setResonance(float value) {
        if (value == resonance)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(FilterMessage.FILTER_RESONANCE.toString(), "0..1", value);
        resonance = value;
        FilterMessage.FILTER_RESONANCE.send(getEngine(), getToneIndex(), resonance);
    }

    public FilterComponentBase() {
    }

    @Override
    public void restore() {
        setCutoff(getCutoff(true));
        setResonance(getResonance(true));
    }
}

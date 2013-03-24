////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.machine;

import com.teotigraphix.caustic.filter.IFilter;
import com.teotigraphix.caustic.filter.IPCMSynthLFO1;
import com.teotigraphix.caustic.filter.IPitchTuner;
import com.teotigraphix.caustic.filter.IVolumeEnvelope;
import com.teotigraphix.caustic.sampler.IPCMSampler;

/**
 * The IPCMSynth interface creates a highly sophisticated sampling synth that
 * has an inboard pattern sequencer.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPCMSynth extends ISynth
{

    /**
     * Returns the {@link IPCMSynth}s implemented {@link IVolumeEnvelope}
     * component.
     */
    IVolumeEnvelope getVolume();

    /**
     * Returns the {@link IPCMSynth}s implemented {@link IFilter} component.
     */
    IFilter getFilter();

    /**
     * Returns the {@link IPCMSynth}s implemented {@link IPCMSynthLFO1}
     * component.
     */
    IPCMSynthLFO1 getLFO1();

    /**
     * Returns the {@link IPCMSynth}s implemented {@link IPitchTuner} component.
     */
    IPitchTuner getPitch();

    /**
     * Returns the {@link IPCMSynth}s implemented {@link IPCMSampler} component.
     */
    IPCMSampler getSampler();

}

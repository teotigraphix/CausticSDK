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

package com.teotigraphix.caustic.sampler;

import com.teotigraphix.caustic.machine.IMachineComponent;

/**
 * The {@link IBeatboxSampler} interface manages the beatbox's sample load and
 * sound qualities.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBeatboxSampler extends IMachineComponent {

    /**
     * Returns the specified beatbox channel.
     * 
     * @param index The index of the beatbox channel.
     */
    IBeatboxSamplerChannel getChannel(int index);

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the name of the sample loaded at the specified channel.
     * <p>
     * Note; This only returns the name of the .wav file that was originally
     * loaded into the sampler and does not contain ".wav" at the end.
     * 
     * @param channel The sampler channel to query against.
     */
    String getSampleName(int channel);

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Loads a WAV file from the specified path into the sample index channel.
     * 
     * @param index The sample channel to load.
     * @param path The full path to the WAV file.
     */
    IBeatboxSamplerChannel loadChannel(int index, String path);
}

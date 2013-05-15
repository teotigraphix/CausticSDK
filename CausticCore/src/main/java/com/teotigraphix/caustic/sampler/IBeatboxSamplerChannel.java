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
 * The {@link IBeatboxSampler} manages on channel within the
 * {@link IBeatboxSampler}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBeatboxSamplerChannel extends IMachineComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    /**
     * Whether the sample has a loaded .wav file.
     */
    boolean hasSample();

    /**
     * The index of the channel within it's parent {@link IBeatboxSampler}.
     */
    int getIndex();

    /**
     * The channels sample name.
     */
    String getName();

    //----------------------------------
    // selected
    //----------------------------------

    /**
     * Whether the sample on channel index is selected (current channel).
     * 
     * @param index The sample channel to select.
     */
    boolean isSelected();

    /**
     * @see #isSelected()
     */
    void setSelected(boolean value);

    //----------------------------------
    // mute
    //----------------------------------

    /**
     * Whether all channels are muted.
     */
    boolean isMute();

    /**
     * Whether the sample on channel index is muted or sounding.
     * <p>
     * Values; (0,1) default; 0
     * 
     * @param index The sample channel to edit.
     */
    void setMute(boolean value);

    //----------------------------------
    // solo
    //----------------------------------

    /**
     * Whether all channels are soloed.
     */
    boolean isSolo();

    /**
     * Whether the sample on channel index is soloed or sounding.
     * <p>
     * Values; (0,1) default; 0
     * 
     * @param index The sample channel to edit.
     */
    void setSolo(boolean value);

    //----------------------------------
    // tune
    //----------------------------------

    /**
     * The sample on channel index's tune setting.
     * <p>
     * Values; (-6..6) default; 0
     * 
     * @param index The sample channel to edit.
     */
    float getTune();

    /**
     * @see #getTune(int)
     */
    void setTune(float value);

    //----------------------------------
    // punch
    //----------------------------------

    /**
     * The sample on channel index's punch (attack) setting.
     * <p>
     * Values; (0..1) default; 1
     * 
     * @param index The sample channel to edit.
     */
    float getPunch();

    /**
     * @see #getPunch(int)
     */
    void setPunch(float value);

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * The sample on channel index's decay setting.
     * <p>
     * Values; (0..1) default; 1
     * 
     * @param index The sample channel to edit.
     */
    float getDecay();

    /**
     * @see #getDecay(int)
     */
    void setDecay(float value);

    //----------------------------------
    // pan
    //----------------------------------

    /**
     * The sample on channel index's pan setting.
     * <p>
     * Values; (-1..1) default; 0
     * 
     * @param index The sample channel to edit.
     */
    float getPan();

    /**
     * @see #getPan(int)
     */
    void setPan(float value);

    //----------------------------------
    // volume
    //----------------------------------

    /**
     * The sample on channel index's volume setting.
     * <p>
     * Values; (0..2) default; 1
     * 
     * @param index The sample channel to edit.
     */
    float getVolume();

    /**
     * @see #getVolume(int)
     */
    void setVolume(float value);
}

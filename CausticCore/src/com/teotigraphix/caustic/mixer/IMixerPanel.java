////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.mixer;

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.effect.IEffectComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRackAware;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * The {@link IMixerPanel} interface controls the master mixer which mixes all
 * {@link IMachine}s and their {@link IEffectComponent}s in the current session.
 * <p>
 * The {@link IRack} implementation usually creates and manages this instance.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IMixerPanel extends IDevice, IPersist, IRackAware
{

    /**
     * The device name.
     */
    public static final String REVERB_ID = "reverb";

    /**
     * The <code>room</code> control.
     */
    public static final String CONTROL_ROOM = "room";

    /**
     * The <code>damping</code> control.
     */
    public static final String CONTROL_DAMPING = "damping";

    /**
     * The <code>stereo</code> control.
     */
    public static final String CONTROL_STEREO = "stereo";

    /**
     * The <code>eq_bass</code> control.
     */
    public static final String CONTROL_EQ_BASS = "eq_bass";

    /**
     * The <code>eq_mid</code> control.
     */
    public static final String CONTROL_EQ_MID = "eq_mid";

    /**
     * The <code>eq_high</code> control.
     */
    public static final String CONTROL_EQ_HIGH = "eq_high";

    /**
     * The <code>delay_send</code> control.
     */
    public static final String CONTROL_DELAY_SEND = "delay_send";

    /**
     * The <code>reverb_send</code> control.
     */
    public static final String CONTROL_REVERB_SEND = "reverb_send";

    /**
     * The <code>pan</code> control.
     */
    public static final String CONTROL_PAN = "pan";

    /**
     * The <code>stereo_width</code> control.
     */
    public static final String CONTROL_STEREO_WIDTH = "stereo_width";

    /**
     * The <code>mute</code> control.
     */
    public static final String CONTROL_MUTE = "mute";

    /**
     * The <code>solo</code> control.
     */
    public static final String CONTROL_SOLO = "solo";

    /**
     * The <code>volume</code> control.
     */
    public static final String CONTROL_VOLUME = "volume";

    public static final String CHANNEL_MASTER = "master";

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // delay
    //----------------------------------

    /**
     * The mixer delay effect.
     */
    IMixerDelay getDelay();

    /**
     * @see #getDelay()
     */
    void setDelay(IMixerDelay value);

    //----------------------------------
    // reverb
    //----------------------------------

    /**
     * The mixer reverb effect.
     */
    IMixerReverb getReverb();

    /**
     * @see #getReverb()
     */
    void setReverb(IMixerReverb value);

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // Master
    //----------------------------------

    /**
     * Returns the master channels bass level.
     */
    float getMasterBass();

    /**
     * Sets the master channels bass level.
     */
    void setMasterBass(float value);

    /**
     * Returns the master channels mid level.
     */
    float getMasterMid();

    /**
     * Sets the master channels mid level.
     */
    void setMasterMid(float value);

    /**
     * Returns the master channels high level.
     */
    float getMasterHigh();

    /**
     * Sets the master channels high level.
     */
    void setMasterHigh(float value);

    /**
     * Returns the master channels volume level.
     */
    float getMasterVolume();

    /**
     * Sets the master channels volume level.
     */
    void setMasterVolume(float value);

    //----------------------------------
    // Bass
    //----------------------------------

    /**
     * Adjusts the bass range send level on the {@link IDevice} using it's name.
     * <p>
     * Default; 0, Range (0..1)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's delay send level.
     * @param amount The amount of bass for the {@link IDevice}. (0..1)
     */
    void setBass(int index, float amount);

    /**
     * Returns the current bass send set by {@link #setBass(int, float)} by
     * {@link IDevice#getId()}.
     * 
     * @param index The {@link IDevice} to retrieve bass send for.
     */
    float getBass(int index);

    //----------------------------------
    // Mid
    //----------------------------------

    /**
     * Adjusts the mid range send level on the {@link IDevice} using it's name.
     * <p>
     * Default; 0, Range (0..1)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's mid range send level.
     * @param amount The amount of mid range for the {@link IDevice}. (0..1)
     */
    void setMid(int index, float amount);

    /**
     * Returns the current mid range send set by {@link #setMid(int, float)} by
     * {@link IDevice#getId()}.
     * 
     * @param index The {@link IDevice} to retrieve mid range send for.
     */
    float getMid(int index);

    //----------------------------------
    // High
    //----------------------------------

    /**
     * Adjusts the high range send level on the {@link IDevice} using it's name.
     * <p>
     * Default; 0, Range (0..1)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's high range send level.
     * @param amount The amount of high range for the {@link IDevice}. (0..1)
     */
    void setHigh(int index, float amount);

    /**
     * Returns the current high range send set by {@link #setHigh(int, float)}
     * by {@link IDevice#getId()}.
     * 
     * @param index The {@link IDevice} to retrieve high range send for.
     */
    float getHigh(int index);

    //----------------------------------
    // Delay
    //----------------------------------

    /**
     * Adjusts the delay send level on the {@link IDevice} using it's name and
     * setting the send level base on the amount.
     * <p>
     * Default; 0, Range (0..1)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's delay send level.
     * @param amount The amount of delay for the {@link IDevice}. (0..1)
     */
    void setDelaySend(int index, float amount);

    /**
     * Returns the current delay send set by {@link #setDelaySend(int, float)}
     * by {@link IDevice#getId()}.
     * 
     * @param index The {@link IDevice} to retrieve delay send for.
     */
    float getDelaySend(int index);

    //----------------------------------
    // Reverb
    //----------------------------------

    /**
     * Adjusts the reverb send level on the {@link IDevice} using it's name and
     * setting the send level base on the amount.
     * <p>
     * Default; 0, Range (0..1)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's reverb send level.
     * @param amount The amount of reverb for the {@link IDevice}. (0..1)
     */
    void setReverbSend(int index, float amount);

    /**
     * Returns the current reverb send set by {@link #setReverbSend(int, float)}
     * by {@link IDevice#getId()}.
     * 
     * @param index The {@link IDevice} to retrieve reverb send for.
     */
    float getReverbSend(int index);

    //----------------------------------
    // Pan
    //----------------------------------

    /**
     * Adjusts the machines pan setting.
     * <p>
     * Default; 0, Range (-1..1)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's pan level.
     * @param amount 0 is default (-1..1)
     */
    void setPan(int index, float amount);

    /**
     * Returns the current pan setting set by {@link #setPan(int, float)} by
     * {@link IDevice#getId()}.
     * 
     * @param index The {@link IDevice} to retrieve pan setting for.
     */
    float getPan(int index);

    //----------------------------------
    // Stereo Width
    //----------------------------------

    /**
     * Adjusts the machines stereo width.
     * <p>
     * Default; 0, Range (0..1)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's stereo width level.
     * @param amount 0 is default (0..1)
     */
    void setStereoWidth(int index, float amount);

    /**
     * Returns the current stereo width send set by
     * {@link #setStereoWidth(int, float)} by {@link IDevice#getId()}.
     * 
     * @param index The {@link IDevice} to retrieve stereo width send for.
     */
    float getStereoWidth(int index);

    //----------------------------------
    // mute
    //----------------------------------

    /**
     * Sets a machine muted (out of mix) or unmuted (in mix).
     * 
     * @param index The {@link IDevice} to set it's mute status.
     * @param value false is default.
     */
    void setMute(int index, boolean value);

    /**
     * Returns the current mute status set by {@link #setMute(int, boolean)}.
     * 
     * @param index The {@link IDevice} to set it's mute status.
     */
    boolean isMute(int index);

    //----------------------------------
    // solo
    //----------------------------------

    /**
     * Sets a machine soloed.
     * 
     * @param index The {@link IDevice} to set it's soloed status.
     * @param value false is default.
     */
    void setSolo(int index, boolean value);

    /**
     * Returns the current solo status set by {@link #setSolo(int, boolean)}.
     * 
     * @param index The {@link IDevice} to set it's mute status.
     */
    boolean isSolo(int index);

    //----------------------------------
    // Volume
    //----------------------------------

    /**
     * Adjusts the machines volume level.
     * <p>
     * Default; 1, Range (0..2)
     * </p>
     * 
     * @param index The {@link IDevice} to adjust it's volume level.
     * @param amount 1 is default (0..2)
     */
    void setVolume(int index, float amount);

    /**
     * Returns the current volume level set by {@link #setVolume(int, float)}.
     * 
     * @param index The {@link IDevice} to retrieve volume level for.
     */
    float getVolume(int index);

    void copyChannel(IMachine machine, IMemento memento);

    void pasteChannel(IMachine machine, IMemento memento);
}

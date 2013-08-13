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

package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.sound.master.MasterDelay;
import com.teotigraphix.caustk.sound.master.MasterEqualizer;
import com.teotigraphix.caustk.sound.master.MasterLimiter;
import com.teotigraphix.caustk.sound.master.MasterReverb;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public class MasterMixer implements ISerialize, IRestore {

    private transient ICaustkController controller;

    public void setController(ICaustkController controller) {
        this.controller = controller;
    }

    private ICausticEngine getEngine() {
        return controller;
    }

    //----------------------------------
    // equalizer
    //----------------------------------

    private MasterEqualizer equalizer;

    public final MasterEqualizer getEqualizer() {
        return equalizer;
    }

    public final void setEqualizer(MasterEqualizer value) {
        equalizer = value;
    }

    //----------------------------------
    // limiter
    //----------------------------------

    private MasterLimiter limiter;

    public final MasterLimiter getLimiter() {
        return limiter;
    }

    public final void setLimiter(MasterLimiter value) {
        limiter = value;
    }

    //----------------------------------
    // delay
    //----------------------------------

    private MasterDelay delay;

    public final MasterDelay getDelay() {
        return delay;
    }

    public final void setDelay(MasterDelay value) {
        delay = value;
    }

    //----------------------------------
    // reverb
    //----------------------------------

    private MasterReverb reverb;

    public final MasterReverb getReverb() {
        return reverb;
    }

    public final void setReverb(MasterReverb value) {
        reverb = value;
    }

    //----------------------------------
    // volume
    //----------------------------------

    private float volume = 1f;

    public float getVolume() {
        return volume;
    }

    float getVolume(boolean restore) {
        return MasterMixerMessage.VOLUME.query(getEngine());
    }

    public void setVolume(float value) {
        if (volume == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("volume", "0..2", value);
        volume = value;
        MasterMixerMessage.VOLUME.send(getEngine(), value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterMixer() {
    }

    public MasterMixer(ICaustkController controller) {
        this.controller = controller;

        equalizer = new MasterEqualizer(controller);
        limiter = new MasterLimiter(controller);
        delay = new MasterDelay(controller);
        reverb = new MasterReverb(controller);
    }

    @Override
    public void restore() {
        setVolume(getVolume(true));
        equalizer.restore();
        limiter.restore();
        delay.restore();
        reverb.restore();
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        equalizer.setController(controller);
        limiter.setController(controller);
        delay.setController(controller);
        reverb.setController(controller);
    }

    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }

}

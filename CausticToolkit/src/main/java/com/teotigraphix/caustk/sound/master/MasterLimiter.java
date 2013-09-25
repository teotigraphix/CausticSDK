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

package com.teotigraphix.caustk.sound.master;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;

public class MasterLimiter extends MasterComponent {

    private static final long serialVersionUID = 7366612792215282397L;

    //--------------------------------------------------------------------------
    // API
    //--------------------------------------------------------------------------

    //----------------------------------
    // pre
    //----------------------------------

    private float pre = 1f;

    public float getPre() {
        return pre;
    }

    float getPre(boolean restore) {
        return MasterMixerMessage.LIMITER_PRE.query(getEngine());
    }

    public void setPre(float value) {
        if (pre == value)
            return;
        if (value < 0f || value > 8f)
            throw newRangeException("pre", "0..8", value);
        pre = value;
        MasterMixerMessage.LIMITER_PRE.send(getEngine(), value);
    }

    //----------------------------------
    // attack
    //----------------------------------

    private float attack = 0.02f;

    public float getAttack() {
        return attack;
    }

    float getAttack(boolean restore) {
        return MasterMixerMessage.LIMITER_ATTACK.query(getEngine());
    }

    public void setAttack(float value) {
        if (attack == value)
            return;
        if (value < 0f || value > 0.1f)
            throw newRangeException("attack", "0..0.1", value);
        attack = value;
        MasterMixerMessage.LIMITER_ATTACK.send(getEngine(), value);
    }

    //----------------------------------
    // release
    //----------------------------------

    private float release = 0.25f;

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return MasterMixerMessage.LIMITER_RELEASE.query(getEngine());
    }

    public void setRelease(float value) {
        if (release == value)
            return;
        if (value < 0f || value > 0.5f)
            throw newRangeException("release", "0..0.5", value);
        release = value;
        MasterMixerMessage.LIMITER_RELEASE.send(getEngine(), value);
    }

    //----------------------------------
    // post
    //----------------------------------

    private float post = 1f;

    public float getPost() {
        return post;
    }

    float getPost(boolean restore) {
        return MasterMixerMessage.LIMITER_POST.query(getEngine());
    }

    public void setPost(float value) {
        if (post == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("post", "0..2", value);
        post = value;
        MasterMixerMessage.LIMITER_POST.send(getEngine(), value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterLimiter() {
        bypassMessage = MasterMixerMessage.LIMITER_BYPASS;
    }

    public MasterLimiter(ICaustkController controller) {
        super(controller);
        bypassMessage = MasterMixerMessage.LIMITER_BYPASS;
    }

    @Override
    public void restore() {
        super.restore();
        setAttack(getAttack(true));
        setPost(getPost(true));
        setPre(getPre(true));
        setRelease(getRelease(true));
    }
}

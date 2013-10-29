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
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;

public class MasterLimiter extends MasterComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float pre = 1f;

    @Tag(101)
    private float attack = 0.02f;

    @Tag(102)
    private float release = 0.25f;

    @Tag(103)
    private float post = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // pre
    //----------------------------------

    public float getPre() {
        return pre;
    }

    float getPre(boolean restore) {
        return MasterMixerMessage.LIMITER_PRE.query(rack);
    }

    public void setPre(float value) {
        if (pre == value)
            return;
        if (value < 0f || value > 8f)
            throw newRangeException("pre", "0..8", value);
        pre = value;
        MasterMixerMessage.LIMITER_PRE.send(rack, value);
    }

    //----------------------------------
    // attack
    //----------------------------------

    public float getAttack() {
        return attack;
    }

    float getAttack(boolean restore) {
        return MasterMixerMessage.LIMITER_ATTACK.query(rack);
    }

    public void setAttack(float value) {
        if (attack == value)
            return;
        if (value < 0f || value > 0.1f)
            throw newRangeException("attack", "0..0.1", value);
        attack = value;
        MasterMixerMessage.LIMITER_ATTACK.send(rack, value);
    }

    //----------------------------------
    // release
    //----------------------------------

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return MasterMixerMessage.LIMITER_RELEASE.query(rack);
    }

    public void setRelease(float value) {
        if (release == value)
            return;
        if (value < 0f || value > 0.5f)
            throw newRangeException("release", "0..0.5", value);
        release = value;
        MasterMixerMessage.LIMITER_RELEASE.send(rack, value);
    }

    //----------------------------------
    // post
    //----------------------------------

    public float getPost() {
        return post;
    }

    float getPost(boolean restore) {
        return MasterMixerMessage.LIMITER_POST.query(rack);
    }

    public void setPost(float value) {
        if (post == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("post", "0..2", value);
        post = value;
        MasterMixerMessage.LIMITER_POST.send(rack, value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterLimiter() {
        bypassMessage = MasterMixerMessage.LIMITER_BYPASS;
    }

    //--------------------------------------------------------------------------
    // IRackSerializer API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
        super.restore();
        setAttack(getAttack(true));
        setPost(getPost(true));
        setPre(getPre(true));
        setRelease(getRelease(true));
    }

    @Override
    public void update() {
        super.update();
        MasterMixerMessage.LIMITER_ATTACK.send(rack, attack);
        MasterMixerMessage.LIMITER_POST.send(rack, post);
        MasterMixerMessage.LIMITER_PRE.send(rack, pre);
        MasterMixerMessage.LIMITER_RELEASE.send(rack, release);
    }
}

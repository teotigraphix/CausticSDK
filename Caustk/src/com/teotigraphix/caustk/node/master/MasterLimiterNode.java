////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.master;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage.MasterMixerControl;

/**
 * The master limiter insert node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MasterLimiterNode extends MasterChildNode {

    //--------------------------------------------------------------------------
    // Private :: Variables
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

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_PRE
     */
    public float getPre() {
        return pre;
    }

    public float queryPre() {
        return MasterMixerMessage.LIMITER_PRE.query(getRack());
    }

    /**
     * @param pre (0..8)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_PRE
     */
    public void setPre(float pre) {
        if (pre == this.pre)
            return;
        if (pre < 0f || pre > 8f)
            throw newRangeException(MasterMixerMessage.LIMITER_PRE, "0..8", pre);
        this.pre = pre;
        MasterMixerMessage.LIMITER_PRE.send(getRack(), pre);
        post(MasterMixerControl.LimiterPre, pre);
    }

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_ATTACK
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return MasterMixerMessage.LIMITER_ATTACK.query(getRack());
    }

    /**
     * @param attack (0..0.1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_ATTACK
     */
    public void setAttack(float attack) {
        if (attack == this.attack)
            return;
        if (attack < 0f || attack > 0.1f)
            throw newRangeException("attack", "0..0.1", attack);
        this.attack = attack;
        MasterMixerMessage.LIMITER_ATTACK.send(getRack(), attack);
        post(MasterMixerControl.LimiterAttack, attack);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_RELEASE
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return MasterMixerMessage.LIMITER_RELEASE.query(getRack());
    }

    /**
     * @param release (0..0.5)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_RELEASE
     */
    public void setRelease(float release) {
        if (release == this.release)
            return;
        if (release < 0f || release > 0.5f)
            throw newRangeException(MasterMixerMessage.LIMITER_RELEASE, "0..0.5", release);
        this.release = release;
        MasterMixerMessage.LIMITER_RELEASE.send(getRack(), release);
        post(MasterMixerControl.LimiterRelease, release);
    }

    //----------------------------------
    // post
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_POST
     */
    public float getPost() {
        return post;
    }

    public float queryPost() {
        return MasterMixerMessage.LIMITER_POST.query(getRack());
    }

    /**
     * @param post (0..2)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#LIMITER_POST
     */
    public void setPost(float post) {
        if (post == this.post)
            return;
        if (post < 0f || post > 2f)
            throw newRangeException(MasterMixerMessage.LIMITER_POST, "0..2", post);
        this.post = post;
        MasterMixerMessage.LIMITER_POST.send(getRack(), post);
        post(MasterMixerControl.LimiterPost, post);
    }

    @Override
    CausticMessage getBypassMessage() {
        return MasterMixerMessage.LIMITER_BYPASS;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MasterLimiterNode() {
    }

    public MasterLimiterNode(MasterChannel masterNode) {
        super(masterNode);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        MasterMixerMessage.LIMITER_ATTACK.send(getRack(), attack);
        MasterMixerMessage.LIMITER_POST.send(getRack(), post);
        MasterMixerMessage.LIMITER_PRE.send(getRack(), pre);
        MasterMixerMessage.LIMITER_RELEASE.send(getRack(), release);
    }

    @Override
    protected void restoreComponents() {
        setAttack(queryAttack());
        setPost(queryPost());
        setPre(queryPre());
        setRelease(queryRelease());
    }

    @Override
    public String toString() {
        return "MasterLimiterNode [pre=" + pre + ", attack=" + attack + ", release=" + release
                + ", post=" + post + ", isBypass=" + isBypass() + "]";
    }

}

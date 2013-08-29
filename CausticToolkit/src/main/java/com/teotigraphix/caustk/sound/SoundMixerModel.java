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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ControllerComponentState;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.tone.Tone;


/**
 * Serialized - v1.0
 * <ul>
 * <li><code>masterMixer</code> - A serialized {@link MasterMixer}.</li>
 * <li><code>selectedLibraryId</code> - The current {@link Library} {@link UUID}
 * </li>
 * </ul>
 */
public class SoundMixerModel extends ControllerComponentState implements IRestore {

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // masterMixer
    //----------------------------------

    private MasterMixer masterMixer;

    public MasterMixer getMasterMixer() {
        return masterMixer;
    }

    //----------------------------------
    // channels
    //----------------------------------

    Map<Integer, SoundMixerChannel> channels = new HashMap<Integer, SoundMixerChannel>();

    Map<Integer, SoundMixerChannel> getChannels() {
        return channels;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public SoundMixerModel() {
    }

    public SoundMixerModel(ICaustkController controller) {
        super(controller);
        masterMixer = new MasterMixer(controller);
    }

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    public void update() {
        for (SoundMixerChannel channel : channels.values()) {
            channel.update();
        }
    }

    void toneAdded(Tone tone) {
        SoundMixerChannel channel = new SoundMixerChannel(getController());
        channel.setIndex(tone.getIndex());
        channels.put(tone.getIndex(), channel);
    }

    void toneRemoved(Tone tone) {
        channels.remove(tone.getIndex());
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        super.wakeup(controller);
        masterMixer.setController(controller);
        for (SoundMixerChannel channel : channels.values()) {
            channel.wakeup(controller);
        }
    }

    @Override
    public void restore() {
        for (SoundMixerChannel channel : channels.values()) {
            channel.restore();
        }
    }

}

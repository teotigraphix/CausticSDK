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
import com.teotigraphix.caustk.controller.IControllerAPI;
import com.teotigraphix.caustk.sound.SoundMixer.SoundMixerSetSendCommand;

public class SoundMixerAPI implements IControllerAPI {

    private ICaustkController controller;

    private SoundMixer getSoundMixer() {
        return (SoundMixer)controller.getSoundMixer();
    }

    public SoundMixerAPI(ICaustkController controller) {
        this.controller = controller;
        commitCommands();
    }

    private void commitCommands() {
        controller.getCommandManager().put(SoundMixer.COMMAND_SET_SEND,
                SoundMixerSetSendCommand.class);
    }

    public float getMasterVolume() {
        return getSoundMixer().getMasterVolume();
    }

    public void setMasterVolume(float value) {
        getSoundMixer().setMasterVolume(value);
    }

    public float getMasterBass() {
        return getSoundMixer().getMasterBass();
    }

    public void setMasterBass(float value) {
        getSoundMixer().setMasterBass(value);
    }

    public float getMasterMid() {
        return getSoundMixer().getMasterMid();
    }

    public void setMasterMid(float value) {
        getSoundMixer().setMasterMid(value);
    }

    public float getMasterHigh() {
        return getSoundMixer().getMasterHigh();
    }

    public void setMasterHigh(float value) {
        getSoundMixer().setMasterHigh(value);
    }

    public float getDelaySend(int index) {
        return getSoundMixer().getDelaySend(index);
    }

    public void setDelaySend(int index, float value) {
        getSoundMixer().setDelaySend(index, value);
    }

    public float getReverbSend(int index) {
        return getSoundMixer().getReverbSend(index);
    }

    public void setReverbSend(int index, float value) {
        getSoundMixer().setReverbSend(index, value);
    }
}

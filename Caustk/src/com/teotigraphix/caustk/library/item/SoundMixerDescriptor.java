
package com.teotigraphix.caustk.library.item;

import com.teotigraphix.caustk.sound.mixer.MasterMixer;

public class SoundMixerDescriptor {

    private MasterMixer masterMixer;

    public MasterMixer getMasterMixer() {
        return masterMixer;
    }

    public void setMasterMixer(MasterMixer value) {
        masterMixer = value;
    }

    public SoundMixerDescriptor() {
    }

}

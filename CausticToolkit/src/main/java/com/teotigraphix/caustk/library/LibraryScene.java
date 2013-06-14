
package com.teotigraphix.caustk.library;

import com.teotigraphix.caustk.library.vo.EffectRackInfo;
import com.teotigraphix.caustk.library.vo.MixerPanelInfo;
import com.teotigraphix.caustk.library.vo.RackInfo;

public class LibraryScene extends LibraryItem {

    private RackInfo rackInfo;

    public RackInfo getRackInfo() {
        return rackInfo;
    }

    public void setRackInfo(RackInfo rackInfo) {
        this.rackInfo = rackInfo;
    }

    private MixerPanelInfo mixerInfo;

    public MixerPanelInfo getMixerInfo() {
        return mixerInfo;
    }

    public void setMixerInfo(MixerPanelInfo mixerInfo) {
        this.mixerInfo = mixerInfo;
    }

    private EffectRackInfo effectRackInfo;

    public EffectRackInfo getEffectRackInfo() {
        return effectRackInfo;
    }

    public void setEffectRackInfo(EffectRackInfo effectRackInfo) {
        this.effectRackInfo = effectRackInfo;
    }

    public LibraryScene() {
    }

}

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

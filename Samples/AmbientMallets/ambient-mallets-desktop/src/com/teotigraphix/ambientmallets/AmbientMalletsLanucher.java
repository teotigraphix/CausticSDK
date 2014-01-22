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

package com.teotigraphix.ambientmallets;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.teotigraphix.caustk.core.internal.DesktopSoundGenerator;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class AmbientMalletsLanucher {
    public static void main(String[] args) {
        System.out.println("AmbientMalletsLanucher: " + "main()");

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "AmbientMallets";
        cfg.useGL20 = false;
        cfg.width = 800;
        cfg.height = 400;
        cfg.resizable = true;

        System.out.println("AmbientMalletsLanucher: "
                + "create AmbientMallets(DesktopSoundGenerator)");
        AmbientMallets listener = new AmbientMallets(new DesktopSoundGenerator());

        System.out.println("AmbientMalletsLanucher: "
                + "lauch LwjglApplication(AmbientMallets, LwjglApplicationConfiguration)");
        new LwjglApplication(listener, cfg);
    }
}

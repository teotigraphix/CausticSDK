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

package com.teotigraphix.caustk.core.internal;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.jna.Native;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class CausticCoreDesktop {
    private static byte[] m_byResponseString;

    private static CausticLibrary caustic;

    public CausticCoreDesktop() {
        RuntimeUtils.STORAGE_ROOT = Constants.STORAGE_ROOT;

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("CausticCoreDesktop: Construct - relative path is: " + s);

        System.setProperty("jna.library.path", s + "\\libs");

        m_byResponseString = new byte[4096];
        caustic = (CausticLibrary)Native.loadLibrary("CausticCore.dll", CausticLibrary.class);
        caustic.CausticCore_Init(1024);
        caustic.CausticCore_SetStorageRootDir(Constants.STORAGE_ROOT);
        SendOSCMessage("/caustic/blankrack");
    }

    public static void reset() {
    }

    public float SendOSCMessage(String message) {
        CtkDebug.osc(message);
        return caustic.CausticCore_OSCMessage(message, null);
    }

    public String QueryOSC(String message) {
        m_byResponseString = new byte[4096];

        int nStrLen = (int)caustic.CausticCore_OSCMessage(message, m_byResponseString);
        return new String(m_byResponseString, 0, nStrLen);
    }

    public int getVersion() {
        return caustic.CausticCore_GetVersion();
    }

    public float getCurrentBeat() {
        return caustic.CausticCore_GetCurrentBeat();
    }

    public int getCurrentSongMeasure() {
        return caustic.CausticCore_GetCurrentSongMeasure();
    }

    public void deinit() {
        caustic.CausticCore_Deinit();
    }
}

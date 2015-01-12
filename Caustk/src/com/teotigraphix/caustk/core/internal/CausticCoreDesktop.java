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

import java.io.File;
import java.io.IOException;

import com.sun.jna.Native;
import com.teotigraphix.caustk.core.CaustkEngine;
import com.teotigraphix.caustk.core.generator.NativeUtils;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class CausticCoreDesktop {
    private static byte[] m_byResponseString;

    private static CausticLibrary caustic;

    private boolean isDisposed = false;

    public CausticCoreDesktop() {
        //Path currentRelativePath = Paths.get("");
        File currentRelativePath = new File("");
        String s = currentRelativePath.getAbsolutePath();
        System.out.println("CausticCoreDesktop: Construct - relative path is: " + s);

        File root;
        try {
            NativeUtils.loadLibraryFromJar("/CausticCore.lib", false);
            NativeUtils.loadLibraryFromJar("/fmodex.dll", false);
            root = NativeUtils.loadLibraryFromJar("/CausticCore.dll", true);

            //System.setProperty("jna.library.path", s + "\\libs");
            System.setProperty("jna.library.path", root.getParentFile().getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        m_byResponseString = new byte[16384];
        caustic = (CausticLibrary)Native.loadLibrary("CausticCore.dll", CausticLibrary.class);
        caustic.CausticCore_Init(1024);
        String storageRoot = RuntimeUtils.STORAGE_ROOT;
        System.out.println("CausticCoreDesktop: storage root : " + storageRoot);
        caustic.CausticCore_SetStorageRootDir(storageRoot);
        SendOSCMessage("/caustic/blankrack");
    }

    public static void reset() {
    }

    public float SendOSCMessage(String message) {
        if (isDisposed)
            return Float.NaN;

        if (CaustkEngine.DEBUG_MESSAGES) {
            System.out.println("Message: " + message);
        }
        return caustic.CausticCore_OSCMessage(message, null);
    }

    public String QueryOSC(String message) {
        if (isDisposed)
            return null;

        m_byResponseString = new byte[16384]; // 4096
        if (CaustkEngine.DEBUG_QUERIES) {
            System.out.println("Query: " + message);
        }
        int nStrLen = (int)caustic.CausticCore_OSCMessage(message, m_byResponseString);
        try {
            return new String(m_byResponseString, 0, nStrLen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        isDisposed = true;
        caustic.CausticCore_Deinit();
    }
}

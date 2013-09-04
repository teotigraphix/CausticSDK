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

import com.sun.jna.Library;

public interface CausticLibrary extends Library {
    void CausticCore_Init(int nBufferSize);

    void CausticCore_Deinit();

    void CausticCore_SetStorageRootDir(String path);

    float CausticCore_OSCMessage(String message, byte[] response);

    float CausticCore_GetCurrentBeat();

    int CausticCore_GetCurrentSongMeasure();

    /**
     * return a 32 bit value where the version number is split into 4 parts, 8
     * bits each
     * <ul>
     * <li>bits [24..31] are major version</li>
     * <li>bits [16..23] are minor version</li>
     * <li>bits [8..15] are release</li>
     * <li>bits [0..7] are build # (though I doubt you'll ever see that change
     * for OSC)</li>
     * </ul>
     * <p>
     * For example right now it returns 0x03000000 for 3.0.0.0
     */
    int CausticCore_GetVersion();
}

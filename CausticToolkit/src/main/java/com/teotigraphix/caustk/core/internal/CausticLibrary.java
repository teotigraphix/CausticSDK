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

    int CausticCore_GetCurrentBeat();

    int CausticCore_GetCurrentSongMeasure();
}


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

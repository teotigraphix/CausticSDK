
package com.teotigraphix.caustk.core.internal;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import com.sun.jna.Native;
import com.teotigraphix.caustk.core.CausticEventListener;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class CausticCoreDesktop {
    private static byte[] m_byResponseString;

    private static CausticLibrary caustic;

    private static CausticAudioMonitor audioThread;

    public CausticCoreDesktop() {
        RuntimeUtils.STORAGE_ROOT = Constants.STORAGE_ROOT;

        URL resource = CausticCoreDesktop.class.getClassLoader().getResource(
                "com/teotigraphix/caustk/core/internal/CausticCore.dll");
        String path = null;
        try {
            path = new File(resource.toURI()).getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.setProperty("jna.library.path", path);

        m_byResponseString = new byte[4096];

        caustic = (CausticLibrary)Native.loadLibrary("CausticCore.dll", CausticLibrary.class);
        caustic.CausticCore_Init(1024);
        caustic.CausticCore_SetStorageRootDir(Constants.STORAGE_ROOT);
        SendOSCMessage("/caustic/blankrack");

        audioThread = new CausticAudioMonitor();
        audioThread.start();
        audioThread.m_bProcess = true;
    }

    public static void reset() {
        if (audioThread != null)
            audioThread.m_EventListeners.clear();
    }

    public float SendOSCMessage(String message) {
        return caustic.CausticCore_OSCMessage(message, null);
    }

    public String QueryOSC(String message) {
        m_byResponseString = new byte[4096];

        int nStrLen = (int)caustic.CausticCore_OSCMessage(message, m_byResponseString);
        return new String(m_byResponseString, 0, nStrLen);
    }

    public int GetCurrentBeat() {
        return caustic.CausticCore_GetCurrentBeat();
    }

    public int GetCurrentSongMeasure() {
        return caustic.CausticCore_GetCurrentSongMeasure();
    }

    public void CausticCore_Deinit() {
        audioThread.m_bProcess = false;
        audioThread.m_bRun = false;
        audioThread.m_EventListeners.clear();
        caustic.CausticCore_Deinit();
    }

    public void addEventListener(CausticEventListener l) {
        audioThread.AddEventListener(l);
    }

    public void removeEventListener(CausticEventListener l) {
        audioThread.RemoveEventListener(l);
    }

    class CausticAudioMonitor extends Thread {
        CausticAudioMonitor() {
            m_nCurrentBeat = 0;
            m_nCurrentMeasure = -1;
            m_bRun = true;
            m_bProcess = false;
            m_EventListeners = new ArrayList<CausticEventListener>();
            setDaemon(true);
        }

        @Override
        public void run() {
            while (m_bRun) {
                if (m_bProcess) {
                    int nCurBeat = 0;// GetCurrentBeat();
                    if (nCurBeat != m_nCurrentBeat) {
                        m_nCurrentBeat = nCurBeat;
                        for (CausticEventListener listener : m_EventListeners) {
                            listener.OnBeatChanged(nCurBeat);
                        }
                    }

                    int nCurMeasure = 0;//GetCurrentSongMeasure();
                    if (nCurMeasure != m_nCurrentMeasure) {
                        m_nCurrentMeasure = nCurMeasure;
                        for (CausticEventListener listener : m_EventListeners) {
                            listener.OnMeasureChanged(nCurMeasure);
                        }
                    }
                } else {
                    try {
                        sleep(10);
                    } catch (Exception e) {
                    }
                }
            }
        }

        public void AddEventListener(CausticEventListener l) {
            m_EventListeners.add(l);
        }

        public void RemoveEventListener(CausticEventListener l) {
            m_EventListeners.remove(l);
        }

        ArrayList<CausticEventListener> m_EventListeners;

        public boolean m_bRun;

        public boolean m_bProcess;

        private int m_nCurrentBeat;

        private int m_nCurrentMeasure;
    }

}

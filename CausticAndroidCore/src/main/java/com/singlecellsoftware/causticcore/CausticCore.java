
package com.singlecellsoftware.causticcore;

import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;

import com.teotigraphix.caustk.core.CausticEventListener;

//=================================================================================================

class CausticAudioLoop extends Thread {
    static public final int PROCESS_BUFFER_SIZE_BYTES = 256; // 64 samples *
                                                             // stereo * 16bit,
                                                             // Multiple of 4.
                                                             // Change this for
                                                             // latency vs
                                                             // stability
                                                             // tradeoffs

    CausticAudioLoop() {
        // Log.d("Caustic", "Audio thread created");
        int nMinBufSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        m_AudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, nMinBufSize,
                AudioTrack.MODE_STREAM);
        m_audioData = new byte[PROCESS_BUFFER_SIZE_BYTES];

        nativeSetProcessBufferSize(PROCESS_BUFFER_SIZE_BYTES / 4); // div by 2 for
                                                                   // stereo, and by
                                                                   // 2 for 16 bit

        m_bRun = true;
        m_bProcess = false;
    }

    public void run() {
        if (m_AudioTrack == null)
            return;

        m_AudioTrack.play();

        while (m_bRun) {
            if (m_bProcess) {
                nativeProcess(m_audioData);
                m_AudioTrack.write(m_audioData, 0, PROCESS_BUFFER_SIZE_BYTES);
            } else {
                try {
                    sleep(10);
                } catch (Exception e) {
                }
            }
        }

        m_AudioTrack.stop();
    }

    public boolean m_bRun;

    public boolean m_bProcess;

    private int m_mixingBufferSize;

    private AudioTrack m_AudioTrack;

    byte m_audioData[];

    private static native void nativeProcess(byte[] bytes);

    private static native void nativeSetProcessBufferSize(int nSize);
}

class CausticAudioMonitor extends Thread {
    CausticAudioMonitor() {
        m_nCurrentBeat = 0;
        m_nCurrentMeasure = -1;
        m_bRun = true;
        m_bProcess = false;
        m_EventListeners = new ArrayList<CausticEventListener>();
    }

    public void run() {
        while (m_bRun) {
            if (m_bProcess) {
                int nCurBeat = nativeGetCurrentBeat();
                if (nCurBeat != m_nCurrentBeat) {
                    m_nCurrentBeat = nCurBeat;
                    for (CausticEventListener listener : m_EventListeners) {
                        listener.OnBeatChanged(nCurBeat);
                    }
                }

                int nCurMeasure = nativeGetCurrentSongMeasure();
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

    public void AddEventListener(CausticEventListener evtListener) {
        m_EventListeners.add(evtListener);
    }

    ArrayList<CausticEventListener> m_EventListeners;

    public boolean m_bRun;

    public boolean m_bProcess;

    private int m_nCurrentBeat;

    private int m_nCurrentMeasure;

    private static native int nativeGetCurrentBeat();

    private static native int nativeGetCurrentSongMeasure();
}

// =================================================================================================

public class CausticCore {
    public CausticCore() {
    }

    public void initialize() {
        try {
            System.loadLibrary("caustic");
        } catch (Exception e) {
            Log.e("CausticCore", e.getMessage());
        }

        m_byResponseString = new byte[4096];
        nativeSetStorageRootDir(Environment.getExternalStorageDirectory().getPath() + "/");
        nativeCreateMachines();
        SendOSCMessage("/caustic/blankrack");

        m_AudioMonitor = new CausticAudioMonitor();
    }

    public void Start() {
        // this happens when an application is brought back to the front
        // and hasn't been destroyed
        if (m_AudioLoop != null)
            return;

        m_AudioLoop = new CausticAudioLoop();
        m_AudioLoop.setPriority(Thread.MAX_PRIORITY);
        m_AudioLoop.start();

        m_AudioMonitor.setPriority(Thread.MAX_PRIORITY - 1);
        m_AudioMonitor.start();
    }

    public void Pause() {
        m_AudioLoop.m_bProcess = false;
        m_AudioLoop.setPriority(Thread.NORM_PRIORITY);

        m_AudioMonitor.m_bProcess = false;
        m_AudioMonitor.setPriority(Thread.NORM_PRIORITY);
    }

    public void Resume() {
        m_AudioLoop.m_bProcess = true;
        m_AudioLoop.setPriority(Thread.MAX_PRIORITY);

        m_AudioMonitor.m_bProcess = true;
        m_AudioMonitor.setPriority(Thread.MAX_PRIORITY - 1);
    }

    public void Restart() {
        m_AudioLoop = new CausticAudioLoop();
        m_AudioLoop.setPriority(Thread.MAX_PRIORITY);
        m_AudioLoop.start();
    }

    public void Stop() {
        m_AudioLoop.m_bRun = false;
        m_AudioMonitor.m_bRun = false;
    }

    public float SendOSCMessage(String msg) {
        return nativeOSCMessage(msg, null);
    }

    public String QueryOSC(String msg) {
        int nStrLen = (int)nativeOSCMessage(msg, m_byResponseString);
        return new String(m_byResponseString, 0, nStrLen);
    }

    public void AddEventListener(CausticEventListener evtListener) {
        m_AudioMonitor.AddEventListener(evtListener);
    }

    private CausticAudioLoop m_AudioLoop;

    private CausticAudioMonitor m_AudioMonitor;

    private byte[] m_byResponseString;

    private static native void nativeCreateMachines();

    private static native void nativeSetBufferSize(int nSize);

    private static native float nativeOSCMessage(String msg, byte[] response);

    private static native void nativeSetStorageRootDir(String sRootDir);
}


package com.singlecellsoftware.causticcore;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;

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

    @Override
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

// =================================================================================================

public class CausticCore {

    public int getVersion() {
        return nativeGetVersion();
    }

    public float getCurrentBeat() {
        return nativeGetCurrentBeat();
    }

    public float getCurrentSongMeasure() {
        return nativeGetCurrentSongMeasure();
    }

    public CausticCore() {
    }

    public void initialize(Context context, int key) {
        try {
            System.loadLibrary("caustic");
        } catch (Exception e) {
            Log.e("CausticCore", e.getMessage());
        }

        // This must be called before any other functions, to unlock Core functionality 
        nativeEnableOSC(context, key);

        m_byResponseString = new byte[4096];
        nativeSetStorageRootDir(Environment.getExternalStorageDirectory().getPath() + "/");
        nativeCreateMachines();
        SendOSCMessage("/caustic/blankrack");
    }

    public void Start() {
        // this happens when an application is brought back to the front
        // and hasn't been destroyed
        if (m_AudioLoop != null)
            return;

        m_AudioLoop = new CausticAudioLoop();
        m_AudioLoop.setPriority(Thread.MAX_PRIORITY);
        m_AudioLoop.start();
    }

    public void Pause() {
        m_AudioLoop.m_bProcess = false;
        m_AudioLoop.setPriority(Thread.NORM_PRIORITY);
    }

    public void Resume() {
        m_AudioLoop.m_bProcess = true;
        m_AudioLoop.setPriority(Thread.MAX_PRIORITY);
    }

    public void Restart() {
        m_AudioLoop = new CausticAudioLoop();
        m_AudioLoop.setPriority(Thread.MAX_PRIORITY);
        m_AudioLoop.start();
    }

    public void Stop() {
        m_AudioLoop.m_bRun = false;
    }

    public float SendOSCMessage(String msg) {
        return nativeOSCMessage(msg, null);
    }

    public String QueryOSC(String msg) {
        int nStrLen = (int)nativeOSCMessage(msg, m_byResponseString);
        return new String(m_byResponseString, 0, nStrLen);
    }

    private CausticAudioLoop m_AudioLoop;

    private byte[] m_byResponseString;

    private static native void nativeEnableOSC(Context ctx, int nKey);

    private static native void nativeCreateMachines();

    private static native void nativeSetBufferSize(int nSize);

    private static native float nativeOSCMessage(String msg, byte[] response);

    private static native void nativeSetStorageRootDir(String sRootDir);

    static native float nativeGetCurrentBeat();

    static native int nativeGetCurrentSongMeasure();

    static native int nativeGetVersion();
}

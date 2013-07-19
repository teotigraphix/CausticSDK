
package com.teotigraphix.caustk.sound;

import java.util.logging.Logger;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticEventListener;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.internal.CausticCoreDesktop;

/**
 * The sound generator holds references the actual raw Caustic machines created
 * in the SoundSource.
 * <p>
 * Holds the only reference to the actual {@link ICausticEngine} that interfaces
 * with the core.
 * <p>
 * The machines are the synthesizers or instruments that will be played by
 * clients of the sound generator.
 * <p>
 * Produces sounds through the Caustic core JNI interface.
 * <p>
 * Produces sounds form the controller or sequencer events.
 * <p>
 * Played by OSC or MIDI messages.
 */
public class DesktopSoundGenerator implements ISoundGenerator {
    protected static final Logger log;

    private static ICausticEngine instance;

    static {
        log = Logger.getLogger(DesktopSoundGenerator.class.getPackage().getName());
    }

    /**
     * For testing.
     * 
     * @return The single instance.
     */
    public static ICausticEngine getInstance() {
        if (instance == null)
            instance = new DesktopSoundGenerator(null);
        return instance;
    }

    @SuppressWarnings("unused")
    private ICaustkController controller;

    //----------------------------------
    // instance
    //----------------------------------

    private static CausticCoreDesktop causticCore;

    public static final CausticCoreDesktop core() {
        if (causticCore == null)
            causticCore = new CausticCoreDesktop();
        return causticCore;
    }

    @Override
    public void initialize() {
        core();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public DesktopSoundGenerator(ICaustkController controller) {
        this.controller = controller;
    }

    @Override
    public void close() {
        causticCore.CausticCore_Deinit();
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    @Override
    public float sendMessage(String message) {
        //System.out.println(message);
        //log.info("Message:" + message);
        float value = core().SendOSCMessage(message);
        return value;
    }

    @Override
    public String queryMessage(String message) {
        //log.info("Query:" + message);
        //System.err.println(message);
        String result = core().QueryOSC(message);
        if (result != null && result.equals(""))
            return null;
        return result;
    }

    @Override
    public void addEventListener(CausticEventListener l) {
        core().addEventListener(l);
    }

    @Override
    public void removeEventListener(CausticEventListener l) {
        core().removeEventListener(l);
    }
}

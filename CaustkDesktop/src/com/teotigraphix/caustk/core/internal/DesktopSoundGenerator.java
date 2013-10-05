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

import java.util.logging.Logger;

import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.sound.ISoundGenerator;

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

    private static DesktopSoundGenerator instance;

    static {
        log = Logger.getLogger(DesktopSoundGenerator.class.getPackage().getName());
    }

    //----------------------------------
    // instance
    //----------------------------------

    private static CausticCoreDesktop causticCore;

    /**
     * For testing.
     * 
     * @return The single instance.
     */
    public static ICausticEngine getInstance() {
        return instance;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public DesktopSoundGenerator() {
    }

    @Override
    public void initialize() {
        instance = new DesktopSoundGenerator();
        causticCore = new CausticCoreDesktop();
    }

    @Override
    public void close() {
        causticCore.deinit();
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    @Override
    public float sendMessage(String message) {
        //System.out.println(message);
        //log.info("Message:" + message);
        float value = causticCore.SendOSCMessage(message);
        return value;
    }

    @Override
    public String queryMessage(String message) {
        //log.info("Query:" + message);
        String result = causticCore.QueryOSC(message);
        if (result != null && result.equals(""))
            return null;
        return result;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        causticCore.deinit();
    }

    @Override
    public void onRestart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public float getCurrentBeat() {
        return causticCore.getCurrentBeat();
    }

    @Override
    public float getCurrentSongMeasure() {
        return causticCore.getCurrentSongMeasure();
    }

    @Override
    public int getVerison() {
        return causticCore.getVersion();
    }
}

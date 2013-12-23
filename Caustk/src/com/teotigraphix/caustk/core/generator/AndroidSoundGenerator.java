////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.core.generator;

import java.util.logging.Logger;

import android.content.Context;

import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.ISoundGenerator;

/**
 * The base implementation of the {@link ICausticEngine}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class AndroidSoundGenerator implements ISoundGenerator {

    /**
     * The CausticCore OSC audio engine name.
     */
    public final static String ENGINE_NAME = CoreConstants.ID;

    protected static final Logger log;

    static {
        log = Logger.getLogger(AndroidSoundGenerator.class.getPackage().getName());
    }

    //--------------------------------------------------------------------------
    //
    //  Variables
    //
    //--------------------------------------------------------------------------

    private Caustic causticCore;

    //--------------------------------------------------------------------------
    //
    //  Constructors
    //
    //--------------------------------------------------------------------------

    public AndroidSoundGenerator(Context context, int key) {
        causticCore = new Caustic();
        try {
            causticCore.initialize(context, key);
        } catch (UnsatisfiedLinkError e) {
            causticCore.setDebug(true);
            // Debug tests, or can't find the .so on the device
        }
    }

    @Override
    public void initialize() {
    }

    @Override
    public void close() {
    }

    //--------------------------------------------------------------------------
    //
    //  ICausticEngine API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public float sendMessage(String message) {
        if (causticCore == null)
            return Float.NaN;

        float value = causticCore.SendOSCMessage(message);
        return value;
    }

    @Override
    public String queryMessage(String message) {
        if (causticCore == null)
            return null;

        String result = causticCore.QueryOSC(message);
        if (result.equals(""))
            result = null;
        return result;
    }

    //--------------------------------------------------------------------------
    //
    //  Life cycle API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void onStart() {
        causticCore.Start();
    }

    @Override
    public void onResume() {
        causticCore.Resume();
    }

    @Override
    public void onPause() {
        causticCore.Pause();
    }

    @Override
    public void onStop() {
        causticCore.Stop();
    }

    @Override
    public void onDestroy() {
        //causticCore.Destroy();
    }

    @Override
    public void onRestart() {
        causticCore.Restart();
    }

    //--------------------------------------------------------------------------
    //
    //  IDispose API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void dispose() {
        if (causticCore != null) {
            onStop();
            causticCore = null;
        }
    }

    @Override
    public int getVerison() {
        return causticCore.getVersion();
    }

    @Override
    public float getCurrentBeat() {
        return causticCore.getCurrentBeat();
    }

    @Override
    public float getCurrentSongMeasure() {
        return causticCore.getCurrentSongMeasure();
    }
}

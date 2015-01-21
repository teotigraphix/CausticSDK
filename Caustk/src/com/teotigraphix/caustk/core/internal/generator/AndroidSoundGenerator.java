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

package com.teotigraphix.caustk.core.internal.generator;

import android.content.Context;

import com.teotigraphix.caustk.core.CausticException;
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

    //--------------------------------------------------------------------------
    //  Variables
    //--------------------------------------------------------------------------

    private Caustic caustic;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public AndroidSoundGenerator(Context context, int key) throws CausticException {
        caustic = new Caustic();
        try {
            caustic.initialize(context, key);
        } catch (UnsatisfiedLinkError e) {
            throw new CausticException("UnsatisfiedLinkError for " + context.getPackageName());
        }
    }

    @Override
    public void initialize() {
    }

    @Override
    public void close() {
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public float sendMessage(String message) {
        if (caustic == null)
            return Float.NaN;

        float value = caustic.SendOSCMessage(message);
        return value;
    }

    @Override
    public String queryMessage(String message) {
        if (caustic == null)
            return null;

        String result = caustic.QueryOSC(message);
        if (result.equals(""))
            result = null;
        return result;
    }

    //--------------------------------------------------------------------------
    // API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onStart() {
        caustic.Start();
    }

    @Override
    public void onResume() {
        caustic.Resume();
    }

    @Override
    public void onPause() {
        caustic.Pause();
    }

    @Override
    public void onStop() {
        caustic.Stop();
    }

    @Override
    public void onDestroy() {
        //causticCore.Destroy();
    }

    @Override
    public void onRestart() {
        caustic.Restart();
    }

    @Override
    public void onDispose() {
        if (caustic != null) {
            onStop();
            caustic = null;
        }
    }

    @Override
    public int getVerison() {
        return caustic.getVersion();
    }

    @Override
    public float getCurrentBeat() {
        return caustic.getCurrentBeat();
    }

    @Override
    public float getCurrentSongMeasure() {
        return caustic.getCurrentSongMeasure();
    }
}

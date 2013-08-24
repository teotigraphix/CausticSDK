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

package com.teotigraphix.caustic.core;

import java.util.logging.Logger;

import com.teotigraphix.caustk.core.CausticEventListener;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.sound.ISoundGenerator;

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

    public AndroidSoundGenerator() {
        causticCore = new Caustic();
        try {
            causticCore.initialize();
        } catch (UnsatisfiedLinkError e) {
            causticCore.setDebug(true);
            // Debug tests, or can't find the .so on the device
            // TODO Log UnsatisfiedLinkError for CasuticCore
        }
    }

    //--------------------------------------------------------------------------
    //
    //  ICausticEngine API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public float sendMessage(String message) {
        //        fireOSCMessage(message);

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
        //fireLifeCycleChange(LifeCycleState.START);
    }

    @Override
    public void onResume() {
        causticCore.Resume();
        //fireLifeCycleChange(LifeCycleState.RESUME);
    }

    @Override
    public void onPause() {
        causticCore.Pause();
        //fireLifeCycleChange(LifeCycleState.PAUSE);
    }

    @Override
    public void onStop() {
        causticCore.Stop();
        //fireLifeCycleChange(LifeCycleState.STOP);
    }

    @Override
    public void onDestroy() {
        //mCausticCore.Destroy();
        //fireLifeCycleChange(LifeCycleState.DESTROY);
    }

    @Override
    public void onRestart() {
        causticCore.Restart();
        //fireLifeCycleChange(LifeCycleState.RESTART);
    }

    //--------------------------------------------------------------------------
    //
    //  IDispose API :: Methods
    //
    //--------------------------------------------------------------------------

    public void dispose() {
        if (causticCore != null) {
            onStop();
            causticCore = null;
        }
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addEventListener(CausticEventListener l) {
        causticCore.AddEventListener(l);
    }

    @Override
    public void removeEventListener(CausticEventListener l) {
        causticCore.RemoveEventListener(l);        
    }

    @Override
    public float getCurrentBeat() {
        return causticCore.getCurrentBeat();
    }

    //--------------------------------------------------------------------------
    // 
    //  Listeners
    // 
    //--------------------------------------------------------------------------

    //    private OnOSCMessageListener mOSCMessageListener;
    //
    //    @Override
    //    public final void setOnOSCMessageListener(OnOSCMessageListener value) {
    //        mOSCMessageListener = value;
    //    }
    //
    //    private OnLifeCycleChangeListener mLifeCycleChangeListener;
    //
    //    @Override
    //    public final void setOnLifeCycleChangeListener(OnLifeCycleChangeListener value) {
    //        mLifeCycleChangeListener = value;
    //    }

    //--------------------------------------------------------------------------
    //
    //  Protected :: Methods
    //
    //--------------------------------------------------------------------------
    //
    //    protected final void fireOSCMessage(String message) {
    //        if (mOSCMessageListener != null) {
    //            mOSCMessageListener.onOSCMessage(message);
    //        }
    //    }
    //
    //    protected final void fireLifeCycleChange(LifeCycleState state) {
    //        if (mLifeCycleChangeListener != null) {
    //            mLifeCycleChangeListener.onLifeCycleChange(state);
    //        }
    //    }
    //
    //    @Override
    //    public void addCoreEventListener(CausticEventListener l) {
    //        if (mCausticCore != null)
    //            mCausticCore.AddEventListener(l);
    //    }
    //
    //    @Override
    //    public void removeCoreEventListener(CausticEventListener l) {
    //        if (mCausticCore != null)
    //            mCausticCore.RemoveEventListener(l);
    //    }
}

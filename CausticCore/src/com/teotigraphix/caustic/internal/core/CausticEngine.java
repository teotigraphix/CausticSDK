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

package com.teotigraphix.caustic.internal.core;

import java.util.logging.Logger;

import com.singlecellsoftware.causticcore.CausticEventListener;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.core.ICausticEngineLifesCycle;
import com.teotigraphix.common.IDispose;

/**
 * The base implementation of the {@link ICausticEngine}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class CausticEngine implements ICausticEngine, ICausticEngineLifesCycle, IDispose {

    /**
     * The CausticCore OSC audio engine name.
     */
    public final static String ENGINE_NAME = CoreConstants.ID;

    protected static final Logger log;

    static {
        log = Logger.getLogger(CausticEngine.class.getPackage().getName());
    }

    //--------------------------------------------------------------------------
    //
    // Variables
    //
    //--------------------------------------------------------------------------

    private Caustic mCausticCore;

    //--------------------------------------------------------------------------
    //
    // Constructors
    //
    //--------------------------------------------------------------------------

    public CausticEngine(Context context, int key) {
        mCausticCore = new Caustic();
        try {
            mCausticCore.initialize(context, key);
        } catch (UnsatisfiedLinkError e) {
            mCausticCore.setDebug(true);
            // Debug tests, or can't find the .so on the device
            // (mschmalle)  Log UnsatisfiedLinkError for CasuticCore
        }
    }

    //--------------------------------------------------------------------------
    //
    // ICausticEngine API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public float sendMessage(String message) {
        fireOSCMessage(message);

        if (mCausticCore == null)
            return Float.NaN;

        float value = mCausticCore.SendOSCMessage(message);
        return value;
    }

    @Override
    public String queryMessage(String message) {
        if (mCausticCore == null)
            return null;

        String result = mCausticCore.QueryOSC(message);
        if (result.equals(""))
            result = null;
        return result;
    }

    //--------------------------------------------------------------------------
    //
    // Life cycle API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void onStart() {
        mCausticCore.Start();
        fireLifeCycleChange(LifeCycleState.START);
    }

    @Override
    public void onResume() {
        mCausticCore.Resume();
        fireLifeCycleChange(LifeCycleState.RESUME);
    }

    @Override
    public void onPause() {
        mCausticCore.Pause();
        fireLifeCycleChange(LifeCycleState.PAUSE);
    }

    @Override
    public void onStop() {
        mCausticCore.Stop();
        fireLifeCycleChange(LifeCycleState.STOP);
    }

    @Override
    public void onDestroy() {
        mCausticCore.Destroy();
        fireLifeCycleChange(LifeCycleState.DESTROY);
    }

    @Override
    public void onRestart() {
        mCausticCore.Restart();
        fireLifeCycleChange(LifeCycleState.RESTART);
    }

    //--------------------------------------------------------------------------
    //
    // IDispose API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void dispose() {
        if (mCausticCore != null) {
            onStop();
            mCausticCore = null;
        }
    }

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    private OnOSCMessageListener mOSCMessageListener;

    @Override
    public final void setOnOSCMessageListener(OnOSCMessageListener value) {
        mOSCMessageListener = value;
    }

    private OnLifeCycleChangeListener mLifeCycleChangeListener;

    @Override
    public final void setOnLifeCycleChangeListener(OnLifeCycleChangeListener value) {
        mLifeCycleChangeListener = value;
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    protected final void fireOSCMessage(String message) {
        if (mOSCMessageListener != null) {
            mOSCMessageListener.onOSCMessage(message);
        }
    }

    protected final void fireLifeCycleChange(LifeCycleState state) {
        if (mLifeCycleChangeListener != null) {
            mLifeCycleChangeListener.onLifeCycleChange(state);
        }
    }

    @Override
    public void addCoreEventListener(CausticEventListener l) {
        if (mCausticCore != null)
            mCausticCore.AddEventListener(l);
    }

    @Override
    public void removeCoreEventListener(CausticEventListener l) {
        if (mCausticCore != null)
            mCausticCore.RemoveEventListener(l);
    }
}

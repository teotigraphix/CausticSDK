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

/**
 * The Android lifecycle events.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ICausticEngineLifesCycle {

    /**
     * Called from an <code>Activity#onStart()</code> method.
     * 
     * @since 1.0
     */
    void onStart();

    /**
     * Called from an <code>Activity#onResume()</code> method.
     * 
     * @since 1.0
     */
    void onResume();

    /**
     * Called from an <code>Activity#onPause()</code> method.
     * 
     * @since 1.0
     */
    void onPause();

    /**
     * Called from an <code>Activity#onStop()</code> method.
     * 
     * @since 1.0
     */
    void onStop();

    /**
     * Called from an Activity#onDestroy()</code> method.
     * 
     * @since 1.0
     */
    void onDestroy();

    /**
     * Called from an Activity#onRestart()</code> method.
     * 
     * @since 1.0
     */
    void onRestart();

    /**
     * Sets the listener.
     * 
     * @param l The listener.
     */
    void setOnLifeCycleChangeListener(OnLifeCycleChangeListener l);

    /**
     * Dispatched when a change in LifeCycleState has happened within the
     * CausticEngine.
     * 
     * @param state The new {@link LifeCycleState}.
     */
    public interface OnLifeCycleChangeListener {
        void onLifeCycleChange(LifeCycleState state);
    }

    /**
     * The life cycle state for the {@link ICausticEngine} and the callback of
     * OnLifeCycleChangeListener.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     * @see ICausticEngine#setOnLifeCycleChangeListener()
     */
    public enum LifeCycleState {

        /**
         * When the Android application's onStart() method is called.
         */
        START,

        /**
         * When the Android application's onResume() method is called.
         */
        RESUME,

        /**
         * When the Android application's onPause() method is called.
         */
        PAUSE,

        /**
         * When the Android application's onStop() method is called.
         */
        STOP,

        /**
         * When the Android application's onDestroy() method is called.
         */
        DESTROY,

        /**
         * When the Android application's onRestart() method is called.
         */
        RESTART
    }
}

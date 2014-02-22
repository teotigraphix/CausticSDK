////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.core;

import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.RackNode;

/**
 * The {@link CaustkEngine} manages all messages sent to the native sound
 * engine.
 * <p>
 * The engine is really just a Facade over the audio engine call backs.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CaustkEngine implements ISoundGenerator {

    public static boolean DEBUG_QUERIES = false;

    public static boolean DEBUG_MESSAGES = false;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private final ISoundGenerator soundGenerator;

    private final EventBus eventBus;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // eventBus
    //----------------------------------

    /**
     * Returns the single event bus for the {@link CaustkRack}, this event bus
     * will be constant throughout the whole application life cycle.
     * <p>
     * All {@link NodeBase} events are dispatched through this event bus.
     * {@link NodeBase} instances are never subscribers, only dispatchers.
     * <p>
     * A {@link NodeBase} event is prefixed with the node class name and post
     * fixed with 'Event'. So a {@link RackNode} event would be
     * <code>RackNodeEvent</code>. Any custom event based off the
     * <code>RackNodeEvent</code> will look like
     * <code>RackNodeChangeEvent</code> or <code>RackNodeMachineAddEvent</code>.
     * 
     * @see EventBus#register(Object)
     * @see EventBus#unregister(Object)
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    CaustkEngine(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
        eventBus = new EventBus("engine");
    }

    //--------------------------------------------------------------------------
    // ISoundGenerator API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        soundGenerator.initialize();
    }

    @Override
    public void close() {
        soundGenerator.close();
    }

    @Override
    public int getVerison() {
        return soundGenerator.getVerison();
    }

    @Override
    public final float getCurrentSongMeasure() {
        return soundGenerator.getCurrentSongMeasure();
    }

    @Override
    public final float getCurrentBeat() {
        return soundGenerator.getCurrentBeat();
    }

    //----------------------------------
    // IActivityCycle API
    //----------------------------------

    @Override
    public void onStart() {
        soundGenerator.onStart();
        soundGenerator.onResume();
    }

    @Override
    public void onResume() {
        soundGenerator.onResume();
    }

    @Override
    public void onPause() {
        soundGenerator.onPause();
    }

    @Override
    public void onStop() {
        soundGenerator.onStop();
    }

    @Override
    public void onDestroy() {
        soundGenerator.onDestroy();
    }

    @Override
    public void onRestart() {
        soundGenerator.onRestart();
    }

    @Override
    public void dispose() {
        // XXX impl CaustkRack.dispose()
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    private final StringBuilder oscMessages = new StringBuilder();

    public String getRawOSCMessages() {
        return oscMessages.toString();
    }

    @Override
    public final float sendMessage(String message) {
        oscMessages.append("[Message] " + message);
        oscMessages.append("\n");
        return soundGenerator.sendMessage(message);
    }

    @Override
    public final String queryMessage(String message) {
        oscMessages.append("[  Query] " + message);
        oscMessages.append("\n");
        return soundGenerator.queryMessage(message);
    }
}

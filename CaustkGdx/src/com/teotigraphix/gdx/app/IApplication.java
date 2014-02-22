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

package com.teotigraphix.gdx.app;

import com.badlogic.gdx.ApplicationListener;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.CaustkRack;
import com.teotigraphix.caustk.core.ICaustkLogger;

/**
 * The {@link IApplication} API is the top level container for all user
 * interface and Caustic Core logic.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IApplication extends ApplicationListener {

    /**
     * The application name, will show up in the title bar in desktop
     * applications.
     */
    String getApplicationName();

    /**
     * The OSC machine name of this application.
     */
    String getApplicationId();

    /**
     * The width of the application.
     */
    float getWidth();

    /**
     * The height of the application.
     */
    float getHeight();

    /**
     * Returns the application rack.
     */
    CaustkRack getRack();

    /**
     * Returns the application logger.
     */
    ICaustkLogger getLogger();

    /**
     * Returns the application level event bus.
     */
    EventBus getEventBus();

    /**
     * Sets the current scene.
     * <p>
     * Will be created and shown on the next frame render.
     * 
     * @param sceneId The scene id.
     */
    void setScene(int sceneId);

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    //    public static class OnApplicationAwakeEvent {
    //    }

    public static class OnApplicationDestoryEvent {
    }

}

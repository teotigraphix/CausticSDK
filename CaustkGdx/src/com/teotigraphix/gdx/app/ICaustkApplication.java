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

import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.CaustkRack;
import com.teotigraphix.caustk.core.ICaustkLogger;

/**
 * The {@link ICaustkApplication} API is the top level container for all user
 * interface and Caustic Core logic.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface ICaustkApplication extends IApplication {

    /**
     * The application name, will show up in the title bar in desktop
     * applications.
     */
    @Override
    String getApplicationName();

    /**
     * The OSC machine name of this application.
     */
    @Override
    String getApplicationId();

    /**
     * The width of the application.
     */
    @Override
    float getWidth();

    /**
     * The height of the application.
     */
    @Override
    float getHeight();

    /**
     * Returns the application logger.
     */
    ICaustkLogger getLogger();

    /**
     * Returns the application level event bus.
     */
    @Override
    EventBus getEventBus();

    /**
     * Returns the application rack.
     */
    CaustkRack getRack();

    /**
     * Returns the current scene.
     */
    @Override
    ICaustkScene getScene();

    /**
     * Sets the current scene.
     * <p>
     * Will be created and shown on the next frame render.
     * 
     * @param sceneId The scene id.
     */
    @Override
    void setScene(int sceneId);

    /**
     * Whether the current scene id matches the argument passed.
     * 
     * @param sceneId The scene id to test against the current scene's id.
     */
    @Override
    boolean isCurrentScene(int sceneId);

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    //    public static class OnApplicationAwakeEvent {
    //    }

    public static class OnApplicationDestoryEvent {
    }

    void onSceneChange(ICaustkScene scene);

}

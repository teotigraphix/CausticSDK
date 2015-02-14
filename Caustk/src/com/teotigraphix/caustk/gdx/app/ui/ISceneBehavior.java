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

package com.teotigraphix.caustk.gdx.app.ui;

/**
 * The {@link ISceneBehavior} is registered with a
 * {@link com.teotigraphix.caustk.gdx.app.ui.Scene} to mediate its views and
 * receives messages from the scene.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface ISceneBehavior extends ISceneComponent {

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isEnabled();

    void setEnabled(boolean enabled);

    /**
     * Start is called on the frame when a behavior is enabled just before any
     * of the Update methods is called the first time.
     * <p>
     * Called right after the Scene's createUI() has been called. All default
     * components have been created and the app model is restored, so components
     * can be populated with restored data.
     */
    void onStart();

    /**
     * Update is called every frame, if the behavior is enabled.
     */
    void onUpdate();

    /**
     * Resize is called when the main application frame is resized.
     * <p>
     * This callback can be used for things like hiding a menubar's menu if the
     * main frame has been resized, or adjust other popups that may need to
     * change position due to the frame size change.
     */
    void onResize();

    /**
     * Reset behavior to default values.
     */
    void onReset();

    /**
     * The behavior is shown.
     * 
     * @see com.teotigraphix.caustk.gdx.app.ui.IScene#set
     */
    void onShow();

    /**
     * The behavior is hidden.
     * 
     * @see com.teotigraphix.caustk.gdx.app.ui.IScene#hide()
     */
    void onHide();

    /**
     * The behavior is enabled.
     * 
     * @see com.teotigraphix.caustk.gdx.app.ui.IScene#resume()
     */
    void onEnable();

    /**
     * The behavior is enabled.
     * 
     * @see com.teotigraphix.caustk.gdx.app.ui.IScene#dispose()
     */
    void onDisable();

    /**
     * The behavior is paused.
     * 
     * @see com.teotigraphix.caustk.gdx.app.ui.IScene#pause()
     */
    void onPause();

    /**
     * The behavior is resumed.
     * 
     * @see com.teotigraphix.caustk.gdx.app.ui.IScene#resume()
     */
    void onResume();

    /**
     * De reference.
     */
    void dispose();
}

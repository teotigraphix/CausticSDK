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

package com.teotigraphix.caustk.gdx.app;

import com.badlogic.gdx.Preferences;

/**
 * The {@link IApplicationComponent} is a high level data structure that posts
 * events.
 * <p>
 * Components will dispatch events through their local
 * {@link com.google.common.eventbus.EventBus} or global
 * {@link com.teotigraphix.caustk.gdx.app.ICaustkApplication#getEventBus()}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IApplicationComponent {

    /**
     * The model's {@link com.badlogic.gdx.Preferences} instance.
     */
    Preferences getPreferences();

    /**
     * Returns the {@link com.teotigraphix.caustk.gdx.app.ICaustkApplication}
     * instance.
     */
    //ICaustkApplication getApplication();
}

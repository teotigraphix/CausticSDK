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

package com.teotigraphix.gdx.app;

import com.teotigraphix.gdx.GdxScene;
import com.teotigraphix.gdx.IGdxApplication;
import com.teotigraphix.gdx.IGdxScene;

/**
 * The {@link IGdxComponent} is registered with a {@link GdxScene} to mediate
 * its views.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IGdxComponent {

    /**
     * Returns the component's {@link IGdxApplication} application.
     */
    IGdxApplication getApplication();

    /**
     * Returns the {@link IGdxScene} this component is attached to.
     */
    IGdxScene getScene();

    /**
     * Awake is called when the behavior is being loaded.
     * <p>
     * The {@link IGdxComponent#getScene()} is guaranteed to be non
     * <code>null</code>.
     * <p>
     * Add global/application event listeners.
     */
    void onAwake();

}

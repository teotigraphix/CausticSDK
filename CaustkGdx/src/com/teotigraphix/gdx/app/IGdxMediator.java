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

/**
 * The {@link IGdxMediator} is registered with a {@link GdxScene} to mediate
 * its views.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IGdxMediator {

    /**
     * Called when the mediator is attached to the application.
     * <p>
     * Add global/application event listeners.
     */
    void onAttach();

}

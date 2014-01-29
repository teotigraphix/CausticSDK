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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.eventbus.EventBus;

/**
 * The {@link IGdxView} API wraps a view widget.
 * <p>
 * The view is the composite component that holds {@link Actor}s an
 * {@link IGdxBehavior} mediates.
 * <p>
 * The view will use it's {@link EventBus} API to post messages to all
 * subscribers which in this case would be the owning {@link IGdxBehavior}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IGdxView {

    /**
     * Register an Event with the view.
     * 
     * @param event The event subscriber.
     */
    void register(Object subscriber);

    /**
     * Unregister an Event with the view.
     * 
     * @param event The event subscriber.
     */
    void unregister(Object subscriber);

    /**
     * Reigsters the view's {@link Skin}.
     */
    void registerSkin(Skin skin);

}

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

package com.teotigraphix.caustk.gdx.app.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.app.ApplicationComponent;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
@Singleton
public class HelpManager extends ApplicationComponent implements IHelpManager {

    private Map<Actor, HelpInfo> map = new HashMap<Actor, HelpManager.HelpInfo>();

    @Override
    public void register(final Actor actor, String helpText) {
        if (map.containsKey(actor))
            throw new IllegalStateException("Actor already registered");

        HelpInfo helpInfo = new HelpInfo(helpText);
        helpInfo.setListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                helpChange(event.getListenerActor());
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                helpChange(null);
            }
        });
        actor.addListener(helpInfo.getListener());
        map.put(actor, helpInfo);
    }

    @Override
    public void unregister(Actor actor) {
        HelpInfo helpInfo = map.remove(actor);
        actor.removeListener(helpInfo.getListener());
        helpInfo.dispose();
    }

    protected void helpChange(Actor listenerActor) {
        getEventBus().post(new OnHelpManagerHelpChange(map.get(listenerActor)));
    }
}

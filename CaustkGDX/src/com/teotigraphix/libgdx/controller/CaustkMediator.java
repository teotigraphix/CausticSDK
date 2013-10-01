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

package com.teotigraphix.libgdx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;

// Mediators never dispatch events!, only listen and act with logic
// that could eventually be put in a Command
public abstract class CaustkMediator implements ICaustkMediator {

    private ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    @Inject
    void setApplicationController(IApplicationController value) {
        controller = value.getController();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CaustkMediator() {
    }

    @Override
    public void onRegister() {
    }

    //--------------------------------------------------------------------------
    // Event
    //--------------------------------------------------------------------------

    @SuppressWarnings("rawtypes")
    private Map<IDispatcher, List<EventObserver>> observers = new HashMap<IDispatcher, List<EventObserver>>();

    /**
     * Registers an event on the {@link ICaustkController#getDispatcher()}.
     * 
     * @param event
     * @param observer
     */
    protected <T> void register(Class<T> event, EventObserver<T> observer) {
        register(getController(), event, observer);
    }

    /**
     * Registers an event on the passed {@link IDispatcher}.
     * 
     * @param dispatcher
     * @param event
     * @param observer
     */
    @SuppressWarnings("rawtypes")
    protected <T> void register(IDispatcher dispatcher, Class<T> event, EventObserver<T> observer) {
        List<EventObserver> list = observers.get(dispatcher);
        if (list == null) {
            list = new ArrayList<EventObserver>();
            observers.put(dispatcher, list);
        }
        dispatcher.register(event, observer);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void dispose() {
        for (Entry<IDispatcher, List<EventObserver>> entry : observers.entrySet()) {
            IDispatcher dispatcher = entry.getKey();
            for (EventObserver observer : entry.getValue()) {
                dispatcher.unregister(observer);
            }
        }
    }
}

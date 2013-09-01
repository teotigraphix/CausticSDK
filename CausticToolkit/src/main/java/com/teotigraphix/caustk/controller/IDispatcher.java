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

package com.teotigraphix.caustk.controller;

import org.androidtransfuse.event.EventObserver;

public interface IDispatcher {
    /**
     * Register the given observer to be triggered if the given event type is
     * triggered.
     * 
     * @param event type
     * @param observer event observer
     * @param <T> relating type
     */
    <T> void register(Class<T> event, EventObserver<T> observer);

    /**
     * Unregisters an EventObserver by equality.
     * 
     * @param observer Event Observer
     */
    void unregister(EventObserver<?> observer);

    /**
     * Triggers an event through the EventManager. This will call the registered
     * EventObservers with the provided event.
     * 
     * @param event object
     */
    void trigger(Object event);
}

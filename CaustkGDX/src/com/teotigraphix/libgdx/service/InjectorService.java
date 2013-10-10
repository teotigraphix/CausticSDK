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

package com.teotigraphix.libgdx.service;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.caustk.service.IInjectorService;

@Singleton
public class InjectorService implements IInjectorService {

    @Inject
    Injector injector;

    private final IDispatcher dispatcher;

    public InjectorService() {
        dispatcher = new Dispatcher();
    }

    @Override
    public void inject(Object instance) {
        injector.injectMembers(instance);
    }

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
        dispatcher.register(event, observer);
    }

    @Override
    public void unregister(EventObserver<?> observer) {
        dispatcher.unregister(observer);
    }

    @Override
    public void trigger(Object event) {
        dispatcher.trigger(event);
    }

    @Override
    public void clear() {
        dispatcher.clear();
    }
}

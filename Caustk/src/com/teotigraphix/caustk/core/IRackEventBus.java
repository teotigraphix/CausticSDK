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

package com.teotigraphix.caustk.core;

/**
 * The {@link IRackEventBus} API allows subscription to the {@link ICaustkRack}
 * 's event buss.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IRackEventBus {

    /**
     * Register a subscriber.
     * 
     * @param subscriber The subscriber add.
     */
    void register(Object subscriber);

    /**
     * Unegister a subscriber.
     * 
     * @param subscriber The subscriber to remove.
     */
    void unregister(Object subscriber);

    /**
     * Posts an event through the {@link ICaustkRack}.
     * 
     * @param event The event object.
     */
    void post(Object event);
}

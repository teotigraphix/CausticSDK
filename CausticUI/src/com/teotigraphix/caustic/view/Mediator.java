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

package com.teotigraphix.caustic.view;

import roboguice.event.EventManager;
import roboguice.event.Observes;
import android.view.View;

import com.google.inject.Inject;
import com.teotigraphix.caustic.song.ISong;

public class Mediator implements IMediator {

    @Inject
    EventManager eventManager;

    protected final EventManager getEventManager() {
        return eventManager;
    }

    View view;

    public void setView(View value) {
        view = value;
    }

    public View getView() {
        return view;
    }

    void onSetupMediatorEvent(@Observes OnSetupMediatorEvent event) {
        onSetupMediator();
    }

    void onAttachMediatorEvent(@Observes OnAttachMediatorEvent event) {
        onAttachMediator();
        onResetMediator();
    }

    void onResetMediatorEvent(@Observes OnResetMediatorEvent event) {
        onResetMediator();
    }

    void onDetachMediatorEvent(@Observes OnDetachMediatorEvent event) {
        onDetachMediator(null);
    }

    protected void onSetupMediator() {
    }

    /**
     * The {@link #onAttachMediator()} is called, then
     * {@link #onResetMediator()}.
     */
    protected void onAttachMediator() {
    }

    /**
     * Setup values on the view from the mediators models.
     * <p>
     * This method will be called after {@link #onAttachMediator()} when a view
     * is setup. When the {@link OnResetMediatorEvent} is fired only this method
     * will be called.
     */
    protected void onResetMediator() {
    }

    /**
     * Detach a mediator from it's view.
     * <p>
     * If the mediator is <code>null</code> all mediators are being requested to
     * detach.
     * 
     * @param mediator The detached mediator.
     */
    protected void onDetachMediator(Mediator mediator) {
    }

    /**
     * Setup all view to model relationships BEFORE the views get there
     * {@link OnAttachMediatorEvent} handled.
     * <p>
     * This allows views to setup properly before running.
     */
    public static class OnSetupMediatorEvent {
        public OnSetupMediatorEvent() {
        }
    }

    /**
     * Fired from the application model right after OnWorkspaceRunEvent has been
     * handled.
     * <p>
     * At this point the workspace, project exist and have been initialized,
     * there is also a current song in the project to be accessed. The
     * {@link ISong} state will also be loaded with the current song from disk.
     */
    public static class OnAttachMediatorEvent {
        public OnAttachMediatorEvent() {
        }
    }

    public static class OnResetMediatorEvent {
        public OnResetMediatorEvent() {
        }
    }

    public static class OnDetachMediatorEvent {
        public OnDetachMediatorEvent() {
        }
    }
}

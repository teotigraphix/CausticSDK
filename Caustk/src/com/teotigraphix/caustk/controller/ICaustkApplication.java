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

import java.io.IOException;

import com.teotigraphix.caustk.controller.core.CaustkConfigurationBase;

/**
 * The {@link ICaustkApplication} API is the startup instrumentation for the
 * CausticSDK.
 * <p>
 * The whole load, setup sequence is determined by this API template as well as
 * save and shutdown.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see ICaustkConfiguration
 * @see ICaustkController
 */
public interface ICaustkApplication {

    /**
     * Returns the application logger.
     */
    ICausticLogger getLogger();

    /**
     * The application specific phase strategy. A strategy for custom
     * application implementation of application phases such as create, save and
     * close.
     * 
     * @param value The handler instance the application will use during it's
     *            phase callbacks.
     */
    void setApplicationHandler(IApplicationHandler value);

    /**
     * Returns the application's single {@link ICaustkConfiguration}.
     * <p>
     * The {@link ICaustkConfiguration} is the initialize instance for the
     * {@link ICaustkApplication} and {@link ICaustkController}.
     * <p>
     * Individual application can configure the application and controller
     * specifically to suite there needs by either fully implementing the
     * {@link ICaustkConfiguration} API or sub classing the
     * {@link CaustkConfigurationBase} class that has already implemented the
     * basic configuration setup.
     */
    ICaustkConfiguration getConfiguration();

    /**
     * Returns the application's single {@link ICaustkController}.
     * <p>
     * The {@link ICaustkController} is created when the application is
     * constructed. When this construction occurs, the
     * {@link ICaustkConfiguration#createController(ICaustkApplication)} method
     * is called to return the single instance of the controller.
     * <p>
     * Only the {@link ICaustkController#getDispatcher()} is created during the
     * controllers construction. This allows other clients of the controller to
     * register themselves for events during their construction.
     * <p>
     * Also note, the default implementation of the {@link ICaustkController}'s
     * dispatcher is actually wrapped by the {@link IDispatcher} implemented by
     * the controller. This means that the
     * {@link ICaustkController#getDispatcher()}'s <code>trigger()</code> method
     * and the {@link ICaustkController#trigger(Object)} are pointing to the
     * same method, just facaded.
     */
    ICaustkController getController();

    /**
     * Template method; initializes the {@link ICaustkController} and call
     * configuration on the application.
     */
    void create();

    /**
     * Closes the application.
     * 
     * @see OnApplicationClose
     */
    void close();

    /**
     * @see OnApplicationSave
     * @throws IOException
     */
    void save() throws IOException;

    public enum StateChangeKind {

        /**
         * @see ICaustkApplication#initialize()
         */
        Create,

        /**
         * @see ICaustkApplication#save()
         */
        Save,

        /**
         * @see ICaustkApplication#close()
         */
        Close
    }

    public static class OnCausticApplicationStateChange {

        private StateChangeKind kind;

        public StateChangeKind getKind() {
            return kind;
        }

        public OnCausticApplicationStateChange(StateChangeKind kind) {
            this.kind = kind;
        }
    }

}

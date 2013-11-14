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
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.ISoundGenerator;
import com.teotigraphix.caustk.workstation.ICaustkFactory;
import com.teotigraphix.caustk.workstation.RackSet;

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
    ICaustkLogger getLogger();

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
     * Returns the {@link ICaustkFactory} for this application.
     */
    ICaustkFactory getFactory();

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
     * Returns the single instance of the {@link IRack} for the application.
     * <p>
     * The {@link IRack} is not serialized so it's instance stays around the
     * whole application life cycle. The {@link RackSet} is plugged into the
     * rack which is actually the {@link IRack}s internal state.
     */
    IRack getRack();

    /**
     * Initializes the {@link ISoundGenerator}.
     */
    void initialize();

    /**
     * Template method; initializes the {@link ICaustkController} and call
     * configuration on the application.
     */
    void create();

    /**
     * Template method; the last phase in startup after {@link #initialize()}
     * and {@link #create()} have been called.
     * <p>
     * Restore state before first UI is created.
     */
    void run();

    /**
     * Closes the application.
     * 
     * @see OnApplicationClose
     */
    void close();

    /**
     * @see StateChangeKind#Close
     * @throws IOException
     */
    void save() throws IOException;

    public enum StateChangeKind {

        /**
         * @see ICaustkApplication#initialize()
         */
        Create,

        /**
         * @see ICaustkApplication#run()
         */
        Run,

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

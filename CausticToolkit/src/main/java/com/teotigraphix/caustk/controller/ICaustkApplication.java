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

import com.teotigraphix.caustk.controller.core.IApplicationHandler;

/**
 * The {@link ICaustkApplication} API is the startup instrumentation.
 * <p>
 * The whole load, setup sequence is determined by this API template.
 * 
 * @author Michael Schmalle
 */
public interface ICaustkApplication {

    /**
     * The application specific phase strategy.
     * 
     * @param value
     */
    void setApplicationHandler(IApplicationHandler value);

    /**
     * Returns the application's single {@link ICaustkConfiguration}.
     */
    ICaustkConfiguration getConfiguration();

    /**
     * Returns the application's single {@link ICaustkController}.
     */
    ICaustkController getController();

    /**
     * Template method; initializes the {@link ICaustkController} and call
     * configuration on the application.
     */
    void create();

    /**
     * Template method; starts the application processing.
     * 
     * @see ICaustkController#start()
     * @see OnApplicationStart
     */
    //void start();

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

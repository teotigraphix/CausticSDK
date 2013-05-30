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

package com.teotigraphix.caustk.application;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustk.controller.CaustkController;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.sound.ICaustkSoundSource;

/**
 * @author Michael Schmalle
 */
public interface ICaustkConfigurator {

    /**
     * Configures the {@link CaustkController} before startup.
     * 
     * @param controller The single {@link CaustkController} instance.
     */
    void configureController(ICaustkController controller);

    /**
     * Configures the {@link ICaustkSoundSource} before startup.
     * 
     * @param soundSource The single {@link ICaustkSoundSource} instance.
     * @throws CausticException
     */
    void configureSoundSource(ICaustkSoundSource soundSource) throws CausticException;
}

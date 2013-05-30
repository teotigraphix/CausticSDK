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

import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.device.IDeviceFactory;
import com.teotigraphix.caustic.internal.device.DeviceFactory;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustk.controller.CaustkController;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.sound.ICaustkSoundGenerator;

/**
 * @author Michael Schmalle
 */
public interface ICaustkConfiguration {

    String getApplicationId();

    /**
     * Returns a {@link DeviceFactory} that creates caustic components.
     * <p>
     * The {@link SoundSource} is used to create {@link IMachine}s and other
     * components while managing them and using them with {@link Tone}
     * instances.
     * <p>
     * Default returns a {@link GrooveDeviceFactory}.
     */
    IDeviceFactory getDeviceFactory(ICausticEngine engine);

    //--------------------------------------------------------------------------
    // Factory Methods
    //--------------------------------------------------------------------------

    /**
     * Each platform and application needs to implement its sound/part/tone
     * configuration.
     * <p>
     * Impls will create machines through the {@link SoundSource}.
     */
    ICaustkConfigurator getConfigurator();

    /**
     * The main {@link CaustkController} instance that instrumentates the whole
     * application sequencing from patterns, parts, presets, memory and all
     * other things needing controlling.
     * <p>
     * If the device framework was a hierarchy which it kind of is, the
     * {@link CaustkController} is the top device, other than a
     * GrooveBoxApplication.
     * 
     * @param application
     */
    ICaustkController createController(ICaustkApplication application);

    /**
     * Creates the core {@link ICausticEngine} implementation for the desktop or
     * android device.
     * 
     * @param controller The main controller.
     * @return The single instance of the {@link ICaustkSoundGenerator}.
     */
    ICaustkSoundGenerator createSoundGenerator(ICaustkController controller);
}

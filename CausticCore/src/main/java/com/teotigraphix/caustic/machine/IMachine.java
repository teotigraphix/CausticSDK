////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.machine;

import com.teotigraphix.caustic.core.IPersist;
import com.teotigraphix.caustic.core.IRestore;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.device.IDeviceFactory;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;

/**
 * The IMachine interface is the base interface for all Caustic machines.
 * <p>
 * A machine is defined as a tone generator of some kind that can but not always
 * be controlled by an IPatternSequencer.
 * </p>
 * <p>
 * Caustic machines include;
 * </p>
 * <ul>
 * <li>{@link ISubSynth}</li>
 * <li>{@link IPCMSynth}</li>
 * <li>{@link IBassline}</li>
 * <li>{@link IBeatbox}</li>
 * </ul>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IMachine extends IDevice, IRestore, IPersist
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // factory
    //----------------------------------

    /**
     * Returns the factory that is used to create all top level devices and
     * effects.
     */
    @Override
    IDeviceFactory getFactory();

    /**
     * @see #getFactory()
     */
    @Override
    void setFactory(IDeviceFactory value);

    //----------------------------------
    // type
    //----------------------------------

    /**
     * The IMachine type assigned when created by the IRack.
     */
    MachineType getType();

    //----------------------------------
    // sequencer
    //----------------------------------

    /**
     * The pattern sequencer used to add note data to the machine.
     */
    IPatternSequencer getSequencer();

    //----------------------------------
    // presetName
    //----------------------------------

    /**
     * Returns the name of the preset if loaded.
     * <p>
     * There is no path information just the previously loaded preset based on
     * the machine type IE <code>PRESET_NAME.subsynth</code>,
     * <code>PRESET_NAME.pcmsynth</code>, <code>PRESET_NAME.bassline</code>,
     * <code>PRESET_NAME.beatbox</code>.
     */
    String getPresetName();

    /**
     * If the {@link #loadPreset(String)} was called on this machine, the
     * absolute path to the preset file will be returned, <code>null</code>
     * otherwise.
     */
    String getPresetPath();

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Loads a machine preset file into the machine.
     * 
     * @param absolutePath The full path to the valid preset file.
     */
    void loadPreset(String absolutePath);

    /**
     * Saves a machine preset into the /caustic/presets directory.
     * <p>
     * The Core knows which directory to save to.
     * 
     * @param name The name of the preset, no path, just the name.
     */
    void savePreset(String name);
}

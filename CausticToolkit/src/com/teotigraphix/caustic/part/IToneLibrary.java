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

package com.teotigraphix.caustic.part;

import com.teotigraphix.caustic.machine.MachineException;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.ITone;

/**
 * The IToneLibrary interface allows default tones from extension packs to be
 * loaded into an application and used as ITones in IPatches.
 * <p>
 * An ITone is a tone default, once the tone is added to the IPatch, it is still
 * linked to it's original tone but it's settings will no longer reflect that of
 * the original tone. The one setting that will never change from a patch tone
 * to default tone is it's sound source generating machine IE SubSynth
 * </p>
 * .
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IToneLibrary extends IPresetLibrary {

    /**
     * Creates a default {@link ITone} based off of the {@link MachineType}.
     * 
     * @param index The index of the tone, corresponds to the tone's channel in
     *            the rack
     * @param toneId The machine readable tone name.
     * @param toneName The human readable tone name.
     * @param type The tone's machine type.
     * @return A new {@link ITone} initialized.
     * @throws MachineException Error creating tone for machine.
     */
    ITone createDefaultTone(int index, String toneId, String toneName, MachineType type)
            throws MachineException;
}

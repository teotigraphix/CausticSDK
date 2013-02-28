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

import java.io.File;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.IPatch;

/**
 * The IPatchLibrary interface allows default patches from extension packs to be
 * loaded into an application and used as IPatchs in IParts.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPatchLibrary extends IPresetLibrary {

    IPatch createDefaultPatch(MachineType type);

    void loadPreset(IPart part, File file) throws CausticException;

    void savePreset(IPart part) throws CausticException;

    void savePreset(IPart part, File file) throws CausticException;

}

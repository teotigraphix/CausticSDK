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

package com.teotigraphix.caustk.machine;

import java.io.File;
import java.util.UUID;

public interface ICaustkComponent {

    /**
     * Returns the unique id of the component.
     * <p>
     * <strong>Assigned only at construction.</strong>
     */
    UUID getId();

    /**
     * Returns the display name of the component.
     */
    String getName();

    /**
     * Returns the relative path from the owning {@link CaustkLibrary}.
     * <p>
     * The path could be something like;
     * <code>machines/subsynth/Trance/FM Synth Setup.ctkmachine</code>
     * 
     * @see CaustkLibrary#getAbsoluteComponentLocation(ICaustkComponent)
     */
    File getFile();
}

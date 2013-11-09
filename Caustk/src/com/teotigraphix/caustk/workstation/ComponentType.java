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

package com.teotigraphix.caustk.workstation;

import com.teotigraphix.caustk.rack.effect.RackEffect;
import com.teotigraphix.caustk.rack.mixer.MasterDelay;
import com.teotigraphix.caustk.rack.mixer.MasterEqualizer;
import com.teotigraphix.caustk.rack.mixer.MasterLimiter;
import com.teotigraphix.caustk.rack.mixer.MasterReverb;

/**
 * @author Michael Schmalle
 */
public enum ComponentType {

    /**
     * A {@link Library} holds {@link RackSet}, {@link Machine}, {@link Patch},
     * {@link Effect}, {@link Phrase}, {@link MasterMixer} and
     * {@link MasterSequencer} components.
     * <p>
     * Extension: <strong>.clb</strong>
     */
    Library("clb"),

    /**
     * A {@link TrackSet} holds TODO components.
     * <p>
     * Extension: <strong>.cts</strong>
     */
    TrackSet("cls"),

    /**
     * A {@link RackSet} holds {@link Machine} components.
     * <p>
     * Extension: <strong>.csc</strong>
     */
    RackSet("crs"),

    /**
     * A {@link Machine} holds one {@link Patch} and multiple {@link Phrase}
     * components.
     * <p>
     * Extension: <strong>.cmc</strong>
     */
    Machine("cmc"),

    /**
     * A {@link Patch} holds one {@link MachinePreset}, one
     * {@link MasterMixerPreset} and up to two {@link Effect} components.
     * <p>
     * Extension: <strong>.cpt</strong>
     */
    Patch("cpt"),

    /**
     * A {@link Effect} hold up to 2 live or serialized {@link RackEffect}
     * components.
     * <p>
     * Extension: <strong>.cef</strong>
     */
    Effect("cef"),

    /**
     * A {@link Phrase} holds a
     * <p>
     * Extension: <strong>.cph</strong>
     */
    Phrase("cph"),

    /**
     * A {@link PatternSet} holds a
     * <p>
     * Extension: <strong>.cps</strong>
     */
    PatternSet("cps"),

    /**
     * A {@link Pattern} holds a
     * <p>
     * Extension: <strong>.cpn</strong>
     */
    Pattern("cpn"),

    /**
     * A {@link Part} holds a
     * <p>
     * Extension: <strong>.cpr</strong>
     */
    Part("cpr"),

    /**
     * A {@link SongSet} holds a
     * <p>
     * Extension: <strong>.css</strong>
     */
    SongSet("css"),

    /**
     * A {@link Song} holds a
     * <p>
     * Extension: <strong>.csg</strong>
     */
    Song("csg"),

    /**
     * A {@link CastkMasterMixer} holds a live or serialized {@link MasterMixer}
     * which in turn contains a {@link MasterDelay}, {@link MasterReverb},
     * {@link MasterEqualizer} and {@link MasterLimiter} component.
     * <p>
     * Extension: <strong>.cmx</strong>
     */
    MasterMixer("cmx"),

    /**
     * <p>
     * Extension: <strong>.csq</strong>
     */
    MasterSequencer("csq");

    private final String extension;

    public final String getExtension() {
        return extension;
    }

    ComponentType(String extension) {
        this.extension = extension;
    }
}

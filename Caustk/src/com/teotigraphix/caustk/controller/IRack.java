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

import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.Tone;

/**
 * The {@link IRack} is the top API for dealing with {@link Tone}s,
 * {@link Phrase}s.
 * <p>
 * Manages the {@link ISoundMixer}, {@link ISoundSource},
 * {@link ISystemSequencer}, {@link ITrackSequencer}.
 */
public interface IRack extends ICausticEngine {

    ICaustkController getController();

    ISoundSource getSoundSource();

    ISoundMixer getSoundMixer();

    ISystemSequencer getSystemSequencer();

    ITrackSequencer getTrackSequencer();

    float getCurrentSongMeasure();

    float getCurrentBeat();

    void create();

    void update();

}

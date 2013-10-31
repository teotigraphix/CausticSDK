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

package com.teotigraphix.caustk.rack;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.controller.ICaustkFactory;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.machine.Phrase;
import com.teotigraphix.caustk.machine.Scene;
import com.teotigraphix.caustk.rack.tone.Tone;

/**
 * The {@link IRack} is the top API for dealing with {@link Tone}s,
 * {@link Phrase}s.
 * <p>
 * Manages the {@link ISoundMixer}, {@link ISoundSource},
 * {@link ISystemSequencer}, {@link ITrackSequencer}.
 */
public interface IRack extends ICausticEngine, IRestore {

    IDispatcher getDispatcher();

    IDispatcher getGlobalDispatcher();

    ICaustkFactory getFactory();

    //----------------------------------
    // scene
    //----------------------------------

    Scene getScene();

    void setScene(Scene scene);

    ISystemSequencer getSystemSequencer();

    float getCurrentSongMeasure();

    float getCurrentBeat();

    void frameChanged(float delta);

    //----------------------------------
    // SoundSource
    //----------------------------------

    /**
     * Clears all {@link Tone}s from the sound source and resets the core audio
     * rack.
     * 
     * @throws CausticException
     */
    void clearAndReset() throws CausticException;

    boolean isEmpty();

    /**
     * Loads a <code>.caustic</code> file from disk.
     * <p>
     * Note: All clients MUST take responsibility for calling
     * {@link #clearAndReset()} if they wish to remove all state from the sound
     * source.
     * <p>
     * Will only restore the rack state of machines, all tones are not restored
     * at this point.
     * 
     * @param causticFile The absolute location of the <code>.caustic</code>
     *            file with extension.
     * @throws CausticException
     */
    void loadSong(File causticFile) throws CausticException;

    void loadSongRaw(File causticFile) throws CausticException;

    /**
     * Saves a <code>.caustic</code> song file to the <code>caustic/songs</code>
     * driectory.
     * 
     * @param name The simple song name.
     * @return A File representing the location of the saved song.
     */
    File saveSong(String name);

    /**
     * Saves a <code>.caustic</code> song file to the location specified with
     * the file argument.
     * <p>
     * Note; The song file is moved from the caustic/songs directory to the new
     * location.
     * 
     * @param file The location to save the song.
     * @return
     * @throws IOException
     */
    File saveSongAs(File file) throws IOException;
}

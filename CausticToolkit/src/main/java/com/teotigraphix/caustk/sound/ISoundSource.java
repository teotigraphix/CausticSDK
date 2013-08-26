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

package com.teotigraphix.caustk.sound;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;
import com.teotigraphix.caustk.tone.ToneType;

/**
 * @author Michael Schmalle
 */
public interface ISoundSource extends IRestore {

    /**
     * The {@link ISoundSource} dispatcher.
     */
    IDispatcher getDispatcher();

    int getToneCount();

    Collection<Tone> getTones();

    Tone getTone(int index);

    Tone getToneByName(String value);

    int getTranspose();

    void setTranspose(int value);

    //    void noteOn(Tone tone, int pitch, float velocity);
    //
    //    void noteOff(Tone tone, int pitch);

    /**
     * Creates and returns a Tone instance from serialized JSON data.
     * <p>
     * When deserialized, the sound source does not use the index property of
     * the tone, it will assign the next available index to the tone.
     * <p>
     * Use {@link #createScene(LibraryScene)} to recreate a rack session.
     * 
     * @param data Valid serialized Tone data.
     * @throws CausticException
     */
    <T extends Tone> T createTone(String data) throws CausticException;

    // XXX make tone creation generic

    /**
     * @param name
     * @param toneType
     * @throws CausticException
     */
    Tone createTone(String name, ToneType toneType) throws CausticException;

    <T extends Tone> T createTone(String name, Class<? extends Tone> toneClass)
            throws CausticException;

    <T extends Tone> T createTone(int index, String name, Class<? extends Tone> toneClass)
            throws CausticException;

    /**
     * @param index
     * @param name
     * @param toneType
     * @throws CausticException
     */
    Tone createTone(int index, String name, ToneType toneType) throws CausticException;

    /**
     * @param descriptor
     * @throws CausticException
     * @see OnSoundSourceToneAdd
     */
    Tone createTone(ToneDescriptor descriptor) throws CausticException;

    /**
     * @param index
     * @see OnSoundSourceToneRemove
     */
    void destroyTone(int index);

    /**
     * Clears all {@link Tone}s from the sound source and resets the core audio
     * rack.
     */
    void clearAndReset();

    void loadSong(File causticFile) throws CausticException;

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

    public static class OnSoundSourceSongLoad {

        private File file;

        public File getFile() {
            return file;
        }

        public OnSoundSourceSongLoad(File file) {
            this.file = file;
        }
    }

    void createScene(LibraryScene libraryScene) throws CausticException;

    List<Tone> findToneStartsWith(String name);

}

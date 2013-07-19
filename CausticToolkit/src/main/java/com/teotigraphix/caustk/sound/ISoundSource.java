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
import java.util.Collection;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.IRestore;
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

    int getTranspose();

    void setTranspose(int value);

    //    void noteOn(Tone tone, int pitch, float velocity);
    //
    //    void noteOff(Tone tone, int pitch);

    /**
     * @param name
     * @param toneType
     * @throws CausticException
     */
    Tone createTone(String name, ToneType toneType) throws CausticException;

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

    public static class OnSoundSourceSongLoad {

    }

}

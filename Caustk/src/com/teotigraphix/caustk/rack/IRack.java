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

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ICaustkLogger;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.CaustkApplication;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.live.ICaustkFactory;
import com.teotigraphix.caustk.live.Library;
import com.teotigraphix.caustk.live.Machine;
import com.teotigraphix.caustk.live.Phrase;
import com.teotigraphix.caustk.live.Phrase.OnPhraseChange;
import com.teotigraphix.caustk.live.RackSet;
import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;

/**
 * The {@link IRack} API acts as the singleton instance that can load and save
 * <code>.caustic</code> songs.
 * <p>
 * The rack can also hold a {@link RackSet} which is acts as the internal rack
 * state that can be serialized and replaced when new sets need to be loaded
 * from projects.
 * <p>
 * Note: For simple applications, the {@link RackSet} is not required to run the
 * rack. The simple setup of the {@link ICaustkApplication} is the only
 * requirement to use this framework.
 * <p>
 * Calling the static <code>startAndRun()</code> method of the application is
 * all that is needed to initialize the sound engine to start using the
 * {@link ICaustkFactory} and {@link IRack}.
 * 
 * <pre>
 * ISoundGenerator soundGenerator = DesktopSoundGenerator.getInstance();
 * File causticStorageRoot = new File(&quot;C:\\Users\\Teoti\\Documents&quot;);
 * File applicationStorageRoot = new File(causticStorageRoot, &quot;CaustkTests&quot;);
 * 
 * application = CaustkApplication.startAndRun(soundGenerator, causticStorageRoot,
 *         applicationStorageRoot);
 * factory = application.getFactory();
 * </pre>
 * 
 * @author Michael Schmalle
 * @see CaustkApplication#startAndRun(ISoundGenerator, File, File)
 */
public interface IRack extends ICausticEngine, IRestore {

    /**
     * The {@link IRack}'s {@link IDispatcher} instance that it's sub components
     * dispatch their events through.
     * <p>
     * It is safe to listen to {@link OnPhraseChange}, etc events since the rack
     * is a singleton in the app that will never be destroyed whereas the
     * {@link RackSet} will be changed out during a state change. Theoretically
     * the phrase events are bound to the {@link RackSet}'s {@link Phrase}s but
     * since the client is not actually putting it's observer references on the
     * {@link RackSet}s composites, this works in a very decoupled way avoiding
     * memory leaks.
     */
    IDispatcher getDispatcher();

    /**
     * The global dispatcher, this is the {@link ICaustkController}.
     */
    IDispatcher getGlobalDispatcher();

    /**
     * The {@link ICaustkApplication#getLogger()}.
     */
    ICaustkLogger getLogger();

    //----------------------------------
    // rackSet
    //----------------------------------

    /**
     * Returns the current {@link RackSet} that is being used in the rack.
     * <p>
     * A {@link RackSet} is a collection of {@link Machine}s that have been
     * created through the {@link ICaustkFactory}.
     * <p>
     * The {@link RackSet} is fully serializable and can be included in
     * {@link Library}s. The rack set can easily be loaded and unloaded within
     * the rack essentially restoring previous Rack - Machine states.
     * <p>
     * The {@link RackSet} also holds the master sequencer and mixer states as
     * well, so instead of replacing the rack instance in the application, the
     * framework replaces the rack set instance to create or restore state.
     */
    RackSet getRackSet();

    /**
     * Sets the current {@link RackSet} state for the rack.
     * 
     * @param value The new {@link RackSet} that will reinitialize the rack.
     */
    void setRackSet(RackSet value);

    //----------------------------------
    // systemSequencer
    //----------------------------------

    /**
     * The rack's {@link ISystemSequencer} that holds all the OSC messages for
     * the native song sequencer.
     */
    ISystemSequencer getSystemSequencer();

    /**
     * Returns the native core's current song measure.
     * <p>
     * If the outputpanel is not in Song mode, this value is invalid.
     */
    float getCurrentSongMeasure();

    /**
     * Returns the native core's current song or pattern beat.
     * <p>
     * If the {@link ISystemSequencer#getSequencerMode()} is
     * {@link SequencerMode#Song} mode, this value starts at 0 and is continuous
     * as the song plays. If the {@link ISystemSequencer#getSequencerMode()} is
     * {@link SequencerMode#Pattern} mode, the beats will loop from 0..31 (8
     * measures).
     */
    float getCurrentBeat();

    /**
     * Called from an outside client to update the current measure, beat and any
     * other frame based values that need updating.
     * <p>
     * When using the <strong>CaustkGDX</strong> framework, this method is
     * called during the <code>GDXGame#render()</code> call before any UI is
     * drawn. This allows all graphics to render with the current frame values
     * received from the native core.
     * 
     * @param delta The float time change since the last frame update.
     */
    void frameChanged(float delta);

    /**
     * Clears all native rack machines.
     * <p>
     * This method does NOT clear the {@link RackSet} when non <code>null</code>
     * . The {@link RackSet} will callback to this method for the final reset of
     * the native audio system.
     * 
     * @see RackSet#clearMachines()
     * @throws CausticException
     */
    void clearRack() throws CausticException;

    /**
     * Loads a <code>.caustic</code> file from disk.
     * <p>
     * Note: All clients MUST take responsibility for calling
     * {@link #clearRack()} if they wish to remove all state from the native
     * audio system.
     * 
     * @param causticFile The absolute location of the <code>.caustic</code>
     *            file with extension.
     * @throws IOException
     */
    void loadSong(File causticFile) throws IOException;

    /**
     * Saves a <code>.caustic</code> song file to the <code>caustic/songs</code>
     * directory.
     * 
     * @param name The simple song name.
     * @return A File representing the location of the saved song.
     * @throws IOException
     */
    File saveSong(String name) throws IOException;

    /**
     * Saves a <code>.caustic</code> song file to the location specified with
     * the file argument.
     * <p>
     * Note; The song file is moved from the <code>caustic/songs</code>
     * directory to the new location. The original File in the
     * <code>caustic/songs</code> is deleted.
     * 
     * @param file The location to save the song.
     * @return A File representing the saved target location of the song.
     * @throws IOException
     */
    File saveSongAs(File file) throws IOException;
}

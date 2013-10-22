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

package com.teotigraphix.caustk.controller.core;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICausticLogger;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.controller.command.ICommand;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.system.SystemSequencer;
import com.teotigraphix.caustk.sequencer.track.TrackSequencer;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.mixer.SoundMixer;
import com.teotigraphix.caustk.sound.source.SoundSource;

/**
 * The {@link Rack} is a fully serializable state instance.
 */
public class Rack implements IRack {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private transient ISoundGenerator soundGenerator;

    private transient ICaustkController controller;

    private transient IDispatcher dispatcher;

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public IDispatcher getGlobalDispatcher() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ISoundSource soundSource;

    @Tag(1)
    private ISoundMixer soundMixer;

    @Tag(2)
    private ISystemSequencer systemSequencer;

    @Tag(3)
    private ITrackSequencer trackSequencer;

    //----------------------------------
    // soundSource
    //----------------------------------

    public ICaustkController _getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;
        if (dispatcher == null)
            dispatcher = new Dispatcher();

        soundGenerator = controller.getApplication().getConfiguration().getSoundGenerator();
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    @Override
    public ISoundSource getSoundSource() {
        return soundSource;
    }

    //----------------------------------
    // soundMixer
    //----------------------------------

    @Override
    public ISoundMixer getSoundMixer() {
        return soundMixer;
    }

    //----------------------------------
    // systemSequencer
    //----------------------------------

    @Override
    public ISystemSequencer getSystemSequencer() {
        return systemSequencer;
    }

    //----------------------------------
    // trackSequencer
    //----------------------------------

    @Override
    public ITrackSequencer getTrackSequencer() {
        return trackSequencer;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public Rack() {
    }

    public Rack(ICaustkController controller) {
        this.controller = controller;
        this.dispatcher = new Dispatcher();

        ((CaustkController)this.controller).setRack(this);

        soundGenerator = controller.getApplication().getConfiguration().getSoundGenerator();

        soundSource = new SoundSource(this);
        soundMixer = new SoundMixer(this);
        systemSequencer = new SystemSequencer(this);
        trackSequencer = new TrackSequencer(this);

        // create a new TrackSong for the TrackSequencer
        trackSequencer.createSong();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void registerObservers() {
        soundSource.registerObservers();
        soundMixer.registerObservers();
        systemSequencer.registerObservers();
        trackSequencer.registerObservers();

        // XXX this is not going to work unless there is NO way
        // a client can hold onto things if the rack gets recreated
        controller.addComponent(ISoundSource.class, soundSource);
        controller.addComponent(ISoundMixer.class, soundMixer);
        controller.addComponent(ISystemSequencer.class, systemSequencer);
        controller.addComponent(ITrackSequencer.class, trackSequencer);
    }

    @Override
    public void unregisterObservers() {
        soundSource.unregisterObservers();
        soundMixer.unregisterObservers();
        systemSequencer.unregisterObservers();
        trackSequencer.unregisterObservers();
    }

    @Override
    public void update() {
        final float measure = getCurrentSongMeasure();
        final float beat = getCurrentBeat();
        getSystemSequencer().beatUpdate((int)measure, beat);
    }

    //--------------------------------------------------------------------------
    // SoundGenerator
    //--------------------------------------------------------------------------

    @Override
    public final float getCurrentSongMeasure() {
        return soundGenerator.getCurrentSongMeasure();
    }

    @Override
    public final float getCurrentBeat() {
        return soundGenerator.getCurrentBeat();
    }

    //----------------------------------
    // IActivityCycle API
    //----------------------------------

    @Override
    public void onStart() {
        soundGenerator.onStart();
        soundGenerator.onResume();
    }

    @Override
    public void onResume() {
        soundGenerator.onResume();
    }

    @Override
    public void onPause() {
        soundGenerator.onPause();
    }

    @Override
    public void onStop() {
        soundGenerator.onStop();
    }

    @Override
    public void onDestroy() {
        soundGenerator.onDestroy();
    }

    @Override
    public void onRestart() {
        soundGenerator.onRestart();
    }

    @Override
    public void dispose() {
        soundGenerator.dispose();
    }

    //--------------------------------------------------------------------------
    // IDispatcher API
    //--------------------------------------------------------------------------

    @Override
    public void put(String message, Class<? extends ICommand> command) {
        controller.put(message, command);
    }

    @Override
    public void remove(String message) {
        controller.remove(message);
    }

    @Override
    public void execute(String message, Object... args) throws CausticException {
        controller.execute(message, args);
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    // we proxy the actual OSC impl so we can stop, or reroute
    @Override
    public final float sendMessage(String message) {
        return soundGenerator.sendMessage(message);
    }

    @Override
    public final String queryMessage(String message) {
        return soundGenerator.queryMessage(message);
    }

    @Override
    public ICausticLogger getLogger() {
        return controller.getLogger();
    }

    @Override
    public Project getProject() {
        return controller.getProjectManager().getProject();
    }

    @Override
    public Library getLibrary() {
        return controller.getLibraryManager().getSelectedLibrary();
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

}

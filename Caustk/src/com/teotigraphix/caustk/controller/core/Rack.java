
package com.teotigraphix.caustk.controller.core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.system.SystemSequencer;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.caustk.sequencer.track.TrackSequencer;
import com.teotigraphix.caustk.sequencer.track.TrackSong;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.mixer.SoundMixer;
import com.teotigraphix.caustk.sound.mixer.SoundMixer.MixerInput;
import com.teotigraphix.caustk.sound.mixer.SoundMixerChannel;
import com.teotigraphix.caustk.sound.source.SoundSource;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneType;

/**
 * The {@link Rack} is a fully serializable state instance.
 */
public class Rack implements Serializable, ICausticEngine {

    private static final long serialVersionUID = 3465185099859140754L;

    private transient ISoundGenerator soundGenerator;

    private transient ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;

        updateSoundGenerator();
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    private ISoundSource soundSource;

    public ISoundSource getSoundSource() {
        return soundSource;
    }

    //----------------------------------
    // soundMixer
    //----------------------------------

    private ISoundMixer soundMixer;

    public ISoundMixer getSoundMixer() {
        return soundMixer;
    }

    //----------------------------------
    // systemSequencer
    //----------------------------------

    private ISystemSequencer systemSequencer;

    public ISystemSequencer getSystemSequencer() {
        return systemSequencer;
    }

    //----------------------------------
    // trackSequencer
    //----------------------------------

    private ITrackSequencer trackSequencer;

    public ITrackSequencer getTrackSequencer() {
        return trackSequencer;
    }

    public Rack() {
    }

    public Rack(ICaustkController controller) {
        this.controller = controller;
        this.controller.setRack(this);

        updateSoundGenerator();

        soundSource = new SoundSource(this);
        soundMixer = new SoundMixer(this);
        systemSequencer = new SystemSequencer(this);
        trackSequencer = new TrackSequencer(this);
        // create a new TrackSong for the TrackSequencer
        trackSequencer.createSong();
    }

    public void registerObservers() {
        soundSource.registerObservers();
        soundMixer.registerObservers();
        systemSequencer.registerObservers();
        trackSequencer.registerObservers();
    }

    //--------------------------------------------------------------------------
    // SoundGenerator
    //--------------------------------------------------------------------------

    public final float getCurrentSongMeasure() {
        return soundGenerator.getCurrentSongMeasure();
    }

    public final float getCurrentBeat() {
        return soundGenerator.getCurrentBeat();
    }

    //--------------------------------------------------------------------------
    // SystemSequencer
    //--------------------------------------------------------------------------

    public float getTempo() {
        return systemSequencer.getTempo();
    }

    public void setTempo(float value) {
        systemSequencer.setTempo(value);
    }

    public boolean isPlaying() {
        return systemSequencer.isPlaying();
    }

    public void stop() {
        systemSequencer.stop();
    }

    public void play(SequencerMode sequencerMode) {
        systemSequencer.play(sequencerMode);
    }

    public int getCurrentSixteenthStep() {
        return systemSequencer.getCurrentSixteenthStep();
    }

    public void addPattern(Tone tone, int bank, int pattern, int start, int end)
            throws CausticException {
        systemSequencer.addPattern(tone, bank, pattern, start, end);
    }

    public void removePattern(Tone tone, int start, int end) throws CausticException {
        systemSequencer.removePattern(tone, start, end);
    }

    //--------------------------------------------------------------------------
    // SoundMixer
    //--------------------------------------------------------------------------

    public void setVolume(float value) {
        getSoundMixer().getMasterMixer().setVolume(value);
    }

    public float getVolume() {
        return getSoundMixer().getMasterMixer().getVolume();
    }

    public SoundMixerChannel getMixerChannel(int index) {
        return getSoundMixer().getChannel(index);
    }

    public void executeSetValue(int toneIndex, MixerInput input, Number value) {
        getSoundMixer().executeSetValue(toneIndex, input, value);
    }

    //--------------------------------------------------------------------------
    // Tones
    //--------------------------------------------------------------------------

    public void clearAndReset() {
        soundSource.clearAndReset();
    }

    public void loadSong(File absoluteCausticFile) throws CausticException {
        soundSource.loadSong(absoluteCausticFile);
    }

    public boolean hasTone(int index) {
        return soundSource.getTone(index) != null;
    }

    public Collection<Tone> getTones() {
        return soundSource.getTones();
    }

    public Tone getTone(int index) {
        return soundSource.getTone(index);
    }

    public Tone createTone(String name, Class<? extends Tone> toneClass) throws CausticException {
        return soundSource.createTone(name, toneClass);
    }

    public Tone createTone(String name, ToneType toneType) throws CausticException {
        return soundSource.createTone(name, toneType);
    }

    public void saveSong(String name) throws IOException {
        soundSource.saveSong(name);
    }

    public void saveSongAs(File absoluteTargetSongFile) throws IOException {
        soundSource.saveSongAs(absoluteTargetSongFile);
    }

    //--------------------------------------------------------------------------
    // IActivityCycle API
    //--------------------------------------------------------------------------

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

    public void close() {
    }

    public void update() {
        final float measure = getCurrentSongMeasure();
        final float beat = getCurrentBeat();
        getSystemSequencer().beatUpdate((int)measure, beat);
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void updateSoundGenerator() {
        soundGenerator = controller.getApplication().getConfiguration().getSoundGenerator();
        soundGenerator.initialize();
    }

    //--------------------------------------------------------------------------
    // TrackSequencer
    //--------------------------------------------------------------------------

    public TrackSong getTrackSong() {
        return trackSequencer.getTrackSong();
    }

    public TrackSong createSong(File file) throws IOException {
        return trackSequencer.createSong(file);
    }

    public void setCurrentTrack(int index) {
        trackSequencer.setCurrentTrack(index);
    }

    public Track getTrack(int index) {
        return trackSequencer.getTrack(index);
    }

    public Collection<Track> getTracks() {
        return trackSequencer.getTracks();
    }

}

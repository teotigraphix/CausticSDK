
package com.teotigraphix.caustk.track;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;

public interface ITrackSequencer extends IControllerComponent {

    @Override
    IDispatcher getDispatcher();

    /**
     * Returns the current track/tone index.
     */
    int getCurrentTrack();

    /**
     * Sets the current track index.
     * 
     * @param value Valid values are existing tracks 0-13.
     * @throws IllegalArgumentException
     */
    void setCurrentTrack(int value);

    /**
     * Returns the selected bank of the {@link #getSelectedTrack()}.
     */
    int getCurrentBank();

    int getCurrentBank(int trackIndex);

    int getCurrentPattern();

    int getCurrentPattern(int trackIndex);

    /**
     * Returns whether the sequencer contains tracks.
     * <p>
     * A track is created in response to the {@link OnSoundSourceToneAdd} event,
     * a track is removed in response to the {@link OnSoundSourceToneRemove}
     * Event.
     */
    boolean hasTracks();

    /**
     * Returns the selected/focused track in the sequencer.
     */
    TrackChannel getSelectedTrack();

    /**
     * Returns a collection of {@link TrackChannel}s that have been created due
     * to {@link Tone} creation in the {@link ISoundSource}.
     */
    Collection<TrackChannel> getTracks();

    /**
     * Returns a {@link TrackChannel} at the specified index.
     * <p>
     * If the channel has not been created, a new {@link TrackChannel} instance
     * is created and placed in the sequencer.
     * 
     * @param index The tone index
     */
    // XXX I am not sure this belongs in the public api
    TrackChannel getTrack(int index);

    /**
     * Creates a new {@link TrackSong}.
     * 
     * @param songFile The absolute path to the file, the <code>.caustic</code>
     *            file is saved in the same directory with the same name as this
     *            song file.
     * @throws IOException
     */
    void create(File songFile) throws IOException;

    public enum PropertyChangeKind {

        /**
         * @see TrackChannel#setCurrentBank(int)
         */
        Bank,

        /**
         * @see TrackChannel#setCurrentPattern(int)
         */
        Pattern,

        /**
         * @see TrackPhrase#setLength(int)
         */
        Length,

        /**
         * @see TrackPhrase#setNoteData(String)
         */
        NoteData,

        /**
         * @see TrackPhrase#addNote(int, float, float, float, int)
         */
        NoteAdd,

        /**
         * @see TrackPhrase#removeNote(int, float)
         */
        NoteRemove,

        /**
         * @see TrackPhrase#setPlayMeasure(int)
         */
        PlayMeasure,

        /**
         * @see TrackPhrase#setEditMeasure(int)
         */
        EditMeasure
    }

    public static class OnTrackSequencerPropertyChange {

        final private PropertyChangeKind kind;

        public PropertyChangeKind getKind() {
            return kind;
        }

        TrackChannel trackChannel;

        public TrackChannel getTrackChannel() {
            return trackChannel;
        }

        private TrackPhrase trackPhrase;

        public TrackPhrase getTrackPhrase() {
            return trackPhrase;
        }

        private PhraseNote phraseNote;

        public PhraseNote getPhraseNote() {
            return phraseNote;
        }

        public OnTrackSequencerPropertyChange(PropertyChangeKind kind, TrackChannel trackChannel) {
            this.kind = kind;
            this.trackChannel = trackChannel;
        }

        public OnTrackSequencerPropertyChange(PropertyChangeKind kind, TrackPhrase trackPhrase) {
            this.kind = kind;
            this.trackPhrase = trackPhrase;
            trackChannel = trackPhrase.getController().getTrackSequencer()
                    .getTrack(trackPhrase.getToneIndex());
        }

        public OnTrackSequencerPropertyChange(PropertyChangeKind kind, TrackPhrase trackPhrase,
                PhraseNote phraseNote) {
            this.kind = kind;
            this.trackPhrase = trackPhrase;
            this.phraseNote = phraseNote;
            trackChannel = trackPhrase.getController().getTrackSequencer()
                    .getTrack(trackPhrase.getToneIndex());
        }
    }

    public static class OnTrackSequencerLoad {
    }

    public static class OnTrackSequencerCurrentTrackChange {

        private int index;

        public int getIndex() {
            return index;
        }

        public OnTrackSequencerCurrentTrackChange(int index) {
            this.index = index;
        }
    }

}

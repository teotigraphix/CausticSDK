
package com.teotigraphix.caustk.sequencer.track;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackPhraseChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackChange;
import com.teotigraphix.caustk.tone.Tone;

public class TrackSequencerHandlers {

    private final TrackSequencer trackSequencer;

    private final IDispatcher getDispatcher() {
        return trackSequencer;
    }

    public TrackSequencerHandlers(TrackSequencer trackSequencer) {
        this.trackSequencer = trackSequencer;
        // since we register in the constructor, we always know we will be called first
        registerObservers();
    }

    void registerObservers() {
        getDispatcher().register(OnTrackChange.class, onTrackPropertyChange);
        getDispatcher().register(OnTrackPhraseChange.class, onTrackPhrasePropertyChange);
    }

    void unregisterObservers() {
        getDispatcher().unregister(onTrackPropertyChange);
        getDispatcher().unregister(onTrackPhrasePropertyChange);
    }

    //--------------------------------------------------------------------------
    // Handlers
    //--------------------------------------------------------------------------

    private EventObserver<OnTrackChange> onTrackPropertyChange = new EventObserver<OnTrackChange>() {
        @Override
        public void trigger(OnTrackChange object) {
            final Track track = object.getTrack();
            switch (object.getKind()) {

                case Bank:
                    int bank = track.getCurrentBank();
                    track.getTone().getPatternSequencer().setSelectedBank(bank);
                    break;

                case Pattern:
                    int pattern = track.getCurrentPattern();
                    track.getTone().getPatternSequencer().setSelectedPattern(pattern);
                    break;

                default:
                    break;

            }
        }
    };

    private EventObserver<OnTrackPhraseChange> onTrackPhrasePropertyChange = new EventObserver<OnTrackPhraseChange>() {
        @Override
        public void trigger(OnTrackPhraseChange object) {
            Tone tone = null;
            PhraseNote note = null;
            switch (object.getKind()) {
                case Length:
                    tone = object.getTrackPhrase().getTone();
                    tone.getPatternSequencer().setLength(object.getTrackPhrase().getBank(),
                            object.getTrackPhrase().getPattern(),
                            object.getTrackPhrase().getLength());
                    break;

                case NoteData:
                    tone = object.getTrackPhrase().getTone();
                    tone.getPatternSequencer().assignNoteData(object.getTrackPhrase().getBank(),
                            object.getTrackPhrase().getPattern(),
                            object.getTrackPhrase().getNoteData());
                    break;

                case EditMeasure:
                    break;

                case PlayMeasure:
                    break;

                case NoteAdd:
                    tone = object.getTrackPhrase().getTone();
                    note = object.getPhraseNote();
                    tone.getPatternSequencer().addNote(object.getTrackPhrase().getBank(),
                            object.getTrackPhrase().getPattern(), note.getPitch(), note.getStart(),
                            note.getEnd(), note.getVelocity(), note.getFlags());
                    break;

                case NoteRemove:
                    tone = object.getTrackPhrase().getTone();
                    note = object.getPhraseNote();
                    tone.getPatternSequencer().removeNote(note.getPitch(), note.getStart());
                    break;

                case ClearMeasure:
                    break;

                case Position:
                    break;

                case Scale:
                    break;

                default:
                    break;

            }
        }
    };

}

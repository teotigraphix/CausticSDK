
package com.teotigraphix.caustk.sequencer.track;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackSequencerPropertyChange;
import com.teotigraphix.caustk.tone.Tone;

public class TrackSequencerHandlers {

    private final TrackSequencer trackSequencer;

    private final IDispatcher getDispatcher() {
        return trackSequencer.getDispatcher();
    }

    public TrackSequencerHandlers(TrackSequencer trackSequencer) {
        this.trackSequencer = trackSequencer;
        // since we register in the constructor, we always know we will be called first
        registerObservers();
    }

    void registerObservers() {
        getDispatcher().register(OnTrackSequencerPropertyChange.class, propertyChange);
    }

    void unregisterObservers() {
        getDispatcher().unregister(propertyChange);
    }

    //--------------------------------------------------------------------------
    // Handlers
    //--------------------------------------------------------------------------

    // Bank

    private EventObserver<OnTrackSequencerPropertyChange> propertyChange = new EventObserver<OnTrackSequencerPropertyChange>() {
        @Override
        public void trigger(OnTrackSequencerPropertyChange object) {
            Track channel = null;
            Tone tone = null;
            PhraseNote note = null;
            switch (object.getKind()) {

                case Bank:
                    channel = object.getTrackChannel();
                    int bank = channel.getCurrentBank();
                    channel.getTone().getPatternSequencer().setSelectedBank(bank);
                    break;

                case Pattern:
                    channel = object.getTrackChannel();
                    int pattern = object.getTrackChannel().getCurrentPattern();
                    channel.getTone().getPatternSequencer().setSelectedPattern(pattern);
                    break;

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
                default:
                    break;

            }
        }
    };

}


package com.teotigraphix.caustk.track;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.track.ITrackSequencer.OnTrackSequencerPropertyChange;

public class TrackSequencerHandlers {

    private final TrackSequencer trackSequencer;

    private final IDispatcher getDispatcher() {
        return trackSequencer.getDispatcher();
    }

    public TrackSequencerHandlers(TrackSequencer trackSequencer) {
        this.trackSequencer = trackSequencer;

        registerObservers();
    }

    void registerObservers() {
        getDispatcher().register(OnTrackSequencerPropertyChange.class, bankChange);
    }

    void unregisterObservers() {
        getDispatcher().unregister(bankChange);
    }

    //--------------------------------------------------------------------------
    // Handlers
    //--------------------------------------------------------------------------

    // Bank

    private EventObserver<OnTrackSequencerPropertyChange> bankChange = new EventObserver<OnTrackSequencerPropertyChange>() {
        @Override
        public void trigger(OnTrackSequencerPropertyChange object) {
            TrackChannel channel = null;
            Tone tone = null;
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
                    tone.getPatternSequencer().setLength(object.getTrackPhrase().getLength());
                    break;

                case NoteData:
                    tone = object.getTrackPhrase().getTone();
                    tone.getPatternSequencer()
                            .assignNoteData(object.getTrackPhrase().getNoteData());
                    break;

                case EditMeasure:

                    break;

                case PlayMeasure:

                    break;

                default:
                    break;
            }
        }
    };

}

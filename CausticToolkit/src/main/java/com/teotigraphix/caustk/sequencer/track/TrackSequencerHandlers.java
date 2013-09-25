
package com.teotigraphix.caustk.sequencer.track;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnPhraseChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackChange;
import com.teotigraphix.caustk.tone.Tone;

public class TrackSequencerHandlers {

    private final TrackSequencer trackSequencer;

    private final IDispatcher getDispatcher() {
        return trackSequencer.getController();
    }

    public TrackSequencerHandlers(TrackSequencer trackSequencer) {
        this.trackSequencer = trackSequencer;
        // since we register in the constructor, we always know we will be called first
        //registerObservers();
    }

    void onAttach() {
        getDispatcher().register(OnTrackChange.class, onTrackPropertyChange);
        getDispatcher().register(OnPhraseChange.class, onTrackPhrasePropertyChange);
    }

    void unregisterObservers() {
        getDispatcher().unregister(onTrackPropertyChange);
        getDispatcher().unregister(onTrackPhrasePropertyChange);
    }

    //--------------------------------------------------------------------------
    // Handlers
    //--------------------------------------------------------------------------

    private transient EventObserver<OnTrackChange> onTrackPropertyChange = new EventObserver<OnTrackChange>() {
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

    private transient EventObserver<OnPhraseChange> onTrackPhrasePropertyChange = new EventObserver<OnPhraseChange>() {
        @Override
        public void trigger(OnPhraseChange object) {
            Tone tone = null;
            //Note note = null;
            switch (object.getKind()) {
                case Length:
                    tone = object.getPhrase().getTone();
                    tone.getPatternSequencer().setLength(object.getPhrase().getBank(),
                            object.getPhrase().getPattern(), object.getPhrase().getLength());
                    break;

                case NoteData:
                    break;

                case EditMeasure:
                    break;

                case PlayMeasure:
                    break;

                case NoteAdd:
                    break;

                case NoteRemove:
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

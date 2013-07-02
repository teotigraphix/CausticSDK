
package com.teotigraphix.caustk.project;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustk.library.LibraryPhrase;

/*

What is a Track?
- a channel in a rack (machine) which pattern and automation state is saved over time.

A track has;
- index, the current Tone index
- name, the current Tone name, can be set different than the tone name temp
- current beat, measure proxied from the parent Song
- soft transient reference to its containing song

- patterns, Map<Integer, TrackPattern> NO REFERENCE data structure

- add/remove/get TrackPattern API
- some type of listener API

*/

public class Track {
    // the phrase map is keyed on the measure start
    Map<Integer, TrackPhrase> map = new HashMap<Integer, TrackPhrase>();

    private transient IDispatcher dispatcher;

    /**
     * The {@link TrackSong#getDispatcher()}.
     */
    public final IDispatcher getDispatcher() {
        return dispatcher;
    }

    public final void setDispatcher(IDispatcher value) {
        dispatcher = value;
    }

    private final int index;

    public final int getIndex() {
        return index;
    }

    public Track(int index) {
        this.index = index;
    }

    public TrackPhrase addPhrase(int measure, LibraryPhrase phrase) throws CausticException {
        if (map.containsKey(measure))
            throw new CausticException("Patterns contain phrase at: " + measure);

        TrackPhrase trackPhrase = new TrackPhrase();
        trackPhrase.setBankIndex(0);
        trackPhrase.setPatternIndex(0);
        trackPhrase.setStartMeasure(measure);
        trackPhrase.setEndMeasure(measure + phrase.getLength());
        map.put(measure, trackPhrase);
        getDispatcher().trigger(new OnTrackPhraseAdd(this, trackPhrase));
        return trackPhrase;
    }

    public void removePhrase(TrackPhrase trackPhrase) throws CausticException {
        int measure = trackPhrase.getStartMeasure();
        if (!map.containsKey(measure))
            throw new CausticException("Patterns does not contain phrase at: " + measure);
        map.remove(measure);
        getDispatcher().trigger(new OnTrackPhraseRemove(this, trackPhrase));
    }

    public static class TrackEvent {

        private Track track;

        public final Track getTrack() {
            return track;
        }

        public TrackEvent(Track track) {
            this.track = track;
        }

    }

    public static class OnTrackPhraseAdd extends TrackEvent {

        private TrackPhrase trackPhrase;

        public OnTrackPhraseAdd(Track track, TrackPhrase trackPhrase) {
            super(track);
            this.trackPhrase = trackPhrase;
        }

        public final TrackPhrase getTrackPhrase() {
            return trackPhrase;
        }

    }

    public static class OnTrackPhraseRemove extends TrackEvent {

        private TrackPhrase trackPhrase;

        public OnTrackPhraseRemove(Track track, TrackPhrase trackPhrase) {
            super(track);
            this.trackPhrase = trackPhrase;
        }

        public final TrackPhrase getTrackPhrase() {
            return trackPhrase;
        }

    }

}

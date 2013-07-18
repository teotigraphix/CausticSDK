////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.project;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ISerialize;
import com.teotigraphix.caustk.library.vo.EffectRackInfo;
import com.teotigraphix.caustk.library.vo.MixerPanelInfo;
import com.teotigraphix.caustk.library.vo.RackInfo;
import com.teotigraphix.caustk.project.Track.OnTrackPhraseAdd;
import com.teotigraphix.caustk.project.Track.OnTrackPhraseRemove;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class TrackSong extends Song implements ISerialize {

    private transient ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    public final void setController(ICaustkController value) {
        controller = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  ITrackSong API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  rackInfo
    //----------------------------------

    private RackInfo rackInfo;

    public RackInfo getRackInfo() {
        return rackInfo;
    }

    public void setRackInfo(RackInfo value) {
        rackInfo = value;
    }

    //----------------------------------
    //  mixerInfo
    //----------------------------------

    private MixerPanelInfo mixerInfo;

    public MixerPanelInfo getMixerInfo() {
        return mixerInfo;
    }

    public void setMixerInfo(MixerPanelInfo value) {
        mixerInfo = value;
    }

    //----------------------------------
    //  effectRackInfo
    //----------------------------------

    private EffectRackInfo effectRackInfo;

    public EffectRackInfo getEffectRackInfo() {
        return effectRackInfo;
    }

    public void setEffectRackInfo(EffectRackInfo value) {
        effectRackInfo = value;
    }

    //----------------------------------
    //  file
    //----------------------------------

    private File file;

    /**
     * Returns the relative song file path from the base <code>songs</code>
     * directory.
     * <p>
     * Use the {@link ISongManager} to create an absolute file path for this
     * song.
     */
    public final File getFile() {
        return file;
    }

    public final void setFile(File value) {
        file = value;
    }

    //----------------------------------
    //  tracks
    //----------------------------------

    private int numTracks = -1;

    public int getNumTracks() {
        return numTracks;
    }

    /**
     * Resets the number of {@link Track}s int he song.
     * <p>
     * Setting this property will reset all data and tracks.
     * 
     * @param value The number of tracks in the song.
     */
    public void setNumTracks(int value) {
        numTracks = value;
        createTracks();
        initializeTracks();
    }

    private void createTracks() {
        tracks.clear();
        for (int i = 0; i < numTracks; i++) {
            Track track = new Track();
            track.setIndex(i);
            tracks.put(i, track);
        }
    }

    /**
     * Initializes the {@link Track} instance.
     * <p>
     * Must be called after a {@link TrackSong} deserialization.
     */
    public void initializeTracks() {
        for (Track track : tracks.values()) {
            track.setDispatcher(getDispatcher());
            getDispatcher().register(OnTrackPhraseAdd.class, onTrackPhraseHandler);
            getDispatcher().register(OnTrackPhraseRemove.class, onTrackPhraseRemoveHandler);
        }
    }

    private transient EventObserver<OnTrackPhraseAdd> onTrackPhraseHandler = new EventObserver<OnTrackPhraseAdd>() {
        @Override
        public void trigger(OnTrackPhraseAdd object) {
            Track track = object.getTrack();
            TrackItem trackItem = object.getItem();
            //            IMachine machine = controller.getSoundSource().getTone(track.getIndex()).getMachine();
            //            int bank = trackItem.getBankIndex();
            //            int pattern = trackItem.getPatternIndex();
            //            int start = trackItem.getStartMeasure();
            //            int end = trackItem.getEndMeasure();
            //            // add the track to the song sequencer
            //            controller.getSystemSequencer().addPattern(machine, bank, pattern, start, end);
        }
    };

    private transient EventObserver<OnTrackPhraseRemove> onTrackPhraseRemoveHandler = new EventObserver<OnTrackPhraseRemove>() {
        @Override
        public void trigger(OnTrackPhraseRemove object) {
            Track track = object.getTrack();
            TrackItem trackItem = object.getItem();
            //            IMachine machine = controller.getSoundSource().getTone(track.getIndex()).getMachine();
            //            int start = trackItem.getStartMeasure();
            //            int end = trackItem.getEndMeasure();
            //            // remove the track to the song sequencer
            //            controller.getSystemSequencer().removePattern(machine, start, end);
        }
    };

    //----------------------------------
    //  tracks
    //----------------------------------

    private Map<Integer, Track> tracks = new HashMap<Integer, Track>();

    /*
     * The last pattern in the song in All tracks. This is used to easily
     * calculate the measure length of the song.
     */
    private TrackItem lastPatternInTracks;

    public Collection<Track> getTracks() {
        return Collections.unmodifiableCollection(tracks.values());
    }

    @Override
    public int getNumBeats() {
        if (lastPatternInTracks == null)
            return 0;
        int measures = lastPatternInTracks.getEndMeasure();
        return measures * 4;
    }

    @Override
    public int getNumMeasures() {
        if (lastPatternInTracks == null)
            return 0;
        // 0 index, we need to use the end measure that is measures + 1
        int measures = lastPatternInTracks.getEndMeasure();
        return measures;
    }

    @Override
    public int getTotalTime() {
        float bpm = getBPM();
        float timeInSec = 60 / bpm;
        float totalNumBeats = getNumBeats() + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    @Override
    public int getCurrentTime() {
        float bpm = getBPM();
        float timeInSec = 60 / bpm;
        float totalNumBeats = (getCurrentMeasure() * 4) + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    public TrackSong() {
        super();
    }

    /**
     * Returns the {@link Track} at the specified index.
     * 
     * @param index The tack index.
     */
    public Track getTrack(int index) {
        return tracks.get(index);
    }

    /**
     * Clears all the tracks {@link TrackPhrase}s.
     * 
     * @throws CausticException
     */
    public void clearTracks() throws CausticException {
        for (Track track : tracks.values()) {
            track.clearPhrases();
        }
    }

    @Override
    public void sleep() {
        TrackUtils.refreshTrackSongInfos(controller, this);
        for (Track track : tracks.values()) {
            track.sleep();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        initializeTracks();
        for (Track track : tracks.values()) {
            track.wakeup(controller);
        }
    }

}

/*

RackInfo

<?xml version="1.0" encoding="UTF-8"?>
<rack>
    <machine active="1" id="LOWLEAD" index="0" patchId="56c054c8-1dab-449c-89d6-d31249e9dddc" type="subsynth"/>
    <machine active="1" id="HIGHLEAD" index="1" patchId="c3c71220-68bd-457d-8f95-f2af0fda4b6e" type="subsynth"/>
    <machine active="1" id="BASS" index="2" patchId="ba868cf2-ef6d-49b2-a10f-591c1d853a9c" type="subsynth"/>
    <machine active="1" id="MELODY" index="3" patchId="0e3932e8-301e-4aa1-a7e8-865935e7f46e" type="bassline"/>
    <machine active="1" id="STRINGS" index="4" patchId="dbc19f2a-5bc6-426b-bff0-76b6efd184ea" type="pcmsynth"/>
    <machine active="1" id="DRUMSIES" index="5" patchId="a146b131-d14d-4828-97b0-369c1accfa2d" type="beatbox"/>
</rack>

MixerInfo
<?xml version="1.0" encoding="UTF-8"?>
<mixer>
    <master eq_bass="0.19565237" eq_high="0.100000024" eq_mid="0.0" id="-1" index="-1" volume="1.1478264"/>
    <channels>
        <channel delay_send="0.504348" eq_bass="-0.008696437" eq_high="0.06956482" eq_mid="1.1920929E-7" id="0" index="0" mute="0" pan="0.0" reverb_send="0.05869567" solo="0" stereo_width="0.99609375" volume="1.2782611"/>
        <channel delay_send="0.5000001" eq_bass="-1.0" eq_high="0.02666676" eq_mid="0.034783125" id="1" index="1" mute="0" pan="0.0" reverb_send="0.0" solo="0" stereo_width="0.0" volume="1.3652176"/>
        <channel delay_send="0.0" eq_bass="1.0" eq_high="-0.008695722" eq_mid="0.0" id="2" index="2" mute="0" pan="0.0" reverb_send="0.0" solo="0" stereo_width="0.0" volume="1.2782608"/>
        <channel delay_send="0.52173936" eq_bass="-1.0" eq_high="-2.9802322E-7" eq_mid="-0.026086628" id="3" index="3" mute="0" pan="0.18260896" reverb_send="0.07173912" solo="0" stereo_width="0.0" volume="1.4383385"/>
        <channel delay_send="0.0" eq_bass="-0.017391145" eq_high="0.38260782" eq_mid="0.0" id="4" index="4" mute="0" pan="0.0" reverb_send="0.07391316" solo="0" stereo_width="0.53269273" volume="1.3913045"/>
        <channel delay_send="0.0" eq_bass="1.0" eq_high="5.9604645E-7" eq_mid="-0.008694708" id="5" index="5" mute="0" pan="0.0" reverb_send="0.0" solo="0" stereo_width="0.99609375" volume="0.99999934"/>
    </channels>
    <delay feedback="0.7565215" stereo="1" time="5"/>
    <reverb damping="0.0" room="0.9" stereo="1"/>
</mixer>
*/


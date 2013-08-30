
package com.teotigraphix.caustk.track;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.track.ITrackSequencer.OnTrackSequencerCurrentTrackChange;

public class TrackSong implements ISerialize {

    private transient ICaustkController controller;

    private Map<Integer, TrackChannel> tracks = new HashMap<Integer, TrackChannel>();

    IDispatcher getDispatcher() {
        return controller.getTrackSequencer().getDispatcher();
    }

    //----------------------------------
    // file
    //----------------------------------

    public boolean exists() {
        return file != null;
    }

    private File file;

    public final File getFile() {
        return file;
    }

    void setFile(File value) {
        file = value;
    }

    public final File getDirectory() {
        if (file == null)
            return null;
        return file.getParentFile();
    }

    public String getFileName() {
        if (file == null)
            return null;
        return file.getName().replace(".ctks", "");
    }

    public File getCausticFile() {
        if (file == null)
            return null;
        return new File(getDirectory(), getFileName() + ".caustic");
    }

    public File getAbsoluteCausticFile() {
        if (file == null)
            return null;
        File absoluteFile = controller.getProjectManager().getProject().getResource(file.getPath())
                .getAbsoluteFile();
        return new File(absoluteFile.getParentFile(), getFileName() + ".caustic");
    }

    //----------------------------------
    // currentTrack
    //----------------------------------

    private int currentTrack = -1;

    public int getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(int value) {
        if (currentTrack < 0 || currentTrack > 13)
            throw new IllegalArgumentException("Illigal track index");
        if (tracks.containsKey(value))
            throw new IllegalArgumentException("Track index does not exist;" + value);
        if (value == currentTrack)
            return;
        currentTrack = value;
        getDispatcher().trigger(new OnTrackSequencerCurrentTrackChange(currentTrack));
    }

    //----------------------------------
    // currentBank
    //----------------------------------

    public int getCurrentBank() {
        return getCurrentBank(currentTrack);
    }

    public int getCurrentBank(int trackIndex) {
        return getTrack(trackIndex).getCurrentBank();
    }

    //----------------------------------
    // currentPattern
    //----------------------------------

    public int getCurrentPattern() {
        return getCurrentPattern(currentTrack);
    }

    public int getCurrentPattern(int trackIndex) {
        return getTrack(trackIndex).getCurrentPattern();
    }

    //----------------------------------
    // track
    //----------------------------------

    public boolean hasTracks() {
        return tracks.size() > 0;
    }

    public Collection<TrackChannel> getTracks() {
        return tracks.values();
    }

    public TrackChannel getSelectedTrack() {
        return getTrack(currentTrack);
    }

    public TrackChannel getTrack(int index) {
        TrackChannel track = tracks.get(index);
        if (track == null) {
            track = new TrackChannel(controller, index);
            tracks.put(index, track);
        }
        return track;
    }

    public TrackChannel findTrack(int index) {
        return tracks.get(index);
    }

    public TrackPhrase getPhrase(int toneIndex, int bankIndex, int patterIndex) {
        return getTrack(toneIndex).getPhrase(bankIndex, patterIndex);
    }

    public TrackSong() {
    }

    public TrackSong(File file) {
        this.file = file;
    }

    @Override
    public void sleep() {
        // save the .caustic file
        try {
            File absoluteTargetSongFile = getAbsoluteCausticFile();
            controller.getSoundSource().saveSongAs(absoluteTargetSongFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (TrackChannel item : tracks.values()) {
            item.wakeup(controller);
        }
    }

    void toneAdd(Tone tone) {
        TrackChannel channel = getTrack(tone.getIndex());
        tracks.put(tone.getIndex(), channel);
        channel.onAdded();
    }

    void toneRemove(Tone tone) {
        TrackChannel channel = tracks.remove(tone.getIndex());
        channel.onRemoved();
    }

}

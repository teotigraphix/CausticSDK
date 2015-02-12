
package com.teotigraphix.caustk.gdx.app.model.machine;

import com.teotigraphix.caustk.node.machine.MachineType;
import com.teotigraphix.caustk.node.machine.sequencer.TrackChannel;

public class SongFileMachine {

    private int index;

    private String name;

    private MachineType type;

    private TrackChannel track;

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public MachineType getType() {
        return type;
    }

    public TrackChannel getTrack() {
        return track;
    }

    public SongFileMachine(int index, String name, MachineType type, TrackChannel track) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.track = track;

    }

}

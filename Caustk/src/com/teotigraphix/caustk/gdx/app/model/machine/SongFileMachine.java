
package com.teotigraphix.caustk.gdx.app.model.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.machine.MachineType;
import com.teotigraphix.caustk.node.machine.sequencer.TrackChannel;

public class SongFileMachine {

    @Tag(0)
    private int index;

    @Tag(1)
    private String name;

    @Tag(2)
    private MachineType type;

    @Tag(5)
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

    SongFileMachine() {
    }

    public SongFileMachine(int index, String name, MachineType type, TrackChannel track) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.track = track;
    }
}


package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import android.annotation.SuppressLint;

import com.teotigraphix.caustk.core.CausticFile;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.gdx.app.model.machine.SongFileMachine;
import com.teotigraphix.caustk.node.machine.MachineType;
import com.teotigraphix.caustk.node.machine.sequencer.TrackChannel;
import com.teotigraphix.caustk.utils.node.PhraseUtils;

public class SongFile extends CausticFile {

    /*
     * - name
     * - path
     * - tempo
     * - number of sequenced measures
     * - file size
     * - machine names/types
     * - machine effects/types
     * - machine patterns (string list)
     * 
     * 
     * 
     */

    private boolean selected;

    private boolean playing;

    long size;

    int bpm;

    int numMeasures;

    private int minutes;

    private int seconds;

    private boolean sequenced;

    private Map<Integer, SongFileMachine> machines;

    private int measureCount;

    private float fbpm;

    public String getDisplayName() {
        return getBaseName() + " - " + getMinutes() + ":" + getSeconds() + " - " + getBpm()
                + "bpm - " + humanReadableByteCount(getSize(), false);
    }

    public String getBaseName() {
        return FilenameUtils.getBaseName(getFile().getName());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public Map<Integer, SongFileMachine> getMachines() {
        return machines;
    }

    public long getSize() {
        return size;
    }

    public int getBpm() {
        return bpm;
    }

    public int getNumMeasures() {
        return numMeasures;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isSequenced() {
        return sequenced;
    }

    public void setSequenced(boolean sequenced) {
        this.sequenced = sequenced;
    }

    public int getMeasureCount() {
        return measureCount;
    }

    SongFile(File file) {
        super(file);
        machines = new HashMap<Integer, SongFileMachine>();
    }

    public static SongFile create(File file) {
        return new SongFile(file);
    }

    public Map<Integer, TrackChannel> getTracks() {
        HashMap<Integer, TrackChannel> result = new HashMap<Integer, TrackChannel>();
        for (SongFileMachine machine : machines.values()) {
            result.put(machine.getIndex(), machine.getTrack());
        }
        return result;
    }

    void load(ICaustkRack rack) {
        size = FileUtils.sizeOf(getFile());

        RackMessage.BLANKRACK.send(rack);
        RackMessage.LOAD_SONG.send(rack, getFile().getAbsolutePath());
        fbpm = OutputPanelMessage.BPM.query(rack);
        bpm = (int)fbpm;
        String songPatterns = SequencerMessage.QUERY_PATTERN_EVENT.queryString(rack);

        if (songPatterns != null) {
            sequenced = true;

            for (int i = 0; i < 14; i++) {

                String name = RackMessage.QUERY_MACHINE_NAME.queryString(rack, i);
                if (name != null) {

                    MachineType type = MachineType.fromString(RackMessage.QUERY_MACHINE_TYPE
                            .queryString(rack, i));

                    TrackChannel track = new TrackChannel(i);
                    track.restore();

                    SongFileMachine machine = new SongFileMachine(i, name, type, track);
                    machines.put(i, machine);
                }
            }

            int appendMeasure = 0;
            for (TrackChannel trackNode : getTracks().values()) {
                if (trackNode.getAppendMeasure() > appendMeasure)
                    appendMeasure = trackNode.getAppendMeasure();
            }

            //int hoursDecimal = (appendMeasure * 4f) / fbpm);
            //int hours = (int)hoursDecimal ;
            float minutesDecimal = (appendMeasure * 4f) / fbpm;
            minutes = (int)minutesDecimal;
            seconds = (int)((minutesDecimal - minutes) * 60);

            this.measureCount = appendMeasure;
            //this.songLength = (appendMeasure * 4f) / fbpm;
        }
        RackMessage.BLANKRACK.send(rack);
    }

    public String toTrackPositionString() {
        int measure = CaustkRuntime.getInstance().getRack().getSequencer().getCurrentMeasure();
        int beat = CaustkRuntime.getInstance().getRack().getSequencer().getCurrentBeat();
        int sixteenth = CaustkRuntime.getInstance().getRack().getSequencer()
                .getCurrentSixteenthStep();
        return measure + ":" + PhraseUtils.toMeasureBeat(beat) + ":" + sixteenth;
    }

    public String toCurrentTimeString() {
        float beat = CaustkRuntime.getInstance().getRack().getCurrentBeat();
        float minutesDecimal = beat / fbpm;
        int minutes = (int)minutesDecimal;
        int seconds = (int)((minutesDecimal - minutes) * 60);
        String extra = "0";
        if (seconds > 9)
            extra = "";
        return minutes + ":" + extra + seconds;
    }

    public String toTimeString() {
        return getMinutes() + ":" + getSeconds();
    }

    @Override
    public String toString() {
        return getBaseName();
    }

    @SuppressLint("DefaultLocale")
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int)(Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}

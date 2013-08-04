////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.sequencer.ISongSequencer.ExportType;
import com.teotigraphix.caustk.tone.Tone;

public class SongSequencerModel extends SubControllerModel {

    public SongSequencerModel() {
    }

    public SongSequencerModel(ICaustkController controller) {
        super(controller);
    }

    public void addPattern(Tone tone, int bank, int pattern, int start, int end) {
        SequencerMessage.PATTERN_EVENT.send(getController(), tone.getIndex(), start, bank, pattern,
                end);
    }

    public void removePattern(Tone tone, int start, int end) {
        SequencerMessage.PATTERN_EVENT.send(getController(), tone.getIndex(), start, -1, -1, end);
    }

    public void clearAutomation() {
        SequencerMessage.CLEAR_AUTOMATION.send(getController());
    }

    public void clearAutomation(Tone tone) {
        SequencerMessage.CLEAR_MACHINE_AUTOMATION.send(getController(), tone.getIndex());
    }

    public void clearPatterns() {
        SequencerMessage.CLEAR_PATTERNS.send(getController());
    }

    public void setLoopPoints(int startBar, int endBar) {
        SequencerMessage.LOOP_POINTS.send(getController(), startBar, endBar);
    }

    public void playPosition(int beat) {
        SequencerMessage.PLAY_POSITION.send(getController(), beat);
    }

    public void playPositionAt(int bar, int step) {
        // the number of beats in the bars
        int beats = (bar * 4);
        if (step > 0) {
            beats += step / 4;
        }
        playPosition(beats);
    }

    public void exportSong(String exportPath, ExportType type, int quality) {
        String ftype = "";
        String fquality = "";
        if (type != null) {
            ftype = type.getValue();
            fquality = Integer.toString(quality);
        }
        SequencerMessage.EXPORT_SONG.send(getController(), exportPath, ftype, fquality);
    }

    public float exportSongProgress() {
        // TODO (mschmalle) an AsyncHandler would work at about 100ms update to
        // send the progress event
        return SequencerMessage.EXPORT_PROGRESS.query(getController());
    }
}

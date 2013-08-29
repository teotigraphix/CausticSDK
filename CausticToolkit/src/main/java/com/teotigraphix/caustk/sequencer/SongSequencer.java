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
import com.teotigraphix.caustk.controller.ControllerComponent;
import com.teotigraphix.caustk.controller.ControllerComponentState;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.tone.Tone;

public class SongSequencer extends ControllerComponent implements ISongSequencer {

    SongSequencerModel getModel() {
        return (SongSequencerModel)getInternalState();
    }

    @Override
    protected Class<? extends ControllerComponentState> getStateType() {
        return SongSequencerModel.class;
    }

    public SongSequencer(ICaustkController controller) {
        super(controller);
    }

    @Override
    public void setSongEndMode(SongEndMode mode) {
        SequencerMessage.SONG_END_MODE.send(getController(), mode.getValue());
    }

    @Override
    public SongEndMode getSongEndMode() {
        return SongEndMode.fromInt((int)SequencerMessage.SONG_END_MODE.send(getController()));
    }

    @Override
    public String getPatterns() {
        return SequencerMessage.QUERY_PATTERN_EVENT.queryString(getController());
    }

    @Override
    public void addPattern(Tone tone, int bank, int pattern, int start, int end)
            throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getController(), tone.getIndex(), start, bank, pattern,
                end);
    }

    @Override
    public void removePattern(Tone tone, int start, int end) throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getController(), tone.getIndex(), start, -1, -1, end);
    }

    @Override
    public void setLoopPoints(int startBar, int endBar) {
        SequencerMessage.LOOP_POINTS.send(getController(), startBar, endBar);
    }

    @Override
    public void playPosition(int beat) {
        SequencerMessage.PLAY_POSITION.send(getController(), beat);
    }

    @Override
    public void playPositionAt(int bar, int step) {
        playPositionAt(bar, step);
    }

    @Override
    public void exportSong(String exportPath, ExportType type, int quality) {
        String ftype = "";
        String fquality = "";
        if (type != null) {
            ftype = type.getValue();
            fquality = Integer.toString(quality);
        }
        SequencerMessage.EXPORT_SONG.send(getController(), exportPath, ftype, fquality);
    }

    @Override
    public void exportSong(String exportPath, ExportType type) {
        exportSong(exportPath, type, 70);
    }

    @Override
    public float exportSongProgress() {
        return SequencerMessage.EXPORT_PROGRESS.query(getController());
    }

    @Override
    public void clearPatterns() {
        SequencerMessage.CLEAR_PATTERNS.send(getController());
    }

    @Override
    public void clearAutomation() {
        SequencerMessage.CLEAR_AUTOMATION.send(getController());
    }

    @Override
    public void clearAutomation(Tone tone) {
        SequencerMessage.CLEAR_MACHINE_AUTOMATION.send(getController(), tone.getIndex());
    }

    @Override
    public void restore() {
    }

}

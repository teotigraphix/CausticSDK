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
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.Tone;

public class SongSequencer extends SubControllerBase implements ISongSequencer {

    SongSequencerModel getModel() {
        return (SongSequencerModel)getInternalModel();
    }

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SongSequencerModel.class;
    }

    public SongSequencer(ICaustkController controller) {
        super(controller);
    }

    @Override
    public void restore() {
    }

    @Override
    public void addPattern(Tone tone, int bank, int pattern, int start, int end)
            throws CausticException {
        getModel().addPattern(tone, bank, pattern, start, end);
    }

    @Override
    public void removePattern(Tone tone, int start, int end) throws CausticException {
        getModel().removePattern(tone, start, end);
    }

    @Override
    public void setLoopPoints(int startBar, int endBar) {
        getModel().setLoopPoints(startBar, endBar);
    }

    @Override
    public void playPosition(int positionInBeats) {
        getModel().playPosition(positionInBeats);
    }

    @Override
    public void playPositionAt(int bar, int step) {
        getModel().playPositionAt(bar, step);
    }

    @Override
    public void exportSong(String exportPath, ExportType type, int quality) {
        getModel().exportSong(exportPath, type, quality);
    }

    @Override
    public void exportSong(String exportPath, ExportType type) {
        getModel().exportSong(exportPath, type, 70);
    }

    @Override
    public float exportSongProgress() {
        return getModel().exportSongProgress();
    }

    @Override
    public void clearPatterns() {
        getModel().clearPatterns();
    }

    @Override
    public void clearAutomation() {
        getModel().clearAutomation();
    }

    @Override
    public void clearAutomation(Tone tone) {
        getModel().clearAutomation(tone);
    }

}

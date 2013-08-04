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
import com.teotigraphix.caustk.controller.command.CommandBase;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.CausticEventListener;

public class SystemSequencer extends SubControllerBase implements ISystemSequencer,
        CausticEventListener {

    //----------------------------------
    // modelType
    //----------------------------------

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SystemSequencerModel.class;
    }

    //----------------------------------
    // model
    //----------------------------------

    SystemSequencerModel getModel() {
        return (SystemSequencerModel)getInternalModel();
    }

    @Override
    public void setIsPlaying(boolean value) {
        getModel().setIsPlaying(value);
    }

    @Override
    public boolean isPlaying() {
        return getModel().isPlaying();
    }

    @Override
    public final SequencerMode getSequencerMode() {
        return getModel().getSequencerMode();
    }

    @Override
    public void setTempo(float value) {
        getModel().setTempo(value);
    }

    @Override
    public float getTempo() {
        return getModel().getTempo();
    }

    @Override
    public final void setSequencerMode(SequencerMode value) {
        getModel().setSequencerMode(value);
    }

    public SystemSequencer(ICaustkController controller) {
        super(controller);

        controller.addComponent(ISystemSequencer.class, this);

        controller.getSoundGenerator().addEventListener(this);
    }

    @Override
    public void play(SequencerMode mode) {
        setSequencerMode(mode);
        setIsPlaying(true);
    }

    @Override
    public void stop() {
        setIsPlaying(false);
    }

    public void executePlay() {
        getController().execute(COMMAND_PLAY);
    }

    public void executeStop() {
        getController().execute(COMMAND_STOP);
    }

    public void executeTempo(float tempo) {
        getController().execute(COMMAND_STOP, tempo);
    }

    @Override
    public void restore() {
    }

    //--------------------------------------------------------------------------
    // Public Command API
    //--------------------------------------------------------------------------

    public static class SystemSequencerPlayCommand extends CommandBase {
        @Override
        public void execute() {
            int smode = CommandUtils.getInteger(getContext(), 0);
            SequencerMode mode = SequencerMode.fromInt(smode);
            getContext().getComponent(ISystemSequencer.class).play(mode);
        }
    }

    public static class SystemSequencerStopCommand extends CommandBase {
        @Override
        public void execute() {
            getContext().getComponent(ISystemSequencer.class).stop();
        }
    }

    public static class SystemSequencerTempoCommand extends UndoCommand {

        float last;

        @Override
        protected void doExecute() {
            last = getContext().getComponent(ISystemSequencer.class).getTempo();
            float bpm = CommandUtils.getFloat(getContext(), 0);
            getContext().getComponent(ISystemSequencer.class).setTempo(bpm);
        }

        @Override
        protected void undoExecute() {
            getContext().getComponent(ISystemSequencer.class).setTempo(last);
        }
    }

    @Override
    public void OnBeatChanged(int beat) {
        //System.out.println("   beat " + beat);
    }

    @Override
    public void OnMeasureChanged(int measure) {
        //System.out.println("measure " + measure);
    }

    //--------------------------------------------------------------------------

    //----------------------------------
    // currentMeasure
    //----------------------------------

    private int mCurrentMeasure = 0;

    @Override
    public int getCurrentMeasure() {
        return mCurrentMeasure;
    }

    void setCurrentMeasure(int value) {
        mCurrentMeasure = value;
        //        
        //        final int numMeasures = getPattern().getLength();
        //        int last = 1;
        //        if (numMeasures == 2)
        //            last = 
        if (mCurrentMeasure == 0) {
            //controller.getPatternSequencer().playNextPattern();
        }
        //        getDispatcher().trigger(new OnSystemSequencerMeasureChange(mCurrentMeasure));
    }

    public int getMeasureBeat() {
        return mCurrentBeat % 4;
    }

    //----------------------------------
    // currentBeat
    //----------------------------------

    private int mCurrentBeat = 0;

    @Override
    public int getCurrentBeat() {
        return mCurrentBeat;
    }

    void setCurrentBeat(int value) {
        setCurrentBeat(value, false);
    }

    void setCurrentBeat(int value, boolean seeking) {
        int beatsInLength = 4;

        int last = mCurrentBeat;
        mCurrentBeat = value;

        //fireBeatChange(mCurrentBeat, last);

        if (last < value) {
            // forward
            if (mCurrentBeat == 0) {
                setCurrentMeasure(0);
            } else {
                int remainder = mCurrentBeat % beatsInLength;
                System.out.println("    remainder " + getMeasureBeat());
                if (seeking) {
                    setCurrentMeasure(mCurrentBeat / beatsInLength);
                } else if (remainder == 0) {
                    setCurrentMeasure(mCurrentMeasure + 1);
                }
            }
        } else if (last > value) {
            // reverse
            // if the last beat was a measure change, decrement measure
            int remainder = last % 4;
            if (remainder == 0) {
                setCurrentMeasure(mCurrentMeasure - 1);
            }
        }
    }

    int updateMeasure(int beat) {
        int len = 1; //controller.getPatternSequencer().getPattern().getLength();
        final int remainder = (beat + 1) % 4;
        if (len == 1) {
            if (remainder == 0)
                setCurrentMeasure(0);
        } else if (len == 2) {
            if (beat == 0 || beat == 8 || beat == 16 || beat == 24 || beat == 32)
                setCurrentMeasure(0);
            else if (beat == 4 || beat == 12 || beat == 20 || beat == 28)
                setCurrentMeasure(mCurrentMeasure + 1);
        } else if (len == 4) {
            if (beat == 0 || beat == 16 || beat == 32)
                setCurrentMeasure(0);
            else if (beat == 4 || beat == 8 || beat == 12 || beat == 20 || beat == 24 || beat == 28)
                setCurrentMeasure(mCurrentMeasure + 1);
        } else if (len == 8) {
            if (beat == 0)
                setCurrentMeasure(0);
            else if (remainder == 0)
                setCurrentMeasure(mCurrentMeasure + 1);
        }
        return 0;
    }
}

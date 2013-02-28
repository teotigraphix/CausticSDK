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

package com.teotigraphix.caustic.internal.controller;

import roboguice.activity.event.OnStopEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import android.os.Handler;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.ISequencerController;
import com.teotigraphix.caustic.internal.controller.sequencer.StepEngine;
import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.output.IOutputPanel.Mode;
import com.teotigraphix.caustic.sequencer.ISequencer;
import com.teotigraphix.caustic.sequencer.ISequencer.OnBeatChangeListener;
import com.teotigraphix.caustic.sequencer.ISequencer.OnMeasureChangeListener;
import com.teotigraphix.caustic.song.IProject.OnProjectSongChangeEvent;
import com.teotigraphix.caustic.song.ISong;
import com.teotigraphix.caustic.song.IWorkspace;

@ContextSingleton
public class SequencerController implements ISequencerController, OnBeatChangeListener,
        OnMeasureChangeListener {

    IWorkspace workspace;

    EventManager eventManager;

    @SuppressWarnings("unused")
    private static final String TAG = "SequencerController";

    private boolean isPlaying;

    private Handler mHandler = new Handler();

    private final ISequencer mSequencer;

    private StepEngine mStepEngine;

    private int mCurrentMeasure;

    private int mCurrentBeat;

    private IOutputPanel mOutputPanel;

    @Override
    public final boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public float getBPM() {
        return workspace.getRack().getOutputPanel().getBPM();
    }

    @Override
    public void setBPM(float value) {
        // XXX bounds testing so we don't get exception from core
        workspace.getRack().getOutputPanel().setBPM(value);
        eventManager.fire(new OnSequencerBPMChangeEvent(value));
    }

    public final int getCurrentMeasure() {
        return mCurrentMeasure;
    }

    public final int getCurrentBeat() {
        return mCurrentBeat;
    }

    @Inject
    public SequencerController(IWorkspace workspace, EventManager eventManager) {
        this.workspace = workspace;
        this.eventManager = eventManager;

        mSequencer = workspace.getRack().getSequencer();
        mOutputPanel = workspace.getRack().getOutputPanel();
        // the sequencer listens to the only beat and measure changes
        mSequencer.setOnBeatChangeListener(this);
        mSequencer.setOnMeasureChangeListener(this);

        mStepEngine = new StepEngine(this, eventManager);
    }

    void onProjectSongChangeEvent(@Observes OnProjectSongChangeEvent event) {
        //ITrackSong song = (ITrackSong)event.getSong();
        //Log.d(TAG, "onProjectSongChangeEvent [" + song.getData().getName() + "]");

    }

    /**
     * Measure change in the ui thread.
     */
    protected void measureChanged() {
    }

    /**
     * Beat change in the ui thread.
     */
    protected void beatChanged() {
        ISong song = workspace.getProject().getSelectedSong();
        if (song != null)
            song.nextBeat();
        //Log.d(TAG, "beat[" + mCurrentBeat + "]");

        mStepEngine.beatChanged(mCurrentBeat);

        eventManager.fire(new OnSequencerBeatChangeEvent(mCurrentBeat));
    }

    @Override
    public void onBeatChanged(final int beat) {
        //Log.d(TAG, "beat [" + beat + "]");
        mCurrentBeat = beat;
        // we are in the Audio thread right now, must post back to the ui thread
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                beatChanged();
            }
        });
    }

    @Override
    public void onMeasureChanged(final int measure) {
        //Log.d(TAG, "measure [" + measure + "]");
        mCurrentMeasure = measure;
        // we are in the Audio thread right now, must post back to the ui thread
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                measureChanged();
            }
        });
    }

    @Override
    public void setMode(IOutputPanel.Mode mode) {
        mOutputPanel.setMode(mode);
    }

    @Override
    public void play(boolean play, Mode mode) {
        setMode(mode);
        if (play) {
            //Log.d(TAG, "play(" + isPlaying + ") PLAY");
            if (isPlaying)
                return;

            isPlaying = true;

            mOutputPanel.play();
            mStepEngine.start();

            eventManager.fire(new OnSequencerPlayEvent());

        } else {
            //Log.d(TAG, "play(" + isPlaying + ") STOP");
            if (!isPlaying)
                return;

            isPlaying = false;

            mOutputPanel.stop();
            mStepEngine.stop();

            eventManager.fire(new OnSequencerStopEvent());
        }
    }

    void onStop(@Observes OnStopEvent event) {
        play(false);
    }

    @Override
    public void play(boolean play) {
        play(play, mOutputPanel.getMode());
    }

    @Override
    public void seek(int beat) {
        // XXX This has to notify things like the ITrackSOng because it effects
        // the position in the song.
        mSequencer.playPosition(beat);
        eventManager.fire(new OnSequencerSeekEvent(beat));
    }

}

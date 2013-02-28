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

package com.teotigraphix.caustic.internal.controller.sequencer;

import java.util.Timer;
import java.util.TimerTask;

import roboguice.event.EventManager;
import android.os.Handler;

import com.teotigraphix.caustic.controller.ISequencerController;
import com.teotigraphix.caustic.controller.ISequencerController.OnSequencerStepChangeEvent;

public class StepEngine {

    @SuppressWarnings("unused")
    private static final String TAG = "StepEngine";

    @SuppressWarnings("unused")
    private int mMeasureCalculated;

    private int mCurrentStepCalculated;

    private TimerTask task;

    private Timer timer;

    private Handler mHandler = new Handler();

    private final ISequencerController controller;

    private final EventManager eventManager;

    // the amount of beats 1(4), 2(8), 4(16), 8(32)
    private int mNumBeats = 4;

    private int mNumMeasures = 2;

    @SuppressWarnings("unused")
    private int mCurrentMeasure;

    public StepEngine(ISequencerController controller, EventManager eventManager) {
        this.controller = controller;
        this.eventManager = eventManager;
    }

    public void stop() {
        stopStepLoop();
        mCurrentStepCalculated = 0;
        mCurrentMeasure = 0;
        mMeasureCalculated = 0;
    }

    public void start() {
        startStepLoop();
    }

    public void beatChanged(final int beat) {
        mMeasureCalculated = beat % mNumBeats;
        mCurrentMeasure = (int)Math.floor(beat / mNumBeats);

        int beatsInMeasure = mNumMeasures * mNumBeats;
        int remainderForMeasure = beat % beatsInMeasure;

        if (remainderForMeasure == 0) {
            mCurrentStepCalculated = 0;
            mCurrentMeasure = 0;
        }

        stopStepLoop();

        startStepLoop();
    }

    private void startStepLoop() {
        if (!controller.isPlaying())
            return;

        task = new TimerTask() {
            @Override
            public void run() {

                final int step = mCurrentStepCalculated;
                //Log.d(TAG, "run(" + step + ")");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        eventManager.fire(new OnSequencerStepChangeEvent(step));
                    }
                });
                mCurrentStepCalculated++;
            }
        };

        timer = new Timer();
        final int msAdjust = 10;
        final int bpm = (int)controller.getBPM();
        timer.scheduleAtFixedRate(task, 0, ((60000 / bpm) / mNumBeats) + msAdjust);
    }

    private void stopStepLoop() {
        //Log.d(TAG, "stopStepLoop()");
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}

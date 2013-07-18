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

import java.util.Timer;
import java.util.TimerTask;

import com.teotigraphix.caustk.controller.ICaustkController;

public class StepEngine {
    private int currentStep;

    private TimerTask task;

    private Timer timer;

    private final ICaustkController controller;

    private int tempo;

    public StepEngine(ICaustkController systemController) {
        this.controller = systemController;
    }

    public void setTempo(float value) {
        tempo = (int)value;
        if (timer != null) {
            stopStepLoop();
            startStepLoop();
        }
    }

    public void stop() {
        stopStepLoop();
        currentStep = 0;
    }

    public void start(final int beat, float tempo) {
        setTempo(tempo);
        beatChanged(beat);
    }

    public void beatChanged(final int beat) {
        if (beat == 0 || beat == 4 || beat == 8 || beat == 12 || beat == 16 || beat == 20
                || beat == 24 || beat == 28) {
            currentStep = 0;
            startStepLoop();
        }
    }

    private void startStepLoop() {
        if (timer != null || !controller.api(SequencerAPI.class).isPlaying())
            return;

        task = new TimerTask() {
            @Override
            public void run() {
                final int step = currentStep;
                controller.getSystemSequencer().stepChanged(step);
                currentStep++;
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, ((60000 / tempo) / 4));
    }

    private void stopStepLoop() {
        //synchronized (timer)
        //{
        if (timer != null) {
            timer.cancel();
            //timer.purge();
            timer = null;
        }
        //}
    }

}

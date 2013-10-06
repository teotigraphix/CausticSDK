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

package com.example.hellocaustkstepsequencer.view;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.hellocaustkstepsequencer.R;
import com.example.hellocaustkstepsequencer.model.SequencerModel;

/**
 * @author Michael Schmalle
 */
public class TransportControlMediator {

    private Activity activity;

    private SequencerModel sequencerModel;

    private Button playButton;

    private Button stopButton;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TransportControlMediator(Activity activity, SequencerModel sequencerModel) {
        this.activity = activity;
        this.sequencerModel = sequencerModel;
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public void onAttach() {
        playButton = (Button)activity.findViewById(R.id.playButton);
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sequencerModel.play();
            }
        });
        stopButton = (Button)activity.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sequencerModel.stop();
            }
        });
    }
}

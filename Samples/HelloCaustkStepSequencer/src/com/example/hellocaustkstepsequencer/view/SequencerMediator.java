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
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.example.hellocaustkstepsequencer.R;
import com.example.hellocaustkstepsequencer.model.SequencerModel;
import com.example.hellocaustkstepsequencer.model.SequencerModel.IStepListener;

/**
 * @author Michael Schmalle
 */
public class SequencerMediator {

    private Activity activity;

    private SequencerModel sequencerModel;

    private ViewGroup view;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SequencerMediator(Activity activity, SequencerModel sequencerModel) {
        this.activity = activity;
        this.sequencerModel = sequencerModel;
    }

    public void onAttach() {
        this.view = (ViewGroup)activity.findViewById(R.id.stepContainer);

        sequencerModel.setListener(new IStepListener() {
            @Override
            public void onStepChanged(int index) {
            }
        });

        int len = view.getChildCount();
        for (int i = 0; i < len; i++) {
            ToggleButton button = (ToggleButton)view.getChildAt(i);
            String text = Integer.toString(i);
            button.setText(text);
            button.setTextOn(text);
            button.setTextOff(text);
            button.setTag(i);
            button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                    onStepChange((ToggleButton)button);
                }
            });
        }
    }

    protected void onStepChange(ToggleButton button) {
        // trigger on/off for the step in the sequencer
        int index = (Integer)button.getTag();
        sequencerModel.setSelected(index, button.isChecked());
    }
}

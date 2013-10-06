
package com.example.hellocaustkstepsequencer.view;

import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.hellocaustkstepsequencer.R;
import com.example.hellocaustkstepsequencer.model.SequencerModel;

public class ControlsMediator {

    private SeekBar cutoffSlider;

    private Activity activity;

    private SequencerModel sequencerModel;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ControlsMediator(Activity activity, SequencerModel sequencerModel) {
        this.activity = activity;
        this.sequencerModel = sequencerModel;
    }

    public void onAttach() {
        cutoffSlider = (SeekBar)activity.findViewById(R.id.cutoffSlider);
        cutoffSlider.setMax(100);
        cutoffSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                float value = arg1 / 100f;
                sequencerModel.setCutoff(value);
            }
        });
    }

}

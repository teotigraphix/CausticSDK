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

package com.teotigraphix.android.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class AutoRepeatButton extends Button {

    private long mRepeatDelay = 500;

    private long mRepeatIntervalInMilliseconds = 100;

    private Runnable repeatClickWhileButtonHeldRunnable = new Runnable() {
        @Override
        public void run() {
            //Perform the present repetition of the click action provided by the user
            // in setOnClickListener().
            performClick();

            //Schedule the next repetitions of the click action, using a faster repeat
            // interval than the initial repeat delay interval.
            postDelayed(repeatClickWhileButtonHeldRunnable, mRepeatIntervalInMilliseconds);
        }
    };

    private void commonConstructorCode() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    //Just to be sure that we removed all callbacks, 
                    // which should have occurred in the ACTION_UP
                    removeCallbacks(repeatClickWhileButtonHeldRunnable);

                    //Perform the default click action.
                    performClick();

                    //Schedule the start of repetitions after a one half second delay.
                    postDelayed(repeatClickWhileButtonHeldRunnable, mRepeatDelay);
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    //Cancel any repetition in progress.
                    removeCallbacks(repeatClickWhileButtonHeldRunnable);
                }

                //Returning true here prevents performClick() from getting called 
                // in the usual manner, which would be redundant, given that we are 
                // already calling it above.
                return true;
            }
        });
    }

    public AutoRepeatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        commonConstructorCode();
    }

    public AutoRepeatButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        /*
        TypedArray a = context.obtainStyledAttributes(attrs,
        		R.styleable.AutoRepeatButton);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
        	int attr = a.getIndex(i);

        	switch (attr) {
        	case R.styleable.AutoRepeatButton_initial_delay:
        		initialRepeatDelay = a.getInt(attr, DEFAULT_INITIAL_DELAY);
        		break;
        	case R.styleable.AutoRepeatButton_repeat_interval:
        		repeatIntervalInMilliseconds = a.getInt(attr,
        				DEFAULT_REPEAT_INTERVAL);
        		break;
        	}
        }
        */

        commonConstructorCode();
    }

    public AutoRepeatButton(Context context) {
        super(context);
        commonConstructorCode();
    }
}

/*
attrs.xml

<resources>
<declare-styleable name="AutoRepeatButton">
    <attr name="initial_delay"  format="integer" />
    <attr name="repeat_interval"  format="integer" />
</declare-styleable>
</resources>


   <com.thepath.AutoRepeatButton
        xmlns:repeat="http://schemas.android.com/apk/res/com.thepath"
        android:id="@+id/btn_delete"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/selector_btn_delete"
        android:onClick="onBtnClick"
        android:layout_weight="1"
        android:layout_margin="2dp"

        repeat:initial_delay="1500"
        repeat:repeat_interval="150"
        />


*/

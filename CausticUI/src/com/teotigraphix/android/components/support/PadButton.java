////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.android.components.support;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.teotigraphix.android.components.IPadMatrix.PadData;
import com.teotigraphix.caustic.internal.song.PatternQueue.PatternQueueData;
import com.teotigraphix.caustic.internal.song.PatternQueue.PatternState;
import com.teotigraphix.caustic.ui.R;

/*

The PadButton

- select mode
  - touch down selects
    - dispatch onSelect()
  - touch down unselects
    - dispatch onSelect()

- onshot mode
  - touch down selects
    - onPress()
  - tocuh up unselects
    - onRelease()

*/

public class PadButton extends View {

    @SuppressWarnings("unused")
    private static final String TAG = "PadButton";

    //--------------------------------------------------------------------------
    // 
    //  Variables
    // 
    //--------------------------------------------------------------------------

    private boolean mSelectMode = true;

    public final boolean isSelectMode() {
        return mSelectMode;
    }

    public final void setSelectMode(boolean value) {
        mSelectMode = value;
    }

    private OnSelectListener mOnSelectListener;

    private OnPressedListener mOnPressedListener;

    private Paint mBackgroundPaint;

    private Matrix mBackgroundMatrix;

    private boolean mIsLit = false;

    @SuppressWarnings("unused")
    private boolean mDispatchSelected;

    public final boolean isLit() {
        return mIsLit;
    }

    public void setIsLit(boolean value) {
        if (mIsLit == value)
            return;
        mIsLit = value;
        invalidate();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        fireSelectedChanged(selected);
    }

    public void setSelectedNoEvent(boolean selected) {
        super.setSelected(selected);
    }

    protected void fireSelectedChanged(boolean selected) {
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelect(this, selected);
        }
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        if (mOnPressedListener != null) {
            mOnPressedListener.onPressed(this, pressed);
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructors
    // 
    //--------------------------------------------------------------------------

    public PadButton(Context context) {
        super(context);
        init();
    }

    public PadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PadButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods 
    // 
    //--------------------------------------------------------------------------

    private NinePatchDrawable buttonIdleBitmap;

    protected void init() {
        Resources resources = getContext().getResources();
        buttonIdleBitmap = (NinePatchDrawable)resources.getDrawable(R.drawable.pad_up);

        mBackgroundPaint = new Paint();
        mBackgroundMatrix = new Matrix();
    }

    //--------------------------------------------------------------------------
    // 
    //  Overridden Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    private void drawBackground(Canvas canvas) {

        if (buttonIdleBitmap == null)
            return;

        float sx = getWidth();/// mBackground.getWidth();
        float sy = getHeight();// / mBackground.getHeight();

        mBackgroundMatrix.setScale(sx, sy);

        mBackgroundPaint.setColorFilter(null);
        buttonIdleBitmap.setColorFilter(null);

        int color = getColor();
        if (isSelected()) {
            LightingColorFilter logoFilter = new LightingColorFilter(0xff666666, color);
            //mBackgroundPaint.setColorFilter(logoFilter);
            buttonIdleBitmap.setColorFilter(logoFilter);
        }

        //canvas.drawBitmap(mBackground, mBackgroundMatrix, mBackgroundPaint);

        if (buttonIdleBitmap != null) {
            buttonIdleBitmap.setBounds(0, 0, getWidth(), getHeight());
            buttonIdleBitmap.draw(canvas);
        }

    }

    private int getColor() {
        int color = 0x00000000;
        PadData tag = (PadData)getTag();
        PatternQueueData data = (PatternQueueData)tag.getData();

        if (data == null)
            return color;

        if (data.getState() == PatternState.INDETERMINATE) {
            color = 0xff33CC00;
        } else if (data.getState() == PatternState.PLAYING) {
            color = 0xff2369D1;
        } else if (data.getState() == PatternState.REMOVING) {
            color = 0xffffcc00;
        }

        return color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
    }

    //--------------------------------------------------------------------------
    // 
    //  Listener :: Methods
    // 
    //--------------------------------------------------------------------------

    public void setOnSelectListener(OnSelectListener listener) {
        mOnSelectListener = listener;
    }

    public void setOnPressedListener(OnPressedListener listener) {
        mOnPressedListener = listener;
    }

    public interface OnSelectListener {
        void onSelect(View view, boolean selected);
    }

    public interface OnPressedListener {
        void onPressed(View view, boolean pressed);
    }
}

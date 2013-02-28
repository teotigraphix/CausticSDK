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

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.teotigraphix.android.components.support.UIComponent;
import com.teotigraphix.caustic.ui.R;

public class PadMatrix2 extends UIComponent implements IMultitouchView, IPadMatrix {

    //private static final String TAG = "PadMatrix2";

    private PatternModel currentPatternModel;

    private int padWidth;

    private int padHeight;

    private Paint padBackgroundPaint;

    private Paint padSelectedBackgroundPaint;

    private Paint padLitBackgroundPaint;

    private OnPadSelectListener mPadListener;

    private Bitmap mGridBackground;

    private Paint mGridBackgroundPaint;

    private int mCurrentStep;

    private boolean mBackgroundGridDirty;

    private NinePatchDrawable buttonIdleBitmap;

    public void setCurrentStep(int step) {
        mCurrentStep = step;
        invalidate();
    }

    public PatternModel getCurrentPatternModel() {
        return currentPatternModel;
    }

    public void setCurrentPatternModel(PatternModel value) {
        currentPatternModel = value;
        mBackgroundGridDirty = true;
        invalidate();
    }

    public PadMatrix2(Context context) {
        super(context);
    }

    public PadMatrix2(Context context, AttributeSet set) {
        super(context, set);
    }

    public PadMatrix2(Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
    }

    @Override
    protected void preinitialize() {
        //setIsDebug(true);

        // the linear gradient is a bit skewed for realism
        padBackgroundPaint = new Paint();
        //rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        padBackgroundPaint.setAntiAlias(true);
        padBackgroundPaint.setStyle(Style.FILL_AND_STROKE);
        padBackgroundPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f, Color.rgb(0xf0,
                0xf5, 0xf0), Color.rgb(0x30, 0x31, 0x30), Shader.TileMode.CLAMP));

        // the linear gradient is a bit skewed for realism
        padSelectedBackgroundPaint = new Paint();
        //rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        padSelectedBackgroundPaint.setAntiAlias(true);
        padSelectedBackgroundPaint.setStyle(Style.FILL);
        padSelectedBackgroundPaint.setAlpha(200);
        // #5C93D1
        padSelectedBackgroundPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f, Color
                .rgb(0x5c, 0x93, 0xd1), Color.rgb(0x5c, 0x93, 0xd1), Shader.TileMode.CLAMP));

        padLitBackgroundPaint = new Paint();
        //rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        padLitBackgroundPaint.setAntiAlias(true);
        padLitBackgroundPaint.setAlpha(142);
        padLitBackgroundPaint.setStyle(Style.FILL);
        //#FEBD5C
        padLitBackgroundPaint.setShader(new LinearGradient(0f, 0f, 1f, 1f, Color.rgb(0xfe, 0xbd,
                0x5c), Color.rgb(0xfe, 0xbd, 0x5c), Shader.TileMode.CLAMP));

        initializeBitmaps();
    }

    private void initializeBitmaps() {
        Resources resources = getContext().getResources();
        buttonIdleBitmap = (NinePatchDrawable)resources.getDrawable(R.drawable.pad_up);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBackgroundGridDirty) {
            regenerateGrid();
            mBackgroundGridDirty = false;
        }

        drawGridBackground(canvas);

        if (currentPatternModel == null)
            return; // edit mode

        for (PadData data : getCurrentPatternModel().getSelectedList()) {
            if (mCurrentStep == data.getColumn()) {
                canvas.drawRoundRect(data.getLitRect(), 6f, 6f, padLitBackgroundPaint);
            } else {
                canvas.drawRoundRect(data.getSelectedRect(), 6f, 6f, padSelectedBackgroundPaint);
            }
        }

        //invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (currentPatternModel == null) {
            return;
        }
        regenerateGrid();
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        // background
        mGridBackgroundPaint = new Paint();
        mGridBackgroundPaint.setFilterBitmap(true);
    }

    private void regenerateGrid() {
        // free the old bitmap
        if (mGridBackground != null) {
            mGridBackground.recycle();
        }
        // create the bitmap canvas that will hold the static grid pads
        mGridBackground = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mGridBackground);

        drawBackgroundGrid(canvas);
    }

    private void drawGridBackground(Canvas canvas) {
        if (mGridBackground == null) {
            //Log.w(TAG, "Background not created");
        } else {
            canvas.drawBitmap(mGridBackground, 0, 0, mGridBackgroundPaint);
        }
    }

    protected void drawBackgroundGrid(Canvas canvas) {
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int paddingLeft = getPaddingLeft();

        final int width = getWidth() - paddingLeft - paddingRight;
        final int height = getHeight() - paddingTop - paddingBottom;

        final int padding = 0;

        final int mNumColumns = currentPatternModel.getNumColumns();
        final int mNumRows = currentPatternModel.getNumRows();

        final int horizontalGaps = padding * (mNumColumns - 1);
        final int verticalGaps = padding * (mNumRows - 1);

        padWidth = ((width - horizontalGaps) / mNumColumns);
        padHeight = ((height - verticalGaps) / mNumRows);

        int calcX = paddingLeft;
        int calcY = paddingTop;

        int index = 0;
        for (int row = 0; row < mNumRows; row++) {
            for (int col = 0; col < mNumColumns; col++) {
                PadData data = getDataAt(index);
                Rect rect = new Rect(calcX, calcY, calcX + padWidth, calcY + padHeight);
                data.setRect(rect);
                drawBackgroundPad(canvas, rect);

                calcX += padWidth + padding;
                index++;
            }
            calcX = paddingLeft;
            calcY += padHeight + padding;
        }
    }

    private void drawBackgroundPad(Canvas canvas, Rect rect) {
        if (isInEditMode()) {
            canvas.drawRoundRect(new RectF(rect), 4f, 4f, padBackgroundPaint);
        } else {
            if (buttonIdleBitmap != null) {
                buttonIdleBitmap.setBounds(rect.left, rect.top, rect.right, rect.bottom);
                buttonIdleBitmap.draw(canvas);
            }
        }
    }

    //@Override
    @Override
    public boolean onTouch(MotionEvent event) {
        int masked = event.getActionMasked();
        int actionId = event.getActionIndex();

        if (masked == MotionEvent.ACTION_CANCEL) {
            return true;
        }

        // this is MainLayout coords, the view that received the touch event
        int calcX = (int)event.getX(actionId) - getLeft();
        int calcY = (int)event.getY(actionId) - getTop();

        PadData data = getButtonByCoords(calcX, calcY);
        // somehow test padding or make an invisible part of the pad extend into padding
        if (data == null)
            return false;
        //if (event.getActionMasked() != MotionEvent.ACTION_MOVE)
        //   Log.d(TAG, event.toString());
        //Log.d(TAG, event.getPointerCount() + "");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                setPressed(data, true);
                return true;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (getCurrentPatternModel().getPressedList().contains(data)) {
                    // if the touch up is over an original pressed data
                    setPressed(data, false);

                    // if the data is already selected
                    if (getCurrentPatternModel().getSelectedList().contains(data)) {
                        // unselect the data
                        setSelected(data, false);
                    } else {
                        setSelected(data, true);
                    }
                    return true;
                }
                if (event.getPointerCount() == 0) {

                }
                return false;
            default:
                break;
        }

        return false;//super.onTouch(event);
    }

    private void setSelected(PadData data, boolean value) {
        if (value)
            getCurrentPatternModel().getSelectedList().add(data);
        else
            getCurrentPatternModel().getSelectedList().remove(data);
        data.setSelected(value);

        if (mPadListener != null)
            mPadListener.onSelected(data, value);

        // could use invalidate rect here for performance?
        invalidate();
    }

    private void setPressed(PadData data, boolean value) {
        if (value)
            getCurrentPatternModel().getPressedList().add(data);
        else
            getCurrentPatternModel().getPressedList().remove(data);
        data.setPressed(value);
    }

    private PadData getDataAt(int index) {
        return getCurrentPatternModel().getPadDataList().get(index);
    }

    @SuppressWarnings("unused")
    private PadData getDataAt(int row, int column) {
        for (PadData data : getCurrentPatternModel().getPadDataList()) {
            if (data.getColumn() == row && data.getRow() == column)
                return data;
        }
        return null;
    }

    private PadData getButtonByCoords(int x, int y) {
        for (PadData data : getCurrentPatternModel().getPadDataList()) {
            if (data.getRect().contains(x, y))
                return data;
        }
        return null;
    }

    @Override
    public void setOnPadSelectListener(OnPadSelectListener l) {
        mPadListener = l;
    }

    public static class PatternModel {

        private final PadMatrix2 view;

        private boolean mRestored;

        private ArrayList<PadData> padDataList;

        private ArrayList<PadData> pressedList;

        private ArrayList<PadData> selectedList;

        private int mNumColumns = 16;

        private int mNumRows = 12;

        public int getNumColumns() {
            return mNumColumns;
        }

        public int getNumRows() {
            return mNumRows;
        }

        public void setRowsColumns(int rows, int columns) {
            mNumRows = rows;
            mNumColumns = columns;
            initialize();
            view.mBackgroundGridDirty = true;
            view.invalidate();
        }

        public final ArrayList<PadData> getPadDataList() {
            return padDataList;
        }

        public final ArrayList<PadData> getPressedList() {
            return pressedList;
        }

        public final ArrayList<PadData> getSelectedList() {
            return selectedList;
        }

        public PatternModel(PadMatrix2 view) {
            this.view = view;
        }

        public void initialize() {
            padDataList = new ArrayList<IPadMatrix.PadData>();
            pressedList = new ArrayList<IPadMatrix.PadData>();
            selectedList = new ArrayList<IPadMatrix.PadData>();
            int index = 0;
            for (int row = 0; row < getNumRows(); row++) {
                for (int column = 0; column < getNumColumns(); column++) {
                    padDataList.add(new PadData(index, row, column));
                    index++;
                }
            }
        }

        public boolean isRestored() {
            return mRestored;
        }

        public void setRestored(boolean value) {
            mRestored = value;
        }
    }

}

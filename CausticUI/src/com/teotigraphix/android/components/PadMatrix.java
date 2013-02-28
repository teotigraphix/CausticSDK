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

package com.teotigraphix.android.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.teotigraphix.android.components.support.PadButton;
import com.teotigraphix.android.components.support.UIViewGroup;
import com.teotigraphix.caustic.ui.R;

/**
 * The {@link PadMatrix} allows for variable row and column amounts and will
 * measure and lay itself out based on the pad width and height plus padding.
 * 
 * @author Michael Schmalle
 */
public class PadMatrix extends UIViewGroup implements IPadMatrix, IMultitouchView {

    @SuppressWarnings("unused")
    private static final String TAG = "PadMatrix";

    private int padWidth;

    private int padHeight;

    // TODO (mschmalle) when num columns and rows are updated, redraw the pads
    // might need to send notifications to listeners

    //----------------------------------
    //  numColumns
    //----------------------------------

    private int nNumColumns;

    //@Override
    public final int getNumColumns() {
        return nNumColumns;
    }

    //@Override
    public final void setNumColumns(int value) {
        if (value == nNumColumns)
            return;

        nNumColumns = value;
        invalidate();
    }

    //----------------------------------
    //  numRows
    //----------------------------------

    private int nNumRows;

    //@Override
    public final int getNumRows() {
        return nNumRows;
    }

    //@Override
    public final void setNumRows(int value) {
        nNumRows = value;
        invalidate();
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructors
    // 
    //--------------------------------------------------------------------------

    public PadMatrix(Context context) {
        super(context);
    }

    public PadMatrix(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PadMatrix(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        int masked = event.getActionMasked();
        int actionId = event.getActionIndex();

        if (masked == MotionEvent.ACTION_CANCEL) {
            return true;
        }

        // this is MainLayout coords, the view that received the touch event
        int calcX = (int)event.getX(actionId);
        int calcY = (int)event.getY(actionId);

        PadButton button = (PadButton)getViewAt(calcX, calcY);
        // somehow test padding or make an invisible part of the pad extend into padding
        if (button == null)
            return false;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (button.isSelectMode()) {
                    // toggle select
                    button.setSelected(!button.isSelected());
                } else {
                    // onshot (set down)
                    button.setPressed(true);
                }

                //selectChild(calcX, calcY);
                return false;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (button.isSelectMode()) {
                    // toggle select

                } else {
                    // oneshot (set down)
                    button.setPressed(false);
                }
                return false;
            default:
                break;
        }

        return super.onTouch(event);
    }

    public final PadButton getButton(int column, int row) {
        final int len = getChildCount();
        for (int i = 0; i < len; i++) {
            PadData tag = (PadData)getChildAt(i).getTag();
            if (tag.getRow() == column && tag.getColumn() == row)
                return (PadButton)getChildAt(i);
        }
        return null;
    }

    @Override
    protected void initializeSet(AttributeSet set) {
        super.initializeSet(set);
        TypedArray a = getContext().obtainStyledAttributes(set, R.styleable.pad_matrix);
        int columns = a.getInt(R.styleable.pad_matrix_num_columns, 4);
        setNumColumns(columns);
        int rows = a.getInt(R.styleable.pad_matrix_num_rows, 4);
        setNumRows(rows);
        a.recycle();
    }

    @Override
    protected void preinitialize() {
    }

    @Override
    protected void createChildren() {
        removeAllViews();
        int index = 0;
        for (int row = 0; row < nNumRows; row++) {
            for (int column = 0; column < nNumColumns; column++) {
                addView(createPad(getContext(), index, row, column));
                index++;
            }
        }
    }

    protected PadButton createPad(Context context, int index, int row, int column) {
        PadButton pad = new PadButton(context);
        pad.setTag(new PadData(index, row, column));
        return pad;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
        //Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int chosenDimension = Math.min(chosenWidth, chosenHeight);

        setMeasuredDimension(chosenDimension, chosenDimension);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int paddingLeft = getPaddingLeft();

        final int width = getWidth() - paddingLeft - paddingRight;
        final int height = getHeight() - paddingTop - paddingBottom;

        final int padding = 10;

        final int horizontalGaps = padding * (nNumColumns - 1);
        final int verticalGaps = padding * (nNumRows - 1);

        padWidth = ((width - horizontalGaps) / nNumColumns);
        padHeight = ((height - verticalGaps) / nNumRows);

        int calcX = paddingLeft;
        int calcY = paddingTop;

        int index = 0;
        for (int row = 0; row < nNumRows; row++) {
            for (int col = 0; col < nNumColumns; col++) {
                View button = getChildAt(index);
                button.layout(calcX, calcY, calcX + padWidth, calcY + padHeight);
                calcX += padWidth + padding;
                index++;
            }
            calcX = paddingLeft;
            calcY += padHeight + padding;
        }
    }

    private int chooseDimension(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredSize();
        }
    }

    private int getPreferredSize() {
        return 300;
    }

    @Override
    public void setOnPadSelectListener(OnPadSelectListener l) {
    }

}

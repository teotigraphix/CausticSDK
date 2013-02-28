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

import android.graphics.Rect;
import android.graphics.RectF;

public interface IPadMatrix extends ITouchListener, IMultitouchView {

    //    int getNumColumns();
    //
    //    void setNumColumns(int value);
    //
    //    int getNumRows();
    //
    //    void setNumRows(int value);

    /**
     * A data holder for the {@link PadMatrix} button.
     */
    public final static class PadData {

        private Object data;

        private int row;

        private int column;

        private int index;

        private Rect rect;

        private boolean selected;

        private boolean pressed;

        private RectF litRect;

        private RectF selectedRect;

        public final Object getData() {
            return data;
        }

        public final void setData(Object value) {
            data = value;
        }

        public final int getRow() {
            return row;
        }

        public final int getColumn() {
            return column;
        }

        public int getIndex() {
            return index;
        }

        public PadData(int index, int row, int column) {
            this.index = index;
            this.row = row;
            this.column = column;
        }

        public boolean isSelectMode() {
            return false;
        }

        public boolean isSelected() {
            return selected;
        }

        public boolean isPressed() {
            return pressed;
        }

        public void setPressed(boolean value) {
            // Log.d("PadData", toString() + " pressed(" + value + ")");
            pressed = value;
        }

        public void setSelected(boolean value) {
            //Log.d("PadData", toString() + " selected(" + value + ")");
            selected = value;
        }

        @Override
        public String toString() {
            return "{PadData row[" + row + "], column[" + column + "]}";
        }

        public Rect getRect() {
            return rect;
        }

        public void setRect(Rect value) {
            rect = value;
            selectedRect = new RectF(rect);
            litRect = new RectF(rect);
            float padding = 5f;
            selectedRect.set(selectedRect.left + padding, selectedRect.top + padding,
                    selectedRect.right - padding, selectedRect.bottom - padding);
            padding = -5f;
            litRect.set(rect.left + padding, rect.top + padding, rect.right - padding, rect.bottom
                    - padding);
        }

        public RectF getLitRect() {
            return litRect;
        }

        public RectF getSelectedRect() {
            return selectedRect;
        }
    }

    void setOnPadSelectListener(OnPadSelectListener l);

    public interface OnPadSelectListener {
        void onSelected(PadData data, boolean selected);
    }

}

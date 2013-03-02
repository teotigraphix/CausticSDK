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

package com.teotigraphix.android.internal.service;

import java.util.ArrayList;

import roboguice.inject.ContextSingleton;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.teotigraphix.android.components.IMultitouchView;
import com.teotigraphix.android.components.ITouchListener;
import com.teotigraphix.android.service.ITouchService;

@ContextSingleton
public class TouchService implements ITouchService {

    @SuppressWarnings("unused")
    private static final String TAG = "TS";

    private final ArrayList<ITouchListener> mListeners = new ArrayList<ITouchListener>();

    final Rect rect = new Rect();

    @SuppressLint("Recycle")
    final MotionEvent cloneMotionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 0, 0, 0);

    final static int maxFingers = 10;

    final ViewData[] views = new ViewData[maxFingers];

    private ViewGroup mParent;

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public TouchService() {
        super();
        for (int i = 0; i < maxFingers; ++i) {
            views[i] = new ViewData();
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  ITouchService API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public boolean hasTouchListener(View listener) {
        return mListeners.contains(listener);
    }

    @Override
    public void addTouchListener(ITouchListener listener) {
        if (!mListeners.contains(listener)) {
            listener.setMultiTouch(true);
            mListeners.add(listener);
        }
    }

    @Override
    public void removeTouchListener(ITouchListener listener) {
        listener.setMultiTouch(false);
        mListeners.remove(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(ViewGroup parent, MotionEvent event) {
        mParent = parent;
        for (ITouchListener listener : mListeners) {
            View view = (View)listener;
            if (hitTest(mParent, view, event.getRawX(), event.getRawY()))
                return true;
        }
        mParent = null;
        return false;
    }

    @Override
    public boolean onTouchEvent(ViewGroup parent, MotionEvent event) {
        mParent = parent;

        //Log.d(TAG, event.toString());

        int masked = event.getActionMasked();
        int actionId = event.getActionIndex();
        int count = event.getPointerCount();

        if (masked == MotionEvent.ACTION_CANCEL) {
            return true;
        }

        int stageX = (int)event.getX(actionId);
        int stageY = (int)event.getY(actionId);

        ViewData data;
        View view;

        switch (masked) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < maxFingers; ++i)
                    views[i].reset(); // no break;

            case MotionEvent.ACTION_POINTER_DOWN:
                for (ITouchListener listener : mListeners) {
                    view = (View)listener;
                    if (hitTest(mParent, view, stageX, stageY)) {
                        // Hit rectangle in parent's coordinates
                        recycleViewRect(view);

                        data = views[actionId];
                        data.view = view;
                        if (view instanceof IMultitouchView)
                            data.isMultitouch = true;
                        if (data.isMultitouch) {
                            //						view.dispatchTouchEvent(event);
                            listener.onTouch(event);
                        } else {
                            event.setLocation(stageX - rect.left, stageY - rect.top);
                            event.setAction(MotionEvent.ACTION_DOWN);
                            if (!view.performClick()) {
                                //view.dispatchTouchEvent(event);
                                listener.onTouch(event);
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                ITouchListener listener;

                // this loop allows a multitouch component such as a key board
                // to receive ALL move events at the same time, instead of hittesting
                // them as in the second loop
                for (int i = 0; i < count; ++i) {
                    int n = event.getPointerId(i);
                    if (n >= maxFingers)
                        return true;
                    data = views[n];
                    view = data.view;
                    listener = (ITouchListener)view;
                    if (view != null && view.getVisibility() == View.VISIBLE) {
                        if (data.isMultitouch) {
                            //view.dispatchTouchEvent(event);
                            listener.onTouch(event);
                            break; // stop it!
                        }
                    }
                }
                for (int i = 0; i < count; ++i) {
                    int n = event.getPointerId(i);
                    if (n >= maxFingers)
                        return true;
                    data = views[n];
                    view = data.view;
                    listener = (ITouchListener)view;
                    if (view != null && view.getVisibility() == View.VISIBLE) {
                        if (!data.isMultitouch) {
                            stageX = (int)event.getX(i);
                            stageY = (int)event.getY(i);
                            if (data.lastX != stageX || data.lastY != stageY) {
                                data.lastX = stageX;
                                data.lastY = stageY;

                                recycleViewRect(view);

                                cloneMotionEvent.setLocation(stageX - rect.left, stageY - rect.top);
                                //view.dispatchTouchEvent(cloneMotionEvent);
                                listener.onTouch(cloneMotionEvent);
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                data = views[actionId];
                view = data.view;
                listener = (ITouchListener)view;
                //views[actionId].reset();
                if (view != null && view.getVisibility() == View.VISIBLE) {
                    if (data.isMultitouch) {
                        //view.dispatchTouchEvent(event);
                        listener.onTouch(event);
                    } else {
                        views[actionId].reset();
                        recycleViewRect(view);
                        event.setLocation(stageX - rect.left, stageY - rect.top);
                        event.setAction(MotionEvent.ACTION_UP);
                        //view.dispatchTouchEvent(event);
                        listener.onTouch(event);
                    }
                }

                break;

            case MotionEvent.ACTION_CANCEL:
                //Log.d("MainLayout", "ACTION_CANCEL " + id);
                break;
        }

        mParent = null;
        return true;
    }

    private void recycleViewRect(View child) {
        final int[] point = toPoint(mParent, child);
        rect.set(point[0], point[1], child.getWidth(), child.getHeight());
    }

    private static final int[] toPoint(ViewGroup parent, View child) {
        int[] parentXY = {
                0, 0
        };
        int[] viewXY = {
                0, 0
        };

        // get the parent (MainLayout) x,y in stage coords
        parent.getLocationInWindow(parentXY);
        child.getLocationInWindow(viewXY);

        // take the parent's x,y out so we are left with window coords
        int viewX = viewXY[0] - parentXY[0];
        int viewY = viewXY[1] - parentXY[1];

        return new int[] {
                viewX, viewY
        };
    }

    private static final boolean hitTest(ViewGroup parent, View child, float stageX, float stageY) {
        final int[] point = toPoint(parent, child);
        final Rect hit = new Rect(point[0], point[1], point[0] + child.getWidth(), point[1]
                + child.getHeight());
        return hit.contains((int)stageX, (int)stageY);
    }

    class ViewData {

        public View view;

        public int lastX;

        public int lastY;

        // PadMatrix, Keyboard etc, a full component that needs more than one finger
        public boolean isMultitouch;

        void reset() {
            view = null;
            lastX = lastY = -1;
        }
    }

}

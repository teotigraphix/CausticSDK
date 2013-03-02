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

package com.teotigraphix.android.service;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.ImplementedBy;
import com.teotigraphix.android.components.ITouchListener;
import com.teotigraphix.android.internal.service.TouchService;

@ImplementedBy(TouchService.class)
public interface ITouchService {

    boolean onInterceptTouchEvent(ViewGroup parent, MotionEvent event);

    boolean onTouchEvent(ViewGroup parent, MotionEvent event);

    void addTouchListener(ITouchListener listener);

    void removeTouchListener(ITouchListener listener);

    boolean hasTouchListener(View listener);
}

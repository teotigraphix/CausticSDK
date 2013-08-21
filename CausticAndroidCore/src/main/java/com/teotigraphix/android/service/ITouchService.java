package com.teotigraphix.android.service;

import android.view.MotionEvent;
import android.view.ViewGroup;

import com.google.inject.ImplementedBy;

@ImplementedBy(TouchService.class)
public interface ITouchService {
	
	boolean onInterceptTouchEvent(ViewGroup parent, MotionEvent event);
	
	boolean onTouchEvent(ViewGroup parent, MotionEvent event);

	void addTouchListener(ITouchListener listener);

	void removeTouchListener(ITouchListener listener);
}

package com.teotigraphix.android.service;

import android.view.MotionEvent;

public interface ITouchListener {
	int getFingerId();

	void setFingerId(int index);
	
	void setMultiTouch(boolean value);
	
	boolean isMultiTouch();

	boolean onTouch(MotionEvent event);
}

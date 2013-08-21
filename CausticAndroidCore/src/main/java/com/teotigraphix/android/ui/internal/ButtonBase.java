
package com.teotigraphix.android.ui.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.caustic.android.R;

public class ButtonBase extends UIComponent {

    @Inject
    ITouchService touchService;

    private static final int[] STATE_NORMAL = {
        R.attr.state_normal
    };

    private static final int[] STATE_CHECKED = {
        R.attr.state_checked
    };

    private static final int[] STATE_INDETERMINATE = {
        R.attr.state_indeterminate
    };

    private static final String TAG = "ButtonBase";

    private boolean mIndeterminate;

    private OnCheckedListener mOnCheckedListener;

    protected boolean isToggle;

    private Paint mTextPaint;

    //--------------------------------------------------------------------------
    //
    //  Public API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    //  text
    //----------------------------------

    private String mText;

    public String getText() {
        return mText;
    }

    public void setText(String value) {
        mText = value;
        invalidate();
        requestLayout();
    }

    //----------------------------------
    //  checked
    //----------------------------------

    private boolean mChecked;

    /**
     * The button is only checked on a click (up).
     */
    public final boolean isChecked() {
        return mChecked;
    }

    public final void setChecked(boolean value) {
        if (value == mChecked)
            return;
        setCheckedNoEvent(value);
        fireOnChange();
    }

    public final void setCheckedNoEvent(boolean value) {
        if (value == mChecked)
            return;
        mChecked = value;
        invalidate();
        requestLayout();
        refreshDrawableState();
    }

    private boolean hasLabel() {
        return mText != null;
    }

    public ButtonBase(Context context) {
        super(context);
    }

    public ButtonBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Slider);

        final int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.Slider_text:
                    setText(a.getString(attr));
                    break;
            }
        }
        a.recycle();
    }

    public ButtonBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (hasLabel()) {
            // Setup text Paint	
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(11f);
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
            float a = mTextPaint.ascent();
            canvas.drawText(getText(), getWidth() / 2, (getHeight() - a) / 2, mTextPaint);
        }
    }

    @Override
    protected void touchDownHandler(int x, int y, int index) {
        super.touchDownHandler(x, y, index);

        setPressed(true);

        invalidate();
        refreshDrawableState();
    }

    @Override
    protected void touchMoveHandler(int x, int y, int index) {
        super.touchMoveHandler(x, y, index);
    }

    @Override
    protected void touchUpHandler(int x, int y, int index) {
        super.touchUpHandler(x, y, index);
        // XXX If the x & y hittest does not intersect us, do not check the button
        Log.d(TAG, "touchUpHandler()");
        setPressed(false);
        if (!mLongClickPerformed) {
            //			Log.d(TAG, "x:" + x + " y:" + y + " left:"
            //					+ (getRight() - getLeft()) + " top:"
            //					+ (getBottom() - getTop()));
            Rect r = new Rect();
            getLocalVisibleRect(r);
            if (_pointInView(x, y)) {
                if (isToggle) {
                    setChecked(!isChecked());
                }
            }
        }
    }

    boolean _pointInView(float localX, float localY) {
        return localX >= 0 && localX < (getRight() - getLeft()) && localY >= 0
                && localY < (getBottom() - getTop());
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if (isChecked()) { //  || isDown
            mergeDrawableStates(drawableState, STATE_CHECKED);
        }
        if (isNormal()) {
            mergeDrawableStates(drawableState, STATE_NORMAL);
        }
        if (isIndeterminate()) {
            mergeDrawableStates(drawableState, STATE_INDETERMINATE);
        }
        return drawableState;
    }

    @Override
    protected void drawBackground(Canvas canvas) {
        /*
        if (hasLabel()) {
        	// Setup text Paint	
        	mTextPaint = new Paint();
        	mTextPaint.setAntiAlias(true);
        	mTextPaint.setTextAlign(Paint.Align.CENTER);
        	mTextPaint.setTextSize(11f);
        	mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        	float a = mTextPaint.ascent();
        	canvas.drawText(getText(), getWidth() / 2, (getHeight() - a) / 2,
        			mTextPaint);
        }
        */
    }

    public void setIndeterminate(boolean value) {
        mIndeterminate = value;
        invalidate();
        refreshDrawableState();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        fireOnChange();
    }

    /**
     * Updates the view of the button and does not dispatch an event.
     * 
     * @param selected
     */
    public void setSelectedNoEvent(boolean selected) {
        super.setSelected(selected);
    }

    public boolean isIndeterminate() {
        return mIndeterminate;
    }

    public boolean isNormal() {
        return !isChecked() && !mIndeterminate;
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        mOnCheckedListener = listener;
    }

    private void fireOnChange() {
        if (mOnCheckedListener != null) {
            mOnCheckedListener.onChecked(this, mChecked);
        }
    }

    public interface OnCheckedListener {
        void onChecked(ButtonBase button, boolean checked);
    }
}

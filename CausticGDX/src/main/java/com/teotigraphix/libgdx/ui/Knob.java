
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

public class Knob extends Stack {

    private float originalAngle;

    private float lastAngle = 0f;

    protected Vector2 clickOffset;

    private Float currentAngle = null;

    private float minimumAngle = 40f;

    private boolean disabled = false;

    private int draggingPointer = -1;

    private KnobStyle style;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // value
    //----------------------------------

    private float value = 0f;

    public float getValue() {
        return value;
    }

    public boolean setValue(float value) {
        float oldValue = this.value;
        if (value == oldValue)
            return false;
        this.value = value;

        ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
        boolean cancelled = fire(changeEvent);
        if (cancelled)
            this.value = oldValue;

        Pools.free(changeEvent);
        return !cancelled;
    }

    //----------------------------------
    // stepSize
    //----------------------------------

    private float stepSize = 0.1f;

    public float getStepSize() {
        return stepSize;
    }

    public void setStepSize(float value) {
        stepSize = value;
    }

    //----------------------------------
    // maxValue
    //----------------------------------

    private float maxValue = 2f;

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float value) {
        maxValue = value;
    }

    //----------------------------------
    // minValue
    //----------------------------------

    private float minValue = 0f;

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float value) {
        minValue = value;
    }

    public Knob(float min, float max, float stepSize, Skin skin) {
        super();
        this.minValue = min;
        this.maxValue = max;
        this.stepSize = stepSize;
        setTouchable(Touchable.enabled);
        setWidth(60);
        setHeight(60);
        style = skin.get("default", KnobStyle.class);
        createChildren();

    }

    private Image background;

    private Image knob;

    private void createChildren() {
        background = new Image(style.background);
        add(background);

        knob = new Image(style.knob);
        knob.setWidth(60);
        knob.setHeight(60);
        add(knob);

        init();

        invalidate();
    }

    @Override
    public void layout() {
        super.layout();
        knob.setOrigin(knob.getWidth() / 2, knob.getHeight() / 2);
        if (currentAngle == null)
            currentAngle = getAngleFromValue(value);
        knob.setRotation(currentAngle);
    }

    private void init() {
        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (disabled)
                    return false;
                if (draggingPointer != -1)
                    return false;
                draggingPointer = pointer;

                originalAngle = lastAngle;
                clickOffset = new Vector2(x, y);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != draggingPointer)
                    return;
                draggingPointer = -1;

                lastAngle = currentAngle;

                if (!calculatePositionAndValue(x, y)) {
                    // Fire an event on touchUp even if the value didn't change, 
                    // so listeners can see when a drag ends via isDragging.
                    ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
                    fire(changeEvent);
                    Pools.free(changeEvent);
                }

                currentAngle = getAngleFromValue(value) - 140;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                calculatePositionAndValue(x, y);
                currentAngle = getAngleFromValue(value) - 140;
            }
        });
    }

    boolean calculatePositionAndValue(float x, float y) {
        Vector2 p = new Vector2(x, y);

        float newValue = pointToValue(p.x - clickOffset.x, p.y - clickOffset.y);
        boolean valueSet = setValue(nearestValidValue(newValue, getStepSize()));
        invalidate();

        return valueSet;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    protected float pointToValue(float x, float y) {
        float delta = x; // changed this to negate
        float result;

        currentAngle = originalAngle - delta;

        if (currentAngle < 0) {
            if (Math.abs(currentAngle) >= 180 - minimumAngle)
                currentAngle = -(180 - minimumAngle);
        } else {
            if (currentAngle >= 180 - minimumAngle)
                currentAngle = 180 - minimumAngle;
        }

        // 280 degrees to min/mx
        float spanAngle = 360 - (minimumAngle * 2);
        float spanValue = (currentAngle + spanAngle - 140);

        result = getMinValue() + (spanValue / spanAngle) * (getMaxValue() - getMinValue());

        return result;
    }

    protected float nearestValidValue(float value, float interval) {
        if (interval == 0)
            return Math.max(getMinValue(), Math.min(getMaxValue(), value));

        float maxValue = getMaxValue() - getMinValue();
        float scale = 1;

        value -= getMinValue();

        // If interval isn't an integer, there's a possibility that the floating point
        // approximation of value or value/interval will be slightly larger or smaller
        // than the real value. This can lead to errors in calculations like
        // floor(value/interval)*interval, which one might expect to just equal value,
        // when value is an exact multiple of interval. Not so if value=0.58 and
        // interval=0.01, in that case the calculation yields 0.57! To avoid problems,
        // we scale by the implicit precision of the interval and then round. For
        // example if interval=0.01, then we scale by 100.

        if (interval != Math.round(interval)) {
            String v = (new String(1 + Float.toString(interval)));

            String parts[] = v.split("\\.");
            scale = (float)Math.pow(10, parts[1].length());
            maxValue *= scale;
            value = Math.round(value * scale);
            interval = Math.round(interval * scale);
        }

        float lower = (float)Math.max(0, Math.floor(value / interval) * interval);
        float upper = (float)Math.min(maxValue, Math.floor((value + interval) / interval)
                * interval);
        float validValue = ((value - lower) >= ((upper - lower) / 2)) ? upper : lower;

        float v = (validValue / scale) + getMinValue();
        return v;
    }

    protected float getAngleFromValue(float value) throws RuntimeException {
        if (value < getMinValue() || value > getMaxValue()) {
            throw new RuntimeException("Invalid value found when attempting to retrieve angle.");
        }

        float valuePercentage = (value - getMinValue()) / (getMaxValue() - getMinValue());
        float maxRotation = 360 - (minimumAngle * 2);
        float angleForValue = valuePercentage * maxRotation;
        return angleForValue;
    }

    static public class KnobStyle {
        /** The slider background, stretched only in one direction. */
        public Drawable background;

        /** Optional. **/
        public Drawable disabledBackground;

        /** Optional, centered on the background. */
        public Drawable knob, disabledKnob;

        /** Optional. */
        public Drawable knobBefore, knobAfter, disabledKnobBefore, disabledKnobAfter;

        public KnobStyle() {
        }

        public KnobStyle(Drawable background, Drawable knob) {
            this.background = background;
            this.knob = knob;
        }

        public KnobStyle(KnobStyle style) {
            this.background = style.background;
            this.knob = style.knob;
        }
    }
}

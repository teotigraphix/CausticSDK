
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Dial extends ControlTable {

    private Image background;

    private Image knob;

    public Dial(Skin skin) {
        super(skin);
        styleClass = DialStyle.class;
        create(skin);
    }

    private void create(Skin skin) {

        DialStyle dialStyle = new DialStyle();
        dialStyle.background = skin.getDrawable("dial_background");
        dialStyle.knob = skin.getDrawable("dial_knob");
        skin.add("default", dialStyle);
    }

    @Override
    protected void initialize() {
        super.initialize();

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                x -= getWidth() / 2;
                y -= getHeight() / 2;
                //System.out.println("Dial:" + "c " + currentNick + " ch " + nicks + "");
                dragStartDeg = xyToDegrees(x, y);
                //System.out.println("Dial-degrees:" + dragStartDeg);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                x -= getWidth() / 2;
                y -= getHeight() / 2;
                //System.out.println("x:" + x + " y:" + y);
                //System.out.println("deltaX:" + deltaX + " deltaY:" + deltaY);
                float d = xyToDegrees(x, y);
                //System.out.println("Dial-degrees:" + d);

                if (!Float.isNaN(dragStartDeg)) {
                    float currentDeg = xyToDegrees(x, y);

                    if (!Float.isNaN(currentDeg)) {
                        float degPerNick = 360.0f / getTotalNicks();
                        float deltaDeg = dragStartDeg - currentDeg;

                        final int nicks = (int)(Math.signum(deltaDeg) * Math.floor(Math
                                .abs(deltaDeg) / degPerNick));

                        if (nicks != 0) {
                            dragStartDeg = currentDeg;
                            rotate(nicks);
                        }
                    }
                } else {
                }
            }
        });
    }

    private float xyToDegrees(float x, float y) {
        Vector2 vector = new Vector2(x, y);
        float distanceFromCenter = vector.len();
        System.out.println(distanceFromCenter);
        if (distanceFromCenter < 10f || distanceFromCenter > 100f) {
            return Float.NaN;
        }
        float degrees = (float)Math.toDegrees(Math.atan2(x, y));
        return degrees;
    }

    @Override
    protected void createChildren() {
        DialStyle style = getStyle();
        background = new Image(style.background);

        knob = new Image(style.knob);
        knob.setTouchable(Touchable.disabled);

        Stack stack = new Stack();
        stack.add(background);
        stack.add(knob);

        add(stack).size(background.getPrefWidth(), background.getPrefHeight());
    }

    @Override
    public void layout() {
        super.layout();
        knob.setOrigin(getWidth() / 2, getHeight() / 2);
        knob.setRotation(getRotationInDegrees());
    }

    public interface OnDialListener {
        void onDialPositionChanged(Dial sender, int nicksChanged);

        void onIncrement();

        void onDecrement();
    }

    //--------------------------------------------------------------------------

    private float dragStartDeg;

    private int totalNicks = 12;

    private int currentNick = 0;

    private int lastNick = 0;

    public final int getTotalNicks() {
        return totalNicks;
    }

    public final int getCurrentNick() {
        return currentNick;
    }

    public final float getRotationInDegrees() {
        return (360.0f / totalNicks) * currentNick;
    }

    public final void rotate(int nicks) {
        lastNick = currentNick;

        currentNick = (currentNick + nicks);
        if (currentNick >= totalNicks) {
            currentNick %= totalNicks;
        } else if (currentNick < 0) {
            currentNick = (totalNicks + currentNick);
        }

        int change = currentNick - lastNick;
        if (change > 0)
            change = Math.min(change, 1);
        else if (change < 0)
            change = Math.max(change, -1);

        if (change > 0) {
            //onDialListener.onIncrement();
            System.out.println("Dial-onIncrement:");
        }

        if (change < 0) {
            //onDialListener.onDecrement();
            System.out.println("Dial-onDecrement:");
        }

        invalidate();
    }

    private OnDialListener onDialListener;

    public void setOnDialListener(OnDialListener l) {
        onDialListener = l;
    }

    public static class DialStyle {
        public Drawable background;

        public Drawable knob;

        public DialStyle() {
        }
    }
}

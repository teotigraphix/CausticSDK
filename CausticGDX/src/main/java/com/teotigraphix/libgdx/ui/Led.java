
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class Led extends Stack {

    private Skin skin;

    private boolean isOn;

    private Image offImage;

    private Image onImage;

    public Led(Skin skin) {
        this.skin = skin;
        createChilren();
    }

    private void createChilren() {
        offImage = new Image(skin.getDrawable("led_off"));
        onImage = new Image(skin.getDrawable("led_on"));
        onImage.setVisible(false);

        add(offImage);
        add(onImage);
    }

    @Override
    public void layout() {
        super.layout();

        onImage.setVisible(isOn);
    }

    /**
     * Called by client to turn on the led for the specified amount.
     * 
     * @param millis
     */
    public void turnOn(float millis) {
        turnOn();
        onImage.addAction(Actions.sequence(Actions.fadeIn(millis), Actions.delay(0.05f),
                Actions.fadeOut(millis), new Action() {
                    @Override
                    public boolean act(float delta) {
                        onImage.setVisible(false);
                        return true;
                    }
                }));
    }

    public void turnOn() {
        isOn = true;
        invalidate();
    }

    public void turnOff() {
        isOn = false;
        invalidate();
    }
}

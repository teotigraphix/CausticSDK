
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Pools;

public class ToggleButton extends TextButton {

    private boolean noEvent;

    //----------------------------------
    // isToggle
    //----------------------------------

    boolean isToggle = false;

    public boolean isToggle() {
        return isToggle;
    }

    public void setToggle(boolean value) {
        isToggle = value;
    }

    @Override
    public void setChecked(boolean isChecked) {
        if (!isToggle)
            return;

        if (this.isChecked == isChecked)
            return;
        if (buttonGroup != null && !buttonGroup.canCheck(this, isChecked))
            return;
        this.isChecked = isChecked;
        if (!isDisabled && !noEvent) {
            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
            if (fire(changeEvent))
                this.isChecked = !isChecked;
            Pools.free(changeEvent);
        }
    }

    /**
     * Checks the button without dispatching a {@link ChangeEvent} event.
     * 
     * @param checked Whether the ui shows checked or unchecked.
     */
    public void check(boolean checked) {
        noEvent = true;
        setChecked(checked);
        noEvent = false;
    }

    public ToggleButton(String text, Skin skin) {
        super(text, skin);
    }

    public ToggleButton(String text, TextButtonStyle style) {
        super(text, style);
    }

    public ToggleButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

}


package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Pools;

public class ToggleButtonInternal extends TextButton {

    @Override
    public void setChecked(boolean isChecked) {
        setChecked(isChecked, true);
    }

    public void setChecked(boolean isChecked, boolean fireEvent) {
        if (this.isChecked == isChecked)
            return;
        if (buttonGroup != null && !buttonGroup.canCheck(this, isChecked))
            return;
        this.isChecked = isChecked;
        if (fireEvent) {
            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
            if (fire(changeEvent))
                this.isChecked = !isChecked;
            Pools.free(changeEvent);
        }
    }

    public ToggleButtonInternal(String text, Skin skin) {
        super(text, skin);
    }

    public ToggleButtonInternal(String text, TextButtonStyle style) {
        super(text, style);
    }

    public ToggleButtonInternal(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

}

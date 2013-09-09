
package com.teotigraphix.libgdx.ui.caustk;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.caustk.StepButton.OnStepButtonListener;
import com.teotigraphix.libgdx.ui.caustk.StepButton.StepButtonItem;

public class StepKeyboard extends ControlTable {

    @SuppressWarnings("unused")
    private OnStepKeyboardListener onStepKeyboardListener;

    public StepKeyboard(Skin skin) {
        super(skin);
        //debug();
    }

    StepButton lastStepButton;

    int[] sharps = new int[] {
            1, 4, 6, 9, 11, 13
    };

    private boolean isSharp(int index) {
        for (int i = 0; i < sharps.length; i++) {
            if (sharps[i] == index)
                return true;
        }
        return false;
    }

    @Override
    protected void createChildren() {
        for (int i = 0; i < 16; i++) {
            String text = "" + (i + 1);
            String styleName = "step-button";
            if (isSharp(i))
                styleName = "step-button-sharp";

            StepButtonItem item = new StepButtonItem(i, text, styleName);
            final StepButton stepButton = new StepButton(item, getSkin());
            stepButton.setOnStepButtonListener(new OnStepButtonListener() {
                @Override
                public void onChange(int index, boolean selected) {
                    System.out.println("index:" + index + " selected:" + selected);
                    if (lastStepButton != null)
                        lastStepButton.selectActive(false);
                    stepButton.selectActive(true);
                    lastStepButton = stepButton;
                }
            });
            add(stepButton).space(10f).size(50f, 70f).fill().expand().minWidth(0f).prefWidth(999f);
        }
    }

    public void setOnStepKeyboardListener(OnStepKeyboardListener l) {
        onStepKeyboardListener = l;
    }

    public interface OnStepKeyboardListener {
        void onStepChange(int index, boolean selected);
    }
}

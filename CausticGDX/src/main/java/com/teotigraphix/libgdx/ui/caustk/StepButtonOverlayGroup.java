
package com.teotigraphix.libgdx.ui.caustk;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class StepButtonOverlayGroup extends WidgetGroup {

    private Skin skin;

    private Image selectedOverlay;

    float selectionDuration = 0.2f;

    public StepButtonOverlayGroup(Skin skin) {
        this.skin = skin;
        setTouchable(Touchable.disabled);
        createChildren();
    }

    private void createChildren() {
        selectedOverlay = new Image(skin.getDrawable("pad_lit_green"));
        selectedOverlay.setVisible(false);
        addActor(selectedOverlay);
    }

    public void select(boolean selected) {
        selectedOverlay.setVisible(selected);
        if (selected) {
            selectedOverlay.addAction(Actions.forever(Actions.sequence(
                    Actions.fadeIn(selectionDuration), Actions.delay(0.15f),
                    Actions.fadeOut(selectionDuration), new Action() {
                        @Override
                        public boolean act(float delta) {
                            return true;
                        }
                    })));
        } else {
            selectedOverlay.clearActions();
        }
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();

        if (selectedOverlay.isVisible()) {
            selectedOverlay.setPosition(-5f, -5f);
            selectedOverlay.setSize(getWidth() + 10f, getHeight() + 10f);
        }

    }

}

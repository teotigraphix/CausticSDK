////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.libgdx.ui.caustk;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class StepButtonOverlayGroup extends WidgetGroup {

    private Skin skin;

    private Image activeOverlay;

    private Image currentOverlay;

    float activeDuration = 0.2f;

    public StepButtonOverlayGroup(Skin skin) {
        this.skin = skin;
        setTouchable(Touchable.disabled);
        createChildren();
    }

    private void createChildren() {
        activeOverlay = new Image(skin.getDrawable("pad_lit_green"));
        activeOverlay.setVisible(false);
        addActor(activeOverlay);

        currentOverlay = new Image(skin.getDrawable("pad_lit_green"));
        currentOverlay.setVisible(false);
        addActor(currentOverlay);
    }

    public void current(boolean selected) {
        currentOverlay.setVisible(selected);
        if (selected) {
            currentOverlay.addAction(Actions.forever(Actions.sequence(
                    Actions.fadeIn(activeDuration), Actions.delay(0.15f),
                    Actions.fadeOut(activeDuration), new Action() {
                        @Override
                        public boolean act(float delta) {
                            return true;
                        }
                    })));
        } else {
            currentOverlay.clearActions();
        }
        invalidate();
    }

    public void active(boolean selected) {
        activeOverlay.setVisible(selected);
        if (selected) {
            activeOverlay.addAction(Actions.forever(Actions.sequence(
                    Actions.fadeIn(activeDuration), Actions.delay(0.15f),
                    Actions.fadeOut(activeDuration), new Action() {
                        @Override
                        public boolean act(float delta) {
                            return true;
                        }
                    })));
        } else {
            activeOverlay.clearActions();
        }
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();

        if (activeOverlay.isVisible()) {
            activeOverlay.setPosition(-5f, -5f);
            activeOverlay.setSize(getWidth() + 10f, getHeight() + 10f);
        }

        if (currentOverlay.isVisible()) {
            currentOverlay.setPosition(-5f, -5f);
            currentOverlay.setSize(getWidth() + 10f, getHeight() + 10f);
        }
    }

}

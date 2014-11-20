
package com.teotigraphix.gdx.groove.ui.components.auto;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.gdx.scene2d.ui.TextSlider;

public class AutoTextSlider extends AutomationTableBase {

    private TextSlider slider;

    @Override
    public float getValue() {
        return slider.getValue();
    }

    @Override
    public void setValue(float value) {
        slider.setValue(value);
    }

    public AutoTextSlider(Skin skin, AutomationItem automationItem) {
        super(skin, automationItem);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        AutomationItem item = getAutomationItem();
        slider = new TextSlider(item.getLabel(), item.getMin(), item.getMax(), item.getStep(),
                true, getSkin());
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fireChange();
            }
        });
        add(slider).expand().fill();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        slider.setColor(getColor());
        super.draw(batch, parentAlpha);
    }

}

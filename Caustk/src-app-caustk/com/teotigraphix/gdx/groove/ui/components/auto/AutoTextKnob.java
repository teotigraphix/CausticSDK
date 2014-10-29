
package com.teotigraphix.gdx.groove.ui.components.auto;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.gdx.scene2d.ui.TextKnob;

public class AutoTextKnob extends AutomationTableBase {

    private TextKnob knob;

    @Override
    public float getValue() {
        return knob.getValue();
    }

    @Override
    public void setValue(float value) {
        knob.setValue(value);
    }

    public AutoTextKnob(Skin skin, AutomationItem automationItem) {
        super(skin, automationItem);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        AutomationItem item = getAutomationItem();
        knob = new TextKnob(item.getMin(), item.getMax(), 0.01f,
                item.getControl().getDisplayName(), getSkin(), "default");
        knob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fireChange();
            }
        });
        add(knob).expand().fill();
    }
}


package com.teotigraphix.gdx.groove.ui.components.auto;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.gdx.groove.ui.components.UITable;
import com.teotigraphix.gdx.groove.ui.components.auto.AutomationTableListener.AutomationTableEvent;
import com.teotigraphix.gdx.groove.ui.components.auto.AutomationTableListener.AutomationTableEventKind;

public abstract class AutomationTableBase extends UITable {

    public abstract float getValue();

    public abstract void setValue(float value);

    private AutomationItem automationItem;

    public AutomationItem getAutomationItem() {
        return automationItem;
    }

    public AutomationTableBase(Skin skin, AutomationItem automationItem) {
        super(skin);
        this.automationItem = automationItem;
    }

    @Override
    protected void createChildren() {
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (count == 2) {
                    resetDefaultValue();
                }
            }
        });
    }

    protected void resetDefaultValue() {
        setValue(automationItem.getControl().getDefaultValue());
    }

    protected void fireChange() {
        automationItem.setValue(getValue());

        AutomationTableEvent event = Pools.obtain(AutomationTableEvent.class);
        event.setKind(AutomationTableEventKind.ValueChange);
        event.setAutomationItem(automationItem);
        fire(event);
        Pools.free(event);
    }

}

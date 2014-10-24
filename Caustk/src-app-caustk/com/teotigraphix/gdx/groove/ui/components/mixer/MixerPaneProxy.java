
package com.teotigraphix.gdx.groove.ui.components.mixer;

import com.teotigraphix.caustk.controller.core.AbstractDisplay;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage.MixerChannelControl;
import com.teotigraphix.caustk.node.BehaviorUtils;
import com.teotigraphix.gdx.groove.app.GrooveBehavior;
import com.teotigraphix.gdx.groove.ui.components.mixer.MixerPane.MixerPaneListener;

/**
 * @see MixerPane
 * @see MixerPaneItem
 * @see #create()
 * @see #redraw()
 */
public class MixerPaneProxy {

    private GrooveBehavior behavior;

    private MixerPane pane;

    public MixerPane getPane() {
        return pane;
    }

    private AbstractDisplay display;

    public MixerPaneProxy(GrooveBehavior behavior, AbstractDisplay display) {
        this.behavior = behavior;
        this.display = display;
    }

    public MixerPane create() {
        pane = new MixerPane(behavior.getSkin(), false);
        pane.setMixerPaneListener(new MixerPaneListener() {
            @Override
            public void onSend(int index, MixerChannelControl control, float value) {
                send(index, control, value);
            }
        });
        pane.create("default");
        return pane;
    }

    public void redraw() {
        pane.redraw(behavior.getRack().getMachines());
    }

    protected void send(int index, MixerChannelControl control, float value) {
        BehaviorUtils.send(behavior.getRack(), index, control, value);
        //viewManager.getSubDisplay().setCell(0, 1, value + "").done(0);
        if (display != null)
            display.showNotification(value + "", 1f);
    }

}


package com.teotigraphix.gdx.groove.ui.components.mixer;

import java.util.List;

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

    private MixerPanePropertyProvider provider;

    public MixerPaneProxy(GrooveBehavior behavior, MixerPanePropertyProvider provider) {
        this.behavior = behavior;
        this.provider = provider;
    }

    public MixerPane create() {
        pane = new MixerPane(behavior.getSkin(), provider);
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

    public void redrawTriggers(List<Integer> machines) {
        pane.redrawTriggers(machines);
    }

    protected void send(int index, MixerChannelControl control, float value) {
        BehaviorUtils.send(behavior.getRack(), index, control, value);
        //viewManager.getSubDisplay().setCell(0, 1, value + "").done(0);
        if (provider.getDisplay() != null)
            provider.getDisplay().showNotification(value + "", 1f, true);
    }

}

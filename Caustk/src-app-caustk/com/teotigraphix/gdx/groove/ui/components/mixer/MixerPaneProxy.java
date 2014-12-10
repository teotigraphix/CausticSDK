
package com.teotigraphix.gdx.groove.ui.components.mixer;

import java.util.List;

import com.teotigraphix.caustk.core.osc.MixerControls;
import com.teotigraphix.caustk.core.osc.OSCUtils;
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
            public void onSend(int index, MixerControls control, float value) {
                send(index, control, value);
            }
        });
        pane.create("default");
        return pane;
    }

    public void redraw() {
        pane.redraw(behavior.getRack().machines());
        pane.onMachineSelection(behavior.getProjectModel().getMachineAPI().getSelectedMachine());
    }

    public void redrawTriggers(List<Integer> machines) {
        pane.redrawTriggers(machines);
    }

    protected void send(int index, MixerControls control, float value) {
        BehaviorUtils.send(behavior.getRack(), index, control, value);
        if (provider.getDisplay() != null)
            provider.getDisplay().showNotification(
                    OSCUtils.optimizeName(control.getDisplayName(), 8) + " "
                            + OSCUtils.precision(value, 2), 1f, true);
    }

}

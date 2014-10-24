
package com.teotigraphix.gdx.groove.ui.components.mixer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage.MixerChannelControl;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.gdx.groove.ui.components.UITable;
import com.teotigraphix.gdx.groove.ui.components.mixer.MixerPaneItem.MixerPaneItemListener;

public class MixerPane extends UITable {

    private ScrollPane scrollPane;

    private List<MixerPaneItem> mixers = new ArrayList<MixerPaneItem>();

    private MixerPaneListener listener;

    private MixerPaneItem masterItem;

    private MixerPanePropertyProvider povider;

    public MixerPane(Skin skin, MixerPanePropertyProvider povider) {
        super(skin);
        this.povider = povider;
    }

    @Override
    protected void createChildren() {
        Table root = new Table();

        scrollPane = new ScrollPane(root);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlickScroll(false);

        for (int i = 0; i < 14; i++) {
            MixerPaneItem item = new MixerPaneItem(getSkin(), i);
            item.setMixerPaneItemListener(new MixerPaneItemListener() {
                @Override
                public void onSend(int index, MixerChannelControl control, float value) {
                    listener.onSend(index, control, value);
                }
            });
            item.create("default");
            root.add(item).size(50f, 400f);
            mixers.add(item);
        }

        if (povider.hasMaster()) {
            masterItem = new MixerPaneItem(getSkin(), -1);
            masterItem.setMixerPaneItemListener(new MixerPaneItemListener() {
                @Override
                public void onSend(int index, MixerChannelControl control, float value) {
                    listener.onSend(index, control, value);
                }
            });
            masterItem.create("default");
            root.add(masterItem).size(60f, 400f);
        }

        add(scrollPane);//.size(400f);
    }

    public void scrollLeft(Rectangle bounds, boolean down) {
        doScroll(bounds);
    }

    public void scrollRight(Rectangle bounds, boolean down) {
        doScroll(bounds);
    }

    private void doScroll(Rectangle bounds) { // the mixerpaneitem bounds
        scrollPane.scrollTo(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void onMachineSelection(MachineNode machineNode) {
        for (MixerPaneItem item : mixers) {
            item.setSelected(false);
        }
        mixers.get(machineNode.getIndex()).setSelected(true);
    }

    public void refreshSolo(Collection<? extends MachineNode> machines) {
        for (MachineNode machineNode : machines) {
            MixerPaneItem item = mixers.get(machineNode.getIndex());
            item.refreshSolo(machineNode);
        }
    }

    public static interface MixerPaneListener {
        void onSend(int index, MixerChannelControl control, float value);
    }

    public void setMixerPaneListener(MixerPaneListener l) {
        this.listener = l;

    }

    public void redraw(Collection<? extends MachineNode> machines) {
        for (MachineNode machineNode : machines) {
            redraw(machineNode);
        }
    }

    private void redraw(MachineNode machineNode) {
        MixerPaneItem item = mixers.get(machineNode.getIndex());
        item.setMachineColor(povider.getItemColor(machineNode.getIndex()));
        item.redraw(machineNode);

        if (povider.hasMaster()) {
            masterItem.redraw(povider.getRack().getRackNode().getMaster());
        }
    }
}

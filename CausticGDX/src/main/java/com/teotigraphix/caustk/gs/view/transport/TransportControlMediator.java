
package com.teotigraphix.caustk.gs.view.transport;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerTransportChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup.OnTransportGroupListener;

public class TransportControlMediator extends CaustkMediator {

    private TransportGroup view;

    public TransportControlMediator() {
    }

    @Override
    public void onAttach() {
        super.onAttach();

        // listen for transport changes on the main sequencer
        register(getController().getSystemSequencer(), OnSystemSequencerTransportChange.class,
                new EventObserver<OnSystemSequencerTransportChange>() {
                    @Override
                    public void trigger(OnSystemSequencerTransportChange object) {
                        view.selectPlayPause(getController().getSystemSequencer().isPlaying());
                    }
                });
    }

    @Override
    public void create(IScreen screen) {
        final Table table = new Table();
        table.debug();

        view = new TransportGroup(screen.getSkin());
        view.setOnTransportGroupListener(new OnTransportGroupListener() {
            @Override
            public void onTransposeClick() {
            }

            @Override
            public void onTapClick() {
            }

            @Override
            public void onStopClick() {
                getController().execute(ISystemSequencer.COMMAND_STOP);
            }

            @Override
            public void onRecordChange(boolean selected) {
            }

            @Override
            public void onPlayChange(boolean selected) {
                if (selected) {
                    getController().execute(ISystemSequencer.COMMAND_PLAY,
                            SequencerMode.PATTERN.getValue());
                } else {
                    getController().execute(ISystemSequencer.COMMAND_STOP);
                }
            }
        });
        table.add(view);

        UIUtils.setBounds(table, UI.boundsTransport);
        screen.getStage().addActor(table);
    }
}

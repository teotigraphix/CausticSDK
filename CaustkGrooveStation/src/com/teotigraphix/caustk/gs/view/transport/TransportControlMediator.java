
package com.teotigraphix.caustk.gs.view.transport;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.gs.view.screen.MachineMediatorBase;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerTransportChange;
import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup.OnTransportGroupListener;

public class TransportControlMediator extends MachineMediatorBase {

    private TransportGroup view;

    public TransportControlMediator() {
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

        // listen for transport changes on the main sequencer
        register(getController(), OnSystemSequencerTransportChange.class,
                new EventObserver<OnSystemSequencerTransportChange>() {
                    @Override
                    public void trigger(OnSystemSequencerTransportChange object) {
                        view.selectPlayPause(getController().getRackSet().getSequencer()
                                .isPlaying());
                    }
                });
    }

    public Table createTable(IScreen screen) {
        return new Table();
    }

    @Override
    public void onCreate(IScreen screen) {
        final Table table = createTable(screen);
        //table.debug();

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
                try {
                    //getController().execute(ISystemSequencer.COMMAND_STOP);
                    getController().getRackSet().getSequencer().stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRecordChange(boolean selected) {
            }

            @Override
            public void onPlayChange(boolean selected) {
                if (selected) {
                    try {
                        //getController().execute(ISystemSequencer.COMMAND_PLAY,
                        //        SequencerMode.PATTERN.getValue());
                        getController().getRackSet().getSequencer().play(SequencerMode.Pattern);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        //getController().execute(ISystemSequencer.COMMAND_STOP);
                        getController().getRackSet().getSequencer().stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        table.add(view);
    }
}

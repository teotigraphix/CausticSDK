
package com.teotigraphix.caustk.gs.view.transportControls;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.teotigraphix.caustk.gs.view.GrooveBoxMediatorChild;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerTransportChange;
import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup.OnTransportGroupListener;

public class TransportButtonsMediator extends GrooveBoxMediatorChild {

    private TransportGroup view;

    public TransportButtonsMediator() {
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

        register(getController().getRack().getDispatcher(), OnSystemSequencerTransportChange.class,
                new EventObserver<OnSystemSequencerTransportChange>() {
                    @Override
                    public void trigger(OnSystemSequencerTransportChange object) {
                        refreshView();
                    }
                });
    }

    @Override
    public void onCreate(IScreen screen, Cell<Actor> parent) {
        super.onCreate(screen, parent);

        final Table table = new Table();
        table.left().padLeft(60f);
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
                        getController().getRackSet().getSequencer().play(SequencerMode.Pattern);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        getController().getRackSet().getSequencer().stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        table.add(view);
        parent.setWidget(table);
    }

    protected void refreshView() {
        view.selectPlayPause(getController().getRackSet().getSequencer().isPlaying());
    }
}

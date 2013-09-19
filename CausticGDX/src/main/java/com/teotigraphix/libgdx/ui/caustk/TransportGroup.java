
package com.teotigraphix.libgdx.ui.caustk;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.libgdx.ui.ControlTable;

@SuppressWarnings("unused")
public class TransportGroup extends ControlTable {

    private Button recordButton;

    private Button stopButton;

    private Button playPauseButton;

    private Button tapButton;

    private Button transposeButton;

    private OnTransportGroupListener onTransportGroupListener;

    public TransportGroup(Skin skin) {
        super(skin);
    }

    @Override
    protected void createChildren() {
        defaults().pad(10f);

        createRecordButton();
        createStopButton();
        createPlayPauseButton();
        createTapButton();
        createTransposeButton();
    }

    // XXX Figure out if you are going to use graphic width/height
    //     or set the width/height on the table cell

    private void createRecordButton() {
        recordButton = createButton("transport/record");
        recordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onTransportGroupListener.onRecordChange(recordButton.isChecked());
            }
        });
    }

    private void createStopButton() {
        stopButton = createButton("transport/stop");
        stopButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
            }
        });
        stopButton.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onTransportGroupListener.onStopClick();
            }
        });
    }

    private void createPlayPauseButton() {
        playPauseButton = createButton("transport/play_pause");
        playPauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onTransportGroupListener.onPlayChange(playPauseButton.isChecked());
            }
        });
    }

    private void createTapButton() {
        tapButton = createButton("transport/tap");
        tapButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
            }
        });
        tapButton.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onTransportGroupListener.onTapClick();
            }
        });
    }

    private void createTransposeButton() {
        transposeButton = createButton("transport/transpose");
        // transpose;
        // touchDown, touchDown step key for octave, touchUp()
        // - if after this operation the octave is other than 0, this
        //   control shows the selected state
    }

    private Button createButton(String imagePath) {
        Image image = new Image(getSkin().getDrawable(imagePath));
        Button button = new Button(image, getSkin(), "default");
        add(button);
        return button;
    }

    public void setOnTransportGroupListener(OnTransportGroupListener l) {
        onTransportGroupListener = l;
    }

    public interface OnTransportGroupListener {
        void onRecordChange(boolean selected);

        void onStopClick();

        void onPlayChange(boolean selected);

        void onTapClick();

        void onTransposeClick();
    }
}

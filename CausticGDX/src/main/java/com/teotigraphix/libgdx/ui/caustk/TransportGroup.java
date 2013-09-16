
package com.teotigraphix.libgdx.ui.caustk;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.libgdx.ui.ControlTable;

@SuppressWarnings("unused")
public class TransportGroup extends ControlTable {

    private Button recordButton;

    private Button stopButton;

    private Button playPauseButton;

    private Button tapButton;

    private Button transposeButton;

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

    private void createRecordButton() {
        recordButton = createButton("transport/record");
    }

    private void createStopButton() {
        stopButton = createButton("transport/stop");
    }

    private void createPlayPauseButton() {
        playPauseButton = createButton("transport/play_pause");
    }

    private void createTapButton() {
        tapButton = createButton("transport/tap");
    }

    private void createTransposeButton() {
        transposeButton = createButton("transport/transpose");
    }

    private Button createButton(String imagePath) {
        Image image = new Image(getSkin().getDrawable(imagePath));
        Button button = new Button(image, getSkin(), "default");
        add(button);
        return button;
    }
}

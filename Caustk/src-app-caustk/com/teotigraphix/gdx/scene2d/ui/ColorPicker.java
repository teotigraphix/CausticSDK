
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.caustk.controller.daw.Colors;
import com.teotigraphix.gdx.groove.ui.components.UITable;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;
import com.teotigraphix.gdx.scene2d.ui.ColorPickerListener.ColorPickerEvent;
import com.teotigraphix.gdx.scene2d.ui.ColorPickerListener.ColorPickerEventKind;

public class ColorPicker extends UITable {

    public ColorPicker(Skin skin) {
        super(skin);
    }

    @Override
    protected void createChildren() {

        Colors[] values = Colors.values();

        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                final Colors color = values[index];
                Image image = new Image(getSkin().getDrawable(
                        StylesDefault.ColorPicker_swatch_background));
                image.setColor(color.getColor());
                image.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        ColorPickerEvent e = new ColorPickerEvent(ColorPickerEventKind.ColorChange,
                                color);
                        fire(e);
                    }
                });
                add(image).expand().fill().pad(1f);
                index++;
            }
            row();
        }
    }

}

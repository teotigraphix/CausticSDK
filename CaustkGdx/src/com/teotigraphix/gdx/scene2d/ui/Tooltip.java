
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Tooltip extends Dialog {

    private String message;

    private Label label;

    private String postMessage;

    private Label postLabel;

    @Override
    public TooltipStyle getStyle() {
        return (TooltipStyle)super.getStyle();
    }

    public Tooltip(String message, String postMessage, TooltipStyle tooltipStyle) {
        super("", tooltipStyle);
        this.message = message;
        this.postMessage = postMessage;
        setModal(false);
        initialize();
    }

    private void initialize() {
        label = new Label(message, getStyle().labelStyle);
        pad(10f);
        getContentTable().add(label);

        if (postMessage != null) {
            postLabel = new Label(postMessage, getStyle().labelStyle);
            getContentTable().row();
            getContentTable().add(postLabel);
        }
    }

    public static Tooltip show(Stage stage, Skin skin, String message) {
        Tooltip tooltip = new Tooltip(message, null, skin.get(TooltipStyle.class));
        tooltip.show(stage);
        return tooltip;
    }

    public static Tooltip show(Stage stage, Skin skin, String message, String postMessage) {
        Tooltip tooltip = new Tooltip(message, postMessage, skin.get(TooltipStyle.class));
        tooltip.show(stage);
        return tooltip;
    }

    public static class TooltipStyle extends WindowStyle {
        public LabelStyle labelStyle;
    }
}


package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class Dialog extends Window {

    private Skin skin;

    public Dialog(String title, Skin skin, String styleName) {
        super(title, skin, styleName);
        this.skin = skin;

        // TODO Auto-generated constructor stub
    }

    public Dialog(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;
    }

    public Dialog(String title, WindowStyle style) {
        super(title, style);
    }

    @Override
    public void setStyle(WindowStyle style) {
        super.setStyle(style);
        createChilren();
    }

    private void createChilren() {
        createContent();
        createButtonBar();

    }

    private void createButtonBar() {
        Button okButton = new Button("OK", skin);
        Button cancelButton = new Button("Cancel", skin);
        okButton.setSize(100f, 40f);
        cancelButton.setSize(100f, 40f);
        defaults().spaceBottom(10);
        row().fill().expandX();
        add(okButton).fill(0f, 0f).width(100f).height(30f);
        add(cancelButton).fill(0f, 0f).width(100f);
        row();
        setSize(getPrefWidth(), getPrefHeight());
    }

    private void createContent() {
        // TODO Auto-generated method stub

    }

}

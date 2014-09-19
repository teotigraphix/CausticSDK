
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.scene2d.ui.AlertDialog;
import com.teotigraphix.gdx.scene2d.ui.ScrollList;

public class FileExplorer extends AlertDialog {

    private ScrollList scrollList;

    private Array<String> items;

    private Label statusLabel;

    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    public void setItems(Array<String> list) {
        this.items = list;
        scrollList.setItems(list);
    }

    public int getSelectedIndex() {
        return scrollList.getSelectedIndex();
    }

    public FileExplorer(String title, Skin skin, String styleName, String buttonStyleName) {
        super(title, skin, styleName, buttonStyleName);
        setTitleAlignment(Align.center);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        statusLabel = new Label("", getSkin(), "FileExplorer.StatusLabel");
        getContentTable().add(statusLabel).expandX().fillX().padLeft(10f).padTop(5f);

        getContentTable().row();

        items = new Array<String>();
        scrollList = new ScrollList(getSkin(), items);
        scrollList.setMouseDownChange(false);
        setTitleAlignment(Align.left | Align.bottom);
        getContentTable().add(scrollList).expand().fill().pad(4f);

        getButtonTable().padBottom(8f);

    }
}

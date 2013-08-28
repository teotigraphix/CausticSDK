
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;
import com.teotigraphix.libgdx.ui.Button;

public class AlertDialog extends Dialog {

    private Skin skin;

    public Skin getSkin() {
        return skin;
    }

    private Button okButton;

    private Button cancelButton;

    private OnAlertDialogListener listener;

    public AlertDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
        this.skin = skin;
        createChildren();
    }

    public AlertDialog(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;
        createChildren();
    }

    public AlertDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
        createChildren();
    }

    protected void createChildren() {
        // create buttons
        okButton = new Button("OK", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onOk();
                hide();
            }
        });
        cancelButton = new Button("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onCancel();
                hide();
            }
        });

        button(okButton, true);
        button(cancelButton, false);
        buttonTable.getCell(okButton).width(100f);
        buttonTable.getCell(cancelButton).width(100f);
    }

    @Override
    protected void result(Object object) {
        boolean result = (Boolean)object;
        if (result) {
            listener.onOk();
        } else {
            listener.onCancel();
        }
        hide();
    }

    public void setOnAlertDialogListener(OnAlertDialogListener l) {
        listener = l;
    }

    public interface OnAlertDialogListener {
        void onCancel();

        void onOk();
    }

    @SuppressWarnings("rawtypes")
    public Cell setContent(Actor actor) {
        return contentTable.add(actor);
    }

    /*
       public void changed (ChangeEvent event, Actor actor) {
    new Dialog("Some Dialog", skin, "dialog") {
    protected void result (Object object) {
    System.out.println("Chosen: " + object);
    }
    }.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
    .key(Keys.ESCAPE, false).show(stage);
    } 
       
    */

}

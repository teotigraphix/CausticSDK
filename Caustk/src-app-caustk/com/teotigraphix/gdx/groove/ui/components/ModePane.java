
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.groove.ui.components.ModePaneListener.ModePaneEvent;
import com.teotigraphix.gdx.groove.ui.components.ModePaneListener.ModePaneEventKind;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;
import com.teotigraphix.gdx.scene2d.ui.ButtonBarListener;

public class ModePane extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Array<ButtonBarItem> items;

    private ButtonBar buttonBar;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ModePane(Skin skin, Array<ButtonBarItem> items) {
        super(skin);
        this.items = items;
        setStyleClass(ModePaneStyle.class);
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public void select(int index) {
        buttonBar.select(index, true);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        ModePaneStyle style = getStyle();

        setBackground(style.backgorund);

        buttonBar = new ButtonBar(getSkin(), items, true, style.buttonStyle);
        buttonBar.setGap(style.buttonGap);
        buttonBar.addListener(new ButtonBarListener() {
            @Override
            public void selectedIndexChange(int selectedIndex) {
                ModePaneEvent event = new ModePaneEvent(ModePaneEventKind.selectedIndexChange,
                        selectedIndex);
                fire(event);
            }
        });

        buttonBar.create("default");

        add(buttonBar).expand().fill();
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class ModePaneStyle {

        public float buttonGap;

        public TextButtonStyle buttonStyle;

        /**
         * Optional.
         */
        public Drawable backgorund;

        public ModePaneStyle(TextButtonStyle buttonStyle, float buttonGap) {
            this.buttonStyle = buttonStyle;
            this.buttonGap = buttonGap;
        }
    }

}

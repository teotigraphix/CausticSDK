
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar;

public class TopBar extends UITable {

    private Table left;

    private Table center;

    private Table right;

    public Table getLeftChild() {
        return left;
    }

    public Table getCenterChild() {
        return center;
    }

    public ButtonBar getButtonBar() {
        return (ButtonBar)center.getChildren().get(0);
    }

    public Table getRightChild() {
        return right;
    }

    public TopBar(Skin skin) {
        super(skin);
        setStyleClass(TopBarStyle.class);
        debug();
    }

    @Override
    protected void createChildren() {

        TopBarStyle style = getStyle();
        setBackground(style.background);

        left = new Table();
        center = new Table();
        right = new Table();

        add(left).expandY().fillY().width(200f);
        add(center).expandY().fillY().width(400f);
        add(right).expandY().fillY().width(200f);
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class TopBarStyle {

        /**
         * The 9 slice background.
         */
        public Drawable background;

        public TopBarStyle(Drawable background) {
            this.background = background;
        }
    }

}

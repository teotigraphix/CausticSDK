
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar;

public class TopBar extends UITable {

    private Table left;

    private Table center;

    private Table right;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // left
    //----------------------------------

    public Table getLeftChild() {
        return left;
    }

    //----------------------------------
    // center
    //----------------------------------

    public Table getCenterChild() {
        return center;
    }

    //----------------------------------
    // right
    //----------------------------------

    public Table getRightChild() {
        return right;
    }

    //----------------------------------
    // buttonBar
    //----------------------------------

    public ButtonBar getButtonBar() {
        return (ButtonBar)center.getChildren().get(0);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TopBar(Skin skin) {
        super(skin);
        setStyleClass(TopBarStyle.class);
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

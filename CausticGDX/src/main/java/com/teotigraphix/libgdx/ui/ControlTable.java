
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class ControlTable extends Table implements ISkinAware {

    private boolean childrenCreated = false;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // skin
    //----------------------------------

    private Skin skin;

    @Override
    public Skin getSkin() {
        return skin;
    }

    @Override
    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    //----------------------------------
    // styleName
    //----------------------------------

    private String styleName;

    @Override
    public String getStyleName() {
        if (styleName == null)
            return "default";
        return styleName;
    }

    @Override
    public void setStyleName(String value) {
        styleName = value;
    }

    Class<? extends Object> styleClass;

    @SuppressWarnings("unchecked")
    public <T> T getStyle() {
        return (T)StyleUtils.getStyle(this, styleClass);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public ControlTable() {
        super();
    }

    public ControlTable(Skin skin) {
        super(skin);
    }

    @Override
    public void validate() {
        if (!childrenCreated) {
            initialize();
            createChildren();
            childrenCreated = true;
        }
        commitProperties();
        super.validate();
    }

    protected void initialize() {
    }

    protected void createChildren() {
    }

    protected void commitProperties() {
    }
}

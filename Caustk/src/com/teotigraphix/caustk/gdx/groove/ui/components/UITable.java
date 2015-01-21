////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.gdx.groove.ui.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class UITable extends Table {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Map<String, Object> properties;

    private String styleName;

    private Class<? extends Object> styleClass;

    private Skin skin;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // properties
    //----------------------------------

    /**
     * Lazy property map.
     */
    public final Map<String, Object> getProperties() {
        if (properties == null)
            properties = new HashMap<String, Object>();
        return properties;
    }

    //----------------------------------
    // styleName
    //----------------------------------

    /**
     * Returns the unique styleName for style lookup of this component.
     */
    public String getStyleName() {
        if (styleName == null)
            return "default";
        return styleName;
    }

    /**
     * Sets the unique styleName for style lookup.
     * 
     * @param value The String styleName.
     */
    public void setStyleName(String value) {
        styleName = value;
    }

    /**
     * Returns the component's style based on {@link #getStyleName()}.
     */
    @SuppressWarnings("unchecked")
    public <T> T getStyle() {
        return (T)getStyle(styleClass);
    }

    /**
     * Subclasses set the specific styleClass.
     * 
     * @param styleClass The class used for styling.
     */
    protected void setStyleClass(Class<? extends Object> styleClass) {
        this.styleClass = styleClass;
    }

    //----------------------------------
    // skin
    //----------------------------------

    /**
     * Returns the skin.
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Override original behavior, superclass will never create text, buttons
     * etc.
     */
    @Override
    public void setSkin(Skin skin) {
        super.setSkin(skin);
        this.skin = skin;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public UITable(Skin skin) {
        setSkin(skin);
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates the componet's children.
     * 
     * @param styleName The styleName to use for child construction.
     */
    public final void create(String styleName) {
        setStyleName(styleName);
        createChildren();
    }

    public final void create() {
        create("default");
    }

    /**
     * Create all component children using current {@link #getStyle()}.
     */
    protected abstract void createChildren();

    /**
     * Returns the component style based on styleName.
     * 
     * @param styleClass The styleClass to lookup it's styleName instance.
     */
    public <T> T getStyle(Class<T> styleClass) {
        return styleClass.cast(skin.get(styleName, styleClass));
    }
}

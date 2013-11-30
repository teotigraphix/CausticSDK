////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.libgdx.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.libgdx.controller.IValueAware;

public abstract class ControlTable extends Table implements ISkinAware, IValueAware {

    private boolean initialized = false;

    private boolean propertiedInvalidated = false;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // properties
    //----------------------------------

    private Map<String, Object> properties;

    public final Map<String, Object> getProperties() {
        if (properties == null)
            properties = new HashMap<String, Object>();
        return properties;
    }

    //----------------------------------
    // originalValue
    //----------------------------------

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public float getValue() {
        return Float.MIN_VALUE;
    }

    public boolean setValue(float value) {
        return false;
    }

    private float originalValue = Float.MIN_VALUE;

    @Override
    public float getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(float value) {
        originalValue = value;
    }

    @Override
    public void resetValue() {
        if (originalValue == Float.MIN_VALUE)
            return;
        setValue(originalValue);
    }

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

    protected Class<? extends Object> styleClass;

    @SuppressWarnings("unchecked")
    public <T> T getStyle() {
        return (T)StyleUtils.getStyle(this, styleClass);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public ControlTable(Skin skin) {
        super(skin);
        setSkin(skin);
    }

    protected void initialize() {
    }

    protected void createChildren() {
    }

    protected void invalidateProperties() {
        propertiedInvalidated = true;
        invalidate();
    }

    @Override
    public void validate() {
        if (!initialized) {
            initialize();
            createChildren();
            initialized = true;
        }
        if (propertiedInvalidated) {
            commitProperties();
            propertiedInvalidated = false;
        }
        super.validate();
    }

    protected void commitProperties() {
    }
}

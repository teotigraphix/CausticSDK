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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class ControlTable extends Table implements ISkinAware {

    private boolean childrenCreated = false;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // properties
    //----------------------------------

    Map<String, Object> properties;

    public final Map<String, Object> getProperties() {
        if (properties == null)
            properties = new HashMap<String, Object>();
        return properties;
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
        setSkin(skin);
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

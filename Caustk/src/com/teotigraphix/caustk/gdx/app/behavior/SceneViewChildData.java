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

package com.teotigraphix.caustk.gdx.app.behavior;

import com.teotigraphix.caustk.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

public class SceneViewChildData {
    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------
    private SceneViewChildBehavior behavior;

    private String id;

    private String label;

    private String icon;

    private String helpText;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // behavior
    //----------------------------------

    public SceneViewChildBehavior getBehavior() {
        return behavior;
    }

    //----------------------------------
    // id
    //----------------------------------

    public String getId() {
        return id;
    }

    //----------------------------------
    // label
    //----------------------------------

    public String getLabel() {
        return label;
    }

    //----------------------------------
    // icon
    //----------------------------------

    public String getIcon() {
        return icon;
    }

    //----------------------------------
    // helpText
    //----------------------------------

    public String getHelpText() {
        return helpText;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SceneViewChildData(SceneViewChildBehavior behavior, String id, String label,
            String icon, String helpText) {
        this.behavior = behavior;
        this.id = id;
        this.label = label;
        this.icon = icon;
        this.helpText = helpText;
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public ButtonBarItem toButtonBarItem() {
        return new ButtonBarItem(id, label, icon, helpText);
    }
}

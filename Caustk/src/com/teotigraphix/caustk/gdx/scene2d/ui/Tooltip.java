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

package com.teotigraphix.caustk.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Tooltip extends Dialog {

    private String message;

    private Label label;

    private String postMessage;

    private Label postLabel;

    @Override
    public TooltipStyle getStyle() {
        return (TooltipStyle)super.getStyle();
    }

    public Tooltip(String message, String postMessage, TooltipStyle tooltipStyle) {
        super("", tooltipStyle);
        this.message = message;
        this.postMessage = postMessage;
        setModal(false);
        initialize();
    }

    private void initialize() {
        label = new Label(message, getStyle().labelStyle);
        pad(10f);
        getContentTable().add(label);

        if (postMessage != null) {
            postLabel = new Label(postMessage, getStyle().labelStyle);
            getContentTable().row();
            getContentTable().add(postLabel);
        }
    }

    public static Tooltip show(Stage stage, Skin skin, String message) {
        Tooltip tooltip = new Tooltip(message, null, skin.get(TooltipStyle.class));
        tooltip.show(stage);
        return tooltip;
    }

    public static Tooltip show(Stage stage, Skin skin, String message, String postMessage) {
        Tooltip tooltip = new Tooltip(message, postMessage, skin.get(TooltipStyle.class));
        tooltip.show(stage);
        return tooltip;
    }

    public static class TooltipStyle extends WindowStyle {
        public LabelStyle labelStyle;
    }
}

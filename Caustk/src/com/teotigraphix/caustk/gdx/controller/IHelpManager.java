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

package com.teotigraphix.caustk.gdx.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teotigraphix.caustk.gdx.app.IApplicationComponent;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IHelpManager extends IApplicationComponent {

    void register(Actor actor, String helpText);

    void unregister(Actor actor);

    public static class HelpInfo {

        private ClickListener listener;

        private String text;

        public void setListener(ClickListener listener) {
            this.listener = listener;
        }

        public ClickListener getListener() {
            return listener;
        }

        public String getText() {
            return text;
        }

        public HelpInfo(String text) {
            this.text = text;
        }

        public HelpInfo(String text, String url) {
        }

        public void dispose() {
            listener = null;
            text = null;
        }
    }

    /**
     * If helpInfo is null, clear the help text.
     */
    public static class OnHelpManagerHelpChange {

        private HelpInfo helpInfo;

        public HelpInfo getHelpInfo() {
            return helpInfo;
        }

        public OnHelpManagerHelpChange(HelpInfo helpInfo) {
            this.helpInfo = helpInfo;
        }

        public boolean hasHelp() {
            return helpInfo != null;
        }
    }

}

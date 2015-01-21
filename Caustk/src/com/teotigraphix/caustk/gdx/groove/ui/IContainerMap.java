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

package com.teotigraphix.caustk.gdx.groove.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.teotigraphix.caustk.gdx.app.ui.CaustkScene;

public interface IContainerMap {

    void setScene(CaustkScene scene);

    WidgetGroup get(IContainerKind key);

    void put(IContainerKind key, WidgetGroup group);

    void addActor(IContainerKind kind, WidgetGroup actor);

    void register(Object object);

    /**
     * Container is a user interface component 'container'.
     */
    public enum TopBarViewStackLayout implements IContainerKind {

        /**
         * 0,440 | 800x40
         */
        TopBar("topBar", 0f, 440f, 800f, 40f),

        /**
         * 20,40 | 760x400
         */
        ViewPane("viewPane", 0f, 0f, 800f, 440f);

        /**
         * 0,0 | 800x40
         */
        //BottomBar("bottomBar", 0f, 0f, 800f, 40f);

        /**
         * Floating; 0,40 | 20x400
         */
        //LeftTrim("leftTrim", 0f, 40f, 20f, 400f),

        /**
         * Floating; 780,40 | 20x400
         */
        //RightTrim("rightTrim", 780f, 40f, 20f, 400f);

        private String id;

        private float x;

        private float y;

        private float width;

        private float height;

        @Override
        public String getId() {
            return id;
        }

        @Override
        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }

        private TopBarViewStackLayout(String id, float x, float y, float width, float height) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

}

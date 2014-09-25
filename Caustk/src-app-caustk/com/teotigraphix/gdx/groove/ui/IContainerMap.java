
package com.teotigraphix.gdx.groove.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.teotigraphix.gdx.app.CaustkScene;

public interface IContainerMap {

    void setScene(CaustkScene scene);

    WidgetGroup get(IContainerKind key);

    void addActor(IContainerKind kind, WidgetGroup actor);

    void register(Object object);

    /**
     * Container is a user interface component 'container'.
     */
    public enum MainTemplateLayout implements IContainerKind {

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

        private MainTemplateLayout(String id, float x, float y, float width, float height) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

}

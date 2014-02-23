
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.scene2d.ui.MenuRowRenderer.MenuRowRendererStyle;

public class MenuBar extends ButtonBar {

    private boolean isOpen;

    private boolean justOpened;

    private MenuBarStyle menuBarStyle;

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    public MenuBar(Skin skin, Array<MenuItem> items, MenuBarStyle menuBarStyle) {
        super(skin, (Array)items, false, menuBarStyle.buttonStyle);
        this.menuBarStyle = menuBarStyle;
        initialize();
    }

    protected void initialize() {
        setBackground(menuBarStyle.background);
        setSelectedIndex(-1);
    }

    private InputListener stageListener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Actor hit = getStage().hit(x, y, true);
            if (hit == menu) {
                execute();
            } else {
                hide();
            }
            return false;
        }
    };

    private Dialog menu;

    private List list;

    protected void show(TextButton listenerActor) {
        MenuItem menuItem = (MenuItem)listenerActor.getUserObject();

        isOpen = true;

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                getStage().addListener(stageListener);
            }
        });

        // currentMenuItems = new Object[menuItem.getChildren().size];
        //        int i = 0;
        //        for (MenuItem childMenuItem : menuItem.getChildren()) {
        //            currentMenuItems[i] = childMenuItem;
        //            i++;
        //        }

        Vector2 localCoords = new Vector2(listenerActor.getX(), listenerActor.getY());
        localCoords = listenerActor.getParent().localToStageCoordinates(localCoords);

        Dialog.fadeDuration = 0f;
        menu = new Menu(menuItem.getChildren(), menuBarStyle, getSkin());

        //        list = new List(currentMenuItems, menuBarStyle.listStyle);
        //        list.addCaptureListener(new InputListener() {
        //            @Override
        //            public boolean mouseMoved(InputEvent event, float x, float y) {
        //                touchMove(y);
        //                return super.mouseMoved(event, x, y);
        //            }
        //
        //            @Override
        //            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        //                MenuItem menuItem = (MenuItem)currentMenuItems[list.getSelectedIndex()];
        //                itemClick(menuItem);
        //                return super.touchDown(event, x, y, pointer, button);
        //            }
        //        });
        //        list.setSelectedIndex(-1);

        //menu.getContentTable().add(list).expand().fill();
        //menu.getButtonTable().remove();
        menu.show(getStage());
        // menu.setWidth(215f);
        menu.setPosition(localCoords.x, localCoords.y - menu.getHeight());
    }

    protected void execute() {
        String selection = list.getSelection();
        System.out.println("Execute: " + selection);
        hide();
    }

    protected void hide() {
        getStage().removeListener(stageListener);
        isOpen = false;
        getGroup().uncheckAll();
        setSelectedIndex(-1);
        menu.remove();
    }

    @Override
    protected void configureButton(TextButton button) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TextButton listenerActor = (TextButton)event.getListenerActor();
                int index = getChildren().indexOf(listenerActor, true);
                if (isOpen) {
                    hide();
                    select(index, true);
                    show(listenerActor);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isOpen) {
                    hide();
                } else {
                    justOpened = true;
                    Actor listenerActor = event.getListenerActor();
                    show((TextButton)listenerActor);
                }
                return true;
            }
        });

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!justOpened) {
                    hide();
                }
                justOpened = false;
            }
        });
    }

    public static class MenuItem extends ButtonBarItem {

        private Array<MenuItem> children;

        private MenuItem parent;

        private String command;

        private String keyBinding;

        private boolean isSeparator = false;

        public Array<MenuItem> getChildren() {
            return children;
        }

        public MenuItem getParent() {
            return parent;
        }

        public void setParent(MenuItem parent) {
            this.parent = parent;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getKeyBinding() {
            return keyBinding;
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isSeparator() {
            return isSeparator;
        }

        public MenuItem(String label, String icon, String keyBinding, String helpText) {
            super(label, icon, helpText);
            this.keyBinding = keyBinding;
            children = new Array<MenuBar.MenuItem>();
        }

        public MenuItem() {
            super("", "", "");
            isSeparator = true;
        }

        public MenuItem addItem(String label, String icon, String keyBinding, String helpText,
                String command) {
            MenuItem menuBarItem = new MenuItem(label, icon, keyBinding, helpText);
            children.add(menuBarItem);
            menuBarItem.setParent(this);
            menuBarItem.setCommand(command);
            return menuBarItem;
        }

        public void addSeparator() {
            MenuItem menuBarItem = new MenuItem();
            children.add(menuBarItem);
        }

        @Override
        public String toString() {
            return getLabel();
        }

    }

    public static class MenuBarStyle {

        public Drawable background;

        public TextButtonStyle buttonStyle;

        public MenuRowRendererStyle rowRendererStyle;

        public WindowStyle windowStyle;

        public MenuBarStyle() {
        }
    }
}


package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

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
            if (hit == dialog) {
                execute();
            } else {
                hide();
            }
            return false;
        }
    };

    private Dialog dialog;

    private List list;

    private Object[] currentMenuItems;

    protected void show(TextButton listenerActor) {
        MenuItem menuItem = (MenuItem)listenerActor.getUserObject();

        isOpen = true;

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                getStage().addListener(stageListener);
            }
        });

        currentMenuItems = new Object[menuItem.getChildren().size];
        int i = 0;
        for (MenuItem childMenuItem : menuItem.getChildren()) {
            currentMenuItems[i] = childMenuItem;
            i++;
        }

        Vector2 localCoords = new Vector2(listenerActor.getX(), listenerActor.getY());
        localCoords = listenerActor.getParent().localToStageCoordinates(localCoords);

        Dialog.fadeDuration = 0f;
        dialog = new Dialog("", menuBarStyle.windowStyle);
        dialog.setModal(false);

        list = new List(currentMenuItems, menuBarStyle.listStyle);
        list.addCaptureListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                touchMove(y);
                return super.mouseMoved(event, x, y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MenuItem menuItem = (MenuItem)currentMenuItems[list.getSelectedIndex()];
                itemClick(menuItem);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        list.setSelectedIndex(-1);

        dialog.getContentTable().add(list).expand().fill();
        dialog.getButtonTable().remove();
        dialog.show(getStage());
        dialog.setWidth(215f);
        dialog.setPosition(localCoords.x, localCoords.y - dialog.getHeight());
    }

    protected void itemClick(MenuItem menuItem) {
        System.out.println("ItemClick : " + menuItem);
    }

    void touchMove(float y) {
        int oldIndex = list.getSelectedIndex();
        int selectedIndex = (int)((list.getHeight() - y) / list.getItemHeight());
        selectedIndex = Math.max(0, selectedIndex);
        selectedIndex = Math.min(list.getItems().length - 1, selectedIndex);
        if (oldIndex != selectedIndex) {
            list.setSelectedIndex(selectedIndex);
            list.invalidate();
            list.validate();
        }
        //        if (oldIndex != selectedIndex) {
        //            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
        //            if (fire(changeEvent)) selectedIndex = oldIndex;
        //            Pools.free(changeEvent);
        //        }
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
        dialog.remove();
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

        public boolean isRoot() {
            return parent == null;
        }

        public MenuItem(String label, String icon, String helpText) {
            super(label, icon, helpText);
            children = new Array<MenuBar.MenuItem>();
        }

        public MenuItem addItem(String label, String icon, String helpText, String command) {
            MenuItem menuBarItem = new MenuItem(label, icon, helpText);
            children.add(menuBarItem);
            menuBarItem.setParent(this);
            menuBarItem.setCommand(command);
            return menuBarItem;
        }

        @Override
        public String toString() {
            return getLabel();
        }
    }

    public static class MenuBarStyle {

        public Drawable background;

        public TextButtonStyle buttonStyle;

        public ListStyle listStyle;

        public WindowStyle windowStyle;

        public MenuBarStyle() {
        }
    }
}

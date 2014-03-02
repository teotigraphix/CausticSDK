
package com.teotigraphix.gdx.scene2d.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.gdx.scene2d.ui.ListRowRenderer.ListRowRendererStyle;
import com.teotigraphix.gdx.scene2d.ui.ScrollList.LabelRow;

// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=11108&p=50062&hilit=list#p50062

public class AdvancedList<T extends ListRowRenderer> extends Table {

    private Array<T> renderers = new Array<T>();

    private int selectedIndex = -1;

    private boolean selectable = true;

    private Object[] items;

    public Object[] getItems() {
        return items;
    }

    private Class<T> type;

    private Skin skin;

    private boolean mouseDownChange = true;

    private ListRowRendererStyle rendererStyle;

    public boolean isMouseDownChange() {
        return mouseDownChange;
    }

    public void setMouseDownChange(boolean mouseDownChange) {
        this.mouseDownChange = mouseDownChange;
    }

    //private OnAdvancedListListener listener;

    protected Class<T> getType() {
        return type;
    }

    public AdvancedList() {
        this(null, null, null, null);
    }

    public AdvancedList(Object[] items, Class<T> type, Skin skin, ListRowRendererStyle rendererStyle) {
        this.items = items;
        this.type = type;
        this.skin = skin;
        this.rendererStyle = rendererStyle;

        //setWidth(getPrefWidth());
        //setHeight(getPrefHeight());

        align(Align.top);
        defaults().expandX().fillX();
    }

    /**
     * Sets whether this List's items are selectable. If not selectable, touch
     * events will not be consumed.
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /** @return True if items are selectable. */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * @return The index of the currently selected item. The top item has an
     *         index of 0. Nothing selected has an index of -1.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (renderers.size == 0)
            return;

        if (selectedIndex != -1)
            renderers.get(selectedIndex).setIsSelected(false);
        selectedIndex = value;
        if (value != -1)
            renderers.get(selectedIndex).setIsSelected(true);

        //        AdvancedListChangeEvent changeEvent = Pools.obtain(AdvancedListChangeEvent.class);
        //        Object selection = null;
        //        if (getSelection() != null) {
        //            selection = getSelection().getUserObject();
        //        }
        //        changeEvent.setSelectedIndex(selectedIndex, selection);
        //        if (fire(changeEvent)) {
        //        }
        //        Pools.free(changeEvent);

        invalidate();
    }

    public static class AdvancedListItemChangeEvent extends Event {
        private int selectedIndex;

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }
    }

    public static class AdvancedListChangeEvent extends Event {
        private int selectedIndex;

        private Object selection;

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public Object getSelection() {
            return selection;
        }

        public void setSelectedIndex(int selectedIndex, Object selection) {
            this.selectedIndex = selectedIndex;
            this.selection = selection;
        }
    }

    /**
     * @return The ListRow of the currently selected item, or null if the list
     *         is empty or nothing is selected.
     */
    public T getSelection() {
        if (renderers.size == 0 || selectedIndex == -1)
            return null;

        return renderers.get(selectedIndex);
    }

    @SuppressWarnings("unchecked")
    public void createChildren(Skin skin) {
        for (Object item : items) {
            String text = item.toString();
            try {
                Constructor<T> constructor = type.getConstructor(Skin.class);
                ListRowRenderer instance = constructor.newInstance(skin);
                instance.setStyle(rendererStyle);
                instance.setText(text);
                instance.setUserObject(item);
                instance.createChildren();
                addRenderItem((T)instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public float getItemHeight() {
        return renderers.get(0).getHeight();
    }

    public Array<T> _getItems() {
        return renderers;
    }

    public void addRenderItem(T item) {
        item.addCaptureListener(new ClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void clicked(InputEvent event, float x, float y) {
                T listenerActor = (T)event.getListenerActor(); // renderer item
                if (selectable && !mouseDownChange) {
                    fireChange(renderers.indexOf(listenerActor, false));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                T listenerActor = (T)event.getListenerActor(); // renderer item
                if (selectable && mouseDownChange) {
                    fireChange(renderers.indexOf(listenerActor, false));
                }
                return false;
            }
        });

        renderers.add(item);
        add(item);
        row();
    }

    public void removeRenderItem(T item) {
        item.remove();
        int _index = renderers.indexOf(item, false);
        renderers.removeValue(item, false);
        renderers.get(_index).setIsSelected(true);
    }

    /**
     * Remove the selected row
     */
    public void removeSelected() {
        if (selectedIndex < 0 || selectedIndex >= renderers.size)
            return;

        removeRenderItem(renderers.get(selectedIndex));
    }

    public static abstract class AdvancedListListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof AdvancedListEvent))
                return false;
            if (event instanceof AdvancedListChangeEvent)
                changed((AdvancedListChangeEvent)event, event.getTarget());
            if (event instanceof AdvancedListLongPressEvent)
                longPress((AdvancedListLongPressEvent)event, event.getTarget());
            if (event instanceof AdvancedListDoubleTapEvent)
                doubleTap((AdvancedListDoubleTapEvent)event, event.getTarget());
            return false;
        }

        public abstract void changed(AdvancedListChangeEvent event, Actor actor);

        public abstract void longPress(AdvancedListLongPressEvent event, Actor actor);

        public abstract void doubleTap(AdvancedListDoubleTapEvent event, Actor actor);

    }

    public static abstract class AdvancedListEvent extends Event {
    }

    public static class AdvancedListLongPressEvent extends AdvancedListEvent {
    }

    public static class AdvancedListDoubleTapEvent extends AdvancedListEvent {
    }

    //    public static class AdvancedListChangeEvent extends AdvancedListEvent {
    //    }

    /**
     * Refreshes the item renderers.
     */
    public void refresh() {
        int index = 0;
        for (Object item : items) {
            String text = item.toString();
            LabelRow instance = (LabelRow)getChildren().get(index);
            instance.setText(text);
            index++;
        }
    }

    public void setItems(Object[] items) {
        this.items = items;
        renderers.clear();
        selectedIndex = -1;
        clearChildren();
        createChildren(skin);
        invalidateHierarchy();
    }

    private void fireChange(int newSelectedIndex) {
        int oldIndex = selectedIndex;
        if (oldIndex != newSelectedIndex) {
            //            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
            //            setSelectedIndex(newSelectedIndex);
            //            if (fire(changeEvent)) {
            //                setSelectedIndex(oldIndex);
            //            }
            //            Pools.free(changeEvent);

            AdvancedListChangeEvent changeEvent = Pools.obtain(AdvancedListChangeEvent.class);
            setSelectedIndex(newSelectedIndex);

            Object selection = null;
            if (getSelection() != null) {
                selection = getSelection().getUserObject();
            }

            changeEvent.setSelectedIndex(newSelectedIndex, selection);

            if (fire(changeEvent)) {
                setSelectedIndex(oldIndex);
            }
            Pools.free(changeEvent);
        }
    }

}

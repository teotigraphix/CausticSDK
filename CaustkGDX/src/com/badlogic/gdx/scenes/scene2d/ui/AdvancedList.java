
package com.badlogic.gdx.scenes.scene2d.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.libgdx.ui.ScrollList.LabelRow;

// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=11108&p=50062&hilit=list#p50062

public class AdvancedList<T extends ListRowRenderer> extends Table {

    private Array<T> renderers = new Array<T>();

    private int selectedIndex;

    private boolean selectable = true;

    private Object[] items;

    private Class<T> type;

    //private OnAdvancedListListener listener;

    protected Class<T> getType() {
        return type;
    }

    public AdvancedList() {
        this(null, null);
    }

    public AdvancedList(Object[] items, Class<T> type) {
        this.items = items;
        this.type = type;

        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

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
                LabelRow instance = (LabelRow)constructor.newInstance(skin);
                instance.createChildren();
                instance.setText(text);
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

    public Array<T> _getItems() {
        return renderers;
    }

    public void addRenderItem(final T item) {
        item.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                renderers.get(selectedIndex).setIsSelected(false);
                selectedIndex = renderers.indexOf(item, false);
                item.setIsSelected(true);
            }

        });

        renderers.add(item);
        add(item).height(30f);
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

    public static class AdvancedListChangeEvent extends AdvancedListEvent {
    }

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
}

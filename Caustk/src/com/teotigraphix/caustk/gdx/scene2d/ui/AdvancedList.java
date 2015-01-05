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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.caustk.gdx.scene2d.ui.AdvancedListListener.AdvancedListEvent;
import com.teotigraphix.caustk.gdx.scene2d.ui.AdvancedListListener.AdvancedListEventKind;
import com.teotigraphix.caustk.gdx.scene2d.ui.ListRowRenderer.ListRowRendererStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.ScrollList.LabelRow;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=11108&p=50062&hilit=list#p50062

public class AdvancedList<T extends ListRowRenderer> extends Table {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Object[] items;

    private Class<T> type;

    private Skin skin;

    private ListRowRendererStyle rendererStyle;

    private Array<T> renderers = new Array<T>();

    private int selectedIndex = -1;

    private boolean selectable = true;

    private boolean mouseDownChange = true;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // mouseDownChange
    //----------------------------------

    public boolean isMouseDownChange() {
        return mouseDownChange;
    }

    public void setMouseDownChange(boolean mouseDownChange) {
        this.mouseDownChange = mouseDownChange;
    }

    //----------------------------------
    // items
    //----------------------------------

    public Object[] getItems() {
        return items;
    }

    public void setItems(Object[] items) {
        this.items = items;
        renderers.clear();
        selectedIndex = -1;
        clearChildren();
        createChildren(skin);
        invalidateHierarchy();
    }

    //----------------------------------
    // type
    //----------------------------------

    protected Class<T> getType() {
        return type;
    }

    //----------------------------------
    // selectable
    //----------------------------------

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

    //----------------------------------
    // selectedIndex
    //----------------------------------

    /**
     * @return The index of the currently selected item. The top item has an
     *         index of 0. Nothing selected has an index of -1.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;

        int oldIndex = selectedIndex;
        this.selectedIndex = value;

        AdvancedListEvent changeEvent = Pools.obtain(AdvancedListEvent.class);
        changeEvent.setKind(AdvancedListEventKind.Change);

        Object selection = null;
        if (getSelection() != null) {
            selection = getSelection().getUserObject();
        }

        changeEvent.setSelectedIndex(selectedIndex, selection);

        if (fire(changeEvent)) {
            this.selectedIndex = oldIndex;
        }
        Pools.free(changeEvent);

        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public AdvancedList() {
        this(null, null, null, null);
    }

    public AdvancedList(Object[] items, Class<T> type, Skin skin, ListRowRendererStyle rendererStyle) {
        this.items = items;
        this.type = type;
        this.skin = skin;
        this.rendererStyle = rendererStyle;

        align(Align.top);
        defaults().expandX().fillX();
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

    public void addRenderItem(final T item) {

        item.addListener(new ActorGestureListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                T listenerActor = (T)event.getListenerActor(); // renderer item
                if (selectable && !mouseDownChange) {
                    if (count == 1) {
                        fireChange(renderers.indexOf(listenerActor, false));
                    } else if (count == 2) {
                        AdvancedListEvent e = Pools.obtain(AdvancedListEvent.class);
                        e.setKind(AdvancedListEventKind.DoubleTap);
                        e.setSelectedIndex(selectedIndex, getSelection());
                        fire(e);
                        Pools.free(e);
                    }
                    listenerActor.setDown(false);
                }
            }
        });

        item.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                @SuppressWarnings("unchecked")
                T listenerActor = (T)event.getListenerActor(); // renderer item
                listenerActor.setDown(false);
            }

            @SuppressWarnings("unchecked")
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                T listenerActor = (T)event.getListenerActor(); // renderer item
                if (selectable && mouseDownChange) {
                    fireChange(renderers.indexOf(listenerActor, false));
                }
                if (selectable)
                    listenerActor.setDown(true);
                return true;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                Actor actor = event.getListenerActor();
                Vector2 localCoords = new Vector2(x, y);
                localCoords = actor.localToParentCoordinates(localCoords);
                touchMove(localCoords.y);
                return super.mouseMoved(event, x, y);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (toActor == null || !toActor.isDescendantOf(AdvancedList.this))
                    setOverIndex(-1);
            }
        });

        renderers.add(item);
        add(item);
        row();
    }

    private int overIndex = -1;

    public int getOverIndex() {
        return overIndex;
    }

    public void setOverIndex(int overIndex) {
        if (overIndex == this.overIndex)
            return;

        this.overIndex = overIndex;

        Object item = null;
        if (overIndex != -1) {
            item = items[overIndex];
        }

        AdvancedListEvent event = Pools.obtain(AdvancedListEvent.class);
        event.setKind(AdvancedListEventKind.OverChange);
        event.setOverIndex(overIndex, item);
        if (fire(event)) {
        }
        Pools.free(event);
        invalidate();
    }

    void touchMove(float y) {
        int oldIndex = overIndex;
        int newOverIndex = (int)((getHeight() - y) / getItemHeight());
        newOverIndex = Math.max(0, newOverIndex);
        newOverIndex = Math.min(getItems().length - 1, newOverIndex);
        if (oldIndex != newOverIndex) {
            setOverIndex(newOverIndex);
        }
    }

    public void removeRenderItem(T item) {
        item.remove();
        int _index = renderers.indexOf(item, false);
        renderers.removeValue(item, false);
        renderers.get(_index).setSelected(true);
    }

    /**
     * Remove the selected row
     */
    public void removeSelected() {
        if (selectedIndex < 0 || selectedIndex >= renderers.size)
            return;

        removeRenderItem(renderers.get(selectedIndex));
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

    @Override
    public void layout() {
        super.layout();

        if (renderers.size != 0) {
            for (T renderer : renderers) {
                renderer.setSelected(false);
                renderer.setOver(false);
            }

            if (selectedIndex != -1)
                renderers.get(selectedIndex).setSelected(true);

            if (overIndex != -1)
                renderers.get(overIndex).setOver(true);
        }
    }

    private void fireChange(int newSelectedIndex) {
        setSelectedIndex(newSelectedIndex);
    }
}

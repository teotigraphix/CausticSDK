
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarChangeEvent;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

/**
 * The {@link PaneStack} holds a stack of panes and uses a selectedIndex to show
 * the top pane while hiding all other panes.
 * <p>
 * The PaneStack also carries a {@link ButtonBar} option for quick tab bar like
 * functionality.
 * 
 * @author Michael Schmalle
 */
public class PaneStack extends Table {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Skin skin;

    private PaneStackStyle style;

    private Table barContainer;

    private Table contentContainer;

    private ButtonBar buttonBar;

    private Stack stack;

    Array<Actor> pendingPanes = new Array<Actor>();

    private int buttonBarAlign;

    private Float maxButtonSize;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        int old = selectedIndex;
        selectedIndex = value;
        PaneStackEvent event = Pools.obtain(PaneStackEvent.class);
        event.setType(PaneStackEventType.SelectedIndexChange);
        if (fire(event)) {
            selectedIndex = old;
        }
        Pools.free(event);
        invalidate();
    }

    //----------------------------------
    // maxButtonSize
    //----------------------------------

    public void setMaxButtonSize(Float maxButtonSize) {
        this.maxButtonSize = maxButtonSize;
        invalidateHierarchy();
    }

    public Float getMaxButtonSize() {
        return maxButtonSize;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * @param skin
     * @param buttonBarAlign An {@link Align} value, top or bottom.
     */
    public PaneStack(Skin skin, String styleName, int buttonBarAlign, float maxButtonSize) {
        super(skin);
        style = skin.get(styleName, PaneStackStyle.class);
        this.skin = skin;
        this.buttonBarAlign = buttonBarAlign;
        this.maxButtonSize = maxButtonSize;
        initialize();
    }

    public void addPane(Actor actor) {
        pendingPanes.add(actor);
    }

    protected void initialize() {
        pad(5f);

        barContainer = new Table();
        barContainer.left();
        barContainer.setBackground(style.tabBarBackground);

        Array<ButtonBarItem> items = new Array<ButtonBar.ButtonBarItem>();

        buttonBar = new ButtonBar(skin, items, false, style.tabStyle);
        buttonBar.padBottom(-1f).padTop(4f);
        buttonBar.padLeft(6f);
        buttonBar.setMaxButtonSize(maxButtonSize);
        buttonBar.setGap(style.tabBarGap);
        buttonBar.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ButtonBarChangeEvent) {
                    ButtonBarChangeEvent e = (ButtonBarChangeEvent)event;
                    int index = e.getSelectedIndex();
                    setSelectedIndex(index);
                    return true;
                }
                return false;
            }
        });

        barContainer.add(buttonBar);

        contentContainer = new Table();
        contentContainer.setBackground(style.background);
        contentContainer.pad(5f);

        stack = new Stack();

        if (buttonBarAlign == Align.top) {
            add(barContainer).fillX();
            row();
            add(contentContainer).expand().fill();
            contentContainer.add(stack).expand().fill();
        } else if (buttonBarAlign == Align.bottom) {
            add(contentContainer).expand().fill();
            contentContainer.add(stack).expand().fill();
            row();
            add(barContainer).fillX();
        }
    }

    protected void updateSelectedIndex() {
        buttonBar.select(selectedIndex, true);
        for (Actor actor : stack.getChildren()) {
            actor.setVisible(false);
        }
        stack.getChildren().get(selectedIndex).setVisible(true);
    }

    @Override
    public void layout() {
        if (pendingPanes.size > 0) {
            Array<ButtonBarItem> labels = new Array<ButtonBarItem>();
            for (Actor pane : pendingPanes) {
                stack.addActor(pane);
                labels.add(new ButtonBarItem(pane.getName(), "", "TODO PaneStack help text"));
            }
            pendingPanes.clear();
            buttonBar.setItems(labels);
        }

        super.layout();

        updateSelectedIndex();
    }

    public static class PaneStackStyle {

        public Drawable tabBarBackground;

        public Drawable background;

        public TextButtonStyle tabStyle;

        public float tabBarGap = 2f;

        public float maxTabWidth = Float.NaN;

        public PaneStackStyle() {
        }
    }

    public enum PaneStackEventType {
        SelectedIndexChange
    }

    public static class PaneStackEvent extends Event {

        private PaneStackEventType type;

        @Override
        public PaneStack getTarget() {
            return (PaneStack)super.getTarget();
        }

        public PaneStackEventType getType() {
            return type;
        }

        public void setType(PaneStackEventType type) {
            this.type = type;
        }

        public PaneStackEvent() {
        }

        @Override
        public void reset() {
            super.reset();
            type = null;
        }
    }
}

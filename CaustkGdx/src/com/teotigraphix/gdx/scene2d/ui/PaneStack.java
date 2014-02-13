
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.OnButtonBarListener;

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

    private Skin skin;

    private Stack stack;

    private ButtonBar buttonBar;

    private Table extrasBar;

    public Table getToolsBar() {
        if (extrasBar == null)
            extrasBar = new Table(getSkin());
        return extrasBar;
    }

    private Skin getSkin() {
        return skin;
    }

    private String buttonStyleName = "default";

    Array<Actor> pendingPanes = new Array<Actor>();

    private int buttonBarAlign;

    private Float maxButtonSize;

    public void setMaxButtonSize(Float value) {
        maxButtonSize = value;
        invalidateHierarchy();
    }

    public Float getMaxButtonSize() {
        return maxButtonSize;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex;

    private Table toolBar;

    private OnPaneStackListener listener;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        selectedIndex = value;
        if (listener != null)
            listener.onChange(selectedIndex);
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * @param skin
     * @param buttonBarAlign An {@link Align} value, top or bottom.
     */
    public PaneStack(Skin skin, int buttonBarAlign) {
        super(skin);
        this.skin = skin;
        this.buttonBarAlign = buttonBarAlign;
        initialize();
    }

    private void initialize() {
        toolBar = new Table(getSkin());

        buttonBar = new ButtonBar(getSkin(), new String[] {}, false, buttonStyleName);
        buttonBar.setMaxButtonSize(maxButtonSize);
        buttonBar.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                setSelectedIndex(index);
            }
        });

        if (extrasBar == null)
            extrasBar = new Table(getSkin());

        toolBar.add(buttonBar).fill().expand();
        toolBar.add(extrasBar).fillY();

        stack = new Stack();

        if (buttonBarAlign == Align.top) {
            //add(buttonBar).expandX().height(30f).align(Align.left);
            //add(extrasBar);
            add(toolBar).expandX().fillX().padTop(4f).padLeft(4f).padRight(4f);
            row();
            add(stack).fill().expand().pad(4f);
        } else {
            add(stack).fill().expand();
            row();
            //add(buttonBar).expandX().height(30f).align(Align.left);
            //add(extrasBar);
            add(toolBar).expandX().fillX();
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
            Array<String> labels = new Array<String>();
            for (Actor pane : pendingPanes) {
                stack.addActor(pane);
                labels.add(pane.getName());
            }
            pendingPanes.clear();
            String[] items = new String[labels.size];
            int i = 0;
            for (String label : labels) {
                items[i] = label;
                i++;
            }
            buttonBar.setItems(items);
        }

        super.layout();

        updateSelectedIndex();
    }

    public void addPane(Actor actor) {
        pendingPanes.add(actor);
    }

    public void setOnOnPaneStackListener(OnPaneStackListener l) {
        listener = l;
    }

    public interface OnPaneStackListener {
        void onChange(int index);
    }
}

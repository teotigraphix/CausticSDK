
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;

/**
 * The {@link PaneStack} holds a stack of panes and uses a selectedIndex to show
 * the top pane while hiding all other panes.
 * <p>
 * The PaneStack also carries a {@link ButtonBar} option for quick tab bar like
 * functionality.
 * 
 * @author Michael Schmalle
 */
public class PaneStack extends ControlTable {

    private Stack stack;

    private ButtonBar buttonBar;

    private Table extrasBar;

    public Table getToolsBar() {
        if (extrasBar == null)
            extrasBar = new Table(getSkin());
        return extrasBar;
    }

    private String buttonStyleName = "default";

    Array<Pane> pendingPanes = new Array<Pane>();

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

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        selectedIndex = value;
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
        this.buttonBarAlign = buttonBarAlign;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

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
            add(toolBar).expandX().fillX();
            row();
            add(stack).fill().expand();
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
            for (Pane pane : pendingPanes) {
                stack.addActor(pane);
                labels.add(pane.getLabel());
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

    public void addPane(Pane actor) {
        pendingPanes.add(actor);
    }
}

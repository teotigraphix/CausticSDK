
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.teotigraphix.gdx.groove.ui.components.ViewStackListener.ViewStackEvent;
import com.teotigraphix.gdx.groove.ui.components.ViewStackListener.ViewStackEventKind;

public class ViewStack extends UITable {

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex = 0;

    private Stack stack;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        // XXX Possibly think about a fade or slide transition
        int old = selectedIndex;
        selectedIndex = value;
        ViewStackEvent event = new ViewStackEvent(ViewStackEventKind.SelectedIndexChange,
                selectedIndex, old);
        if (fire(event)) {
            selectedIndex = old;
        }
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ViewStack(Skin skin) {
        super(skin);

        // needs access
        stack = new Stack();
    }

    public void addView(Actor actor) {
        stack.add(actor);
        actor.setVisible(false);
    }

    @Override
    protected void createChildren() {
        add(stack).expand().fill();
    }

    @Override
    public void layout() {

        for (Actor actor : stack.getChildren()) {
            actor.setVisible(false);
        }

        stack.getChildren().get(selectedIndex).setVisible(true);

        super.layout();

    }

}

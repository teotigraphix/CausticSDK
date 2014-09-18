
package com.teotigraphix.gdx.groove.ui.model;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationComponent;
import com.teotigraphix.gdx.groove.ui.behavior.MainTemplateBehavior;
import com.teotigraphix.gdx.groove.ui.components.ViewStackData;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

/**
 * Holds state for the {@link MainTemplateBehavior} ui.
 */
@Singleton
public class UIModel extends ApplicationComponent {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private UIModelState state;

    private Array<ViewStackData> views;

    private Array<ButtonBarItem> buttons;

    @Override
    protected String getPreferenceId() {
        return getApplication().getApplicationId() + "/UIModel";
    }

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // viewIndex
    //----------------------------------

    public int getViewIndex() {
        return state.getViewIndex();
    }

    /**
     * @param viewIndex
     * @see UIModelEventKind#ViewIndexChange
     */
    public void setViewIndex(int viewIndex) {
        if (state.getViewIndex() == viewIndex)
            return;
        state.setViewIndex(viewIndex);
        getEventBus().post(new UIModelEvent(UIModelEventKind.ViewIndexChange, this));
    }

    //----------------------------------
    // prefsViewIndex
    //----------------------------------

    public int getPrefsViewIndex() {
        return state.getPrefsViewIndex();
    }

    /**
     * @param viewIndex
     * @see UIModelEventKind#PrefsViewIndexChange
     */
    public void setPrefsViewIndex(int viewIndex) {
        if (state.getPrefsViewIndex() == viewIndex)
            return;
        state.setPrefsViewIndex(viewIndex);
        getEventBus().post(new UIModelEvent(UIModelEventKind.PrefsViewIndexChange, this));
    }

    //----------------------------------
    // views
    //----------------------------------

    public Array<ViewStackData> getViews() {
        return views;
    }

    public void setViews(Array<ViewStackData> views) {
        this.views = views;
    }

    //----------------------------------
    // buttons
    //----------------------------------

    public Array<ButtonBarItem> getButtons() {
        if (buttons != null)
            return buttons;

        buttons = new Array<ButtonBarItem>();
        for (ViewStackData data : views) {
            buttons.add(data.toButtonBarItem());
        }

        return buttons;
    }

    public void setButtons(Array<ButtonBarItem> buttons) {
        this.buttons = buttons;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public UIModel() {
    }

    public void restore(UIModelState state) {
        this.state = state;

        getEventBus().post(new UIModelEvent(UIModelEventKind.ViewIndexChange, this));
        getEventBus().post(new UIModelEvent(UIModelEventKind.PrefsViewIndexChange, this));
    }

    //--------------------------------------------------------------------------
    // Event
    //--------------------------------------------------------------------------

    public enum UIModelEventKind {
        ViewIndexChange, PrefsViewIndexChange
    }

    public static class UIModelEvent {

        private UIModelEventKind kind;

        private UIModel model;

        public UIModelEventKind getKind() {
            return kind;
        }

        public UIModel getModel() {
            return model;
        }

        public UIModelEvent(UIModelEventKind kind, UIModel model) {
            this.kind = kind;
            this.model = model;
        }
    }
}


package com.teotigraphix.gdx.groove.ui;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationComponent;

@Singleton
public class UIModel extends ApplicationComponent {

    private UIModelState state;

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
        return state.viewIndex;
    }

    /**
     * @param viewIndex
     * @see UIModelEventKind#ViewIndexChange
     */
    public void setViewIndex(int viewIndex) {
        if (state.viewIndex == viewIndex)
            return;
        state.viewIndex = viewIndex;
        getEventBus().post(new UIModelEvent(UIModelEventKind.ViewIndexChange, this));
    }

    public void restore(UIModelState state) {
        this.state = state;

        getEventBus().post(new UIModelEvent(UIModelEventKind.ViewIndexChange, this));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public UIModel() {
    }

    //--------------------------------------------------------------------------
    // Event
    //--------------------------------------------------------------------------

    public enum UIModelEventKind {
        ViewIndexChange
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

    //--------------------------------------------------------------------------
    // State
    //--------------------------------------------------------------------------

    public static class UIModelState {

        @Tag(0)
        private int viewIndex = -1;
    }
}

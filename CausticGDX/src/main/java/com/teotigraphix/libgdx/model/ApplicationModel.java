
package com.teotigraphix.libgdx.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CtkDebug;

@Singleton
public class ApplicationModel extends ModelBase implements IApplicationModel {

    @Override
    public String getName() {
        return "TODO Resources";//resourceBundle.getString("APP_TITLE");
    }

    //----------------------------------
    // dirty
    //----------------------------------

    private boolean dirty;

    @Override
    public final boolean isDirty() {
        return dirty;
    }

    @Override
    public final void setDirty() {
        setDirty(true);
    }

    @Override
    public final void setDirty(boolean value) {
        if (value == dirty)
            return;
        CtkDebug.model("ApplicationModel dirty: " + value);
        dirty = value;
        getDispatcher().trigger(new OnApplicationModelDirtyChanged(dirty));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationModel() {
        super();
    }

    //--------------------------------------------------------------------------
    // Public API
    //--------------------------------------------------------------------------

    @Override
    public void start() {
        CtkDebug.model("ApplicationModel.start() fires OnApplicationModelStart");
    }

    //--------------------------------------------------------------------------
    // Project :: Events
    //--------------------------------------------------------------------------

    @Override
    public void run() {
        CtkDebug.model(">>>>> ApplicationModel.run() fires OnApplicationModelRun");
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
    }
}

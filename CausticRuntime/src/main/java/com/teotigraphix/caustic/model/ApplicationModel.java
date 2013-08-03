
package com.teotigraphix.caustic.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CtkDebug;

@Singleton
public class ApplicationModel extends ModelBase implements IApplicationModel {

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
        getController().getDispatcher().trigger(new OnApplicationModelStart());
    }

    //--------------------------------------------------------------------------
    // Project :: Events
    //--------------------------------------------------------------------------

    @Override
    public void run() {
        CtkDebug.model(">>>>> ApplicationModel.run() fires OnApplicationModelRun");
        getController().getDispatcher().trigger(new OnApplicationModelRun());
    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegister() {
        // TODO Auto-generated method stub

    }

}


package com.teotigraphix.caustic.model;

import com.google.inject.Inject;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.ModelBase;
import com.teotigraphix.caustk.core.CtkDebug;

//@Singleton
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
        CtkDebug.log("ApplicationModel dirty: " + value);
        dirty = value;
        getDispatcher().trigger(new OnApplicationModelDirtyChanged(dirty));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    @Inject
    public ApplicationModel(ICaustkApplicationProvider provider) {
        super(provider);
    }

    //--------------------------------------------------------------------------
    // Public API
    //--------------------------------------------------------------------------

    @Override
    public void start() {
        CtkDebug.log("ApplicationModel.start() fires OnApplicationModelStart");
        getController().getDispatcher().trigger(new OnApplicationModelStart());
    }

    //--------------------------------------------------------------------------
    // Project :: Events
    //--------------------------------------------------------------------------

    @Override
    protected void onProjectCreate() {
    }

    @Override
    protected void onProjectLoad() {
    }

}


package com.teotigraphix.caustic.internal.controller.application;

import roboguice.activity.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.caustic.application.IApplicationModel;
import com.teotigraphix.caustic.controller.IApplicationPreferences;
import com.teotigraphix.caustic.router.IRouter;

@ContextSingleton
public class ApplicationControllerHandlers {

    private static final String TAG = "ApplicationControllerHandlers";

    private final IApplicationModel applicationModel;

    private final ITouchService touchService;

    @Inject
    IRouter controller;

    @Inject
    IApplicationPreferences applicationPreferences;

    private boolean isRestarting;

    @Inject
    public ApplicationControllerHandlers(IApplicationModel model, ITouchService touchService) {
        this.applicationModel = model;
        this.touchService = touchService;
    }

    //--------------------------------------------------------------------------
    // Android Activity Lifecycle Handlers
    //--------------------------------------------------------------------------

    void onCreateListener(@Observes OnCreateEvent event) {
        Log.d(TAG, "OnCreateEvent");
        //MainLayout layout = (MainLayout)applicationModel.getWorkspace().getActivity()
        //        .findViewById(R.id.main_layout);
        //layout.setTouchService(touchService);
    }
}


package com.teotigraphix.caustk.application;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustic.core.Dispatcher;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;

public class ModelBase {

    private ICaustkController controller;

    private IDispatcher dispatcher;

    public final ICaustkController getController() {
        return controller;
    }

    //    protected IDispatcher getDispatcher() {
    //        return controller.getDispatcher();
    //    }

    /**
     * The model's {@link IDispatcher} for local event dispatching.
     */
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    // @Inject
    public ModelBase(ICaustkApplicationProvider provider) {
        controller = provider.get().getController();
        dispatcher = new Dispatcher();

        controller.getDispatcher().register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        if (object.getKind() == ProjectManagerChangeKind.CREATE) {
                            onProjectCreate();
                        } else if (object.getKind() == ProjectManagerChangeKind.LOAD) {
                            onProjectLoad();
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE) {
                            onProjectSave();
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE_COMPLETE) {
                        }
                    }
                });
    }

    public void onRegister() {
    }

    protected void onProjectSave() {
    }

    protected void onProjectLoad() {
    }

    protected void onProjectCreate() {
    }
}

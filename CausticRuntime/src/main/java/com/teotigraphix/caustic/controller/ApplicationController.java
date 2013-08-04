
package com.teotigraphix.caustic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Named;
import javax.swing.JFileChooser;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.ICaustkMediator;
import com.teotigraphix.caustic.model.ApplicationModel;
import com.teotigraphix.caustic.model.IApplicationModel;
import com.teotigraphix.caustic.model.ICaustkModel;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;
import com.teotigraphix.caustk.project.Project;

/**
 * Mediates the {@link ApplicationModel}.
 */
@Singleton
public class ApplicationController implements IApplicationController {

    private ICaustkController controller;

    @Override
    public ICaustkController getController() {
        return controller;
    }

    private List<ICaustkMediator> mediators = new ArrayList<ICaustkMediator>();

    @Override
    public void registerMeditor(ICaustkMediator mediator) {
        if (mediators.contains(mediator)) {
            CtkDebug.warn("ApplicationController already contains " + mediator);
            return;
        }
        mediators.add(mediator);
    }

    @Override
    public void registerMediatorObservers() {
        CtkDebug.log("ApplicationController Register Mediator Observers");
        for (ICaustkMediator mediator : mediators) {
            CtkDebug.log("   Register; " + mediator.getClass().getSimpleName());
            mediator.onRegisterObservers();
        }
    }

    @Override
    public void registerMeditors() {
        CtkDebug.log("ApplicationController Register Mediators");
        for (ICaustkMediator mediator : mediators) {
            CtkDebug.log("   Register; " + mediator.getClass().getSimpleName());
            mediator.onRegister();
        }
    }

    private List<ICaustkModel> models = new ArrayList<ICaustkModel>();

    @Override
    public void registerModel(ICaustkModel model) {
        if (models.contains(model)) {
            CtkDebug.warn("ApplicationController already contains " + model);
            return;
        }
        models.add(model);
    }

    @Override
    public void registerModels() {
        CtkDebug.log("ApplicationController Register Models");
        for (ICaustkModel model : models) {
            CtkDebug.log("   Register; " + model.getClass().getSimpleName());
            model.onRegister();
        }
    }

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    @Named("resources")
    ResourceBundle resourceBundle;

    @Inject
    public ApplicationController(ICaustkApplicationProvider provider) {
        controller = provider.get().getController();
        controller.getDispatcher().register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        if (object.getKind() == ProjectManagerChangeKind.CREATE) {
                            //onProjectCreate();
                        } else if (object.getKind() == ProjectManagerChangeKind.LOAD) {
                            //onProjectLoad();
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE) {
                            onProjectSave();
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE_COMPLETE) {
                        }
                    }
                });
    }

    @Override
    public void start() throws IOException {
        File causticStorage = new JFileChooser().getFileSystemView().getDefaultDirectory();
        File applicationRoot = new File(causticStorage, resourceBundle.getString("APP_DIRECTORY"));

        CtkDebug.log("> set caustic storage: " + causticStorage.getAbsolutePath());
        CtkDebug.log("> set application root: " + applicationRoot.getAbsolutePath());
        getController().getConfiguration().setCausticStorage(causticStorage);
        getController().getConfiguration().setApplicationRoot(applicationRoot);

        getController().getApplication().initialize();

        // from event calls appModel.start()
        getController().getApplication().start();

        applicationModel.start();

        IProjectManager projectManager = getController().getProjectManager();
        String path = projectManager.getSessionPreferences().getString("lastProject");

        Project project = null;
        if (path == null) {
            project = projectManager.create(new File("UntitledProject.ctk"));
            projectManager.save();
            // adding a LOAD event here to keep consistent with all startup.
            // whether a project is created or loaded on start, mediators will always
            // get a load event at start.
            getController().getDispatcher().trigger(
                    new OnProjectManagerChange(project, ProjectManagerChangeKind.LOAD));
        } else {
            project = projectManager.load(new File(path));
        }
    }

    /**
     * @see OnApplicationControllerShow
     */
    @Override
    public void show() {
        CtkDebug.log("ApplicationController.show()");
        applicationModel.run();
        for (ICaustkModel model : models) {
            CtkDebug.log("    Show " + model.getClass().getSimpleName());
            model.onShow();
        }
    }

    protected void onProjectSave() {
        applicationModel.setDirty(false);
    }

}

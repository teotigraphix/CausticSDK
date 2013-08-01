
package com.teotigraphix.caustic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.inject.Named;
import javax.swing.JFileChooser;

import com.google.inject.Inject;
import com.teotigraphix.caustic.model.ApplicationModel;
import com.teotigraphix.caustic.model.IApplicationModel;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.application.core.MediatorBase;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;
import com.teotigraphix.caustk.project.Project;

/**
 * Mediates the {@link ApplicationModel}.
 */
//@Singleton
public class ApplicationController extends MediatorBase implements IApplicationController {

    private IApplicationModel applicationModel;

    @Inject
    @Named("resources")
    ResourceBundle resourceBundle;

    @Inject
    public ApplicationController(ICaustkApplicationProvider provider,
            IApplicationModel applicationModel) {
        super(provider);
        this.applicationModel = applicationModel;
    }

    /**
     * Starts the {@link ICaustkApplication}.
     * 
     * @see ICaustkConfiguration#setApplicationRoot(File)
     * @see ICaustkConfiguration#setCausticStorage(File)
     * @see ICaustkApplication#initialize()
     * @see IProjectManager#initialize(File)
     * @see ICaustkApplication#start()
     * @see IProjectManager#load(File)
     * @throws IOException
     */
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

    @Override
    protected void onProjectSave() {
        applicationModel.setDirty(false);
    }

}

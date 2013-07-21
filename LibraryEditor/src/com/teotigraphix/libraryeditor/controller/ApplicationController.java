
package com.teotigraphix.libraryeditor.controller;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.application.core.MediatorBase;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.libraryeditor.model.ApplicationModel;

@Singleton
public class ApplicationController extends MediatorBase {

    @SuppressWarnings("unused")
    private ApplicationModel applicationModel;

    @Inject
    public ApplicationController(ICaustkApplicationProvider provider,
            ApplicationModel applicationModel) {
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
        File applicationRoot = new File(causticStorage, "LibraryEditor");

        getController().getConfiguration().setCausticStorage(causticStorage);
        getController().getConfiguration().setApplicationRoot(applicationRoot);

        getController().getApplication().initialize();

        IProjectManager projectManager = getController().getProjectManager();
        projectManager.initialize(applicationRoot);

        // from event calls appModel.start()
        getController().getApplication().start();

        String path = projectManager.getSessionPreferences().getString("lastProject");

        if (path == null) {
            projectManager.create(new File("UntitledProject.ctk"));
            projectManager.save();
        } else {
            projectManager.load(new File(path));
        }
    }
}

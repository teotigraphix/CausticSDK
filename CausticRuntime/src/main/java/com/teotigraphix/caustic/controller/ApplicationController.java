
package com.teotigraphix.caustic.controller;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustic.model.ApplicationModel;
import com.teotigraphix.caustic.model.IApplicationModel;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.ICaustkApplication.OnApplicationStart;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.application.core.MediatorBase;
import com.teotigraphix.caustk.project.IProjectManager;

/**
 * Mediates the {@link ApplicationModel}.
 */
//@Singleton
public class ApplicationController extends MediatorBase implements IApplicationController {

    private IApplicationModel applicationModel;

    @Inject
    public ApplicationController(ICaustkApplicationProvider provider,
            IApplicationModel applicationModel) {
        super(provider);
        this.applicationModel = applicationModel;

        getController().getDispatcher().register(OnApplicationStart.class,
                new EventObserver<OnApplicationStart>() {
                    @Override
                    public void trigger(OnApplicationStart object) {
                        onApplicationStartHandler();
                    }
                });
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

    protected void onApplicationStartHandler() {
        applicationModel.start();
    }

    @Override
    protected void onProjectSave() {
        applicationModel.setDirty(false);
    }

}

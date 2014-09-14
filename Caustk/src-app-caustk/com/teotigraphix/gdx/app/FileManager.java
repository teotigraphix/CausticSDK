
package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CaustkProject;
import com.teotigraphix.caustk.utils.RuntimeUtils;

@Singleton
public class FileManager implements IFileManager {

    private static final String PROJECTS = "projects";

    public static final String LAST_PROJECT_PATH = "last-project-path";

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    IApplicationStates applicationStates;

    private File applicationDirectory;

    public final File getProjectsDirectory() {
        return new File(applicationDirectory, PROJECTS);
    }

    public FileManager() {
    }

    public void startup() {
        // create the application projects folder if not exists
        applicationDirectory = RuntimeUtils.getApplicationDirectory();
        File projectsDirectory = getProjectsDirectory();
        if (!projectsDirectory.exists())
            projectsDirectory.mkdirs();

    }

    @Override
    public File getStartupProjectFile() {
        String path = applicationModel.getPreferences().getString(LAST_PROJECT_PATH, null);
        return new File(path);
    }

    @Override
    public void setStartupProject(CaustkProject project) {
        applicationModel.getPreferences().putString(LAST_PROJECT_PATH,
                project.getFile().getAbsolutePath());
    }

    @Override
    public CaustkProject createOrLoadStartupProject() throws IOException {
        CaustkProject project = null;
        File startupFile = getStartupProjectFile();
        if (startupFile.exists()) {
            // load existing

            project = applicationStates.readProject(startupFile);
        } else {
            System.err.println("Could not load project file " + startupFile.getAbsolutePath());

            // create new Untitled Project
            String projectName = getNextProjectName();
            File projectBaseDirectory = new File(getProjectsDirectory(), projectName);
            // create the container directory for the project
            if (!projectBaseDirectory.exists())
                projectBaseDirectory.mkdirs();

            startupFile = new File(projectBaseDirectory, projectName + ".prj");

            project = applicationStates.createDefaultProject(projectName, startupFile);
        }

        //        // C:\Users\Teoti\Documents\Tones\projects\Untitled.prj
        //        if (!projectLocation.exists()) {
        //            project = createDefaultProject(projectName, projectLocation.getAbsolutePath());
        //        } else {
        //            project = loadProject(projectLocation);
        //        }
        return project;
    }

    private String getNextProjectName() {
        return "Untitled";
    }
}


package com.teotigraphix.caustk.gdx.app.api;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.Gdx;
import com.teotigraphix.caustk.gdx.app.AbstractProjectModelAPI;
import com.teotigraphix.caustk.gdx.app.IApplication.ApplicationExportType;
import com.teotigraphix.caustk.gdx.app.Project;
import com.teotigraphix.caustk.gdx.app.ProjectModel;
import com.teotigraphix.caustk.gdx.app.ProjectState;

public class ProjectAPI extends AbstractProjectModelAPI {

    private static final String TAG = "ProjectAPI";

    private Project project;

    //----------------------------------
    // project
    //----------------------------------

    @SuppressWarnings("unchecked")
    public <T extends Project> T getProject() {
        return (T)project;
    }

    @SuppressWarnings("unchecked")
    public <T extends Project> void setProject(T project) throws IOException {
        T oldProject = (T)this.project;
        if (oldProject != null) {
            closeProject(oldProject);
        }

        getLogger().log(TAG, "setProject() - " + project.getFile());
        this.project = project;

        if (!project.isCreated()) {
            createProject(project);
        } else {
            loadProject(project);
        }
    }

    public ProjectAPI(ProjectModel projectModel) {
        super(projectModel);
    }

    @Override
    public void restore(ProjectState state) {

    }

    public void newProject(File projectLocation) throws IOException {
        Project project = getFileManager().createProject(projectLocation);
        getProjectModel().getProjectAPI().setProject(project);
    }

    public void loadProject(File projectFile) throws IOException {
        Project project = getFileManager().loadProject(projectFile);
        getProjectModel().getProjectAPI().setProject(project);
    }

    public File saveProjectAs(String projectName) throws IOException {
        return saveProjectAs(new File(getFileManager().getProjectsDirectory(), projectName));
    }

    /**
     * @param projectLocation The project directory, getName() returns the
     *            project name to copy.
     * @throws java.io.IOException
     */
    public File saveProjectAs(File projectLocation) throws IOException {
        File srcDir = getProjectModel().getProjectDirectory();
        File destDir = projectLocation;
        FileUtils.copyDirectory(srcDir, destDir);

        // load the copied project discreetly
        final File oldProjectFile = new File(projectLocation, srcDir.getName() + ".prj");
        File newProjectFile = new File(projectLocation, destDir.getName() + ".prj");

        Project newProject = getFileManager().readProject(oldProjectFile);
        newProject.setRack(getRack()); // needed for deserialization
        newProject.rename(newProjectFile);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // XXX This is NOTE DELETING
                FileUtils.deleteQuietly(oldProjectFile);
            }
        });

        loadProject(newProject.getFile());

        return newProjectFile;
    }

    public File exportProject(File file, ApplicationExportType exportType) throws IOException {
        File srcDir = getProjectModel().getProjectDirectory();
        File exportedFile = new File(srcDir, "exported/");
        exportedFile.mkdirs();
        // NO .caustic ext
        // XXX Can't have spaces, must replace all spaces with '-'
        exportedFile = new File(exportedFile, file.getName().replaceAll(" ", "-") + ".caustic");

        File savedFile = getRackNode().saveSongAs(exportedFile);
        return savedFile;
    }

    public void save() throws IOException {
        saveProject(getProjectModel().getProjectAPI().getProject());
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void createProject(Project project) throws IOException {
        getLogger().log(TAG, "createProject()");
        getApplicationStates().onProjectCreate(project);
        getFileManager().setStartupProject(project);
        project.save();
    }

    private void loadProject(Project project) {
        getLogger().log(TAG, "loadProject()");
        getFileManager().setStartupProject(project);
        getApplicationStates().onProjectLoad(project);
    }

    private void closeProject(Project project) {
        getLogger().log(TAG, "closeProject()");
        getApplicationStates().onProjectClose(project);
    }

    private void saveProject(Project project) throws IOException {
        getLogger().log(TAG, "saveProject()");
        getApplicationStates().onProjectSave(project);
        project.save();
    }

}

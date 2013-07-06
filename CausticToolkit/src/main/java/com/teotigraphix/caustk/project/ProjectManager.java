
package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;

/**
 * The project manager manages the single project loaded for an application.
 * <p>
 * The manager will have a root directory passed to it when it is created. All
 * project related files are stored within this directory.
 */
public class ProjectManager implements IProjectManager {

    private ICaustkController controller;

    private File projectDirectory;

    private File sessionPreferencesFile;

    //----------------------------------
    // sessionPreferences
    //----------------------------------

    private SessionPreferences sessionPreferences;

    @Override
    public SessionPreferences getSessionPreferences() {
        return sessionPreferences;
    }

    //----------------------------------
    // applicationRoot
    //----------------------------------

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     */
    private File applicationRoot;

    @Override
    public File getApplicationRoot() {
        return applicationRoot;
    }

    //----------------------------------
    // project
    //----------------------------------

    private Project project;

    @Override
    public Project getProject() {
        return project;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectManager(ICaustkController controller, File applicationRoot) {
        this.controller = controller;
        this.applicationRoot = applicationRoot;

        projectDirectory = new File(applicationRoot, "projects");
        sessionPreferencesFile = new File(applicationRoot, ".settings");

        if (!sessionPreferencesFile.exists()) {
            try {
                sessionPreferencesFile.createNewFile();
                sessionPreferences = new SessionPreferences();
                saveProjectPreferences();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (sessionPreferencesFile.exists()) {
                sessionPreferences = controller.getSerializeService().fromFile(
                        sessionPreferencesFile, SessionPreferences.class);
            }
        }
    }

    //--------------------------------------------------------------------------
    // IProjectManager API
    //--------------------------------------------------------------------------

    @Override
    public boolean isProject(File file) {
        if (file.isAbsolute())
            return file.exists();
        return toProjectFile(file).exists();
    }

    @Override
    public void save() throws IOException {
        sessionPreferences.put("lastProject", project.getFile().getPath());

        controller.getDispatcher().trigger(new OnProjectManagerSave(project));

        String data = controller.getSerializeService().toString(project);
        FileUtils.writeStringToFile(project.getFile(), data);
        String debug = project.getFile().getAbsolutePath().replace(".clp", "_d.clp");
        FileUtils.writeStringToFile(new File(debug),
                controller.getSerializeService().toString(project));

        saveProjectPreferences();

        controller.getDispatcher().trigger(new OnProjectManagerSaveComplete(project));
    }

    private void saveProjectPreferences() throws IOException {
        String data = controller.getSerializeService().toString(sessionPreferences);
        FileUtils.writeStringToFile(sessionPreferencesFile, data);
    }

    @Override
    public Project load(File file) throws IOException {
        file = toProjectFile(file);
        if (!file.exists())
            throw new IOException("Project file does not exist");

        project = controller.getSerializeService().fromFile(file, Project.class);
        controller.getDispatcher().trigger(new OnProjectManagerLoad(project));
        return project;
    }

    @Override
    public Project create(File projectFile) throws IOException {
        project = new Project();
        project.setFile(new File(projectDirectory, projectFile.getPath()));
        project.setInfo(createInfo());
        controller.getDispatcher().trigger(new OnProjectManagerCreate(project));
        return project;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    private ProjectInfo createInfo() {
        ProjectInfo info = new ProjectInfo();
        info.setName("Untitled Project");
        info.setAuthor("Untitled Author");
        info.setCreated(new Date());
        info.setModified(new Date());
        info.setDescription("A new project");
        return info;
    }

    private File toProjectFile(File file) {
        if (file.isAbsolute())
            return file;
        return new File(projectDirectory, file.getPath());
    }
}

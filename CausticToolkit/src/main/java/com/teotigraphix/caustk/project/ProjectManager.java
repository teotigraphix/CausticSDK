
package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * The project manager manages the single project loaded for an application.
 * <p>
 * The manager will have a root directory passed to it when it is created. All
 * project related files are stored within this directory.
 */
public class ProjectManager implements IProjectManager {

    private XStream projectStream;

    @SuppressWarnings("unused")
    private ICaustkController controller;

    private Project project;

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     */
    private File applicationRoot;

    private File projectDirectory;

    public File getApplicationRoot() {
        return applicationRoot;
    }

    /**
     * Returns the current {@link Project} instantiated by
     * {@link #create(File, File)} or {@link #load(File)}.
     */
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

        projectStream = new XStream(new JettisonMappedXmlDriver());
        projectStream.setMode(XStream.NO_REFERENCES);
    }

    /**
     * Saves a {@link Project} to disk using the project's file location.
     * 
     * @throws IOException
     */
    public void save() throws IOException {
        String data = projectStream.toXML(project);
        FileUtils.writeStringToFile(project.getFile(), data);
    }

    /**
     * Loads a project from disk using the <code>.ctk</code> project format.
     * <p>
     * The state is the same as if this {@link Project} was loaded from the
     * {@link #load(File)} method.
     * 
     * @param file The project <code>.ctk</code> file.
     * @return A fully loaded <code>.ctk</code> project state.
     * @throws IOException
     */
    public Project load(File file) throws IOException {
        if (!file.exists())
            throw new IOException("Project file does not exist");

        projectStream = new XStream(new JettisonMappedXmlDriver());
        projectStream.setMode(XStream.NO_REFERENCES);
        projectStream.alias("project", Project.class);

        project = (Project)projectStream.fromXML(file);
        
        return project;
    }

    public Project create(File projectFile) throws IOException {
        project = new Project();
        project.setFile(new File(projectDirectory, projectFile.getPath()));
        project.setInfo(createInfo());
        save();
        return project;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    private ProjectInfo createInfo() {
        ProjectInfo info = new ProjectInfo();
        info.setName("Untitle Project");
        info.setAuthor("Untitled Author");
        info.setCreated(new Date());
        info.setModified(new Date());
        info.setDescription("A new project");
        //        info.setDescription("Copied and initialized from the " + songFile.getAbsolutePath()
        //                + " file.");
        return info;
    }

}

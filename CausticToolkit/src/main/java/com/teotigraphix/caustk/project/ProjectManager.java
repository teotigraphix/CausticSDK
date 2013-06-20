
package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustic.desktop.RuntimeUtils;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.system.bank.CausticFileMemoryDescriptor;
import com.teotigraphix.caustk.system.bank.MemoryDescriptor;
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

    private ICaustkController controller;

    private Project project;

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     */
    private File applicationRoot;

    private File projectDirectory;

    @Override
    public File getApplicationRoot() {
        return applicationRoot;
    }

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

    @Override
    public boolean isProject(File file) {
        return new File(projectDirectory, file.getPath()).exists();
    }

    @Override
    public void save() throws IOException {
        String data = projectStream.toXML(project);
        FileUtils.writeStringToFile(project.getFile(), data);
    }

    @Override
    public Project load(File file) throws IOException {
        file = new File(projectDirectory, file.getPath());
        if (!file.exists())
            throw new IOException("Project file does not exist");

        projectStream = new XStream(new JettisonMappedXmlDriver());
        projectStream.setMode(XStream.NO_REFERENCES);
        projectStream.alias("project", Project.class);

        project = (Project)projectStream.fromXML(file);
        return project;
    }

    @Override
    public Project create(File projectFile) throws IOException {
        project = new Project();
        project.setFile(new File(projectDirectory, projectFile.getPath()));
        project.setInfo(createInfo());
        return project;
    }

    public void add(File songFile) {
        // TODO Auto-generated method stub

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

}

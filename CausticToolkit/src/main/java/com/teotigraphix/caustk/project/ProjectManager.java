
package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustic.desktop.RuntimeUtils;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.RackMessage;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.system.bank.CausticFileMemoryDescriptor;
import com.teotigraphix.caustk.system.bank.MemoryDescriptor;
import com.teotigraphix.caustk.system.bank.MemoryLoader;
import com.teotigraphix.caustk.system.bank.MemoryLoader.OnPatchCopy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * The project manager manages the single project loaded for an application.
 * <p>
 * The manager will have a root directory passed to it when it is created. All
 * project related files are stored within this directory.
 */
public class ProjectManager implements IProjectManager {

    private MemoryLoader memoryLoader;

    private XStream projectStream;

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

        memoryLoader = new MemoryLoader(controller);
        memoryLoader.setOnPatchCopyListener(new OnPatchCopy() {
            @Override
            public void onPatchCopy(IMachine machine) {
                try {
                    savePatch(machine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    /**
     * Creates a {@link Project} from a <code>.caustic</code> file.
     * <p>
     * This method will automatically save the {@link Project} to disk using the
     * projectFile location. The <code>.ctk</code> file is guaranteed to be on
     * disk before this method returns.
     * 
     * @param songFile
     * @param projectFile the relative path and name of the <code>.ctk</code>
     *            file without the <code>project</code> sub directory. This is
     *            the file path located within the projects app directory.
     * @return
     * @throws IOException
     */
    public Project create(File songFile, File projectFile) throws IOException {
        if (!songFile.exists())
            throw new IOException(".caustic file does not exist");

        project = create(projectFile);
        project.getInfo().setName(songFile.getName());
        project.getInfo().setDescription(
                "Copied and initialized from the " + songFile.getAbsolutePath() + " file.");

        MemoryDescriptor descriptor = createMemoryDescriptor(songFile);
        descriptor.setIndex(0);
        // load all patterns, one phrase per pattern
        descriptor.fullLoad = true;

        // passing null for the tone descriptors forces the loader to
        // create them when it first loads the song file, will populate
        // the patch, phrase and pattern items
        memoryLoader.load(descriptor, null);

        // clear the core sound generator (CausticEngine)
        RackMessage.BLANKRACK.send(controller);

        loadProject(project, descriptor);

        save();

        return project;
    }

    public void add(File songFile) {
        // TODO Auto-generated method stub

    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    private void loadProject(Project project, MemoryDescriptor descriptor) {

    }

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

    private MemoryDescriptor createMemoryDescriptor(File file) {
        return new CausticFileMemoryDescriptor("Preset", file);
    }

    //--------------------------------------------------------------------------

    protected void savePatch(IMachine machine) throws IOException {
        String patchId = machine.getId();
        machine.savePreset(patchId);

        // XXX Copy and delete from the presets directory to the project
        // root dir

        File presetFile = RuntimeUtils.getCausticPresetsFile(machine.getType().toString(), patchId);
        if (!presetFile.exists())
            throw new IOException("Preset file does not exist");

        File projectRoot = project.getFile().getParentFile();

        File destFile = new File(projectRoot, "presets/" + presetFile.getName());
        FileUtils.copyFile(presetFile, destFile);
        presetFile.delete();
    }

}

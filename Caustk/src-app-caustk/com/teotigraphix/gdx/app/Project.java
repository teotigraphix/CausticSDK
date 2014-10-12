
package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The project holds the single {@link RackNode} in the applications current
 * session.
 */
public abstract class Project {

    private ICaustkRack rack;

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private ProjectProperties properties;

    @Tag(5)
    private UUID id;

    @Tag(6)
    private String nativePath; // XXX This is dangerous, do you really want to hold onto this?

    @Tag(7)
    private String name;

    @Tag(10)
    private RackNode rackNode;

    @Tag(11)
    private byte[] rackBytes;

    @Tag(25)
    private ProjectState state;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    public ProjectProperties getProperties() {
        return properties;
    }

    //----------------------------------
    // rack
    //----------------------------------

    public ICaustkRack getRack() {
        return rack;
    }

    public void setRack(ICaustkRack rack) {
        this.rack = rack;
    }

    //----------------------------------
    // id
    //----------------------------------

    public UUID getId() {
        return id;
    }

    //----------------------------------
    // nativePath
    //----------------------------------

    public String getNativePath() {
        return nativePath;
    }

    //----------------------------------
    // file
    //----------------------------------

    /**
     * Returns the .prj file location.
     */
    public File getFile() {
        return new File(nativePath);
    }

    public File getDirectory() {
        return getFile().getParentFile();
    }

    public boolean exists() {
        return getFile().exists();
    }

    /**
     * Returns a directory or file from within the project's root directory.
     * <p>
     * Does not create a directory.
     * 
     * @param relativePath The resource path.
     */
    public File findResource(String relativePath) {
        return new File(getDirectory(), relativePath);
    }

    /**
     * Returns a directory or file from within the project's root directory.
     * <p>
     * Create the file or directory if not exists.
     * 
     * @param relativePath The resource path.
     * @throws IOException
     */
    public File getResource(String relativePath) throws IOException {
        File resource = findResource(relativePath);
        if (!resource.exists())
            resource.mkdirs();
        return resource;
    }

    //----------------------------------
    // name
    //----------------------------------

    public String getName() {
        return name;
    }

    //----------------------------------
    // rackNode
    //----------------------------------

    public RackNode getRackNode() {
        return rackNode;
    }

    //----------------------------------
    // rackNode
    //----------------------------------

    public byte[] getRackBytes() {
        return rackBytes;
    }

    /**
     * Returns whether the {@link #getRackBytes()} is null, meaning the project
     * was just created (has no bytes) or existing (has bytes).
     */
    public boolean isCreated() {
        return rackBytes != null;
    }

    //----------------------------------
    // state
    //----------------------------------

    public ProjectState getState() {
        return state;
    }

    protected void setState(ProjectState state) {
        this.state = state;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    public Project() {
    }

    public Project(ICaustkRack rack, File nativeLocation, String name) {
        this(rack, nativeLocation.getAbsolutePath(), name);
    }

    public Project(ICaustkRack rack, String nativePath, String name) {
        this.rack = rack;
        this.nativePath = nativePath;
        this.name = name;
        id = UUID.randomUUID();
        this.rackNode = rack.create();
        this.properties = new ProjectProperties();
    }

    /**
     * @param projectFile The prj file.
     * @throws IOException Could not save
     */
    public void rename(File projectFile) throws IOException {
        this.id = UUID.randomUUID();
        this.name = FilenameUtils.getBaseName(projectFile.getName());
        this.nativePath = projectFile.getAbsolutePath();
        rack.getSerializer().serialize(projectFile, this);
    }

    public void save() throws IOException {
        flush();
        rack.getSerializer().serialize(getFile(), this);
    }

    public void flush() throws IOException {
        File tempDirectory = RuntimeUtils.getApplicationTempDirectory();
        File racksDirectory = new File(tempDirectory, "racks");
        File tempFile = new File(racksDirectory, id.toString() + ".caustic");
        if (!racksDirectory.exists())
            racksDirectory.mkdir();

        rackNode.saveSongAs(tempFile);

        if (!tempFile.exists())
            throw new IOException(".caustic file was not saved in .temp for project save");

        rackBytes = FileUtils.readFileToByteArray(tempFile);

        FileUtils.deleteQuietly(tempFile);
    }

    /**
     * Called before {@link #onCreate()} or {@link #onLoad()}.
     * 
     * @see ApplicationState#onProjectCreate(Project)
     * @see ApplicationState#onProjectLoad(Project)
     */
    public abstract void onInitialize();

    /**
     * Called when a new project is created.
     * 
     * @see ApplicationState#onProjectCreate(Project)
     */
    public abstract void onCreate();

    /**
     * Called when a project has been de-serialized from a binary file.
     * 
     * @see ApplicationState#onProjectLoad(Project)
     */
    public abstract void onLoad();

    /**
     * Called when the project is saved.
     * 
     * @see ApplicationState#onProjectSave(Project)
     */
    public abstract void onSave();

    /**
     * Called when a project is closed before another project is either created
     * or loaded.
     * 
     * @see ApplicationState#onProjectClose(Project)
     */
    public abstract void onClose();

}

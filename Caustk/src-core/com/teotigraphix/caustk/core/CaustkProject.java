
package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.gdx.groove.ui.model.UIModelState;

/**
 * The project holds the single {@link RackNode} in the applications current
 * session.
 */
public abstract class CaustkProject {

    private ICaustkRack rack;

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private String nativePath; // XXX This is dangerous, do you really want to hold onto this?

    @Tag(2)
    private String name;

    @Tag(10)
    private RackNode rackNode;

    @Tag(11)
    private byte[] rackBytes;

    @Tag(25)
    private UIModelState uiState;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

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
    // uiState
    //----------------------------------

    public UIModelState getUiState() {
        return uiState;
    }

    protected void setUiState(UIModelState uiState) {
        this.uiState = uiState;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    public CaustkProject() {
    }

    public CaustkProject(ICaustkRack rack, File nativeLocation, String name) {
        this(rack, nativeLocation.getAbsolutePath(), name);
    }

    public CaustkProject(ICaustkRack rack, String nativePath, String name) {
        this.rack = rack;
        this.nativePath = nativePath;
        this.name = name;
        id = UUID.randomUUID();
        this.rackNode = rack.create();
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
     */
    public abstract void onInitialize();

    /**
     * Called when a new project is created.
     */
    public abstract void onCreate();

    /**
     * Called when a project has been deserialized from a binary file.
     */
    public abstract void onLoad();

    /**
     * Called when the project is saved.
     */
    public abstract void onSave();

    /**
     * Called when a project is closed before another project is either created
     * or loaded.
     */
    public abstract void onClose();

}

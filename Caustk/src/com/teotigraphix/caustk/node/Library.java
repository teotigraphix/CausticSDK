////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node;

/**
 * A {@link Library} is a self contained directory that holds various serialized
 * {@link ICaustkNode} types.
 * <p>
 * All library node path's are saved relative to the library's root directory.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class Library extends NodeBase {

    @Override
    protected void createComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void destroyComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void restoreComponents() {
        // TODO Auto-generated method stub

    }

    //    private static final String MANIFEST = ".library";
    //
    //    private static final String LIBRARIES = "Libraries";
    //
    //    @SuppressWarnings("unused")
    //    private transient CaustkFactory factory;
    //
    //    private transient LibraryPathResolver libraryPathResolver;
    //
    //    protected LibraryPathResolver getPathResolver() {
    //        if (libraryPathResolver == null)
    //            libraryPathResolver = new LibraryPathResolver(this);
    //        return libraryPathResolver;
    //    }
    //
    //    /**
    //     * used with {@link CaustkFactory#loadLibrary(String)}.
    //     * 
    //     * @param factory
    //     */
    //    void setFactory(CaustkFactory factory) {
    //        this.factory = factory;
    //    }
    //
    //    //--------------------------------------------------------------------------
    //    // Serialized API
    //    //--------------------------------------------------------------------------
    //
    //    private Map<NodeType, Map<UUID, NodeInfo>> map = new HashMap<NodeType, Map<UUID, NodeInfo>>();
    //
    //    private File directory;
    //
    //    //--------------------------------------------------------------------------
    //    // Public API :: Properties
    //    //--------------------------------------------------------------------------
    //
    //    //----------------------------------
    //    // map
    //    //----------------------------------
    //
    //    Map<NodeType, Map<UUID, NodeInfo>> getMap() {
    //        return map;
    //    }
    //
    //    protected final Map<UUID, NodeInfo> getTypeMap(NodeInfo info) {
    //        return map.get(info.getType());
    //    }
    //
    //    //----------------------------------
    //    // name
    //    //----------------------------------
    //
    //    /**
    //     * Returns the name of the library and is also used as the directory name of
    //     * the library held within the <code>/storageRoot/AppName/libraries</code>
    //     * directory.
    //     */
    //    public final String getName() {
    //        return getInfo().getName();
    //    }
    //
    //    /**
    //     * Returns the <code>/storageRoot/AppName/Libraries</code> directory.
    //     */
    //    final File getLibrariesDirectory() {
    //        if (directory != null)
    //            return getDirectory().getParentFile();
    //        return RuntimeUtils.getApplicationDirectory(LIBRARIES);
    //    }
    //
    //    /**
    //     * Returns the <code>/storageRoot/AppName/Libraries/[library_name]</code>
    //     * directory.
    //     */
    //    public final File getDirectory() {
    //        if (directory != null) {
    //            if (!directory.isAbsolute())
    //                return RuntimeUtils.getApplicationDirectory(directory.getPath());
    //            else
    //                return directory;
    //        }
    //        return new File(getLibrariesDirectory(), getName());
    //    }
    //
    //    protected void setDirectory(File directory) {
    //        this.directory = directory;
    //    }
    //
    //    /**
    //     * Returns the
    //     * <code>/storageRoot/AppName/Libraries/[library_name]/.library</code> file.
    //     */
    //    public final File getManifestFile() {
    //        return new File(getDirectory(), MANIFEST);
    //    }
    //
    //    //--------------------------------------------------------------------------
    //    // Constructors
    //    //--------------------------------------------------------------------------
    //
    //    /**
    //     * Serialization
    //     */
    //    Library() {
    //    }
    //
    //    Library(NodeInfo info, CaustkFactory factory) {
    //        setInfo(info);
    //        this.factory = factory;
    //    }
    //
    //    Library(NodeInfo info, CaustkFactory factory, File directory) {
    //        setInfo(info);
    //        this.factory = factory;
    //        this.directory = directory;
    //    }
    //
    //    //--------------------------------------------------------------------------
    //    // Public API :: Methods
    //    //--------------------------------------------------------------------------
    //
    //    /**
    //     * Whether this library exists on disk.
    //     */
    //    public boolean exists() {
    //        return getDirectory().exists();
    //    }
    //
    //    /**
    //     * Returns whether the library contains the node by {@link UUID} in the node
    //     * type map.
    //     * 
    //     * @param node The library node.
    //     */
    //    public final boolean contains(ICaustkNode node) {
    //        Map<UUID, NodeInfo> infos = getTypeMap(node.getInfo());
    //        if (infos == null)
    //            return false;
    //        return infos.containsKey(node.getInfo().getId());
    //    }
    //
    //    /**
    //     * Returns a {@link NodeInfo} if found in the library's map.
    //     * 
    //     * @param id The {@link UUID} to search for.
    //     */
    //    public NodeInfo get(UUID id) {
    //        for (Map<UUID, NodeInfo> list : map.values()) {
    //            for (NodeInfo info : list.values()) {
    //                if (info.getId().equals(id))
    //                    return info;
    //            }
    //        }
    //        return null;
    //    }
    //
    //    /**
    //     * Adds a node to the library.
    //     * <p>
    //     * Will save the associated node serialized to disk.
    //     * <p>
    //     * The node passed will be deep copied.
    //     * 
    //     * @param node The node to add and serialize to the library on disk.
    //     * @throws IOException
    //     */
    //    public final boolean add(ICaustkNode node) throws IOException {
    //        if (contains(node))
    //            return false;
    //        definitionAdded(node);
    //        return true;
    //    }
    //
    //    /**
    //     * Removes a file from the library and uses the absolute location to remove
    //     * the item from the library manifest.
    //     * 
    //     * @param file
    //     * @throws IOException
    //     * @throws IllegalArgumentException must be file, no directory
    //     */
    //    public final boolean remove(File file) throws IOException {
    //        if (file.isDirectory())
    //            throw new IllegalArgumentException("remove() must take a File");
    //        NodeInfo nodeInfo = get(file);
    //        boolean removed = remove(nodeInfo);
    //        if (removed) {
    //            save();
    //        }
    //        return removed;
    //    }
    //
    //    /**
    //     * Remove a node from the library by {@link UUID}.
    //     * <p>
    //     * The {@link Library} holds copies(templates) of nodes and at no time is a
    //     * node that is found in the app, contained in the library. When a
    //     * definition is added to the library it's copied into it.
    //     * <p>
    //     * Will delete the associated serialized file on disk corresponding to the
    //     * node.
    //     * 
    //     * @param node The node whose {@link NodeInfo#getId()} is used to remove the
    //     *            "Same" node ref from the library.
    //     * @return Whether the operation was successful
    //     * @throws IOException
    //     */
    //    public final boolean remove(ICaustkNode node) throws IOException {
    //        if (!contains(node))
    //            return false;
    //
    //        Map<UUID, NodeInfo> typeMap = getTypeMap(node.getInfo());
    //        NodeInfo removed = typeMap.remove(node.getInfo().getId());
    //        if (removed == null)
    //            throw new IllegalStateException("Failed to remove node");
    //
    //        definitionRemoved(removed);
    //        return true;
    //    }
    //
    //    private boolean remove(NodeInfo nodeInfo) throws IOException {
    //        Map<UUID, NodeInfo> typeMap = getTypeMap(nodeInfo);
    //        NodeInfo removed = typeMap.remove(nodeInfo.getId());
    //        if (removed == null)
    //            throw new IllegalStateException("Failed to remove node");
    //
    //        definitionRemoved(removed);
    //        return true;
    //    }
    //
    //    public NodeInfo get(File file) {
    //        for (Map<UUID, NodeInfo> types : map.values()) {
    //            for (NodeInfo info : types.values()) {
    //                File other = resolveAbsoluteArchive(info);
    //                if (other.compareTo(file) == 0)
    //                    return info;
    //            }
    //        }
    //        return null;
    //    }
    //
    //    public List<NodeInfo> findAll(NodeType type) {
    //        List<NodeInfo> result = new ArrayList<NodeInfo>();
    //        Map<UUID, NodeInfo> list = map.get(type);
    //        for (NodeInfo info : list.values()) {
    //            if (!filter(info))
    //                result.add(info);
    //        }
    //        return result;
    //    }
    //
    //    public void save() throws IOException {
    //        File manifestFile = getManifestFile();
    //        String json = getFactory().serialize(this);
    //        FileUtils.writeStringToFile(manifestFile, json);
    //    }
    //
    //    /**
    //     * Saves a {@link ICaustkNode} to disk.
    //     * 
    //     * @param node The {@link ICaustkNode} to save.
    //     * @return
    //     * @throws CausticException
    //     * @throws IOException
    //     */
    //    public File save(ICaustkNode node) throws IOException {
    //        File location = resolveAbsoluteArchive(node.getInfo());
    //        String json = getFactory().serialize(node);
    //
    //        String fileName = FilenameUtils.getBaseName(location.getName());
    //        File sourceDirectory = new File(RuntimeUtils.getApplicationTempDirectory(), fileName);
    //        sourceDirectory.mkdirs();
    //
    //        File stateFile = new File(sourceDirectory, "manifest.json");
    //        FileUtils.writeStringToFile(stateFile, json);
    //
    //        // XXX construct the archive for the specific node type
    //        // for now the only special node type is MachineNode that needs
    //        // the presets directory
    //        if (node instanceof MachineNode && !(node instanceof VocoderMachine)) {
    //            File presetsDirectory = new File(sourceDirectory, "presets");
    //            MachineNode machineNode = (MachineNode)node;
    //            File file = new File(presetsDirectory, fileName + "."
    //                    + machineNode.getType().getExtension());
    //            FileUtils.writeByteArrayToFile(file, machineNode.getPreset().getRestoredData());
    //            //            machineNode.getPreset().exportPreset(presetsDirectory,
    //            //                    machineNode.getPreset().getName());
    //            // XXX Must have the bytes for this to work in this state
    //            // who knows where the rack is and we can't just "save" a preset
    //            // for a machine we don't even know exists
    //            //machineNode.getPreset().fill();
    //        }
    //
    //        ZipCompress compress = new ZipCompress(sourceDirectory);
    //        compress.zip(location);
    //        try {
    //            Thread.sleep(200);
    //        } catch (InterruptedException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //        FileUtils.deleteDirectory(sourceDirectory);
    //
    //        return location;
    //    }
    //
    //    /**
    //     * Loads and creates an {@link ICaustkNode} from an archive that was
    //     * previously serialized.
    //     * 
    //     * @param info The {@link NodeInfo} that is the target.
    //     * @param clazz The clazz type the <code>manifest.json</code> is
    //     *            deserialized to.
    //     * @return A new instance of the deserialized clazz type.
    //     * @throws IOException File not found
    //     * @throws CausticException Node file note found
    //     */
    //    public <T extends ICaustkNode> T newInstance(NodeInfo info, Class<T> clazz) throws IOException,
    //            CausticException {
    //        File archiveFile = resolveAbsoluteArchive(info);
    //        if (!archiveFile.exists())
    //            throw new CausticException("Node file not found: " + archiveFile);
    //
    //        File outputDirectory = uncompressToTempDirectory(archiveFile);
    //
    //        File manifest = new File(outputDirectory, "manifest.json");
    //        String json = FileUtils.readFileToString(manifest);
    //
    //        T definition = getFactory().deserialize(json, clazz);
    //        if (definition instanceof MachineNode) {
    //            String presetName = info.getName();
    //            File presetDirectory = new File(outputDirectory, "presets");
    //            MachineNode machineNode = (MachineNode)definition;
    //            File presetFile = new File(presetDirectory, presetName + "."
    //                    + machineNode.getType().getExtension());
    //            if (!presetFile.exists())
    //                throw new CausticException("Preset file not found: " + presetFile);
    //            // pass the bytes into the preset for future #restorePreset()
    //            machineNode.getPreset().fill(presetFile);
    //        }
    //
    //        try {
    //            Thread.sleep(200);
    //        } catch (InterruptedException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //
    //        FileUtils.deleteDirectory(outputDirectory);
    //        return definition;
    //    }
    //
    //    private static File uncompressToTempDirectory(File archiveFile) {
    //        ZipUncompress uncompress = new ZipUncompress(archiveFile);
    //        File outputDirectory = new File(RuntimeUtils.getApplicationTempDirectory(), "foo__"
    //                + FilenameUtils.getBaseName(archiveFile.getName()));
    //        outputDirectory.mkdirs();
    //        uncompress.unzip(outputDirectory);
    //        return outputDirectory;
    //    }
    //
    //    //--------------------------------------------------------------------------
    //    // Overridden Protected :: Methods
    //    //--------------------------------------------------------------------------
    //
    //    @Override
    //    protected void createComponents() {
    //        if (!exists())
    //            getDirectory().mkdirs();
    //        try {
    //            save();
    //        } catch (IOException e) {
    //            // TODO log err()
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    @Override
    //    protected void destroyComponents() {
    //        try {
    //            FileUtils.deleteDirectory(getDirectory());
    //        } catch (IOException e) {
    //            // TODO log err()
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    @Override
    //    protected void updateComponents() {
    //    }
    //
    //    @Override
    //    protected void restoreComponents() {
    //    }
    //
    //    //--------------------------------------------------------------------------
    //    // Private :: Methods
    //    //--------------------------------------------------------------------------
    //
    //    private void definitionAdded(ICaustkNode node) throws IOException {
    //        //        invalidated = true;
    //        if (node.getInfo().getFile() == null && node.getInfo().getName() == null)
    //            throw new RuntimeException("File and/or Name is null on NodeInfo for write");
    //
    //        NodeInfo info = node.getInfo();
    //        UUID uuid = info.getId();
    //        NodeType type = info.getType();
    //
    //        String path = getPathResolver().resolvePath(node);
    //        info.setFile(new File(path));
    //
    //        Map<UUID, NodeInfo> list = map.get(type);
    //        if (list == null) {
    //            list = new HashMap<UUID, NodeInfo>();
    //            map.put(type, list);
    //        }
    //
    //        list.put(uuid, info);
    //        writeToDisk(node);
    //
    //        save();
    //    }
    //
    //    private void definitionRemoved(NodeInfo info) throws FileNotFoundException {
    //        //        invalidated = true;
    //        deleteFromDisk(info);
    //    }
    //
    //    private File writeToDisk(ICaustkNode node) throws IOException {
    //        // definition must exist in Library
    //        if (!contains(node))
    //            throw new IOException("Library does not contian node; " + node);
    //
    //        File location = null;
    //
    //        location = save(node);
    //
    //        return location;
    //    }
    //
    //    private File deleteFromDisk(NodeInfo info) throws FileNotFoundException {
    //        File file = resolveAbsoluteArchive(info);
    //        FileUtils.deleteQuietly(file);
    //        if (file.exists())
    //            throw new IllegalStateException("File not deleted:" + file);
    //        return file;
    //    }
    //
    //    public File resolveAbsoluteArchive(NodeInfo info) {
    //
    //        File file = info.getFile();
    //        if (file == null)
    //            throw new IllegalStateException("path must not be null");
    //        return new File(getDirectory(), info.getRelativePath());
    //    }
    //
    //    private boolean filter(NodeInfo info) {
    //        return false;
    //    }

}

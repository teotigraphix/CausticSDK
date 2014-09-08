
package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.google.inject.Inject;
import com.teotigraphix.caustk.core.CaustkEngine;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public abstract class ApplicationStates extends ApplicationComponent implements IApplicationStates {

    private Kryo kryo;

    @Inject
    private IApplicationModel applicationModel;

    @Override
    protected String getPreferenceId() {
        throw new IllegalStateException();
    }

    public ApplicationStates() {
        kryo = new Kryo();
        kryo.setRegistrationRequired(true);

        register(kryo);
    }

    protected void register(Kryo kryo) {

        kryo.register(HashMap.class);
        kryo.register(File.class);

        kryo.register(HashMap.class, new MapSerializer());
        kryo.register(File.class, new FileSerializer());
    }

    @Override
    public void loadLastProjectState() throws IOException {
        ApplicationProject project = null;

        String path = applicationModel.getPreferences().getString(
                ApplicationModel.LAST_PROJECT_PATH, null);
        File rootDirectory = RuntimeUtils.getApplicationDirectory();
        File projectsDirectory = new File(rootDirectory, "projects");
        File projectLocation = null;

        if (!projectsDirectory.exists())
            projectsDirectory.mkdirs();

        if (path != null) {
            projectLocation = new File(path);
        } else {
            projectLocation = new File(projectsDirectory, "Untitled.prj");
        }
        // C:\Users\Teoti\Documents\Tones\projects\Untitled.prj
        if (!projectLocation.exists()) {
            project = createDefaultProject("Untitled", projectLocation.getAbsolutePath());
        } else {
            project = loadProject(projectLocation);
        }

        getApplication().getLogger().log(">>>>> ApplicationStates", "loadLastProjectState()");
        CaustkEngine.DEBUG_MESSAGES = false;
        CaustkEngine.DEBUG_QUERIES = false;
        // XXX        model.importSoundKit(RuntimeUtils.getSongFile("TESTLIB1"));
        CaustkEngine.DEBUG_MESSAGES = true;
        CaustkEngine.DEBUG_QUERIES = true;

        applicationModel.setProject(project);

    }

    public void save(ApplicationProject project) throws FileNotFoundException {
        saveProject(project);
    }

    private void saveProject(ApplicationProject project) throws FileNotFoundException {
        Output output = new Output(new FileOutputStream(project.getLocation().getAbsolutePath()));
        kryo.writeObject(output, project);
        output.close();
    }

    private ApplicationProject loadProject(File projectLocation) throws FileNotFoundException {
        Input input = new Input(new FileInputStream(projectLocation));
        ApplicationProject instance = readProject(kryo, input);
        return instance;
    }

    protected abstract ApplicationProject readProject(Kryo kryo, Input input);

    protected abstract ApplicationProject createDefaultProject(String string, String location);

    @Override
    public void startUI() {
        getApplication().getLogger().log(">>>>> ApplicationStates", "startUI()");
        // XXX TEMP until I figure out where Project prefs are
        //        restartUI();
        //testMidi();
    }

    @Override
    public void restartUI() {
        getApplication().getLogger().log(">>>>> ApplicationStates", "restartUI()");
        applicationModel.setProject(applicationModel.getProject());
    }

    @Override
    protected void construct() {
        getApplication().getEventBus().register(this);
        File tempDirectory = getApplicationTempDirectory();
        if (!tempDirectory.exists())
            tempDirectory.mkdirs();
    }

    private static File getApplicationTempDirectory() {
        String storagePath = Gdx.files.getExternalStoragePath();
        return new File(new File(storagePath), ".caustk");
    }

    //--------------------------------------------------------------------------

    public class FileSerializer extends Serializer<File> {

        @Override
        public File read(Kryo kryo, Input input, Class<File> file) {
            String readString = input.readString();
            return new File(readString);
        }

        @Override
        public void write(Kryo kryo, Output output, File file) {
            output.write(file.getAbsolutePath().getBytes());
        }

    }

    public class UUIDSerializer extends Serializer<UUID> {
        public UUIDSerializer() {
            setImmutable(true);
        }

        @Override
        public void write(final Kryo kryo, final Output output, final UUID uuid) {
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }

        @Override
        public UUID read(final Kryo kryo, final Input input, final Class<UUID> uuidClass) {
            return new UUID(input.readLong(), input.readLong());
        }
    }
}

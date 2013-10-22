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

package com.teotigraphix.libgdx.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.androidtransfuse.event.EventObserver;
import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.model.ApplicationModel;
import com.teotigraphix.libgdx.model.ApplicationModelState;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.model.IApplicationModel.OnApplicationModelProjectChange;

/**
 * A bas application mediator for application state, first run and load logic.
 * 
 * @author Michael Schmalle
 * @see #firstRun(ApplicationModelState)
 * @see #onLoad()
 */
public abstract class ApplicationMediatorBase extends CaustkMediator implements
        IApplicationMediator {

    private static final String TAG = "ApplicationMediatorBase";

    @Inject
    protected IApplicationModel applicationModel;

    public ApplicationModel getApplicationModel() {
        return (ApplicationModel)applicationModel;
    }

    protected boolean deleteCausticFile = true;

    protected Class<? extends ApplicationModelState> stateType;

    public ApplicationMediatorBase() {
        initialize();
        createKryo();
    }

    protected void initialize() {
        // TODO Auto-generated method stub

    }

    private Kryo kryo;

    private void createKryo() {
        kryo = new Kryo();
        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        //kryo.setRegistrationRequired(true);

        kryo.setAsmEnabled(true);
        kryo.register(stateType);
        kryo.register(UUID.class, new UUIDSerializer());
        kryo.register(File.class, new FileSerializer());
        //        kryo.register(Class.class);
        //        kryo.register(ISystemSequencer.SequencerMode.class);
        //
        //        kryo.register(Rack.class);
        //
        //        kryo.register(SubSynthTone.class);
        //
        //        kryo.register(SoundMixer.class);
        //        kryo.register(MasterMixer.class);
        //        kryo.register(HashMap.class);
        //        kryo.register(ArrayList.class);
        //        kryo.register(MasterDelay.class);
        //        kryo.register(MasterEqualizer.class);
        //        kryo.register(MasterLimiter.class);
        //        kryo.register(MasterReverb.class);
        //        kryo.register(SoundSource.class);
        //        kryo.register(SystemSequencer.class);
        //        kryo.register(TrackSequencer.class);
        //        kryo.register(TrackSong.class);
        //        kryo.register(CausticSongFile.class);
        //
        //        kryo.register(SoundMixerChannel.class);
        //        kryo.register(Track.class);
        //        kryo.register(TrackItem.class);
        //        kryo.register(TreeMap.class);
        //        kryo.register(Trigger.class);
        //        kryo.register(TriggerMap.class);
        //        kryo.register(Note.class);

    }

    public final boolean isFirstRun() {
        return applicationModel.getProject().isFirstRun();
    }

    protected void doLoadRun(File file) {
        getController().getLogger().view("ApplicationMediator", "Load last State - " + file);
        try {
            loadApplicationState(file, stateType);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    protected void doFirstRun(File file) {
        getController().getLogger().view("ApplicationMediator", "Create new State - " + file);

        ApplicationModelState state = null;
        try {
            Constructor<? extends ApplicationModelState> constructor = stateType
                    .getConstructor(ICaustkController.class);
            state = constructor.newInstance(getController());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        saveApplicationState(file, state);
        getApplicationModel().setState(state);

        firstRun(state);
    }

    @Override
    public void onRegister() {
        register(OnApplicationModelProjectChange.class,
                new EventObserver<OnApplicationModelProjectChange>() {
                    @Override
                    public void trigger(OnApplicationModelProjectChange object) {
                        Project project = object.getProject();
                        File file = getProjectBinaryFile(project);
                        if (project.isFirstRun()) {
                            doFirstRun(file);
                        } else {
                            doLoadRun(file);
                        }
                    }
                });
    }

    /**
     * Called during {@link #registerObservers()} when the state does not exist
     * on disk.
     * 
     * @param state The current application state after creation or
     *            deserialization.
     */
    protected void firstRun(ApplicationModelState state) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void run() {
        onRun();
    }

    protected void onRun() {
    }

    @Override
    public void save() {
        saveApplicationState(getProjectBinaryFile(getApplicationModel().getProject()),
                getApplicationModel().getState());
    }

    protected ApplicationModelState loadApplicationState(File file,
            Class<? extends ApplicationModelState> stateType) throws CausticException {
        ApplicationModelState state = null;
        try {

            // read the full state back into memory
            readState(file);

            // save a temp .caustic file to load the binary data into the native core
            File absoluteCausticFile = getTempCausticFile();
            FileUtils.writeByteArrayToFile(absoluteCausticFile, getApplicationModel().getState()
                    .getSongFile().getData());

            // only load the song into the core memory, we already have
            // the object graph mirrored in the Rack
            getController().getRack().loadSongRaw(absoluteCausticFile);

            // remove the temp file
            FileUtils.deleteQuietly(absoluteCausticFile);

        } catch (IOException e) {
            getController().getLogger().err(TAG, "IOException", e);
        } catch (CausticException e) {
            throw e;
        }
        return state;
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

    public class FileSerializer extends Serializer<File> {
        @Override
        public File read(Kryo kryo, Input input, Class<File> type) {
            return new File(input.readString());
        }

        @Override
        public void write(Kryo kryo, Output output, File object) {
            output.writeString(object.getAbsolutePath());
        }
    }

    protected void saveApplicationState(File file, ApplicationModelState state) {
        try {
            final File causticFile = getTempCausticFile();

            state.save();
            getController().getRack().saveSongAs(causticFile);

            //            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

            Output output = new Output(new FileOutputStream(file));

            byte[] byteArray = FileUtils.readFileToByteArray(causticFile);
            CausticSongFile songFile = new CausticSongFile(causticFile.getName(), byteArray);
            state.setSongFile(songFile);

            //            out.writeObject(state);
            kryo.writeObject(output, state);

            //            out.close();
            output.close();

            if (deleteCausticFile)
                FileUtils.deleteQuietly(causticFile);

        } catch (FileNotFoundException e) {
            getController().getLogger().err(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            getController().getLogger().err(TAG, "IOException", e);
        }
    }

    private void readState(File file) throws CausticException {
        //ObjectInputStream in = null;
        ApplicationModelState state = null;
        try {

            Input input = new Input(new FileInputStream(file));
            state = kryo.readObject(input, stateType);
            input.close();
            //            in = new ObjectInputStream(new FileInputStream(file));
            //            state = stateType.cast(in.readObject());
            //            in.close();
        } catch (FileNotFoundException e) {
            getController().getLogger().err(TAG, "FileNotFoundException", e);
        }

        if (state == null)
            throw new CausticException("Application state failed to load");

        // load the application model with the deserialized state
        state.setController(getController());

        getApplicationModel().setState(state);
    }

    protected File getTempCausticFile() {
        String projectName = applicationModel.getProject().getName();
        File file = applicationModel.getProject()
                .getAbsoluteResource(projectName + "Audio.caustic");
        return file;
    }

    protected File getProjectBinaryFile(Project project) {
        File file = project.getAbsoluteResource(project.getName() + ".bin");
        return file;
    }
}

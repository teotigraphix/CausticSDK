
package com.teotigraphix.caustk.project;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerCreate;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerLoad;

public class SongManager implements ISongManager {

    @SuppressWarnings("unused")
    private final ICaustkController controller;

    SongModel songModel;

    public SongManager(ICaustkController controller) {
        this.controller = controller;
        controller.getDispatcher().register(OnProjectManagerCreate.class,
                new EventObserver<OnProjectManagerCreate>() {
                    @Override
                    public void trigger(OnProjectManagerCreate object) {
                        object.getProject().register(SongModel.class, new SongModel());
                        songModel = object.getProject().data(SongModel.class);
                    }
                });
        controller.getDispatcher().register(OnProjectManagerLoad.class,
                new EventObserver<OnProjectManagerLoad>() {
                    @Override
                    public void trigger(OnProjectManagerLoad object) {
                        songModel = object.getProject().data(SongModel.class);
                    }
                });
    }
}

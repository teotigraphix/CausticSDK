
package com.teotigraphix.libraryeditor.mediator;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.MediatorBase;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerSelectedLibraryChange;

@Singleton
public class ApplicationMediator extends MediatorBase {

    @Inject
    IStageModel stageModel;

    public ApplicationMediator() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        getController().register(OnLibraryManagerSelectedLibraryChange.class,
                new EventObserver<OnLibraryManagerSelectedLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryManagerSelectedLibraryChange object) {
                        String name = object.getLibrary().getName();
                        stageModel.setTitle(name);
                    }
                });
    }

    @Override
    public void onRegister() {

    }

}

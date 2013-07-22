
package com.teotigraphix.libraryeditor.model;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.BeanPathAdapter;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.ModelBase;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryScene;

@Singleton
public class LibraryModel extends ModelBase {

    private Library library;

    private SceneItem selectedScene;

    public final SceneItem getSelectedScene() {
        return selectedScene;
    }

    public final void setSelectedScene(SceneItem selectedItem) {
        this.selectedScene = selectedItem;
        getController().getDispatcher().trigger(new OnLibraryModelSceneChange());
    }

    List<SceneItem> scenes = new ArrayList<>();

    public final List<SceneItem> getScenes() {
        return scenes;
    }

    public final void setScenes(List<SceneItem> scenes) {
        this.scenes = scenes;
    }

    BeanPathAdapter<LibraryModel> sceneHolder;

    public BeanPathAdapter<LibraryModel> getSceneHolder() {
        return sceneHolder;
    }

    @Inject
    public LibraryModel(ICaustkApplicationProvider provider) {
        super(provider);

        sceneHolder = new BeanPathAdapter<>(this);
    }

    public void refresh(Library library) {
        this.library = library;
        scenes.clear();

        for (LibraryScene scene : library.getScenes()) {
            scenes.add(new SceneItem(scene));
        }
    }

    public static class OnLibraryModelSceneChange {
    }

    public static class OnLibraryModelRefresh {
    }

    public void update() {
        sceneHolder.setBean(this);
    }
}

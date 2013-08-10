
package com.teotigraphix.libraryeditor.model;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.application.IPreferenceManager;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;

@Singleton
public class LibraryModel extends ModelBase {

    private static final String PREF_SELECTED_KIND = "LibraryModel_selectedKind";

    @Inject
    IPreferenceManager applicationPreferences;

    private ItemKind selectedKind = ItemKind.SCENE;

    public final ItemKind getSelectedKind() {
        return selectedKind;
    }

    public final void setSelectedKind(ItemKind value) {
        if (value == selectedKind)
            return;
        ItemKind oldKind = selectedKind;
        selectedKind = value;
        getController().getDispatcher().trigger(
                new OnLibraryModelSelectedKindChange(selectedKind, oldKind));
        applicationPreferences.edit().putInt(PREF_SELECTED_KIND, selectedKind.getIndex()).commit();

    }

    private LibraryItemProxy selectedItem;

    private LibraryPatch libraryPatch;

    private LibraryPhrase libraryPhrase;

    private int pendingKind;

    public void setSelectedItem(LibraryItemProxy value) {
        selectedItem = value;
        if (selectedItem.getItem() instanceof LibraryPatch)
            libraryPatch = (LibraryPatch)selectedItem.getItem();
        else if (selectedItem.getItem() instanceof LibraryPhrase)
            libraryPhrase = (LibraryPhrase)selectedItem.getItem();
        getController().getDispatcher().trigger(new OnLibraryModelSelectedItemChange(selectedItem));
    }

    public LibraryPatch getLibraryPatch() {
        return libraryPatch;
    }

    public LibraryPhrase getLibraryPhrase() {
        return libraryPhrase;
    }

    public LibraryItemProxy getSelectedItem() {
        return selectedItem;
    }

    public enum ItemKind {
        SCENE(0), PATCH(1), PHRASE(2);

        private int index;

        ItemKind(int index) {
            this.index = index;
        }

        public final int getIndex() {
            return index;
        }

        public static ItemKind fromInt(int index) {
            for (ItemKind kind : values()) {
                if (kind.getIndex() == index)
                    return kind;
            }
            return null;
        }
    }

    /**
     * @see ICaustkController#getDispatcher()
     */
    public static class OnLibraryModelSelectedKindChange {

        private ItemKind kind;

        private ItemKind oldKind;

        public final ItemKind getKind() {
            return kind;
        }

        public final ItemKind getOldKind() {
            return oldKind;
        }

        public OnLibraryModelSelectedKindChange(ItemKind kind, ItemKind oldKind) {
            this.kind = kind;
            this.oldKind = oldKind;
        }
    }

    public static class OnLibraryModelSelectedItemChange {

        private LibraryItemProxy item;

        public final LibraryItemProxy getItem() {
            return item;
        }

        public OnLibraryModelSelectedItemChange(LibraryItemProxy item) {
            this.item = item;
        }
    }

    public static class OnLibraryModelRefresh {

    }

    @Override
    public void onShow() {

        setSelectedKind(ItemKind.fromInt(pendingKind));
        pendingKind = -1;
    }

    @Override
    public void onRegister() {
        pendingKind = applicationPreferences.getInt(PREF_SELECTED_KIND, 0);
    }

    private List<LibraryItemProxy> scenes = new ArrayList<>();

    private List<LibraryItemProxy> patches = new ArrayList<>();

    private List<LibraryItemProxy> phrases = new ArrayList<>();

    public List<LibraryItemProxy> getScenes() {
        return scenes;
    }

    public List<LibraryItemProxy> getPatches() {
        return patches;
    }

    public List<LibraryItemProxy> getPhrases() {
        return phrases;
    }

    public void refresh() {
        scenes.clear();
        patches.clear();
        phrases.clear();

        Library library = getController().getLibraryManager().getSelectedLibrary();
        for (LibraryScene item : library.getScenes()) {
            scenes.add(new LibraryItemProxy(item));
        }

        for (LibraryPatch item : library.getPatches()) {
            patches.add(new LibraryItemProxy(item));
        }

        for (LibraryPhrase item : library.getPhrases()) {
            phrases.add(new LibraryItemProxy(item));
        }
    }

    public static class LibraryItemProxy {

        public final String getName() {
            return item.getMetadataInfo().getName();
        }

        public final void setName(String name) {
            item.getMetadataInfo().setName(name);
        }

        public final String getAuthor() {
            return item.getMetadataInfo().getAuthor();
        }

        public final void setAuthor(String author) {
            item.getMetadataInfo().setAuthor(author);
        }

        public final String getDescription() {
            return item.getMetadataInfo().getDescription();
        }

        public final void setDescription(String description) {
            item.getMetadataInfo().setDescription(description);
        }

        public final String getTagsString() {
            return item.getMetadataInfo().getTagsString();
        }

        public final void setTagsString(String tagsString) {
            item.getMetadataInfo().setTagsString(tagsString);
        }

        private LibraryItem item;

        public LibraryItem getItem() {
            return item;
        }

        public LibraryItemProxy(LibraryItem item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return item.toString();
        }
    }

    @Override
    public void save() {
        // TODO Auto-generated method stub
        
    }

}

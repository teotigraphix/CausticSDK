
package com.teotigraphix.libraryeditor.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.ModelBase;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;

@Singleton
public class LibraryItemModel extends ModelBase {

    private ItemKind selectedKind;

    public final ItemKind getSelectedKind() {
        return selectedKind;
    }

    public final void setSelectedKind(ItemKind value) {
        if (value == selectedKind)
            return;
        ItemKind oldKind = selectedKind;
        selectedKind = value;
        getController().getDispatcher().trigger(
                new OnLibraryItemModelSelectedKindChange(selectedKind, oldKind));
    }

    private LibraryScene selectedScene;

    private LibraryPatch selectedPatch;

    private LibraryPhrase selectedPhrase;

    public final LibraryScene getSelectedScene() {
        return selectedScene;
    }

    public final void setSelectedScene(LibraryScene value) {
        if (value == selectedScene)
            return;
        selectedScene = value;
    }

    public final LibraryPatch getSelectedPatch() {
        return selectedPatch;
    }

    public final void setSelectedPatch(LibraryPatch value) {
        if (value == selectedPatch)
            return;
        selectedPatch = value;
    }

    public final LibraryPhrase getSelectedPhrase() {
        return selectedPhrase;
    }

    public final void setSelectedPhrase(LibraryPhrase value) {
        if (value == selectedPhrase)
            return;
        selectedPhrase = value;
    }

    public LibraryItem getSelectedItem() {
        if (selectedKind == ItemKind.SCENE)
            return selectedScene;
        else if (selectedKind == ItemKind.PATCH)
            return selectedPatch;
        else if (selectedKind == ItemKind.PHRASE)
            return selectedPhrase;
        return null;
    }

    @Inject
    public LibraryItemModel(ICaustkApplicationProvider provider) {
        super(provider);
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
    public static class OnLibraryItemModelSelectedKindChange {

        private ItemKind kind;

        private ItemKind oldKind;

        public final ItemKind getKind() {
            return kind;
        }

        public final ItemKind getOldKind() {
            return oldKind;
        }

        public OnLibraryItemModelSelectedKindChange(ItemKind kind, ItemKind oldKind) {
            this.kind = kind;
            this.oldKind = oldKind;
        }
    }

    public static class OnLibraryItemModelItemChange {

        private LibraryItem item;

        public final LibraryItem getItem() {
            return item;
        }

        public OnLibraryItemModelItemChange(LibraryItem item) {
            this.item = item;
        }

    }

    private LibraryItem itemProxy;

    public void setSelectedProxy(LibraryItem value) {
        itemProxy = value;
        getController().getDispatcher().trigger(new OnLibraryItemModelItemChange(itemProxy));
    }

    public LibraryItem getSelectedProxy() {
        return itemProxy;
    }
}

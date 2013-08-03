
package com.teotigraphix.libraryeditor.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;

@Singleton
public class LibraryModel extends ModelBase {

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
                new OnLibraryModelSelectedKindChange(selectedKind, oldKind));
    }

    private LibraryItem selectedItem;

    private LibraryPatch libraryPatch;

    private LibraryPhrase libraryPhrase;

    public void setSelectedItem(LibraryItem value) {
        selectedItem = value;
        if (selectedItem instanceof LibraryPatch)
            libraryPatch = (LibraryPatch)selectedItem;
        if (selectedItem instanceof LibraryPhrase)
            libraryPhrase = (LibraryPhrase)selectedItem;
        getController().getDispatcher().trigger(new OnLibraryModelSelectedItemChange(selectedItem));
    }

    public LibraryPatch getLibraryPatch() {
        return libraryPatch;
    }

    public LibraryPhrase getLibraryPhrase() {
        return libraryPhrase;
    }

    public LibraryItem getSelectedItem() {
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

        private LibraryItem item;

        public final LibraryItem getItem() {
            return item;
        }

        public OnLibraryModelSelectedItemChange(LibraryItem item) {
            this.item = item;
        }
    }

    public static class OnLibraryModelRefresh {

    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onRegister() {
        // TODO Auto-generated method stub
        
    }

}

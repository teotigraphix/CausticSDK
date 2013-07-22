
package com.teotigraphix.libraryeditor.model;

import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.caustk.library.LibraryScene;

public class SceneItem {

    private LibraryScene item;

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

    public final String getTags() {
        return item.getMetadataInfo().getTags().toString();
    }

    public final void setTags(String tags) {
        //item.getMetadataInfo().setTags(tags.split(" "));
    }

    public SceneItem(LibraryScene item) {
        this.item = item;
    }

    public LibraryItem getItem() {
        return item;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return getName();
    }
}

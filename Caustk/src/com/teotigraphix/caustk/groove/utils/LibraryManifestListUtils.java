
package com.teotigraphix.caustk.groove.utils;

import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class LibraryManifestListUtils {

    public static enum LibraryManifestSortOrder {
        DisplayName, Author;
    }

    public static void sort(List<LibraryItemManifest> list, LibraryManifestSortOrder order) {
        switch (order) {
            case Author:
                sortByAuthor(list);
                break;

            case DisplayName:
                sortByDisplayName(list);
                break;
        }
    }

    public static List<LibraryItemManifest> getItemsFromTag(List<LibraryItemManifest> list,
            String tag) {
        List<LibraryItemManifest> result = new ArrayList<LibraryItemManifest>();
        for (LibraryItemManifest manifest : list) {
            if (manifest.hasTags() && manifest.hasTag(tag))
                result.add(manifest);
        }
        return result;
    }

    public static List<LibraryItemManifest> getItemsFromTags(List<LibraryItemManifest> list,
            List<String> tags) {
        List<LibraryItemManifest> result = new ArrayList<LibraryItemManifest>();
        for (LibraryItemManifest manifest : list) {
            if (manifest.hasTags() && manifest.hasTag(tags))
                result.add(manifest);
        }
        return result;
    }

    static void sortByDisplayName(List<LibraryItemManifest> list) {
        Collections.sort(list, new Comparator<LibraryItemManifest>() {
            @Override
            public int compare(LibraryItemManifest lhs, LibraryItemManifest rhs) {
                return lhs.getDisplayName().compareTo(rhs.getDisplayName());
            }
        });
    }

    static void sortByAuthor(List<LibraryItemManifest> list) {
        Collections.sort(list, new Comparator<LibraryItemManifest>() {
            @Override
            public int compare(LibraryItemManifest lhs, LibraryItemManifest rhs) {
                return lhs.getAuthor().compareTo(rhs.getAuthor());
            }
        });
    }

}

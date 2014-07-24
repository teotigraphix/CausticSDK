
package com.teotigraphix.caustk.groove;

import java.util.ArrayList;
import java.util.List;

/*

Category - SubSynth
Types - tag list
Modes - tag list

*/

public class LibraryItemBank {

    private String category;

    private List<String> types = new ArrayList<String>();

    private List<String> modes = new ArrayList<String>();

    public String getCategory() {
        return category;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getModes() {
        return modes;
    }

    public LibraryItemBank() {
    }

}

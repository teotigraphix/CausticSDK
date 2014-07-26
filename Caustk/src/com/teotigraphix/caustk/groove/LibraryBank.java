
package com.teotigraphix.caustk.groove;

import java.util.ArrayList;
import java.util.List;

/*

Drill Down

Format   - Instrument

Name     - Vendor/Pack name 'CaustkSynths v1'

Category - SubSynth
Types - tag list
Modes - tag list


The main Browser holds LibraryProducts

LibraryProduct
- name - The directory it uncompresses to
- file - The eventual uncompressed location OR existing location of uncompressed product

LibraryBankManager.install(LibraryProduct, 

*/

public class LibraryBank {

    private LibraryItemFormat format;

    private String name;

    private String category;

    private List<String> types = new ArrayList<String>();

    private List<String> modes = new ArrayList<String>();

    public LibraryItemFormat getFormat() {
        return format;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getModes() {
        return modes;
    }

    public LibraryBank() {
    }

}

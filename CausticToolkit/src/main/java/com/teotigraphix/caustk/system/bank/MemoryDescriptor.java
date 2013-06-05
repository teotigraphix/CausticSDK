
package com.teotigraphix.caustk.system.bank;

import java.util.List;

public class MemoryDescriptor {
    
    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    private int index;

    public int getIndex() {
        return index;
    }

    /**
     * @param value
     * @see MemoryLoader#addDescriptor()
     */
    public void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    private String name;

    public String getName() {
        return name;
    }

    //----------------------------------
    // patternItems
    //----------------------------------

    private List<PatternItem> patternItems;

    public List<PatternItem> getPatternItems() {
        return patternItems;
    }

    public void setPatternItems(List<PatternItem> value) {
        patternItems = value;
    }

    //----------------------------------
    // patchItems
    //----------------------------------

    private List<PhraseItem> phraseItems;

    public List<PhraseItem> getPhraseItems() {
        return phraseItems;
    }

    public void setPhraseItems(List<PhraseItem> value) {
        phraseItems = value;
    }

    //----------------------------------
    // patchItems
    //----------------------------------

    private List<PatchItem> patchItems;

    public List<PatchItem> getPatchItems() {
        return patchItems;
    }

    public void setPatchItems(List<PatchItem> value) {
        patchItems = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MemoryDescriptor(String name) {
        this.name = name;
    }

}

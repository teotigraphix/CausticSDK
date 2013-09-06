////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.library.item.LibraryPattern;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.pattern.Part;
import com.teotigraphix.caustk.pattern.Patch;
import com.teotigraphix.caustk.pattern.Pattern;
import com.teotigraphix.caustk.pattern.Phrase;
import com.teotigraphix.caustk.tone.BasslineTone;

/**
 * Represents an abstract memory bank, USER, PRESET etc.
 * <p>
 * The {@link MemoryManager} will use the {@link MemoryLoader} to load the
 * {@link MemorySlotItem}s into the memory bank's category.
 */
public abstract class Memory {
    protected ICaustkController controller;

    //----------------------------------
    // currentLibrary
    //----------------------------------

    public Library getCurrentLibrary() {
        return controller.getLibraryManager().getSelectedLibrary();
    }

    private Map<Category, MemorySlot> slots = new HashMap<Category, MemorySlot>();

    /**
     * The {@link MemoryLoader} will load these slots at startup.
     * 
     * @param category The category type.
     * @param slot The {@link MemorySlot} to fill the category.
     */
    public void put(Category category, MemorySlot slot) {
        slots.put(category, slot);
    }

    private MemorySlot getMemorySlot(Category category) {
        return slots.get(category);
    }

    public MemorySlot getPatternSlot() {
        return getMemorySlot(Category.PATTERN);
    }

    public MemorySlot getPhraseSlot() {
        return getMemorySlot(Category.PHRASE);
    }

    public MemorySlot getPatchSlot() {
        return getMemorySlot(Category.PATCH);
    }

    //----------------------------------
    // currentMemorySlot
    //----------------------------------

    private MemorySlot currentMemorySlot;

    public MemorySlot getCurrentMemorySlot() {
        return currentMemorySlot;
    }

    //----------------------------------
    // type
    //----------------------------------

    private Type type;

    public Type getType() {
        return type;
    }

    public void getType(Type value) {
        if (type == value)
            return;

        type = value;
    }

    //----------------------------------
    // category
    //----------------------------------

    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category value) {
        if (category == value)
            return;

        category = value;

        // instead of have a bunch of subclasses of memory bank
        // I can just have a strategy provider that was loaded by the loader
        // so I would have PatchProvider, PatternProvider, PhraseProvider, SongProvider
        // The loader would load the machines of a preset .caustic file
        // populate all machines, patterns and patches.
        // it will then create each strategy and populate it with MemoryBankItems
        // such as PatchItem in a LINEAR list that will forever more be used in the 
        // duration of the application. So the linear list index is established
        // by the loader when it is creating the memory bank items

        //================
        // So, the loader will have to load each type of bank and have items for each bank
        // IE USER, PRESET etc. So PRESET will have its own lists of PatchItem etc

        currentMemorySlot = slots.get(category);
    }

    public Memory(ICaustkController systemController, Type type) {
        this.controller = systemController;
        this.type = type;
    }

    /**
     * Returns the number of patterns that have been loaded into the banks'
     * memory.
     */
    public int getPatternCount() {
        return getPatternSlot().size();
    }

    /**
     * Loads the {@link Library} for the specific memory bank.
     * 
     * @param library TODO
     * @throws IOException
     */
    public void load(Library library) throws IOException {
        //XXX this needs to be run in a service
        // this list for now is ONLY used to create the bank and pattern names
        // for the UI display
        // seems like the most logical way is to have
        // A[0], B[1], C[2], D[3], E[4], F[5]
        // where the index is the machine
        // each bank has 64 possible patterns from the machine
        // the algorithm will automatically fill all 64 with either existing patterns
        // or empty, if the machine does not exist, the bank will not be created

        getPhraseSlot().addAll(library.getPhrases());
        getPatchSlot().addAll(library.getPatches());
        getPatternSlot().addAll(library.getScenes());
    }

    /**
     * Copies a pattern data from the bank into a NEW {@link Pattern} instance
     * and returns it.
     * <p>
     * This will work out to a pattern having the string id of <code>U14</code>
     * or <code>U01</code> all the way up to <code>U63</code>.
     * <p>
     * Until more complex logic is implemented, 64 patterns per {@link Pattern}
     * is the most easiest to manager right now.
     * 
     * @param index The index (0-63) of the patter. Note, the linear value must
     *            be mapped from the Caustic bank/pattern paradigm of 4 banks
     *            and 16 patterns in a bank.
     * @return A new instance of a {@link Pattern} initialized with the
     *         serialized full pattern data OR initialized with the default
     *         configurator.
     */
    public Pattern copyPattern(int index) {
        Pattern pattern = getPattern(index);
        controller.getPatternManager().configure(pattern);
        return pattern;
    }

    /**
     * Always returns a NEW instance with {@link Part} and {@link Phrase} data
     * loaded.
     * <p>
     * It looks like the implementation is going to change a bit, since this
     * needs to return a fully loaded Pattern with parts and phrase data.
     * <p>
     * The commit() method of the temporary memory will actually set the part
     * and phrase data INTO the tone.
     * 
     * @param index The index of the {@link Pattern} to return based on the
     *            selection in the UI that was based off what this provider
     *            returned as the initial list of patterns.
     */
    Pattern getPattern(int index) {
        // XXX If this works with libraries, it might be better to add the items
        // to the slods in the USER, SYSTEM banks so there is no ref to a library in this class
        //LibraryItem item = (LibraryItem)getPatternSlot().getItem(index);
        LibraryPattern item = getCurrentLibrary().getPatterns().get(index);
        Pattern pattern = new Pattern(controller, item);
        return pattern;
    }

    public Phrase copyPhrase(Part part, int index) {
        Phrase phrase = getPhrase(part);
        //phrase.configure();
        return phrase;
    }

    /**
     * Copies a {@link Patch} from memory into a new {@link Patch} instance.
     * 
     * @param part
     * @param index The index of the patch to copy form memory.
     * @return A new instance of the {@link Patch}.
     */
    public Patch copyPatch(Part part, int index) {
        Patch patch = getPatch(part);
        patch.configure();
        return patch;
    }

    /**
     * Creates a {@link Phrase} for the {@link Part}.
     * 
     * @param part The {@link Part} needing a {@link Phrase}.
     */
    Phrase getPhrase(Part part) {
        int index = part.getIndex();
        UUID id = part.getPattern().getPatternItem().getPhrase(index);
        LibraryPhrase libraryPhrase = getCurrentLibrary().findPhraseById(id);
        if (libraryPhrase == null) {
            libraryPhrase = new LibraryPhrase();
        }
        Phrase phrase = new Phrase(part, libraryPhrase);
        return phrase;
    }

    /**
     * Creates and returns a {@link Patch} based on the {@link Part} passed.
     * <p>
     * An example is a {@link Part#getTone()} being an instance of
     * {@link BasslineTone}, the method would return a {@link BasslinePatch}.
     * 
     * @param part The {@link Part} needing a {@link Patch}.
     */
    Patch getPatch(Part part) {
        int index = part.getIndex();
        UUID patchId = part.getPattern().getPatternItem().getToneSet().getDescriptors().get(index)
                .getPatchId();
        LibraryPatch item = getCurrentLibrary().findPatchById(patchId);
        //if (part.getTone() instanceof BasslineTone)
        return new Patch(part, item);
        //return null;
    }

    public enum Type {
        PRESET, USER, EXTERNAL, TEMPORARY;
    }

    public enum Category {
        PATTERN_SET, PATTERN, SONG, RPSSET, PATCH, PHRASE;
    }

}

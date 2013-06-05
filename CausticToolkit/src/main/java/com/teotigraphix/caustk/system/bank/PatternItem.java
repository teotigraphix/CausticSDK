
package com.teotigraphix.caustk.system.bank;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustic.internal.utils.PatternUtils;
import com.teotigraphix.caustic.machine.IMachine;

/**
 * A {@link PatternItem} is an unserialized representation of a Caustic
 * {@link IMachine}s single pattern.
 * 
 * @author Work
 */
public class PatternItem extends MemorySlotItem {
    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // phrases
    //----------------------------------

    private List<PhraseItem> phrases = new ArrayList<PhraseItem>();

    /**
     * Returns the list of {@link PhraseItem}s.
     */
    public List<PhraseItem> getPhrases() {
        return phrases;
    }

    //----------------------------------
    // bank
    //----------------------------------

    private final int bankIndex;

    /**
     * The bank index as it was loaded from the {@link IMachine}.
     */
    public final int getBankIndex() {
        return bankIndex;
    }

    //----------------------------------
    // patternIndex
    //----------------------------------

    private final int patternIndex;

    /**
     * The pattern index as it was loaded from the {@link IMachine}.
     */
    public final int getPatternIndex() {
        return patternIndex;
    }

    //----------------------------------
    // tempo
    //----------------------------------

    private int tempo;

    /**
     * The tempo of the pattern.
     * <p>
     * When loading by default, this is determined by the tempo of the
     * <code>.causitc</code> file it was loaded from.
     */
    public int getTempo() {
        return tempo;
    }

    //----------------------------------
    // length
    //----------------------------------

    private int length;

    /**
     * The length of the pattern as loaded from the {@link IMachine}.
     */
    public int getLength() {
        return length;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public PatternItem(String patternName, int length, int tempo) {
        this.length = length;
        this.tempo = tempo;

        bankIndex = PatternUtils.toBank(patternName);
        patternIndex = PatternUtils.toPattern(patternName);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * @param index
     * @return
     */
    public PhraseItem getPhraseAt(int index) {
        return phrases.get(index);
    }

    /**
     * @param item
     */
    public void addPhraseItem(PhraseItem item) {
        phrases.add(item);
    }
}

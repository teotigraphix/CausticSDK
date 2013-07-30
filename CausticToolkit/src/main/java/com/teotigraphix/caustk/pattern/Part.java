
package com.teotigraphix.caustk.pattern;

import com.teotigraphix.caustk.tone.Tone;

public class Part {
    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return pattern.getParts().indexOf(this);
    }

    //----------------------------------
    // tone
    //----------------------------------

    private Tone tone;

    public Tone getTone() {
        return tone;
    }

    //----------------------------------
    // pattern
    //----------------------------------

    private Pattern pattern;

    public Pattern getPattern() {
        return pattern;
    }

    //----------------------------------
    // patch
    //----------------------------------

    private Patch patch;

    public Patch getPatch() {
        return patch;
    }

    public void setPatch(Patch value) {
        patch = value;
    }

    //----------------------------------
    // phrase
    //----------------------------------

    private Phrase phrase;

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase value) {
        phrase = value;
    }

    //----------------------------------
    // isRhythm
    //----------------------------------

    public boolean isRhythm() {
        return this instanceof RhythmPart;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Part(Pattern pattern, Tone tone) {
        this.pattern = pattern;
        this.tone = tone;
    }

    /**
     * Transposes the {@link Phrase} by the delta positive or negative.
     * <p>
     * The {@link Phrase} will track the original, the value of 0 will set the
     * original pitch on the phrase.
     * 
     * @param delta
     */
    public void transpose(int delta) {
        //        getPhrase().transpose(delta);
    }

}

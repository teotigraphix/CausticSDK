
package com.teotigraphix.caustic.internal.sequencer;

import java.util.Map;

import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.IStepPhrase;
import com.teotigraphix.caustic.sequencer.ITrigger;

public class PatternSequencerUtils {

    public static IStepPhrase addEmptyStepPhrase(IPatternSequencer sequencer, int bank, int pattern) {
        return addStepPhrase((PatternSequencer)sequencer, bank, pattern, null);
    }

    /**
     * Adds a new phrase to the sequencer at the bank and pattern.
     * <p>
     * If the phrase exists, it will be returned.
     * 
     * @param bank The bank index.
     * @param pattern The pattern index.
     */
    static IStepPhrase addStepPhrase(PatternSequencer sequencer, int bank, int pattern) {
        return addStepPhrase(sequencer, bank, pattern, null);
    }

    /**
     * Adds a new phrase to the sequencer at the bank and pattern using the
     * String pattern data to initialize the {@link ITrigger}s in the new
     * phrase.
     * <p>
     * If the phrase exists, it will be returned and it's phrases will be
     * removed and the serialized data will be added to the existing phrase
     * which will reinitialize the phrase.
     * <p>
     * Using this method will <strong>not</strong> send note_data messages to
     * the core, use {@link #addNote(int, float, float, float, int)} instead.
     * <p>
     * Mainly used for restoring song state from a caustic file where the core
     * already has it's bank/pattern/note structure.
     * 
     * @param bank The bank index.
     * @param pattern The pattern index.
     * @param data The serialized String data to initialize the new phrase with.
     */
    public static IStepPhrase addStepPhrase(PatternSequencer sequencer, int bank, int pattern,
            String data) {
        IStepPhrase phrase = null;

        Map<Integer, IStepPhrase> bankMap = sequencer.getBankMap(bank);
        if (bankMap.containsKey(pattern)) {
            phrase = bankMap.get(pattern);
        } else {
            phrase = sequencer.getFactory().createStepPhrase(bank, pattern);
            bankMap.put(phrase.getIndex(), phrase);
            stepPhraseAdd(sequencer, phrase);
        }

        if (data != null)
            phrase.setNoteData(data);

        return phrase;
    }

    public static IStepPhrase removeStepPhrase(PatternSequencer sequencer, int bank, int pattern) {
        IStepPhrase phrase = sequencer.getBankMap(bank).remove(pattern);
        stepPhraseRemove(sequencer, phrase);
        return phrase;
    }

    static void stepPhraseAdd(PatternSequencer sequencer, IStepPhrase phrase) {
        phrase.setSequencer(sequencer);
        phrase.addStepPhraseListener(sequencer);
    }

    static private void stepPhraseRemove(PatternSequencer sequencer, IStepPhrase phrase) {
        phrase.removeStepPhraseListener(sequencer);
        phrase.setSequencer(null);
    }

}

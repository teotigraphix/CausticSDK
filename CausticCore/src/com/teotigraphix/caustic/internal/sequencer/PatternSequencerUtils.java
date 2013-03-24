package com.teotigraphix.caustic.internal.sequencer;

import java.util.Map;

import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.sequencer.ITrigger;

public class PatternSequencerUtils
{

    public static IPhrase addEmptyPhrase(IPatternSequencer sequencer, int bank,
            int pattern)
    {
        return addPhrase((PatternSequencer) sequencer, bank, pattern, null);
    }

    /**
     * Adds a new phrase to the sequencer at the bank and pattern.
     * <p>
     * If the phrase exists, it will be returned.
     * 
     * @param bank The bank index.
     * @param pattern The pattern index.
     */
    static Phrase addPhrase(PatternSequencer sequencer, int bank, int pattern)
    {
        return addPhrase(sequencer, bank, pattern, null);
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
    static Phrase addPhrase(PatternSequencer sequencer, int bank, int pattern,
            String data)
    {
        Phrase phrase = null;

        Map<Integer, IPhrase> bankMap = sequencer.getBankMap(bank);
        if (bankMap.containsKey(pattern))
        {
            phrase = (Phrase) bankMap.get(pattern);
        }
        else
        {
            phrase = new Phrase(bank, pattern);
            bankMap.put(phrase.getIndex(), phrase);
            phraseAdd(sequencer, phrase);
        }

        if (data != null)
            phrase.setStringData(data);

        return phrase;
    }

    static Phrase removePhrase(PatternSequencer sequencer, int bank, int pattern)
    {
        Phrase phrase = (Phrase) sequencer.getBankMap(bank).remove(pattern);
        phraseRemove(sequencer, phrase);
        return phrase;
    }

    static void phraseAdd(PatternSequencer sequencer, IPhrase phrase)
    {
        phrase.setSequencer(sequencer);
        phrase.addPhraseListener(sequencer);
    }

    static private void phraseRemove(PatternSequencer sequencer, IPhrase phrase)
    {
        phrase.removePhraseListener(sequencer);
        phrase.setSequencer(null);
    }

}

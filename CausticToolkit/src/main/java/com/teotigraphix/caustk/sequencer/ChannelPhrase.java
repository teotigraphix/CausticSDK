
package com.teotigraphix.caustk.sequencer;

import java.util.UUID;

import com.teotigraphix.caustk.library.LibraryPhrase;

public class ChannelPhrase {

    private LibraryPhrase phrase;

    /**
     * Proxy to {@link ChannelPhrase#getId()}.
     */
    public UUID getId() {
        return phrase.getId();
    }

    /**
     * Proxy to {@link ChannelPhrase#getNoteData()}.
     */
    public String getNoteData() {
        return phrase.getNoteData();
    }

    /**
     * Proxy to {@link ChannelPhrase#getLength()}.
     */
    public int getLength() {
        return phrase.getLength();
    }

    /**
     * Proxy to {@link ChannelPhrase#getBankIndex()}.
     */
    public int getBankIndex() {
        return phrase.getBankIndex();
    }

    /**
     * Proxy to {@link ChannelPhrase#getPatternIndex()}.
     */
    public int getPatternIndex() {
        return phrase.getPatternIndex();
    }

    public ChannelPhrase() {
    }

    public ChannelPhrase(LibraryPhrase phraseCopy) {
        phrase = phraseCopy;
    }

    @Override
    public String toString() {
        return "[" + getBankIndex() + "," + getPatternIndex() + "]{" + getLength() + "} ";
    }
}

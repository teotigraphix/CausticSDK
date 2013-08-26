
package com.teotigraphix.caustk.sequencer;

import java.util.UUID;

public class ProxyChannelPhrase extends ChannelPhrase {

    private UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    private int bankIndex;

    @Override
    public int getBankIndex() {
        return bankIndex;
    }

    private int patternIndex;

    @Override
    public int getPatternIndex() {
        return patternIndex;
    }

    private int length;

    @Override
    public int getLength() {
        return length;
    }

    private String noteData;

    @Override
    public String getNoteData() {
        return noteData;
    }

    public ProxyChannelPhrase(UUID id, int bankIndex, int patternIndex, int length, String noteData) {
        this.id = id;
        this.bankIndex = bankIndex;
        this.patternIndex = patternIndex;
        this.length = length;
        this.noteData = noteData;
    }

}

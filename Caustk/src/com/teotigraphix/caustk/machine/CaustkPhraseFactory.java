
package com.teotigraphix.caustk.machine;

import java.util.UUID;

import com.teotigraphix.caustk.rack.IRack;

public class CaustkPhraseFactory {

    private IRack rack;

    public CaustkPhraseFactory(IRack rack) {
        this.rack = rack;
    }

    public CaustkPhrase createPhrase(MachineType machineType, int bankIndex, int patternIndex) {
        final int index = bankIndex * patternIndex;
        CaustkPhrase caustkPhrase = new CaustkPhrase(UUID.randomUUID(), index, machineType);
        return caustkPhrase;
    }

    public CaustkPhrase createPhrase(CaustkMachine caustkMachine, int bankIndex, int patternIndex) {
        final int index = bankIndex * patternIndex;
        CaustkPhrase caustkPhrase = new CaustkPhrase(UUID.randomUUID(), index, caustkMachine);
        return caustkPhrase;
    }
}

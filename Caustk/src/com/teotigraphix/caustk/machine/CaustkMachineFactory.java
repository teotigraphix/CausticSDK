
package com.teotigraphix.caustk.machine;

import java.util.UUID;

import com.teotigraphix.caustk.rack.IRack;

public class CaustkMachineFactory {

    private IRack rack;

    public CaustkMachineFactory(IRack rack) {
        this.rack = rack;
    }

    public CaustkMachine createMachine(MachineType machineType) {
        CaustkMachine caustkMachine = new CaustkMachine(UUID.randomUUID(), machineType);
        return caustkMachine;
    }

    public CaustkMachine createMachine(MachineType machineType, int index, String machineName) {
        CaustkMachine caustkMachine = new CaustkMachine(UUID.randomUUID(), machineType, index,
                machineName);
        return caustkMachine;
    }
}

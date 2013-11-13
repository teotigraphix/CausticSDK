
package com.teotigraphix.caustk.workstation;

public interface IGrooveFactory extends ICaustkFactory {

    GrooveSet createGrooveSet(ComponentInfo info, RackSet rackSet);

    GrooveMachine createGrooveMachine(GrooveMachineDescriptor descriptor);

}

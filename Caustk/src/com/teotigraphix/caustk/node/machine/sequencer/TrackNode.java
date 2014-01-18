
package com.teotigraphix.caustk.node.machine.sequencer;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class TrackNode extends NodeBase {

    private int machineIndex;

    public int getMachineIndex() {
        return machineIndex;
    }

    // measure
    private Map<Integer, PatternNode> patterns = new HashMap<Integer, PatternNode>();

    public PatternNode getPattern(int measure) {
        return patterns.get(measure);
    }

    public TrackNode() {
    }

    public TrackNode(int machineIndex) {
        this.machineIndex = machineIndex;
    }

    public TrackNode(MachineNode machineNode) {
        this(machineNode.getIndex());
    }

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void restoreComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateComponents() {
        // TODO Auto-generated method stub

    }

}

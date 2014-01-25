////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.machine.sequencer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * A {@link TrackNode} represents a track in the song sequencer of a
 * {@link MachineNode}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class TrackNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private Map<Integer, TrackEntryNode> entries = new HashMap<Integer, TrackEntryNode>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the machine index.
     */
    @Override
    public Integer getIndex() {
        return index;
    }

    //----------------------------------
    // entries
    //----------------------------------

    /**
     * Returns the number of {@link TrackEntryNode} that exist this
     * {@link TrackNode}.
     */
    public int getNumEntries() {
        return entries.size();
    }

    /**
     * Returns the {@link TrackEntryNode} at the specified measure or
     * <code>null</code> if dosn't exist, this index is 0 index.
     * <p>
     * The first measure starts a 0 ends at 1.
     * 
     * @param measure The entry's startMeasure.
     */
    public TrackEntryNode getEntry(int measure) {
        return entries.get(measure);
    }

    /**
     * Adds a {@link PatternNode} as an entry in the track.
     * <p>
     * The {@link PatternNode} is not actually added to this instance, its
     * values are used to add a {@link TrackEntryNode}.
     * 
     * @param patternNode The {@link PatternNode} for values.
     * @param startMeasure The start measure of the entry.
     * @param endMeasure The end measure of the entry.
     * @return The added {@link TrackEntryNode}.
     * @throws CausticException Track entry exists at measure
     */
    public TrackEntryNode addPattern(PatternNode patternNode, int startMeasure, int endMeasure)
            throws CausticException {
        if (entries.containsKey(startMeasure))
            throw new CausticException("Track entry exists at measure: " + startMeasure);

        TrackEntryNode trackEntryNode = new TrackEntryNode(patternNode.getIndex(),
                patternNode.getName(), startMeasure, endMeasure);
        entries.put(startMeasure, trackEntryNode);

        SequencerMessage.PATTERN_EVENT.send(getRack(), trackEntryNode.getIndex(),
                trackEntryNode.getStartMeasure(), trackEntryNode.getBankIndex(),
                trackEntryNode.getPatternIndex(), trackEntryNode.getEndMeasure());

        return trackEntryNode;
    }

    /**
     * Removes a {@link TrackEntryNode} from the track.
     * 
     * @param trackEntryNode The {@link TrackEntryNode} to remove.
     * @return The removed {@link TrackEntryNode} or <code>null</code> if
     *         nothing was removed.
     */
    public TrackEntryNode removeEntry(TrackEntryNode trackEntryNode) {
        return removeEntry(trackEntryNode.getStartMeasure());
    }

    /**
     * Removes a {@link TrackEntryNode} from the track at the specified start
     * measure.
     * 
     * @param startMeasure The track entry start measure.
     * @return The removed {@link TrackEntryNode} or <code>null</code> if
     *         nothing was removed.
     */
    public TrackEntryNode removeEntry(int startMeasure) {
        TrackEntryNode trackEntryNode = entries.remove(startMeasure);
        if (trackEntryNode == null)
            return null;
        SequencerMessage.PATTERN_EVENT_REMOVE.send(getRack(), trackEntryNode.getIndex(),
                trackEntryNode.getStartMeasure(), trackEntryNode.getEndMeasure());
        return trackEntryNode;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public TrackNode() {
    }

    public TrackNode(int machineIndex) {
        this.index = machineIndex;
    }

    public TrackNode(MachineNode machineNode) {
        this(machineNode.getIndex());
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
    }

    @Override
    protected void restoreComponents() {
        // create TrackEntryNodes for nodes retured.

        List<String> patterns = PatternUtils.getPatterns(getRack(), index);
        for (String pattern : patterns) {
            String[] split = pattern.split(" ");
            int startMeasure = Integer.valueOf(split[1]);
            int bankIndex = Integer.valueOf(split[2]);
            int patternIndex = Integer.valueOf(split[3]);
            int endMeasure = Integer.valueOf(split[4]);

            TrackEntryNode trackEntryNode = new TrackEntryNode(index, PatternUtils.toString(
                    bankIndex, patternIndex), startMeasure, endMeasure);
            entries.put(startMeasure, trackEntryNode);
        }
    }
}

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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * A {@link TrackComponent} represents a track in the song sequencer of a
 * {@link MachineNode}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class TrackComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private final TreeMap<Integer, TrackEntryNode> entries = new TreeMap<Integer, TrackEntryNode>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // entries
    //----------------------------------

    /**
     * Returns the number of {@link TrackEntryNode} that exist this
     * {@link TrackComponent}.
     */
    public int size() {
        return entries.size();
    }

    /**
     * Returns <code>true</code> if the {@link TrackEntryNode} can be moved to
     * the new start and end measure.
     * <p>
     * Note; If the new start and end measures are the same as the entry's start
     * and end, the check will return <code>false</code> as no move is required.
     * 
     * @param trackEntry The {@link TrackEntryNode} to test for move.
     * @param startMeasure The new start measure.
     * @param endMeasure The old start measure.
     */
    public boolean canMove(TrackEntryNode trackEntry, int startMeasure, int endMeasure) {
        // no move, same positions
        if (trackEntry.getStartMeasure() == startMeasure
                && trackEntry.getEndMeasure() == endMeasure)
            return false;

        // startMeasure valid, endMeasure 'inside' trackEntry; move left
        if (!isContained(startMeasure) && trackEntry.isContained(endMeasure)) {
            return true;
        }

        // end valid OR the end is a start of another entry, start inside self; move right
        if ((!isContained(endMeasure) || containsStart(endMeasure))
                && trackEntry.isContained(startMeasure)) {
            return true;
        }

        return isSpanValid(startMeasure, endMeasure);
    }

    public boolean canDrop(int startMeasure, int endMeasure) {
        return isSpanValid(startMeasure, endMeasure);
    }

    public boolean canTrimFromLeft(TrackEntryNode trackEntry, int newStartMeasure) {
        if (newStartMeasure < 0)
            return false;
        if (newStartMeasure == trackEntry.getStartMeasure())
            return false;
        if (newStartMeasure >= trackEntry.getEndMeasure())
            return false;
        if (newStartMeasure < TrackNodeUtils.nearestEnd(this, trackEntry))
            return false;

        return true;
    }

    public boolean canTrimFromRight(TrackEntryNode trackEntry, int newEndMeasure) {
        if (newEndMeasure == trackEntry.getEndMeasure())
            return false;
        if (newEndMeasure <= trackEntry.getStartMeasure())
            return false;
        final int nearestStart = TrackNodeUtils.nearestStart(this, trackEntry);
        if (nearestStart == -1)
            return true;
        if (newEndMeasure > nearestStart)
            return false;

        return true;
    }

    public boolean containsStart(int measure) {
        return entries.containsKey(measure);
    }

    /**
     * Returns whether the measure is not a start measure of an entry in this
     * track.
     * 
     * @param measure The measure to test for start.
     */
    public boolean isStartValid(int measure) {
        return !entries.containsKey(measure);
    }

    /**
     * Returns whether this measure is contained within an entry in this track.
     * <p>
     * A measure is considered contained if it cannot without error be added to
     * this track without overwriting another entry.
     * <p>
     * The last measure in an entry is considered the entry's endMeasure - 1.
     * The entry's endMeasure can be the start of a new track entry span.
     * 
     * @param measure The measure to test for containment.
     */
    public boolean isContained(int measure) {
        for (TrackEntryNode entry : entries.values()) {
            if (entry.isContained(measure))
                return true;
        }
        return false;
    }

    /**
     * Checks whether a start to end measure span is empty in this track.
     * 
     * @param startMeasure The start measure to test.
     * @param endMeasure The end measure to test.
     * @return
     */
    public boolean isSpanValid(int startMeasure, int endMeasure) {
        if (!isStartValid(startMeasure))
            return false;
        // check whether the start is contained in a entry span
        if (isContained(startMeasure))
            return false;
        for (int i = startMeasure; i < endMeasure; i++) {
            if (!isStartValid(i))
                return false;
        }
        return true;
    }

    /**
     * Returns an unmodifiable Map of {@link TrackEntryNode}s.
     */
    public Map<Integer, TrackEntryNode> getEntries() {
        return Collections.unmodifiableMap(entries);
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
     * @throws CausticException Track entry span invalid, measures exist
     */
    public TrackEntryNode addEntry(PatternNode patternNode, int startMeasure, int endMeasure)
            throws CausticException {
        if (!isStartValid(startMeasure))
            throw new CausticException("Track entry exists at measure: " + startMeasure);
        if (!isSpanValid(startMeasure, endMeasure))
            throw new CausticException("Track entry span invalid, measures exist: " + startMeasure
                    + ", " + endMeasure);

        TrackEntryNode trackEntryNode = new TrackEntryNode(getMachineNode(), patternNode.getName(),
                patternNode.getNumMeasures(), startMeasure, endMeasure);

        addEntry(trackEntryNode);

        return trackEntryNode;
    }

    /**
     * Adds an existing {@link TrackEntryNode} to the {@link TrackComponent}.
     * This may happen during an undo operation, where the track entry existed
     * in this track prior.
     * 
     * @param trackEntry The track entry to add.
     */
    public void addEntry(TrackEntryNode trackEntry) {
        if (!isStartValid(trackEntry.getStartMeasure()))
            throw new IllegalStateException("TrackEntryNode already exists");

        entries.put(trackEntry.getStartMeasure(), trackEntry);

        SequencerMessage.PATTERN_EVENT.send(getRack(), trackEntry.getMachineIndex(),
                trackEntry.getStartMeasure(), trackEntry.getBankIndex(),
                trackEntry.getPatternIndex(), trackEntry.getEndMeasure());
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
        SequencerMessage.PATTERN_EVENT_REMOVE.send(getRack(), trackEntryNode.getMachineIndex(),
                trackEntryNode.getStartMeasure(), trackEntryNode.getEndMeasure());
        return trackEntryNode;
    }

    public void moveEntry(TrackEntryNode trackEntry, int newStartMeausre) {
        // remove first
        removeEntry(trackEntry);
        trackEntry.move(newStartMeausre);
        // re-add at new position
        addEntry(trackEntry);
    }

    public void trimEntry(TrackEntryNode trackEntry, int startMeasure, int endMeasure) {
        // remove first
        removeEntry(trackEntry);
        trackEntry.setPosition(startMeasure, endMeasure);
        // re-add at new position
        addEntry(trackEntry);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    protected TrackComponent() {
    }

    public TrackComponent(MachineNode machineNode) {
        super(machineNode);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * @param trackEntryNode
     * @param startMeasure
     * @param endMeasure
     * @throws CausticException Entry does not exist for measure
     */
    public void setPositon(TrackEntryNode trackEntryNode, int startMeasure, int endMeasure)
            throws CausticException {
        TrackEntryNode entry = getEntry(startMeasure);
        if (entry == null)
            throw new CausticException("Entry does not exist for measure: " + startMeasure);

        // XXX implement range and previous, next measure shrink/move
        entry.setPosition(startMeasure, endMeasure);
    }

    /**
     * Returns the append measure plus it's entries measure span.
     * <p>
     * The append measure is the last measure found in the track. The value
     * returned is the start measure plus the {@link TrackEntryNode}'s measure
     * span. This value will give the correct placement to append a new entry.
     */
    public int getAppendMeasure() {
        int appendMeasure = 0;
        TrackEntryNode entry = getLastEntry();
        if (entry != null) {
            appendMeasure = entry.getNextMeasure();
        }
        return appendMeasure;
    }

    /**
     * Returns the last entry in the track, can be <code>null</code> if no
     * entries exist.
     */
    public TrackEntryNode getLastEntry() {
        int startMeasure = 0;
        TrackEntryNode result = getEntry(startMeasure);
        for (TrackEntryNode trackEntryNode : entries.values()) {
            if (trackEntryNode.getStartMeasure() > startMeasure) {
                startMeasure = trackEntryNode.getStartMeasure();
                result = trackEntryNode;
            }
        }
        return result;
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

        int machineIndex = getMachineIndex();
        List<String> patterns = PatternUtils.getPatterns(getRack(), machineIndex);
        for (String pattern : patterns) {
            String[] split = pattern.split(" ");
            int startMeasure = Integer.valueOf(split[1]);
            int bankIndex = Integer.valueOf(split[2]);
            int patternIndex = Integer.valueOf(split[3]);
            int endMeasure = Integer.valueOf(split[4]);

            int numMeasures = PatternUtils.getNumMeasures(getRack(), machineIndex, bankIndex,
                    patternIndex);
            TrackEntryNode trackEntryNode = new TrackEntryNode(getMachineNode(),
                    PatternUtils.toString(bankIndex, patternIndex), numMeasures, startMeasure,
                    endMeasure);
            entries.put(startMeasure, trackEntryNode);
        }
    }

}

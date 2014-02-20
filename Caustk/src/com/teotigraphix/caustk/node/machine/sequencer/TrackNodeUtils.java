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

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public final class TrackNodeUtils {

    public static int nearestStart(TrackNode track, TrackEntryNode trackEntry) {
        // all entries have their start measure greater than the entries end
        ArrayList<TrackEntryNode> tests = new ArrayList<TrackEntryNode>(track.getEntries().values());
        Collections.reverse(tests);

        TrackEntryNode lastEntry = null;
        for (TrackEntryNode testEntry : tests) {
            if (testEntry.getStartMeasure() > trackEntry.getEndMeasure() && testEntry != trackEntry)
                lastEntry = testEntry;
            else if (lastEntry == null)
                return -1;
            else
                break;
        }

        return lastEntry.getStartMeasure();
    }

    public static int nearestEnd(TrackNode track, TrackEntryNode trackEntry) {
        // all entries have their end measure less than the entries start
        TrackEntryNode lastEntry = null;
        for (TrackEntryNode testEntry : track.getEntries().values()) {
            if (testEntry.getEndMeasure() < trackEntry.getStartMeasure() && testEntry != trackEntry)
                lastEntry = testEntry;
            else if (lastEntry == null)
                return 0;
            else
                break;
        }

        return lastEntry.getEndMeasure();
    }
}

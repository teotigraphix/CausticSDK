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

package com.teotigraphix.caustk.groove.importer;

import java.util.ArrayList;
import java.util.Collection;

import com.teotigraphix.caustk.groove.library.LibraryPatternBank;
import com.teotigraphix.caustk.groove.library.LibraryProductAccess;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;

public class CausticPatternBank extends CausticItem {

    private transient Collection<PatternNode> patterns = new ArrayList<PatternNode>();

    /**
     * Returns the patterns for this bank, only populated when deserialized with
     * {@link LibraryProductAccess#getSounds()}.
     */
    public Collection<PatternNode> getPatterns() {
        return patterns;
    }

    public void setPatterns(Collection<PatternNode> patterns) {
        this.patterns = patterns;
    }

    public CausticPatternBank(LibraryPatternBank item) {
        super(item);
    }

}

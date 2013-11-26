////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.workstation;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class PatternReference {

    private PatternBank patternBank;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private int index;

    @Tag(2)
    private UUID patternBankId;

    public ComponentInfo getInfo() {
        return info;
    }

    void setInfo(ComponentInfo value) {
        this.info = value;
    }

    public int getIndex() {
        return index;
    }

    public UUID getPatternBankId() {
        return patternBankId;
    }

    public boolean hasPattern() {
        return info != null;
    }

    public PatternBank getPatternBank() {
        return patternBank;
    }

    public String getName() {
        return PatternUtils.toString(index);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    PatternReference() {
    }

    public PatternReference(int index, PatternBank patternBank) {
        this.index = index;
        this.patternBank = patternBank;
        this.patternBankId = patternBank.getInfo().getId();
    }

}

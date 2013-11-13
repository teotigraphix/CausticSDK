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

/**
 * Application specific enums will implemented this when creating
 * {@link PatternBank}s, to allow for a distinction when loading sets between
 * various applications.
 * <p>
 * Some sets might work in different applications since their part types match,
 * others will not since there will be a completely different {@link Part} setup
 * for the {@link Pattern}.
 * 
 * @author Michael Schmalle
 */
public interface IPatternType {

    /**
     * Returns the unique {@link Pattern} type as a String.
     */
    String getType();

    /**
     * Determines whether the passed type is compatible with this type.
     * 
     * @param type The {@link Pattern} type.
     */
    boolean hasType(IPatternType type);
}

////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.rack;

import android.content.Context;

import com.teotigraphix.caustic.internal.core.CausticEngine;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRackFactory;

/**
 * {@link Rack} utility class.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class RackUtils {
    //private static final String UNTITLED_SONG_NAME = "Untitled";

    //public static IRack createRack() {
    //    IRack rack = createRack(new RackFactory(), UNTITLED_SONG_NAME, null);
    //    return rack;
    //}

    public static IRack createRack(Context context, int key, IRackFactory factory) {
        Rack rack = new Rack(factory);
        rack.setEngine(new CausticEngine(context, key));
        return rack;
    }

    //    public static IRack createRack(IRackFactory factory, String songName, String songPath) {
    //        Rack rack = new Rack(factory);
    //        rack.setEngine(new CausticEngine());
    //        try {
    //            rack.createSong(songName, songPath);
    //        } catch (CausticException e) {
    //            // the default song will never throw an error
    //        }
    //        return rack;
    //    }

    public static void setFactory(IRack rack, IRackFactory factory) {
        ((Rack)rack).setFactory(factory);
    }
}

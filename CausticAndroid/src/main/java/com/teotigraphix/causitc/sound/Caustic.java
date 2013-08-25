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

package com.teotigraphix.causitc.sound;

import android.util.Log;

import com.singlecellsoftware.causticcore.CausticCore;
import com.teotigraphix.caustk.core.CausticEventListener;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Caustic extends CausticCore {

    private static final String TAG = "Caustic";

    private boolean mDebug;

    public boolean getDebug() {
        return mDebug;
    }

    public void setDebug(boolean value) {
        mDebug = value;
    }

    public Caustic() {
        super();
    }

    public float SendOSCMessage(String msg) {
        if (!mDebug) {
            Log.d(TAG, msg);
            return super.SendOSCMessage(msg);
        } else {
            System.out.println(msg);
        }
        return Float.NaN;
    }

    @Override
    public String QueryOSC(String msg) {
        if (!mDebug) {
            return super.QueryOSC(msg);
        } else {
            return "";
        }
    }

    public void RemoveEventListener(CausticEventListener l) {
        // TODO Auto-generated method stub

    }

}

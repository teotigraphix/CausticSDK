////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.air;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.teotigraphix.caustic.internal.core.Caustic;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class CausticExtension implements FREExtension {

    public static final String TAG = "CausticExtension";

    protected CausticExtensionContext mContext;

    protected Caustic mCausticCore;

    @Override
    public FREContext createContext(String name) {
        Log.d(TAG, "createContext()");
        mContext = new CausticExtensionContext();
        mContext.setCausticCore(mCausticCore);
        return mContext;
    }

    @Override
    public void initialize() {
        Log.d(TAG, "initialize()");
        mCausticCore = new Caustic();
    }

    @Override
    public void dispose() {
        Log.d(TAG, "dispose()");
        mContext.dispose();
        mContext = null;
        mCausticCore.Stop();
        mCausticCore = null;
    }

}

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

package com.teotigraphix.caustic.air.functions;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.singlecellsoftware.causticcore.CausticCore;
import com.teotigraphix.caustic.air.CausticExtensionContext;
import com.teotigraphix.caustic.air.utils.MessageUtils;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SendMessage implements FREFunction {

    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        CausticExtensionContext c = (CausticExtensionContext)context;
        CausticCore engine = c.getCausticCore();

        String message = MessageUtils.getString(args[0]);
        Log.d("SendMessage", message);

        float result = engine.SendOSCMessage(message);
        if (!Float.isNaN(result)) {
            try {
                //Log.d("SendMessage return", Float.toString(result));
                return FREObject.newObject(result);
            } catch (FREWrongThreadException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

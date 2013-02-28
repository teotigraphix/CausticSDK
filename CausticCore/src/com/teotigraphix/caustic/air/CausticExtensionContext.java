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

import java.util.HashMap;
import java.util.Map;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.teotigraphix.caustic.air.functions.ActivateMessage;
import com.teotigraphix.caustic.air.functions.DeactivateMessage;
import com.teotigraphix.caustic.air.functions.InitializeFunction;
import com.teotigraphix.caustic.air.functions.LogFunction;
import com.teotigraphix.caustic.air.functions.QueryMessage;
import com.teotigraphix.caustic.air.functions.SendMessage;
import com.teotigraphix.caustic.internal.core.Caustic;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class CausticExtensionContext extends FREContext {

    private static final String LOG = "log";

    private static final String INITIALIZE = "initialize";

    private static final String ACTIVATE = "activate";

    private static final String DEACTIVATE = "deactivate";

    private static final String SEND_MESSAGE = "sendMessage";

    private static final String QUERY_MESSAGE = "queryMessage";

    protected HashMap<String, FREFunction> functions;

    protected Caustic mCausticCore;

    public Caustic getCausticCore() {
        return mCausticCore;
    }

    public void setCausticCore(Caustic value) {
        mCausticCore = value;
    }

    @Override
    public void dispose() {
        functions = null;
    }

    @Override
    public Map<String, FREFunction> getFunctions() {
        functions = new HashMap<String, FREFunction>();
        functions.put(INITIALIZE, new InitializeFunction());
        functions.put(ACTIVATE, new ActivateMessage());
        functions.put(DEACTIVATE, new DeactivateMessage());
        functions.put(SEND_MESSAGE, new SendMessage());
        functions.put(QUERY_MESSAGE, new QueryMessage());
        functions.put(LOG, new LogFunction());
        return functions;
    }

    public void start() {
        mCausticCore.Start();
    }

    public void resume() {
        mCausticCore.Resume();
    }

    public void pause() {
        mCausticCore.Pause();
    }

    public void stop() {
        mCausticCore.Stop();
    }

}

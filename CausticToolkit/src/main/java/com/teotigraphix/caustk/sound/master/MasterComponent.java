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

package com.teotigraphix.caustk.sound.master;

import java.io.Serializable;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public class MasterComponent implements Serializable, IRestore {

    private static final long serialVersionUID = 4281646159472145003L;

    private transient ICaustkController controller;

    public void setController(ICaustkController controller) {
        this.controller = controller;
    }

    protected ICausticEngine getEngine() {
        return controller;
    }

    protected transient CausticMessage bypassMessage;

    //----------------------------------
    // bypass
    //----------------------------------

    private boolean bypass = false;

    public boolean isBypass() {
        return bypass;
    }

    boolean isBypass(boolean restore) {
        return bypassMessage.query(getEngine()) == 1 ? true : false;
    }

    public void setBypass(boolean value) {
        if (bypass == value)
            return;
        bypass = value;
        bypassMessage.send(getEngine(), value ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterComponent() {
    }

    public MasterComponent(ICaustkController controller) {
        this.controller = controller;
    }

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param control The OSC control involved.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }

    @Override
    public void restore() {
        setBypass(isBypass(true));
    }
}

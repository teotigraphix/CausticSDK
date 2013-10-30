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

package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkFactory;
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.rack.IRack;

/**
 * @author Michael Schmalle
 */
public class RackContext implements IRackContext {

    private ICaustkApplication application;

    public RackContext(ICaustkApplication application) {
        this.application = application;
    }

    @Override
    public ICaustkFactory getFactory() {
        return application.getFactory();
    }

    @Override
    public IRack getRack() {
        return application.getRack();
    }

}

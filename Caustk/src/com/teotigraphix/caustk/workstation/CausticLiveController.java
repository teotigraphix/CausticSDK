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

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;

/*
 * - New LiveSet
 * - Open LiveSet
 * - Close LiveSet
 */

/**
 * @author Michael Schmalle
 */
public class CausticLiveController {

    @SuppressWarnings("unused")
    private ICaustkController controller;

    private Library library;

    private LiveSet liveSet;

    public LiveSet getLiveSet() {
        return liveSet;
    }

    //----------------------------------
    // library
    //----------------------------------

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library value) {
        library = value;
    }

    public CausticLiveController(ICaustkController controller) {
        this.controller = controller;
    }

    public LiveSet newLiveSet(String name) {
        //        ComponentInfo info = controller.getFactory().createInfo(ComponentType.LiveSet, "Untitled");
        //        LiveSet liveSet = controller.getFactory().createLiveSet(info);
        //        return liveSet;
        return null;
    }

    public LiveSet openLiveSet(File file) throws IOException {
        return null;
    }

    public void closeLiveSet(LiveSet liveSet) throws CausticException {

    }
}

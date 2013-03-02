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

package com.teotigraphix.caustic.application;

import com.google.inject.Binder;
import com.teotigraphix.android.internal.service.TouchService;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.internal.actvity.CausticModule;
import com.teotigraphix.caustic.internal.application.ApplicationModel;
import com.teotigraphix.caustic.internal.controller.application.ApplicationController;
import com.teotigraphix.caustic.internal.router.Router;
import com.teotigraphix.caustic.router.IRouter;

public class CausticUIModule extends CausticModule {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(ITouchService.class).to(TouchService.class);
        binder.bind(IRouter.class).to(Router.class);
        // overridable
        binder.bind(IApplicationModel.class).to(ApplicationModel.class);
        binder.bind(IApplicationController.class).to(ApplicationController.class);
        //binder.bind(IApplicationPreferences.class).to(ApplicationPreferences.class);
    }

}

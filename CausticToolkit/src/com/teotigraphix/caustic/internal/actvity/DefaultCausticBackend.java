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

package com.teotigraphix.caustic.internal.actvity;

import com.teotigraphix.caustic.activity.ICausticBackend;
import com.teotigraphix.caustic.internal.part.PartRackFactory;
import com.teotigraphix.caustic.internal.part.SoundGenerator;
import com.teotigraphix.caustic.internal.rack.RackUtils;
import com.teotigraphix.caustic.internal.song.ProjectData;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.song.IWorkspace;

public abstract class DefaultCausticBackend implements ICausticBackend {

    @Override
    public IRack createRack(IWorkspace workpace) {
        return RackUtils.createRack(workpace.getApplication(), returnCausticCoreKey(),
                new PartRackFactory());
    }

    @Override
    public ISoundGenerator createSoundGenerator(IWorkspace workpace) {
        return new SoundGenerator(workpace);
    }

    @Override
    public ProjectData createProjectData() {
        return new ProjectData();
    }

}

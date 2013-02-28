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

package com.teotigraphix.caustic.internal.song;

import com.teotigraphix.caustic.internal.song.Project.StartupInfo;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class ProjectData implements IPersist {

    private static final String ATT_VERSION = "version";

    private boolean mIsNewProject = false;

    /**
     * Whether this session is the first run of a project.
     * <p>
     * This property will be true as long as the project has not been loaded
     * from disk.
     * </p>
     * 
     * @return
     */
    public final boolean isNewProject() {
        return mIsNewProject;
    }

    public final void setIsNewProject() {
        if (mSavedProject)
            return;
        mIsNewProject = true;
    }

    private float mOldVersion;

    private StartupInfo mInfo;

    private boolean mSavedProject = false;

    public float getVersion() {
        return mOldVersion;
    }

    public void setVersion(float version) {
        mOldVersion = version;
    }

    public ProjectData() {
    }

    @Override
    public void copy(IMemento memento) {
        if (mInfo != null)
            memento.putFloat(ATT_VERSION, mInfo.getVersion());

    }

    @Override
    public void paste(IMemento memento) {
        setVersion(memento.getFloat(ATT_VERSION));

    }

    public StartupInfo getInfo() {
        return mInfo;
    }

    public void setInfo(StartupInfo mInfo) {
        this.mInfo = mInfo;
    }

    public void setIsSavedProject() {
        mIsNewProject = false;
        mSavedProject = true;
    }

    public final boolean isSavedProject() {
        return mSavedProject;
    }

}

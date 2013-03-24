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

package com.teotigraphix.caustic.activity;

import android.content.Context;

import com.teotigraphix.caustic.service.IFileService;
import com.teotigraphix.caustic.song.IWorkspace;

public interface IApplicationConfiguration {

    float getVersion();

    int getVersionMajor();

    int getVersionMinor();

    int getVersionRevision();

    boolean isTestMode();

    void setTestMode();

    /**
     * Returns the application's directory name used with the
     * {@link IFileService}.
     */
    String getApplicationName();

    IFileService createFileService(Context context);

    IApplicationRuntime createRuntime(IWorkspace workspace);

    /**
     * Creates the backend factory that is responsible for making instances the
     * {@link IWorkspace} and sub components will use during the application.
     */
    ICausticBackend createBackend();
}

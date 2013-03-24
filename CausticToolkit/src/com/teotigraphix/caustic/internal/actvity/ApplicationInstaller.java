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

import java.io.IOException;

import com.teotigraphix.caustic.activity.IApplicationRuntime;
import com.teotigraphix.caustic.core.CausticException;

public class ApplicationInstaller {

    private final IApplicationRuntime mRuntime;

    public ApplicationInstaller(IApplicationRuntime runtime) {
        mRuntime = runtime;
    }

    public void setup() throws CausticException {
        try {
            install();
        } catch (CausticException e) {
            throw e;
        }

        try {
            boot();
        } catch (IOException e) {
            throw new CausticException("IOException in ApplicationInstaller.boot()", e);
        }

        run();
    }

    protected void install() throws CausticException {
        // will install on first run or call update in the installer
        // if the application has already been installed
        try {
            mRuntime.install();
        } catch (IOException e) {
            throw new CausticException("Exception when installing", e);
        }
    }

    protected void boot() throws IOException {
        mRuntime.boot();
    }

    protected void run() {
        mRuntime.run();
    }
}

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

package com.teotigraphix.gdx.app;

import com.teotigraphix.gdx.app.internal.ApplicationComponentRegistery;


/**
 * A component that can be registered with the application.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IApplicationComponent {

    /**
     * Returns the {@link IApplication} instance.
     */
    IApplication getApplication();

    /**
     * Called when attached to the {@link ApplicationComponentRegistery}.
     * <p>
     * The {@link #getApplication()} instance is guaranteed to be non
     * <code>null</code>.
     */
    void onAwake();

    /**
     * Called when detached from the {@link ApplicationComponentRegistery}.
     * <p>
     * The {@link #getApplication()} instance is set to <code>null</code> after
     * this method returns.
     */
    void onDestroy();
}

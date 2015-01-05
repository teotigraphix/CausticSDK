////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.core;

import com.teotigraphix.caustk.core.factory.LibraryFactory;
import com.teotigraphix.caustk.core.factory.NodeFactory;

import java.io.File;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public interface ICaustkFactory {

    /**
     * The {@link CaustkRuntime} that owns this factory.
     */
    ICaustkRuntime getRuntime();

    NodeFactory getNodeFactory();

    LibraryFactory getLibraryFactory();

    /**
     * Returns a sub directory in the application's <code>cache</code>
     * directory.
     * 
     * @param reletivePath The path within the cache directory.
     * @throws java.io.IOException
     */
    File getCacheDirectory(String relativePath);

    void initialize() throws CausticException;

}

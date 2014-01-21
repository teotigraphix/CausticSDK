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

package com.teotigraphix.caustk.node;

import java.io.File;

/**
 * Factory to create a {@link Library}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryFactory extends NodeFactoryBase {

    LibraryFactory(CaustkFactory factory) {
        super(factory);
    }

    public Library createLibrary(File reletiveDirectory) {
        NodeInfo info = getFactory().createInfo(NodeType.Library, reletiveDirectory.getName());
        Library caustkLibrary = new Library(info, getFactory(), reletiveDirectory);
        return caustkLibrary;
    }

    public Library createLibrary(String name) {
        NodeInfo info = getFactory().createInfo(NodeType.Library, name);
        Library caustkLibrary = new Library(info, getFactory());
        return caustkLibrary;
    }

    //    public Library loadLibrary(String name) throws IOException {
    //        Library library = createLibrary(name);
    //        library = getFactory().load(library.getManifestFile(), Library.class);
    //        library.setFactory(getFactory());
    //        return library;
    //    }

}

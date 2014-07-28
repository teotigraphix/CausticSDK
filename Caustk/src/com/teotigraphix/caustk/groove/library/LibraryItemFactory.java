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

package com.teotigraphix.caustk.groove.library;

import java.io.File;

import com.teotigraphix.caustk.core.CaustkFactory;
import com.teotigraphix.caustk.core.factory.CaustkFactoryChildBase;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.node.effect.EffectNode;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryItemFactory extends CaustkFactoryChildBase {

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    public LibraryItemFactory(CaustkFactory factory) {
        super(factory);
    }

    //----------------------------------
    // LibraryManifest
    //----------------------------------

    public LibraryEffect createLibraryEffect(LibraryProduct product, String displayName,
            File archiveFile, String relativePath, EffectNode efffect0, EffectNode efffect1) {

        LibraryEffectManifest manifest = new LibraryEffectManifest(displayName, archiveFile,
                relativePath, efffect0, efffect1);

        LibraryEffect libraryEffect = new LibraryEffect(product.getId(), manifest);
        libraryEffect.add(0, efffect0);
        libraryEffect.add(1, efffect1);

        return libraryEffect;
    }

    //----------------------------------
    // LibraryEffect
    //----------------------------------

}

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

package com.teotigraphix.caustk.core.factory;

import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryPatternBank;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryGroupManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryInstrumentManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryPatternBankManifest;
import com.teotigraphix.caustk.groove.manifest.LibrarySoundManifest;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryFactory extends CaustkFactoryChildBase {

    public LibraryFactory(CaustkFactory factory) {
        super(factory);
    }

    public LibraryEffect createEffect(LibraryProduct product, String name, String relativePath,
            EffectNode efffect0, EffectNode efffect1) {
        LibraryEffectManifest manifest = new LibraryEffectManifest(name, relativePath, efffect0,
                efffect1);
        manifest.setProductId(product.getId());
        LibraryEffect libraryEffect = new LibraryEffect(manifest);

        if (efffect0 != null) {
            libraryEffect.add(0, efffect0);
        }
        if (efffect1 != null) {
            libraryEffect.add(1, efffect1);
        }

        return libraryEffect;
    }

    public LibraryInstrument createInstrument(LibraryProduct product, String name,
            String relativePath, MachineNode machineNode) {
        LibraryInstrumentManifest manifest = new LibraryInstrumentManifest(name, relativePath,
                machineNode);
        manifest.setProductId(product.getId());
        LibraryInstrument instrument = new LibraryInstrument(manifest);
        instrument.setMachineNode(machineNode);
        return instrument;
    }

    public LibrarySound createSound(LibraryProduct product, int index, String name,
            String relativePath) {
        LibrarySoundManifest manifest = new LibrarySoundManifest(name, relativePath);
        manifest.setProductId(product.getId());
        LibrarySound librarySound = new LibrarySound(manifest, index);
        return librarySound;
    }

    public LibraryGroup createGroup(LibraryProduct product, String name, String relativePath) {
        LibraryGroupManifest manifest = new LibraryGroupManifest(name, relativePath);
        manifest.setProductId(product.getId());
        LibraryGroup libraryGroup = new LibraryGroup(manifest);
        return libraryGroup;
    }

    public LibraryPatternBank createPatternBank(LibraryProduct product, String name,
            String relativePath, MachineNode machineNode) {
        LibraryPatternBankManifest manifest = new LibraryPatternBankManifest(name, relativePath);
        manifest.setProductId(product.getId());
        LibraryPatternBank libraryPatternBank = new LibraryPatternBank(manifest, machineNode);
        return libraryPatternBank;
    }
}

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

package com.teotigraphix.caustk.groove.importer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkFactory;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryProduct;

/*
<group sourceFile="C:\Users\Teoti\Documents\caustic\songs\ALLEY 01.caustic" 
       displayName="Alley Drum Kit" 
       path="Kits">
  <sound displayName="Kick 1" index="0" path="Drum/Kick">
    <effect displayName="High Shelf Compression" path="Compressor"/>
  </sound>
  <sound displayName="Snare" index="1" path="Drum/Snare">
    <effect displayName="Limited Compression" path="Compressor"/>
  </sound>
  <sound displayName="ClosedHH" index="2" path="Drum/HiHat">
    <effect displayName="Crushed Reverb" path="Reverb"/>
  </sound>
  <sound displayName="Ride" index="3" path="Drum/Percussion">
    <effect displayName="High Flange" path="Flanger"/>
  </sound>
  <sound displayName="Kick 2" index="4" path="Drum/Kick">
    <effect displayName="Compressed Distortion" path="Compressor"/>
  </sound>
  <sound displayName="Clap 1" index="5" path="Drum/Clap">
    <effect displayName="High Shelf" path="EQ"/>
  </sound>
  <sound displayName="Shaker" index="6" path="Drum/Shaker">
    <effect displayName="Vinyl Delay" path="Delay"/>
  </sound>
  <sound displayName="Clap 2" index="7" path="Drum/Clap">
    <effect displayName="Limited Wah" path="Wah"/>
  </sound>
  <sound displayName="Tamb" index="8" path="Drum/Percussion">
    <effect displayName="Chorus Comber"  path="Chorus"/>
  </sound>
  <sound displayName="Shaker Roll" index="9" path="Drum/Percussion">
    <effect displayName="Low Cut 1" path="EQ"/>
  </sound>
  <sound displayName="Atmosphere" index="10" path="Pad">
    <effect displayName="Compressed Static Flange" path="Compressor"/>
  </sound>
  <sound displayName="Stab" index="11" path="Synth">
    <effect displayName="Low Cut Vinyl" path="EQ"/>
  </sound>
  <sound displayName="Bass 1" index="12" path="Bass">
    <effect displayName="Comb Delay 1" path="Delay"/>
  </sound>
  <sound displayName="Bass 2" index="13" path="Bass">
    <effect displayName="Basic Phaser" path="Phaser"/>
  </sound>
</group>
*/

/**
 * Imported from a .caustic file.
 */
public class CausticGroup extends CausticItem {

    private String name;

    private File sourceFile;

    private String displayName;

    Map<Integer, CausticSound> sounds = new HashMap<Integer, CausticSound>();

    public String getName() {
        return name;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<Integer, CausticSound> getSounds() {
        return sounds;
    }

    public CausticGroup(File sourceFile, String name, String displayName) {
        this.sourceFile = sourceFile;
        this.name = name;
        this.displayName = displayName;
    }

    public CausticSound addSound(CausticSound sound) {
        sounds.put(sound.getIndex(), sound);
        return sound;
    }

    public CausticSound addSound(int index, String soundName, String effectName) {
        CausticSound machine = new CausticSound(index, soundName, effectName);
        sounds.put(index, machine);
        return machine;
    }

    public LibraryGroup create(LibraryProduct product) {
        LibraryGroup libraryGroup = getFactory().createLibraryGroup(product, getDisplayName(),
                getPath());
        libraryGroup.setCausticGroup(this);
        return libraryGroup;
    }

    private ICaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }

    @Override
    public String toString() {
        return "CausticGroup [name=" + name + ", sourceFile=" + sourceFile + ", displayName="
                + displayName + ", sounds=" + sounds + "]";
    }

}

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

package com.teotigraphix.libgdx.model;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.machine.ComponentInfo;
import com.teotigraphix.caustk.machine.ComponentType;
import com.teotigraphix.caustk.machine.RackSet;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.libgdx.application.CausticSongFile;

/**
 * The serialized binary file root.
 * <p>
 * Each application creates a specific model used within it's
 * {@link ICaustkModel} implementations.
 * 
 * @author Michael Schmalle
 */
public abstract class ApplicationModelState {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private String id;

    @Tag(1)
    private CausticSongFile songFile;

    @Tag(2)
    private RackSet rackSet;

    //----------------------------------
    // controller
    //----------------------------------

    private transient ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;
    }

    //----------------------------------
    // id
    //----------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //----------------------------------
    // songFile
    //----------------------------------

    public CausticSongFile getSongFile() {
        return songFile;
    }

    public void setSongFile(CausticSongFile value) {
        songFile = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationModelState() {
        setId(getClass().getName());
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Called when the state has just been created due to a new {@link Project}
     * being initialized.
     * <p>
     * Create subclass state instances in this method.
     * <p>
     * The {@link #getController()} is guaranteed to be non <code>null</code> at
     * this point.
     * <p>
     * The super must be called in order to create the state {@link Rack}
     * instance.
     */
    public void create() {
        IRack rack = getController().getRack();
        // XXX don't know exactly where to put this but when a new Project
        // is created, when reiniting the state, the Rack needs to be wiped
        RackMessage.BLANKRACK.send(rack);
        ComponentInfo info = rack.getFactory()
                .createInfo(ComponentType.RackSet, "Untitled RackSet");
        rackSet = rack.getFactory().createScene(info);
        rackSet.setInternal();
        rack.setRackSet(rackSet);
    }

    /**
     * Called just after the state has been deserialized and the {@link Project}
     * has been reloaded from disk.
     * <p>
     * All subclass state instances exist and hold their previous serialized
     * state.
     * <p>
     * The {@link #getController()} and {@link #getRack()} is guaranteed to be
     * non <code>null</code> at this point.
     * <p>
     * The super must be called in order to assign the {@link ICaustkController}
     * to the {@link IRack} instance.
     */
    public void update() {
        getController().getRack().setRackSet(rackSet);
        // XXX This runs right before the first screen is created in GDX game
        // which is a good place to restore things because mediators are not
        // around yet, for now this fixes the "bpm" bug with the outputpanel
        // not returning the correct bpm right when a song is loaded
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                getController().getRack().restore();
            }
        });
    }

    /**
     * Called when the {@link ICaustkApplication#save()} is issued by the client
     * application.
     */
    public void save() {
    }

    public void registerObservers() {
    }

    public void dispose() {
        songFile.dispose();
        id = null;
        songFile = null;
    }
}

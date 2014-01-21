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

package com.teotigraphix.caustk.rack;

import java.io.File;
import java.util.Collection;

import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.caustk.core.internal.CaustkLogger;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class Rack implements ISoundGenerator {

    private transient CaustkLogger caustkLogger;

    private transient ISoundGenerator soundGenerator;

    private transient CaustkRuntime runtime;

    public final CaustkRuntime getRuntime() {
        return runtime;
    }

    final ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    /**
     * Set the soundGenerator after a {@link Rack} is deserialized from the
     * client.
     * 
     * @param soundGenerator The current {@link ISoundGenerator}.
     */
    void setSoundGenerator(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
    }

    Rack(CaustkRuntime runtime) {
        this.runtime = runtime;
        setSoundGenerator(runtime.getSoundGenerator());
        caustkLogger = new CaustkLogger();
    }

    //----------------------------------
    // rackNode
    //----------------------------------

    private RackNode rackNode;

    private void setRackNode(RackNode rackNode) {
        this.rackNode = rackNode;
    }

    public ICaustkLogger getLogger() {
        return caustkLogger;
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    public String getPath() {
        return rackNode.getPath();
    }

    public File getFile() {
        return rackNode.getAbsoluteFile();
    }

    public String getName() {
        return rackNode.getName();
    }

    public MasterDelayNode getDelay() {
        return rackNode.getMaster().getDelay();
    }

    public MasterReverbNode getReverb() {
        return rackNode.getMaster().getReverb();
    }

    public MasterEqualizerNode getEqualizer() {
        return rackNode.getMaster().getEqualizer();
    }

    public MasterLimiterNode getLimiter() {
        return rackNode.getMaster().getLimiter();
    }

    public MasterVolumeNode getVolume() {
        return rackNode.getMaster().getVolume();
    }

    /**
     * Returns an unmodifiable collection of {@link MachineNode}s.
     */
    public Collection<? extends MachineNode> getMachines() {
        return rackNode.getMachines();
    }

    public MachineNode getMachine(int machineIndex) {
        return rackNode.getMachine(machineIndex);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    /**
     * Takes the state of the {@link RackNode} and applies it to the
     * {@link Rack} by creating machines and updating all native rack state
     * based on the node graph.
     * <p>
     * The {@link Rack} is reset, native rack cleared and all machines are
     * created through OSC and updated through OSC in the node graph.
     * 
     * @param rackNode The new rack node state.
     */
    public void create(RackNode rackNode) {
        // first clear all state from the native rack
        reset();

        // will take the existing state of the node and update the native rack
        // must create machines with CREATE here
        setRackNode(rackNode);
        rackNode.create();
    }

    protected void reset() {
        RackMessage.BLANKRACK.send(this);
        // reset all composites
    }

    public void restore(RackNode rackNode) {
        // will save the state of the native rack into the node
        // this basically takes a snap shot of the native rack
        setRackNode(rackNode);
        rackNode.restore();
    }

    //--------------------------------------------------------------------------
    // ISoundGenerator API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        soundGenerator.initialize();
    }

    @Override
    public void close() {
        soundGenerator.close();
    }

    @Override
    public int getVerison() {
        return soundGenerator.getVerison();
    }

    @Override
    public final float getCurrentSongMeasure() {
        return soundGenerator.getCurrentSongMeasure();
    }

    @Override
    public final float getCurrentBeat() {
        return soundGenerator.getCurrentBeat();
    }

    //----------------------------------
    // IActivityCycle API
    //----------------------------------

    @Override
    public void onStart() {
        soundGenerator.onStart();
        soundGenerator.onResume();
    }

    @Override
    public void onResume() {
        soundGenerator.onResume();
    }

    @Override
    public void onPause() {
        soundGenerator.onPause();
    }

    @Override
    public void onStop() {
        soundGenerator.onStop();
    }

    @Override
    public void onDestroy() {
        soundGenerator.onDestroy();
    }

    @Override
    public void onRestart() {
        soundGenerator.onRestart();
    }

    @Override
    public void dispose() {
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    // we proxy the actual OSC impl so we can stop, or reroute
    @Override
    public final float sendMessage(String message) {
        return soundGenerator.sendMessage(message);
    }

    @Override
    public final String queryMessage(String message) {
        return soundGenerator.queryMessage(message);
    }

}

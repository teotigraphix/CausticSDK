////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.rack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.androidtransfuse.event.EventManager;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.device.IDeviceFactory;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.internal.device.Device;
import com.teotigraphix.caustic.internal.effect.EffectsRack;
import com.teotigraphix.caustic.internal.machine.Machine;
import com.teotigraphix.caustic.internal.mixer.MixerPanel;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineException;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.mixer.IMixerPanel;
import com.teotigraphix.caustic.osc.RackMessage;
import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.sequencer.ISequencer;

/**
 * The default implementation of the {@link IRack} interface.
 * <p>
 * A song is created when;
 * <ul>
 * <li>{@link #loadSong(String)} is called</li>
 * <li>{@link #addMachine(String, MachineType)} or
 * {@link #addMachineAt(int, String, MachineType)} is called and there are
 * currently 0 machines in the rack.</li>
 * </ul>
 * <p>
 * The song is never <code>null</code>, only recreated.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Rack extends Device implements IRack
{
    EventManager dispatcher;

    public EventManager getDispatcher()
    {
        return dispatcher;
    }

    //--------------------------------------------------------------------------
    //
    // Variables
    //
    //--------------------------------------------------------------------------

    private static final int TOTAL_TRACKS = 6;

    boolean skipMachinePopulate = false;

    boolean skipRackRestore = false;

    private boolean suppressMessages;

    protected final Map<Integer, IMachine> map = new TreeMap<Integer, IMachine>();

    private boolean restored = false;

    //--------------------------------------------------------------------------
    //
    // IRack API :: Properties
    //
    //--------------------------------------------------------------------------
    @Override
    public boolean isRestored()
    {
        return restored;
    }

    //----------------------------------
    // factory
    //----------------------------------

    private IDeviceFactory factory;

    @Override
    public IDeviceFactory getFactory()
    {
        return factory;
    }

    public void setFactory(IDeviceFactory value)
    {
        factory = value;
    }

    //----------------------------------
    // mixerPanel
    //----------------------------------

    private IMixerPanel mMixerPanel;

    @Override
    public IMixerPanel getMixerPanel()
    {
        return mMixerPanel;
    }

    protected void setMixerPanel(IMixerPanel mixerPanel)
    {
        mMixerPanel = mixerPanel;
    }

    //----------------------------------
    // effectsRack
    //----------------------------------

    private IEffectsRack mEffectsRack;

    @Override
    public IEffectsRack getEffectsRack()
    {
        return mEffectsRack;
    }

    protected void setEffectsRack(IEffectsRack value)
    {
        mEffectsRack = value;
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    private ISequencer mSequencer;

    @Override
    public ISequencer getSequencer()
    {
        return mSequencer;
    }

    protected void setSequencer(ISequencer sequencer)
    {
        mSequencer = sequencer;
    }

    //----------------------------------
    // outputPanel
    //----------------------------------

    private IOutputPanel mOutputPanel;

    private boolean mLoadingMachines;

    @Override
    public IOutputPanel getOutputPanel()
    {
        return mOutputPanel;
    }

    protected void setOutputPanel(IOutputPanel outputPanel)
    {
        mOutputPanel = outputPanel;
    }

    //----------------------------------
    // numTracks
    //----------------------------------

    @Override
    public int getNumTracks()
    {
        return TOTAL_TRACKS;
    }

    void setNumTracks(int value)
    {
        throw new UnsupportedOperationException(
                "Number of tracks cannot currently be set");
    }

    //----------------------------------
    // machines
    //----------------------------------

    @Override
    public int getNumMachines()
    {
        return getMachineMap().size();
    }

    @Override
    public Map<Integer, IMachine> getTrackMap()
    {
        Map<Integer, IMachine> result = new HashMap<Integer, IMachine>();
        final int len = getNumTracks();
        for (int i = 0; i < len; i++)
        {
            IMachine machine = null;
            if (hasMachine(i))
            {
                machine = getMachine(i);
            }
            result.put(i, machine);
        }
        return result;
    }

    @Override
    public Map<Integer, IMachine> getMachineMap()
    {
        Map<Integer, IMachine> result = new TreeMap<Integer, IMachine>();
        for (IMachine machine : map.values())
        {
            result.put(machine.getIndex(), machine);
        }
        return result;
    }

    @Override
    public Map<Integer, String> getMachineNames()
    {
        Map<Integer, String> result = new HashMap<Integer, String>();
        final int len = getNumTracks();
        for (int i = 0; i < len; i++)
        {
            String name = null;
            if (hasMachine(i))
            {
                name = getMachine(i).getId();
            }
            result.put(i, name);
        }
        return result;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Rack(IDeviceFactory factory)
    {
        setId("rack");
        setFactory(factory);
    }

    //--------------------------------------------------------------------------
    //
    // IRack API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void loadSong(String path) throws CausticException
    {
        File songFile = new File(path);

        if (!path.endsWith("caustic"))
            throw new CausticException(
                    "Loaded song file must be *.caustic format");
        if (!songFile.exists())
            throw new CausticException("Song file does not exist [" + path
                    + "]");

        List<IMachine> machines = new ArrayList<IMachine>(map.values());
        for (IMachine machine : machines)
        {
            removeMachineAt(machine.getIndex());
        }

        RackMessage.LOAD_SONG.send(getEngine(), songFile.getPath());

        songLoaded();
    }

    @Override
    public void saveSong(String name) throws CausticException
    {
        if (name.endsWith("caustic"))
            throw new CausticException(
                    "Save song name must not contain .caustic");
        RackMessage.SAVE_SONG.send(getEngine(), name);
        fireSongSaved();
    }

    @Override
    public void destroySong() throws MachineException
    {
        List<IMachine> machines = new ArrayList<IMachine>(map.values());
        for (IMachine machine : machines)
        {
            removeMachineAt(machine.getIndex());
        }
    }

    @Override
    public String queryMachineName(int index)
    {
        String result = RackMessage.QUERY_MACHINE_NAME.queryString(getEngine(),
                index);
        return result;
    }

    @Override
    public String queryMachineType(int index)
    {
        String result = RackMessage.QUERY_MACHINE_TYPE.queryString(getEngine(),
                index);
        return result;
    }

    public IMachine addMachineAt(int index, String name, String type,
            boolean suppress) throws CausticException
    {
        suppressMessages = suppress;

        MachineType machineType = MachineType.fromString(type);
        IMachine machine = null;
        try
        {
            machine = createMachine(index, machineType, name);
        } catch (CausticException e)
        {
            throw e;
        }

        if (machine == null)
        {
            throw new CausticException("Machine not created {" + name + ":"
                    + type + "}");
        }

        ((EffectsRack) getEffectsRack()).addMachine(machine);
        ((MixerPanel) getMixerPanel()).addMachine(machine);

        suppressMessages = false;

        return machine;
    }

    @Override
    public IMachine addMachineAt(int index, String name, MachineType type)
            throws CausticException
    {
        return addMachineAt(index, name, type.getValue(), false);
    }

    @Override
    public IMachine addMachine(String name, MachineType type)
            throws CausticException
    {
        return addMachineAt(nextIndex(), name, type.getValue(), false);
    }

    @Override
    public IMachine removeMachine(IMachine machine) throws MachineException
    {
        return removeMachineAt(machine.getIndex());
    }

    @Override
    public IMachine removeMachineAt(int index) throws MachineException
    {
        IMachine machine = map.get(index);
        if (machine == null)
            return null;

        map.remove(machine.getIndex());

        fireMachineRemoved(machine);

        ((Machine) machine).setIndex(-1);

        //machine.dispose();

        RackMessage.REMOVE.send(getEngine(), index);

        ((EffectsRack) getEffectsRack()).removeMachine(machine);
        ((MixerPanel) getMixerPanel()).removeMachine(machine);

        return machine;
    }

    @Override
    public IMachine getMachine(int index)
    {
        return map.get(index);
    }

    @Override
    public boolean hasMachine(int index)
    {
        return map.containsKey(index);
    }

    //--------------------------------------------------------------------------
    //
    // Public :: Methods
    //
    //--------------------------------------------------------------------------

    private int nextIndex()
    {
        int index = 0;
        // find the next index that is empty
        for (index = 0; index < 10; index++)
        {
            if (!map.containsKey(index))
            {
                break;
            }
        }
        return index;
    }

    private IMachine createMachine(int index, MachineType machineType,
            String machineId) throws CausticException
    {
        if (map.containsKey(index))
        {
            throw new CausticException("{" + index
                    + "} machine is already defined");
        }

        Machine machine = (Machine) factory.create(machineId, machineType);

        if (machine == null)
        {
            throw new CausticException("{" + machineType
                    + "} IMachine type not defined");
        }

        createCausticMachine(machineType.getValue(), machineId, index);

        machine.setFactory(factory);
        machine.setIndex(index);
        machine.setType(machineType);
        machine.setEngine(getEngine());

        map.put(index, machine);

        fireMachineAdded(machine);

        return machine;
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void initializeEngine(ICausticEngine engine)
    {
        super.initializeEngine(engine);
        initializeRack();
    }

    @Override
    protected void disposeEngine(ICausticEngine engine)
    {
        super.disposeEngine(engine);
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    protected void initializeRack()
    {
        setMixerPanel(factory.createMixerPanel());
        setEffectsRack(factory.createEffectRack());
        setOutputPanel(factory.createOutputPanel());
        setSequencer(factory.createSequencer());
    }

    protected void createCausticMachine(String machineType, String machineName,
            int index)
    {
        if (!suppressMessages)
        {
            RackMessage.CREATE.send(getEngine(), machineType, machineName,
                    index);
        }
    }

    protected void songLoaded() throws CausticException
    {
        fireSongPreloaded();

        // load the machines
        if (!skipMachinePopulate)
        {
            populateMachines();
        }

        fireSongLoaded();
    }

    void populateMachines() throws CausticException
    {
        mLoadingMachines = true;
        final int len = getNumTracks();
        for (int i = 0; i < len; i++)
        {
            String name = queryMachineName(i);
            String type = queryMachineType(i);
            if (name != null && type != null)
            {
                try
                {
                    IMachine machine = addMachineAt(i, name, type, true);
                    if (machine == null)
                    {
                        throw new CausticException("Error restoring IMachine "
                                + name + " type:" + type);
                    }
                } catch (CausticException e)
                {
                    throw e;
                }
            }
        }
        mLoadingMachines = false;
    }

    @Override
    public void restore()
    {
        mOutputPanel.restore();
        mMixerPanel.restore();
        mEffectsRack.restore();
        for (IMachine machine : map.values())
        {
            machine.restore();
        }
        restored = true;
        fireSongRestored();
    }

    protected final void fireMachineAdded(IMachine machine)
    {
        MachineChangeKind kind = mLoadingMachines ? MachineChangeKind.LOADED
                : MachineChangeKind.ADDED;
        for (OnMachineChangeListener l : mOnMachineChangeListener)
        {
            l.onMachineChanged(machine, kind);
        }
    }

    protected final void fireMachineRemoved(IMachine machine)
    {
        for (OnMachineChangeListener l : mOnMachineChangeListener)
        {
            l.onMachineChanged(machine, MachineChangeKind.REMOVED);
        }
    }

    protected final void fireSongAdded()
    {
        if (mOnSongStateChangeListener != null)
            mOnSongStateChangeListener
                    .onSongStateChanged(SongStateChangeKind.ADDED);
    }

    protected final void fireSongRemoved()
    {
        if (mOnSongStateChangeListener != null)
            mOnSongStateChangeListener
                    .onSongStateChanged(SongStateChangeKind.REMOVED);
    }

    protected final void fireSongPreloaded()
    {
        if (mOnSongStateChangeListener != null)
            mOnSongStateChangeListener
                    .onSongStateChanged(SongStateChangeKind.PRELOADED);
    }

    protected final void fireSongLoaded()
    {
        if (mOnSongStateChangeListener != null)
            mOnSongStateChangeListener
                    .onSongStateChanged(SongStateChangeKind.LOADED);
    }

    protected final void fireSongSaved()
    {
        if (mOnSongStateChangeListener != null)
            mOnSongStateChangeListener
                    .onSongStateChanged(SongStateChangeKind.SAVED);
    }

    protected final void fireSongRestored()
    {
        if (mOnSongStateChangeListener != null)
            mOnSongStateChangeListener
                    .onSongStateChanged(SongStateChangeKind.RESTORED);
    }

    private OnSongStateChangeListener mOnSongStateChangeListener;

    @Override
    public void setOnSongStateChangeListener(OnSongStateChangeListener l)
    {
        mOnSongStateChangeListener = l;
    }

    private final ArrayList<OnMachineChangeListener> mOnMachineChangeListener = new ArrayList<IRack.OnMachineChangeListener>();

    @Override
    public void addOnMachineChangeListener(OnMachineChangeListener l)
    {
        if (!mOnMachineChangeListener.contains(l))
        {
            mOnMachineChangeListener.add(l);
        }
    }

    @Override
    public void removeOnMachineChangeListener(OnMachineChangeListener l)
    {
        if (mOnMachineChangeListener.contains(l))
        {
            mOnMachineChangeListener.remove(l);
        }
    }

    @Override
    public String toString()
    {
        return map.toString();
    }

}

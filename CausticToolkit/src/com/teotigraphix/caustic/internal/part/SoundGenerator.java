////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.part;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineException;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.IPart.IPartListener;
import com.teotigraphix.caustic.part.IPatch;
import com.teotigraphix.caustic.part.IPatchLibrary;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.part.ITone;
import com.teotigraphix.caustic.part.IToneLibrary;
import com.teotigraphix.caustic.rack.IRack.MachineChangeKind;
import com.teotigraphix.caustic.rack.IRack.OnMachineChangeListener;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
@SuppressLint("UseSparseArrays")
public class SoundGenerator implements ISoundGenerator {

    private static final int NUM_TOTAL_MACHINES = 10;

    //--------------------------------------------------------------------------
    // 
    //  Variables
    // 
    //--------------------------------------------------------------------------

    /**
     * Internal part list, not ordered.
     */
    private final List<IPart> mParts = new ArrayList<IPart>();

    private final Map<Integer, IPart> mMap = new HashMap<Integer, IPart>();

    private final List<IPart> mMuteParts = new ArrayList<IPart>();

    private final List<IPart> mSoloParts = new ArrayList<IPart>();

    private IPart mLastSoloPart;

    IProject getProject() {
        return mWorkspace.getProject();
    }

    //--------------------------------------------------------------------------
    // 
    //  ISoundGenerator API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  patchLibrary
    //----------------------------------

    private IPatchLibrary mPatchLibrary;

    public IPatchLibrary getPatchLibrary() {
        return mPatchLibrary;
    }

    //----------------------------------
    //  toneLibrary
    //----------------------------------

    private IToneLibrary mToneLibrary;

    private IWorkspace mWorkspace;

    public IToneLibrary getToneLibrary() {
        return mToneLibrary;
    }

    //----------------------------------
    //  parts
    //----------------------------------

    @Override
    public Collection<IPart> getParts() {
        return Collections.unmodifiableList(mParts);
    }

    //----------------------------------
    //  muteParts
    //----------------------------------

    @Override
    public Collection<IPart> getMuteParts() {
        return Collections.unmodifiableList(mMuteParts);
    }

    //----------------------------------
    //  soloParts
    //----------------------------------

    @Override
    public Collection<IPart> getSoloParts() {
        return Collections.unmodifiableList(mSoloParts);
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * 
     * @param rack
     * @param registry
     */
    public SoundGenerator(IWorkspace workspace) {
        mWorkspace = workspace;
        mWorkspace.getRack().addOnMachineChangeListener(mOnMachineChangeListener);
        mToneLibrary = new ToneLibrary(mWorkspace);
        mPatchLibrary = new PatchLibrary(mWorkspace);
    }

    //--------------------------------------------------------------------------
    // 
    //  ISoundGenerator API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public IPart getPart(int index) {
        return mMap.get(index);
    }

    @Override
    public IPart create(String partId, String partName, MachineType partType, IPatch patch)
            throws CausticException {
        int index = nextIndex();
        if (patch == null)
            patch = mPatchLibrary.createDefaultPatch(partType);
        IPart part = doCreate(index, partId, partName, partType, patch);
        return part;
    }

    IPart doCreate(int index, String partId, String partName, MachineType partType, IPatch patch)
            throws CausticException {

        if (mMap.containsKey(index))
            throw new CausticException();

        Part part = createPart(index, partId, partName, partType);

        loadPatch(part, patch);

        return part;
    }

    @Override
    public IPart load(IMachine machine) throws CausticException {
        if (mMap.containsKey(machine.getIndex()))
            throw new CausticException();

        String name = machine.getId();
        Part part = createPart(machine.getIndex(), name, name, machine.getType());

        // all phrases were created in machine.sequencer.restore()
        // we need to set the current SequencerPhrase the was set in the
        // sequencers restore() as well
        IPatternSequencer ps = machine.getSequencer();
        IPhrase phrase = ps.getPhrase();
        part.setPhrase(phrase);

        // since all patterns are loaded from the .caustic file, it's initialized
        part.setInitialized(true);

        return part;
    }

    @Override
    public void loadPreset(IPart part, File file) throws CausticException {
        mPatchLibrary.loadPreset(part, file);
    }

    @Override
    public void savePreset(IPart part) throws CausticException {
        mPatchLibrary.savePreset(part, null);
    }

    @Override
    public void savePreset(IPart part, File file) throws CausticException {
        mPatchLibrary.savePreset(part, file);
    }

    public void loadPatch(IPart part, IPatch patch) {
        // so everytime a part is loaded, it set's the current patch
        // which will reset the IMachine's settings to the patches settings
        patch.setRack(mWorkspace.getRack());
        // if there is a memento waiting it gets pasted here
        ((Part)part).setPatch(patch);
    }

    @Override
    public void allMute() {
        for (IPart part : mParts) {
            part.setMute(true);
        }
    }

    @Override
    public void noMute() {
        for (IPart part : mParts) {
            part.setMute(false);
        }
    }

    @Override
    public void inverseMute() {
        for (IPart part : mParts) {
            part.setMute(!part.isMute());
        }
    }

    /**
     * The IPart has no domain over the IRack, so anything that needs to be do
     * with mixer commands, need to be done from the outside of the part.
     */
    private void mutePart(Part part) {
        // XXX ACtually this might be better just sending a message from the part
        // using OSC to the engine
        for (ITone tone : part.getTones()) {
            mWorkspace.getRack().getMixerPanel().setMute(tone.getIndex(), part.isMute());
        }
    }

    private void soloPart(Part part) {

        if (part.isSolo()) {
            if (mLastSoloPart != null) {
                mLastSoloPart.setSolo(false);
            }
            // mute all parts except the solo part
            for (IPart mpart : getParts()) {
                if (mpart != part && !mpart.isMute()) {
                    mpart.setMute(true);
                }
            }
            mLastSoloPart = part;
        } else {
            for (IPart mpart : getParts()) {
                mpart.setMute(false);
            }
            if (mLastSoloPart != null && mLastSoloPart.isSolo()) {
                mLastSoloPart.setSolo(false);
            }
        }

        // XXX When multiple tones are implemented, the solo feature will
        // need to have a custom implementation, I doubt the mixer allows for
        // multiple machine solo
        for (ITone tone : part.getTones()) {
            mWorkspace.getRack().getMixerPanel().setSolo(tone.getIndex(), part.isSolo());
        }
    }

    private Part createPart(int index, String partId, String partName, MachineType type)
            throws CausticException {
        // 1) create the correct part
        Part part = type == MachineType.BEATBOX ? new RhythmPart() : new SynthPart();

        // 2) create the tone (IMachine)
        ITone tone = null;
        try {
            tone = mToneLibrary.createDefaultTone(index, partId, partName, type);
        } catch (MachineException e) {
            e.printStackTrace();
        }

        // 3) configure the part
        part.setId(partId);
        part.setName(partName);
        part.addTone(tone);

        // 4) set the part's current IPhrase
        // for creation, there is no event, set it explicitly
        part.setPhrase(tone.getSequencer().getPhrase());

        // 5) add part to collection and map
        mParts.add(part);
        mMap.put(index, part);

        // 6) add the part listener for mute, solo etc. changes
        part.addPartListener(mPartListener);

        return part;
    }

    //--------------------------------------------------------------------------
    // 
    //  PartListener API :: Listeners
    // 
    //--------------------------------------------------------------------------

    private IPartListener mPartListener = new IPartListener() {

        @Override
        public void onSoloChange(IPart part, boolean isSolo) {
            if (part.isSolo()) {
                if (!mSoloParts.contains(part)) {
                    mSoloParts.add(part);
                }
            } else if (mSoloParts.contains(part)) {
                mSoloParts.remove(part);
            }
            soloPart((Part)part);
        }

        @Override
        public void onSelectedChange(IPart part, boolean isSelected) {
        }

        @Override
        public void onMuteChange(IPart part, boolean isMute) {
            if (part.isMute()) {
                if (!mMuteParts.contains(part)) {
                    mMuteParts.add(part);
                }
            } else if (mMuteParts.contains(part)) {
                mMuteParts.remove(part);
            }
            mutePart((Part)part);
        }
    };

    private OnMachineChangeListener mOnMachineChangeListener = new OnMachineChangeListener() {
        @Override
        public void onMachineChanged(IMachine machine, MachineChangeKind kind) {
            if (kind == MachineChangeKind.LOADED) {
                try {
                    load(machine);
                } catch (CausticException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (kind == MachineChangeKind.REMOVED) {
                destroyPart(findPart(machine));
            }
        }
    };

    void destroyPart(IPart part) {
        if (part == null)
            return;

        part.removePartListener(mPartListener);
        mParts.remove(part);
        mMap.remove(part.getIndex());

        part.dispose();
    }

    IPart findPart(IMachine machine) {
        for (IPart part : mParts) {
            if (part.getIndex() == machine.getIndex())
                return part;
        }
        return null;
    }

    private int nextIndex() {
        int index = 0;
        for (index = 0; index < NUM_TOTAL_MACHINES; index++) {
            if (!mWorkspace.getRack().hasMachine(index)) {
                break;
            }
        }
        return index;
    }

    public void removeAllParts() {
        List<IPart> old = new ArrayList<IPart>(getParts());
        for (IPart part : old) {
            destroyPart(part);
        }
    }

}

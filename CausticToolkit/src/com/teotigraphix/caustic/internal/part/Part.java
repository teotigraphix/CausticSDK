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

package com.teotigraphix.caustic.internal.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.IPatch;
import com.teotigraphix.caustic.part.ITone;
import com.teotigraphix.caustic.part.PartData;
import com.teotigraphix.caustic.sequencer.IPatternSequencer.IPatternSequencerListener;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.IPattern;
import com.teotigraphix.common.IMemento;

/*

- ISoundGenerator
	
  - THIS is what I want the generator to do
    - create a raw machine and wrap it in a part
    - load a machine during IRack.loadSong(), this is restore()
    
    - load a preset file INTO a part
      - IPatchLibrary.loadPreset()
    - save a preset of a Part into a file

		PresetPatch
		  - file:File > MyPrest.subsynth
		  - save() > machine.savePreset()
		    - uses the name of the file that was passed in the constructor
		  - load()
		    - if the file dosn't exist, throw CausticException
		    - machine.loadPreset(file)
		    - machine.restore()  
    
    - all mute, no mute, inverse mute
    
    create()
      - addPart()
        - createPart()
          - new Part ? RhythmPart
          - IToneLibrary.createDefaultTone()
          - setup Part properties
          - add part to part map
          - add partListener
            - listens for part mute & solo changes, adds/removes from map
        - loadPatch()

  
  - listens to the IRack for onMachineAdded, onMachineRemoved callbacks
    - 
  
  - creates/manages all IPart instances
  - tracks muted parts
  - tracks soloed parts
  - manages the IToneLibrary
  - manages the IPatchLibrary
  


- IPart > ITone > IMachine

IMachine
  - id:


  - id:String -> Machine readable name (unique)
  - name:String -> Human readable name (non-unique)
  - index:int -> The index location in the ISoundGenerator
  
  - tone:ITone -> The part's IMachine
  - pattern:IPattern -> The part's current pattern (sequencer/effect/mixer data) can be null
  - patch:IPatch -> The state manager for sound parameters
  - phrase:IPhrase -> The current sequencer data the Part playing from a IMachine
  
  - selected:boolean -> Whether the part is the current editing part
  - mute:boolean -> Whether the part is mute
  - solo:boolean -> Whether the part is solo




ITone
  - 




*/

/**
 * The default implementation of the IPart interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Part implements IPart, IPatternSequencerListener {

    private static final String TAG_PATCH = "patch";

    private static final String ATT_NAME = "name";

    private static final String ATT_ID = "id";

    //--------------------------------------------------------------------------
    // 
    //  IPart API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  initialized
    //----------------------------------

    private boolean mInitialized;

    @Override
    public boolean isInitialized() {
        return mInitialized;
    }

    @Override
    public void setInitialized(boolean value) {
        mInitialized = value;
    }

    //----------------------------------
    //  id
    //----------------------------------

    private String mId;

    @Override
    public String getId() {
        return mId;
    }

    public void setId(String value) {
        mId = value;
    }

    //----------------------------------
    //  name
    //----------------------------------

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = value;
    }

    //----------------------------------
    //  index
    //----------------------------------

    @Override
    public int getIndex() {
        return getTone().getIndex();
    }

    //----------------------------------
    //  tone
    //----------------------------------

    private final List<ITone> mTones = new ArrayList<ITone>();

    //@Override
    public ITone getToneAt(int index) {
        return mTones.get(index);
    }

    //@Override
    public List<ITone> getTones() {
        return Collections.unmodifiableList(mTones);
    }

    /**
     * Temp until unlimited machines are allowed.
     */
    @Override
    public ITone getTone() {
        return mTones.get(0);
    }

    public void addTone(ITone tone) throws CausticException {
        if (mTones.contains(tone))
            throw new CausticException("Tone already exists in part; " + tone.toString());

        int size = mTones.size();
        mTones.add(tone);

        toneAdd(tone, size);
    }

    //----------------------------------
    //  pattern
    //----------------------------------

    private IPattern mPattern;

    @Override
    public IPattern getPattern() {
        return mPattern;
    }

    void setPattern(IPattern value) {
        if (value == null) {
            mPattern = null;
            return;
        }
        // XXX Part MUST have it's tones by the time a pattern is assigned or its 
        // and exception!

        mPattern = value;
    }

    //----------------------------------
    //  patch
    //----------------------------------

    private IPatch mPatch;

    @Override
    public IPatch getPatch() {
        return mPatch;
    }

    void setPatch(IPatch value) {
        if (mPatch != null) {
            ((Patch)mPatch).setPart(null);
        }

        mPatch = value;

        if (mPatch != null) {
            ((Patch)mPatch).setPart(this);
        }
    }

    //----------------------------------
    //  phrase
    //----------------------------------

    private IPhrase mPhrase;

    @Override
    public IPhrase getPhrase() {
        return mPhrase;
    }

    void setPhrase(IPhrase value) {
        mPhrase = value;
    }

    //----------------------------------
    //  selected
    //----------------------------------

    private boolean mSelected;

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    @Override
    public void setSelected(boolean value) {
        if (value == mSelected)
            return;
        mSelected = value;
        fireSelectedChange(mSelected);
    }

    //----------------------------------
    //  mute
    //----------------------------------

    private boolean mMute;

    @Override
    public boolean isMute() {
        return mMute;
    }

    @Override
    public void setMute(boolean value) {
        if (value == mMute)
            return;
        mMute = value;
        fireMuteChange(mMute);
    }

    //----------------------------------
    //  solo
    //----------------------------------

    private boolean mSolo;

    @Override
    public boolean isSolo() {
        return mSolo;
    }

    @Override
    public void setSolo(boolean value) {
        if (value == mSolo)
            return;
        mSolo = value;
        fireSoloChange(mSolo);
    }

    //----------------------------------
    //  data
    //----------------------------------

    private PartData mData;

    @Override
    public PartData getData() {
        return mData;
    }

    @Override
    public void setData(PartData value) {
        mData = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public Part() {
        setData(new PartData());
    }

    //--------------------------------------------------------------------------
    // 
    //  IPart API :: Methods
    // 
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    protected void toneAdd(ITone tone, int index) throws CausticException {
        tone.getSequencer().addPatternSequencerListener(this);
    }

    protected void toneRemove(ITone tone) {
        tone.getSequencer().removePatternSequencerListener(this);
        //tone.setIndex(-1);
    }

    //--------------------------------------------------------------------------
    // 
    //  Signal :: Handlers
    // 
    //--------------------------------------------------------------------------

    /*
     What is a Part and what does it save?
     - part has id, name and index(the index can be ignored depending on conditions)
     - part has a patch, either reference to an existing patch in the
       PatchLibrary or a straight copy of the machine state that is the part's tone
     - part has a tone
     - Phrase data is NOT copied since that relates to a Pattern
     
     */

    @Override
    public void copy(IMemento memento) {
        // we don't save index, its relative to machine creation
        memento.putString(ATT_ID, getId());
        memento.putString(ATT_NAME, getName());
        // patch; If I actually save the state of the patch here it would
        // freeze the current state with the tones changes?
        getPatch().copy(memento.createChild(TAG_PATCH));
    }

    @Override
    public void paste(IMemento memento) {
        setId(memento.getString(ATT_ID));
        setName(memento.getString(ATT_NAME));
        getPatch().paste(memento.getChild(TAG_PATCH));
    }

    //	@Override
    //	public String toString() {
    //		return "Part(" + mId + ") [name=" + mName + ", index=" + getIndex()
    //				+ ", selected=" + mSelected + ", mute=" + mMute + ", solo="
    //				+ mSolo + ", tone= {\n" + mTone + " \n}\n ]";
    //	}

    @Override
    public String toString() {
        return "Part(" + mId + ")";
    }

    //--------------------------------------------------------------------------
    // 
    //  IDispose API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void dispose() {
    }

    //--------------------------------------------------------------------------
    // 
    //  IPatternSequencerListener API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void onPhraseChange(IPhrase phrase, IPhrase oldPhrase) {
        setPhrase(phrase);
    }

    //--------------------------------------------------------------------------
    // 
    //  Part listener :: Methods
    // 
    //--------------------------------------------------------------------------

    private final List<IPartListener> mPartListeners = new ArrayList<IPartListener>();

    @Override
    public final void addPartListener(IPartListener value) {
        if (mPartListeners.contains(value))
            return;
        mPartListeners.add(value);
    }

    @Override
    public final void removePartListener(IPartListener value) {
        if (!mPartListeners.contains(value))
            return;
        mPartListeners.remove(value);
    }

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    protected final void fireMuteChange(boolean isMute) {
        for (IPartListener listener : mPartListeners) {
            listener.onMuteChange(this, isMute);
        }
    }

    protected final void fireSoloChange(boolean isSolo) {
        for (IPartListener listener : mPartListeners) {
            listener.onSoloChange(this, isSolo);
        }
    }

    protected final void fireSelectedChange(boolean isSelected) {
        for (IPartListener listener : mPartListeners) {
            listener.onSelectedChange(this, isSelected);
        }
    }

    @Override
    public void copyTone(IMemento memento) {
        for (ITone tone : mTones) {
            tone.copy(memento.createChild("tone"));
        }
    }

    @Override
    public void pasteTone(IMemento memento) {
        for (IMemento child : memento.getChildren("tone")) {
            ITone tone = getToneAt(child.getInteger("index"));
            // memento is the <tone> tag
            tone.paste(child);
        }
    }
}

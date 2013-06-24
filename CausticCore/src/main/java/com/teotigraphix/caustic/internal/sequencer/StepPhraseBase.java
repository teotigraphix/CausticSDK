
package com.teotigraphix.caustic.internal.sequencer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustic.internal.utils.PatternUtils;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.ITrigger;
import com.teotigraphix.caustic.sequencer.IStepPhrase;
import com.teotigraphix.caustic.sequencer.PatternMeasures;
import com.teotigraphix.caustic.sequencer.data.StepPhraseData;

public abstract class StepPhraseBase implements IStepPhrase {
    private Map<Integer, Map<Integer, ITrigger>> stepMap;

    //----------------------------------
    // id
    //----------------------------------

    @Override
    public String getId() {
        return PatternUtils.toString(bank, index);
    }

    //----------------------------------
    // data
    //----------------------------------

    private StepPhraseData mData;

    @Override
    public StepPhraseData getData() {
        return mData;
    }

    @Override
    public void setData(StepPhraseData value) {
        mData = value;
    }

    //----------------------------------
    // active
    //----------------------------------

    private boolean mActive;

    @Override
    public final boolean isActive() {
        return mActive;
    }

    @Override
    public final void setActive(boolean value) {
        mActive = value;
    }

    //----------------------------------
    // bank
    //----------------------------------

    private int bank;

    @Override
    public final int getBank() {
        return bank;
    }

    //----------------------------------
    // index
    //----------------------------------

    private int index;

    @Override
    public final int getIndex() {
        return index;
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    private IPatternSequencer sequencer;

    @Override
    public IPatternSequencer getSequencer() {
        return sequencer;
    }

    @Override
    public void setSequencer(IPatternSequencer value) {
        sequencer = value;
    }

    //----------------------------------
    // stringData
    //----------------------------------

    private String noteData;

    @Override
    public final String getNoteData() {
        return noteData;
    }

    @Override
    public final void setNoteData(String value) {
        noteData = value;
        if (noteData == null || noteData.equals(""))
            return;
        initializeData(noteData);
    }

    protected void initializeData(String data) {
        // push the notes into the machines sequencer
        String[] notes = data.split("\\|");
        for (String noteData : notes) {
            String[] split = noteData.split(" ");

            float start = Float.valueOf(split[0]);
            int pitch = Float.valueOf(split[1]).intValue();
            float velocity = Float.valueOf(split[2]);
            float end = Float.valueOf(split[3]);
            float gate = end - start;
            int flags = Float.valueOf(split[4]).intValue();
            int step = Resolution.toStep(start, getResolution());

            triggerOn(step, pitch, gate, velocity, flags);
        }
    }

    //----------------------------------
    // resolution
    //----------------------------------

    private Resolution mResolution = Resolution.SIXTEENTH;

    @Override
    public Resolution getResolution() {
        return mResolution;
    }

    @Override
    public void setResolution(Resolution value) {
        if (value == mResolution)
            return;

        Resolution oldValue = mResolution;
        mResolution = value;
        if (value.getValue() < oldValue.getValue()) {
            StepPhraseUtils.expandResolution(this, oldValue, value);
        } else {
            StepPhraseUtils.contractResolution(this, oldValue, value);
        }

        fireResolutionChange(mResolution);
    }

    //----------------------------------
    // length
    //----------------------------------

    private int mLength = -1;

    @Override
    public int getLength() {
        return mLength;
    }

    @Override
    public void setLength(int value) {
        if (value == mLength)
            return;

        if (!PatternMeasures.isValid(value)) {
            return;
        }

        int oldValue = mLength;
        mLength = value;

        // the value has changed so the trigger list needs to
        // be truncated or extended
        // extension dosn't affect the data but truncation erases
        // existing trigger data
        StepPhraseUtils.updateLength(this, oldValue, value);

        fireLengthChange(mLength);
    }

    //----------------------------------
    // position
    //----------------------------------

    private int mPosition = 0;

    @Override
    public int getPosition() {
        return mPosition;
    }

    // (mschmalle) This might be deprecated now since it could just be passed
    // to getViewTriggers() and everything would be calculated
    // based on the resolution, this way the client is in total control
    // of the result with on method call
    @Override
    public void setPosition(int value) {
        if (value == mPosition)
            return;

        // the position can only be at the most one less than the current length
        // times the resolution
        // value = Math.max(0, Math.min(value, mLength - 1));

        mPosition = value;

        firePositionChange(mPosition);
    }

    //----------------------------------
    // numSteps
    //----------------------------------

    @Override
    public int getNumSteps() {
        return stepMap.size();
    }

    //----------------------------------
    // stepMap
    //----------------------------------

    @Override
    public Map<Integer, Map<Integer, ITrigger>> getStepMap() {
        // Collections.unmodifiableMap
        return stepMap;
    }

    // XXX Is this nessecary ?
    public void setStepMap(Map<Integer, Map<Integer, ITrigger>> value) {
        stepMap = value;
    }

    public StepPhraseBase(int bank, int index) {
        stepMap = new TreeMap<Integer, Map<Integer, ITrigger>>();

        this.bank = bank;
        this.index = index;
    }

    @Override
    public abstract boolean hasTriggers();

    @Override
    public abstract void clear();

    @Override
    public abstract ITrigger getTriggerAtStep(int step, int pitch);

    @Override
    public abstract ITrigger getTriggerAtBeat(float beat, int pitch);

    @Override
    public abstract Collection<ITrigger> getTriggersAtStep(int step);

    @Override
    public abstract Collection<ITrigger> getTriggersAtBeat(float beat);

    @Override
    public abstract List<ITrigger> getTriggers();

    @Override
    public abstract void addNote(int pitch, float start, float end, float velocity, int flags);

    @Override
    public abstract void removeNote(int pitch, float start);

    @Override
    public abstract void triggerOn(int step, int pitch, float gate, float velocity, int flags);

    @Override
    public abstract void triggerOff(int step, int pitch);

    //--------------------------------------------------------------------------
    //
    // Listeners :: Methods
    //
    //--------------------------------------------------------------------------

    private final List<IStepPhraseListener> mPhraseListeners = new ArrayList<IStepPhraseListener>();

    @Override
    public final void addStepPhraseListener(IStepPhraseListener value) {
        if (mPhraseListeners.contains(value))
            return;
        mPhraseListeners.add(value);
    }

    @Override
    public final void removeStepPhraseListener(IStepPhraseListener value) {
        if (!mPhraseListeners.contains(value))
            return;
        mPhraseListeners.remove(value);
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    protected Map<Integer, ITrigger> getPitchMapAt(int step) {
        return getStepMap().get(step);
    }

    protected final void fireLengthChange(int length) {
        for (IStepPhraseListener listener : mPhraseListeners)
            listener.onLengthChange(this, length);
    }

    protected final void firePositionChange(int position) {
        for (IStepPhraseListener listener : mPhraseListeners)
            listener.onPositionChange(this, position);
    }

    protected final void fireResolutionChange(Resolution resolution) {
        for (IStepPhraseListener listener : mPhraseListeners)
            listener.onResolutionChange(this, resolution);
    }

    protected final void fireTriggerDataChange(ITrigger trigger, TriggerChangeKind kind) {
        for (IStepPhraseListener listener : mPhraseListeners)
            listener.onTriggerDataChange(trigger, kind);
    }

    @Override
    public String toString() {
        return PatternUtils.toString(bank, index);
    }
}

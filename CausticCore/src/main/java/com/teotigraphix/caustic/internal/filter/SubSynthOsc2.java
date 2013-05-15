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

package com.teotigraphix.caustic.internal.filter;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.filter.ISubSynthOsc2;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.SubSynthOscMessage;

/**
 * The default implementation of the {@link ISubSynthOsc2} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynthOsc2 extends OscillatorComponent implements ISubSynthOsc2 {

    //--------------------------------------------------------------------------
    //
    // ISubSynthOsc2 API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    private int mCents = 0;

    @Override
    public int getCents() {
        return mCents;
    }

    public int getCents(boolean restore) {
        return (int)SubSynthOscMessage.OSC2_CENTS.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setCents(int value) {
        if (value == mCents)
            return;
        if (value < -50 || value > 50)
            throw newRangeException(SubSynthOscMessage.OSC2_CENTS.toString(), "-50..50", value);
        mCents = value;
        SubSynthOscMessage.OSC2_CENTS.send(getEngine(), getMachineIndex(), mCents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int mOctave = 0;

    @Override
    public int getOctave() {
        return mOctave;
    }

    public int getOctave(boolean restore) {
        return (int)SubSynthOscMessage.OSC2_OCTAVE.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setOctave(int value) {
        if (value == mOctave)
            return;
        if (value < -3 || value > 3)
            throw newRangeException(SubSynthOscMessage.OSC2_OCTAVE.toString(), "-3..3", value);
        mOctave = value;
        SubSynthOscMessage.OSC2_OCTAVE.send(getEngine(), getMachineIndex(), mOctave);
    }

    //----------------------------------
    // phase
    //----------------------------------

    private float mPhase = 0f;

    @Override
    public float getPhase() {
        return mPhase;
    }

    public float getPhase(boolean restore) {
        return SubSynthOscMessage.OSC2_PHASE.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setPhase(float value) {
        if (value == mPhase)
            return;
        if (value < -0.5f || value > 0.5f)
            throw newRangeException(SubSynthOscMessage.OSC2_PHASE.toString(), "-0.5..0.5", value);
        mPhase = value;
        SubSynthOscMessage.OSC2_PHASE.send(getEngine(), getMachineIndex(), mPhase);
    }

    //----------------------------------
    // semis
    //----------------------------------

    private int mSemis = 0;

    @Override
    public int getSemis() {
        return mSemis;
    }

    public int getSemis(boolean restore) {
        return (int)SubSynthOscMessage.OSC2_SEMIS.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setSemis(int value) {
        if (value == mSemis)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(SubSynthOscMessage.OSC2_SEMIS.toString(), "-12..12", value);
        mSemis = value;
        SubSynthOscMessage.OSC2_SEMIS.send(getEngine(), getMachineIndex(), mSemis);
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    private WaveForm mWaveForm = WaveForm.NONE;

    @Override
    public WaveForm getWaveform() {
        return mWaveForm;
    }

    public WaveForm getWaveform(boolean restore) {
        return WaveForm.toType(SubSynthOscMessage.OSC2_WAVEFORM.query(getEngine(),
                getMachineIndex()));
    }

    @Override
    public void setWaveform(WaveForm value) {
        if (value == mWaveForm)
            return;
        mWaveForm = value;
        SubSynthOscMessage.OSC2_WAVEFORM.send(getEngine(), getMachineIndex(), mWaveForm.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SubSynthOsc2(IMachine machine) {
        super(machine);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putInteger(FilterConstants.ATT_CENTS, getCents());
        memento.putInteger(FilterConstants.ATT_OCTAVE, getOctave());
        memento.putFloat(FilterConstants.ATT_PHASE, getPhase());
        memento.putInteger(FilterConstants.ATT_SEMIS, getSemis());
        memento.putInteger(FilterConstants.ATT_WAVEFORM, getWaveform().getValue());
    }

    @Override
    public void paste(IMemento memento) {
        setCents(memento.getInteger(FilterConstants.ATT_CENTS));
        setOctave(memento.getInteger(FilterConstants.ATT_OCTAVE));
        setPhase(memento.getFloat(FilterConstants.ATT_PHASE));
        setSemis(memento.getInteger(FilterConstants.ATT_SEMIS));
        setWaveform(WaveForm.toType(memento.getInteger(FilterConstants.ATT_WAVEFORM)));
    }

    @Override
    public void restore() {
        setCents(getCents(true));
        setOctave(getOctave(true));
        setPhase(getPhase(true));
        setSemis(getSemis(true));
        setWaveform(getWaveform(true));
    }
}

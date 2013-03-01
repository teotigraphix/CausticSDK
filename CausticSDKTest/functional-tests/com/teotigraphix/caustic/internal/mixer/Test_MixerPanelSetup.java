
package com.teotigraphix.caustic.internal.mixer;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.mixer.IMixerDelay;
import com.teotigraphix.caustic.mixer.IMixerReverb;
import com.teotigraphix.caustic.test.EmptyRackTestBase;

public class Test_MixerPanelSetup extends EmptyRackTestBase {

    @Override
    protected void setupValues() {
        setupDelayValues((MixerDelay)mMixerPanel.getDelay());
        setupReverbValues((MixerReverb)mMixerPanel.getReverb());
        setupMasterValues();

        setupMachineValues(subSynth1);
        setupMachineValues(pcmSynth1);
        setupMachineValues(pcmSynth2);
        setupMachineValues(bassline1);
        setupMachineValues(bassline2);
        setupMachineValues(beatbox);
    }

    @Override
    protected void assertValues() {
        assertDelayValues(mMixerPanel.getDelay());
        assertReverbValues(mMixerPanel.getReverb());
        assertMasterValues();

        assertMachineValues(subSynth1);
        assertMachineValues(pcmSynth1);
        assertMachineValues(pcmSynth2);
        assertMachineValues(bassline1);
        assertMachineValues(bassline2);
        assertMachineValues(beatbox);
    }

    private void setupDelayValues(MixerDelay component) {
        // test delay (time[1..9], feedback, stereo)
        assertEquals(7, component.getTime(true));
        assertEquals(0.5f, component.getFeedback(true));
        assertFalse(component.isStereo(true));

        component.setTime(3);
        component.setFeedback(0.62f);
        component.setStereo(true);
    }

    private void setupReverbValues(MixerReverb component) {
        // test reverb (room, damping, stereo)
        assertEquals(0.75f, component.getRoom(true));
        assertEquals(0.2f, component.getDamping(true));
        assertFalse(component.isStereo(true));

        component.setRoom(0.5f);
        component.setDamping(0.6f);
        component.setStereo(true);
    }

    private void assertMasterValues() {
        assertEquals(0.5f, mMixerPanel.getMasterBass());
        assertEquals(-0.5f, mMixerPanel.getMasterMid());
        assertEquals(1f, mMixerPanel.getMasterHigh());
        assertEquals(1.5f, mMixerPanel.getMasterVolume());
    }

    private void setupMasterValues() {
        // TODO (mschmalle) restore /caustic/mixer/master/eq_bass
        assertEquals(0.100000024f, mMixerPanel.getMasterBass(true));
        assertEquals(0.0f, mMixerPanel.getMasterMid(true));
        // TODO (mschmalle) restore /caustic/mixer/delay/eq_high
        assertEquals(0.100000024f, mMixerPanel.getMasterHigh(true));
        assertEquals(1.0f, mMixerPanel.getMasterVolume(true));

        mMixerPanel.setMasterBass(0.5f);
        mMixerPanel.setMasterMid(-0.5f);
        mMixerPanel.setMasterHigh(1f);
        mMixerPanel.setMasterVolume(1.5f);
    }

    private void setupMachineValues(IMachine machine) {
        assertMachinePreRestoreValues(machine);

        mMixerPanel.setBass(machine.getIndex(), -0.3f);
        mMixerPanel.setMid(machine.getIndex(), 0.2f);
        mMixerPanel.setHigh(machine.getIndex(), -0.4f);
        mMixerPanel.setDelaySend(machine.getIndex(), 0.5f);
        mMixerPanel.setReverbSend(machine.getIndex(), 0.3f);
        mMixerPanel.setPan(machine.getIndex(), 1f);
        mMixerPanel.setStereoWidth(machine.getIndex(), 0.4f);
        mMixerPanel.setMute(machine.getIndex(), true);
        mMixerPanel.setVolume(machine.getIndex(), 1.5f);
    }

    private void assertDelayValues(IMixerDelay component) {
        assertEquals(3, component.getTime());
        assertEquals(0.62f, component.getFeedback());
        assertTrue(component.isStereo());
    }

    private void assertReverbValues(IMixerReverb component) {
        assertEquals(0.5f, component.getRoom());
        assertEquals(0.6f, component.getDamping());
        assertTrue(component.isStereo());
    }

    private void assertMachinePreRestoreValues(IMachine machine) {
        assertEquals(0f, mMixerPanel.getBass(machine.getIndex(), true));
        assertEquals(0f, mMixerPanel.getMid(machine.getIndex(), true));
        assertEquals(0f, mMixerPanel.getHigh(machine.getIndex(), true));
        assertEquals(0f, mMixerPanel.getDelaySend(machine.getIndex(), true));
        assertEquals(0f, mMixerPanel.getReverbSend(machine.getIndex(), true));
        assertEquals(0f, mMixerPanel.getPan(machine.getIndex(), true));
        assertEquals(0f, mMixerPanel.getStereoWidth(machine.getIndex(), true));
        assertFalse(mMixerPanel.isMute(machine.getIndex(), true));
        assertFalse(mMixerPanel.isSolo(machine.getIndex(), true));
        assertEquals(1f, mMixerPanel.getVolume(machine.getIndex(), true));
    }

    private void assertMachineValues(IMachine machine) {
        assertEquals(-0.3f, mMixerPanel.getBass(machine.getIndex()));
        assertEquals(0.20000005f, mMixerPanel.getMid(machine.getIndex()));
        assertEquals(-0.39999998f, mMixerPanel.getHigh(machine.getIndex()));
        assertEquals(0.5f, mMixerPanel.getDelaySend(machine.getIndex()));
        assertEquals(0.3f, mMixerPanel.getReverbSend(machine.getIndex()));
        assertEquals(1f, mMixerPanel.getPan(machine.getIndex()));
        assertEquals(0.4f, mMixerPanel.getStereoWidth(machine.getIndex()));
        assertTrue(mMixerPanel.isMute(machine.getIndex()));
        assertFalse(mMixerPanel.isSolo(machine.getIndex()));
        assertEquals(1.5f, mMixerPanel.getVolume(machine.getIndex()));
    }
}

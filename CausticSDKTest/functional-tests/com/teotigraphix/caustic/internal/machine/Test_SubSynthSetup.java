
package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.filter.ILFOComponent;
import com.teotigraphix.caustic.filter.ISubSynthLFO1;
import com.teotigraphix.caustic.filter.ISubSynthLFO1.LFOTarget;
import com.teotigraphix.caustic.filter.ISubSynthLFO2;
import com.teotigraphix.caustic.filter.ISubSynthOsc1;
import com.teotigraphix.caustic.filter.ISubSynthOsc1.Waveform;
import com.teotigraphix.caustic.filter.ISubSynthOsc2;
import com.teotigraphix.caustic.filter.ISubSynthOsc2.WaveForm;
import com.teotigraphix.caustic.internal.filter.SubSynthLFO1;
import com.teotigraphix.caustic.internal.filter.SubSynthLFO2;
import com.teotigraphix.caustic.internal.filter.SubSynthOsc1;
import com.teotigraphix.caustic.internal.filter.SubSynthOsc2;
import com.teotigraphix.caustic.internal.filter.SynthFilter;
import com.teotigraphix.caustic.internal.filter.VolumeComponent;

public class Test_SubSynthSetup extends EmptyMachineTestBase {

    @Override
    protected void setupValues() {
        setupSynth((SynthComponent)subSynth1.getSynth());
        setupVolume((VolumeComponent)subSynth1.getVolume());
        setupFilter((SynthFilter)subSynth1.getFilter());
        setupOsc1((SubSynthOsc1)subSynth1.getOsc1());
        setupOsc2((SubSynthOsc2)subSynth1.getOsc2());
        setupLFO1((SubSynthLFO1)subSynth1.getLFO1());
        setupLFO2((SubSynthLFO2)subSynth1.getLFO2());
    }

    @Override
    protected void assertValues() {
        assertSynthValues(subSynth1.getSynth());
        assertVolumeValues(subSynth1.getVolume());
        assertFilterValues(subSynth1.getFilter());
        assertOsc1Values(subSynth1.getOsc1());
        assertOsc2Values(subSynth1.getOsc2());
        assertLFO1Values(subSynth1.getLFO1());
        assertLFO2Values(subSynth1.getLFO2());
    }

    //----------------------------------
    //  ISubSynthOsc1
    //----------------------------------

    private void setupOsc1(SubSynthOsc1 component) {
        assertEquals(0.0f, component.getBend(true));
        assertEquals(0.0f, component.getFM(true));
        assertEquals(0.5f, component.getMix(true));
        assertEquals(ISubSynthOsc1.Waveform.SINE, component.getWaveform(true));

        component.setWaveform(Waveform.SAW);
        component.setMix(0.7f);
        component.setFM(0.6f);
        component.setBend(0.3f);
    }

    private void assertOsc1Values(ISubSynthOsc1 component) {
        assertEquals(ISubSynthOsc1.Waveform.SAW, component.getWaveform());
        assertEquals(0.7f, component.getMix());
        assertEquals(0.6f, component.getFM());
        assertEquals(0.3f, component.getBend());
    }

    //----------------------------------
    //  ISubSynthOsc2
    //----------------------------------

    private void setupOsc2(SubSynthOsc2 component) {
        assertEquals(0, component.getCents(true));
        assertEquals(0, component.getOctave(true));
        assertEquals(0f, component.getPhase(true));
        assertEquals(0, component.getSemis(true));
        assertEquals(ISubSynthOsc2.WaveForm.NONE, component.getWaveform(true));

        component.setCents(25);
        component.setOctave(-1);
        component.setPhase(-0.2f);
        component.setSemis(-5);
        component.setWaveform(WaveForm.SQUARE);
    }

    private void assertOsc2Values(ISubSynthOsc2 component) {
        assertEquals(25, component.getCents());
        assertEquals(-1, component.getOctave());
        assertEquals(-0.2f, component.getPhase());
        assertEquals(-5, component.getSemis());
        assertEquals(ISubSynthOsc2.WaveForm.SQUARE, component.getWaveform());
    }

    //----------------------------------
    //  ISubSynthLFO1
    //----------------------------------

    private void setupLFO1(SubSynthLFO1 component) {
        assertEquals(0.0f, component.getDepth(true));
        assertEquals(1, component.getRate(true));
        assertEquals(LFOTarget.NONE, component.getTarget(true));
        assertEquals(ILFOComponent.WaveForm.SINE, component.getWaveform(true));

        component.setDepth(0.3f);
        component.setRate(8);
        component.setTarget(LFOTarget.VOLUME);
        component.setWaveForm(ILFOComponent.WaveForm.SQUARE);
    }

    private void assertLFO1Values(ISubSynthLFO1 component) {
        assertEquals(0.3f, component.getDepth());
        assertEquals(8, component.getRate());
        assertEquals(LFOTarget.VOLUME, component.getTarget());
        assertEquals(ILFOComponent.WaveForm.SQUARE, component.getWaveform());
    }

    //----------------------------------
    //  ISubSynthLFO2
    //----------------------------------

    private void setupLFO2(SubSynthLFO2 component) {
        assertEquals(0.0f, component.getDepth(true));
        assertEquals(1, component.getRate(true));
        assertEquals(ISubSynthLFO2.LFOTarget.NONE, component.getTarget(true));

        component.setDepth(0.8f);
        component.setRate(5);
        component.setTarget(ISubSynthLFO2.LFOTarget.CUTOFF);
    }

    private void assertLFO2Values(ISubSynthLFO2 component) {
        assertEquals(0.8f, component.getDepth());
        assertEquals(5, component.getRate());
        assertEquals(ISubSynthLFO2.LFOTarget.CUTOFF, component.getTarget());
    }
}

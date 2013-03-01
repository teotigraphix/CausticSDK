
package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.effect.IBasslineDistortionUnit;
import com.teotigraphix.caustic.effect.IBasslineDistortionUnit.Program;
import com.teotigraphix.caustic.filter.IBasslineFilter;
import com.teotigraphix.caustic.filter.IBasslineLFO1;
import com.teotigraphix.caustic.filter.IBasslineOSC1;
import com.teotigraphix.caustic.internal.effect.BasslineDistortionUnit;
import com.teotigraphix.caustic.internal.filter.BasslineFilter;
import com.teotigraphix.caustic.internal.filter.BasslineLFO1;
import com.teotigraphix.caustic.internal.filter.BasslineOSC1;
import com.teotigraphix.caustic.internal.filter.VolumeComponent;

public class Test_BasslineSetup extends EmptyMachineTestBase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setupValues() {
        //setupSynth(bassline1.getSynth());
        assertEquals(1, bassline1.getSynth().getPolyphony());
        setupVolume((VolumeComponent)bassline1.getVolume());
        setupBasslineOsc1Values((BasslineOSC1)bassline1.getOsc1());
        setupBasslineFilterValues((BasslineFilter)bassline1.getFilter());
        setupBasslineLFO1Values((BasslineLFO1)bassline1.getLFO1());
        setupBasslineDistortionValues((BasslineDistortionUnit)bassline1.getDistortion());
    }

    @Override
    protected void assertValues() {
        //assertSynthValues(bassline1.getSynth());
        assertEquals(1, bassline1.getSynth().getPolyphony());
        assertVolumeValues(bassline1.getVolume());
        assertBasslineOsc1Values(bassline1.getOsc1());
        assertBasslineFilterValues(bassline1.getFilter());
        assertBasslineLFO1Values(bassline1.getLFO1());
        assertBasslineDistortionValues(bassline1.getDistortion());
    }

    //----------------------------------
    //  IBasslineOSC1
    //----------------------------------

    private void setupBasslineOsc1Values(BasslineOSC1 component) {
        assertEquals(0.5f, component.getAccent(true));
        assertEquals(0.5f, component.getPulseWidth(true));
        assertEquals(0, component.getTune(true));
        assertEquals(IBasslineOSC1.Waveform.SAW, component.getWaveForm(true));

        component.setAccent(1.0f);
        component.setPulseWidth(0.35f);
        component.setTune(-12);
        component.setWaveForm(IBasslineOSC1.Waveform.SQUARE);
    }

    private void assertBasslineOsc1Values(IBasslineOSC1 component) {
        assertEquals(1.0f, component.getAccent());
        assertEquals(0.35f, component.getPulseWidth());
        assertEquals(-12, component.getTune());
        assertEquals(IBasslineOSC1.Waveform.SQUARE, component.getWaveForm());
    }

    //----------------------------------
    //  IBasslineFilter
    //----------------------------------

    private void setupBasslineFilterValues(BasslineFilter component) {
        assertEquals(0.405f, component.getCutoff(true));
        assertEquals(0.0f, component.getDecay(true));
        assertEquals(0.99f, component.getEnvMod(true));
        assertEquals(0.99f, component.getResonance(true));

        component.setCutoff(0.8f);
        component.setDecay(0.45f);
        component.setEnvMod(0.2f);
        component.setResonance(0.6f);
    }

    private void assertBasslineFilterValues(IBasslineFilter component) {
        assertEquals(0.8f, component.getCutoff());
        assertEquals(0.45f, component.getDecay());
        assertEquals(0.2f, component.getEnvMod());
        assertEquals(0.6f, component.getResonance());
    }

    //----------------------------------
    //  IBasslineLFO1
    //----------------------------------

    private void setupBasslineLFO1Values(BasslineLFO1 component) {
        assertEquals(0.0f, component.getDepth(true));
        assertEquals(0.0f, component.getPhase(true));
        assertEquals(1, component.getRate(true));
        assertEquals(IBasslineLFO1.LFOTarget.OFF, component.getTarget(true));

        component.setDepth(0.35f);
        component.setPhase(0.7f);
        component.setRate(8);
        component.setTarget(IBasslineLFO1.LFOTarget.VCF);
    }

    private void assertBasslineLFO1Values(IBasslineLFO1 component) {
        assertEquals(0.35f, component.getDepth());
        assertEquals(0.7f, component.getPhase());
        assertEquals(8, component.getRate());
        assertEquals(IBasslineLFO1.LFOTarget.VCF, component.getTarget());
    }

    //----------------------------------
    //  IBasslineDistortionEffect
    //----------------------------------

    private void setupBasslineDistortionValues(BasslineDistortionUnit component) {
        assertEquals(16.3f, component.getAmount(true));
        assertEquals(0.1f, component.getPostGain(true));
        assertEquals(4.05f, component.getPreGain(true));
        assertEquals(Program.OFF, component.getProgram(true));

        component.setAmount(11.4f);
        component.setPostGain(0.67f);
        component.setPreGain(3.0f);
        component.setProgram(Program.FOLDBACK);
    }

    private void assertBasslineDistortionValues(IBasslineDistortionUnit component) {
        assertEquals(11.4f, component.getAmount());
        assertEquals(0.67f, component.getPostGain());
        assertEquals(3.0f, component.getPreGain());
        assertEquals(Program.FOLDBACK, component.getProgram());
    }
}

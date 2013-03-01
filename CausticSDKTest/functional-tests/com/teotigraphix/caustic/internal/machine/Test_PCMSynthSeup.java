
package com.teotigraphix.caustic.internal.machine;

import java.io.File;

import com.teotigraphix.caustic.filter.ILFOComponent;
import com.teotigraphix.caustic.filter.IPCMSynthLFO1;
import com.teotigraphix.caustic.filter.IPitchTuner;
import com.teotigraphix.caustic.internal.filter.PCMSynthLFO1;
import com.teotigraphix.caustic.internal.filter.PitchTuner;
import com.teotigraphix.caustic.internal.filter.SynthFilter;
import com.teotigraphix.caustic.internal.filter.VolumeComponent;
import com.teotigraphix.caustic.sampler.IPCMSampler;
import com.teotigraphix.caustic.sampler.IPCMSampler.PlayMode;
import com.teotigraphix.caustic.sampler.IPCMSamplerChannel;
import com.teotigraphix.common.utils.RuntimeUtils;

public class Test_PCMSynthSeup extends EmptyMachineTestBase {
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
        setupSynth((SynthComponent)pcmSynth1.getSynth());
        setupVolume((VolumeComponent)pcmSynth1.getVolume());
        setupFilter((SynthFilter)pcmSynth1.getFilter());
        setupLFO((PCMSynthLFO1)pcmSynth1.getLFO1());
        setupPitch((PitchTuner)pcmSynth1.getPitch());
        setupSampler(pcmSynth1.getSampler());
    }

    private void setupSampler(IPCMSampler component) {
        assertEquals(0, component.getActiveIndex());
        File file = RuntimeUtils.getCausticSamplesFile("pcmsynth", "Cold");
        component.loadChannel(0, file.getAbsolutePath());
        component.setChannelSamplePoints(0, 5, 1500);
        component.setChannelKeys(0, 60, 61, 60);
        component.setChannelProperties(0, 0.4f, 42, PlayMode.LOOP_FWD_BACK);

        assertEquals("Cold", component.getSampleName(0));
    }

    @Override
    protected void assertValues() {
        assertSynthValues(pcmSynth1.getSynth());
        assertVolumeValues(pcmSynth1.getVolume());
        assertFilterValues(pcmSynth1.getFilter());
        assertLFOValues(pcmSynth1.getLFO1());
        assertPitchValues(pcmSynth1.getPitch());
        assertSampler(pcmSynth1.getSampler());
    }

    private void assertSampler(IPCMSampler component) {
        assertEquals(0, component.getActiveIndex());
        IPCMSamplerChannel channel = component.getActiveChannel();
        assertNotNull(channel);
        assertEquals("Cold", channel.getName());
        assertEquals(5, channel.getStart());
        assertEquals(1500, channel.getEnd());
        assertEquals(60, channel.getLowKey());
        assertEquals(61, channel.getHighKey());
        assertEquals(60, channel.getRootKey());
        assertEquals(0.4f, channel.getLevel());
        assertEquals(42, channel.getTune());
        assertEquals(PlayMode.LOOP_FWD_BACK, channel.getMode());
    }

    private void setupLFO(PCMSynthLFO1 component) {
        assertEquals(0.0f, component.getDepth(true));
        assertEquals(1, component.getRate(true));
        assertEquals(IPCMSynthLFO1.LFOTarget.NONE, component.getTarget(true));
        assertEquals(ILFOComponent.WaveForm.SINE, component.getWaveform(true));

        component.setDepth(0.3f);
        component.setRate(8);
        component.setTarget(IPCMSynthLFO1.LFOTarget.PITCH);
        component.setWaveForm(ILFOComponent.WaveForm.SAW);
    }

    private void assertLFOValues(IPCMSynthLFO1 component) {
        assertEquals(0.3f, component.getDepth());
        assertEquals(8, component.getRate());
        assertEquals(IPCMSynthLFO1.LFOTarget.PITCH, component.getTarget());
        assertEquals(ILFOComponent.WaveForm.SAW, component.getWaveform());
    }

    private void setupPitch(PitchTuner component) {
        assertEquals(0, component.getCents(true));
        assertEquals(0, component.getOctave(true));
        assertEquals(0, component.getSemis(true));

        component.setCents(-25);
        component.setOctave(3);
        component.setSemis(5);
    }

    private void assertPitchValues(IPitchTuner component) {
        assertEquals(-25, component.getCents());
        assertEquals(3, component.getOctave());
        assertEquals(5, component.getSemis());
    }
}

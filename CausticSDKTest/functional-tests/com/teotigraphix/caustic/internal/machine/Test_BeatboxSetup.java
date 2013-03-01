
package com.teotigraphix.caustic.internal.machine;

import java.io.File;

import com.teotigraphix.caustic.sampler.IBeatboxSampler;
import com.teotigraphix.caustic.sampler.IBeatboxSamplerChannel;
import com.teotigraphix.common.utils.RuntimeUtils;

public class Test_BeatboxSetup extends EmptyMachineTestBase {
    @Override
    protected void setupValues() {
        super.setupValues();

        setupSampler(beatbox.getSampler());
    }

    private void setupSampler(IBeatboxSampler sampler) {
        File file = RuntimeUtils.getCausticSamplesFile("beatbox/909", "909-Kick");
        IBeatboxSamplerChannel channel = beatbox.getSampler()
                .loadChannel(0, file.getAbsolutePath());
        channel.setDecay(0.4f);
        channel.setMute(true);
        channel.setPan(-0.5f);
        channel.setPunch(0.25f);
        channel.setSelected(true);
        channel.setSolo(false);
        channel.setTune(5f);
        channel.setVolume(1.3f);
    }

    @Override
    protected void assertValues() {
        super.assertValues();

        assertSampler(beatbox.getSampler());
    }

    private void assertSampler(IBeatboxSampler sampler) {
        IBeatboxSamplerChannel channel = beatbox.getSampler().getChannel(0);
        assertNotNull(channel);
        assertEquals("909-Kick", channel.getName());
        assertEquals(0.40000004f, channel.getDecay());
        assertTrue(channel.isMute());
        assertEquals(-0.5f, channel.getPan());
        assertEquals(0.25f, channel.getPunch());
        assertFalse(channel.isSolo());
        assertEquals(5.0f, channel.getTune());
        assertEquals(1.3f, channel.getVolume());
    }
}


package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.filter.IFilter;
import com.teotigraphix.caustic.filter.IFilter.FilterType;
import com.teotigraphix.caustic.filter.IVolumeComponent;
import com.teotigraphix.caustic.filter.IVolumeEnvelope;
import com.teotigraphix.caustic.internal.filter.SynthFilter;
import com.teotigraphix.caustic.internal.filter.VolumeComponent;
import com.teotigraphix.caustic.internal.filter.VolumeEnvelope;
import com.teotigraphix.caustic.machine.IPCMSynth;
import com.teotigraphix.caustic.machine.ISubSynth;
import com.teotigraphix.caustic.machine.ISynthComponent;
import com.teotigraphix.caustic.test.EmptyRackTestBase;

public class EmptyMachineTestBase extends EmptyRackTestBase {

    @Override
    protected void setupValues() {
    }

    @Override
    protected void assertValues() {
    }

    //----------------------------------
    //  ISynthComponent
    //----------------------------------

    protected void setupSynth(SynthComponent component) {
        assertEquals(4, component.getPolyphony(true));
        component.setPolyphony(3);
    }

    protected void assertSynthValues(ISynthComponent component) {
        assertEquals(3, component.getPolyphony());
    }

    //----------------------------------
    //  IVolumeEnvelope
    //----------------------------------

    protected void setupVolume(VolumeComponent component) {
        if (component.getDevice() instanceof IPCMSynth) {
            assertEquals(2.0f, component.getOut(true));
        } else {
            assertEquals(1.0f, component.getOut(true));
        }
        if (component instanceof IVolumeEnvelope) {
            setupVolumeEnvelope((VolumeEnvelope)component);
        }

        component.setOut(1.5f);
    }

    protected void setupVolumeEnvelope(VolumeEnvelope component) {
        assertEquals(0.0f, component.getAttack(true));
        assertEquals(0.0f, component.getDecay(true));
        assertEquals(0.0f, component.getRelease(true));
        assertEquals(1.0f, component.getSustain(true));

        component.setAttack(1.54f);
        component.setDecay(0.8f);
        component.setRelease(0.9f);
        component.setSustain(0.42f);
    }

    protected void assertVolumeValues(IVolumeComponent component) {
        assertEquals(1.5f, component.getOut());
        if (component instanceof IVolumeEnvelope) {
            assertVolumeEnvelopValues((IVolumeEnvelope)component);
        }
    }

    protected void assertVolumeEnvelopValues(IVolumeEnvelope component) {
        assertEquals(1.54f, component.getAttack());
        assertEquals(0.8f, component.getDecay());
        assertEquals(0.9f, component.getRelease());
        assertEquals(0.42f, component.getSustain());
    }

    //----------------------------------
    //  IFilter
    //----------------------------------

    protected void setupFilter(SynthFilter component) {
        assertEquals(0.0f, component.getAttack(true));
        assertEquals(1.0f, component.getCutoff(true));
        assertEquals(0.0f, component.getDecay(true));
        assertEquals(1.75f, component.getRelease(true));
        assertEquals(0.0f, component.getResonance(true));
        assertEquals(1.0f, component.getSustain(true));
        if (component.getDevice() instanceof ISubSynth) {
            assertEquals(0.0f, component.getTrack(true));
        }
        assertEquals(FilterType.NONE, component.getType(true));

        component.setAttack(0.9f);
        component.setCutoff(0.4f);
        component.setDecay(1.4f);
        component.setRelease(1.45f);
        component.setResonance(0.5f);
        component.setSustain(0.8f);
        component.setTrack(0.4f);
        component.setType(FilterType.INV_BP);
    }

    protected void assertFilterValues(IFilter component) {
        assertEquals(0.9f, component.getAttack());
        assertEquals(0.4f, component.getCutoff());
        assertEquals(1.4f, component.getDecay());
        assertEquals(1.45f, component.getRelease());
        assertEquals(0.5f, component.getResonance());
        assertEquals(0.8f, component.getSustain());
        if (component.getDevice() instanceof ISubSynth) {
            assertEquals(0.4f, component.getTrack());
        }
        assertEquals(FilterType.INV_BP, component.getType());
    }
}

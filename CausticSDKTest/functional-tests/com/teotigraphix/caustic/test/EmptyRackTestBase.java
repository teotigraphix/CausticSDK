
package com.teotigraphix.caustic.test;

import java.io.File;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.internal.mixer.MixerPanel;
import com.teotigraphix.caustic.internal.output.OutputPanel;
import com.teotigraphix.caustic.machine.IBassline;
import com.teotigraphix.caustic.machine.IBeatbox;
import com.teotigraphix.caustic.machine.IPCMSynth;
import com.teotigraphix.caustic.machine.ISubSynth;
import com.teotigraphix.caustic.sequencer.ISequencer;

public abstract class EmptyRackTestBase extends RackTestDeviceBase {

    static File mGeneratedSongFile = new File(mSongsDirectory, "GENERATED.caustic");

    public static final int INDEX_SUBSYNTH = 0;

    public static final int INDEX_PCMSYNTH1 = 1;

    public static final int INDEX_PCMSYNTH2 = 2;

    public static final int INDEX_BASSLINE1 = 3;

    public static final int INDEX_BASSLINE2 = 4;

    public static final int INDEX_BEATBOX = 5;

    protected OutputPanel mOutputPanel;

    protected MixerPanel mMixerPanel;

    protected ICausticEngine mEngine;

    protected IEffectsRack mEffectsRack;

    protected ISequencer mSequencer;

    protected ISubSynth subSynth1;

    protected IPCMSynth pcmSynth1;

    protected IPCMSynth pcmSynth2;

    protected IBassline bassline1;

    protected IBassline bassline2;

    protected IBeatbox beatbox;

    @Override
    protected void loadEmptyCausticFile() throws CausticException {
        super.loadEmptyCausticFile();

        resetDevices();
        resetMachines();
    }

    private void resetDevices() {
        mEngine = rack.getEngine();
        assertNotNull(mEngine);
        mOutputPanel = (OutputPanel)rack.getOutputPanel();
        assertNotNull(mOutputPanel);
        mMixerPanel = (MixerPanel)rack.getMixerPanel();
        assertNotNull(mMixerPanel);
        mEffectsRack = rack.getEffectsRack();
        assertNotNull(mEffectsRack);
        mSequencer = rack.getSequencer();
        assertNotNull(mSequencer);
    }

    private void resetMachines() {
        subSynth1 = (ISubSynth)rack.getMachine(INDEX_SUBSYNTH);
        assertNotNull(subSynth1);
        assertEquals(INDEX_SUBSYNTH, subSynth1.getIndex());
        pcmSynth1 = (IPCMSynth)rack.getMachine(INDEX_PCMSYNTH1);
        assertNotNull(pcmSynth1);
        assertEquals(INDEX_PCMSYNTH1, pcmSynth1.getIndex());
        pcmSynth2 = (IPCMSynth)rack.getMachine(INDEX_PCMSYNTH2);
        assertNotNull(pcmSynth2);
        assertEquals(INDEX_PCMSYNTH2, pcmSynth2.getIndex());
        bassline1 = (IBassline)rack.getMachine(INDEX_BASSLINE1);
        assertNotNull(bassline1);
        assertEquals(INDEX_BASSLINE1, bassline1.getIndex());
        bassline2 = (IBassline)rack.getMachine(INDEX_BASSLINE2);
        assertNotNull(bassline2);
        assertEquals(INDEX_BASSLINE2, bassline2.getIndex());
        beatbox = (IBeatbox)rack.getMachine(INDEX_BEATBOX);
        assertNotNull(beatbox);
        assertEquals(INDEX_BEATBOX, beatbox.getIndex());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Template test for subclasses to implement hooks of {@link #setupValues()}
     * and {@link #assertValues()}.
     * 
     * @throws CausticException
     */
    public void test_loadSong_generated() throws CausticException {
        // reset the state to the empty caustic file
        // called in setUp()
        resetAndLoad_EMPTY_CausticFile();
        // subclasses setup all values for the test
        setupValues();
        // save the GENERATED song file with the setup values
        saveSong();
        // reset the startup service, factory, project and rack
        reset();
        // reset the main device references
        resetDevices();
        // load the GENERATED caustic file back into memory
        // which will then call restore on the whole hierarchy
        loadSong();
        // reset the machine references AFTER the restore call
        resetMachines();
        // subclasses assert that the values are the same as when the file was saved
        assertValues();
    }

    private void saveSong() throws CausticException {
        rack.saveSong("GENERATED");
    }

    private void loadSong() {
        resetDevices();
        // the onSongLoaded() event will call the project.restore()
        // which then calls the rack.resotre()
        try {
            rack.loadSong(mGeneratedSongFile.getAbsolutePath());
            rack.restore();
        } catch (CausticException e) {
            fail("Load song error " + e.getMessage());
        }
    }

    /**
     * Asserts default EMPTY.caustic values for the component and then will
     * setup new values for assertion in {@link #assertValues()} method to test
     * that the saved GENERATED.caustic file loaded them correctly.
     */
    protected void setupValues() {
    }

    /**
     * Asserts the values saved in the GENERATED.caustic file.
     */
    protected void assertValues() {
    }
}

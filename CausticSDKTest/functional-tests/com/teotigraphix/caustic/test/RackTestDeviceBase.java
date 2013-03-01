
package com.teotigraphix.caustic.test;

import java.io.File;

import roboguice.RoboGuice;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.Inject;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.rack.Rack;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.utils.RuntimeUtils;

/**
 * Loads the EMPTY.caustic file asserts defaults, sets new values and then saves
 * to the GENERATED.caustic file, reloads the GENERATED.caustic file and asserts
 * the values previously set before saving.
 */
public abstract class RackTestDeviceBase extends
        ActivityInstrumentationTestCase2<CausticTestActivity> {

    private static final String EMPTY_CAUSTIC = "EMPTY";

    private static final String NAME_BEATBOX = "Beatbox";

    private static final String NAME_SUB_SYNTH = "SubSynth";

    private static final String NAME_PCM_SYNTH1 = "PCMSynth1";

    private static final String NAME_PCM_SYNTH2 = "PCMSynth2";

    private static final String NAME_BASSLINE1 = "Bassline1";

    private static final String NAME_BASSLINE2 = "Bassline2";

    protected static File mSongsDirectory = RuntimeUtils.getDirectory("/caustic/songs/");

    protected static File mSamplesDirectory = RuntimeUtils.getDirectory("/caustic/samples/");

    protected static File mPresetsDirectory = RuntimeUtils.getDirectory("/caustic/presets/");

    protected static File mEmptySongFile = new File(mSongsDirectory, "EMPTY.caustic");

    public static File getEmptySongFile() {
        return mEmptySongFile;
    }

    /**
     * The single test project.
     */
    protected static IProject project;

    /**
     * The single test rack.
     */
    protected static Rack rack;

    @Inject
    IWorkspace workspace;

    //private ISoundGenerator generator;

    private CausticTestActivity mActivity;

    protected Rack getRack() {
        return rack;
    }

    protected static boolean mEmptyLoaded;

    public RackTestDeviceBase() {
        super(CausticTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        assertTrue(mSongsDirectory.isDirectory());

        //if (!mEmptyLoaded) {
        resetAndLoad_EMPTY_CausticFile();
        //	mEmptyLoaded = true;
        //}
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    /**
     * Resets the factory instances and loads the test EMPTY.caustic file.
     * 
     * @see #reset()
     * @see #loadEmptyCausticFile()
     * @throws CausticException
     */
    protected void resetAndLoad_EMPTY_CausticFile() throws CausticException {
        reset();
        loadEmptyCausticFile();
        assertEquals(NAME_SUB_SYNTH, rack.getMachine(0).getId());
        assertEquals(NAME_PCM_SYNTH1, rack.getMachine(1).getId());
        assertEquals(NAME_PCM_SYNTH2, rack.getMachine(2).getId());
        assertEquals(NAME_BASSLINE1, rack.getMachine(3).getId());
        assertEquals(NAME_BASSLINE2, rack.getMachine(4).getId());
        assertEquals(NAME_BEATBOX, rack.getMachine(5).getId());
    }

    /**
     * Resets the {@link #factory}, {@link #project} and {@link #rack}.
     * 
     * @throws CausticException
     */
    protected void reset() throws CausticException {
        // creates the runtime installer
        //        mActivity = getActivity();
        //
        //        workspace = new Workspace(mActivity, "TEST_APP");
        //
        //        rack = (Rack)workspace.getRack();
        //        project = workspace.createProject(workspace, "TestProject", "Foo Project");
        //        generator = workspace.getGenerator();
        //
        //        // install(), boot(), run()
        //        workspace.startAndRun();
        //
        //        assertNotNull(project);
        //        assertNotNull(rack);
        //        assertNotNull(generator);
        mActivity = getActivity();
        RoboGuice.injectMembers(mActivity, this);

        rack = (Rack)workspace.getRack();
        rack.createSong("Foo", "Bar");
    }

    protected void loadEmptyCausticFile() throws CausticException {
        createEmptyFile();
        // /caustic/load_song /path/to/caustic/songs/EMPTY.caustic
        rack.loadSong(mEmptySongFile.getAbsolutePath());
    }

    private void createEmptyFile() throws CausticException {
        rack.addMachine(NAME_SUB_SYNTH, MachineType.SUBSYNTH);
        rack.addMachine(NAME_PCM_SYNTH1, MachineType.PCMSYNTH);
        rack.addMachineAt(2, NAME_PCM_SYNTH2, MachineType.PCMSYNTH);
        rack.addMachine(NAME_BASSLINE1, MachineType.BASSLINE);
        rack.addMachineAt(4, NAME_BASSLINE2, MachineType.BASSLINE);
        rack.addMachine(NAME_BEATBOX, MachineType.BEATBOX);
        rack.saveSong(EMPTY_CAUSTIC);
    }
}


package com.teotigraphix.caustic.internal.part;

import java.io.File;

import roboguice.RoboGuice;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.Inject;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.filter.IFilter.FilterType;
import com.teotigraphix.caustic.machine.ISubSynth;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.part.ISynthPart;
import com.teotigraphix.caustic.rack.IRack.OnSongStateChangeListener;
import com.teotigraphix.caustic.rack.IRack.SongStateChangeKind;
import com.teotigraphix.caustic.rack.IRackSong;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.caustic.test.CausticTestActivity;
import com.teotigraphix.common.utils.RuntimeUtils;

public class Test_SoundGenerator extends ActivityInstrumentationTestCase2<CausticTestActivity> {

    protected static File mSongsDirectory = RuntimeUtils.getDirectory("/caustic/songs/");

    protected static File mGeneratedSongFile = new File(mSongsDirectory, "FUNC_TEST.caustic");

    private CausticTestActivity mActivity;

    @Inject
    IWorkspace workspace;

    private ISoundGenerator generator;

    public Test_SoundGenerator() {
        super(CausticTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mSongsDirectory = RuntimeUtils.getDirectory("/caustic/songs/");
        mGeneratedSongFile = new File(mSongsDirectory, "FUNC_TEST.caustic");

        mActivity = getActivity();
        RoboGuice.injectMembers(mActivity, this);
        generator = workspace.getGenerator();

        @SuppressWarnings("unused")
        IRackSong currentSong = workspace.getRack().createSong("FUNC_TEST",
                mGeneratedSongFile.getAbsolutePath());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //----------------------------------
    // part API
    //----------------------------------

    public void test_getPart() throws CausticException {
        IPart part = generator.create("foo1", "Foo 1", MachineType.SUBSYNTH, null);
        assertNotNull(part);
        assertSame(part, generator.getPart(0));
        assertEquals("foo1", generator.getPart(0).getId());
        assertEquals("Foo 1", generator.getPart(0).getName());
    }

    public void test_getParts() throws CausticException {
        createMockParts();
        assertEquals(6, generator.getParts().size());
        // test order
        assertEquals("foo1", generator.getPart(0).getId());
        assertEquals("foo2", generator.getPart(1).getId());
        assertEquals("foo3", generator.getPart(2).getId());
        assertEquals("foo4", generator.getPart(3).getId());
        assertEquals("foo5", generator.getPart(4).getId());
        assertEquals("foo6", generator.getPart(5).getId());
    }

    public void test_create() throws CausticException {
        IPart part = generator.create("foo1", "Foo 1", MachineType.SUBSYNTH, null);
        assertNotNull(part);
        assertSame(part, generator.getPart(0));
        assertEquals("foo1", generator.getPart(0).getId());
        assertEquals("Foo 1", generator.getPart(0).getName());
    }

    public void test_load() throws CausticException {

        createFuncTest();

        IRackSong lastSong = workspace.getRack().getSong();
        assertEquals("FUNC_TEST", lastSong.getName());
        workspace.getRack().setOnSongStateChangeListener(new OnSongStateChangeListener() {
            @Override
            public void onSongStateChanged(IRackSong song, SongStateChangeKind kind) {
                if (kind == SongStateChangeKind.REMOVED) {
                    assertEquals(0, workspace.getRack().getNumMachines());
                    assertEquals(0, generator.getParts().size());
                }
            }
        });

        IRackSong testSong = workspace.getRack().loadSong(mGeneratedSongFile.getAbsolutePath());

        assertNotSame(lastSong, testSong);

        assertEquals(6, generator.getParts().size());

        // test the song properties
        assertEquals("foo1", generator.getPart(0).getName());
        assertEquals("foo2", generator.getPart(1).getName());
        assertEquals("foo3", generator.getPart(2).getName());
        assertEquals("foo4", generator.getPart(3).getName());
        assertEquals("foo5", generator.getPart(4).getName());
        assertEquals("foo6", generator.getPart(5).getName());
        // the rack and machines have NOT been restored yet
        assertFalse(workspace.getRack().isRestored());

        workspace.getRack().restore();

        assertTrue(workspace.getRack().isRestored());
        ISynthPart part2 = (ISynthPart)generator.getPart(1);
        ISubSynth synth = (ISubSynth)part2.getTone();
        assertEquals(FilterType.INV_HP, synth.getFilter().getType());

        // cleanup
        boolean result = mGeneratedSongFile.delete();
        assertTrue(result);
        assertFalse(mGeneratedSongFile.exists());
    }

    //----------------------------------
    // mute status
    //----------------------------------

    public void test_allMute() throws CausticException {
        createMockParts();
        for (IPart part : generator.getParts()) {
            assertFalse(part.isMute());
        }
        assertEquals(0, generator.getMuteParts().size());
        generator.allMute();
        assertEquals(6, generator.getMuteParts().size());
        for (IPart part : generator.getMuteParts()) {
            assertTrue(part.isMute());
        }
    }

    public void test_noMute() throws CausticException {
        createMockParts();
        generator.allMute();
        assertEquals(6, generator.getMuteParts().size());
        generator.noMute();
        assertEquals(0, generator.getMuteParts().size());
        for (IPart part : generator.getMuteParts()) {
            assertFalse(part.isMute());
        }
    }

    public void test_inverseMute() throws CausticException {
        createMockParts();
        generator.getPart(1).setMute(true);
        generator.getPart(3).setMute(true);
        generator.inverseMute();
        assertTrue(generator.getPart(0).isMute());
        assertFalse(generator.getPart(1).isMute());
        assertTrue(generator.getPart(2).isMute());
        assertFalse(generator.getPart(3).isMute());
        assertTrue(generator.getPart(4).isMute());
        assertTrue(generator.getPart(5).isMute());
        generator.inverseMute();
        assertFalse(generator.getPart(0).isMute());
        assertTrue(generator.getPart(1).isMute());
        assertFalse(generator.getPart(2).isMute());
        assertTrue(generator.getPart(3).isMute());
        assertFalse(generator.getPart(4).isMute());
        assertFalse(generator.getPart(5).isMute());
    }

    //--------------------------------------------------------------------------

    private void createFuncTest() throws CausticException {
        assertFalse(mGeneratedSongFile.exists());
        createMockParts();

        ISynthPart part2 = (ISynthPart)generator.getPart(1);
        assertEquals("foo2", part2.getId());
        assertEquals("Foo 2", part2.getName());
        ISubSynth synth = (ISubSynth)part2.getTone();
        synth.getFilter().setType(FilterType.INV_HP);

        workspace.getRack().saveSong("FUNC_TEST");
        assertTrue(mGeneratedSongFile.exists());
        // machine ADD from IRack calls generator.load() or create() ?
        assertEquals(6, generator.getParts().size());
    }

    private void createMockParts() throws CausticException {
        // When using the IWorkspace, IRack.addMachine() is NOT used
        // the ISoundGenerator.create() is used instead, creating the IMachine and IPart
        generator.create("foo1", "Foo 1", MachineType.SUBSYNTH, null);
        generator.create("foo2", "Foo 2", MachineType.SUBSYNTH, null);
        generator.create("foo3", "Foo 3", MachineType.BASSLINE, null);
        generator.create("foo4", "Foo 4", MachineType.BASSLINE, null);
        generator.create("foo5", "Foo 5", MachineType.PCMSYNTH, null);
        generator.create("foo6", "Foo 6", MachineType.BEATBOX, null);

        assertEquals(6, workspace.getRack().getNumMachines());
        assertEquals(6, generator.getParts().size());
    }
}

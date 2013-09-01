
package com.teotigraphix.caustk.library;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.pattern.PatternUtils;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;

@Ignore
public class PatternLibraryTest {
    // PART1A, PART2A, PART3A, PART1B, PART2B, PART3B
    private static final File A001 = new File(
            "src/test/java/com/teotigraphix/caustk/library/BL303A001.caustic");

    private ICaustkApplication application;

    private ICaustkController controller;

    private ILibraryManager libraryManager;

    private Library library;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        libraryManager = controller.getLibraryManager();

        library = libraryManager.createLibrary("A001");
        libraryManager.importPatterns(library, A001);

        libraryManager.saveLibrary(library);

    }

    @After
    public void tearDown() throws Exception {
        application = null;
        controller = null;
    }

    @Test
    public void test_bank_index() {
        assertEquals(0, PatternUtils.getBank(15));
        assertEquals(1, PatternUtils.getBank(31));
        assertEquals(2, PatternUtils.getBank(46));
        assertEquals(3, PatternUtils.getBank(50));
        assertEquals(2, PatternUtils.getBank(98));
        assertEquals(3, PatternUtils.getBank(120));

        assertEquals(15, PatternUtils.getPattern(31));
        assertEquals(0, PatternUtils.getPattern(32));
        assertEquals(15, PatternUtils.getPattern(127));
        assertEquals(0, PatternUtils.getPattern(128));

        //int sets = 128 / 64; // 2
        //int banks = 128 / 16; // 8

        // 27 [B12] bank=1, pattern=11 0 index
    }

    @Test
    public void test_me() {
        List<LibraryPatch> list1 = library.findPatchesByTagStartsWith("PART1");
        assertEquals(2, list1.size());
        List<LibraryPatch> list2 = library.findPatchesByTagStartsWith("PART2");
        assertEquals(2, list2.size());
        List<LibraryPatch> list3 = library.findPatchesByTagStartsWith("PART3");
        assertEquals(2, list3.size());

        List<LibraryPattern> patterns = library.getPatterns();
        assertEquals(128, patterns.size());

        controller.getLibraryManager().setSelectedLibrary(library);

        controller.getPatternManager().playPattern(1);

        controller.getSystemSequencer().play(SequencerMode.PATTERN);

        controller.getPatternManager().playPattern(0);

        controller.getPatternManager().playPattern(63);

        controller.getPatternManager().playPattern(0);

        controller.getPatternManager().playPattern(64);
    }
}

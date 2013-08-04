
package com.teotigraphix.caustk.pattern;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.pattern.Phrase.Scale;
import com.teotigraphix.caustk.pattern.Phrase.Trigger;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;

@Ignore
public class TestPhrase {

    private Pattern pattern;

    private Part part1;

    private ICaustkApplication application;

    private ICaustkController controller;

    private ILibraryManager libraryManager;

    @Before
    public void setUp() throws Exception {
        // plain ole caustk app and config
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        libraryManager = controller.getLibraryManager();

        Library library = libraryManager.createLibrary("bazx");
        assertTrue(library.getDirectory().exists());
        File causticFile = new File(
                "src/test/java/com/teotigraphix/caustk/pattern/BL303A001.caustic");
        controller.getLibraryManager().importPatterns(library, causticFile);
        controller.getLibraryManager().saveLibrary(library);
        controller.getLibraryManager().setSelectedLibrary(library);

        //controller.getMemoryManager().getSelectedMemoryBank().load(library);

        controller.getSoundSource().createScene(library.getScenes().get(1));

        assertNull(controller.getPatternManager().getPattern());
        pattern = controller.getPatternManager().playPattern(0);
        pattern.setLength(1);
        part1 = pattern.getPart(0);
        assertNotNull(part1);

        controller.getSystemSequencer().play(SequencerMode.PATTERN);
    }

    @After
    public void tearDown() throws Exception {
        application = null;
        controller = null;
    }

    @Test
    public void test_createLibrary() throws CausticException, IOException {

    }

    @Test
    public void test_triggerMap_setup() throws IOException {
        assertSame(part1, part1.getPhrase().getPart());
        assertEquals(Scale.SIXTEENTH, part1.getPhrase().getScale());
        assertEquals(Resolution.SIXTEENTH, part1.getPhrase().getResolution());
        assertEquals(1, part1.getPhrase().getPosition());
        assertEquals(1, part1.getPhrase().getLength());
        assertEquals(16, part1.getPhrase().getStepCount());
    }

    @Test
    public void test_length() {
        // add a selection at step 4 and 12, maintain through all
        Phrase phrase = part1.getPhrase();

        phrase.triggerOn(3, 60, 0.5f, 1f, 0);
        phrase.triggerOn(11, 60, 0.5f, 1f, 0);
        assertTrue(phrase.isSelected(3));
        assertFalse(phrase.isSelected(4));
        assertTrue(phrase.isSelected(11));

        assertEquals(1, phrase.getLength());
        // change to [2]
        phrase.setLength(2);
        assertEquals(2, phrase.getLength());
        assertEquals(32, phrase.getStepCount());
        // change to [1]
        phrase.setLength(1);
        assertEquals(1, phrase.getLength());
        assertEquals(16, phrase.getStepCount());
        // change to [4]
        phrase.setLength(4);
        assertEquals(4, phrase.getLength());
        assertEquals(64, phrase.getStepCount());

        // test that setting a trigger and then removing it, really removes it
        phrase.triggerOn(32, 60, 1f, 1f, 0);
        assertTrue(phrase.isSelected(32));

        // change to [2]
        phrase.setLength(2);
        assertEquals(2, phrase.getLength());
        assertEquals(32, phrase.getStepCount());
        // change to [8]
        phrase.setLength(8);
        assertEquals(8, phrase.getLength());
        assertEquals(128, phrase.getStepCount());

        // reassert that this trigger actually was removed when the length was truncated
        //        assertFalse(phrase.isSelected(32));

        assertTrue(phrase.isSelected(3));
        assertFalse(phrase.isSelected(4));
        assertTrue(phrase.isSelected(11));
    }

    @Test
    public void test_position_viewtriggers() {
        Phrase phrase = part1.getPhrase();
        // cannot set a position greater then the length
        phrase.setPosition(3);
        assertEquals(1, phrase.getPosition());
        phrase.setPosition(-3);
        assertEquals(1, phrase.getPosition());

        phrase.setLength(2);

        assertEquals(1, phrase.getPosition());
        assertEquals(2, phrase.getLength());

        assertEquals(32, phrase.getSteps().size());

        // set a trigger that will appear in the position 2 view
        phrase.triggerOn(20, 42, 0f, 0f, 0);

        // position 1 view
        List<Trigger> steps = phrase.getViewSteps();
        assertEquals(16, steps.size());
        assertEquals(0, steps.get(0).getStep());
        assertEquals(15, steps.get(15).getStep());

        // position 2 view
        phrase.setPosition(2);
        steps = phrase.getViewSteps();
        assertEquals(16, steps.size());
        assertEquals(16, steps.get(0).getStep());
        assertEquals(31, steps.get(15).getStep());

        // test that we have the trigger 20 at 4
        assertEquals(20, steps.get(4).getStep());
    }

    @Test
    public void test_scale() {
        // TODO Impl unit test Phrase.setScale()
    }

    @Test
    public void test_Resolution() {
        // SIXTEENTH

        assertEquals(0f, Resolution.toBeat(0, Resolution.SIXTEENTH), 0f);
        assertEquals(1.0f, Resolution.toBeat(4, Resolution.SIXTEENTH), 0f);
        assertEquals(2.0f, Resolution.toBeat(8, Resolution.SIXTEENTH), 0f);
        assertEquals(3.0f, Resolution.toBeat(12, Resolution.SIXTEENTH), 0f);

        assertEquals(3.75f, Resolution.toBeat(15, Resolution.SIXTEENTH), 0f);

        assertEquals(0, Resolution.toStep(0f, Resolution.SIXTEENTH));
        assertEquals(4, Resolution.toStep(1.0f, Resolution.SIXTEENTH));
        assertEquals(8, Resolution.toStep(2.0f, Resolution.SIXTEENTH));
        assertEquals(12, Resolution.toStep(3.0f, Resolution.SIXTEENTH));

        // THIRTYSECOND

        assertEquals(0f, Resolution.toBeat(0, Resolution.THIRTYSECOND), 0f);
        assertEquals(0.5f, Resolution.toBeat(4, Resolution.THIRTYSECOND), 0f);
        assertEquals(1.0f, Resolution.toBeat(8, Resolution.THIRTYSECOND), 0f);
        assertEquals(1.5f, Resolution.toBeat(12, Resolution.THIRTYSECOND), 0f);

        assertEquals(0, Resolution.toStep(0f, Resolution.THIRTYSECOND));
        assertEquals(4, Resolution.toStep(0.5f, Resolution.THIRTYSECOND));
        assertEquals(8, Resolution.toStep(1.0f, Resolution.THIRTYSECOND));
        assertEquals(12, Resolution.toStep(1.5f, Resolution.THIRTYSECOND));

        assertEquals(3.875f, Resolution.toBeat(31, Resolution.THIRTYSECOND), 0f);
        assertEquals(4.0f, Resolution.toBeat(32, Resolution.THIRTYSECOND), 0f);

        // SIXTYFOURTH

        assertEquals(0f, Resolution.toBeat(0, Resolution.SIXTYFOURTH), 0f);
        assertEquals(0.25f, Resolution.toBeat(4, Resolution.SIXTYFOURTH), 0f);
        assertEquals(0.5f, Resolution.toBeat(8, Resolution.SIXTYFOURTH), 0f);
        assertEquals(0.75f, Resolution.toBeat(12, Resolution.SIXTYFOURTH), 0f);

        assertEquals(0, Resolution.toStep(0f, Resolution.SIXTYFOURTH));
        assertEquals(4, Resolution.toStep(0.25f, Resolution.SIXTYFOURTH));
        assertEquals(8, Resolution.toStep(0.5f, Resolution.SIXTYFOURTH));
        assertEquals(12, Resolution.toStep(0.75f, Resolution.SIXTYFOURTH));
    }

    @Test
    public void test_incDec_positon() {
        Phrase phrase = part1.getPhrase();
        assertEquals(1, phrase.getPosition());
        phrase.incrementPosition();
        assertEquals(1, phrase.getPosition());
        phrase.setLength(8);
        phrase.incrementPosition();
        assertEquals(2, phrase.getPosition());
        phrase.incrementPosition();
        phrase.incrementPosition();
        phrase.incrementPosition();
        phrase.incrementPosition();
        assertEquals(6, phrase.getPosition());
        phrase.incrementPosition();
        phrase.incrementPosition();
        phrase.incrementPosition(); // would be 9
        assertEquals(8, phrase.getPosition());
        phrase.decrementPosition();
        phrase.decrementPosition();
        phrase.decrementPosition();
        phrase.decrementPosition();
        phrase.decrementPosition();
        phrase.decrementPosition();
        phrase.decrementPosition();
        assertEquals(1, phrase.getPosition());
        phrase.decrementPosition(); // would be 0
        assertEquals(1, phrase.getPosition());
        phrase.incrementPosition();
        phrase.incrementPosition();
        phrase.incrementPosition();
        phrase.incrementPosition();
        phrase.incrementPosition(); // 6
        assertEquals(6, phrase.getPosition());
        phrase.setLength(2); // lower length adjusts position
        assertEquals(2, phrase.getPosition());
        phrase.incrementPosition();
        assertEquals(2, phrase.getPosition());
    }

    @Test
    public void test_isSelected() {
        Phrase phrase = part1.getPhrase();
        assertEquals(16, phrase.getSteps().size());
        for (Trigger trigger : phrase.getSteps()) {
            assertFalse(phrase.isSelected(trigger.getStep()));
        }
        phrase.triggerOn(4, 60, 0f, 0f, 0);
        phrase.isSelected(4);
    }

    @Test
    public void test_transpose() {
        // XXX For this simple algorithm to work, each keyboard key
        // MEUST be assigned the correct MIDI note integer 'keyPressed'
        Phrase phrase = part1.getPhrase();
        int keyPressed = 72;
        int lastKeyPressed = 60; // root
        int semitones = keyPressed - lastKeyPressed;
        phrase.transpose(semitones);
        for (Trigger trigger : phrase.getSteps()) {
            assertEquals(72, trigger.getPitch());
        }
        //-------------
        lastKeyPressed = keyPressed;
        keyPressed = 58;
        semitones = keyPressed - lastKeyPressed;
        assertEquals(-14, semitones);
        phrase.transpose(semitones);
        for (Trigger trigger : phrase.getSteps()) {
            assertEquals(58, trigger.getPitch());
        }
        // back up to root C
        lastKeyPressed = keyPressed;
        keyPressed = 60;
        semitones = keyPressed - lastKeyPressed;
        assertEquals(2, semitones);
        phrase.transpose(semitones);
        for (Trigger trigger : phrase.getSteps()) {
            assertEquals(60, trigger.getPitch());
        }
    }

    @Test
    public void test_triggerOnOff() {
        Phrase phrase = part1.getPhrase();
        phrase.triggerOn(4, 65, 0.5f, 1f, 2);
        Trigger trigger = phrase.getStep(4);
        assertEquals(4, trigger.getStep());
        assertEquals(65, trigger.getPitch());
        assertEquals(0.5f, trigger.getGate(), 0f);
        assertEquals(1f, trigger.getVelocity(), 0f);
        assertEquals(2, trigger.getFlags());
        assertTrue(phrase.isSelected(4));
        assertTrue(trigger.isSelected());

        // turn it off, should affect non of the trigger's properties
        phrase.triggerOff(4);
        assertEquals(4, trigger.getStep());
        assertEquals(65, trigger.getPitch());
        assertEquals(0.5f, trigger.getGate(), 0f);
        assertEquals(1f, trigger.getVelocity(), 0f);
        assertEquals(2, trigger.getFlags());
        assertFalse(phrase.isSelected(4));
        assertFalse(trigger.isSelected());
    }

    @Test
    public void test_triggerGate() {
        Phrase phrase = part1.getPhrase();
        Trigger step = phrase.getStep(0);
        assertEquals(0.25f, step.getGate(), 0f);
        assertEquals(0.3125f, PhraseUtils.incrementGate(phrase, step), 0f);
        assertEquals(0.3125f, step.getGate(), 0f);
        assertEquals("1.25", PhraseUtils.toStepDecimalString(step.getGate()));

        assertEquals(0.375f, PhraseUtils.incrementGate(phrase, step), 0f);
        assertEquals(0.4375f, PhraseUtils.incrementGate(phrase, step), 0f);
        assertEquals(0.5f, PhraseUtils.incrementGate(phrase, step), 0f);
        assertEquals("2.0", PhraseUtils.toStepDecimalString(step.getGate()));

        assertEquals(0.4375f, PhraseUtils.decrementGate(phrase, step), 0f);
        assertEquals(0.375f, PhraseUtils.decrementGate(phrase, step), 0f);
        assertEquals(0.3125f, PhraseUtils.decrementGate(phrase, step), 0f);
        assertEquals(0.25f, PhraseUtils.decrementGate(phrase, step), 0f);
        assertEquals("1.0", PhraseUtils.toStepDecimalString(step.getGate()));

        // XXX unit test the end maximum of a step gate
        step = phrase.getStep(15);
        //assertEquals(0.375f, PhraseUtils.incrementGate(phrase, step), 0f);
    }

    @Test
    public void test_triggerUpdate() {
        Phrase phrase = part1.getPhrase();
        phrase.triggerOn(4, 65, 0.5f, 1f, 2);
        Trigger trigger = phrase.getStep(4);
        assertEquals(4, trigger.getStep());
        assertEquals(65, trigger.getPitch());
        assertEquals(0.5f, trigger.getGate(), 0f);
        assertEquals(1f, trigger.getVelocity(), 0f);
        assertEquals(2, trigger.getFlags());
        assertTrue(phrase.isSelected(4));
        assertTrue(trigger.isSelected());

        phrase.triggerUpdate(4, 45, 0.5f, 1f, 2);
        trigger = phrase.getStep(4);
        assertEquals(4, trigger.getStep());
        assertEquals(45, trigger.getPitch());
        assertEquals(0.5f, trigger.getGate(), 0f);
        assertEquals(1f, trigger.getVelocity(), 0f);
        assertEquals(2, trigger.getFlags());
        assertTrue(phrase.isSelected(4));
        assertTrue(trigger.isSelected());

        phrase.triggerUpdateGate(4, 4f);
        trigger = phrase.getStep(4);
        assertEquals(4, trigger.getStep());
        assertEquals(45, trigger.getPitch());
        assertEquals(4f, trigger.getGate(), 0f);
        assertEquals(1f, trigger.getVelocity(), 0f);
        assertEquals(2, trigger.getFlags());
        assertTrue(phrase.isSelected(4));
        assertTrue(trigger.isSelected());
    }
}

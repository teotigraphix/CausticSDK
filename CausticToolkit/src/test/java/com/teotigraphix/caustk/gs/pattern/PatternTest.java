
package com.teotigraphix.caustk.gs.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.pattern.Phrase.Scale;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class PatternTest extends CaustkTestBase {

    private BasslineTone tone1;

    private BasslineTone tone2;

    private Pattern pattern;

    private Part part1;

    private Part part2;

    @SuppressWarnings("unused")
    @Override
    protected void start() throws CausticException, IOException {
        tone1 = controller.getSoundSource().createTone("part1", BasslineTone.class);
        tone2 = controller.getSoundSource().createTone("part1", BasslineTone.class);

        pattern = new Pattern(controller, null);

        part1 = new Part(tone1);
        Phrase phrase1 = new Phrase(part1, null);

        part2 = new Part(tone2);
        Phrase phrase2 = new Phrase(part2, null);

        part1.setPattern(pattern);
        part2.setPattern(pattern);
    }

    @Override
    protected void end() {

    }

    @Test
    public void test_init() {
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
        Phrase phrase1 = part1.getPhrase();
        // cannot set a position greater then the length
        phrase1.setPosition(3);
        assertEquals(1, phrase1.getPosition());
        phrase1.setPosition(-3);
        assertEquals(1, phrase1.getPosition());

        phrase1.setLength(2);

        assertEquals(1, phrase1.getPosition());
        assertEquals(2, phrase1.getLength());

        assertEquals(32, phrase1.getTriggers().size());

        // set a trigger that will appear in the position 2 view
        phrase1.triggerOn(20, 42, 0f, 0f, 0);

        // position 1 view
        List<Trigger> steps = phrase1.getViewTriggers();
        assertEquals(16, steps.size());
        assertEquals(0, steps.get(0).getStep(phrase1.getResolution()));
        assertEquals(15, steps.get(15).getStep(phrase1.getResolution()));

        // position 2 view
        phrase1.setPosition(2);
        steps = phrase1.getViewTriggers();
        assertEquals(16, steps.size());
        assertEquals(16, steps.get(0).getStep(phrase1.getResolution()));
        assertEquals(31, steps.get(15).getStep(phrase1.getResolution()));

        // test that we have the trigger 20 at 4
        assertEquals(20, steps.get(4).getStep(phrase1.getResolution()));
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
        assertEquals(16, phrase.getStepCount());
        for (Trigger trigger : phrase.getTriggers()) {
            assertFalse(phrase.isSelected(trigger.getStep(phrase.getResolution())));
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
        for (Trigger trigger : phrase.getTriggers()) {
            assertEquals(72, trigger.getNotes().get(0).getPitch());
        }
        //-------------
        lastKeyPressed = keyPressed;
        keyPressed = 58;
        semitones = keyPressed - lastKeyPressed;
        assertEquals(-14, semitones);
        phrase.transpose(semitones);
        for (Trigger trigger : phrase.getTriggers()) {
            assertEquals(58, trigger.getNotes().get(0).getPitch());
        }
        // back up to root C
        lastKeyPressed = keyPressed;
        keyPressed = 60;
        semitones = keyPressed - lastKeyPressed;
        assertEquals(2, semitones);
        phrase.transpose(semitones);
        for (Trigger trigger : phrase.getTriggers()) {
            assertEquals(60, trigger.getNotes().get(0).getPitch());
        }
    }

    @Test
    public void test_triggerOnOff() {
        Phrase phrase = part1.getPhrase();
        phrase.triggerOn(4, 65, 0.5f, 1f, 2);
        Trigger trigger = phrase.getTrigger(4);
        Note note = trigger.getNotes().get(0);
        assertEquals(4, note.getStep(phrase.getResolution()));
        assertEquals(65, note.getPitch());
        assertEquals(0.5f, note.getGate(), 0f);
        assertEquals(1f, note.getVelocity(), 0f);
        assertEquals(2, note.getFlags());
        assertTrue(phrase.isSelected(4));
        assertTrue(trigger.isSelected());

        // turn it off, should affect non of the trigger's properties
        phrase.triggerOff(4);
        assertEquals(4, note.getStep(phrase.getResolution()));
        assertEquals(65, note.getPitch());
        assertEquals(0.5f, note.getGate(), 0f);
        assertEquals(1f, note.getVelocity(), 0f);
        assertEquals(2, note.getFlags());
        assertFalse(phrase.isSelected(4));
        assertFalse(trigger.isSelected());
    }

    @Test
    public void test_triggerGate() {
        //        Phrase phrase = part1.getPhrase();
        //        Trigger trigger = phrase.getTrigger(0);
        //        Note note = trigger.g
        //        assertEquals(0.25f, trigger.getGate(), 0f);
        //        assertEquals(0.3125f, PhraseUtils.incrementGate(phrase, trigger), 0f);
        //        assertEquals(0.3125f, trigger.getGate(), 0f);
        //        assertEquals("1.25", PhraseUtils.toStepDecimalString(trigger.getGate()));
        //
        //        assertEquals(0.375f, PhraseUtils.incrementGate(phrase, trigger), 0f);
        //        assertEquals(0.4375f, PhraseUtils.incrementGate(phrase, trigger), 0f);
        //        assertEquals(0.5f, PhraseUtils.incrementGate(phrase, trigger), 0f);
        //        assertEquals("2.0", PhraseUtils.toStepDecimalString(trigger.getGate()));
        //
        //        assertEquals(0.4375f, PhraseUtils.decrementGate(phrase, trigger), 0f);
        //        assertEquals(0.375f, PhraseUtils.decrementGate(phrase, trigger), 0f);
        //        assertEquals(0.3125f, PhraseUtils.decrementGate(phrase, trigger), 0f);
        //        assertEquals(0.25f, PhraseUtils.decrementGate(phrase, trigger), 0f);
        //        assertEquals("1.0", PhraseUtils.toStepDecimalString(trigger.getGate()));
        //
        //        // XXX unit test the end maximum of a step gate
        //        trigger = phrase.getStep(15);
        //        //assertEquals(0.375f, PhraseUtils.incrementGate(phrase, step), 0f);
    }

    @Test
    public void test_triggerUpdate() {
        //        Phrase phrase = part1.getPhrase();
        //        phrase.triggerOn(4, 65, 0.5f, 1f, 2);
        //        Trigger trigger = phrase.getStep(4);
        //        assertEquals(4, trigger.getStep());
        //        assertEquals(65, trigger.getPitch());
        //        assertEquals(0.5f, trigger.getGate(), 0f);
        //        assertEquals(1f, trigger.getVelocity(), 0f);
        //        assertEquals(2, trigger.getFlags());
        //        assertTrue(phrase.isSelected(4));
        //        assertTrue(trigger.isSelected());
        //
        //        phrase.triggerUpdate(4, 45, 0.5f, 1f, 2);
        //        trigger = phrase.getStep(4);
        //        assertEquals(4, trigger.getStep());
        //        assertEquals(45, trigger.getPitch());
        //        assertEquals(0.5f, trigger.getGate(), 0f);
        //        assertEquals(1f, trigger.getVelocity(), 0f);
        //        assertEquals(2, trigger.getFlags());
        //        assertTrue(phrase.isSelected(4));
        //        assertTrue(trigger.isSelected());
        //
        //        phrase.triggerUpdateGate(4, 4f);
        //        trigger = phrase.getStep(4);
        //        assertEquals(4, trigger.getStep());
        //        assertEquals(45, trigger.getPitch());
        //        assertEquals(4f, trigger.getGate(), 0f);
        //        assertEquals(1f, trigger.getVelocity(), 0f);
        //        assertEquals(2, trigger.getFlags());
        //        assertTrue(phrase.isSelected(4));
        //        assertTrue(trigger.isSelected());
    }
}

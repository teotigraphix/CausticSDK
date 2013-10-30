
package com.teotigraphix.caustk.gs.machine.part.sound;

import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.rack.tone.BasslineTone;

public class BasslinePatch extends Patch {

    public BasslinePatch(Part part, LibraryPatch patchItem) {
        super(part, patchItem);
    }

    public float getSynthProperty(SynthProperty property) {
        BasslineTone tone = getTone();
        float value = Float.NaN;
        switch (property) {
            case Accent:
                value = tone.getOsc1().getAccent();
                break;
            case Cutoff:
                value = tone.getFilter().getCutoff();
                break;
            case Decay:
                value = tone.getFilter().getDecay();
                break;
            case EnvMod:
                value = tone.getFilter().getEnvMod();
                break;
            case PulseWidth:
                value = tone.getOsc1().getPulseWidth();
                break;
            case Resonance:
                value = tone.getFilter().getResonance();
                break;
            case Tune:
                value = tone.getOsc1().getTune();
                break;
            case Volume:
                value = tone.getVolume().getOut();
                break;
        }
        return value;
    }

    public void setSynthProperty(SynthProperty property, float value) {
        BasslineTone tone = getTone();
        switch (property) {
            case Accent:
                tone.getOsc1().setAccent(value);
                break;
            case Cutoff:
                tone.getFilter().setCutoff(value);
                break;
            case Decay:
                tone.getFilter().setDecay(value);
                break;
            case EnvMod:
                tone.getFilter().setEnvMod(value);
                break;
            case PulseWidth:
                tone.getOsc1().setPulseWidth(value);
                break;
            case Resonance:
                tone.getFilter().setResonance(value);
                break;
            case Tune:
                tone.getOsc1().setTune((int)value);
                break;
            case Volume:
                tone.getVolume().setOut(value);
                break;
        }
    }

    public enum SynthProperty {
        PulseWidth,

        Tune,

        Cutoff,

        Resonance,

        EnvMod,

        Decay,

        Accent,

        Volume;
    }

    public enum LFOProperty {
        Target,

        Rate,

        Depth,

        Phase;
    }

    public enum DistorionProperty {
        Program,

        Pre,

        Amount,

        Post;
    }
}

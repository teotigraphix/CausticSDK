
package com.teotigraphix.gdx.app;

public interface IRackSequencerListener {

    void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond);

    void onSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond);

    void onThirtysecondChange(int measure, float beat, int sixteenth, int thirtysecond);
}

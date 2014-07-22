
package com.teotigraphix.gdx.app;

public abstract class CaustkScene extends Scene implements ICaustkScene {

    public CaustkScene() {
    }

    @Override
    public void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onBeatChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onSixteenthChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onThirtysecondChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : getBehaviors()) {
            ((CaustkBehavior)behavior).onThirtysecondChange(measure, beat, sixteenth, thirtysecond);
        }
    }
}

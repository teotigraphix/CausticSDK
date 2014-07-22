
package com.teotigraphix.gdx.app;

public abstract class CaustkBehavior extends Behavior {

    @Override
    public ICaustkApplication getApplication() {
        return (ICaustkApplication)super.getApplication();
    }

    public CaustkBehavior() {
    }

    public void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior child : getChildren()) {
            ((CaustkBehavior)child).onBeatChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    public void onSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior child : getChildren()) {
            ((CaustkBehavior)child).onSixteenthChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    public void onThirtysecondChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior child : getChildren()) {
            ((CaustkBehavior)child).onThirtysecondChange(measure, beat, sixteenth, thirtysecond);
        }
    }

}

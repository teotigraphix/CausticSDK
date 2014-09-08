////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

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

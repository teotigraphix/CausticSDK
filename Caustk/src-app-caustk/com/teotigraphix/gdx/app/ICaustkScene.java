////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

import com.teotigraphix.gdx.controller.IViewManager;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;
import com.teotigraphix.gdx.groove.ui.model.IUIModel;

/**
 * The {@link ICaustkScene} API allows an application to display states as UI
 * screens.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface ICaustkScene extends IScene {

    UIFactory getFactory();

    IUIModel getModel();

    IViewManager getViewManager();

    void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond);

    void onSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond);

    void onThirtysecondChange(int measure, float beat, int sixteenth, int thirtysecond);

}

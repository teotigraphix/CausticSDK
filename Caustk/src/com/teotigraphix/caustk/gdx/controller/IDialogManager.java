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

package com.teotigraphix.caustk.gdx.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.teotigraphix.caustk.gdx.app.ICaustkScene;
import com.teotigraphix.caustk.gdx.app.IScene;
import com.teotigraphix.caustk.gdx.scene2d.ui.ListDialog;
import com.teotigraphix.caustk.gdx.scene2d.ui.PopUp;

public interface IDialogManager {

    //    AlertDialog createAlert(IScreen screen, String title, Actor actor);
    //
    //    ListDialog createListDialog(String title, Object[] items, float width, float height);

    ListDialog createListDialog(IScene screen, String title, Object[] items, float width,
            float height);

    //
    //    ContextMenu createContextMenu(Object[] items);

    PopUp createPopUp(ICaustkScene scene, String title, Actor actor);

    PopUp createModalPopUp(ICaustkScene scene, String title, Actor actor);

    void createToast(ICaustkScene scene, String message, float duration);

    /**
     * @param scene
     * @param dialog
     * @param point
     */
    void show(ICaustkScene scene, Dialog dialog, Vector2 point);

    /**
     * Shows the dialog centered on the Stage.
     * 
     * @param dialog
     */
    void show(ICaustkScene scene, Dialog dialog);

}

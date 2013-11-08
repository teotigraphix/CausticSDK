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

package com.teotigraphix.libgdx.dialog;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog;
import com.badlogic.gdx.scenes.scene2d.ui.ContextMenu;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.badlogic.gdx.scenes.scene2d.ui.PopUp;
import com.teotigraphix.libgdx.screen.IScreen;

public interface IDialogManager {

    AlertDialog createAlert(IScreen screen, String title, Actor actor);

    PopUp createPopUp(IScreen screen, String title, Actor actor);

    void createToast(String message, float duration);

    ListDialog createListDialog(String title, Object[] items, float width, float height);

    ListDialog createListDialog(IScreen screen, String title, Object[] items, float width,
            float height);

    ContextMenu createContextMenu(Object[] items);

    /**
     * @param dialog
     * @param point Stage coordinates
     */
    void show(Dialog dialog, Vector2 point);

    /**
     * Shows the dialog centered on the Stage.
     * 
     * @param dialog
     */
    void show(Dialog dialog);

}

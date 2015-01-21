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

package com.teotigraphix.caustk.gdx.scene2d.ui.app;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.gdx.scene2d.ui.AlertDialog;
import com.teotigraphix.caustk.gdx.scene2d.ui.ScrollList;

public class FileExplorer extends AlertDialog {

    private ScrollList scrollList;

    private Array<String> items;

    private Label statusLabel;

    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    public void setItems(Array<String> list) {
        this.items = list;
        scrollList.setItems(list);
    }

    public int getSelectedIndex() {
        return scrollList.getSelectedIndex();
    }

    public FileExplorer(String title, Skin skin, String styleName, String buttonStyleName) {
        super(title, skin, styleName, buttonStyleName);
        setTitleAlignment(Align.center);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        statusLabel = new Label("", getSkin(), "FileExplorer.StatusLabel");
        getContentTable().add(statusLabel).expandX().fillX().padLeft(10f).padTop(5f);

        getContentTable().row();

        items = new Array<String>();
        scrollList = new ScrollList(getSkin(), items);
        scrollList.setMouseDownChange(false);
        setTitleAlignment(Align.left | Align.bottom);
        getContentTable().add(scrollList).expand().fill().pad(4f);

        getButtonTable().padBottom(8f);

    }
}

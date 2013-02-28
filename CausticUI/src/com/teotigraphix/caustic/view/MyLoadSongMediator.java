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

package com.teotigraphix.caustic.view;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teotigraphix.caustic.rack.IRackSong;
import com.teotigraphix.caustic.view.song.LoadSongMediator;

public class MyLoadSongMediator extends LoadSongMediator {

    //@InjectView(R.id.buttonLoadSong)
    Button button;

    //@InjectView(R.id.textViewStatus)
    TextView textViewStatus;

    @Override
    protected void onAttachMediator() {
        setView(button);
    }

    @Override
    protected void onRackSongLoad(IRackSong song) {
        Toast.makeText(getContext(), song.getPath(), Toast.LENGTH_LONG).show();
        textViewStatus.setText("Currently playing: " + song.getName());
    }
}

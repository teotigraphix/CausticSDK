////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.view.song;

import roboguice.activity.event.OnStartEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IRackController;
import com.teotigraphix.caustic.controller.IRackController.OnRackSongLoadListener;
import com.teotigraphix.caustic.rack.IRackSong;
import com.teotigraphix.caustic.view.Mediator;

@ContextSingleton
public abstract class LoadSongMediator extends Mediator {

    @Inject
    protected Context context;

    protected Context getContext() {
        return context;
    }

    @Inject
    IRackController controller;

    public Button getButton() {
        return (Button)getView();
    }

    protected void onStartListener(@Observes OnStartEvent event) {
        getButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSongChooser();
            }
        });
    }

    protected void onStopListener(@Observes OnStopEvent event) {
    }

    protected void showSongChooser() {
        controller.showSongChooser(context, new OnRackSongLoadListener() {
            @Override
            public void onRackSongLoaded(IRackSong song) {
                onRackSongLoad(song);
            }
        });
    }

    protected void onRackSongLoad(IRackSong song) {
    }
}

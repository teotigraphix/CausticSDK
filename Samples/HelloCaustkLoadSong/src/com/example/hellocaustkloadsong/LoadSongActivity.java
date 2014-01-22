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

package com.example.hellocaustkloadsong;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.teotigraphix.caustk.core.CaustkActivity;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * A simple example where a load button opens a Dialog to choose a .caustic file
 * to load in the rack.
 * <p>
 * A Play/Pause button can also play and stop the actual song, like a mini
 * caustic player.
 * 
 * @author Michael Schmalle
 */
public class LoadSongActivity extends CaustkActivity {

    static {
        // by setting this constant, the RuntimeUtils class can be used
        // to get File locations and easy file access to the /caustic/* directories
        RuntimeUtils.STORAGE_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    //----------------------------------
    // Constants
    //----------------------------------

    private static final int DIALOG_LOAD_CAUSTIC_FILE = 1000;

    private static final String FILE_TYPE = ".caustic";

    //----------------------------------
    // UI components
    //----------------------------------

    private Button loadButton;

    private ToggleButton playPauseButton;

    private TextView statusText;

    //----------------------------------
    // Variables
    //----------------------------------

    private ArrayList<String> fileList;

    private String[] songFiles;

    private File songDirectory = RuntimeUtils.getSongsDirectory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load up the files in alpha numeric order that exist in the
        // device's /storage/caustic/songs directory
        loadFileList();

        // create the user interface
        setContentView(R.layout.activity_load_song);

        // configure the load button
        loadButton = (Button)findViewById(R.id.loadButton);
        loadButton.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View view) {
                // showDialog() is deprecated but doing a DialogFragment for this
                // example is huge overkill, you also need to be using the 
                // android-support-v4.jar since this functionality cameinto existence at
                // API level 13; 3.2 Honeycomb
                showDialog(DIALOG_LOAD_CAUSTIC_FILE);
            }
        });

        // configure the playPause button
        playPauseButton = (ToggleButton)findViewById(R.id.playPauseButton);
        // don't allow clicks on the Play button until a song is loaded
        playPauseButton.setEnabled(false);
        playPauseButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if the button is now checked, play (1),  otherwise stop (0)
                OutputPanelMessage.PLAY.send(getGenerator(), isChecked ? 1 : 0);
            }
        });

        // get ref to the status textview
        statusText = (TextView)findViewById(R.id.statusText);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new Builder(this);

        switch (id) {
        // using the id we defined 1000, create a dialog using the String
        // items that are sorted and filtered by .caustic extension
            case DIALOG_LOAD_CAUSTIC_FILE:
                builder.setTitle("Choose a .caustic file");
                builder.setItems(songFiles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedIndex) {
                        // load the song using the selected list item from the dialog
                        loadSong(songFiles[selectedIndex]);
                    }
                });
                break;
        }
        // actually show the build dialog in the user interface
        dialog = builder.show();
        return dialog;
    }

    protected void loadSong(String selectedFileName) {
        // resolve the file name with the absolute path to the songs directory 
        File absoluteFile = new File(songDirectory, selectedFileName);
        // have the core sound engine load the .caustic file into memory
        RackMessage.LOAD_SONG.send(getGenerator(), absoluteFile.getAbsolutePath());
        // allow the play button to be pressed now that there is a song
        playPauseButton.setEnabled(true);
        // print out some info
        StringBuilder sb = new StringBuilder();
        // print out the machine anme:machine type in the status text
        for (int i = 0; i < 13; i++) {
            String name = RackMessage.QUERY_MACHINE_NAME.queryString(getGenerator(), i);
            String type = RackMessage.QUERY_MACHINE_TYPE.queryString(getGenerator(), i);
            // create a message like [0] MySubsynth:subsynth
            if (name != null) {
                sb.append("[" + i + "] ");
                sb.append(name);
                sb.append(":");
                sb.append(type);
                sb.append("<br/>");
            }
        }

        // using the Html.fromHtml() allows easy carriage returns using the <br/> element
        statusText.setText(Html.fromHtml(sb.toString()));
    }

    @Override
    protected int getActivationKey() {
        // Expires 03-01-2014
        return 0x2312F701;
    }

    private void loadFileList() {
        String[] files = null;
        if (songDirectory.exists()) {
            // create a file filter that will ignore directories and files
            // that don't end in .caustic
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String fileName) {
                    return fileName.contains(FILE_TYPE);
                }
            };
            // list out all the files in the directory using the filter
            files = songDirectory.list(filter);
        } else {
            files = new String[0];
        }

        // use the Collections sort, have to create a List first, then sort
        fileList = new ArrayList<String>(Arrays.asList(files));
        Collections.sort(fileList);
        // pass back the sorted file names to the array after sort
        songFiles = fileList.toArray(new String[] {});
    }
}

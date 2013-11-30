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

package com.teotigraphix.caustic.meta.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.teotigraphix.caustic.meta.FileExplorer;
import com.teotigraphix.caustic.meta.MainActivity;
import com.teotigraphix.caustic.meta.R;
import com.teotigraphix.caustic.meta.model.FileModel;
import com.teotigraphix.caustic.meta.model.FileModel.OnFileModelChangeListener;
import com.teotigraphix.caustk.core.CausticFile;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 */
public class MainActivityMediator {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private MainActivity activity;

    private FileModel fileModel;

    private List<EditText> formItems = new ArrayList<EditText>();

    //--------------------------------------------------------------------------
    // UI :: Variables
    //--------------------------------------------------------------------------

    private TextView causticFileText;

    private Button browseButton;

    private EditText artistText;

    private EditText titleText;

    private EditText descriptionText;

    private EditText linkText;

    private EditText linkURLText;

    private Button addButton;

    private Button removeButton;

    private ToggleButton playPauseButton;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MainActivityMediator(MainActivity activity, FileModel fileModel) {
        this.activity = activity;
        this.fileModel = fileModel;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void onAttach() {
        fileModel.setOnFileModelChangeListener(new OnFileModelChangeListener() {
            @Override
            public void onFileChange(CausticFile causticFile) {
                loadCausticSongFile();
                refreshView();
            }

            @Override
            public void onReset() {
                resetView();
            }
        });
    }

    public void onCreate() {
        causticFileText = (TextView)activity.findViewById(R.id.causticFileText);
        artistText = (EditText)activity.findViewById(R.id.artistText);
        titleText = (EditText)activity.findViewById(R.id.titleText);
        descriptionText = (EditText)activity.findViewById(R.id.descriptionText);
        descriptionText.setMaxLines(8);
        descriptionText.addTextChangedListener(new TextWatcher() {
            private String text;

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                text = arg0.toString();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                int lineCount = descriptionText.getLineCount();
                if (lineCount > 8) {
                    descriptionText.setText(text);
                }
            }
        });

        descriptionText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if enter is pressed start calculating
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    int editTextLineCount = ((EditText)v).getLineCount();
                    if (editTextLineCount >= 8)
                        return true;
                }

                return false;
            }
        });

        linkText = (EditText)activity.findViewById(R.id.linkText);
        linkURLText = (EditText)activity.findViewById(R.id.linkURLText);

        formItems.add(artistText);
        formItems.add(titleText);
        formItems.add(descriptionText);
        // formItems.add(linkText);
        // formItems.add(linkURLText);

        browseButton = (Button)activity.findViewById(R.id.browseButton);
        browseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, FileExplorer.class);
                i.putExtra(FileExplorer.EXTRA_ROOT, new File("/"));
                i.putExtra(FileExplorer.EXTRA_SONGS_DIR, RuntimeUtils.getCausticSongsDirectory());
                activity.startActivityForResult(i, 1);
            }
        });

        playPauseButton = (ToggleButton)activity.findViewById(R.id.playButton);
        playPauseButton.setEnabled(false);
        playPauseButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if the button is now checked, play (1),  otherwise stop (0)
                OutputPanelMessage.PLAY.send(activity.getGenerator(), isChecked ? 1 : 0);
            }
        });

        addButton = (Button)activity.findViewById(R.id.addButton);
        addButton.setEnabled(false);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    addCausticMetadata();
                }
            }
        });

        removeButton = (Button)activity.findViewById(R.id.removeButton);
        removeButton.setEnabled(false);
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.createAlertDialog(activity, "Confirm Remove Caustic metadata",
                        R.drawable.ic_launcher, new Runnable() {
                            @Override
                            public void run() {
                                doRemoveMetadata();
                            }
                        }).show();
            }
        });
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void doRemoveMetadata() {
        //                try {
        //                    saveSongAs(fileModel.getCausticFile().getFile());
        //                } catch (IOException e) {
        //                    e.printStackTrace();
        //                }

        Toast.makeText(activity,
                "Metadata removed from " + fileModel.getCausticFile().getFile().getName(),
                Toast.LENGTH_SHORT).show();

        fileModel.reset();
    }

    private boolean isValid() {
        boolean valid = true;
        String formItemName = null;
        View formItem = null;
        for (EditText editText : formItems) {
            if (!checkText(editText)) {
                formItem = editText;
                formItemName = (String)formItem.getTag();
                valid = false;
                break;
            }
        }

        if (formItem != null) {
            Toast.makeText(activity, "'" + formItemName + "' must be filled in", Toast.LENGTH_SHORT)
                    .show();
            formItem.requestFocus();
        }

        return valid;
    }

    private void addCausticMetadata() {
        if (!isValid())
            return;

        DialogUtils.createAlertDialog(activity, "Confirm Add Caustic metadata",
                R.drawable.ic_launcher, new Runnable() {
                    @Override
                    public void run() {
                        doSaveCausticFile();
                    }
                }).show();
    }

    private void doSaveCausticFile() {
        CausticFile causticFile = fileModel.getCausticFile();

        causticFile.setArtist(artistText.getText().toString());
        causticFile.setTitle(titleText.getText().toString());
        causticFile.setDescription(descriptionText.getText().toString());
        causticFile.setLinkText(linkText.getText().toString());
        causticFile.setLinkUrl(linkURLText.getText().toString());

        //if (causticFile.hasMetadata()) {
        // replacing data, need to resave file until I figure out
        // how to chop bytes

        //        String name = causticFile.getFile().getName().replace(".caustic", "");
        //        name = name + "_Meta";
        //        saveSong(name);

        //}

        try {
            causticFile.write();

            removeButton.setEnabled(true);

            Toast.makeText(activity, "Metadata added to " + causticFile.getFile().getName(),
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkText(EditText editText) {
        if (editText.getText().length() == 0) {
            return false;
        }
        return true;
    }

    private void loadCausticSongFile() {
        //        CausticFile causticFile = fileModel.getCausticFile();
        //        RackMessage.LOAD_SONG
        //                .send(activity.getGenerator(), causticFile.getFile().getAbsolutePath());
    }

    /**
     * Needs to refresh the view based on the presence of a CausticFile.
     */
    private void refreshView() {
        removeButton.setEnabled(false);
        addButton.setEnabled(false);
        playPauseButton.setEnabled(false);
        playPauseButton.setChecked(false);

        CausticFile causticFile = fileModel.getCausticFile();
        if (causticFile.isLoaded()) {
            addButton.setEnabled(true);
            playPauseButton.setEnabled(true);

            causticFileText.setText(causticFile.getFile().getAbsolutePath());
            artistText.setText(causticFile.getArtist());
            titleText.setText(causticFile.getTitle());
            descriptionText.setText(causticFile.getDescription());
            linkText.setText(causticFile.getLinkText());
            linkURLText.setText(causticFile.getLinkUrl());
        } else {
            causticFileText.setText("");
            artistText.setText("");
            titleText.setText("");
            descriptionText.setText("");
            linkText.setText("");
            linkURLText.setText("");
        }

        if (causticFile.hasMetadata()) {
            removeButton.setEnabled(true);
        }
    }

    private void resetView() {
        removeButton.setEnabled(false);
        addButton.setEnabled(false);
        //playPauseButton.setEnabled(false);
        //playPauseButton.setChecked(false);

        causticFileText.setText("");
        artistText.setText("");
        titleText.setText("");
        descriptionText.setText("");
        linkText.setText("");
        linkURLText.setText("");
    }

}

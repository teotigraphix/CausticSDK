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

package com.teotigraphix.caustic.meta;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teotigraphix.caustic.meta.model.BrowserModel;
import com.teotigraphix.caustic.meta.model.BrowserModel.OnBrowserModelListener;
import com.teotigraphix.caustic.meta.view.DialogUtils;

/**
 * @author Michael Schmalle
 */
public class FileExplorer extends ListActivity {

    private static final String CAUSTIC = ".caustic";

    public static final String EXTRA_FILE = "file";

    public static final String EXTRA_ROOT = "root";

    public static final String EXTRA_SONGS_DIR = "songsDir";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private TextView locationTextView;

    private BrowserModel browserModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        browserModel = new BrowserModel(this);
        browserModel.setOnBrowserModelListener(new OnBrowserModelListener() {
            @Override
            public void onLocationChange(File location) {
                updateListAdapter(location, browserModel.getItems());
            }
        });

        setContentView(R.layout.main_file_explorer);
        locationTextView = (TextView)findViewById(R.id.locationTextView);

        Intent intent = getIntent();
        File rootDirectory = (File)intent.getSerializableExtra(EXTRA_ROOT);
        File songDir = (File)intent.getSerializableExtra(EXTRA_SONGS_DIR);

        browserModel.setRootDirectory(rootDirectory);
        browserModel.setLocation(songDir);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final File location = new File(browserModel.getPaths().get(position));
        if (location.isDirectory()) {
            if (location.canRead())
                browserModel.setLocation(location);
            else {
                new AlertDialog.Builder(this)
                        .setTitle("[" + location.getName() + "] folder can't be read")
                        .setIcon(R.drawable.ic_launcher)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                DialogUtils.createAlertDialog(this,
                        "[" + location.getName() + "] folder can't be read",
                        R.drawable.ic_launcher, null).show();

            }
        } else {

            DialogUtils.createAlertDialog(this, "Load [" + location.getName() + "]",
                    R.drawable.ic_launcher, new Runnable() {
                        @Override
                        public void run() {
                            if (location.getName().endsWith(CAUSTIC)) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra(EXTRA_FILE, location);
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            }
                        }
                    }).show();
        }
    }

    private void updateListAdapter(File location, List<String> items) {
        locationTextView.setText("Location: " + location.getAbsolutePath());

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, items);
        setListAdapter(fileList);
    }
}

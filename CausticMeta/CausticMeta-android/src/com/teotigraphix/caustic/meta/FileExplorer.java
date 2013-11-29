
package com.teotigraphix.caustic.meta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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

public class FileExplorer extends ListActivity {

    private static final String CAUSTIC = ".caustic";

    public static final String EXTRA_FILE = "file";

    public static final String EXTRA_ROOT = "root";

    public static final String EXTRA_SONGS_DIR = "songsDir";

    private List<String> items = null;

    private List<String> paths = null;

    private String root = "/";

    private TextView locationTextView;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_file_explorer);
        locationTextView = (TextView)findViewById(R.id.locationTextView);

        Intent intent = getIntent();
        File file = (File)intent.getSerializableExtra(EXTRA_ROOT);
        File songDir = (File)intent.getSerializableExtra(EXTRA_SONGS_DIR);
        root = file.getAbsolutePath();

        updateListAdapter(songDir.getAbsolutePath());
    }

    private void updateListAdapter(String absoluteDirectoryPath) {
        locationTextView.setText("Location: " + absoluteDirectoryPath);

        items = new ArrayList<String>();
        paths = new ArrayList<String>();

        File directory = new File(absoluteDirectoryPath);
        File[] allFiles = directory.listFiles();

        if (!absoluteDirectoryPath.equals(root)) {
            items.add(root);
            paths.add(root);
            items.add("../");
            paths.add(directory.getParent());
        }

        List<File> directories = new ArrayList<File>();
        List<File> files = new ArrayList<File>();

        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isDirectory()) {
                directories.add(file);
            } else {
                if (file.getName().endsWith(CAUSTIC))
                    files.add(file);
            }
        }

        Collections.sort(directories);
        Collections.sort(files);

        for (File file : directories) {
            paths.add(file.getPath());
            items.add(file.getName() + "/");
        }

        for (File file : files) {
            paths.add(file.getPath());
            items.add(file.getName());
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, items);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final File file = new File(paths.get(position));
        if (file.isDirectory()) {
            if (file.canRead())
                updateListAdapter(paths.get(position));
            else {
                new AlertDialog.Builder(this)
                        .setTitle("[" + file.getName() + "] folder can't be read")
                        .setIcon(R.drawable.ic_launcher)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        } else {
            new AlertDialog.Builder(this).setTitle("Load [" + file.getName() + "]")
                    .setIcon(R.drawable.ic_launcher)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (file.getName().endsWith(CAUSTIC)) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra(EXTRA_FILE, file);
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}

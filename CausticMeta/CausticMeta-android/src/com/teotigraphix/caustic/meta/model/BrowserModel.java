
package com.teotigraphix.caustic.meta.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;

/**
 * @author Michael Schmalle
 */
public class BrowserModel {

    @SuppressWarnings("unused")
    private Activity activity;

    private List<String> items = null;

    private List<String> paths = null;

    public List<String> getItems() {
        return items;
    }

    public List<String> getPaths() {
        return paths;
    }

    private File rootDirectory;

    public File getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    private File location;

    private OnBrowserModelListener listener;

    public File getLocation() {
        return location;
    }

    public void setLocation(File location) {
        this.location = location;
        updateModel();
        listener.onLocationChange(location);
    }

    private void updateModel() {
        items = new ArrayList<String>();
        paths = new ArrayList<String>();

        File directory = new File(location.getAbsolutePath());
        File[] allFiles = directory.listFiles();

        final String rootPath = rootDirectory.getAbsolutePath();
        if (!location.equals(rootDirectory)) {
            items.add(rootPath);
            paths.add(rootPath);
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
                if (file.getName().endsWith(".caustic"))
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
    }

    public BrowserModel(Activity activity) {
        this.activity = activity;
    }

    public void setOnBrowserModelListener(OnBrowserModelListener l) {
        listener = l;
    }

    public static interface OnBrowserModelListener {
        void onLocationChange(File location);
    }
}

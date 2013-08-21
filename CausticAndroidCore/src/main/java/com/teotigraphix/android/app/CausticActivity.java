
package com.teotigraphix.android.app;

import java.io.File;
import java.io.IOException;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import android.app.Application;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.util.Modules;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.android.ui.MainLayout;
import com.teotigraphix.caustic.application.IApplicationPreferences;
import com.teotigraphix.caustic.application.IPreferenceManager;
import com.teotigraphix.caustic.application.IPreferenceManager.Editor;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.service.IInjectorService;

public abstract class CausticActivity extends RoboActivity {

    // XXX This has to be put in the Andorid Application impl
    // and use controller.addComponent(IInjectorService.class);
    @Inject
    protected IInjectorService injectorService;

    @Inject
    protected IApplicationController applicationController;

    @Inject
    protected ICaustkApplicationProvider applicationProvider;

    @Inject
    protected IPreferenceManager preferenceManager;

    @Inject
    protected IApplicationPreferences applicationPreferences;

    @Inject
    ITouchService touchService;

    private MainLayout layout;

    protected boolean mDebugLog = true;

    private ICaustkController controller;

    private Editor editor;

    IActivityCycle getActivityCycle() {
        return (IActivityCycle)controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        createModule(savedInstanceState);

        super.onCreate(savedInstanceState);

        commitLayout();

        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void commitLayout();

    /**
     * Call in application activity to set the {@link MainLayout}.
     * 
     * @param layoutId The res id of the MainLayout.
     */
    protected void setLayout(int layoutId) {
        layout = (MainLayout)findViewById(layoutId);
        layout.setTouchService(touchService);
        // getSystem().setLayout(mLayout);
    }

    /**
     * Subclass to supply the Applications's config module.
     */
    abstract protected AbstractModule createApplicationModule();

    private void createModule(Bundle state) {
        AbstractModule module = createApplicationModule();
        if (module != null) {
            loadApplicationModule(getApplication(), module);
        }
    }

    private void loadApplicationModule(Application application, AbstractModule module) {
        RoboGuice.setBaseApplicationInjector(application, RoboGuice.DEFAULT_STAGE, Modules
                .override(RoboGuice.newDefaultRoboModule(application)).with(module));
    }

    @Override
    protected void onStart() {
        controller.onStart(); // 1

        super.onStart();
        if (mDebugLog)
            Log.i("CYCLE", "onStart()");
    }

    private void start() throws IOException {
        // XXX special case, maybe a better place to put this
        applicationProvider.get().getController()
                .addComponent(IInjectorService.class, injectorService);

        controller = applicationProvider.get().getController();

        CtkDebug.log("Create main app UI");

        addListeners();

        setupWorkingDirectory();

        // registers screenManager which then will loop through all screens
        applicationController.registerMediatorObservers();

        CtkDebug.log("Start application controller");
        // set roots, call initialize(), start() on application, start app model
        // create or load last project
        applicationController.start();

        applicationController.load();

        applicationController.registerModels();
        applicationController.registerMeditors();

        applicationController.show();

        CtkDebug.log("Show the application");

        show();

    }

    private void setupWorkingDirectory() {
        String causticDirectory = preferenceManager.getString("causticRoot", null);
        if (causticDirectory == null) {
            File causticFile = new File(Environment.getExternalStorageDirectory(), "caustic");
            if (causticFile != null && causticFile.isDirectory()) {
                causticDirectory = causticFile.getPath();
                preferenceManager.edit().putString("causticRoot", causticDirectory).commit();
            } else {
                throw new RuntimeException("Caustic directory invalid.");
            }
        }

        // XXX resourceBundle.getString("APP_DIRECTORY")
        File workingDirectory = new File(causticDirectory).getParentFile();
        applicationProvider.get().getConfiguration().setCausticStorage(workingDirectory);
        File applicationRoot = new File(workingDirectory, "Tones");
        applicationProvider.get().getConfiguration().setApplicationRoot(applicationRoot);
    }

    void show() {

    }

    protected void addListeners() {
        editor = applicationPreferences.edit();

        //        stageModel.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
        //            public void handle(WindowEvent we) {
        //                try {
        //                    editor.commit();
        //                    applicationProvider.get().save();
        //                } catch (IOException e) {
        //                    e.printStackTrace();
        //                }
        //                applicationProvider.get().close();
        //            }
        //        });
    }

    @Override
    protected void onResume() {
        controller.onResume(); // 2
        super.onResume();
        if (mDebugLog)
            Log.i("CYCLE", "onResume()");
    }

    @Override
    protected void onPause() {
        controller.onPause(); // 3
        super.onPause();
        if (mDebugLog)
            Log.i("CYCLE", "onPause()");
    }

    @Override
    protected void onStop() {
        controller.onStop(); // 4
        super.onStop();
        if (mDebugLog)
            Log.i("CYCLE", "onStop()");
    }

    @Override
    protected void onDestroy() {
        controller.onDestroy();
        save();
        super.onDestroy();
        if (mDebugLog)
            Log.i("CYCLE", "onDestroy()");
    }

    private void save() {
        try {
            applicationProvider.get().save();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

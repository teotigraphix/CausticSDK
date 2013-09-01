
package com.teotigraphix.libgdx.application;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.service.IInjectorService;
import com.teotigraphix.libgdx.controller.IApplicationController;

public class StartupExecutor {

    final Set<Module> modules = new HashSet<Module>();

    @Inject
    ICaustkApplicationProvider application;

    @Inject
    IApplicationController applicationController;

    @Inject
    IApplicationMediator applicationMediator;

    private ICaustkController controller;

    private Injector injector;

    public Injector getInjector() {
        return injector;
    }

    @Inject
    private IInjectorService injectorService;

    public ICaustkController getController() {
        return controller;
    }

    public StartupExecutor() {
    }

    public void start(IGame game) throws IOException {
        File root = new File(Gdx.files.getExternalStoragePath());
        File causticDirectory = new File(root.getAbsolutePath());
        File applicationDirectory = new File(root, game.getAppName());

        @SuppressWarnings("unchecked")
        final Class<StartupExecutor> clazz = (Class<StartupExecutor>)getClass();
        final StartupExecutor instance = this;

        final Set<Module> additionalModules = new HashSet<Module>();
        modules.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toInstance(instance);
            }
        });

        // Propagates initialization of additional modules to the specific
        // subclass of this Application instance.
        modules.addAll(additionalModules);

        // Creates an injector with all of the required modules.
        injector = Guice.createInjector(modules);

        // Injects all fields annotated with @Inject into this GuiceApplication instance.
        injector.injectMembers(instance);
        injector.injectMembers(game); // just need the injector

        //CaustkApplication application = new CaustkApplication(configuration);
        application.get().getConfiguration().setSoundGenerator(game.getSoundGenerator());
        application.get().getConfiguration().setCausticStorage(causticDirectory);
        application.get().getConfiguration().setApplicationRoot(applicationDirectory);
        //application.get().initialize();
        //application.get().start();

        controller = application.get().getController();
        controller.addComponent(IInjectorService.class, injectorService);

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

    private void show() {
    }

    public final void addModule(Module module) {
        modules.add(module);
    }

}

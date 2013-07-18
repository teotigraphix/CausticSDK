
package com.teotigraphix.caustk.application;

import org.androidtransfuse.event.EventObserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.ICaustkApplication.OnApplicationInitialize;
import com.teotigraphix.caustk.application.ICaustkApplication.OnApplicationStart;
import com.teotigraphix.caustk.core.CausticException;

public class CaustkApplicationTest {
    private ICaustkApplication application;

    @Before
    public void setUp() throws Exception {
        application = new CaustkApplication(MockApplicationConfiguration.create());
    }

    @After
    public void tearDown() throws Exception {
        application.close();
        application = null;
    }

    @Test
    public void test_setup() throws CausticException {
        final AppInfo info = new AppInfo();
        // the dispatcher is ready when the constructor returns for the application
        application
                .getController()
                .getDispatcher()
                .register(OnApplicationInitialize.class,
                        new EventObserver<OnApplicationInitialize>() {
                            @Override
                            public void trigger(OnApplicationInitialize object) {
                                System.out.println("initialize()");
                                // API components and all sub components of the controller
                                // have been created
                                info.isInitialized = true;
                            }
                        });
        application.getController().getDispatcher()
                .register(OnApplicationStart.class, new EventObserver<OnApplicationStart>() {
                    @Override
                    public void trigger(OnApplicationStart object) {
                        System.out.println("start()");
                        // nothing really happens here on a framework level
                        // models and mediators start processing their state
                        info.isStarted = true;
                    }
                });
        
        // all sub components in Controller are now created
        application.initialize();
        application.start();

        Assert.assertTrue(info.isInitialized);
        Assert.assertTrue(info.isStarted);

        Assert.assertNotNull(application.getConfiguration());
        Assert.assertNotNull(application.getController());

        //SubSynthTone tone = (SubSynthTone)controller.getSoundSource().createTone("tone1",
        //        ToneType.SubSynth);
        //tone.getSynth().noteOn(60, 1f);
    }

    class AppInfo {
        public boolean isInitialized = false;

        public boolean isStarted = false;
    }
}

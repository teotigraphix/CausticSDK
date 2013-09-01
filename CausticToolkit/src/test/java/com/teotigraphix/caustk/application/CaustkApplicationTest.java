
package com.teotigraphix.caustk.application;

import java.io.IOException;

import org.androidtransfuse.event.EventObserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkApplication.OnCausticApplicationStateChange;
import com.teotigraphix.caustk.controller.core.CaustkApplication;
import com.teotigraphix.caustk.core.CausticException;

public class CaustkApplicationTest {
    private ICaustkApplication application;

    private AppInfo info;

    @Before
    public void setUp() throws Exception {
        application = new CaustkApplication(MockApplicationConfiguration.create());
    }

    @After
    public void tearDown() throws Exception {
        application = null;
    }

    @Test
    public void test_setup() throws CausticException, IOException {
        info = new AppInfo();
        // the dispatcher is ready when the constructor returns for the application
        IDispatcher dispatcher = application.getController().getDispatcher();
        dispatcher.register(OnCausticApplicationStateChange.class,
                new EventObserver<OnCausticApplicationStateChange>() {
                    @Override
                    public void trigger(OnCausticApplicationStateChange object) {
                        switch (object.getKind()) {
                            case Initialize:
                                // API components and all sub components of the controller
                                // have been created
                                info.isInitialized = true;
                                break;

                            case Start:
                                // nothing really happens here on a framework level
                                // models and mediators start processing their state
                                info.isStarted = true;
                                break;

                            case Close:
                                info.isClosed = true;
                                break;

                            case Save:
                                info.isSaved = true;
                                break;

                            default:
                                break;

                        }
                    }
                });

        // all sub components in Controller are now created
        application.initialize();
        application.start();

        Assert.assertTrue(info.isInitialized);
        Assert.assertTrue(info.isStarted);

        //        application.save();
        //        Assert.assertTrue(info.isSaved);

        Assert.assertNotNull(application.getConfiguration());
        Assert.assertNotNull(application.getController());

        application.close();
        Assert.assertTrue(info.isClosed);
    }

    class AppInfo {
        public boolean isSaved = false;

        public boolean isClosed = false;

        public boolean isInitialized = false;

        public boolean isStarted = false;
    }
}

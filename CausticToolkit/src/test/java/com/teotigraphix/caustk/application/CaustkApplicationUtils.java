
package com.teotigraphix.caustk.application;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.core.CaustkApplication;
import com.teotigraphix.caustk.controller.core.IApplicationHandler;

public class CaustkApplicationUtils {

    public static ICaustkApplication createAndRun() {
        CaustkApplication application = new CaustkApplication(MockApplicationConfiguration.create());
        application.setApplicationHandler(new IApplicationHandler() {

            @Override
            public void commitSave() {
            }

            @Override
            public void commitCreate() {
            }

            @Override
            public void commitClose() {
            }
        });
        application.create();
        //        application.start();
        return application;
    }

    public static ICaustkApplication createAndRun(ICaustkConfiguration configuration) {
        CaustkApplication application = new CaustkApplication(configuration);
        application.setApplicationHandler(new IApplicationHandler() {

            @Override
            public void commitSave() {
            }

            @Override
            public void commitCreate() {
            }

            @Override
            public void commitClose() {
            }
        });
        application.create();
        //        application.start();
        return application;
    }

}

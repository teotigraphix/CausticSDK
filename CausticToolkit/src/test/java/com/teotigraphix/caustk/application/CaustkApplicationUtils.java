
package com.teotigraphix.caustk.application;

public class CaustkApplicationUtils {

    public static ICaustkApplication createAndRun() {
        CaustkApplication application = new CaustkApplication(MockApplicationConfiguration.create());
        application.initialize();
        application.start();
        return application;
    }

    public static ICaustkApplication createAndRun(ICaustkConfiguration configuration) {
        CaustkApplication application = new CaustkApplication(configuration);
        application.initialize();
        application.start();
        return application;
    }

}

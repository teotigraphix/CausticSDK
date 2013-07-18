
package com.teotigraphix.caustk.application;

import java.io.File;

public class MockApplicationConfiguration extends CaustkConfigurationBase {

    public static ICaustkConfiguration create() {
        return new MockApplicationConfiguration();
    }

    @Override
    public String getApplicationId() {
        return "default";
    }

    public MockApplicationConfiguration() {
        super();
        setCausticStorage(new File(System.getProperty("user.home")));
        setApplicationRoot(new File("src/test/resources/unit_test"));
    }

}

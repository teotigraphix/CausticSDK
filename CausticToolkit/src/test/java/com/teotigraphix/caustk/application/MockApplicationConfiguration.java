
package com.teotigraphix.caustk.application;

import java.io.File;

import com.teotigraphix.caustk.core.internal.Constants;

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
        setCausticStorage(new File(Constants.STORAGE_ROOT));
        setApplicationRoot(new File("src/test/resources/unit_test"));
    }

}


package com.teotigraphix.caustk.application;

import java.io.File;

import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.core.CaustkConfigurationBase;
import com.teotigraphix.caustk.core.internal.Constants;
import com.teotigraphix.caustk.core.internal.DesktopSoundGenerator;

public class MockApplicationConfiguration extends CaustkConfigurationBase {

    public static ICaustkConfiguration create() {
        return new MockApplicationConfiguration();
    }

    public MockApplicationConfiguration() {
        super();
    }

    @Override
    protected void initialize() {
        setCausticStorage(new File(Constants.STORAGE_ROOT));
        setApplicationRoot(new File("src/test/resources/unit_test"));
        setSoundGenerator(new DesktopSoundGenerator());
        setApplicationId("default");
    }
}

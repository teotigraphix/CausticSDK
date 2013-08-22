
package com.teotigraphix.caustk.application;

import java.io.File;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.internal.Constants;
import com.teotigraphix.caustk.sound.DesktopSoundGenerator;
import com.teotigraphix.caustk.sound.ISoundGenerator;

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

    @Override
    public ISoundGenerator createSoundGenerator(ICaustkController controller) {
        return new DesktopSoundGenerator();
    }
}

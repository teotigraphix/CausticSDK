
package com.teotigraphix.libgdx.application;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.core.CaustkApplication;

public class ApplicationProvider implements ICaustkApplicationProvider {

    private ICaustkApplication application;

    @Inject
    ICaustkConfiguration configuration;

    @Override
    public ICaustkApplication get() {
        if (application == null)
            application = new CaustkApplication(configuration);
        return application;
    }
}

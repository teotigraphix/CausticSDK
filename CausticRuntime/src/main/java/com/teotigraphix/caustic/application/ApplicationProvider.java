
package com.teotigraphix.caustic.application;

import com.google.inject.Inject;
import com.teotigraphix.caustk.application.CaustkApplication;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkConfiguration;

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

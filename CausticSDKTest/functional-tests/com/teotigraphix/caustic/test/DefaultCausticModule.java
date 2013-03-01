
package com.teotigraphix.caustic.test;

import com.google.inject.Binder;
import com.teotigraphix.caustic.activity.ICausticConfiguration;
import com.teotigraphix.caustic.internal.actvity.CausticModule;

public class DefaultCausticModule extends CausticModule {
    @Override
    public void configure(Binder binder) {
        binder.bind(ICausticConfiguration.class).to(DefaultConfiguration.class);
        super.configure(binder);
    }
}

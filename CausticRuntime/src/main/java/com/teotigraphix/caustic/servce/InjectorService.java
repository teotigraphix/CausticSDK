
package com.teotigraphix.caustic.servce;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.service.IInjectorService;

@Singleton
public class InjectorService implements IInjectorService {

    @Inject
    Injector injector;

    public InjectorService() {
    }

    @Override
    public void inject(Object instance) {
        injector.injectMembers(instance);
    }

}

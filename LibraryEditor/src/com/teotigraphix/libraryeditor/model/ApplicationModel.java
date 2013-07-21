
package com.teotigraphix.libraryeditor.model;

import com.google.inject.Inject;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.ModelBase;

public class ApplicationModel extends ModelBase {

    @Inject
    public ApplicationModel(ICaustkApplicationProvider provider) {
        super(provider);
    }

}


package com.teotigraphix.libraryeditor.config;

import java.io.File;

import com.teotigraphix.caustk.application.CaustkConfigurationBase;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class ApplicationConfiguration extends CaustkConfigurationBase {

    @Override
    public String getApplicationId() {
        return "libraryeditor";
    }

    @Override
    public void setCausticStorage(File value) {
        super.setCausticStorage(value);
        RuntimeUtils.STORAGE_ROOT = value.getAbsolutePath();
    }

    public ApplicationConfiguration() {
        super();
    }

}


package com.teotigraphix.caustic.application;

import java.util.ResourceBundle;

import javax.inject.Named;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppPreferenceManager extends PreferenceManager implements IApplicationPreferences {

    @Inject
    public AppPreferenceManager(@Named("resources") ResourceBundle resourceBundle) {
        setupUserNode(getClass().getName() + "/" + resourceBundle.getString("APP_DIRECTORY"));
    }
}

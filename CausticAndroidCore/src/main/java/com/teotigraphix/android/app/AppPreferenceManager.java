
package com.teotigraphix.android.app;

import javax.inject.Inject;

import com.teotigraphix.caustic.application.IApplicationPreferences;

import android.content.Context;

public class AppPreferenceManager extends PreferenceManager implements IApplicationPreferences {

    @Inject
    public AppPreferenceManager(Context context) {
        super(context);
    }

    @Override
    void init() {
        preferences = context.getSharedPreferences("Application.Private", Context.MODE_PRIVATE);
    }

}

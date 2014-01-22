
package com.teotigraphix.ambientmallets;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.teotigraphix.caustk.core.generator.AndroidSoundGenerator;

public class MainActivity extends AndroidApplication {

    private AmbientMallets listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        listener = new AmbientMallets(new AndroidSoundGenerator(this, 0xC9AC49A2));
        initialize(listener, cfg);
    }
}

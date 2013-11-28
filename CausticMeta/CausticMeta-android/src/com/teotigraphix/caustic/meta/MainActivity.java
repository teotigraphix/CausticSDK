
package com.teotigraphix.caustic.meta;

import android.os.Bundle;

import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.core.CaustkApplicationActivity;

/**
 * 
 * @author Michael Schmalle
 *
 */
public class MainActivity extends CaustkApplicationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
    }

    @Override
    protected ICaustkConfiguration createConfiguration() {
        return null; // just use the default that is created
    }

    @Override
    protected String getApplicationName() {
        return "CausticMeta";
    }

    @Override
    protected int getActivationKey() {
        return 0xA8DF832A;
    }

}

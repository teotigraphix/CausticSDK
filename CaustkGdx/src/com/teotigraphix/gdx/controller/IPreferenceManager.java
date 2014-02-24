
package com.teotigraphix.gdx.controller;

import com.badlogic.gdx.Preferences;
import com.teotigraphix.gdx.app.IApplicationComponent;

public interface IPreferenceManager extends IApplicationComponent {

    Preferences get(String name);

    void save();

}

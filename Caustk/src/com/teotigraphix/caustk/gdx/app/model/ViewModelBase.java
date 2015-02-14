
package com.teotigraphix.caustk.gdx.app.model;

import com.teotigraphix.caustk.gdx.app.ApplicationComponent;
import com.teotigraphix.caustk.gdx.app.model.song.ISongFileCollection;

public abstract class ViewModelBase extends ApplicationComponent {

    public ViewModelBase() {
    }

    public abstract void reset();

    public abstract ISongFileCollection getCollection();

}

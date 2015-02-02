
package com.teotigraphix.caustk.gdx.app.api;

import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.ICaustkRuntime;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.gdx.app.AbstractProjectModelAPI;
import com.teotigraphix.caustk.gdx.app.ProjectModel;
import com.teotigraphix.caustk.gdx.app.ProjectState;
import com.teotigraphix.caustk.node.RackInstance;

public class RackAPI extends AbstractProjectModelAPI {

    private ICaustkRuntime getRuntime() {
        return CaustkRuntime.getInstance();
    }

    public ICaustkRack getRack() {
        return getRuntime().getRack();
    }

    @Override
    public RackInstance getRackInstance() {
        return getRuntime().getRack().getRackInstance();
    }

    public EventBus getRackEventBus() {
        return getRuntime().getRack().getEventBus();
    }

    public RackAPI(ProjectModel projectModel) {
        super(projectModel);
    }

    @Override
    public void restore(ProjectState state) {
    }

}

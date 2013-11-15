
package com.teotigraphix.caustk.gs.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.workstation.MasterSystem;
import com.teotigraphix.caustk.workstation.system.MasterSystemState;
import com.teotigraphix.caustk.workstation.system.SystemState;
import com.teotigraphix.caustk.workstation.system.SystemStateItem;
import com.teotigraphix.libgdx.model.CaustkModelBase;

/**
 * @author Michael Schmalle
 */
@Singleton
public class SystemStateModel extends CaustkModelBase implements ISystemStateModel {

    private transient MasterSystemState systemState;

    private SystemStateItem currentStateItem;

    public SystemStateItem getCurrentStateItem() {
        return currentStateItem;
    }

    @Override
    public final MasterSystemState getSystemState() {
        return systemState;
    }

    @Override
    public final void setSystemState(MasterSystemState value) {
        systemState = value;
    }

    @Override
    public void setSelectedStateItem(int stateIndex, int stateItemIndex) {
        final MasterSystem masterSystem = getController().getRackSet().getSystem();
        masterSystem.setSelectedStateIndex(stateIndex, stateItemIndex);
        currentStateItem = systemState.getItem(stateIndex, stateItemIndex);
        if (!currentStateItem.hasCommand())
            return;

        // send an update message so displays can be update with Command value
        try {
            getController().execute(currentStateItem.getMessage(), "update");
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSelectedStateIndex() {
        return getController().getRackSet().getSystem().getSelectedStateIndex();
    }

    @Override
    public int getSelectedStateItemIndex() {
        return getController().getRackSet().getSystem().getSelectedStateItemIndex();
    }

    @Override
    public SystemState[] getStates() {
        return systemState.getStates();
    }

    /**
     * Global abstract value increment, depends solely on current state.
     * 
     * @see OnSystemStateModelValueChange
     * @see SystemStateModelValueChangeKind#Increment
     */
    @Override
    public void incrementValue() {
        if (!currentStateItem.hasCommand())
            return;
        try {
            getController().execute(currentStateItem.getMessage(), "inc");
        } catch (CausticException e) {
            e.printStackTrace();
        }
        trigger(new OnSystemStateModelValueChange(this, SystemStateModelValueChangeKind.Increment));
    }

    /**
     * Global abstract value decrement, depends solely on current state.
     * 
     * @see OnSystemStateModelValueChange
     * @see SystemStateModelValueChangeKind#Decrement
     */
    @Override
    public void decrementValue() {
        if (!currentStateItem.hasCommand())
            return;
        try {
            getController().execute(currentStateItem.getMessage(), "dec");
        } catch (CausticException e) {
            e.printStackTrace();
        }
        trigger(new OnSystemStateModelValueChange(this, SystemStateModelValueChangeKind.Decrement));
    }

    public SystemStateModel() {
    }

    public enum SystemStateModelValueChangeKind {

        Increment,

        Decrement,

        Update;
    }

    public static class OnSystemStateModelValueChange {

        private ISystemStateModel systemStateModel;

        private SystemStateModelValueChangeKind kind;

        private Object value;

        public ISystemStateModel getModel() {
            return systemStateModel;
        }

        public SystemStateModelValueChangeKind getKind() {
            return kind;
        }

        public Object getValue() {
            return value;
        }

        public OnSystemStateModelValueChange(ISystemStateModel systemStateModel,
                SystemStateModelValueChangeKind kind) {
            this.systemStateModel = systemStateModel;
            this.kind = kind;
        }

        public OnSystemStateModelValueChange(SystemStateModelValueChangeKind kind, Object value) {
            this.kind = kind;
            this.value = value;
        }
    }
}

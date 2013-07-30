
package com.teotigraphix.caustk.system;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;

public class SystemState extends SubControllerBase implements ISystemState {

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SystemStateModel.class;
    }

    //----------------------------------
    // shiftEnabled
    //----------------------------------

    private boolean shiftEnabled;

    public boolean isShiftEnabled() {
        return shiftEnabled;
    }

    public void setShiftEnabled(boolean value) {
        if (shiftEnabled == value)
            return;
        shiftEnabled = value;
        getController().getDispatcher().trigger(new OnSystemStateShiftModeChange(shiftEnabled));
    }

    //----------------------------------
    // recording
    //----------------------------------

    private boolean recording;

    /**
     * Returns whether the controller is in record mode.
     */
    public boolean isRecording() {
        return recording;
    }

    /**
     * Sets the controllers record mode.
     * 
     * @param value The new record mode.
     * @see OnSystemStateRecordModeChange
     */
    public void setRecording(boolean value) {
        if (recording == value)
            return;
        recording = value;
        getController().getDispatcher().trigger(new OnSystemStateRecordModeChange(recording));
    }

    //----------------------------------
    // systemMode
    //----------------------------------

    private SystemMode systemMode;

    /**
     * Returns the current {@link SystemMode} of the controller.
     */
    public SystemMode getSystemMode() {
        return systemMode;
    }

    /**
     * Sets the global system mode of the controller.
     * 
     * @param value The new {@link SystemMode}.
     * @see OnSystemStateSystemModeChange
     */
    public void setSystemMode(SystemMode value) {
        if (systemMode == value)
            return;
        systemMode = value;
        getController().getDispatcher().trigger(new OnSystemStateSystemModeChange(systemMode));
    }

    //----------------------------------
    // keyboardMode
    //----------------------------------

    private KeyboardMode keyboardMode;

    /**
     * Returns the current {@link KeyboardMode} of the controller.
     */
    public KeyboardMode getKeyboardMode() {
        return keyboardMode;
    }

    /**
     * Sets the global keyboard mode of the controller.
     * 
     * @param value The new {@link KeyboardMode}.
     * @see OnSystemStateKeyboardModeChange
     */
    public void setKeyboardMode(KeyboardMode value) {
        if (keyboardMode == value)
            return;
        keyboardMode = value;
        getController().getDispatcher().trigger(new OnSystemStateKeyboardModeChange(keyboardMode));
    }

    public SystemState(ICaustkController controller) {
        super(controller);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    /**
     * Selects the current {@link IShiftFunction} only if the
     * {@link #isShiftEnabled()} is true.
     * 
     * @param shiftFunction The current function state for shift.
     */
    public void select(IShiftFunction shiftFunction) {
        if (!isShiftEnabled())
            return;
    }

    //--------------------------------------------------------------------------
    // Observer API
    //--------------------------------------------------------------------------

    public static class OnSystemStateShiftModeChange {
        private boolean enabled;

        public boolean getEnabled() {
            return enabled;
        }

        public OnSystemStateShiftModeChange(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class OnSystemStateSystemModeChange {
        private SystemMode mode;

        public SystemMode getMode() {
            return mode;
        }

        public OnSystemStateSystemModeChange(SystemMode mode) {
            this.mode = mode;
        }
    }

    public static class OnSystemStateKeyboardModeChange {
        private KeyboardMode mode;

        public KeyboardMode getMode() {
            return mode;
        }

        public OnSystemStateKeyboardModeChange(KeyboardMode mode) {
            this.mode = mode;
        }
    }

    public static class OnSystemStateRecordModeChange {
        private boolean recording;

        public boolean isRecording() {
            return recording;
        }

        public OnSystemStateRecordModeChange(boolean recording) {
            this.recording = recording;
        }
    }

    public enum SystemMode {
        PATTERN, SONG, GLOBAL;
    }

    public enum KeyboardMode {
        KEYBOARD, STEP;
    }

    @Override
    public void restore() {
        // TODO Auto-generated method stub
        
    }

}

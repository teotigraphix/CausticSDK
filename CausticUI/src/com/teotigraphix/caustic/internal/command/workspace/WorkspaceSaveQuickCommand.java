
package com.teotigraphix.caustic.internal.command.workspace;

import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationPreferences;
import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.command.OSCCommandBase;
import com.teotigraphix.caustic.song.IWorkspace;

public class WorkspaceSaveQuickCommand extends OSCCommandBase {

    @Inject
    IWorkspace workspace;

    @Inject
    IApplicationPreferences applicationPreferences;

    @Override
    public void execute(OSCMessage message) {
        try {
            // this is doing nothing right now, do I want to include it?
            workspace.save();
            // saves the last project path and various other settings that 
            // clients save to the preferences Editor
            // saves the project snapshot last
            applicationPreferences.quickSave();
        } catch (CausticException e) {
            Log.e("WorkspaceQuickSaveCommand", "workspace.save()", e);
        }
    }

}

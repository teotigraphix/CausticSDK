
package com.teotigraphix.libgdx.ui.caustk;

import java.io.IOException;

import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog.OnAlertDialogListener;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.teotigraphix.libgdx.model.IApplicationModel;

public final class DialogFactory {

    public static void createAndShowLoadProjectDialog(final IApplicationModel applicationModel) {
        final String[] projects = applicationModel.getProjectsAsArray();
        final ListDialog dialog = applicationModel.getDialogManager().createListDialog(
                applicationModel.getCurrentScreen(), "Select Project", projects, 400f, 400f);

        dialog.padTop(25f);
        dialog.padLeft(10f);
        dialog.padRight(10f);
        dialog.padBottom(10f);
        dialog.setOnAlertDialogListener(new OnAlertDialogListener() {
            @Override
            public void onOk() {
                int index = dialog.getSelectedIndex();
                try {
                    applicationModel.getController().getApplication().save();
                    applicationModel.loadProject(projects[index]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
            }
        });
        dialog.show(applicationModel.getCurrentScreen().getStage());
    }
}

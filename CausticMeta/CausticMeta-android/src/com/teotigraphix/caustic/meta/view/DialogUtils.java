
package com.teotigraphix.caustic.meta.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * @author Michael Schmalle
 */
public final class DialogUtils {

    private static final String CANCEL = "Cancel";

    private static final String OK = "OK";

    public static AlertDialog createAlertDialog(Activity activity, String title, int iconId,
            final Runnable runnable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle(title)
                .setIcon(iconId).setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (runnable != null)
                            runnable.run();
                        else
                            dialog.dismiss();
                    }

                });
        if (runnable != null) {
            builder.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
        }
        return builder.create();
    }
}

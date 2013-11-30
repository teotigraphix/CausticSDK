////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

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

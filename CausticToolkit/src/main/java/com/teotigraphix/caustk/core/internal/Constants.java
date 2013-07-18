
package com.teotigraphix.caustk.core.internal;

import com.teotigraphix.caustk.core.ICausticEngine;

public class Constants {
    /**
     * This has to be set at Application startup before ANYTHING happens
     * Android; Environment.getExternalStorageDirectory();
     * <p>
     * Set in the SoundGenerator {@link ICausticEngine} implementation for the
     * platform.
     */
    public static String STORAGE_ROOT = "C:\\Users\\Work\\Documents";
}

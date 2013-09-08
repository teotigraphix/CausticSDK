
package com.teotigraphix.libgdx.ui;

public class StyleUtils {
    public static <T> T getStyle(ISkinAware aware, Class<T> styleClass) {
        return styleClass.cast(aware.getSkin().get(aware.getStyleName(), styleClass));
    }
}


package com.teotigraphix.android.ui;

import android.graphics.Paint;

public final class PaintUtils {

    public static Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        return paint;
    }

    public static Paint createFillPaint(int alpha, Paint.Style style) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(alpha);
        paint.setStyle(style);
        return paint;
    }

    public static Paint createTextPaint(int color, boolean alias, float size, Paint.Align align) {
        Paint paint = createPaint(color);
        paint.setAntiAlias(alias);
        paint.setTextSize(size);
        paint.setTextAlign(align);
        return paint;
    }

}

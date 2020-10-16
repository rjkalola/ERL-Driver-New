package com.app.utilities.transformation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by jalpa on 9/25/2017.
 */

public class UtilsTransformation {
    private UtilsTransformation() {
        // Utility class.
    }

    public static Drawable getMaskDrawable(Context context, int maskId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(maskId);
        } else {
            drawable = context.getResources().getDrawable(maskId);
        }

        if (drawable == null) {
            throw new IllegalArgumentException("maskId is invalid");
        }

        return drawable;
    }
}

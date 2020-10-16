package com.app.utilities.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * @Auther Dhaval Jivani
 */

public class ToastHelper {

    /**
     * @param mContext
     * @param message
     * @param duration (Toast.LENGTH_SORT ,Toast.LENGTH_LONG ,etc)
     * @param withIcon
     */
    public static void success(Context mContext, String message, int duration, boolean withIcon) {
        if (duration == Toast.LENGTH_SHORT) {
            Toasty.success(mContext, message, Toast.LENGTH_SHORT, withIcon).show();
        } else if (duration == Toast.LENGTH_LONG) {
            Toasty.success(mContext, message, Toast.LENGTH_LONG, withIcon).show();
        } else {
            Toasty.success(mContext, message, duration, withIcon).show();
        }
    }

    public static void error(Context mContext, String message, int duration, boolean withIcon) {
//        if (duration == Toast.LENGTH_SHORT) {
//            Toasty.error(mContext, message, Toast.LENGTH_SHORT, withIcon).show();
//        } else if (duration == Toast.LENGTH_LONG) {
//            Toasty.error(mContext, message, Toast.LENGTH_LONG, withIcon).show();
//        } else {
//            Toasty.error(mContext, message, duration, withIcon).show();
//        }

        if (duration == Toast.LENGTH_SHORT) {
            Toasty.normal(mContext, message, Toast.LENGTH_SHORT).show();
        } else if (duration == Toast.LENGTH_LONG) {
            Toasty.normal(mContext, message, Toast.LENGTH_LONG).show();
        } else {
            Toasty.normal(mContext, message, duration).show();
        }
    }

    public static void info(Context mContext, String message, int duration, boolean withIcon) {
        if (duration == Toast.LENGTH_SHORT) {
            Toasty.info(mContext, message, Toast.LENGTH_SHORT, withIcon).show();
        } else if (duration == Toast.LENGTH_LONG) {
            Toasty.info(mContext, message, Toast.LENGTH_LONG, withIcon).show();
        } else {
            Toasty.info(mContext, message, duration, withIcon).show();
        }
    }

    public static void warning(Context mContext, String message, int duration, boolean withIcon) {
        if (duration == Toast.LENGTH_SHORT) {
            Toasty.warning(mContext, message, Toast.LENGTH_SHORT, withIcon).show();
        } else if (duration == Toast.LENGTH_LONG) {
            Toasty.warning(mContext, message, Toast.LENGTH_LONG, withIcon).show();
        } else {
            Toasty.warning(mContext, message, duration, withIcon).show();
        }

    }

    public static void normal(Context mContext, String message, int duration, boolean withIcon) {
        if (duration == Toast.LENGTH_SHORT) {
            Toasty.normal(mContext, message, Toast.LENGTH_SHORT).show();
        } else if (duration == Toast.LENGTH_LONG) {
            Toasty.normal(mContext, message, Toast.LENGTH_LONG).show();
        } else {
            Toasty.normal(mContext, message, duration).show();
        }
    }

    public static void custom(Context mContext, String message, Drawable icon, int duration, boolean withIcon) {
        if (duration == Toast.LENGTH_SHORT) {
            Toasty.custom(mContext, message, icon, Toast.LENGTH_SHORT, withIcon).show();
        } else if (duration == Toast.LENGTH_LONG) {
            Toasty.custom(mContext, message, icon, Toast.LENGTH_LONG, withIcon).show();
        } else {
            Toasty.custom(mContext, message, icon, duration, withIcon).show();
        }
    }

    public static void toastyConfig(int errorColor, int infoColor,
                                    int successColor, int textColor,
                                    int textSize, boolean tintIcon) {
        Toasty.Config.getInstance()
                .setErrorColor(errorColor)
                .setInfoColor(infoColor)
                .setSuccessColor(successColor)
                .setTextColor(textColor)
                .setTextSize(textSize)
                .tintIcon(tintIcon)
                .apply();
    }

    public static void toastyConfigReset() {
        Toasty.Config.reset();
    }
}

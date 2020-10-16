package com.app.utilities.utils;

/**
 * @AutherBy Dhaval Jivani
 */

public final class Constant {


    public static final class ImageScaleType{
        public static final int CENTER_CROP = 1;
        public static final int FIT_CENTER = 2;

    }

    public static final class TransformationType{
        public static final String BLUR_TRANSFORMATION =  "BLUR_TRANSFORMATION";
        public static final String COLOR_FILTER_TRANSFORMATION = "COLOR_FILTER_TRANSFORMATION";
        public static final String GRAYSCALE_TRANSFORMATION = "GRAYSCALE_TRANSFORMATION";
        public static final String ROUNDED_CORNERS_TRANSFORMATION = "ROUNDED_CORNERS_TRANSFORMATION";
        public static final String CENTERCROP_TRANSFORM = "CENTERCROP_TRANSFORM";
        public static final String CIRCLECROP_TRANSFORM = "CIRCLECROP_TRANSFORM";
     }

    public static final class IntentKey{
        public static final String MIN_DATE = "MIN_DATE";
        public static final String MAX_DATE = "MAX_DATE";
        public static final String DATE_PICKER_IDENTIFIER = "DATE_PICKER_IDENTIFIER";
        public static final String DATE = "DATE";
        public static final String TIME_PICKER_IDENTIFIER = "TIME_PICKER_IDENTIFIER";
        public static final String TIME = "TIME";
        public static final String DATE_FORMAT = "DATE_FORMAT";
        public static final String TIME_FORMAT = "TIME_FORMAT";
        public static final String SIGNUP_REQUEST = "SIGNUP_REQUEST";
    }

    public static final class SharingKey{
        public static final String TextPlain = "text/plain";
        public static final String ApplicationLinkInfo = "\nLet me recommend you this application\n\n";
        public static final String LinkToPlayStore = "https://play.google.com/store/apps/details?id=";
    }

    public static final class ImagePicker{
        public static final int REQUEST_CAMERA_KITKAT = 0;
        public static final int REQUEST_CAMERA = 1;
        public static final int REQUEST_GALLARY = 2;
        public static final int REQUEST_VIDEO = 3;
        public static final int MULTIPLE_IMAGE_SELECT = 4;
    }
}

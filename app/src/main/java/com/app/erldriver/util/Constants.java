package com.app.erldriver.util;

public class Constants {

    public static final class SharedPrefKey {
        public static final String IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH";

    }


    public static final class IntentKey {
        public static final String GALLERY_LIST = "GALLERY_LIST";
        public static final String POSITION = "POSITION";
        public static final String DONATE_REQUEST_DATA = "DONATE_REQUEST_DATA";
        public static final String TITLE = "TITLE";
        public static final String DATA_LIST = "DATA_LIST";
        public static final String IS_EDIT = "IS_EDIT";
        public static final String DONATE_ID = "DONATE_ID";
        public static final String OTP_CODE = "OPT_CODE";
        public static final String MOBILE_NO = "MOBILE_NO";
        public static final String IS_CHECKBOX_VISIBLE = "IS_CHECKBOX_VISIBLE";
        public static final String SPINNER_IDENTIFIER = "SPINNER_IDENTIFIER";
        public static final String DONOR_DATA = "DONOR_DATA";
    }

    public static final class DialogIdentifier {
        public static final int REMOVE_DONATE_DETAIL = 1;
    }

    public static final class StaticImageUrl {
        public static final String BASE_URI = VariantConfig.getServerBaseUrl()+"gallery/displaygalleryimages?imageUrl=";
    }

    public static final class SpinnerIdentifier {
        public static final int CATEGORY_LIST = 1 ;
        public static final int BOARD_MEDIUM_LIST = 2 ;
        public static final int STANDARD_LIST = 3 ;
        public static final int SUBJECT_LIST = 4 ;
    }
}

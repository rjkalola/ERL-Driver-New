package com.app.erldriver.util;

public final class AppConstant {

    public static final String DEVICE_TYPE = "1";
    public static final int DATA_PER_PAGE = 20;
    public static final int MAX_ALBUM_LENGTH = 8;
    public static final int UNAUTHORIZED = 401;
    public static final String ERROR_UNKNOWN = "ERR0001";
    public static final String EXTRA_CHANNEL_SID = "ERL";

    public static boolean IS_NOT_WANT_TOAST_ICON = false;
    public static final int MAX_IMAGE_WIDTH = 1280;
    public static final int MAX_IMAGE_HEIGHT = 1280;
    public static final int IMAGE_QUALITY = 80;
    public static boolean isOpenChatScreen = false;


    public static final class DialogIdentifier {
        public static final int LOGOUT = 1;
        public static final int SELECT_FROM_TIME = 2;
        public static final int SELECT_TO_TIME = 3;
        public static final int SELECT_CITY = 4;
        public static final int SELECT_AREA = 5;
        public static final int SELECT_TIME = 6;
        public static final int DELETE_ADDRESS = 7;
        public static final int CANCEL_ORDER = 8;
        public static final int CLEAR_CART = 9;
        public static final int SELECT_SHOP = 10;
    }

    public static final class IntentKey {
        public static final String USER_ID = "USER_ID";
        public static final String VERIFICATION_CODE = "VERIFICATION_CODE";
        public static final String EMAIL = "EMAIL";
        public static final String POSITION = "POSITION";
        public static final String DASHBOARD_DATA = "DASHBOARD_DATA";
        public static final String SERVICE_ITEMS_DATA = "SERVICE_ITEMS_DATA";
        public static final String ADDRESS_DATA = "ADDRESS_DATA";
        public static final String ORDER_DATA = "ORDER_DATA";
        public static final String IMAGE_PATH = "IMAGE_PATH";
        public static final String IMAGE_URI = "image_uri";
        public static final String CROP_RATIO_X = "crop_ratio_X";
        public static final String CROP_RATIO_Y = "crop_ratio_Y";
        public static final String FILE_EXTENSION = "file_extension";
        public static final String ITEMS_LIST = "ITEMS_LIST";
        public static final String SERVICE_HOUR_TYPE_ID = "SERVICE_HOUR_TYPE_ID";
        public static final String ORDER_TYPE = "ORDER_TYPE";
        public static final String ORDER_ID = "ORDER_ID";
        public static final String FROM_PAY = "FROM_PAY";
        public static final String TYPE = "TYPE";
        public static final String OUR_SERVICE_INFO = "OUR_SERVICE_INFO";
        public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
        public static final String IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION";
        public static final String LAST_MESSAGE_ID = "LAST_MESSAGE_ID";

        public static final int RC_LOCATION_PERM = 1;
        public static final int LOCATION_SETTING_STATUS = 2;
        public static final int CHANGE_ADDRESS = 3;
        public static final int ADD_NEW_ADDRESS = 4;
        public static final int VIEW_ORDER = 5;
        public static final int VIEW_PROFILE = 6;
        public static final int REQUEST_CROP_IMAGE = 7;
        public static final int REQUEST_GALLERY = 8;
        public static final int REQUEST_CAMERA = 9;
        public static final int REQUEST_CAMERA_KITKAT = 10;
        public static final int EXTERNAL_STORAGE_PERMISSION = 11;
        public static final int VIEW_CART = 12;

    }

    public static final class AppLanguage {
        public static final String ISO_CODE_ENG = "en";
    }

    public static final class SharedPrefKey {
        public static final String USER_INFO = "USER_INFO";
        public static final String APP_LANGUAGE = "APP_LANGUAGE";
        public static final String THEME_MODE = "THEME_MODE";
    }

    public static final class Type {
        public static final String CAMERA = "camera";
        public static final int ME = 1;
        public static final int FRIEND = 2;
        public static final int TERMS_CONDITIONS = 1;
        public static final int PRIVACY_POLICY = 2;
        public static final int OUR_SERVICES = 3;
        public static final int ORDER_PICKUPS = 1;
        public static final int ORDER_DROPS = 2;
    }

    public static final class LocationMode {
        public static final int LOCATION_MODE_OFF = 0;
        public static final int LOCATION_MODE_SENSORS_ONLY = 1;
        public static final int LOCATION_MODE_BATTERY_SAVING = 2;
        public static final int LOCATION_MODE_HIGH_ACCURACY = 3;
    }

    public static final class Action {
        public static final int SELECT_ADDRESS = 1;
        public static final int DELETE_ADDRESS = 2;
        public static final int EDIT_ADDRESS = 3;
        public static final int VIEW_ORDER = 4;
        public static final int SELECT_ALBUM = 5;
        public static final int SELECT_PHOTO = 6;
        public static final int SELECT_SERVICE_HOUR_TYPE = 7;
        public static final int PREVIEW_IMAGE = 8;
        public static final String UPDATE_CHAT_DATA = "updatechatdata";
    }


    public static final class DrawerState {
        public static final String COLLAPSED = "COLLAPSED";
        public static final String ANCHORED = "ANCHORED";
        public static final String EXPANDED = "EXPANDED";

    }

    public static final class NAVIGATION_ITEM {
        public static final int MY_PROFILE = 1;
        public static final int MY_ORDER = 2;
        public static final int SETTINGS = 3;
        public static final int PRIVACY_POLICY = 4;
        public static final int TERMS_AND_CONDITIONS = 5;
        public static final int ABOUT_APP = 6;
        public static final int SHARE = 7;
        public static final int LOGOUT = 8;
    }

    public static final class UserType {
        public static final int ADMIN = 1;
        public static final int SUPERVISOR = 2;
        public static final int PROJECT_MANAGER = 3;
        public static final int EMPLOYEE = 4;
        public static final int DRIVER = 5;
    }

    public static final class THEME_MODE {
        public static final int LIGHT = 0;
        public static final int DARK = 1;
    }

    public static final class FileExtension {
        public static final String JPG = ".jpg";
        public static final String PNG = ".png";
        public static final String PDF = ".pdf";
        public static final String MP3 = ".mp3";
        public static final String M4A = ".m4a";
    }

    public static final class Directory {
        public static final String DEFAULT = "owlmanagement";
        public static final String PDF = "owlmanagement/digitalcard";
        public static final String IMAGES = "owlmanagement/images";
        public static final String AUDIO = "owlmanagement/audio";
    }

    public static final String APP_APK_NAME = "RetailerNavneetDev.apk";

}

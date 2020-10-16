package com.app.erldriver.util;

/**
 *
 */
public class VariantConfig {

    public static String getServerBaseUrl() {
        //live ip
        return "http://erl.workpotency.com/api/v1/rider/";
    }

    public static String getApkfilePath() {
        return getServerBaseUrl() + "mobile/" + AppConstant.APP_APK_NAME;
    }

}

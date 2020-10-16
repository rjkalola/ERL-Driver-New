package com.app.erldriver.util;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.erldriver.ERLApp;
import com.app.erldriver.R;
import com.app.erldriver.model.entity.info.FcmData;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.model.entity.response.User;
import com.app.erldriver.network.RetrofitException;
import com.app.erldriver.view.activity.BaseActivity;
import com.app.imagepicker.Model.FileWithPath;
import com.app.imagepicker.utils.ImageUtil;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.DateFormatsConstants;
import com.app.utilities.utils.ImageUtils;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.app.erldriver.ERLApp.getContext;
import static com.app.erldriver.util.AppConstant.IMAGE_QUALITY;
import static com.app.erldriver.util.AppConstant.MAX_IMAGE_HEIGHT;
import static com.app.erldriver.util.AppConstant.MAX_IMAGE_WIDTH;

/**
 * Created by Jacks on 31-05-2017.
 */

public final class AppUtils {
    public static User getUserPrefrence(Context context) {
        if (context != null
                && context.getApplicationContext() != null) {
            Gson gson = ((ERLApp) context.getApplicationContext()).getNetworkComponent().provideGson();
            if (gson != null) {
                String userInfo = ERLApp.preferenceGetString(AppConstant.SharedPrefKey.USER_INFO, "");
                if (!StringHelper.isEmpty(userInfo)) {
                    return gson.fromJson(userInfo, User.class);
                }
            }
        }
        return null;
    }

    public static void setUserPrefrence(Context context, User userInfo) {
        if (context != null
                && context.getApplicationContext() != null) {
            Gson gson = ((ERLApp) context.getApplicationContext()).getNetworkComponent().provideGson();
            if (gson != null) {
                ERLApp.preferencePutString(AppConstant.SharedPrefKey.USER_INFO, gson.toJson(userInfo));
            }
        }
    }


    public static void showError(Context mContext, String errorCode) {
        try {
            if (errorCode.contains("ERR") || errorCode.contains("SUCC")) {
                ToastHelper.error(mContext, StringHelper.getStringResourceByName(mContext, errorCode)
                        , Toast.LENGTH_LONG, AppConstant.IS_NOT_WANT_TOAST_ICON);
            } else {
                ToastHelper.error(mContext, errorCode
                        , Toast.LENGTH_LONG, AppConstant.IS_NOT_WANT_TOAST_ICON);
            }
        } catch (Exception e) {

        }
    }

    public static FileWithPath compressImage(String path, File file) throws IOException {
        if (file != null && file.exists()) {
            FileWithPath fileWithPath;
            long fileSize = file.length();
            long fileSizeInKB = fileSize / 1024;
            if (fileSizeInKB > 1024) {
                fileWithPath = ImageUtil.createImageFile(Environment.DIRECTORY_DCIM);
                fileWithPath.setUri(Uri.fromFile(fileWithPath.getFile()));
                ImageUtils.compress(path, fileWithPath.getFile().getAbsolutePath(), MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT, IMAGE_QUALITY);
                return fileWithPath;
            }
        }
        return null;
    }


    //------------------------------------get application version-------------------------------------------
    public static String getApplicationVersion(Context mContext) {
        PackageInfo pinfo = null;
        try {
            pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pinfo.versionName;//versionCode
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }

    //------------------------------------to get android id--------------------------------------
    public static String getAndroidId(Context ctx) {
        String androidId;
        final Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        final String ID_KEY = "android_id";
        String[] params = {ID_KEY};
        Cursor c = null;

        c = ctx.getContentResolver().query(URI, null, null, params, null);

        if (c == null || !c.moveToFirst() || c.getColumnCount() < 2)
            return null;
        try {
            androidId = Long.toHexString(Long.parseLong(c.getString(1)));
            c.close();
            return androidId;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //------------------------------------get android version-------------------------------------------
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    //-----------------------------------------------------getDevice Name------------------------------------------
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static String apkStorePath(Context context, String storePath) {

       /* String EXTERNAL_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/NavneetRetailer";//OrderManagement Download

        return EXTERNAL_DIRECTORY_PATH;*/
        String root = Environment.getExternalStorageDirectory().toString();
        if (StringHelper.isEmpty(storePath)) {
            return root + File.separator + Environment.DIRECTORY_DOWNLOADS;
        } else {
            return root + File.separator + storePath;
        }
    }


    //------------------------------------Copy and rename  image-------------------------------------------
    public static String copyFileFromUri(Context context, Uri fileUri, String newFileName) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File saveDirectory = null;
        try {
            ContentResolver content = context.getContentResolver();
            inputStream = content.openInputStream(fileUri);

            saveDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera/");

            // create direcotory if it doesn't exists
            if (!saveDirectory.exists()) {
                saveDirectory.mkdirs();
            }

            outputStream = new FileOutputStream(saveDirectory + "/" + newFileName); // filename.png, .mp3, .mp4 ...
            if (outputStream != null) {
                Log.e("occurred", "Output Stream Opened successfully");
            }

            byte[] buffer = new byte[1000];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                outputStream.write(buffer, 0, buffer.length);
            }
        } catch (Exception e) {

        }
        return saveDirectory.getAbsolutePath() + "/" + newFileName;
    }

    public static void handleUnauthorized(Context context, BaseResponse baseResponse) {

        if (context == null || baseResponse == null) return;

        if (baseResponse.getErrorCode()
                == AppConstant.UNAUTHORIZED
        ) {
            AlertDialogHelper.showDialog(context, null, context.getString(R.string.msg_unauthorized),
                    context.getString(R.string.ok), null, false, new DialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClicked(int dialogIdentifier) {
                            ERLApp.get().clearData();
                            if (context instanceof BaseActivity) {
//                                ((BaseActivity) context).moveActivity(context, MainActivity.class, true, true, null);
                            }
                        }

                        @Override
                        public void onNegativeButtonClicked(int dialogIdentifier) {

                        }
                    }, 0);
        } else {
            handleResponseMessage(context, baseResponse.getMessage());
        }
    }

    public static String getStringResourceByName(Context mContext, String aString) {
        try {

            if (mContext == null) return "";

            if (!StringHelper.isEmpty(aString)) {
                String packageName = mContext.getPackageName();
                String message = mContext.getString(mContext.getResources().getIdentifier(aString, "string", packageName));
                if (StringHelper.isEmpty(message)) {
                    message = mContext.getString(R.string.error_unknown);
                }
                return message;
            } else {
                return mContext.getString(R.string.error_unknown);
            }
        } catch (Exception e) {

        }
        return "";
    }

    public static String getHttpErrorMessage(Context context, int statusCode) {
        String errorMessage = "";
        switch (statusCode) {
            case 400:
                errorMessage = context.getString(R.string.error_bad_request_400);
                break;
            case 401:
                errorMessage = context.getString(R.string.error_unauthorized_401);
                break;
            case 403:
                errorMessage = context.getString(R.string.error_forbidden_403);
                break;
            case 404:
                errorMessage = context.getString(R.string.error_not_found_404);
                break;
            case 405:
                errorMessage = context.getString(R.string.error_method_not_allowed_405);
                break;
            case 408:
                errorMessage = context.getString(R.string.error_request_timeout_408);
                break;
            case 413:
                errorMessage = context.getString(R.string.error_request_entity_too_large_413);
                break;
            case 414:
                errorMessage = context.getString(R.string.error_request_uri_too_long_414);
                break;
            case 500:
                errorMessage = context.getString(R.string.error_internal_server_error_500);
                break;
            default:
                errorMessage = context.getString(R.string.error_unknown);
                break;
        }
        return errorMessage;

    }

    public static void handleResponseMessage(Context context, String messageString) {
        try {
            if (context == null) return;
            String message = "";
            if (messageString.contains("ERR") || messageString.contains("SUCC")) {
                message = AppUtils.getStringResourceByName(context, messageString);
            } else {
                message = messageString;
            }
            AlertDialogHelper.showDialog(context, null, message, context.getString(R.string.ok),
                    null, false, null, 0);
        } catch (Exception e) {

        }
    }

    public static void handleApiError(Context context, RetrofitException retrofitException) {
        try {
            if (context == null) return;

            if (retrofitException != null) {

                switch (retrofitException.getKind()) {
                    case HTTP:
                        if (retrofitException.getRetrofitExceptionBody() != null) {
                            String errorMessage = getHttpErrorMessage(context, retrofitException.getRetrofitExceptionBody().getStatus());
                            AlertDialogHelper.showDialog(context, null, errorMessage
                                    , context.getString(R.string.ok_utils), null, false,
                                    null, 0);
                        }
                        break;
                    case NETWORK:
                        AlertDialogHelper.showDialog(context, null, context.getString(R.string.error_network)
                                , context.getString(R.string.ok_utils), null, false,
                                true);
                        break;
                    case UNEXPECTED:
                        AlertDialogHelper.showDialog(context, null, context.getString(R.string.error_unknown)
                                , context.getString(R.string.ok_utils), null, false,
                                null, 0);
                        break;
                }
            } else {
                AlertDialogHelper.showDialog(context, null, context.getString(R.string.error_unknown_utils)
                        , context.getString(R.string.ok_utils), null, false,
                        null, 0);
            }
        } catch (Exception e) {

        }
    }

    public static void setToolbarTextColor(MenuItem item, String title, int color) {
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
        item.setTitle(s);
    }

    public static long getCurrentDateInLong() {
        return System.currentTimeMillis();
    }


    public static void hideKeyBoard(Context mContext, View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static long getCheckInTimeInMilliSecond(String checkInTime, String breakInTime, int breakInLog, int checkInLog) {
        long milliSeconds = 0, breakInSeconds = 0;
        try {
            if (!StringHelper.isEmpty(checkInTime)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatsConstants.YYYY_MM_DD_TIME_24_DASH2, Locale.US);
                Date startDate = simpleDateFormat.parse(checkInTime);
                Date endDate = new Date();

                if (!StringHelper.isEmpty(breakInTime)) {
                    Date breakInStartDate = simpleDateFormat.parse(breakInTime);
                    breakInSeconds = endDate.getTime() - breakInStartDate.getTime();
                }
                milliSeconds = endDate.getTime() - startDate.getTime() + checkInLog * 1000 - breakInLog * 1000 - breakInSeconds;
            } else {
                milliSeconds = checkInLog * 1000 - breakInLog * 1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return milliSeconds;
    }

    public static long getBreakInTimeInMilliSecond(String breakInTime, int breakInLog) {
        long milliSeconds = 0;
        try {
            if (!StringHelper.isEmpty(breakInTime)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatsConstants.YYYY_MM_DD_TIME_24_DASH2, Locale.US);
                Date startDate = simpleDateFormat.parse(breakInTime);
                Date endDate = new Date();
                long difference = endDate.getTime() - startDate.getTime();
                milliSeconds = difference + breakInLog * 1000;
            } else {
                milliSeconds = breakInLog * 1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return milliSeconds;
    }

    public static FileWithPath createImageFile(String title, String type, String imageExtension) throws IOException {
        String imageFileName = "";
        File storageDir = null;
        FileWithPath fileWithPath = new FileWithPath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        if (imageExtension.equals(AppConstant.FileExtension.PDF)) {
            imageFileName = "DOCUMENT_" + timeStamp + "_";
            storageDir = new File(Environment.getExternalStorageDirectory(), AppConstant.Directory.PDF);
        } else if (imageExtension.equals(AppConstant.FileExtension.M4A)) {
            imageFileName = "MUSIC_" + timeStamp + "_";
            storageDir = new File(Environment.getExternalStorageDirectory(), AppConstant.Directory.AUDIO);
        } else {
            if (type.equals(AppConstant.Type.CAMERA)) {
                imageFileName = "IMAGE_" + timeStamp + "_";
                storageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM), "Camera");
            } else {
                imageFileName = "IMAGE_" + timeStamp + "_";
                storageDir = new File(Environment.getExternalStorageDirectory(), AppConstant.Directory.IMAGES);
            }
        }

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                imageExtension,         /* suffix */
                storageDir      /* directory */
        );

        String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        fileWithPath.setFile(image);
        fileWithPath.setFilePath(mCurrentPhotoPath);
        fileWithPath.setUri(Uri.fromFile(image));
        return fileWithPath;
    }

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static String getFilePathFromBitmap(Context mContext, Bitmap bitmap, String imageExtension) {
        String filePath = "";
        File filesDir = mContext.getFilesDir();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File imageFile = new File(filesDir, timeStamp + imageExtension);
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            if (imageExtension.equals(AppConstant.FileExtension.PNG))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            else
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            filePath = imageFile.getAbsolutePath();
        } catch (Exception e) {

        }
        return filePath;
    }

    public String getAppVersionName(Context mContext) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        return packageInfo.versionName;
    }

    public static void openPlayStore(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageInfo.packageName)));
        } catch (PackageManager.NameNotFoundException e) {

        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageInfo.packageName)));
        }
    }

    public static String formatFileSize(double size) {
        String hrSize = null;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    public static String convertLongDateToString(long longDate, String dateFormat) {
        try {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            calendar.setTimeInMillis(longDate);
            SimpleDateFormat df2 = new SimpleDateFormat(dateFormat);
            String dateText = df2.format(calendar.getTime());
            return dateText;
        } catch (Exception e) {
            Log.e("Error LongDateToString", "" + e);
        }
        return "";
    }

    public static void removeAllFilesFromFolder(File dir) {
        try {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean isMyServiceRunning(Context mContext, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getFullAddress(Context mContext, double latitude, double longitude) {
        String currentLocation = "";
        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            currentLocation = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentLocation;
    }

    public static int getAlbumCount(Context c, String albumName) {
        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED};
        Cursor cursorExternal = c.getContentResolver().query(uriExternal, projection, "bucket_display_name = \"" + albumName + "\"", null, null);
        Cursor cursorInternal = c.getContentResolver().query(uriInternal, projection, "bucket_display_name = \"" + albumName + "\"", null, null);
        Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});
        return cursor.getCount();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static Intent getFcmIntent(FcmData data) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.IntentKey.NOTIFICATION_TYPE, data.getType());
        Log.e("test","5");
        switch (data.getType()) {
            case "5000":
                Log.e("test","6");
                intent = new Intent("com.app.erldriver.view.activity.ChatActivity");
                break;
            default:
                Log.e("test","7");
                intent = new Intent("com.app.erldriver.view.activity.DashboardActivity");
        }

        if (intent != null) {
            intent.putExtras(bundle);
        }

        return intent;
    }

    public static String getDeviceUniqueId() {
        try {
            String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return android_id;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return "";
    }
}

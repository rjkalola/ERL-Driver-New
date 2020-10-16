package com.app.utilities.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jalpa on 9/28/2017.
 */

public class SocialSharingUtils {

    /**
     * @param mContext
     * @param body        ( text to share )
     * @param intentTitle
     */
    public static void SocialTextSharing(Context mContext, String body, String intentTitle) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
        mContext.startActivity(Intent.createChooser(sharingIntent, intentTitle));
    }


    /**
     * @param mContext
     * @param mBitmap
     * @param intentTitle
     */
    public static void SocialImageSharing(Context mContext, Bitmap mBitmap, String intentTitle) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mBitmap, "Title", null);
        Uri uri = Uri.parse(path);

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        mContext.startActivity(Intent.createChooser(shareIntent, intentTitle));
    }


    /**
     * @param drawable
     * @return
     */
    public static Uri getLocalBitmapUri(Drawable drawable) {
        // Extract Bitmap from ImageView drawable
        // Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) drawable).getBitmap();
        } else {
            return null;
        }

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    /**
     * @param mContext
     * @param htmlFormate
     * @param intentTitle
     */
    public static void SocialHTMLSharing(Context mContext, String htmlFormate, String intentTitle) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(htmlFormate));
        mContext.startActivity(Intent.createChooser(sharingIntent, intentTitle));
    }


    /**
     * @param mContext
     * @param body        ( text to share )
     * @param mBitmap    ( image to share )
     * @param intentTitle
     */
    public static void SocialMultipleSharing(Context mContext, String body, Bitmap mBitmap, String intentTitle) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mBitmap, "Title", null);
        Uri uri = Uri.parse(path);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, body);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(shareIntent, intentTitle));
    }


    /**
     * @param mContext
     * @param webViewArticle
     * @param intentTitle
     */
    public static void SocialWebViewUrlSharing(Context mContext, WebView webViewArticle, String intentTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(Constant.SharingKey.TextPlain);
        // pass in the URL currently being used by the WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT, webViewArticle.getUrl());
        mContext.startActivity(Intent.createChooser(shareIntent, intentTitle));
    }


    /**
     *
     * @param mContext
     * @param intentTitle
     */
    public static void SocialApplicationLinkSharing(Context mContext ,String intentTitle){
        try {
             String appPackageName = mContext.getApplicationContext().getPackageName();
            String applicationName = mContext.getApplicationInfo().loadLabel(mContext.getPackageManager()).toString();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType(Constant.SharingKey.TextPlain);
            i.putExtra(Intent.EXTRA_SUBJECT, applicationName);
            String sAux = Constant.SharingKey.ApplicationLinkInfo;
            sAux = sAux + Constant.SharingKey.LinkToPlayStore + appPackageName  +"\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            mContext.startActivity(Intent.createChooser(i, intentTitle));
        } catch(Exception e) {
            e.toString();
        }
    }
}

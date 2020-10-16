package com.app.imagepicker;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.app.imagepicker.Model.FileWithPath;
import com.app.imagepicker.utils.ImageUtil;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.FileUtils;
import com.app.utilities.utils.StringHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jalpa on 10/5/2017.
 */

public class ImagePickerUtils extends AppCompatActivity {
    public static String mCurrentPhotoPath;
    private static Uri photoURI;


    /**
     * TODO:          Step 1: Add this method where show imagePicker
     * TODO:          Step 2: if requestCode = Constant.ImagePicker.REQUEST_CAMERA
     * then Add this code in "onActivityResult"
     * if (requestCode == Constant.ImagePicker.REQUEST_CAMERA_KITKAT) {
     * Uri uri = ImagePickerUtils.getUriForKitKat(mContext, data);
     * } else if (requestCode == Constant.ImagePicker.REQUEST_CAMERA) {
     * Uri uri = ImagePickerUtils.getUriFormCamera(mContext,data);
     * }
     * <p>
     * TODO:          Step 2: if requestCode = Constant.ImagePicker.REQUEST_GALLARY
     * then Add this code in "onActivityResult"
     * if (requestCode == Constant.ImagePicker.REQUEST_GALLARY) {
     * Uri selectedMediaUri = data.getData();
     * }
     * <p>
     * TODO:          Step 2: if requestCode = Constant.ImagePicker.REQUEST_VIDEO
     * then Add this code in "onActivityResult"
     * if (requestCode == Constant.ImagePicker.REQUEST_VIDEO) {
     * Uri videoUri = data.getData();
     * }
     * TODO:          Step 2: if requestCode = Constant.ImagePicker.MULTIPLE_IMAGE_SELECT
     * then Add this code in "onActivityResult"
     * if (requestCode == Constant.ImagePicker.MULTIPLE_IMAGE_SELECT) {
     * ArrayList<Uri> uriArrayList = ImagePickerUtils.getImageUrlList(mContext , data);
     * }
     *
     * @param mContext
     * @param requestCode (Constant.ImagePicker.REQUEST_CAMERA, Constant.ImagePicker.REQUEST_GALLARY, Constant.ImagePicker.REQUEST_VIDEO, Constant.ImagePicker.MULTIPLE_IMAGE_SELECT )
     */
    public static void getDataFromImagePicker(Context mContext, int requestCode) {
        switch (requestCode) {
            case Constant.ImagePicker.REQUEST_CAMERA:
                try {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        ((Activity) mContext).startActivityForResult(takePictureIntent, Constant.ImagePicker.REQUEST_CAMERA_KITKAT);  //REQUEST_CAMERA_KITKAT
                    } else {
                        try {
                            FileWithPath fileWithPath = ImageUtil.createImageFile(Environment.DIRECTORY_DCIM);
                            mCurrentPhotoPath = fileWithPath.getFilePath();
                            if (fileWithPath != null) {
                                photoURI = FileProvider.getUriForFile(mContext, mContext.getPackageName()+".provider", fileWithPath.getFile());
                                /* BuildConfig.APPLICATION_ID*/
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                                    ((Activity) mContext).startActivityForResult(takePictureIntent, requestCode);
                                }
                            }
                        } catch (IOException ex) {
                            ex.getMessage();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.ImagePicker.REQUEST_GALLARY:
                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/* video/*");
                ((Activity) mContext).startActivityForResult(pickIntent, requestCode);
                break;

            case Constant.ImagePicker.REQUEST_VIDEO:
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    ((Activity) mContext).startActivityForResult(takeVideoIntent, requestCode);
                }
                break;

            case Constant.ImagePicker.MULTIPLE_IMAGE_SELECT:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
                break;

            default:
                break;
        }
    }

    /**
     * @param mContext
     * @param data
     * @return
     */
    public static Uri getUriFormCamera(Context mContext, Intent data) {
        Uri imageUri = null;
        FileWithPath fileWithPath = null;
        try {
            fileWithPath = ImageUtil.createImageFile(Environment.DIRECTORY_DCIM);
            String mCurrentPhotoPath = fileWithPath.getFilePath();
            imageUri = Uri.parse(mCurrentPhotoPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;
    }

    /**
     * @param mContext
     * @param data
     * @return
     */
    public static Uri getUriForKitKat(Context mContext, Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        Uri imageUri = getImageUri(mContext, photo);
        return imageUri;
    }


    /**
     * @param mContext
     * @param inImage
     * @return
     */
    public static Uri getImageUri(Context mContext, Bitmap inImage) {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        File pictureFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        if (!pictureFolder.exists()) {
            pictureFolder.mkdirs();
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), inImage, "Title", null);
        if (!StringHelper.isEmpty(path)) {
            return Uri.parse(path);
        }
        return null;
    }

    /**
     * @param mContext
     * @param data
     * @return
     */
    public static ArrayList<Uri> getImageUrlList(Context mContext, Intent data) {
        String imageEncoded;
        List<String> imagesEncodedList;

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        imagesEncodedList = new ArrayList<String>();
        if (data.getData() != null) {

            Uri uri = null;

            if(FileUtils.isMediaDocument(data.getData())){
                uri = data.getData();
            }else if (FileUtils.isExternalStorageDocument(data.getData())) {
                uri = data.getData();
            }else if(FileUtils.isDownloadsDocument(data.getData())){
                uri = data.getData();
            }else{
                uri = getImageContentUri(mContext,new File(data.getData().getPath()));
            }

            // Get the cursor
            Cursor cursor = ((Activity) mContext).getContentResolver().query(uri,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageEncoded = cursor.getString(columnIndex);
            cursor.close();

        } else {
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                for (int i = 0; i < mClipData.getItemCount(); i++) {

                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                    // Get the cursor
                    Cursor cursor = ((Activity) mContext).getContentResolver().query(uri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    imagesEncodedList.add(imageEncoded);
                    cursor.close();

                }
                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                return mArrayUri;
            }

        }
        return null;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}

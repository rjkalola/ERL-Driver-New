package com.app.imagepicker.utils;

import android.os.Environment;

import com.app.imagepicker.Model.FileWithPath;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jalpa on 10/6/2017.
 */

public class ImageUtil {

    /**
     * @param type
     * @return
     * @throws IOException
     */
    public static FileWithPath createImageFile(String type) throws IOException {
        FileWithPath fileWithPath = new FileWithPath();
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                type), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        fileWithPath.setFile(image);
        fileWithPath.setFilePath(mCurrentPhotoPath);
        return fileWithPath;
    }


}

package com.app.imagepicker.Model;

import android.net.Uri;

import java.io.File;

/**
 * Created by jalpa on 10/5/2017.
 */

public class FileWithPath {
    private File file;
    private String filePath;
    private Uri uri;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}

package com.app.erldriver.model.entity.info;

import org.parceler.Parcel;

@Parcel
public class AlbumInfo {
    private String path, album, timestamp;
    private int countPhoto;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getCountPhoto() {
        return countPhoto;
    }

    public void setCountPhoto(int countPhoto) {
        this.countPhoto = countPhoto;
    }
}

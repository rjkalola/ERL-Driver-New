package com.app.erldriver.model.entity.dashboard;

import org.parceler.Parcel;

/**
 * Created by admin on 12-02-2018.
 */
@Parcel
public class DashboardModel {

    String title;
    int imageUrl;
    int colorCode;

    public DashboardModel(String title, int color, int imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.colorCode = color;
    }

    public DashboardModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}

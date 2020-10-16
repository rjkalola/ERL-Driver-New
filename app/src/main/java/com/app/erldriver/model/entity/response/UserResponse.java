
package com.app.erldriver.model.entity.response;


import org.parceler.Parcel;

@Parcel
public class UserResponse extends BaseResponse {
    User info;

    public User getInfo() {
        return info;
    }

    public void setInfo(User info) {
        this.info = info;
    }

}


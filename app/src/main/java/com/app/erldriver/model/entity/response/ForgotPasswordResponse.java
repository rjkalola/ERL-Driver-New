
package com.app.erldriver.model.entity.response;


public class ForgotPasswordResponse extends BaseResponse {
    private int user_id;
    private String access_code;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }
}


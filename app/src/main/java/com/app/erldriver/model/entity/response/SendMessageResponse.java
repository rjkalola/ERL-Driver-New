
package com.app.erldriver.model.entity.response;


import com.app.erldriver.model.entity.info.MessageInfo;

public class SendMessageResponse extends BaseResponse {
    MessageInfo info;

    public MessageInfo getInfo() {
        return info;
    }

    public void setInfo(MessageInfo info) {
        this.info = info;
    }
}



package com.app.erldriver.model.entity.response;


import com.app.erldriver.model.entity.info.MessageInfo;

import java.util.List;

public class GetMessagesResponse extends BaseResponse {
    List<MessageInfo> info;

    public List<MessageInfo> getInfo() {
        return info;
    }

    public void setInfo(List<MessageInfo> info) {
        this.info = info;
    }
}



package com.app.erldriver.model.entity.response;


import com.app.erldriver.model.entity.info.OrderInfo;

import java.util.List;

public class OrderListResponse extends BaseResponse {
    private List<OrderInfo> info;
    private int offset;

    public List<OrderInfo> getInfo() {
        return info;
    }

    public void setInfo(List<OrderInfo> info) {
        this.info = info;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}



package com.app.erldriver.model.entity.response;


import com.app.erldriver.model.entity.info.ModuleInfo;

import java.util.List;

public class StoreLocatorResponse extends BaseResponse {
    private List<ModuleInfo> info;

    public List<ModuleInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ModuleInfo> info) {
        this.info = info;
    }
}


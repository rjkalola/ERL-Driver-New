
package com.app.erldriver.model.entity.response;


import com.app.erldriver.model.entity.info.PrivacyPolicyInfo;

import java.util.List;

public class OurServicesResponse extends BaseResponse {
    private List<PrivacyPolicyInfo> info;

    public List<PrivacyPolicyInfo> getInfo() {
        return info;
    }

    public void setInfo(List<PrivacyPolicyInfo> info) {
        this.info = info;
    }
}


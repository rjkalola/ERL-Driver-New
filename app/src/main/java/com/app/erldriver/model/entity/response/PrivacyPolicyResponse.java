
package com.app.erldriver.model.entity.response;


import com.app.erldriver.model.entity.info.PrivacyPolicyInfo;

public class PrivacyPolicyResponse extends BaseResponse {
    private PrivacyPolicyInfo info;

    public PrivacyPolicyInfo getInfo() {
        return info;
    }

    public void setInfo(PrivacyPolicyInfo info) {
        this.info = info;
    }
}


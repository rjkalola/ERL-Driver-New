
package com.app.erldriver.model.entity.response;


import com.app.erldriver.model.entity.info.ModuleInfo;

import java.util.List;

public class AddressResourcesResponse extends BaseResponse {
    private String name, phone;
    private List<ModuleInfo> area, cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ModuleInfo> getArea() {
        return area;
    }

    public void setArea(List<ModuleInfo> area) {
        this.area = area;
    }

    public List<ModuleInfo> getCities() {
        return cities;
    }

    public void setCities(List<ModuleInfo> cities) {
        this.cities = cities;
    }
}


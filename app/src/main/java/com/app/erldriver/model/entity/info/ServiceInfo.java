
package com.app.erldriver.model.entity.info;

import java.util.List;

public class ServiceInfo {
    private int id;
    private String name;
    private List<PriceListInfo> priceList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PriceListInfo> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceListInfo> priceList) {
        this.priceList = priceList;
    }
}




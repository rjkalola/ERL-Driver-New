
package com.app.erldriver.model.entity.info;

import java.util.List;

public class OrderItemInfo {
    private int id;
    private String name;
    private List<ServiceItemInfo> data;

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

    public List<ServiceItemInfo> getData() {
        return data;
    }

    public void setData(List<ServiceItemInfo> data) {
        this.data = data;
    }
}




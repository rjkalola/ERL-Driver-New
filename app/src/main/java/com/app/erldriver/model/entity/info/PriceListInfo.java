
package com.app.erldriver.model.entity.info;

import java.util.List;

public class PriceListInfo {
    private int id;
    private String name;
    private List<ItemInfo> items;

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

    public List<ItemInfo> getItems() {
        return items;
    }

    public void setItems(List<ItemInfo> items) {
        this.items = items;
    }
}




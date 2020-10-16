
package com.app.erldriver.model.entity.info;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ClientDashBoardInfo {
    int id, count_order_items;
    String name, image;
    List<ServiceItemInfo> service_item;

    public ClientDashBoardInfo() {

    }

    public ClientDashBoardInfo(int id, String name, String image, int count_order_items) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.count_order_items = count_order_items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount_order_items() {
        return count_order_items;
    }

    public void setCount_order_items(int count_order_items) {
        this.count_order_items = count_order_items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ServiceItemInfo> getService_item() {
        return service_item;
    }

    public void setService_item(List<ServiceItemInfo> service_item) {
        this.service_item = service_item;
    }
}




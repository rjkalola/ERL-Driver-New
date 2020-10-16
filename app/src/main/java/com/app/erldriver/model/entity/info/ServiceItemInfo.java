
package com.app.erldriver.model.entity.info;

import org.parceler.Parcel;

@Parcel
public class ServiceItemInfo {
    int id, qty, price,wash_id;
    String name, image;

    public ServiceItemInfo() {

    }

    public ServiceItemInfo(int id, String name, int price, int quantity, String image) {
        this.id = id;
        this.qty = quantity;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public ServiceItemInfo(int id,int wash_id, int quantity) {
        this.id = id;
        this.wash_id = wash_id;
        this.qty = quantity;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return qty;
    }

    public void setQuantity(int quantity) {
        this.qty = quantity;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWash_id() {
        return wash_id;
    }

    public void setWash_id(int wash_id) {
        this.wash_id = wash_id;
    }
}




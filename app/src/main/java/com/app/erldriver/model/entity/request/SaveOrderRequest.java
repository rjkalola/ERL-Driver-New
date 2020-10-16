package com.app.erldriver.model.entity.request;

import com.app.erldriver.model.entity.info.ServiceItemInfo;

import java.util.ArrayList;
import java.util.List;

public class SaveOrderRequest {
    private int pickup_hour_id, address_id,lu_service_hour_type_id,type;
    private String pickup_date;
    private List<ServiceItemInfo> order = new ArrayList<>();

    public int getPickup_hour_id() {
        return pickup_hour_id;
    }

    public void setPickup_hour_id(int pickup_hour_id) {
        this.pickup_hour_id = pickup_hour_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public List<ServiceItemInfo> getOrder() {
        return order;
    }

    public void setOrder(List<ServiceItemInfo> order) {
        this.order = order;
    }

    public int getLu_service_hour_type_id() {
        return lu_service_hour_type_id;
    }

    public void setLu_service_hour_type_id(int lu_service_hour_type_id) {
        this.lu_service_hour_type_id = lu_service_hour_type_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

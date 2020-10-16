
package com.app.erldriver.model.entity.info;

public class ModuleInfo {
    private int id, city_id,tag;
    private String name, zip_code,address,latitude,longitude;
    private boolean check;

    public void copyData(ModuleInfo info){
        this.id = info.getId();
        this.city_id = info.getCity_id();
        this.name = info.getName();
        this.zip_code = info.getZip_code();
        this.address = info.getAddress();
        this.latitude = info.getLatitude();
        this.longitude = info.getLongitude();
        this.check = info.isCheck();
        this.tag = info.getTag();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}




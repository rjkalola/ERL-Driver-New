
package com.app.erldriver.model.entity.response;

public class DashBoardResponse extends BaseResponse {
    private int pickupCount, dropCount;

    public int getPickupCount() {
        return pickupCount;
    }

    public void setPickupCount(int pickupCount) {
        this.pickupCount = pickupCount;
    }

    public int getDropCount() {
        return dropCount;
    }

    public void setDropCount(int dropCount) {
        this.dropCount = dropCount;
    }
}




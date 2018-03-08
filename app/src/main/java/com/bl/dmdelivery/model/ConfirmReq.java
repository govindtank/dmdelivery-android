package com.bl.dmdelivery.model;

/**
 * Created by thundorn_j on 8/3/2561.
 */

public class ConfirmReq {
    private String truckNo;
    private String deliveryDate;

    public String getTruckNo() {
        return truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}

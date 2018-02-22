package com.bl.dmdelivery.model;

/**
 * Created by tHundorn_j on 22/2/2561.
 */

public class OrderScanReq {
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

    private String truckNo;
    private String deliveryDate;
}

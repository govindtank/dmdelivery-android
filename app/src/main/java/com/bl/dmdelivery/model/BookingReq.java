package com.bl.dmdelivery.model;

/**
 * Created by thundorn_j on 4/3/2561.
 */

public class BookingReq {
    private String CartonNo;
    private String truckNo;
    private String deliveryDate;

    public String getCartonNo() {
        return CartonNo;
    }

    public void setCartonNo(String cartonNo) {
        CartonNo = cartonNo;
    }

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

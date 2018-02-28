package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thundorn_j on 27/2/2561.
 */

public class OrderSummary implements Parcelable {
    private String DeliveryDate;
    private String TruckNo;
    private Integer Invoiceno;
    private Integer CartonQty;
    private Integer Bags;
    private Integer Total;

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getTruckNo() {
        return TruckNo;
    }

    public void setTruckNo(String truckNo) {
        TruckNo = truckNo;
    }

    public Integer getInvoiceno() {
        return Invoiceno;
    }

    public void setInvoiceno(Integer invoiceno) {
        Invoiceno = invoiceno;
    }

    public Integer getCartonQty() {
        return CartonQty;
    }

    public void setCartonQty(Integer cartonQty) {
        CartonQty = cartonQty;
    }

    public Integer getBags() {
        return Bags;
    }

    public void setBags(Integer bags) {
        Bags = bags;
    }

    public Integer getTotal() {
        return Total;
    }

    public void setTotal(Integer total) {
        Total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DeliveryDate);
        dest.writeString(this.TruckNo);
        dest.writeValue(this.Invoiceno);
        dest.writeValue(this.CartonQty);
        dest.writeValue(this.Bags);
        dest.writeValue(this.Total);
    }

    public OrderSummary() {
    }

    protected OrderSummary(Parcel in) {
        this.DeliveryDate = in.readString();
        this.TruckNo = in.readString();
        this.Invoiceno = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CartonQty = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Bags = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Total = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<OrderSummary> CREATOR = new Creator<OrderSummary>() {
        @Override
        public OrderSummary createFromParcel(Parcel source) {
            return new OrderSummary(source);
        }

        @Override
        public OrderSummary[] newArray(int size) {
            return new OrderSummary[size];
        }
    };
}

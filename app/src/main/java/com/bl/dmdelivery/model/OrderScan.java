package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tHundorn_j on 22/2/2561.
 */

public class OrderScan implements Parcelable {
    private Integer item;
    private String DeliveryDate;
    private String TruckNo;
    private String InvoiceNo;
    private Integer TotalCanton;
    private Integer TotalScan;
    private Integer TotalNotScan;
    private Integer CartonDiff;
    private Integer BagsDiff;

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

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

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public Integer getTotalCanton() {
        return TotalCanton;
    }

    public void setTotalCanton(Integer totalCanton) {
        TotalCanton = totalCanton;
    }

    public Integer getTotalScan() {
        return TotalScan;
    }

    public void setTotalScan(Integer totalScan) {
        TotalScan = totalScan;
    }

    public Integer getTotalNotScan() {
        return TotalNotScan;
    }

    public void setTotalNotScan(Integer totalNotScan) {
        TotalNotScan = totalNotScan;
    }

    public Integer getCartonDiff() {
        return CartonDiff;
    }

    public void setCartonDiff(Integer cartonDiff) {
        CartonDiff = cartonDiff;
    }

    public Integer getBagsDiff() {
        return BagsDiff;
    }

    public void setBagsDiff(Integer bagsDiff) {
        BagsDiff = bagsDiff;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.item);
        dest.writeString(this.DeliveryDate);
        dest.writeString(this.TruckNo);
        dest.writeString(this.InvoiceNo);
        dest.writeValue(this.TotalCanton);
        dest.writeValue(this.TotalScan);
        dest.writeValue(this.TotalNotScan);
        dest.writeValue(this.CartonDiff);
        dest.writeValue(this.BagsDiff);
    }

    public OrderScan() {
    }

    protected OrderScan(Parcel in) {
        this.item =  (Integer) in.readValue(Integer.class.getClassLoader());
        this.DeliveryDate = in.readString();
        this.TruckNo = in.readString();
        this.InvoiceNo = in.readString();
        this.TotalCanton = (Integer) in.readValue(Integer.class.getClassLoader());
        this.TotalScan = (Integer) in.readValue(Integer.class.getClassLoader());
        this.TotalNotScan = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CartonDiff = (Integer) in.readValue(Integer.class.getClassLoader());
        this.BagsDiff = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<OrderScan> CREATOR = new Creator<OrderScan>() {
        @Override
        public OrderScan createFromParcel(Parcel source) {
            return new OrderScan(source);
        }

        @Override
        public OrderScan[] newArray(int size) {
            return new OrderScan[size];
        }
    };
}

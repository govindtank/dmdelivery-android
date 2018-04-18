package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by thundorn_j on 29/3/2561.
 */

public class OrderSource implements Parcelable {

    private String dc1;
    private String truck_no1;
    private String norder;
    private String trans_no1;
    private String lat;
    private String lng;

    public String getDc1() {
        return dc1;
    }

    public void setDc1(String dc1) {
        this.dc1 = dc1;
    }

    public String getTruck_no1() {
        return truck_no1;
    }

    public void setTruck_no1(String truck_no1) {
        this.truck_no1 = truck_no1;
    }

    public String getNorder() {
        return norder;
    }

    public void setNorder(String norder) {
        this.norder = norder;
    }

    public String getTrans_no1() {
        return trans_no1;
    }

    public void setTrans_no1(String trans_no1) {
        this.trans_no1 = trans_no1;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public OrderSource() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dc1);
        dest.writeString(this.truck_no1);
        dest.writeString(this.norder);
        dest.writeString(this.trans_no1);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    protected OrderSource(Parcel in) {
        this.dc1 = in.readString();
        this.truck_no1 = in.readString();
        this.norder = in.readString();
        this.trans_no1 = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public static final Creator<OrderSource> CREATOR = new Creator<OrderSource>() {
        @Override
        public OrderSource createFromParcel(Parcel source) {
            return new OrderSource(source);
        }

        @Override
        public OrderSource[] newArray(int size) {
            return new OrderSource[size];
        }
    };
}

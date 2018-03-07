package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thorkait_t on 06/03/2561.
 */

public class OrdersChangeList implements Parcelable {

    private  String inv;
    private  String mslidname;
    private  String itemsqty;

    public class Column{
        public static final String inv = "inv";
        public static final String mslidname = "mslidname";
        public static final String itemsqty ="itemsqty";
    }

    public String getInv() {
        return inv;
    }

    public void setInv(String inv) {
        this.inv = inv;
    }

    public String getMslidname() {
        return mslidname;
    }

    public void setMslidname(String mslidname) {
        this.mslidname = mslidname;
    }

    public String getItemsqty() {
        return itemsqty;
    }

    public void setItemsqty(String itemsqty) {
        this.itemsqty = itemsqty;
    }


    public  OrdersChangeList(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.inv);
        dest.writeString(this.mslidname);
        dest.writeString(this.itemsqty);
    }

    protected OrdersChangeList(Parcel in) {
        this.inv = in.readString();
        this.mslidname = in.readString();
        this.itemsqty = in.readString();
    }

    public static final Creator<OrdersChangeList> CREATOR = new Creator<OrdersChangeList>() {
        @Override
        public OrdersChangeList createFromParcel(Parcel source) {
            return new OrdersChangeList(source);
        }

        @Override
        public OrdersChangeList[] newArray(int size) {
            return new OrdersChangeList[size];
        }
    };
}

package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thorkait_t on 06/03/2561.
 */

public class OrdersChangeList implements Parcelable {

    private  String TransNo;
    private  String rep_code;
    private  String rep_name;
    private  String qty;
    private  String unpack_items;

    public class Column{
        public static final String TransNo = "TransNo";
        public static final String rep_code = "rep_code";
        public static final String rep_name ="rep_name";
        public static final String unpack_items = "unpack_items";
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String TransNo) {
        this.TransNo = TransNo;
    }

    public String getRep_code() {
        return rep_code;
    }

    public void setRep_code(String rep_code) {
        this.rep_code = rep_code;
    }

    public String getRep_name() {
        return rep_name;
    }

    public void setRep_name(String rep_name) {
        this.rep_name = rep_name;
    }

    public String getQty() { return qty; }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnpack_items() {
        return unpack_items;
    }

    public void setUnpack_items(String unpack_items) {
        this.unpack_items = unpack_items;
    }


    public  OrdersChangeList(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TransNo);
        dest.writeString(this.rep_code);
        dest.writeString(this.rep_name);
        dest.writeString(this.qty);
        dest.writeString(this.unpack_items);
    }

    protected OrdersChangeList(Parcel in) {
        this.TransNo = in.readString();
        this.rep_code = in.readString();
        this.rep_name = in.readString();
        this.qty = in.readString();
        this.unpack_items = in.readString();
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

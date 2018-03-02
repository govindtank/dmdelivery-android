package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class Unpack implements Parcelable{

    private String transno;
    private String unpack_code;
    private String unpack_desc;
    private String unpack_qty;
    private String unpack_image;

    public class Column{
        public static final String transno = "transno";
        public static final String unpack_code = "unpack_code";
        public static final String unpack_desc ="unpack_desc";
        public static final String unpack_qty = "unpack_qty";
        public static final String unpack_image = "unpack_image";
    }

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public String getUnpack_code() {
        return unpack_code;
    }

    public void setUnpack_code(String unpack_code) {
        this.unpack_code = unpack_code;
    }

    public String getUnpack_desc() {
        return unpack_desc;
    }

    public void setUnpack_desc(String unpack_desc) {
        this.unpack_desc = unpack_desc;
    }



    public String getUnpack_image() {
        return unpack_image;
    }

    public void setUnpack_image(String unpack_image) {
        this.unpack_image = unpack_image;
    }

    public String getUnpack_qty() {
        return unpack_qty;
    }

    public void setUnpack_qty(String unpack_qty) {
        this.unpack_qty = unpack_qty;
    }


    public Unpack() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transno);
        dest.writeString(this.unpack_code);
        dest.writeString(this.unpack_desc);
        dest.writeValue(this.unpack_qty);
        dest.writeString(this.unpack_image);
    }

    protected Unpack(Parcel in) {
        this.transno = in.readString();
        this.unpack_code = in.readString();
        this.unpack_desc = in.readString();
        this.unpack_qty = in.readString();
        this.unpack_image = in.readString();
    }

    public static final Creator<Unpack> CREATOR = new Creator<Unpack>() {
        @Override
        public Unpack createFromParcel(Parcel source) {
            return new Unpack(source);
        }

        @Override
        public Unpack[] newArray(int size) {
            return new Unpack[size];
        }
    };
}

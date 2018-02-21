package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class Unpack implements Parcelable{
    private String fscode;
    private String fsname;
    private String fsunit;

    public String getFscode() {
        return fscode;
    }

    public void setFscode(String fscode) {
        this.fscode = fscode;
    }

    public String getFsname() {
        return fsname;
    }

    public void setFsname(String fsname) {
        this.fsname = fsname;
    }

    public String getFsunit() {
        return fsunit;
    }

    public void setFsunit(String fsunit) {
        this.fsunit = fsunit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fscode);
        dest.writeString(this.fsname);
        dest.writeString(this.fsunit);
    }

    public Unpack() {
    }

    protected Unpack(Parcel in) {
        this.fscode = in.readString();
        this.fsname = in.readString();
        this.fsunit = in.readString();
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

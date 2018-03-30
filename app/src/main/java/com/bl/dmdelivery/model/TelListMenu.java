package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tHundorn_j on 28/2/2561.
 */

public class TelListMenu implements Parcelable {
    private String textname;
    private String isselect;

    public class Column{

        public static final String textname = "textname";
        public static final String isselect ="isselect";

    }

    public String getTextname() {
        return textname;
    }

    public void setTextname(String textname) {
        this.textname = textname;
    }



    public String getIsselect() {
        return isselect;
    }

    public void setIsselect(String isselect) {
        this.isselect = isselect;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.textname);
        dest.writeString(this.isselect);
    }

    public TelListMenu() {
    }

    protected TelListMenu(Parcel in) {
        this.textname = in.readString();
        this.isselect = in.readString();
    }

    public static final Creator<TelListMenu> CREATOR = new Creator<TelListMenu>() {
        @Override
        public TelListMenu createFromParcel(Parcel source) {
            return new TelListMenu(source);
        }

        @Override
        public TelListMenu[] newArray(int size) {
            return new TelListMenu[size];
        }
    };
}

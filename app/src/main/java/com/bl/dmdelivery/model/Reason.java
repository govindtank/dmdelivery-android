package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tHundorn_j on 28/2/2561.
 */

public class Reason implements Parcelable {
    private String reason_code;
    private String reason_desc;
    private String reason_type;

    public class Column{

        public static final String reason_code = "reason_code";
        public static final String reason_desc = "reason_desc";
        public static final String reason_type ="reason_type";

    }

    public String getReason_code() {
        return reason_code;
    }

    public void setReason_code(String reason_code) {
        this.reason_code = reason_code;
    }

    public String getReason_desc() {
        return reason_desc;
    }

    public void setReason_desc(String reason_desc) {
        this.reason_desc = reason_desc;
    }

    public String getReason_type() {
        return reason_type;
    }

    public void setReason_type(String reason_type) {
        this.reason_type = reason_type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reason_code);
        dest.writeString(this.reason_desc);
        dest.writeString(this.reason_type);
    }

    public Reason() {
    }

    protected Reason(Parcel in) {
        this.reason_code = in.readString();
        this.reason_desc = in.readString();
        this.reason_type = in.readString();
    }

    public static final Creator<Reason> CREATOR = new Creator<Reason>() {
        @Override
        public Reason createFromParcel(Parcel source) {
            return new Reason(source);
        }

        @Override
        public Reason[] newArray(int size) {
            return new Reason[size];
        }
    };
}

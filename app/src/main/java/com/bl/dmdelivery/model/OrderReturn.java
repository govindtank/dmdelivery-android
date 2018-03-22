package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tHundorn_j on 28/2/2561.
 */

public class OrderReturn implements Parcelable {
    private String ou_code;
    private String return_no;
    private String return_code;
    private String return_type;
    private String reftrans_no;
    private String reftrans_year;
    private String rep_code;
    private String rep_seq;
    private String rep_name;
    private String return_seq;
    private String fs_code;
    private String fs_desc;
    private String return_unit_real;
    private String return_unit;
    private String return_remark;
    private String return_status;
    private String reason_code;
    private String return_note;

    public class Column{

        public static final String ou_code = "ou_code";
        public static final String return_no = "return_no";
        public static final String return_code ="return_code";
        public static final String return_type = "return_type";
        public static final String reftrans_no = "reftrans_no";
        public static final String reftrans_year = "reftrans_year";
        public static final String rep_code = "rep_code";
        public static final String rep_seq = "rep_seq";
        public static final String rep_name = "rep_name";
        public static final String return_seq = "return_seq";
        public static final String fs_code = "fs_code";
        public static final String fs_desc = "fs_desc";
        public static final String return_unit_real = "return_unit_real";
        public static final String return_unit = "return_unit";
        public static final String return_remark = "return_remark";
        public static final String return_status = "return_status";
        public static final String reason_code = "reason_code";
        public static final String return_note = "return_note";
    }

    public String getOu_code() {
        return ou_code;
    }

    public void setOu_code(String ou_code) {
        this.ou_code = ou_code;
    }

    public String getReturn_no() {
        return return_no;
    }

    public void setReturn_no(String return_no) {
        this.return_no = return_no;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public String getReftrans_no() {
        return reftrans_no;
    }

    public void setReftrans_no(String reftrans_no) {
        this.reftrans_no = reftrans_no;
    }

    public String getReftrans_year() {
        return reftrans_year;
    }

    public void setReftrans_year(String reftrans_year) {
        this.reftrans_year = reftrans_year;
    }

    public String getRep_code() {
        return rep_code;
    }

    public void setRep_code(String rep_code) {
        this.rep_code = rep_code;
    }

    public String getRep_seq() {
        return rep_seq;
    }

    public void setRep_seq(String rep_seq) {
        this.rep_seq = rep_seq;
    }

    public String getRep_name() {
        return rep_name;
    }

    public void setRep_name(String rep_name) {
        this.rep_name = rep_name;
    }

    public String getReturn_seq() {
        return return_seq;
    }

    public void setReturn_seq(String return_seq) {
        this.return_seq = return_seq;
    }

    public String getFs_code() {
        return fs_code;
    }

    public void setFs_code(String fs_code) {
        this.fs_code = fs_code;
    }

    public String getFs_desc() {
        return fs_desc;
    }

    public void setFs_desc(String fs_desc) {
        this.fs_desc = fs_desc;
    }

    public String getReturn_unit_real() { return return_unit_real; }

    public void setReturn_unit_real(String return_unit_real) { this.return_unit_real = return_unit_real; }

    public String getReturn_unit() {
        return return_unit;
    }

    public void setReturn_unit(String return_unit) {
        this.return_unit = return_unit;
    }

    public String getReturn_remark() {
        return return_remark;
    }

    public void setReturn_remark(String return_remark) {
        this.return_remark = return_remark;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getReason_code() {
        return reason_code;
    }

    public void setReason_code(String reason_code) {
        this.reason_code = reason_code;
    }


    public String getReturn_note() {
        return return_note;
    }

    public void setReturn_note(String return_note) {
        this.return_note = return_note;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ou_code);
        dest.writeString(this.return_no);
        dest.writeString(this.return_code);
        dest.writeString(this.return_type);
        dest.writeString(this.reftrans_no);
        dest.writeString(this.reftrans_year);
        dest.writeString(this.rep_code);
        dest.writeString(this.rep_seq);
        dest.writeString(this.rep_name);
        dest.writeString(this.return_seq);
        dest.writeString(this.fs_code);
        dest.writeString(this.fs_desc);
        dest.writeString(this.return_unit_real);
        dest.writeString(this.return_unit);
        dest.writeString(this.return_remark);
        dest.writeString(this.return_status);
        dest.writeString(this.reason_code);
        dest.writeString(this.return_note);
    }

    public OrderReturn() {
    }

    protected OrderReturn(Parcel in) {
        this.ou_code = in.readString();
        this.return_no = in.readString();
        this.return_code = in.readString();
        this.return_type = in.readString();
        this.reftrans_no = in.readString();
        this.reftrans_year = in.readString();
        this.rep_code = in.readString();
        this.rep_seq = in.readString();
        this.rep_name = in.readString();
        this.return_seq = in.readString();
        this.fs_code = in.readString();
        this.fs_desc = in.readString();
        this.return_unit_real = in.readString();
        this.return_unit = in.readString();
        this.return_remark = in.readString();
        this.return_status = in.readString();
        this.reason_code = in.readString();
        this.return_note = in.readString();
    }

    public static final Creator<OrderReturn> CREATOR = new Creator<OrderReturn>() {
        @Override
        public OrderReturn createFromParcel(Parcel source) {
            return new OrderReturn(source);
        }

        @Override
        public OrderReturn[] newArray(int size) {
            return new OrderReturn[size];
        }
    };
}

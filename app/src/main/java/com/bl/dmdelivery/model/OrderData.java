package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nitisak_p on 08/03/2560.
 */
public class OrderData implements Parcelable {

    private String repcode;
    private String repname;
    private String repaddr1;
    private String repaddr2;
    private String repaddr3;
    private String repaddr4;
    private String repphone;
    private String repmobile;
    private String invoiceno;
    private String carton;
    private String bag;
    private String otrstatus;
    private String otrdeliverystatus;
    private String select;



    public OrderData(){}

    // Constructor
    public OrderData(String repcode, String repname, String repaddr1, String repaddr2, String repaddr3, String repaddr4
            , String repphone, String repmobile, String invoiceno, String carton, String bag, String otrstatus, String otrdeliverystatus, String select){
        this.repcode = repcode;
        this.repname = repname;
        this.repaddr1 = repaddr1;
        this.repaddr2 = repaddr2;
        this.repaddr3 = repaddr3;
        this.repaddr4 = repaddr4;
        this.repphone = repphone;
        this.repmobile = repmobile;
        this.invoiceno = invoiceno;
        this.carton = carton;
        this.bag = bag;
        this.otrstatus = otrstatus;
        this.otrdeliverystatus = otrdeliverystatus;
        this.select = select;


    }

    // Parcelling part
    public OrderData(Parcel in){
        String[] data = new String[14];

        in.readStringArray(data);
        this.repcode = data[0];
        this.repname = data[1];
        this.repaddr1 = data[2];
        this.repaddr2 = data[3];
        this.repaddr3 = data[4];
        this.repaddr4 = data[5];
        this.repphone = data[6];
        this.repmobile = data[7];
        this.invoiceno = data[8];
        this.carton = data[9];
        this.bag = data[10];
        this.otrstatus = data[11];
        this.otrdeliverystatus = data[12];
        this.select = data[13];

    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.repcode,
                this.repname,this.repaddr1,this.repaddr2,this.repaddr3,this.repaddr4,this.repphone,
                this.repmobile,this.invoiceno,this.carton,this.bag,this.otrstatus,this.otrdeliverystatus,this.select});
    }

    public String getRepcode() {
        return repcode;
    }
    public void setRepcode(String repcode) {
        this.repcode = repcode;
    }


    public String getRepname() {
        return repname;
    }
    public void setRepname(String repname) {
        this.repname = repname;
    }


    public String getRepaddr1() {
        return repaddr1;
    }
    public void setRepaddr1(String repaddr1) {
        this.repaddr1 = repaddr1;
    }


    public String getRepaddr2() {
        return repaddr2;
    }
    public void setRepaddr2(String repaddr2) {
        this.repaddr2 = repaddr2;
    }


    public String getRepaddr3() {
        return repaddr3;
    }
    public void setRepaddr3(String repaddr3) {
        this.repaddr3 = repaddr3;
    }


    public String getRepaddr4() {
        return repaddr4;
    }
    public void setRepaddr4(String repaddr4) {
        this.repaddr4 = repaddr4;
    }


    public String getRepphone() {
        return repphone;
    }
    public void setRepphone(String repphone) {
        this.repphone = repphone;
    }

    public String getRepmobile() {
        return repmobile;
    }
    public void setRepmobile(String repmobile) {
        this.repmobile = repmobile;
    }


    public String getInvoiceno() {
        return invoiceno;
    }
    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }


    public String getCarton() {
        return carton;
    }
    public void setCarton(String carton) {
        this.carton = carton;
    }


    public String getBag() {
        return bag;
    }
    public void setBag(String bag) {
        this.bag = bag;
    }


    public String getOtrstatus() {
        return otrstatus;
    }
    public void setOtrstatus(String otrstatus) {
        this.otrstatus = otrstatus;
    }

    public String getOtrdeliverystatus() {
        return otrdeliverystatus;
    }
    public void setOtrdeliverystatus(String otrdeliverystatus) {
        this.otrdeliverystatus = otrdeliverystatus;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public static final Creator CREATOR = new Creator() {
        public OrderData createFromParcel(Parcel in) {
            return new OrderData(in);
        }

        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };
}

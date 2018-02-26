package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nitisak_p on 08/03/2560.
 */
public class OrderData implements Parcelable {

    private Integer Itemno;
    private String invoiceno;
    private String invoice_type;
    private String campaign;
    private String carton;
    private String repcode;
    private String repname;
    private String address;
    private String mobilemsl;
    private String mobiledsm;
    private String inv_return;

    public Integer getItemno() {
        return Itemno;
    }

    public void setItemno(Integer itemno) {
        Itemno = itemno;
    }


    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type) {
        this.invoice_type = invoice_type;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getCarton() {
        return carton;
    }

    public void setCarton(String carton) {
        this.carton = carton;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilemsl() {
        return mobilemsl;
    }

    public void setMobilemsl(String mobilemsl) {
        this.mobilemsl = mobilemsl;
    }

    public String getMobiledsm() {
        return mobiledsm;
    }

    public void setMobiledsm(String mobiledsm) {
        this.mobiledsm = mobiledsm;
    }

    public String getInv_return() {
        return inv_return;
    }

    public void setInv_return(String inv_return) {
        this.inv_return = inv_return;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.Itemno);
        dest.writeString(this.invoiceno);
        dest.writeString(this.invoice_type);
        dest.writeString(this.campaign);
        dest.writeString(this.carton);
        dest.writeString(this.repcode);
        dest.writeString(this.repname);
        dest.writeString(this.address);
        dest.writeString(this.mobilemsl);
        dest.writeString(this.mobiledsm);
        dest.writeString(this.inv_return);
    }

    public OrderData() {
    }

    protected OrderData(Parcel in) {
        this.Itemno = (Integer) in.readValue(Integer.class.getClassLoader());
        this.invoiceno = in.readString();
        this.invoice_type = in.readString();
        this.campaign = in.readString();
        this.carton = in.readString();
        this.repcode = in.readString();
        this.repname = in.readString();
        this.address = in.readString();
        this.mobilemsl = in.readString();
        this.mobiledsm = in.readString();
        this.inv_return = in.readString();
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel source) {
            return new OrderData(source);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };
}

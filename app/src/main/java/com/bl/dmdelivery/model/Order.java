package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * Created by tHundorn_j on 28/2/2561.
 */

public class Order implements Parcelable {
    private Integer Itemno;
    private String Oucode;
    private String Year;
    private String Group;
    private String TransNo;
    private String Transdate;
    private String rep_seq;
    private String rep_code;
    private String rep_name;
    private String rep_nickname;
    private String address1;
    private String address2;
    private String tumbon;
    private String amphur;
    private String province;
    private String postal;
    private String rep_telno;
    private String dsm_name;
    private String dsm_telno;
    private String loc_code;
    private String trans_campaign;
    private String ord_campaign;
    private String ord_type;
    private String del_type;
    private String ord_flag_status;
    private String return_flag;
    private String unpack_items;
    private String order_flag_desc;
    private String delivery_desc;
    private String ordertype_desc;
    private String cont_desc;
    private String delivery_date;
    private String truckNo;
    private Integer id;
    private String delivery_status;
    private String isselect;
    private String cre_date;
    private String fullpathimage;
    private String lat;
    private String lon;
    private String signature_timestamp;
    private String reason_code;
    private String reason_note;
    private String send_status;
    private String mobile_serial;
    private String mobile_emei;
    private String mobile_battery;
    private String user_define1;
    private String user_define2;
    private String user_define3;
    private String user_define4;
    private String user_define5;
    private String return_order;
    private String return_status;
    private String sendtoserver_timestamp;

    public class Column{

        public static final String ID = BaseColumns._ID;
        public static final String Itemno = "Itemno";
        public static final String Oucode = "Oucode";
        public static final String Trans_Year = "Trans_Year";
        public static final String Trans_Group ="Trans_Group";
        public static final String TransNo = "TransNo";
        public static final String Transdate = "Transdate";
        public static final String rep_seq = "rep_seq";
        public static final String rep_code = "rep_code";
        public static final String rep_name = "rep_name";
        public static final String rep_nickname = "rep_nickname";
        public static final String address1 = "address1";
        public static final String address2 = "address2";
        public static final String tumbon = "tumbon";
        public static final String amphur = "amphur";
        public static final String province = "province";
        public static final String postal = "postal";
        public static final String rep_telno = "rep_telno";
        public static final String dsm_name = "dsm_name";
        public static final String dsm_telno = "dsm_telno";
        public static final String loc_code = "loc_code";
        public static final String trans_campaign = "trans_campaign";
        public static final String ord_campaign = "ord_campaign";
        public static final String ord_type = "ord_type";
        public static final String del_type = "del_type";
        public static final String ord_flag_status = "ord_flag_status";
        public static final String return_flag = "return_flag";
        public static final String unpack_items = "unpack_items";
        public static final String order_flag_desc = "order_flag_desc";
        public static final String delivery_desc = "delivery_desc";
        public static final String ordertype_desc = "ordertype_desc";
        public static final String cont_desc = "cont_desc";
        public static final String delivery_date = "delivery_date";
        public static final String truckNo = "truckNo";
        public static final String delivery_status = "delivery_status";
        public static final String isselect = "isselect";
        public static final String cre_date = "cre_date";
        public static final String fullpathimage = "fullpathimage";
        public static final String lat = "lat";
        public static final String lon = "lon";
        public static final String signature_timestamp = "signature_timestamp";
        public static final String reason_code = "reason_code";
        public static final String reason_note = "reason_note";
        public static final String send_status = "send_status";
        public static final String mobile_serial = "mobile_serial";
        public static final String mobile_emei = "mobile_emei";
        public static final String mobile_battery = "mobile_battery";
        public static final String user_define1 = "user_define1";
        public static final String user_define2 = "user_define2";
        public static final String user_define3 = "user_define3";
        public static final String user_define4 = "user_define4";
        public static final String user_define5 = "user_define5";
        public static final String return_order = "return_order";
        public static final String return_status = "return_status";
        public static final String sendtoserver_timestamp = "sendtoserver_timestamp";

    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemno() {
        return Itemno;
    }

    public void setItemno(Integer itemno) {
        Itemno = itemno;
    }

    public String getOucode() {
        return Oucode;
    }

    public void setOucode(String oucode) {
        Oucode = oucode;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        TransNo = transNo;
    }

    public String getTransdate() {
        return Transdate;
    }

    public void setTransdate(String transdate) {
        Transdate = transdate;
    }

    public String getRep_seq() {
        return rep_seq;
    }

    public void setRep_seq(String rep_seq) {
        this.rep_seq = rep_seq;
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

    public String getRep_nickname() {
        return rep_nickname;
    }

    public void setRep_nickname(String rep_nickname) {
        this.rep_nickname = rep_nickname;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getTumbon() {
        return tumbon;
    }

    public void setTumbon(String tumbon) {
        this.tumbon = tumbon;
    }

    public String getAmphur() {
        return amphur;
    }

    public void setAmphur(String amphur) {
        this.amphur = amphur;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getRep_telno() {
        return rep_telno;
    }

    public void setRep_telno(String rep_telno) {
        this.rep_telno = rep_telno;
    }

    public String getDsm_name() {
        return dsm_name;
    }

    public void setDsm_name(String dsm_name) {
        this.dsm_name = dsm_name;
    }

    public String getDsm_telno() {
        return dsm_telno;
    }

    public void setDsm_telno(String dsm_telno) {
        this.dsm_telno = dsm_telno;
    }

    public String getLoc_code() {
        return loc_code;
    }

    public void setLoc_code(String loc_code) {
        this.loc_code = loc_code;
    }

    public String getTrans_campaign() {
        return trans_campaign;
    }

    public void setTrans_campaign(String trans_campaign) {
        this.trans_campaign = trans_campaign;
    }

    public String getOrd_campaign() {
        return ord_campaign;
    }

    public void setOrd_campaign(String ord_campaign) {
        this.ord_campaign = ord_campaign;
    }

    public String getOrd_type() {
        return ord_type;
    }

    public void setOrd_type(String ord_type) {
        this.ord_type = ord_type;
    }

    public String getDel_type() {
        return del_type;
    }

    public void setDel_type(String del_type) {
        this.del_type = del_type;
    }

    public String getOrd_flag_status() {
        return ord_flag_status;
    }

    public void setOrd_flag_status(String ord_flag_status) {
        this.ord_flag_status = ord_flag_status;
    }

    public String getReturn_flag() {
        return return_flag;
    }

    public void setReturn_flag(String return_flag) {
        this.return_flag = return_flag;
    }

    public String getUnpack_items() {
        return unpack_items;
    }

    public void setUnpack_items(String unpack_items) {
        this.unpack_items = unpack_items;
    }

    public String getOrder_flag_desc() {
        return order_flag_desc;
    }

    public void setOrder_flag_desc(String order_flag_desc) {
        this.order_flag_desc = order_flag_desc;
    }

    public String getDelivery_desc() {
        return delivery_desc;
    }

    public void setDelivery_desc(String delivery_desc) {
        this.delivery_desc = delivery_desc;
    }

    public String getOrdertype_desc() {
        return ordertype_desc;
    }

    public void setOrdertype_desc(String ordertype_desc) {
        this.ordertype_desc = ordertype_desc;
    }

    public String getCont_desc() {
        return cont_desc;
    }

    public void setCont_desc(String cont_desc) {
        this.cont_desc = cont_desc;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTruckNo() {
        return truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public String getIsselect() {
        return isselect;
    }

    public void setIsselect(String isselect) {
        this.isselect = isselect;
    }

    public String getCre_date() {
        return cre_date;
    }

    public void setCre_date(String cre_date) {
        this.cre_date = cre_date;
    }

    public String getFullpathimage() {
        return fullpathimage;
    }

    public void setFullpathimage(String fullpathimage) {
        this.fullpathimage = fullpathimage;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getSignature_timestamp() {
        return signature_timestamp;
    }

    public void setSignature_timestamp(String signature_timestamp) {
        this.signature_timestamp = signature_timestamp;
    }

    public String getReason_code() {
        return reason_code;
    }

    public void setReason_code(String reason_code) {
        this.reason_code = reason_code;
    }

    public String getReason_note() {
        return reason_note;
    }

    public void setReason_note(String reason_note) {
        this.reason_note = reason_note;
    }

    public String getSend_status() {
        return send_status;
    }

    public void setSend_status(String send_status) {
        this.send_status = send_status;
    }

    public String getMobile_serial() {
        return mobile_serial;
    }

    public void setMobile_serial(String mobile_serial) {
        this.mobile_serial = mobile_serial;
    }

    public String getMobile_emei() {
        return mobile_emei;
    }

    public void setMobile_emei(String mobile_emei) {
        this.mobile_emei = mobile_emei;
    }

    public String getMobile_battery() {
        return mobile_battery;
    }

    public void setMobile_battery(String mobile_battery) {
        this.mobile_battery = mobile_battery;
    }

    public String getUser_define1() {
        return user_define1;
    }

    public void setUser_define1(String user_define1) {
        this.user_define1 = user_define1;
    }

    public String getUser_define2() {
        return user_define2;
    }

    public void setUser_define2(String user_define2) {
        this.user_define2 = user_define2;
    }

    public String getUser_define3() {
        return user_define3;
    }

    public void setUser_define3(String user_define3) {
        this.user_define3 = user_define3;
    }

    public String getUser_define4() {
        return user_define4;
    }

    public void setUser_define4(String user_define4) {
        this.user_define4 = user_define4;
    }

    public String getUser_define5() {
        return user_define5;
    }

    public void setUser_define5(String user_define5) {
        this.user_define5 = user_define5;
    }


    public String getReturn_order() {
        return return_order;
    }

    public void setReturn_order(String return_order) {
        this.return_order = return_order;
    }


    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getSendtoserver_timestamp() {
        return sendtoserver_timestamp;
    }

    public void setSendtoserver_timestamp(String sendtoserver_timestamp) {
        this.sendtoserver_timestamp = sendtoserver_timestamp;
    }




    public Order() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.Itemno);
        dest.writeString(this.Oucode);
        dest.writeString(this.Year);
        dest.writeString(this.Group);
        dest.writeString(this.TransNo);
        dest.writeString(this.Transdate);
        dest.writeString(this.rep_seq);
        dest.writeString(this.rep_code);
        dest.writeString(this.rep_name);
        dest.writeString(this.rep_nickname);
        dest.writeString(this.address1);
        dest.writeString(this.address2);
        dest.writeString(this.tumbon);
        dest.writeString(this.amphur);
        dest.writeString(this.province);
        dest.writeString(this.postal);
        dest.writeString(this.rep_telno);
        dest.writeString(this.dsm_name);
        dest.writeString(this.dsm_telno);
        dest.writeString(this.loc_code);
        dest.writeString(this.trans_campaign);
        dest.writeString(this.ord_campaign);
        dest.writeString(this.ord_type);
        dest.writeString(this.del_type);
        dest.writeString(this.ord_flag_status);
        dest.writeString(this.return_flag);
        dest.writeString(this.unpack_items);
        dest.writeString(this.order_flag_desc);
        dest.writeString(this.delivery_desc);
        dest.writeString(this.ordertype_desc);
        dest.writeString(this.cont_desc);
        dest.writeString(this.delivery_date);
        dest.writeString(this.truckNo);
        dest.writeValue(this.id);
        dest.writeString(this.delivery_status);
        dest.writeString(this.isselect);
        dest.writeString(this.cre_date);
        dest.writeString(this.fullpathimage);
        dest.writeString(this.lat);
        dest.writeString(this.lon);
        dest.writeString(this.signature_timestamp);
        dest.writeString(this.reason_code);
        dest.writeString(this.reason_note);
        dest.writeString(this.send_status);
        dest.writeString(this.mobile_serial);
        dest.writeString(this.mobile_emei);
        dest.writeString(this.mobile_battery);
        dest.writeString(this.user_define1);
        dest.writeString(this.user_define2);
        dest.writeString(this.user_define3);
        dest.writeString(this.user_define4);
        dest.writeString(this.user_define5);
        dest.writeString(this.return_order);
        dest.writeString(this.return_status);
        dest.writeString(this.sendtoserver_timestamp);
    }

    protected Order(Parcel in) {
        this.Itemno = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Oucode = in.readString();
        this.Year = in.readString();
        this.Group = in.readString();
        this.TransNo = in.readString();
        this.Transdate = in.readString();
        this.rep_seq = in.readString();
        this.rep_code = in.readString();
        this.rep_name = in.readString();
        this.rep_nickname = in.readString();
        this.address1 = in.readString();
        this.address2 = in.readString();
        this.tumbon = in.readString();
        this.amphur = in.readString();
        this.province = in.readString();
        this.postal = in.readString();
        this.rep_telno = in.readString();
        this.dsm_name = in.readString();
        this.dsm_telno = in.readString();
        this.loc_code = in.readString();
        this.trans_campaign = in.readString();
        this.ord_campaign = in.readString();
        this.ord_type = in.readString();
        this.del_type = in.readString();
        this.ord_flag_status = in.readString();
        this.return_flag = in.readString();
        this.unpack_items = in.readString();
        this.order_flag_desc = in.readString();
        this.delivery_desc = in.readString();
        this.ordertype_desc = in.readString();
        this.cont_desc = in.readString();
        this.delivery_date = in.readString();
        this.truckNo = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.delivery_status = in.readString();
        this.isselect = in.readString();
        this.cre_date = in.readString();
        this.fullpathimage = in.readString();
        this.lat = in.readString();
        this.lon = in.readString();
        this.signature_timestamp = in.readString();
        this.reason_code = in.readString();
        this.reason_note = in.readString();
        this.send_status = in.readString();
        this.mobile_serial = in.readString();
        this.mobile_emei = in.readString();
        this.mobile_battery = in.readString();
        this.user_define1 = in.readString();
        this.user_define2 = in.readString();
        this.user_define3 = in.readString();
        this.user_define4 = in.readString();
        this.user_define5 = in.readString();
        this.return_order = in.readString();
        this.return_status = in.readString();
        this.sendtoserver_timestamp = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}

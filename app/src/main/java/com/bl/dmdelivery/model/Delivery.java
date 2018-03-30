package com.bl.dmdelivery.model;

import java.util.List;

/**
 * Created by thundorn_j on 30/3/2561.
 */

public class Delivery {

    /**
     * truck_no : sample string 1
     * delivery_date : sample string 2
     * trans_year : sample string 3
     * trans_group : sample string 4
     * trans_no : sample string 5
     * route_seq : sample string 6
     * rep_code : sample string 7
     * lat : sample string 8
     * lon : sample string 9
     * signature_timestamp : sample string 10
     * reason_code : sample string 11
     * reason_note : sample string 12
     * delivery_status : sample string 13
     * mobile_serial : sample string 14
     * mobile_emei : sample string 15
     * mobile_battery : sample string 16
     * app_version : sample string 17
     * image_signature : sample string 18
     * user_define1 : sample string 19
     * user_define2 : sample string 20
     * user_define3 : sample string 21
     * user_define4 : sample string 22
     * user_define5 : sample string 23
     * return_order : [{"track_no":"sample string 1","delivery_date":"sample string 2","return_no":"sample string 3","rep_code":"sample string 4","return_status":"sample string 5","return_reason":"sample string 6","return_notes":"sample string 7","lat":"sample string 8","lon":"sample string 9","signature_timestamp":"sample string 10","signature_image":"sample string 11","return_item":[{"fscode":"sample string 1","fsdesc":"sample string 2","return_request":"sample string 3","return_actual":"sample string 4"},{"fscode":"sample string 1","fsdesc":"sample string 2","return_request":"sample string 3","return_actual":"sample string 4"}]},{"track_no":"sample string 1","delivery_date":"sample string 2","return_no":"sample string 3","rep_code":"sample string 4","return_status":"sample string 5","return_reason":"sample string 6","return_notes":"sample string 7","lat":"sample string 8","lon":"sample string 9","signature_timestamp":"sample string 10","signature_image":"sample string 11","return_item":[{"fscode":"sample string 1","fsdesc":"sample string 2","return_request":"sample string 3","return_actual":"sample string 4"},{"fscode":"sample string 1","fsdesc":"sample string 2","return_request":"sample string 3","return_actual":"sample string 4"}]}]
     */

    private String truck_no;
    private String delivery_date;
    private String trans_year;
    private String trans_group;
    private String trans_no;
    private String route_seq;
    private String rep_code;
    private String lat;
    private String lon;
    private String signature_timestamp;
    private String reason_code;
    private String reason_note;
    private String delivery_status;
    private String mobile_serial;
    private String mobile_emei;
    private String mobile_battery;
    private String app_version;
    private String image_signature;
    private String user_define1;
    private String user_define2;
    private String user_define3;
    private String user_define4;
    private String user_define5;
    private List<ReturnOrder> return_order;

    public String getTruck_no() {
        return truck_no;
    }

    public void setTruck_no(String truck_no) {
        this.truck_no = truck_no;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTrans_year() {
        return trans_year;
    }

    public void setTrans_year(String trans_year) {
        this.trans_year = trans_year;
    }

    public String getTrans_group() {
        return trans_group;
    }

    public void setTrans_group(String trans_group) {
        this.trans_group = trans_group;
    }

    public String getTrans_no() {
        return trans_no;
    }

    public void setTrans_no(String trans_no) {
        this.trans_no = trans_no;
    }

    public String getRoute_seq() {
        return route_seq;
    }

    public void setRoute_seq(String route_seq) {
        this.route_seq = route_seq;
    }

    public String getRep_code() {
        return rep_code;
    }

    public void setRep_code(String rep_code) {
        this.rep_code = rep_code;
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

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
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

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getImage_signature() {
        return image_signature;
    }

    public void setImage_signature(String image_signature) {
        this.image_signature = image_signature;
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

    public List<ReturnOrder> getReturn_order() {
        return return_order;
    }

    public void setReturn_order(List<ReturnOrder> return_order) {
        this.return_order = return_order;
    }

    public static class ReturnOrder {
        /**
         * track_no : sample string 1
         * delivery_date : sample string 2
         * return_no : sample string 3
         * rep_code : sample string 4
         * return_status : sample string 5
         * return_reason : sample string 6
         * return_notes : sample string 7
         * lat : sample string 8
         * lon : sample string 9
         * signature_timestamp : sample string 10
         * signature_image : sample string 11
         * return_item : [{"fscode":"sample string 1","fsdesc":"sample string 2","return_request":"sample string 3","return_actual":"sample string 4"},{"fscode":"sample string 1","fsdesc":"sample string 2","return_request":"sample string 3","return_actual":"sample string 4"}]
         */

        private String track_no;
        private String delivery_date;
        private String return_no;
        private String rep_code;
        private String return_status;
        private String return_reason;
        private String return_notes;
        private String lat;
        private String lon;
        private String signature_timestamp;
        private String signature_image;
        private List<ReturnItem> return_item;

        public String getTrack_no() {
            return track_no;
        }

        public void setTrack_no(String track_no) {
            this.track_no = track_no;
        }

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
        }

        public String getReturn_no() {
            return return_no;
        }

        public void setReturn_no(String return_no) {
            this.return_no = return_no;
        }

        public String getRep_code() {
            return rep_code;
        }

        public void setRep_code(String rep_code) {
            this.rep_code = rep_code;
        }

        public String getReturn_status() {
            return return_status;
        }

        public void setReturn_status(String return_status) {
            this.return_status = return_status;
        }

        public String getReturn_reason() {
            return return_reason;
        }

        public void setReturn_reason(String return_reason) {
            this.return_reason = return_reason;
        }

        public String getReturn_notes() {
            return return_notes;
        }

        public void setReturn_notes(String return_notes) {
            this.return_notes = return_notes;
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

        public String getSignature_image() {
            return signature_image;
        }

        public void setSignature_image(String signature_image) {
            this.signature_image = signature_image;
        }

        public List<ReturnItem> getReturn_item() {
            return return_item;
        }

        public void setReturn_item(List<ReturnItem> return_item) {
            this.return_item = return_item;
        }

        public static class ReturnItem {
            /**
             * fscode : sample string 1
             * fsdesc : sample string 2
             * return_request : sample string 3
             * return_actual : sample string 4
             */

            private String fscode;
            private String fsdesc;
            private String return_request;
            private String return_actual;

            public String getFscode() {
                return fscode;
            }

            public void setFscode(String fscode) {
                this.fscode = fscode;
            }

            public String getFsdesc() {
                return fsdesc;
            }

            public void setFsdesc(String fsdesc) {
                this.fsdesc = fsdesc;
            }

            public String getReturn_request() {
                return return_request;
            }

            public void setReturn_request(String return_request) {
                this.return_request = return_request;
            }

            public String getReturn_actual() {
                return return_actual;
            }

            public void setReturn_actual(String return_actual) {
                this.return_actual = return_actual;
            }
        }
    }
}

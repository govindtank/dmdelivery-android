package com.bl.dmdelivery.model;

import java.util.List;

/**
 * Created by thundorn_j on 10/4/2561.
 */

public class PhoneNumberResponse {

    /**
     * Phonenumber : [{"contact":"sample string 1","phone":"sample string 2"},{"contact":"sample string 1","phone":"sample string 2"}]
     * ResponseCode : sample string 1
     * ResponseMessage : sample string 2
     */

    private String ResponseCode;
    private String ResponseMessage;
    private List<Phonenumber> Phonenumber;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public List<Phonenumber> getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(List<Phonenumber> Phonenumber) {
        this.Phonenumber = Phonenumber;
    }

    public static class Phonenumber {
        /**
         * contact : sample string 1
         * phone : sample string 2
         */

        private String contact;
        private String phone;

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}

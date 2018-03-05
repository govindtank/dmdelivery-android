package com.bl.dmdelivery.model;

import java.util.ArrayList;

/**
 * Created by thundorn_j on 4/3/2561.
 */

public class BookingResponse {
    private ArrayList<OrderScan> CheckOrderScan;
    private String ResponseCode;
    private String ResponseMessage;

    public ArrayList<OrderScan> getCheckOrderScan() {
        return CheckOrderScan;
    }

    public void setCheckOrderScan(ArrayList<OrderScan> checkOrderScan) {
        CheckOrderScan = checkOrderScan;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}

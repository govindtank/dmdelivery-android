package com.bl.dmdelivery.model;

import java.util.ArrayList;

/**
 * Created by thundorn_j on 4/3/2561.
 */

public class BookingResponse {
    private ArrayList<OrderSummary> OrderSummary;
    private String ResponseCode;
    private String ResponseMessage;

    public ArrayList<com.bl.dmdelivery.model.OrderSummary> getOrderSummary() {
        return OrderSummary;
    }

    public void setOrderSummary(ArrayList<com.bl.dmdelivery.model.OrderSummary> orderSummary) {
        OrderSummary = orderSummary;
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

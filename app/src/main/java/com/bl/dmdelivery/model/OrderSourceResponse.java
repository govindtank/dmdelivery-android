package com.bl.dmdelivery.model;

import java.util.ArrayList;

/**
 * Created by thundorn_j on 29/3/2561.
 */

public class OrderSourceResponse {
    private ArrayList<OrderSource> OrderSource;
    private String ResponseCode;
    private String ResponseMessage;

    public ArrayList<com.bl.dmdelivery.model.OrderSource> getOrderSource() {
        return OrderSource;
    }

    public void setOrderSource(ArrayList<com.bl.dmdelivery.model.OrderSource> orderSource) {
        OrderSource = orderSource;
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

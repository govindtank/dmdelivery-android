package com.bl.dmdelivery.model;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 28/2/2561.
 */

public class LoadOrderResponse {
    private ArrayList<Order> Order;
    private ArrayList<Unpack> Unpack;
    private ArrayList<OrderReturn> OrderReturn;
    private ArrayList<Reason> Reason;
    private String ResponseCode;
    private String ResponseMessage;

    public ArrayList<com.bl.dmdelivery.model.Order> getOrder() {
        return Order;
    }

    public void setOrder(ArrayList<com.bl.dmdelivery.model.Order> order) {
        Order = order;
    }

    public ArrayList<com.bl.dmdelivery.model.Unpack> getUnpack() {
        return Unpack;
    }

    public void setUnpack(ArrayList<com.bl.dmdelivery.model.Unpack> unpack) {
        Unpack = unpack;
    }

    public ArrayList<com.bl.dmdelivery.model.OrderReturn> getOrderReturn() {
        return OrderReturn;
    }

    public void setOrderReturn(ArrayList<com.bl.dmdelivery.model.OrderReturn> orderReturn) {
        OrderReturn = orderReturn;
    }

    public ArrayList<com.bl.dmdelivery.model.Reason> getReason() {
        return Reason;
    }

    public void setReason(ArrayList<com.bl.dmdelivery.model.Reason> reason) {
        Reason = reason;
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

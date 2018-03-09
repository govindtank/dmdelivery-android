package com.bl.dmdelivery.model;

import java.util.ArrayList;

/**
 * Created by thundorn_j on 9/3/2561.
 */

public class LoadUnpackResponse {
    private ArrayList<Unpack> Unpack;
    private String ResponseCode;
    private String ResponseMessage;

    public ArrayList<com.bl.dmdelivery.model.Unpack> getUnpack() {
        return Unpack;
    }

    public void setUnpack(ArrayList<com.bl.dmdelivery.model.Unpack> unpack) {
        Unpack = unpack;
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

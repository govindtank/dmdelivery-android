package com.bl.dmdelivery.model;

/**
 * Created by thundorn_j on 8/3/2561.
 */

public class BaseResponse {
    private String ResponseCode;
    private String ResponseMessage;

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

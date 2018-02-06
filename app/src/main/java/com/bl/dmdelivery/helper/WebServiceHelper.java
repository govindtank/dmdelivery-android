package com.bl.dmdelivery.helper;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class WebServiceHelper {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String getServiceAPI (String url) {
        try {
            // 1. connect server with okHttp
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            // 3. transport request to server
            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }

    public String postServiceAPI (String url, String json) {
        try {
            // 1. connect server with okHttp
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

//            json = "{\"quoteId\":\"93\",\"token\":\"4lbvy3che0mgrh479c2c3ekvyx94jlqx\",\"customerId\":\"2\",\"cartItem\":[{\"sku\":\"41134\",\"quote_id\":\"93\",\"qty\":\"3\"}]}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }

    public String putServiceAPI (String url, String json) {
        try {
            // 1. connect server with okHttp
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

//            json = "{\"quoteId\":\"93\",\"token\":\"4lbvy3che0mgrh479c2c3ekvyx94jlqx\",\"customerId\":\"2\",\"cartItem\":[{\"sku\":\"41134\",\"quote_id\":\"93\",\"qty\":\"3\",\"item_id\":\"363\"}]}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteServiceAPI (String url, String json) {
        try {
            // 1. connect server with okHttp
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
                    .build();

            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }
}


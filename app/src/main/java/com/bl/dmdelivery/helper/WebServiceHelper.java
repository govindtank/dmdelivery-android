package com.bl.dmdelivery.helper;

import com.bl.dmdelivery.utility.TagUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;


public class WebServiceHelper {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String getServiceAPI (String url) {
        try {
            // 1. connect server with okHttp
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .authenticator(new Authenticator() {
                        @Override public Request authenticate(Route route, Response response) throws IOException {
                            if (response.request().header("Authorization") != null) {
                                return null; // Give up, we've already attempted to authenticate.
                            }

                            System.out.println("Authenticating for response: " + response);
                            System.out.println("Challenges: " + response.challenges());
                            String credential = Credentials.basic(TagUtils.WEBSERVICEUSER, TagUtils.WEBSERVICEPASS);
                            return response.request().newBuilder()
                                    .header("Authorization", credential)
                                    .build();
                        }
                    })
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
                    .authenticator(new Authenticator() {
                        @Override public Request authenticate(Route route, Response response) throws IOException {
                            if (response.request().header("Authorization") != null) {
                                return null; // Give up, we've already attempted to authenticate.
                            }

                            System.out.println("Authenticating for response: " + response);
                            System.out.println("Challenges: " + response.challenges());
                            String credential = Credentials.basic(TagUtils.WEBSERVICEUSER, TagUtils.WEBSERVICEPASS);
                            return response.request().newBuilder()
                                    .header("Authorization", credential)
                                    .build();
                        }
                    })
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
                    .authenticator(new Authenticator() {
                        @Override public Request authenticate(Route route, Response response) throws IOException {
                            if (response.request().header("Authorization") != null) {
                                return null; // Give up, we've already attempted to authenticate.
                            }

                            System.out.println("Authenticating for response: " + response);
                            System.out.println("Challenges: " + response.challenges());
                            String credential = Credentials.basic(TagUtils.WEBSERVICEUSER, TagUtils.WEBSERVICEPASS);
                            return response.request().newBuilder()
                                    .header("Authorization", credential)
                                    .build();
                        }
                    })
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
                    .authenticator(new Authenticator() {
                        @Override public Request authenticate(Route route, Response response) throws IOException {
                            if (response.request().header("Authorization") != null) {
                                return null; // Give up, we've already attempted to authenticate.
                            }

                            System.out.println("Authenticating for response: " + response);
                            System.out.println("Challenges: " + response.challenges());
                            String credential = Credentials.basic(TagUtils.WEBSERVICEUSER, TagUtils.WEBSERVICEPASS);
                            return response.request().newBuilder()
                                    .header("Authorization", credential)
                                    .build();
                        }
                    })
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


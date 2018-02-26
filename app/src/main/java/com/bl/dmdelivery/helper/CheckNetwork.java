package com.bl.dmdelivery.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nitisak_p on 1/12/2558.
 */
public class CheckNetwork {

    public  static String URL = "http://172.16.103.205/BLMwebservice/WSDMORDERTRACKING.asmx";

    public boolean isConnectionAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public boolean isWebserviceConnected(Context context)
    {
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected())
            {
                //Network is available but check if we can get access from the network.
                //URL url = new URL("http://distributioncenter.mistine.co.th:8206/DMAssignFirst/main.asmx");
                /*URL url = new URL("http://distributioncenter.mistine.co.th:8206/DMAssignFirst/Main_AssignFirstWs.asmx");


                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                String header = "Basic " + new String(android.util.Base64.encode("Administrator:supcsd".getBytes(), android.util.Base64.NO_WRAP));
                urlc.addRequestProperty("Authorization", header);

                if (urlc.getResponseCode() == 200)  //Successful response.
                {
                    return true;
                }
                else
                {
                    //Log.d("NO INTERNET", "NO INTERNET");
                    return false;
                }*/


                java.net.URL url = new URL(URL);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(2000); // Timeout 2 seconds.
                urlc.connect();

                if (urlc.getResponseCode() == 200)  //Successful response.
                {
                    return true;
                }
                else
                {
                    //Log.d("NO INTERNET", "NO INTERNET");
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}

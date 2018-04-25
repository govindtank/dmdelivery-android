package com.bl.dmdelivery.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.bl.dmdelivery.actvity.MainActivity;
import com.bl.dmdelivery.utility.TagUtils;

import java.lang.reflect.Method;

/**
 * Created by sd-m003 on 25/4/2018 AD.
 */

public class LogCall extends BroadcastReceiver {

    private Context context;
    private SimpleCursorAdapter adapter;
    private SQLiteDatabase db;
    private String sql;
    private Cursor cursor;
    private String name = "";
    private String number = "";
    private String matchName = "";
    private String truckNo = "";
    private String imeiNumber = "";
    private Cursor people;
    public static boolean allow_number = true;

    //private SharedPreferences sp;
    private SharedPreferences.Editor editor;

//    static {
//        System.loadLibrary("iconv");
//    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Log.d("DM", "Log call");
        //Log.d("DM", intent.getAction());
        this.context = context;
        //showToast("LogCall");

        //sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);




        try
        {
//            String action = intent.getAction();
//
//            if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//
//                showToast("ACTION_NEW_OUTGOING_CALL");
//            }
//            else
//            {
//                showToast("NO ACTION_NEW_OUTGOING_CALL");
//            }


//            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
//            {
//                showToast("NO ACTION_NEW_OUTGOING_CALL");
//            }else
//            {
//                showToast("ACTION_NEW_OUTGOING_CALL");
//            }

            if ( allow_number == false ) {
                //showToast("calllog found allow_number = false ");

                //showToast("ไม่สามารโทรออกเบอร์นี้ได้ครับ !!");

                disconnectPhoneItelephony(context);
                allow_number = true;
                return;
            }

            SharedPreferences sp = context.getSharedPreferences(TagUtils.DMDELIVERY_PREF,
                    Context.MODE_PRIVATE);

            truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
            imeiNumber = sp.getString(TagUtils.PREF_IMEI, "");

            if(!truckNo.equals(""))
            {



                String url = "";
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_RINGING)) {

                    // Phone number
                    String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                    // Ringing state
                    // This code will execute when the phone has an incoming call
                    Log.d("DM", "RINGING "+incomingNumber);
                    url = "http://fleet.mistine.co.th/call_log.php?sn=" + imeiNumber + "&truck_no=" + truckNo + "&caller_id=" +incomingNumber + "&state=RINGING";
                    new TheTask().execute(url);

                    //showToast("RINGING");




                } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_IDLE)
                        ) {

                    // This code will execute when the call is answered or disconnected
                    Log.d("DM", "HANGUP");
                    url = "http://fleet.mistine.co.th/call_log.php?sn=" + imeiNumber + "&truck_no=" + truckNo + "&caller_id=" + "&state=HANGUP";
                    new TheTask().execute(url);

                    //showToast("HANGUP");

                } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.d("DM", "TALK");
                    url = "http://fleet.mistine.co.th/call_log.php?sn=" + imeiNumber + "&truck_no=" + truckNo + "&caller_id=" + "&state=TALK";
                    new TheTask().execute(url);
                    //disconnectPhoneItelephony(context);

                    //showToast("TALK");

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }






    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneItelephony(Context context)
    {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}

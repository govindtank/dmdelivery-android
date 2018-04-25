package com.bl.dmdelivery.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.bl.dmdelivery.R;
import com.bl.dmdelivery.utility.TagUtils;

import java.lang.reflect.Method;

/**
 * Created by sd-m003 on 25/4/2018 AD.
 */

public class BlockCallReceiver extends BroadcastReceiver {

    private Context context;
    private SimpleCursorAdapter adapter;
    private SQLiteDatabase db;
    private String sql;
    private Cursor cursor;
    private String name = "";
    private String number = "";
    private String matchName = "";
    private Cursor people;
    private String truckNo = "";
    private String imeiNumber = "";

//    static {
//        System.loadLibrary("iconv");
//    }


//    //@Override
//    public void xonReceive(Context context, Intent intent)
//    {
//        // If, the received action is not a type of "Phone_State", ignore it
//        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
//            return;
//
//            // Else, try to do some action
//        else
//        {
//            // Fetch the number of incoming call
//            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//
//            // Check, whether this is a member of "Black listed" phone numbers stored in the database
//            //if(MainActivity.blockList.contains(new Blacklist(number)))
//            //{
//            // If yes, invoke the method
//            //disconnectPhoneItelephony(context);
//            return;
//            //}
//        }
//    }

    @Override
    public void onReceive(Context context, Intent intent) {

//
//        final String oldNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//        this.setResultData("0123456789");
//        final String newNumber = this.getResultData();
//        String msg = "Intercepted outgoing call. Old number " + oldNumber + ", new number " + newNumber;
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

        String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.d("DM","Call " + outgoingNumber);


        SharedPreferences sp = context.getSharedPreferences(TagUtils.DMDELIVERY_PREF,
                Context.MODE_PRIVATE);

        truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
        imeiNumber = sp.getString(TagUtils.PREF_IMEI, "");

        String url = "http://fleet.mistine.co.th/call_log.php?sn=" + imeiNumber + "&truck_no=" + truckNo+ "&caller_id=" + outgoingNumber + "&state=CALLING";
        new TheTask().execute(url);

        if (outgoingNumber.substring(0,2).equals("66")) {
            outgoingNumber = "0" + outgoingNumber.substring(2);
        }




        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        people = context.getContentResolver().query(uri, projection, null, null, null);

        matchName = "";
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String mark = Character.toString((char) 10) + ":";

        people.moveToFirst();
        do {

            name   =  people.getString(indexName);
            number =  people.getString(indexNumber);

            if (outgoingNumber.equals(number)) {
                matchName = name;
                //showToast("Contact : " + name);
                break;
            }

        } while (people.moveToNext());

        if (!("*,1".contains(outgoingNumber.substring(0,1))) && !(matchName.contains(mark)) ) {

            //number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            //showToast("ไม่สามารโทรออกเบอร์นี้ได้ครับ !!");
            //showToast("");

            Toast.makeText(context, context.getResources().getString(R.string.txt_text_not_call), Toast.LENGTH_LONG).show();

            //disconnectPhoneItelephony(context);
            LogCall.allow_number = false;
            //showToast("allow_numer = false");
            setResultData(null);

        } else {

            LogCall.allow_number = true;
            //showToast("allow_numer = true");

        }


        //Toast.makeText(context, outgoingNumber+" - "+truckNo+" - "+imeiNumber, Toast.LENGTH_LONG).show();

        //LogCall.allow_number = false;

//        if(!contactExists(outgoingNumber))
//        {
//            LogCall.allow_number = false;
//        }else
//        {
//            LogCall.allow_number = true;
//        }

        //disconnectPhoneItelephony(context);


    }

    public boolean contactExists(String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }

    public void xonReceive(Context context, Intent intent)  {

        this.context = context;
        //showToast("outgoint detected");

        String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.d("DM","Call " + outgoingNumber);


        SharedPreferences sp = context.getSharedPreferences(TagUtils.DMDELIVERY_PREF,
                Context.MODE_PRIVATE);

        truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
        imeiNumber = sp.getString(TagUtils.PREF_IMEI, "");


        String url = "http://fleet.mistine.co.th/call_log.php?sn=" + imeiNumber + "&truck_no=" + truckNo+ "&caller_id=" + outgoingNumber + "&state=CALLING";
        new TheTask().execute(url);


        if (outgoingNumber.substring(0,2).equals("66")) {
            outgoingNumber = "0" + outgoingNumber.substring(2);
        }

        showToast("allow_numer = true");

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        people = context.getContentResolver().query(uri, projection, null, null, null);


        matchName = "";
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String mark = Character.toString((char) 10) + ":";
        //mark = "*";

        people.moveToFirst();
        do {

            name   =  people.getString(indexName);
            number =  people.getString(indexNumber);

            if (outgoingNumber.equals(number)) {
                matchName = name;
                //showToast("Contact : " + name);
                break;
            }

        } while (people.moveToNext());

//        helper = new NotesHelper(context);
//        db = helper.getReadableDatabase();
//        sql = "SELECT * FROM " + TABLE_NAME + " WHERE rep_tel LIKE ? OR dist_tel LIKE ?";
//        cursor = db.rawQuery(sql, new String[] {"%"+outgoingNumber+"%", "%"+outgoingNumber+"%"});


//        if ((cursor.getCount() == 0) && !("*,1".contains(outgoingNumber.substring(0,1))) && !(matchName.contains(mark)) ) {

        if (!(matchName.contains(mark)) ) {


            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            //showToast("‰¡Ë “¡“√∂‚∑√ÕÕ°‰¥È§√—∫ !!");
            disconnectPhoneItelephony(context);
            //LogCall.allow_number = false;
            showToast("allow_numer = false");
            setResultData(null);

        } else {
            //LogCall.allow_number = true;
            showToast("allow_numer = true");

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

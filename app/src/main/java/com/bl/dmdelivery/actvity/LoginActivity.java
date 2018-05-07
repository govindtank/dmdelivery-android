package com.bl.dmdelivery.actvity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.TheTask;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.Response;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import android.os.AsyncTask;

public class LoginActivity extends AppCompatActivity {

    private TextView mTxtLogotext,mmTxtMsg,mmTxtTitle;
    private EditText mEdtTroukno;
    private Button mBtnlogin,mmBtnClose;
    private ImageView mmImvTitle;
    private static TextView mTxtDate;
    private String serverUrl;
    private ACProgressFlower mProgressDialog;
    private Response resResponse;
    private String truckNo = "";
    private String imei = "";
    private String deliveryDate = "";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static String imeiNumber = "";
    public static int screen_width;
    public static int screen_height;


//    private String mInputPath = Environment.getExternalStorageDirectory().toString() + "/DMSLIP/";
//    private String mOutputPath = Environment.getExternalStorageDirectory().toString() + "/DMPROCESSED/";
//
//    private String mInputPathReturn = Environment.getExternalStorageDirectory().toString() + "/DMSLIPRETURN/";
//    private String mInputPathProcessReturn = Environment.getExternalStorageDirectory().toString() + "/DMRETURNPROCESSED/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

        checkRuntimePermission();

        bindWidget();

        setWidgetControl();


    }

    private void bindWidget() {

        try {

            String truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
            //String deliveryDate = sp.getString(TagUtils.PREF_DELIVERY_DATE, "");


            mTxtLogotext = (TextView) findViewById(R.id.txtlogo);
            mEdtTroukno = (EditText) findViewById(R.id.edttruck);
            mBtnlogin = (Button) findViewById(R.id.btnlogin);
            mTxtLogotext.setText(getResources().getString(R.string.app_name_dtl));
            mTxtDate = (TextView) findViewById(R.id.txtdate);

//            if(deliveryDate.equals(""))
//            {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                String currentDate = sdf.format(new Date());
//                mTxtDate.setText(currentDate);
//            }
//            else
//            {
//                mTxtDate.setText(deliveryDate);
//            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());
            mTxtDate.setText(currentDate);


            mEdtTroukno.setText(truckNo);




        } catch (SecurityException e) {
            showMsgDialog(e.toString());
        }
    }

    public void showMsgDialog(String msg)
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_message, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);

//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mmTxtMsg.setTypeface(tf);
//        mmTxtTitle.setTypeface(tf);
//        mmBtnClose.setTypeface(tf);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText(getResources().getString(R.string.app_name));
        mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }

    private void setWidgetControl()
    {

        try {



            mTxtDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //showDialog();

                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            });



            mBtnlogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    //hideSoftKeyboard(LoginActivity.this);

                    //checkLogin();



                    configGPSLog();

                    clearSlip();

                    clearReturnSlip();

                    //processLog();

                    //getCallDetails();

                    //saveLogin();

                    setTruck();




                    //serverUrl = TagUtils.WEBSERVICEURI + "IsTruckActiveState/"+mEdtTroukno;
                    //new loginAsynTask().execute(serverUrl);

//                    finish();
//                    Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
//                    startActivity(myIntent);
                    //overridePendingTransition(0,0);



                    //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

        } catch (Exception e) {

            showMsgDialog(e.toString());
        }


    }

    private void setTruck() {

        try {

            mBtnlogin.setEnabled(false);

            hideSoftKeyboard(LoginActivity.this);

            TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber = telephony.getDeviceId();

            if(!mEdtTroukno.getText().toString().trim().equals(""))
            {
                deliveryDate = mTxtDate.getText().toString().trim().toUpperCase();
                truckNo = mEdtTroukno.getText().toString().trim().toUpperCase();


                editor = sp.edit();
                editor.putString(TagUtils.PREF_DELIVERY_DATE, deliveryDate);
                editor.putString(TagUtils.PREF_LOGIN_TRUCK_NO, truckNo);
                editor.putString(TagUtils.PREF_IMEI, imeiNumber);
                editor.apply();

                mBtnlogin.setEnabled(true);

                finish();
                Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(myIntent);

            }else
            {
                mBtnlogin.setEnabled(true);
                showMsgDialog(getResources().getString(R.string.error_truck_no_empty));
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    private void clearSlip() {

        try {

            File dir;

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
            Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));

            cal.add(Calendar.DATE, -7); /* save last 7 days */
            cal2.add(Calendar.DATE, -120); /* start from last 120 days */


            java.util.Date currentLocalTime = cal.getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyMMdd");
            date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String localTime = date.format(currentLocalTime);
            Log.d("DM","local time " + localTime);


            java.util.Date currentLocalTime2 = cal2.getTime();
            SimpleDateFormat date2 = new SimpleDateFormat("yyyMMdd");
            date2.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String localTime2 = date2.format(currentLocalTime2);
            Log.d("DM","local time 2 " + localTime2);
            Integer s = 1;
            while ((!localTime2.equals(localTime))) {

                cal2.add(Calendar.DATE, 1);
                currentLocalTime2 = cal2.getTime();
                localTime2 = date2.format(currentLocalTime2);

                dir = new File(Environment.getExternalStorageDirectory()+"/DMPROCESSED/" + localTime2);
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(dir, children[i]).delete();
                    }
                    dir.delete();

                }
                s = s + 1;
                Log.d("DM","Delete Folder  " + Environment.getExternalStorageDirectory()+"/DMPROCESSED/" + localTime2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearReturnSlip() {

        try {

            File dir;

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
            Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));

            cal.add(Calendar.DATE, -7); /* save last 7 days */
            cal2.add(Calendar.DATE, -120); /* start from last 120 days */


            java.util.Date currentLocalTime = cal.getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyMMdd");
            date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String localTime = date.format(currentLocalTime);
            Log.d("DM","local time " + localTime);


            java.util.Date currentLocalTime2 = cal2.getTime();
            SimpleDateFormat date2 = new SimpleDateFormat("yyyMMdd");
            date2.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String localTime2 = date2.format(currentLocalTime2);
            Log.d("DM","local time 2 " + localTime2);
            Integer s = 1;
            while ((!localTime2.equals(localTime))) {

                cal2.add(Calendar.DATE, 1);
                currentLocalTime2 = cal2.getTime();
                localTime2 = date2.format(currentLocalTime2);

                dir = new File(Environment.getExternalStorageDirectory()+"/DMRETURNPROCESSED/" + localTime2);
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(dir, children[i]).delete();
                    }
                    dir.delete();

                }
                s = s + 1;
                Log.d("DM","Delete Folder  " + Environment.getExternalStorageDirectory()+"/DMRETURNPROCESSED/" + localTime2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void configGPSLog() {

        try {



            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screen_width = size.x;
            screen_height = size.y;


            String configFileName = "gpslogger.properties";

                int j=64;
                char c=(char)j;

                j=37;
                char p=(char)j;


                String fcontent = 	"startonbootup=true%n" +
                        "log_gpx=false%n" +
                        "log_kml=false%n" +
                        "log_plain_text=true%n" +
                        "keep_fix=true%n"	+
                        "time_before_logging=30%n"	+
                        "accuracy_before_logging=20%n" +
                        "distance_before_logging=%n"	+
                        "retry_time=30%n" +
                        "absolute_timeout=30%n"	+
                        "new_file_creation=onceaday%n" +
                        "new_file_prefix_serial=true%n" 	+
                        "autosend_enabled=true%n" +
                        "autosend_frequency_whenstoppressed=true%n" +
                        "autosend_sendzip=false%n"	+
                        "autoftp_enabled=false%n" +
                        "autoftp_server=gpslog.mistine.co.th%n" +
                        "autoftp_directory=%n"	+
                        "autoftp_username=ck%n" +
                        "autoftp_password=supcsd%n"	+
                        "autosend_frequency_minutes=10%n"	+
                        "log_customurl_enabled=true%n"	+
                        "log_customurl_url=http://fleet.mistine.co.th/log.php?sn="+c+"SER&acc="+c+"ACC&dir="+c+"DIR&lat="+c+"LAT&lng="+c+"LON&time="+c+"TIME&speed="+c+"SPD&batt="+c+"BATT%n";


                fcontent = String.format(fcontent);
                fcontent = fcontent.replace(c, p);

                String strConfigDirectory = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.mendhak.gpslogger/files/";
                File configDirectory = new File(strConfigDirectory);

                    configDirectory.mkdirs();

            try {

                File file = new File(strConfigDirectory + configFileName );
                if (!file.exists()) {

                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(fcontent);
                bw.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public void processLog () {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        java.util.Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat("yyy-MM-dd");
        date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String curDate = date.format(currentLocalTime).replace("-", "");

        String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.mendhak.gpslogger/files/";

        File f = new File(path);
        File file[] = f.listFiles();
        String fileName = "";
        Integer i = 0;
        for (i=0; i < file.length; i++){
            fileName = file[i].getName();
            if (!fileName.contains(curDate) && (fileName.contains("txt"))) {

                uploadGpsLog(fileName);

            }
            //Toast.makeText(getApplicationContext(),"file[" + i + "] = " + fileName, Toast.LENGTH_SHORT).show();
        }


    }

    public void uploadGpsLog (String logFileName) {


        FileInputStream inputStream;
        FileOutputStream outputStream;

        String Uri = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.mendhak.gpslogger/files/" + logFileName;

        File file = new File(Uri);
        if (!file.exists()) {
            return;
        }

        try {
            inputStream = new FileInputStream(Uri);
            File mFile = new File(getCacheDir(), logFileName);
            outputStream = new FileOutputStream(mFile);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            outputStream.flush();
            outputStream.close();

//            UploadTask task = new UploadTask(getApplicationContext(), imageFile, logFileName);
//            task.execute("http://fleet.mistine.co.th/get_file.php");

            String charset = Charset.defaultCharset().displayName();
            String boundary = Long.toHexString(System.currentTimeMillis());
            String strResult = "";

            URL url = new URL("http://fleet.mistine.co.th/get_file.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            writeMultipart(boundary, charset, bos, false,mFile);
            byte[] extra = bos.toByteArray();
            int contentLength = extra.length;
            contentLength += mFile.length();

            con.setFixedLengthStreamingMode(contentLength);

            OutputStream out = con.getOutputStream();
            writeMultipart(boundary, charset, out, true,mFile);

            strResult = readStream(con.getInputStream());



        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(getApplicationContext()," Error !" +  e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext()," Error !" +  e.getMessage(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext()," Send File Error !" +  logFileName, Toast.LENGTH_LONG).show();
        }


    }

    private void writeMultipart(String boundary, String charset,
                                OutputStream output, boolean writeContent,File mFile) throws IOException {

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(output,
                    Charset.forName(charset)), 8192);

            writer.write("--" + boundary);
            writer.write("\r\n");
            writer.write("Content-Disposition: form-data; name=\"myfile\"; filename=\""
                    + mFile.getName() + "\"");
            writer.write("\r\n");
            writer.write("Content-Type: "
                    + URLConnection.guessContentTypeFromName(
                    mFile.getName()));
            writer.write("\r\n");
            writer.write("Content-Transfer-Encoding: binary");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.flush();

            if (writeContent) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFile);
                    byte[] buffer = new byte[1024];
                    for (int len = 0; (len = fis.read(buffer)) > 0;) {
                        output.write(buffer, 0, len);
                    }
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            writer.write("\r\n");
            writer.flush();

            writer.write("--" + boundary + "--");
            writer.write("\r\n");
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);

        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);

        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            sb.append("\n----------------------------------");
        }

        managedCursor.close();

        String fcontent = sb.toString();
        String callLogFileName = "call_log" + imeiNumber  + ".txt";
        String strCallLogDirectory = Environment.getExternalStorageDirectory().toString() + "/CALL_LOG/";
        File callLogDirectory = new File(strCallLogDirectory);

        try {


            try {
                callLogDirectory.mkdirs();
            } catch (Exception e) {

            }
            File file = new File(strCallLogDirectory + callLogFileName );
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();
            Log.d("DM","Create call log file Sucess " + fcontent);
            uploadCallLog(callLogFileName);

        } catch (IOException e) {
            Log.d("DM", "ERROR ! Create call log file ");
            e.printStackTrace();
        }


    }


    public void uploadCallLog (String logFileName) {


        FileInputStream inputStream;
        FileOutputStream outputStream;

        String Uri = Environment.getExternalStorageDirectory().toString() + "/CALL_LOG/" + logFileName;

        File file = new File(Uri);
        if (!file.exists()) {
            return;
        }

        try {
            inputStream = new FileInputStream(Uri);
            File mFile = new File(getCacheDir(), logFileName);
            outputStream = new FileOutputStream(mFile);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            outputStream.flush();
            outputStream.close();
//            UploadTask task = new UploadTask(getApplicationContext(), imageFile, logFileName);
//            task.execute("http://fleet.mistine.co.th/get_call_log.php");


            String charset = Charset.defaultCharset().displayName();
            String boundary = Long.toHexString(System.currentTimeMillis());
            String strResult = "";

            URL url = new URL("http://fleet.mistine.co.th/get_call_log.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            writeMultipart(boundary, charset, bos, false,mFile);
            byte[] extra = bos.toByteArray();
            int contentLength = extra.length;
            contentLength += mFile.length();

            con.setFixedLengthStreamingMode(contentLength);

            OutputStream out = con.getOutputStream();
            writeMultipart(boundary, charset, out, true,mFile);

            strResult = readStream(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(getApplicationContext()," Error !" +  e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext()," Error !" +  e.getMessage(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext()," Send File Error !" +  logFileName, Toast.LENGTH_LONG).show();
        }


    }

    public void saveLogin () {

        try {

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
            java.util.Date currentLocalTime = cal.getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String time = date.format(currentLocalTime).replace(" ", "T");
            //***********************************************************************************
            String sn = Build.SERIAL.trim();


            String url = "http://fleet.mistine.co.th/login_version.php?truck_no=" + mEdtTroukno.getText().toString().trim() + "&sn=" + sn + "&lat=" + "0" + "&lng=" + "0" + "&time=" + time + "&version="+getResources().getString(R.string.app_version_slip);
            new TheTask().execute(url);


        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(getApplicationContext()," Error !" +  e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext()," Error !" +  e.getMessage(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext()," Send File Error !" +  logFileName, Toast.LENGTH_LONG).show();
        }
    }

    private void checkLogin() {

        try {

            mBtnlogin.setEnabled(false);

            hideSoftKeyboard(LoginActivity.this);

            if(!mEdtTroukno.getText().toString().trim().equals(""))
            {
                truckNo = mEdtTroukno.getText().toString().trim().toUpperCase();

                serverUrl = TagUtils.WEBSERVICEURI + "IsTruckActiveState/"+truckNo;
                new loginAsynTask().execute(serverUrl);

                mBtnlogin.setEnabled(true);

            }else
            {
                mBtnlogin.setEnabled(true);
                showMsgDialog(getResources().getString(R.string.error_truck_no_empty));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class loginAsynTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(LoginActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            PageResultHolder pageResultHolder = new PageResultHolder();
            //String xmlInput = params[0];
            try
            {

                String result = new WebServiceHelper().getServiceAPI(params[0]);
                Log.i("LoginResult", resResponse.body().toString());
                //pageResultHolder.content = result;
                return result;



            } catch (Exception e) {
                pageResultHolder.content = "Exception : CheckTruckNo";
                pageResultHolder.exception = e;
                return null;
            }

        }

        @Override
        protected void onPostExecute(final String result) {
            // TODO Auto-generated method stub

            try {

                if (result == null) {
                    mProgressDialog.dismiss();
                    showMsgDialog(result.toString());
                }
                else
                {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms

                            mProgressDialog.dismiss();


                            Gson gson = new Gson();

                            //String jsonData = resResponse.body().toString();
                            //JSONObject Jobject = new JSONObject(jsonData);

                            String json = gson.toJson(result);
                            Log.i("LoginResultGson",json);


//                            if(resResponse.isSuccessful())
//                            {
//                                Gson gson = new Gson();
//
//                                String jsonData = resResponse.body().toString();
//                                //JSONObject Jobject = new JSONObject(jsonData);
//
//                                String json = gson.toJson(jsonData);
//                                Log.i("LoginResultGson",json);
//                            }
//                            else
//                            {
//                                showMsgDialog(getResources().getString(R.string.error_webservice));
//                            }






                        }
                    }, 200);

                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });*/

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class PageResultHolder {
        private String content;
        private Exception exception;
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(),R.style.DatePickerDialog, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // do some stuff for example write on log and update TextField on activity
            populateSetDate(year, month, day);
        }

        public void populateSetDate(int year, int month, int day) {

            //String a = mTxtDate.getText().toString();

            //String b = formatDate(year,month,day).toString();

            mTxtDate.setText(formatDate(year, month, day));

          /*  if(!mTxtDate.getText().equals(formatDate(year,month,day)))
            {
                mTxtDate.setText(formatDate(year,month,day));
            }*/
        }
    }

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(date);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        hideSoftKeyboard(LoginActivity.this);

        return false;
    }
    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
//
//            List<String> permissions = new ArrayList<String>();
//
//            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.CAMERA);
//
//            }
//            if (!permissions.isEmpty()) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
//            }
//        }
//    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);


                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }*/

    // Begin - check runtimer permission
    private void checkRuntimePermission() {

        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_CALL_LOG,Manifest.permission.READ_CALL_LOG};

        Nammu.init(getApplicationContext());
        Nammu.askForPermission(this, permissions, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                //Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void permissionRefused() {
                finish();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




}

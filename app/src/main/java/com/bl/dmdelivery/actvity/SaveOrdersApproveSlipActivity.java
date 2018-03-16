package com.bl.dmdelivery.actvity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SaveOrdersApproveSlipActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,txtRepcode,txtInvNo,txtAddress1,txtAddress2,txtMslTel,txtgps;
    private Button mBtnBack,mmBtnOk,mmBtnClose,btnCancelGPS,btnCancel,btnGPS,btnSaveGPS,btnSave,btnNew,btnNote;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ArrayList<Reason> mDeliveryAcceptList = new ArrayList<Reason>();

    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

    private ListView lv;
//    private String[] sigDeliverylist;

    private ListView lvDeliveryAcceptList;
    private Intent myIntent=null;

    String mInputPath = Environment.getExternalStorageDirectory().toString() + "/SLIP/";
    String mOutputPath = Environment.getExternalStorageDirectory().toString() + "/PROCESSED/";

    public String batteryPercent = "0";

    private String returnflag = "";

    private File mFile;
    private String mFileName;

    private CanvasView customCanvas;

    //private final int REQUEST_PERMISSION_PHONE_STATE=1;

    Order mOrder;
    ArrayList<Order> order;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private String mLatitude = "0";
    private String mLongitude = "0";

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_approve_slip);

        try {
            bindWidget();

            sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

//            setDefaultFonts();

            setWidgetControl();
        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void bindWidget()
    {
        try{
            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            btnCancelGPS = (Button) findViewById(R.id.btnCancelGPS);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnGPS = (Button) findViewById(R.id.btnGPS);
            btnSaveGPS = (Button) findViewById(R.id.btnSaveGPS);
            btnSave = (Button) findViewById(R.id.btnSave);

            btnNew = (Button) findViewById(R.id.btnNew);
            btnNote = (Button) findViewById(R.id.btnNote);

            txtRepcode = (TextView) findViewById(R.id.txtRepcode);
            txtInvNo = (TextView) findViewById(R.id.txtInvNo);
            txtAddress1 = (TextView) findViewById(R.id.txtAddress1);
            txtAddress2 = (TextView) findViewById(R.id.txtAddress2);
            txtMslTel = (TextView) findViewById(R.id.txtMslTel);
            txtgps = (TextView) findViewById(R.id.txtgps);


            //mBtnNote = (Button) findViewById(R.id.btnNote);
            //mBtnSendGps = (Button) findViewById(R.id.btnSendGps);
            //mBtnSendGpsNo = (Button) findViewById(R.id.btnSendGpsNo);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_slip));

            customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

            //showPhoneStatePermission();

            File dirInput = new File (mInputPath);
            if (!dirInput.exists())
            {
                dirInput.mkdirs();
            }

            File dirOutput = new File (mOutputPath);
            if (!dirOutput.exists())
            {
                dirOutput.mkdirs();
            }

            getData();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            } else {
                Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
            }


        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void getData() {
        try{


            Intent intInv= getIntent();
            Bundle intent = intInv.getExtras();

            if(intent != null)
            {

                order = (ArrayList<Order>) intent.get("data");
                //mOrder = new Order();
                //mOrder =(Order)intent.get("data");


                if(order.size() != 0) {

                    txtRepcode.setText(setRepcodeFormat(order.get(0).getRep_code())+" "+order.get(0).getRep_name());
                    txtInvNo.setText(order.get(0).getTransNo());
                    txtAddress1.setText(order.get(0).getAddress1());
                    txtAddress2.setText(order.get(0).getAddress2()+" "+order.get(0).getPostal());
                    txtMslTel.setText("โทร. "+order.get(0).getRep_telno());
                    //txtgps.setText("GPS : 0,0");

                    returnflag = order.get(0).getRep_telno();

                }



            }



        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


//    private void setDefaultFonts() {
//        try {
////            Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
////            mTxtHeader.setTypeface(tf);
////            mBtnBack.setTypeface(tf);
//
//        } catch (Exception e) {
//            showMsgDialog(e.toString());
//        }
//    }

    private void setWidgetControl() {
        try{
            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

            btnCancelGPS.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {


                    for(int i=0; i<=order.size()-1; i++){
                        takeScreenshot(i);
                    }


                    if(returnflag.equals(""))
                    {
                        finish();
                    }
                    else {

                        finish();
                        myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                        myIntent.putExtra("data",order);
                        startActivity(myIntent);

                    }


                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {


                    for(int i=0; i<=order.size()-1; i++){
                        takeScreenshot(i);
                    }

                    if(returnflag.equals(""))
                    {
                        finish();
                    }
                    else {

                        finish();
                        myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                        myIntent.putExtra("data",order);
                        startActivity(myIntent);

                    }

                }
            });

            btnGPS.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {




                }
            });

            btnSaveGPS.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {


                    for(int i=0; i<=order.size()-1; i++){
                        takeScreenshot(i);
                    }

                    if(returnflag.equals(""))
                    {
                        finish();
                    }
                    else {

                        finish();
                        myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                        myIntent.putExtra("data",order);
                        startActivity(myIntent);

                    }

                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {


                    for(int i=0; i<=order.size()-1; i++){
                        takeScreenshot(i);
                    }

                    if(returnflag.equals(""))
                    {
                        finish();
                    }
                    else {

                        finish();
                        myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                        myIntent.putExtra("data",order);
                        startActivity(myIntent);

                    }
                    //overridePendingTransition(0,0);

                }
            });

            btnNew.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {


                    clearCanvas(view);

                }
            });

//            mBtnNote.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    showMsgReasonApproveSelectedSingleDialog();
//                }
//            });
//
//            mBtnSendGps.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    //ตรวจสอบก่อนว่ามีสินค้ารับคือนหรือไม่ ถ้ามีไปที่ใบรับคืน ถ้าไม่มีไปที่ หน้ารายการจัดส่งหลัก
//                    finish();
//
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                }
//            });
//
//            mBtnSendGpsNo.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    //ตรวจสอบก่อนว่ามีสินค้ารับคือนหรือไม่ ถ้ามีไปที่ใบรับคืน ถ้าไม่มีไปที่ หน้ารายการจัดส่งหลัก
//                    finish();
//
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                }
//            });

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

//    private void showPhoneStatePermission() {
//        int permissionCheck = ContextCompat.checkSelfPermission(
//                this, Manifest.permission.READ_PHONE_STATE);
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_PHONE_STATE)) {
//                showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_PHONE_STATE, REQUEST_PERMISSION_PHONE_STATE);
//            } else {
//                requestPermission(Manifest.permission.READ_PHONE_STATE, REQUEST_PERMISSION_PHONE_STATE);
//            }
//        } else {
//            Toast.makeText(SaveOrdersApproveSlipActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode,
//            String permissions[],
//            int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_PERMISSION_PHONE_STATE:
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(SaveOrdersApproveSlipActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(SaveOrdersApproveSlipActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
//                }
//        }
//    }
//
//    private void showExplanation(String title,
//                                 String message,
//                                 final String permission,
//                                 final int permissionRequestCode) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        requestPermission(permission, permissionRequestCode);
//                    }
//                });
//        builder.create().show();
//    }
//
//    private void requestPermission(String permissionName, int permissionRequestCode) {
//        ActivityCompat.requestPermissions(this,
//                new String[]{permissionName}, permissionRequestCode);
//    }

    private void takeScreenshot(int position) {


        //Date now = new Date();
        //android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);




        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        java.util.Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String localTime = date.format(currentLocalTime);
        localTime = localTime.replace(" ", "").replace("-", "");

        getBatteryPercentage();

        String sendResult = "3";




      /*  NumberFormat nf = NumberFormat.getInstance();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lx = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String latlng = nf.format(lx.getLatitude()) + "," + nf.format(lx.getLongitude());*/

        String latlng = mLatitude+","+mLongitude;

        String truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");


        String fileName= "V" + getResources().getString(R.string.app_version_slip) + "_" + Build.SERIAL.trim() +  "-" + truckNo + "-" + order.get(position).getTransNo() + "-" + localTime + "-" + latlng + "-" + getImeiNumber() + "-" + batteryPercent + "-" + sendResult + ".jpg";

        //String fileName= "V" + "0.3" + "_" + Build.SERIAL.trim() +  "-" + ogject.getTruck() + "-" + mTxtINV.getText().toString() + "-" + localTime + "-" + latlng + "-" + getImeiNumber() + "-" + batteryPercent + "-" + sendResult + ".jpg";

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/SLIP/" + fileName;


            View v = (View) findViewById(R.id.lnlSlip);

            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            v.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            mFileName = fileName;
            mFile =imageFile;

            //processSlip(mPath);

            //openScreenshot(imageFile);

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    public String getImeiNumber() {

        String imeiNumber ="";

        try {
            TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber = telephony.getDeviceId();

        } catch (SecurityException e)
        {
            showMsgDialog(e.toString());
        }

        return imeiNumber;
    }

    private void getBatteryPercentage() {


        try {


            Intent batteryIntent = getApplicationContext().registerReceiver(null,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int rawlevel = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int level = -1;
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
                batteryPercent = "P"+String.valueOf(level);
            }else
            {
                batteryPercent = "P"+String.valueOf(0);
            }


        } catch (Exception e)
        {
            showMsgDialog(e.toString());
        }

    }

    public void showMsgReasonApproveSelectedSingleDialog()
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersApproveSlipActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_save_orders_return_cancel, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("เหตุผล/หมายเหตุ");

        lvDeliveryAcceptList = (ListView) v.findViewById(R.id.lv);
        lvDeliveryAcceptList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        mDeliveryAcceptList.clear();
        mHelper = new DBHelper(getApplicationContext());
        mDeliveryAcceptList = mHelper.getReasonListForCondition("'DELIVERY_ACCEPT'");

        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 0; i < mDeliveryAcceptList.size();i++)
        {
            arrayList.add(mDeliveryAcceptList.get(i).getReason_code() + " " + mDeliveryAcceptList.get(i).getReason_desc());
        }

        if(arrayList.size() > 0)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,arrayList);
            lvDeliveryAcceptList.setAdapter(adapter);

            //ถ้ามีข้อมูลบน ListView ให้เลือกรายการแรกเสมอ
            lvDeliveryAcceptList.setItemChecked(0,true);
        }

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();

//                finish();
//
//                myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
//                startActivity(myIntent);
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

//        // Set item click listener
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String description = sigDeliverylist[position];
//                Toast.makeText(SaveOrdersApproveSlipActivity.this, description, Toast.LENGTH_SHORT).show();
//            }
//        });

        DialogBuilder.show();
    }

    public void showMsgDialog(String msg)
    {
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alert = DialogBuilder.create();
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_message, null, false);

        mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);

        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
        mTxtMsg.setTypeface(tf);
        mTxtMsg.setText(msg);

        DialogBuilder.setView(v);
        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        DialogBuilder.show();
    }

    private String setRepcodeFormat(String repcode) {


        try {

            String repcodeformat = "";

            if(repcode.length() == 10)

            {
                repcodeformat = repcode.substring(0, 4)+"-"+repcode.substring(4, 9)+"-"+repcode.substring(9, 10);
            }
            else
            {
                repcodeformat = repcode;
            }

            return repcodeformat;


        } catch (Exception e)
        {
            return repcode;
            //showMsgDialog(e.toString());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }
    }

    @Override
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
    }


    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*Method to get the enable location settings dialog*/
    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(SaveOrdersApproveSlipActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            /*Getting the location after aquiring location service*/
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                //_progressBar.setVisibility(View.INVISIBLE);
               // _latitude.setText("Latitude: " + String.valueOf(mLastLocation.getLatitude()));
               // _longitude.setText("Longitude: " + String.valueOf(mLastLocation.getLongitude()));

                mLatitude = String.valueOf(mLastLocation.getLatitude());
                mLongitude = String.valueOf(mLastLocation.getLongitude());

                txtgps.setText("Lat: " + String.valueOf(mLastLocation.getLatitude())+" "+"Long: " + String.valueOf(mLastLocation.getLongitude()));
            } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                * So we will get the current location.
                * For this we'll implement Location Listener and override onLocationChanged*/
                Log.i("Current Location", "No data for location found");

                txtgps.setText("No data for location found");

                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, SaveOrdersApproveSlipActivity.this);
            }
        }
    }

    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //_progressBar.setVisibility(View.INVISIBLE);
        //_latitude.setText("Latitude: " + String.valueOf(mLastLocation.getLatitude()));
        //_longitude.setText("Longitude: " + String.valueOf(mLastLocation.getLongitude()));

        mLatitude = String.valueOf(mLastLocation.getLatitude());
        mLongitude = String.valueOf(mLastLocation.getLongitude());

        txtgps.setText("Lat: " + String.valueOf(mLastLocation.getLatitude())+" "+"Long: " + String.valueOf(mLastLocation.getLongitude()));
    }



}

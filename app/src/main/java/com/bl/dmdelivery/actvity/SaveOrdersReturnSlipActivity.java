package com.bl.dmdelivery.actvity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.SaveOrderReasonViewAdapter;
import com.bl.dmdelivery.adapter.SaveOrderReturnReasonViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
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
import java.util.Locale;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SaveOrdersReturnSlipActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView mmTxtMsg,mTxtHeader,mmTxtTitle,mTxtInvNo,mTxtRepcode;
    private Button mBtnBack,mBtnCancelGPS,mBtnCancel,mBtnGPS,mBtnSaveGPS,mBtnSave,mBtnNew,mBtnNote,mmBtnOk,mmBtnClose;
    private ImageView mmImvTitle;
    private EditText medtNote;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ArrayList<Reason> mReturnAcceptRejectList = new ArrayList<Reason>();
    private ArrayList<OrderReturn> mListOrderReturnSavDate = new ArrayList<OrderReturn>();
    private ArrayList<Order> mListOrder= new ArrayList<Order>();
    private OrderReturn mOrderReturnGetData = null;
    private OrderReturn mOrderReturnSaveData = null;
    private CheckNetwork chkNetwork = new CheckNetwork();
    private DBHelper mHelper;
    private ACProgressFlower mProgressDialog;
    private String serverUrl;

    private CanvasViewSlipReturn mCanvasViewSlipReturn;


    private ArrayList<Reason> arrayListReason = new ArrayList<Reason>();
    private RecyclerView lvReturnAcceptRejectList;
    private RecyclerView.Adapter mReturnAcceptRejectListAdapter;
    private String mSelectReson = "";
    private Integer mSelectResonIndex = 0;
    private String  mSelect="0";

    private String mInputPath = Environment.getExternalStorageDirectory().toString() + "/DMSLIPRETURN/";
    private String mInputPathProcess = Environment.getExternalStorageDirectory().toString() + "/DMRETURNPROCESSED/";


    private String sigInvNo="";
    private String sigReturn_no="";
    private String sigRepcode="";
    private String sigRep_name="";
    private String sigReson_code="";
    private String sigNote="";
    private String backToPage="";

    public String batteryPercent = "0";
    private File mFile;
    private String mFileName;


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private String mLatitude = "0";
    private String mLongitude = "0";

    private String sigTruckNo = "";
    private String sigDeliveryDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_return_slip);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

            bindWidget();

            setWidgetControl();
        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        backToPage = "BACK_TO_PAGE";

        editor = sp.edit();
        editor.putString(TagUtils.PREF_BACK_TO_PAGE, backToPage);
        editor.apply();

        finish();
    }

    private void bindWidget()
    {
        try{

            sigTruckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
            sigDeliveryDate = sp.getString(TagUtils.PREF_DELIVERY_DATE, "");

            Intent ineGetIntent= getIntent();
            Bundle bdlGetExtras= ineGetIntent.getExtras();

            if(bdlGetExtras == null) {
                mOrderReturnGetData = new OrderReturn();
                mListOrderReturnSavDate.clear();
                mListOrder.clear();

                sigInvNo="";
                sigReturn_no="";
                sigRepcode="";
            } else {
                mOrderReturnGetData = new OrderReturn();
                mOrderReturnGetData =(OrderReturn)bdlGetExtras.get("datareturn");

                mListOrderReturnSavDate.clear();
                mListOrderReturnSavDate = (ArrayList<OrderReturn>)bdlGetExtras.get("datareturnAll");

                mListOrder.clear();
                mListOrder = (ArrayList<Order>)bdlGetExtras.get("dataInvUpdateToSlip");


                sigInvNo = mOrderReturnGetData.getReftrans_no();
                sigReturn_no = mOrderReturnGetData.getReturn_no();
                sigRepcode = mOrderReturnGetData.getRep_code();
                sigRep_name = mOrderReturnGetData.getRep_name();
            }

            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnCancelGPS = (Button) findViewById(R.id.btnCancelGPS);
            mBtnCancel = (Button) findViewById(R.id.btnCancel);
            mBtnGPS = (Button) findViewById(R.id.btnGPS);
            mBtnSaveGPS = (Button) findViewById(R.id.btnSaveGPS);
            mBtnSave = (Button) findViewById(R.id.btnSave);

            mBtnNew= (Button) findViewById(R.id.btnNew);
            mBtnNote = (Button) findViewById(R.id.btnNote);

            mBtnCancelGPS.setVisibility(View.INVISIBLE);
            mBtnCancel.setVisibility(View.INVISIBLE);
            mBtnGPS.setVisibility(View.INVISIBLE);
            mBtnSaveGPS.setVisibility(View.INVISIBLE);
            mBtnSave.setText("บันทึก");


            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return_slip));

            mTxtInvNo = (TextView) findViewById(R.id.txtInvNo);
            mTxtRepcode = (TextView) findViewById(R.id.txtRepcode);


            mCanvasViewSlipReturn = (CanvasViewSlipReturn) findViewById(R.id.signature_canvas);


            File dirInput = new File (mInputPath);
            if (!dirInput.exists())
            {
                dirInput.mkdirs();
            }

            File dirInputProcess = new File (mInputPathProcess);
            if (!dirInputProcess.exists())
            {
                dirInputProcess.mkdirs();
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

            arrayListReason.clear();
            mHelper = new DBHelper(getApplicationContext());
            arrayListReason = mHelper.getReasonListForCondition("'RETURN_ACCEPT'");


            if(arrayListReason.size() > 0)
            {
                sigReson_code =  arrayListReason.get(0).getReason_code();
                mSelectReson =  arrayListReason.get(0).getReason_desc();
                mSelectResonIndex = 0;

                mCanvasViewSlipReturn.reason = mSelectReson;
                mCanvasViewSlipReturn.invalidate();
            }

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


    private void setWidgetControl() {
        try{

            mTxtInvNo.setText("เลขที่ใบรับคืน : " + sigReturn_no);
            mTxtRepcode.setText("รหัส-ชื่อสมาชิก : " + sigRepcode + " - " + sigRep_name);


            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    backToPage = "BACK_TO_PAGE";

                    editor = sp.edit();
                    editor.putString(TagUtils.PREF_BACK_TO_PAGE, backToPage);
                    editor.apply();

                    CanvasViewSlipReturn.totalDx = 0;
                    CanvasViewSlipReturn.totalDy = 0;

                    finish();
                }
            });

            mBtnNew.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    clearCanvas(view);
                }
            });


            mBtnNote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showMsgReasonApproveSelectedSingleDialog();
                }
            });


            mBtnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    if ( (CanvasViewSlipReturn.totalDx > 100) || (CanvasViewSlipReturn.totalDy > 100)) {

                        takeScreenshot();


//                        SystemClock.sleep(1000);


//                        Thread closeActivity = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    // Do some stuff
                                } catch (Exception e) {
                                    e.getLocalizedMessage();
                                }
//                            }
//                        });


                        doSaveProcessing();
                    }
                    else
                    {
                        showMsgDialog("ไม่มีลายเซ็น");
                        return;
                    }

                }
            });

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    public void clearCanvas(View v) {
        mCanvasViewSlipReturn.totalDx = 0;
        mCanvasViewSlipReturn.totalDy  = 0;
        mCanvasViewSlipReturn.clearCanvas();
    }

    public void showMsgReasonApproveSelectedSingleDialog()
    {

        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_save_orders_return_cancel, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnok);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);
        medtNote = (EditText) v.findViewById(R.id.edtNote);

        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
        medtNote.setTypeface(tf);
        medtNote.setText(sigNote);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText(getResources().getString(R.string.txt_text_reason_remark));
        mmBtnOk.setText(getResources().getString(R.string.btn_text_ok));


        lvReturnAcceptRejectList = (RecyclerView) v.findViewById(R.id.lvacceptList);
        lvReturnAcceptRejectList.setLayoutManager(new LinearLayoutManager(this));
        lvReturnAcceptRejectList.setHasFixedSize(true);


        if(mSelectResonIndex > 0)
        {

            arrayListReason.get(mSelectResonIndex).setIsselect("1");
            sigReson_code = arrayListReason.get(mSelectResonIndex).getReason_code();
        }
        else
        {
            arrayListReason.get(0).setIsselect("1");
            sigReson_code = arrayListReason.get(0).getReason_code();
        }

        mReturnAcceptRejectListAdapter = new SaveOrderReturnReasonViewAdapter(getApplicationContext(),arrayListReason);
        lvReturnAcceptRejectList.setAdapter(mReturnAcceptRejectListAdapter);



        lvReturnAcceptRejectList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                for(int i = arrayListReason.size()-1 ; i >= 0; i--)
                {
                    mSelect = arrayListReason.get(i).getIsselect().toString();

                    if(mSelect.equals("1"))
                    {
                        arrayListReason.get(i).setIsselect("0");
                    }
                }


                mSelect = arrayListReason.get(position).getIsselect();

                arrayListReason.get(position).setIsselect("1");

                String description = arrayListReason.get(position).getReason_desc();
                sigReson_code = arrayListReason.get(mSelectResonIndex).getReason_code();


                mSelectReson = description;

                mSelectResonIndex = position;



                mReturnAcceptRejectListAdapter.notifyDataSetChanged();

            }
        }));


        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //loadData();
                DialogBuilder.dismiss();

                sigNote = medtNote.getText().toString();
                mCanvasViewSlipReturn.reason = mSelectReson;
                mCanvasViewSlipReturn.note = sigNote;
                mCanvasViewSlipReturn.invalidate();
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
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


    private void takeScreenshot() {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        java.util.Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String localTime = date.format(currentLocalTime);
        localTime = localTime.replace(" ", "").replace("-", "");

        getBatteryPercentage();


      /*  NumberFormat nf = NumberFormat.getInstance();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lx = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String latlng = nf.format(lx.getLatitude()) + "," + nf.format(lx.getLongitude());*/

        String latlng = mLatitude+","+mLongitude;

        String truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");


        String fileName= "V" + getResources().getString(R.string.app_version_slip) + "_" + Build.SERIAL.trim() +  "-" + truckNo + "-" + mOrderReturnGetData.getReftrans_no() + "-" + mOrderReturnGetData.getRep_code() + "-" + localTime + "-" + latlng + "-" + getImeiNumber() + "-" + batteryPercent + ".jpg";


        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/DMSLIPRETURN/" + fileName;


            View v = (View) findViewById(R.id.lnlSlip);

            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            v.setDrawingCacheEnabled(true);
            v.setBackgroundColor(Color.parseColor("#ffffff"));
            v.invalidate();
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



    private void doSaveProcessing() {
        try {

            serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/LoadOrder";

            new loadDataDataInAsync().execute(serverUrl);

        } catch (Exception e) {
            //e.printStackTrace();
            showMsgDialog(e.toString());
        }

    }


    private class loadDataDataInAsync extends AsyncTask<String, Void, SaveOrdersReturnSlipActivity.PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(SaveOrdersReturnSlipActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .text(getResources().getString(R.string.progress_loading))
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();

        }

        @Override
        protected SaveOrdersReturnSlipActivity.PageResultHolder doInBackground(String... params) {
            // TODO Auto-generated method stub
            SaveOrdersReturnSlipActivity.PageResultHolder pageResultHolder = new SaveOrdersReturnSlipActivity.PageResultHolder();

            try
            {

                backToPage = "SAVE_TO_PAGE";

                editor = sp.edit();
                editor.putString(TagUtils.PREF_BACK_TO_PAGE, backToPage);
                editor.apply();

                //ถ้าบันทึก ใบคืนไม่ครบ ไปหน้าใบคืน
                if(medtNote != null){
                    sigNote = medtNote.getText().toString();
                }
                else
                {
                    sigNote = "";
                }

//                takeScreenshot();


                // Current Date
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String formattedDate = df.format(cal.getTime());


                for(int i=0; i < mListOrderReturnSavDate.size(); i++){
                    //บันทึกข้อมูล รับได้
                    mHelper = new DBHelper(getApplicationContext());
                    OrderReturn mOrderReturn = new OrderReturn();
                    mOrderReturn.setReturn_no(sigReturn_no);
                    mOrderReturn.setRep_code(sigRepcode);
                    mOrderReturn.setReturn_status("1");
                    mOrderReturn.setReason_code(sigReson_code);
                    mOrderReturn.setReturn_note(sigNote);
                    mOrderReturn.setReturn_unit_real(mListOrderReturnSavDate.get(i).getReturn_unit_real());
                    mOrderReturn.setFullpathimage(mFileName);
                    mOrderReturn.setLat(mLatitude);
                    mOrderReturn.setLon(mLongitude);
                    mOrderReturn.setSignature_timestamp(formattedDate);
                    mOrderReturn.setTrack_no(sigTruckNo);
                    mOrderReturn.setDelivery_date(sigDeliveryDate);
                    mHelper.updateOrderReturnSlip(mOrderReturn);
                }


                //update order
                mHelper = new DBHelper(getApplicationContext());
                mHelper.updateOrdersDeliveryStatus(mListOrder,"S");


            } catch (Exception e) {
                pageResultHolder.content = "Exception : เกิดข้อผิดพลาดในการบันทึกข้อมูล !!!";
                pageResultHolder.exception = e;
            }

            return pageResultHolder;
        }

        @Override
        protected void onPostExecute(final SaveOrdersReturnSlipActivity.PageResultHolder result) {
            // TODO Auto-generated method stub
            try {

                if (result.exception != null) {
                    mProgressDialog.dismiss();
                    showMsgDialog(result.exception.toString());
                }
                else
                {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            mProgressDialog.dismiss();

                            //รับได้
                            finish();
                        }
                    }, 200);

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



    //******** LAT,LONG
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
                            status.startResolutionForResult(SaveOrdersReturnSlipActivity.this, 1000);
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

//                //txtgps.setText("Lat: " + String.valueOf(mLastLocation.getLatitude())+" "+"Long: " + String.valueOf(mLastLocation.getLongitude()));
//
//                customCanvas.gps = "GPS : " + String.valueOf(mLastLocation.getLatitude())+"," + String.valueOf(mLastLocation.getLongitude());
//                //customCanvas.gpstext = "Location : ";
//                customCanvas.gpstext = "Location : "+getCompleteAddressString(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//                customCanvas.invalidate();
            } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                * So we will get the current location.
                * For this we'll implement Location Listener and override onLocationChanged*/
                Log.i("Current Location", "No data for location found");

//                //txtgps.setText("No data for location found");
//                customCanvas.gps = "";
//                customCanvas.gpstext = "No data for location found";
//                customCanvas.invalidate();

                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, SaveOrdersReturnSlipActivity.this);
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

//        //txtgps.setText("Lat: " + String.valueOf(mLastLocation.getLatitude())+" "+"Long: " + String.valueOf(mLastLocation.getLongitude()));
//        customCanvas.gps = "GPS : " + String.valueOf(mLastLocation.getLatitude())+"," + String.valueOf(mLastLocation.getLongitude());
//        customCanvas.gpstext = "Location : "+getCompleteAddressString(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//
//
//        customCanvas.invalidate();
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        Locale th = new Locale("th");

        Geocoder geocoder = new Geocoder(this, th);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                //Address returnedAddress = addresses.get(0);
                //StringBuilder strReturnedAddress = new StringBuilder("");

//                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }

                strAdd = addresses.get(0).getLocality()+" "+addresses.get(0).getSubAdminArea()+" "+addresses.get(0).getAdminArea();


                //strAdd = strReturnedAddress.toString();
                //Log.w("My Current loction address", strReturnedAddress.toString());
            } else {

                strAdd = "";
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }




}

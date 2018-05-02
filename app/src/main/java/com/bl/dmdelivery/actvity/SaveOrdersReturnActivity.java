package com.bl.dmdelivery.actvity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrderReturnDtlViewAdapter;
import com.bl.dmdelivery.adapter.OrderReturnViewAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.SaveOrderReasonViewAdapter;
import com.bl.dmdelivery.adapter.SaveOrderReturnReasonViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.LoadOrderResponse;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.OrderSourceResponse;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.model.Unpack;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SaveOrdersReturnActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView mmTxtMsg,mTxtHeader,mmTxtTitle,mTxtsum,mmTxtQty;
    private Button mBtnBack,mmBtnOk,mmBtnClose,mBtnPlus,mBtnDel,mBtnConfirm,mBtnCheckScan;
    private ImageView mmImvTitle;
    private EditText medtNote;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ArrayList<Reason> arrayListReason = new ArrayList<Reason>();

    private String  mSelect="0";
    private String mSelectReson = "";
    private Integer mSelectResonIndex = 0;
    private Intent myIntent=null;


    private String backToPage="";
    private String ref_rep_code;
    private String ref_return_no;

    private int intQTY_UINT_REAL= 0;
    private int intQTY_UINT= 0;
    private String sigNote="";

    private ArrayList<OrderReturn> mListOrderReturn = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListOrderReturnSaveData = new ArrayList<OrderReturn>();
    private ArrayList<Order> mListOrder= new ArrayList<Order>();
    private OrderReturn mOrderReturnSaveData = null;
    private CheckNetwork chkNetwork = new CheckNetwork();
    private DBHelper mHelper;
    private ACProgressFlower mProgressDialog;
    private String serverUrl;

    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;

    private RecyclerView lvReturnAcceptRejectList;
    private RecyclerView.Adapter mReturnAcceptRejectListAdapter;


    private String sigReson_code="";
    private boolean isResumeState = false;

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
        setContentView(R.layout.activity_save_orders_return);

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
    public void onResume(){
        super.onResume();

        if(isResumeState)
        {
//            //มาจากหน้า slip
//            Toast toast = Toast.makeText(SaveOrdersReturnActivity.this, "onResume - Slip2", Toast.LENGTH_SHORT);
//            toast.show();


            String sigBackToPage = sp.getString(TagUtils.PREF_BACK_TO_PAGE, "");
            if (sigBackToPage.toUpperCase().equals("SAVE_TO_PAGE"))
            {
                finish();
            }
        }
//        else
//        {
//            //มาจากหน้าใบคืน
//            Toast toast = Toast.makeText(SaveOrdersReturnActivity.this, "onResume - returnDoc", Toast.LENGTH_SHORT);
//            toast.show();
//        }
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
                mOrderReturnSaveData = new OrderReturn();
                mListOrderReturnSaveData.clear();
                mListOrder.clear();

                ref_return_no= null;
                ref_rep_code= null;
            } else {
                mOrderReturnSaveData = new OrderReturn();
                mOrderReturnSaveData =(OrderReturn)bdlGetExtras.get("data");

                mListOrderReturnSaveData.clear();
                mListOrderReturnSaveData = (ArrayList<OrderReturn>)bdlGetExtras.get("dataAll");

                mListOrder.clear();
                mListOrder = (ArrayList<Order>)bdlGetExtras.get("dataInvUpdate");


                ref_return_no= mOrderReturnSaveData.getReturn_no();
                ref_rep_code= mOrderReturnSaveData.getRep_code();
            }

            isResumeState = false;

            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
            mBtnCheckScan = (Button) findViewById(R.id.btnCheckScan);

            mTxtsum = (TextView) findViewById(R.id.txtsum);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);


            lv = (RecyclerView) findViewById(R.id.lv);
            lv.setLayoutManager(new LinearLayoutManager(this));
            lv.setHasFixedSize(true);



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
            arrayListReason = mHelper.getReasonListForCondition("'RETURN_REJECT'");


            if(arrayListReason.size() > 0)
            {
                sigReson_code =  arrayListReason.get(0).getReason_code();
                mSelectReson =  arrayListReason.get(0).getReason_desc();
                mSelectResonIndex = 0;
            }

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


    private void setWidgetControl() {
        try{

            getInit();

            mTxtsum.setText("จำนวนรายการสินค้า : "+String.valueOf( mListOrderReturn.size()));
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return));

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                }
            });


            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    //check Qty
                    int intCountReturn_unit_real_Blank = 0;
                    for(int i=0;i<mListOrderReturn.size();i++){
                        if(mListOrderReturn.get(i).getReturn_unit_real().equals("") ||
                                mListOrderReturn.get(i).getReturn_unit_real().equals("0") ||
                                mListOrderReturn.get(i).getReturn_unit_real() == null){
                                intCountReturn_unit_real_Blank++;
                        }
                    }

                    if (mListOrderReturn.size()>0){
                        if(intCountReturn_unit_real_Blank == mListOrderReturn.size())
                        {
                            showMsgDialog("จำนวนรับคืนจริงไม่ถูกต้อง...กรุณาตรวจสอบใหม่อีกครั้ง !!!");
                            return;
                        }
                    }



                    isResumeState = true;
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnSlipActivity.class);
                    myIntent.putExtra("datareturn", mOrderReturnSaveData);
                    myIntent.putExtra("datareturnAll", mListOrderReturn);
                    myIntent.putExtra("dataInvUpdateToSlip", mListOrder);

                    startActivity(myIntent);
                }
            });


            mBtnCheckScan.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showMsgReasonApproveSelectedSingleDialog();
                }
            });

            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    String sigFs_code  =  mListOrderReturn.get(position).getFs_code();
                    String sigReturn_unit_real  =  mListOrderReturn.get(position).getReturn_unit_real();
                    String sigReturn_unit  =  mListOrderReturn.get(position).getReturn_unit();


                    showMsgQtyDialog(sigFs_code,sigReturn_unit_real,sigReturn_unit,position);
                }
            }));


        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void getInit() {

        try {

            mHelper = new DBHelper(getApplicationContext());
            mListOrderReturn.clear();
            mListOrderReturn = mHelper.getOrderReturnDtl(ref_return_no);

            if(mListOrderReturn.size()>0)
            {

                for (int i=0; i<mListOrderReturn.size();i++)
                {
                    String sigReturn_unit = mListOrderReturn.get(i).getReturn_unit();
                    mListOrderReturn.get(i).setReturn_unit_real(sigReturn_unit);
                }

                mAdapter = new OrderReturnDtlViewAdapter(getApplicationContext(),mListOrderReturn);
                lv.setAdapter(mAdapter);
            }else
            {
                //finish();
                //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                showMsgDialog(getResources().getString(R.string.error_data_not_in_system));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showMsgQtyDialog(String sigFs_code,String sigQty_uint_real,String sigQty_uint,int selectedPosition)
    {
        intQTY_UINT_REAL=0;
        intQTY_UINT= 0;

        final String sigFs_code_final = sigFs_code;
        final String sigQty_uint_real_final = sigQty_uint_real;
        final String sigQty_uint_final = sigQty_uint;
        final int selectedPosition_Final = selectedPosition;



        if(sigQty_uint_final.isEmpty() || sigQty_uint_final.equals("") || sigQty_uint_final==null){
            intQTY_UINT = Integer.parseInt("0");
        }
        else
        {
            intQTY_UINT = Integer.parseInt(sigQty_uint_final);
        }


        if(sigQty_uint_real_final.isEmpty() || sigQty_uint_real_final.equals("") || sigQty_uint_real_final==null){
            intQTY_UINT_REAL = Integer.parseInt("0");

            if(intQTY_UINT_REAL == 0)
            {
                intQTY_UINT_REAL = intQTY_UINT;
            }
        }
        else if(sigQty_uint_real_final.equals("0"))
        {
            intQTY_UINT_REAL = 0;
        }
        else
        {
            intQTY_UINT_REAL = Integer.parseInt(sigQty_uint_real_final);

            if(intQTY_UINT_REAL == 0)
            {
                intQTY_UINT_REAL = intQTY_UINT;
            }
            else
            {
                if(intQTY_UINT_REAL <= intQTY_UINT){
                    intQTY_UINT_REAL = Integer.parseInt(sigQty_uint_real_final);
                }
                else
                {
                    intQTY_UINT_REAL = intQTY_UINT;
                }
            }
        }


        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersReturnActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_confirm_qty, null);
        DialogBuilder.setView(v);

        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmImvTitle.setImageResource(R.mipmap.ic_launcher);

        mmTxtQty = (TextView) v.findViewById(R.id.txtQty);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);

        mBtnPlus = (Button) v.findViewById(R.id.btnPlus);
        mBtnDel = (Button) v.findViewById(R.id.btnDel);

        mmTxtTitle.setText("แก้ไขจำนวนรับจริง");
        mmTxtQty.setText(String.valueOf(intQTY_UINT_REAL));


        mBtnPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if( intQTY_UINT_REAL >= intQTY_UINT){
                    return;
                }
                else
                {
                    intQTY_UINT_REAL++;

                    mmTxtQty.setText(String.valueOf(intQTY_UINT_REAL));
                }
            }
        });

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(intQTY_UINT_REAL <= 0){
                    return;
                }
                else
                {
                    intQTY_UINT_REAL--;

                    mmTxtQty.setText(String.valueOf(intQTY_UINT_REAL));
                }
            }
        });

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();

//                //update qty on row
//                mHelper = new DBHelper(getApplicationContext());
//                OrderReturn mOrderReturn = new OrderReturn();
//                mOrderReturn.setRep_code(ref_rep_code);
//                mOrderReturn.setReturn_no(ref_return_no);
//                mOrderReturn.setFs_code(sigFs_code_final);
//                mOrderReturn.setReturn_unit_real(mmTxtQty.getText().toString());
//                mHelper.updateOrderReturnDtl(mOrderReturn);


                mListOrderReturn.get(selectedPosition_Final).setRep_code(ref_rep_code);
                mListOrderReturn.get(selectedPosition_Final).setReturn_no(ref_return_no);
                mListOrderReturn.get(selectedPosition_Final).setFs_code(sigFs_code_final);
                mListOrderReturn.get(selectedPosition_Final).setReturn_unit_real(mmTxtQty.getText().toString());

                if(mListOrderReturn.size() > 0) {
                    mAdapter = new OrderReturnDtlViewAdapter(getApplicationContext(),mListOrderReturn);
                    lv.setAdapter(mAdapter);

                    mAdapter.notifyDataSetChanged();
                    lv.scrollToPosition(selectedPosition_Final);
                }



//
//                //call init
//                getInit();
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
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

                sigReson_code = arrayListReason.get(position).getReason_code();

                mSelectReson = description;

                mSelectResonIndex = position;


                mReturnAcceptRejectListAdapter.notifyDataSetChanged();

            }
        }));


        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//
//                backToPage = "SAVE_TO_PAGE";
//
//                editor = sp.edit();
//                editor.putString(TagUtils.PREF_BACK_TO_PAGE, backToPage);
//                editor.apply();
//
//                //ถ้าบันทึก ใบคืนไม่ครบ ไปหน้าใบคืน
//                if(medtNote != null){
//                    sigNote = medtNote.getText().toString();
//                }
//                else
//                {
//                    sigNote = "";
//                }
//
//                //บันทึกข้อมูล รับไม่ได้
//                mHelper = new DBHelper(getApplicationContext());
//                mOrderReturnSaveData = new OrderReturn();
//                mOrderReturnSaveData.setReturn_no(ref_return_no);
//                mOrderReturnSaveData.setRep_code(ref_rep_code);
//                mOrderReturnSaveData.setReturn_status("2");
//                mOrderReturnSaveData.setReason_code(sigReson_code);
//                mOrderReturnSaveData.setReturn_note(sigNote);
//                mOrderReturnSaveData.setReturn_unit_real("0");
//                mHelper.updateOrderReturnDetails(mOrderReturnSaveData);
//
//
//
//                for(int i=0; i < mListOrder.size(); i++){
//                    //บันทึกข้อมูล รับได้ ไปยัง orders
//                    mHelper = new DBHelper(getApplicationContext());
//                    mHelper.updateOrdersStatus(mListOrder.get(i).getTransNo(),"2");
//                }
//
//
//
//                //รับได้
//                finish();

//                DialogBuilder.dismiss();




                DialogBuilder.dismiss();


                doSaveProcessing();
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
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


    private class loadDataDataInAsync extends AsyncTask<String, Void, SaveOrdersReturnActivity.PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(SaveOrdersReturnActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .text(getResources().getString(R.string.progress_loading))
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();

        }

        @Override
        protected SaveOrdersReturnActivity.PageResultHolder doInBackground(String... params) {
            // TODO Auto-generated method stub
            SaveOrdersReturnActivity.PageResultHolder pageResultHolder = new SaveOrdersReturnActivity.PageResultHolder();

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


                // Current Date
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String formattedDate = df.format(cal.getTime());


                //บันทึกข้อมูล รับไม่ได้ order return
                mHelper = new DBHelper(getApplicationContext());
                mOrderReturnSaveData = new OrderReturn();
                mOrderReturnSaveData.setReturn_no(ref_return_no);
                mOrderReturnSaveData.setRep_code(ref_rep_code);
                mOrderReturnSaveData.setReturn_status("2");
                mOrderReturnSaveData.setReason_code(sigReson_code);
                mOrderReturnSaveData.setReturn_note(sigNote);
                mOrderReturnSaveData.setReturn_unit_real("0");
                mOrderReturnSaveData.setFullpathimage("");
                mOrderReturnSaveData.setLat(mLatitude);
                mOrderReturnSaveData.setLon(mLongitude);
                mOrderReturnSaveData.setSignature_timestamp(formattedDate);
                mOrderReturnSaveData.setTrack_no(sigTruckNo);
                mOrderReturnSaveData.setDelivery_date(sigDeliveryDate);
                mHelper.updateOrderReturnDetails(mOrderReturnSaveData);


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
        protected void onPostExecute(final SaveOrdersReturnActivity.PageResultHolder result) {
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
                            status.startResolutionForResult(SaveOrdersReturnActivity.this, 1000);
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

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, SaveOrdersReturnActivity.this);
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

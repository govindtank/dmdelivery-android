package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrderScanViewAdapter;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.OrderScan;
import com.bl.dmdelivery.model.OrderScanReq;
import com.bl.dmdelivery.model.OrderScanResponse;
import com.bl.dmdelivery.model.Unpack;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.Response;

public class ScanOrdersActivity extends AppCompatActivity {

    private ACProgressFlower mProgressDialog;
    private TextView mTxtMsg,mTxtHeader;
    private Button mBtnBack,mBtnCheckScan,mBtnOk,mBtnClose,mBtnConfirm;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<OrderScan> mListOrderScan = new ArrayList<OrderScan>();
    private Response resResponse;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String truckNo = "";
    private String deliveryDate = "";
    private OrderScanReq mOrderScanReq;
    private String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_orders);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

            bindWidget();
//
//            setDefaultFonts();

            setWidgetControl();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }


    }

    private void bindWidget()
    {
        try{
           mOrderScanReq = new OrderScanReq();
           mOrderScanReq.setTruckNo(sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, ""));
           mOrderScanReq.setDeliveryDate(sp.getString(TagUtils.PREF_DELIVERY_DATE, ""));

            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnCheckScan = (Button)findViewById(R.id.btnCheckScan);
            mBtnConfirm = (Button)findViewById(R.id.btnConfirm);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_scanorders_list));
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

//    private void setDefaultFonts() {
//
//        try {
//            Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//            mTxtHeader.setTypeface(tf);
//            mBtnBack.setTypeface(tf);
//
//        } catch (Exception e) {
//            showMsgDialog(e.toString());
//        }
//    }

    private void setWidgetControl() {
        try{
            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    //previewing = false;
                    //mCamera.setPreviewCallback(null);
                    //mCamera.stopPreview();

                    finish();

                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                }
            });
            mBtnCheckScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showScanDialog("");
                }
            });
            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMsgConfirmDialog("ยืนยันการสแกนสินค้าขึ้นรถ");
                }
            });
        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
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

    public void showMsgConfirmDialog(String msg)
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_confirm, null, false);


//        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
//        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mTxtHeader = (TextView) v.findViewById(R.id.txtTitle);
        mBtnClose = (Button) v.findViewById(R.id.btnClose);
        mBtnOk = (Button)v.findViewById(R.id.btnOk);

//        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mTxtHeader.setText("ยืนยัน");
        mTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }



    public void showScanDialog(String msg)
    {
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alert = DialogBuilder.create();
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_scan_orders_check, null, false);

        lv = (RecyclerView)v.findViewById(R.id.lv);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setHasFixedSize(true);
        getScanData();


        //mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);

        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
        //mTxtMsg.setTypeface(tf);
        //mTxtMsg.setText(msg);

        DialogBuilder.setView(v);
        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        DialogBuilder.show();
    }


    private void getScanData(){
        try {

            // TODO Call Webservice
            //if(checkNetwork.isConnectionAvailable(getContext())){
                serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/CheckOrderScan";
                new getScanDataInAsync().execute(serverUrl);
            //}

            //else {
            //    showMsgDialog("Can't Connect Network");
            //}

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }

    }


    private class getScanDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         /*   mProgressDialog = new ACProgressFlower.Builder(getApplicationContext())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();*/

        }

        @Override
        protected PageResultHolder doInBackground(String... params) {
            // TODO Auto-generated method stub
            PageResultHolder pageResultHolder = new PageResultHolder();
            //String xmlInput = params[0];
            try
            {
                //use call api
                Gson gson = new Gson();
                String json = gson.toJson(mOrderScanReq);
                String result = new WebServiceHelper().postServiceAPI(params[0],json);
                Log.i("LoginResult", result.toString());

                //convert json to obj
                OrderScanResponse obj = gson.fromJson(result,OrderScanResponse.class);
                mListOrderScan.clear();
                for(int i=0; i<obj.getCheckOrderScan().size();i++){

                    OrderScan f = new OrderScan();
                    f.setItem(i+1);
                    f.setInvoiceNo(obj.getCheckOrderScan().get(i).getInvoiceNo().toString());
                    f.setDeliveryDate(obj.getCheckOrderScan().get(i).getDeliveryDate().toString());
                    f.setTruckNo(obj.getCheckOrderScan().get(i).getTruckNo().toString());
                    f.setTotalCanton(obj.getCheckOrderScan().get(i).getTotalCanton());
                    f.setTotalScan(obj.getCheckOrderScan().get(i).getTotalScan());
                    f.setTotalNotScan(obj.getCheckOrderScan().get(i).getTotalNotScan());
                    mListOrderScan.add(f);

                }

            } catch (Exception e) {
                pageResultHolder.content = "Exception : CheckOrderData";
                pageResultHolder.exception = e;
                //return null;
            }

            return pageResultHolder;
        }

        @Override
        protected void onPostExecute(final PageResultHolder result) {
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

                            //mProgressDialog.dismiss();

                            if(mListOrderScan.size()>0)
                            {

                                mAdapter = new OrderScanViewAdapter(getApplicationContext(),mListOrderScan);
                                lv.setAdapter(mAdapter);


                            }else
                            {
                                //finish();
                                //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                                showMsgDialog(getResources().getString(R.string.error_data_not_in_system));

                            }

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





}

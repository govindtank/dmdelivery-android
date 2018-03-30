package com.bl.dmdelivery.actvity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrderScanViewAdapter;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.BaseResponse;
import com.bl.dmdelivery.model.BookingReq;
import com.bl.dmdelivery.model.BookingResponse;
import com.bl.dmdelivery.model.ConfirmReq;
import com.bl.dmdelivery.model.OrderScan;
import com.bl.dmdelivery.model.OrderScanReq;
import com.bl.dmdelivery.model.OrderScanResponse;
import com.bl.dmdelivery.model.OrderSummary;
import com.bl.dmdelivery.model.Unpack;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.Response;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class ScanOrdersActivity extends AppCompatActivity {

    private ACProgressFlower mProgressDialog;
    private TextView mTxtMsg,mTxtHeader,mTxtResult,mTxtOrderSum,mTxtBoxBagSum,mTxtBoxSum,mTxtBagSum,mmTxtMsg,mmTxtTitle;
    private Button mBtnBack,mBtnCheckScan,mBtnOk,mBtnClose,mBtnConfirm,mmBtnOk,mmBtnClose,mBtnFlash;
    private ImageView mmImvTitle;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<OrderScan> mListOrderScan = new ArrayList<OrderScan>();
    private ArrayList<OrderSummary> mListOrderSum = new ArrayList<OrderSummary>();
    private Response resResponse;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String truckNo = "";
    private String deliveryDate = "";
    private OrderScanReq mOrderScanReq;
    private BookingReq mBookingReq;
    private ConfirmReq mConfirmReq;
    private String serverUrl;
    private Integer i = 0;
    private CheckNetwork chkNetwork = new CheckNetwork();

    private boolean isFlash =false;
    //private ImageButton mBtnFlash;


    private DecoratedBarcodeView barcodeScannerView;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            handleDecode(result);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };



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

    public void handleDecode(BarcodeResult rawResult) {
        barcodeScannerView.pause();//Pause preview
        String result = rawResult.getText();

        if (result != null) {
            mTxtResult.setText(result);
            BeepManager bm = new BeepManager(this);
            bm.playBeepSoundAndVibrate();
            //getInsertData(result);
            if(InsertData(result) == "0"){
                barcodeScannerView.resume();
            }

        }else {
            mTxtResult.setText("Error");
            barcodeScannerView.resume();
        }


    }

    private void bindWidget()
    {
        try{
           mOrderScanReq = new OrderScanReq();
           mOrderScanReq.setTruckNo(sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, ""));
           mOrderScanReq.setDeliveryDate(sp.getString(TagUtils.PREF_DELIVERY_DATE, ""));

            mBookingReq = new BookingReq();
            mBookingReq.setTruckNo(sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, ""));
            mBookingReq.setDeliveryDate(sp.getString(TagUtils.PREF_DELIVERY_DATE, ""));

            mConfirmReq = new ConfirmReq();
            mConfirmReq.setTruckNo(sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, ""));
            mConfirmReq.setDeliveryDate(sp.getString(TagUtils.PREF_DELIVERY_DATE, ""));

            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnCheckScan = (Button)findViewById(R.id.btnCheckScan);
            mBtnConfirm = (Button)findViewById(R.id.btnConfirm);
            mBtnFlash = (Button)findViewById(R.id.btnFlash);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_scanorders_list));
            mTxtResult = (TextView)findViewById(R.id.txtResult);
            mTxtOrderSum = (TextView)findViewById(R.id.txtOrdersSum);
            mTxtBoxSum = (TextView)findViewById(R.id.txtBoxSum);
            mTxtBagSum = (TextView)findViewById(R.id.txtBagSum);
            mTxtBoxBagSum = (TextView)findViewById(R.id.txtBoxBagSum);

            //Camara
            barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
            barcodeScannerView.decodeContinuous(callback);


        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }





    private void setWidgetControl() {
        try{
            //getInsertData("");
            LoadData("");
            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

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
                    showMsgConfirmDialog("ยืนยันการสแกนสินค้าขึ้นรถ",getResources().getString(R.string.btn_text_confirm));
                }
            });


            mBtnFlash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFlash){
                        isFlash = true;
                        barcodeScannerView.setTorchOn();
                        //mBtnFlash.setBackgroundResource(R.mipmap.ic_launcher);
                        Drawable icon = getResources(). getDrawable( R.mipmap.ic_flashlightfilled_on);
                        mBtnFlash.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
                        //((ImageButton)view).setImageResource(R.drawable.ic_flash_on_black_24dp);
                    }else{
                        isFlash = false;
                        barcodeScannerView.setTorchOff();
                        //mBtnFlash.setBackgroundResource(R.mipmap.ic_launcher_round);
                        Drawable icon = getResources(). getDrawable( R.mipmap.ic_flashlightfilled_off);
                        mBtnFlash.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
                        //((ImageButton)view).setImageResource(R.drawable.ic_flash_off_black_24dp);
                    }

                   /* if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
                        barcodeScannerView.setTorchOn();
                    } else {
                        barcodeScannerView.setTorchOff();
                    }*/
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

    public void showMsgConfirmDialog(String msg,String btntxt)
    {
//        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
//        DialogBuilder.setIcon(R.mipmap.ic_launcher);
//        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = li.inflate(R.layout.dialog_confirm, null, false);
//
//
////        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
////        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
//        mTxtHeader = (TextView) v.findViewById(R.id.txtTitle);
//        mBtnClose = (Button) v.findViewById(R.id.btnClose);
//        mBtnOk = (Button)v.findViewById(R.id.btnOk);
//
////        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
//        mTxtHeader.setText("ยืนยัน");
//        mTxtMsg.setText(msg);
//
//        DialogBuilder.setView(v);
//
//        mBtnClose.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                DialogBuilder.dismiss();
//            }
//        });
//
//        mBtnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                barcodeScannerView.pause();
//                getConfirmData();
//                DialogBuilder.dismiss();
//            }
//        });
//
//        DialogBuilder.show();



        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_message_confirm, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnok);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);

//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mmTxtMsg.setTypeface(tf);
//        mmTxtTitle.setTypeface(tf);
//        mmBtnClose.setTypeface(tf);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText(getResources().getString(R.string.app_name));
        mmTxtMsg.setText(msg);
        mmBtnOk.setText(btntxt);

        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                barcodeScannerView.pause();
                getConfirmData();
                DialogBuilder.dismiss();
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }



    public void showScanDialog(String msg)
    {
//        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
//        final AlertDialog alert = DialogBuilder.create();
//        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = li.inflate(R.layout.dialog_scan_orders_check, null, false);
//
//        lv = (RecyclerView)v.findViewById(R.id.lv);
//        lv.setLayoutManager(new LinearLayoutManager(this));
//        lv.setHasFixedSize(true);
//        getScanData();
//
//
//        //mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
//
//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        //mTxtMsg.setTypeface(tf);
//        //mTxtMsg.setText(msg);
//
//        DialogBuilder.setView(v);
//        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
//            }
//        });
//        DialogBuilder.show();

        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_check_scan_order, null, false);

        lv = (RecyclerView)v.findViewById(R.id.lv);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setHasFixedSize(true);
        getScanData();


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);

//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mmTxtMsg.setTypeface(tf);
//        mmTxtTitle.setTypeface(tf);
//        mmBtnClose.setTypeface(tf);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText(getResources().getString(R.string.app_name));
        //mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }


    private void getScanData(){
        try {

            // TODO Call Webservice
            if(chkNetwork.isConnectionAvailable(getApplicationContext())){
                if(chkNetwork.isWebserviceConnected(getApplicationContext())){
                    serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/CheckOrderScan";
                    new getScanDataInAsync().execute(serverUrl);
                } else
                {
                    showMsgDialog(getResources().getString(R.string.error_webservice));

                }
            }

            else {
                showMsgDialog("Can't Connect Network");
            }

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

    private String LoadData(String oCarton){
        String _result = "0";
        try {

            // TODO Call Webservice
            if(chkNetwork.isConnectionAvailable(getApplicationContext())){

                if(chkNetwork.isWebserviceConnected(getApplicationContext())){
                    //use call api
                    serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/OrderSummary";
                    Gson gson = new Gson();
                    String json = gson.toJson(mBookingReq);
                    String result = new WebServiceHelper().postServiceAPI(serverUrl,json);
                    Log.i("Booking", result.toString());

                    //convert json to obj
                    BookingResponse obj = gson.fromJson(result,BookingResponse.class);
                    if(obj.getResponseCode().equals("0")){
                        showMsgDialog("มีการสแกนซ้ำ !!!!! ");
                    }else {
                        mListOrderSum.clear();
                        if(obj.getOrderSummary() != null){
                            for(int i=0; i<obj.getOrderSummary().size();i++){

                                OrderSummary f = new OrderSummary();
                                f.setInvoiceno(obj.getOrderSummary().get(i).getInvoiceno());
                                f.setDeliveryDate(obj.getOrderSummary().get(i).getDeliveryDate().toString());
                                f.setTruckNo(obj.getOrderSummary().get(i).getTruckNo().toString());
                                f.setCartonQty(obj.getOrderSummary().get(i).getCartonQty());
                                f.setBags(obj.getOrderSummary().get(i).getBags());
                                f.setTotal(obj.getOrderSummary().get(i).getTotal());
                                mListOrderSum.add(f);

                            }

                            mTxtOrderSum.setText(String.valueOf(mListOrderSum.get(0).getInvoiceno()));
                            mTxtBoxSum.setText(String.valueOf(mListOrderSum.get(0).getCartonQty()));
                            mTxtBagSum.setText(String.valueOf(mListOrderSum.get(0).getBags()));
                            mTxtBoxBagSum.setText(String.valueOf(mListOrderSum.get(0).getTotal()));

                        }
                    }
                }
            }

            else {
                showMsgDialog("Can't Connect Network");
            }

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }


        return _result;

    }


    private String InsertData(String oCarton){
        String _result = "0";
        try {

            // TODO Call Webservice
            if(chkNetwork.isConnectionAvailable(getApplicationContext())){

                if(chkNetwork.isWebserviceConnected(getApplicationContext())){
                    //use call api
                    serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/CreateBooking";
                    mBookingReq.setCartonNo(oCarton);
                    Gson gson = new Gson();
                    String json = gson.toJson(mBookingReq);
                    String result = new WebServiceHelper().postServiceAPI(serverUrl,json);
                    Log.i("Booking", result.toString());

                    //convert json to obj
                    BookingResponse obj = gson.fromJson(result,BookingResponse.class);
                    if(obj.getResponseCode().equals("0")){
                        showMsgDialog("Error !! "+obj.getResponseMessage());
                    }else {
                        mListOrderSum.clear();
                        if(obj.getOrderSummary() != null){
                            for(int i=0; i<obj.getOrderSummary().size();i++){

                                OrderSummary f = new OrderSummary();
                                f.setInvoiceno(obj.getOrderSummary().get(i).getInvoiceno());
                                f.setDeliveryDate(obj.getOrderSummary().get(i).getDeliveryDate().toString());
                                f.setTruckNo(obj.getOrderSummary().get(i).getTruckNo().toString());
                                f.setCartonQty(obj.getOrderSummary().get(i).getCartonQty());
                                f.setBags(obj.getOrderSummary().get(i).getBags());
                                f.setTotal(obj.getOrderSummary().get(i).getTotal());
                                mListOrderSum.add(f);

                            }

                            mTxtOrderSum.setText(String.valueOf(mListOrderSum.get(0).getInvoiceno()));
                            mTxtBoxSum.setText(String.valueOf(mListOrderSum.get(0).getCartonQty()));
                            mTxtBagSum.setText(String.valueOf(mListOrderSum.get(0).getBags()));
                            mTxtBoxBagSum.setText(String.valueOf(mListOrderSum.get(0).getTotal()));

                        }
                    }
                }
            }

            else {
                showMsgDialog("Can't Connect Network");
            }

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }


        return _result;

    }


    private void getInsertData(String oCarton){
        try {

            // TODO Call Webservice
            if(chkNetwork.isConnectionAvailable(getApplicationContext())){
                if(chkNetwork.isWebserviceConnected(getApplicationContext())){
                    serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/CreateBooking";
                    new getInsertDataInAsync().execute(serverUrl,oCarton);


                }

            }

            else {
                showMsgDialog("Can't Connect Network");
            }

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }

    }

    private class getInsertDataInAsync extends AsyncTask<String, Void, PageResultHolder>
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
                mBookingReq.setCartonNo(params[1]);
                Gson gson = new Gson();
                String json = gson.toJson(mBookingReq);
                String result = new WebServiceHelper().postServiceAPI(params[0],json);
                Log.i("Booking", result.toString());

                //convert json to obj
                BookingResponse obj = gson.fromJson(result,BookingResponse.class);
                if(obj.getResponseCode().equals("0")){
                    pageResultHolder.content = "มีการสแกนซ้ำ !!!!! ";
                }else {
                    mListOrderSum.clear();
                    if(obj.getOrderSummary() != null){
                        for(int i=0; i<obj.getOrderSummary().size();i++){

                            OrderSummary f = new OrderSummary();
                            f.setInvoiceno(obj.getOrderSummary().get(i).getInvoiceno());
                            f.setDeliveryDate(obj.getOrderSummary().get(i).getDeliveryDate().toString());
                            f.setTruckNo(obj.getOrderSummary().get(i).getTruckNo().toString());
                            f.setCartonQty(obj.getOrderSummary().get(i).getCartonQty());
                            f.setBags(obj.getOrderSummary().get(i).getBags());
                            f.setTotal(obj.getOrderSummary().get(i).getTotal());
                            mListOrderSum.add(f);

                        }
                    }
                }



            } catch (Exception e) {
                pageResultHolder.content = "Exception : CheckOrderData";
                pageResultHolder.exception = e;
                //return null;
            }

            return pageResultHolder;
        }

        @Override
        protected void onPostExecute(PageResultHolder result) {
            // TODO Auto-generated method stub

            try {

                if (result.content != null) {
                    //mProgressDialog.dismiss();
                    showMsgDialog(result.content.toString());
                }
                else
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms

                            //mProgressDialog.dismiss();
                            if(mListOrderSum.size() != 0){
                                if(mListOrderSum.get(0).getInvoiceno()>0)
                                {

                                    mTxtOrderSum.setText(String.valueOf(mListOrderSum.get(0).getInvoiceno()));
                                    mTxtBoxSum.setText(String.valueOf(mListOrderSum.get(0).getCartonQty()));
                                    mTxtBagSum.setText(String.valueOf(mListOrderSum.get(0).getBags()));
                                    mTxtBoxBagSum.setText(String.valueOf(mListOrderSum.get(0).getTotal()));
                                    //callToast(getResources().getString(R.string.txt_text_scan_yes));

                                }else
                                {
                                    callToast(getResources().getString(R.string.txt_text_scan_no));

                                }
                            }


                        }
                    }, 200);



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void getConfirmData(){
        try {

            // TODO Call Webservice
            if(chkNetwork.isConnectionAvailable(getApplicationContext())){
                serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/ConfirmOrder";
                new getConfirmDataInAsync().execute(serverUrl);
            }

            else {
                showMsgDialog("Can't Connect Network");
            }

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }

    }

    private class getConfirmDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(ScanOrdersActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();

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
                String json = gson.toJson(mConfirmReq);
                String result = new WebServiceHelper().postServiceAPI(params[0],json);
                Log.i("Confirm", result.toString());

                //convert json to obj
                BaseResponse obj = gson.fromJson(result,BaseResponse.class);
                pageResultHolder.content = obj.getResponseCode();

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
                    if (result.content.equals("1"))
                    {

                        result.content = getResources().getString(R.string.txt_text_update_success);
                    }
                    else
                    {
                        result.content = "Error";
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            mProgressDialog.dismiss();
                            showMsgDialog(result.content.toString());

                        }
                    }, 200);



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void callToast (String msg)
    {
        Toast toast = Toast.makeText(this, msg,Toast.LENGTH_SHORT);
        toast.show();
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
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 111: {
//                for (int i = 0; i < permissions.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
//
//
//                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
//
//                    }
//                }
//            }
//            break;
//            default: {
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            }
//        }
//    }
//


    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pauseAndWait();
    }
}

package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class UnpackListActivity extends AppCompatActivity {


    private ACProgressFlower mProgressDialog;
    private TextView mTxtMsg,mTxtHeader;
    private Button mBtnBack;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Unpack> mListOrderData = new ArrayList<Unpack>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpack_list);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {

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
            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_unpack_list));

            //listview
            lv = (RecyclerView) findViewById(R.id.lv);
            lv.setLayoutManager(new LinearLayoutManager(this));
            lv.setHasFixedSize(true);
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
            getInit();
            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    //previewing = false;
                    //mCamera.setPreviewCallback(null);
                    //mCamera.stopPreview();

                    finish();

                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

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

    private void getInit() {

        try {

            new getInitDataInAsync().execute();

           /* if(chkNetwork.isConnectionAvailable(getApplicationContext()))
            {

                if(chkNetwork.isWebserviceConnected(getApplicationContext()))
                {

                    new getOrderDataInAsync().execute();
                }
                else
                {

                    showMsgDialog(getResources().getString(R.string.error_webservice));

                }

            }else
            {

                showMsgDialog(getResources().getString(R.string.error_network));
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class getInitDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(UnpackListActivity.this)
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
                mListOrderData.clear();

                Unpack f = new Unpack();
                f.setFscode("11111");
                f.setFsname("ชื่อสินค้ารายการที่ 1");
                f.setFsunit("1");
                mListOrderData.add(f);

                /*String xmlResult = webHelper.checkTruckno(truckNo);
                pageResultHolder.content = xmlResult;*/

                //SoapObject responseorder = new SoapObject();
                //responseorder = webHelper.getOrderIntransit(ogject.getDate().toString(),ogject.getTruck().toString(),"0");
                //responseorder = webHelper.checkOrderData("1100996628");



                /*mListOrderData.clear();
                for(int i=0; i<responseorder.getPropertyCount();i++){

                    SoapObject array = (SoapObject)responseorder.getProperty(i);

                    OrderData item = new OrderData();
                    item.setRepcode(array.getPrimitivePropertySafelyAsString("repcode"));
                    item.setRepname(array.getPrimitivePropertySafelyAsString("repname"));
                    item.setRepaddr1(array.getPrimitivePropertySafelyAsString("repaddr1"));
                    item.setRepaddr2(array.getPrimitivePropertySafelyAsString("repaddr2"));
                    item.setRepaddr3(array.getPrimitivePropertySafelyAsString("repaddr3"));
                    item.setRepaddr4(array.getPrimitivePropertySafelyAsString("repaddr4"));
                    item.setRepphone(array.getPrimitivePropertySafelyAsString("repphone"));
                    item.setRepmobile(array.getPrimitivePropertySafelyAsString("repmobile"));
                    item.setInvoiceno(array.getPrimitivePropertySafelyAsString("invoiceno"));
                    item.setCarton(array.getPrimitivePropertySafelyAsString("carton"));
                    item.setBag(array.getPrimitivePropertySafelyAsString("bag"));
                    item.setOtrstatus(array.getPrimitivePropertySafelyAsString("otrstatus"));
                    item.setOtrdeliverystatus(array.getPrimitivePropertySafelyAsString("otrdeliverystatus"));

*//*                    if(array.getPrimitivePropertySafelyAsString("otrdeliverystatus").equals("05"))
                    {
                        mDelCount = mDelCount+1;
                    }*//*

                    mListOrderData.add(item);

                }*/




            } catch (Exception e) {
                pageResultHolder.content = "Exception : CheckOrderData";
                pageResultHolder.exception = e;
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

                            mProgressDialog.dismiss();

                            if(mListOrderData.size()>0)
                            {

                                mAdapter = new UnpackViewAdapter(getApplicationContext(),mListOrderData);
                                lv.setAdapter(mAdapter);


                                //ogject.setListCheckOrderData(mListOrderData);

                                //bindData();

                               /* mAdapter = new RVGetOrderAdapter(getApplicationContext(),mListOrderData);
                                lv.setAdapter(mAdapter);


                                mTxtCount.setText(mDelCount+"/"+String.valueOf(mListOrderData.size()));*/



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

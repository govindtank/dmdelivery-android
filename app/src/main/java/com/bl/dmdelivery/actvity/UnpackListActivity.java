package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.LoadUnpackResponse;
import com.bl.dmdelivery.model.OrderScanReq;
import com.bl.dmdelivery.model.Unpack;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class UnpackListActivity extends AppCompatActivity {


    private ACProgressFlower mProgressDialog;
    private TextView mTxtMsg,mTxtHeader,mTxtCode,mTxtDesc,mTxtW,mTxtL,mTxtH,mmTxtMsg,mmTxtTitle,mmTxtsum;
    private ImageView mImageView,mmImvTitle;
    private Button mBtnBack,mBtnClose,mBtnLoad,mmBtnOk,mmBtnClose;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Unpack> mListOrderData = new ArrayList<Unpack>();
    private CheckNetwork chkNetwork = new CheckNetwork();
    private String serverUrl;
    private OrderScanReq mLoadOrderReq;
    private SharedPreferences sp;
    private int itemsCount=0;
    private int itemsSumQty=0;

    DBHelper mHelper;
    SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpack_list);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);


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
            mBtnLoad = (Button)findViewById(R.id.btnMenu);



            //textbox
            mmTxtsum = (TextView) findViewById(R.id.txtsum);
            mmTxtsum.setText("จำนวนนอกกล่องรวม: 0");

            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_unpack_list));



            //listview
            lv = (RecyclerView) findViewById(R.id.lv);
            lv.setLayoutManager(new LinearLayoutManager(this));
            lv.setHasFixedSize(true);

            mLoadOrderReq = new OrderScanReq();
            mLoadOrderReq.setTruckNo(sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, ""));
            mLoadOrderReq.setDeliveryDate(sp.getString(TagUtils.PREF_DELIVERY_DATE, ""));
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

            mBtnLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMsgConfirmDialog("โหลดข้อมูลสินค้านอกกล่อง");
                }
            });



            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mListOrderData.get(position).getUnpack_code().toString().equals("")){
                        return;
                    }
                    showUnpackDialog(mListOrderData.get(position));
                }
            }));

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    public void showMsgConfirmDialog(String msg)
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_confirm, null, false);


        mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);

        mmTxtTitle.setText("ยืนยัน");
        mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loadData();
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

    private void loadData() {

        try {

            if(chkNetwork.isConnectionAvailable(getApplicationContext()))
            {
                if(chkNetwork.isWebserviceConnected(getApplicationContext()))
                {

                    serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/LoadOrderUnpack";
                    new loadDataDataInAsync().execute(serverUrl);

                }
                else
                {
                    showMsgDialog(getResources().getString(R.string.error_webservice));

                }

            }else
            {
                //showDialog("ไม่สามารถเชื่อมต่อ Internet ได้ กรุณากรุณาตรวจสอบ!!!");
                showMsgDialog(getResources().getString(R.string.error_network));
            }

        } catch (Exception e) {
            //e.printStackTrace();
            showMsgDialog(e.toString());
        }
    }

    private int setSumItemQty(){
        try
        {
            itemsSumQty=0;

            if(mListOrderData.size()==0){
                return 0;
            }

            for(int i=0; i<mListOrderData.size();i++){
                if(!mListOrderData.get(i).getUnpack_qty().isEmpty()){
                    itemsSumQty = itemsSumQty + Integer.parseInt(mListOrderData.get(i).getUnpack_qty().toString());
                }
            }

            return  itemsSumQty;
        }catch (Exception e) {
            //e.printStackTrace();
            showMsgDialog(e.toString());
        }
        return  0;
    }

    private class loadDataDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(UnpackListActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .text(getResources().getString(R.string.progress_loading))
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

                mHelper = new DBHelper(getApplicationContext());
                mDb = mHelper.getWritableDatabase();
                mHelper.onUpgradeUnpack(mDb,0,0);

                //mDb.execSQL("DELETE FROM " + DBHelper.TableOrder + ";");

                Gson gson = new Gson();
                String json = gson.toJson(mLoadOrderReq);
                String result = new WebServiceHelper().postServiceAPI(params[0],json);
                Log.i("LoginResult", result.toString());

//                //convert json to obj
                LoadUnpackResponse obj = gson.fromJson(result,LoadUnpackResponse.class);


                ArrayList<Unpack> unpacks = new ArrayList<Unpack>();
                // mListLoadOrder. =new LoadOrderResponse();


                if (obj.getResponseCode().equals("1"))
                {

                    for(int i=0; i<obj.getUnpack().size();i++){
                        Unpack f = new Unpack();
                        f.setTransno(obj.getUnpack().get(i).getTransno().toString());
                        f.setUnpack_code(obj.getUnpack().get(i).getUnpack_code().toString());
                        f.setUnpack_desc(obj.getUnpack().get(i).getUnpack_desc().toString());
                        f.setUnpack_qty(obj.getUnpack().get(i).getUnpack_qty().toString());
                        f.setUnpack_image(obj.getUnpack().get(i).getUnpack_image().toString());
                        unpacks.add(f);

                        mHelper.addUnpack(f);
                    }
                }

                pageResultHolder.content = obj.getResponseCode();

               /* else
                {

                }*/

            } catch (Exception e) {
                pageResultHolder.content = "Exception : NoData";
                pageResultHolder.exception = e;
            }

            return pageResultHolder;
        }

        @Override
        protected void onPostExecute(final PageResultHolder result) {
            // TODO Auto-generated method stub

            //final String msg = "";

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
                        result.content = getResources().getString(R.string.error_data_not_in_system);
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms

                            mProgressDialog.dismiss();

                            showMsgDialog(result.content.toString());
                            getInit();

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


    public void showMsgDialog(String msg)
    {
//        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
//        final AlertDialog alert = DialogBuilder.create();
//        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = li.inflate(R.layout.dialog_message, null, false);
//
//        mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
//        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
//        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
//
//        mmTxtTitle.setText("ยืนยัน");
//        mmTxtMsg.setText(msg);
//
//        DialogBuilder.setView(v);
//
//        mmBtnOk.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                loadData();
//                DialogBuilder.dismiss();
//            }
//        });
//
//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mTxtMsg.setTypeface(tf);
//        mTxtMsg.setText(msg);
//
//        DialogBuilder.setView(v);
////        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
////            public void onClick(DialogInterface dialog, int which) {
////
////                dialog.dismiss();
////            }
////        });
//        DialogBuilder.show();

        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_message, null, false);

        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);


        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
        mmTxtMsg.setTypeface(tf);
        mmTxtTitle.setTypeface(tf);
        mmBtnClose.setTypeface(tf);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText("DM Delivery");
        mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);


        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }



    private void getInit() {

        try {

            new getInitDataInAsync().execute();

//           if(chkNetwork.isConnectionAvailable(getApplicationContext()))
//            {
//
//                if(chkNetwork.isWebserviceConnected(getApplicationContext()))
//                {
//
//                    new getInitDataInAsync().execute();
//                }
//                else
//                {
//
//                    showMsgDialog(getResources().getString(R.string.error_webservice));
//
//                }
//
//            }else
//            {
//
//                showMsgDialog(getResources().getString(R.string.error_network));
//            }
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

                itemsCount=0;
                itemsSumQty=0;

                mHelper = new DBHelper(getApplicationContext());
                mListOrderData.clear();
                mListOrderData = mHelper.getUnpackList();

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
                    mmTxtsum.setText("จำนวนนอกกล่องรวม: 0");

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
                                mmTxtsum.setText("จำนวนนอกกล่องรวม: " + setSumItemQty());

                                mAdapter = new UnpackViewAdapter(getApplicationContext(),mListOrderData);
                                lv.setAdapter(mAdapter);
                            }else
                            {
                                mmTxtsum.setText("จำนวนนอกกล่องรวม: 0");

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

    public void showUnpackDialog(Unpack obj)
    {
        //final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alert = DialogBuilder.create();
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_show_unpack, null, false);


        mTxtCode = (TextView) v.findViewById(R.id.txtCode);
        mTxtDesc = (TextView) v.findViewById(R.id.txtDesc);
//        mTxtH = (TextView)v.findViewById(R.id.txtHeight);
//        mTxtL = (TextView)v.findViewById(R.id.txtLength);
//        mTxtW = (TextView)v.findViewById(R.id.txtWidth);
        mImageView = (ImageView)v.findViewById(R.id.imageView3);


        mTxtCode.setText(obj.getUnpack_code());
        mTxtDesc.setText(obj.getUnpack_desc());
//        mTxtH.setText("ความสูง 19 เซนติเมตร");
//        mTxtW.setText("ความกว้าง 24 เซนติเมตร");
//        mTxtL.setText("ความยาว 24 เซนติเมตร");

        byte[] decodedByteArray = Base64.decode(obj.getUnpack_image().toString(), Base64.NO_WRAP);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        mImageView.setImageBitmap(decodedBitmap);


        DialogBuilder.setView(v);

        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        DialogBuilder.show();
    }



}

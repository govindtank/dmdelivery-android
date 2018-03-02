package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SaveOrdersSlipActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mTxtRepcode,mTxtName,mTxtInv,mTxtAddress,
            mTxtMobilemsl,mTxtType,mTxtReturn,mTxtMobiledsm,
            mTxtLat,mTxtLog,mTxtCode,mTxtDesc,mTxtH,mTxtL,mTxtW;
    private Button mBtnBack,mBtnUnpack,mBtnGPS,mBtnApprove,mBtnReject,mmBtnOk,mmBtnClose,mBtnMenu,mBtnSave,mBtnLoc,mBtnNotsave,mBtnclose;
    private ACProgressFlower mProgressDialog;
    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Unpack> mListOrderData = new ArrayList<Unpack>();
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";


    private ListView lv2;
    private ListView lvinv;
    private String[] sigReturncancellist;
    private String[] sigInvlist;

    private Intent myIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_slip);

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
//            mImvHeader = (ImageView) findViewById(R.id.imvHeader);
//            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
//            mBtnBack = (Button) findViewById(R.id.btnBack);
//            mBtnRefrach = (Button) findViewById(R.id.btnRefrach);
//            mBtnSelectall = (Button) findViewById(R.id.btnSelectall);
//            mBtnSign = (Button) findViewById(R.id.btnSign);
//
//            mTxtOrderCount = (TextView) findViewById(R.id.txtOrderCount);
//            mTxtCount = (TextView) findViewById(R.id.txtCount);


            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnUnpack = (Button) findViewById(R.id.btnUnpack);
            mBtnGPS= (Button) findViewById(R.id.btnGPS);
            mBtnApprove = (Button) findViewById(R.id.btnApprove);
            mBtnReject = (Button) findViewById(R.id.btnReject);


//            mBtnMenu = (Button) findViewById(R.id.btnMenu);
//            mBtnSave  = (Button) findViewById(R.id.btnSave);
//            mBtnLoc = (Button)  findViewById(R.id.btnLoc);
//            mBtnUnpack = (Button)findViewById(R.id.btnUnpack);
//            mBtnNotsave = (Button)findViewById(R.id.btnNotsave);


//
//            //textbox
//            mTxtRepcode = (TextView)findViewById(R.id.txtRepcode);
//            mTxtName = (TextView)findViewById(R.id.txtName);
//            mTxtInv = (TextView)findViewById(R.id.txtInv);
//            mTxtAddress = (TextView)findViewById(R.id.txtAddress);
//            mTxtMobilemsl = (TextView)findViewById(R.id.txtMobilemsl);
//            mTxtMobiledsm = (TextView)findViewById(R.id.txtMobiledsm);
//            mTxtType = (TextView)findViewById(R.id.txtTypeInv);
//            mTxtReturn = (TextView)findViewById(R.id.txtReturn);
//            mTxtLat = (TextView)findViewById(R.id.txtLat);
//            mTxtLog = (TextView)findViewById(R.id.txtLog);
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_slip_details));


            // Create the arrays
            sigInvlist = getResources().getStringArray(R.array.invList);

            lvinv = (ListView) findViewById(R.id.lv);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sigInvlist);
            lvinv.setAdapter(adapter);
        }
        catch (Exception e) {
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

//            mTxtRepcode.setText("0096061405");
//            mTxtName.setText("คุุณชุฑาทรัพย์ อ่อนสี");
//            mTxtInv.setText("1103842117");
//            mTxtAddress.setText("16/69 เทียนทะเลา 26 แยก 2 แขวงแสมดำ กรุงเทพมหานคร 10150");
//            mTxtMobilemsl.setText("MSL : 028973435");
//            mTxtType.setText("1C");
//            mTxtReturn.setText("R");
//            mTxtMobiledsm.setText("DSM : 0818159347");
//            mTxtLat.setText("Lat=14.0302164");
//            mTxtLog.setText("Log=10.364450");


            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

            mBtnUnpack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showUnpacklistDialog("");
                }
            });

            mBtnGPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            mBtnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();

                    myIntent = new Intent(getApplicationContext(), SaveOrdersApproveSlipActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            mBtnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMsgCancelSelectedSingleDialog();
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

    public void showUnpackDialog(String msg)
    {
        //final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alert = DialogBuilder.create();
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_show_unpack, null, false);


        mTxtCode = (TextView) v.findViewById(R.id.txtCode);
        mTxtDesc = (TextView) v.findViewById(R.id.txtDesc);
        mTxtH = (TextView)v.findViewById(R.id.txtHeight);
        mTxtL = (TextView)v.findViewById(R.id.txtLength);
        mTxtW = (TextView)v.findViewById(R.id.txtWidth);


        mTxtCode.setText("66666");
        mTxtDesc.setText("หม้อหุงข้าวขนาดใหญ่ 1 ลิตร SMARTHOME");
        mTxtH.setText("ความสูง 19 เซนติเมตร");
        mTxtW.setText("ความกว้าง 24 เซนติเมตร");
        mTxtL.setText("ความยาว 24 เซนติเมตร");


        DialogBuilder.setView(v);

        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        DialogBuilder.show();
    }

//    public void showReasonDialog(String msg)
//    {
//
////        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
////        //final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
////        //final AlertDialog alert = DialogBuilder.create();
////        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        View v = li.inflate(R.layout.dialog_reason, null, false);
////
////        mBtnclose = (Button)v.findViewById(R.id.btnClose);
////        mBtnclose.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                DialogBuilder.dismiss();
////            }
////        });
////        DialogBuilder.setView(v);
////        DialogBuilder.show();
//
//    }


    public void showMsgCancelSelectedSingleDialog()
    {
        // Create the arrays
        sigReturncancellist = getResources().getStringArray(R.array.returncancellist);

        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersSlipActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_save_orders_return_cancel, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("ยืนยันการยกเลิกส่งสืนค้า");


        lv2 = (ListView) v.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,sigReturncancellist);
        lv2.setAdapter(adapter);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();

                finish();

                myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
                startActivity(myIntent);
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        // Set item click listener
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = sigReturncancellist[position];
                Toast.makeText(SaveOrdersSlipActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        DialogBuilder.show();
    }


    public void showUnpacklistDialog(String msg)
    {
        //final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alert = DialogBuilder.create();
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_list_unpack, null, false);

        lv = (RecyclerView)v.findViewById(R.id.lv);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setHasFixedSize(true);
        lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showUnpackDialog("");
            }
        }));
        getInit();

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

           /* mProgressDialog = new ACProgressFlower.Builder(SaveOrdersSlipActivity.this)
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
                mListOrderData.clear();

                Unpack f = new Unpack();
                f.setUnpack_code("11111");
                f.setUnpack_desc("ชื่อสินค้ารายการที่ 1");
                f.setUnpack_qty("1");
                mListOrderData.add(f);

                f = new Unpack();
                f.setUnpack_code("22222");
                f.setUnpack_desc("ชื่อสินค้ารายการที่ 2");
                f.setUnpack_qty("151");
                mListOrderData.add(f);



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
                    //mProgressDialog.dismiss();
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

                            if(mListOrderData.size()>0)
                            {

                                mAdapter = new UnpackViewAdapter(getApplicationContext(),mListOrderData);
                                lv.setAdapter(mAdapter);




                            }else
                            {

                                showMsgDialog(getResources().getString(R.string.error_data_not_in_system));

                            }

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



}

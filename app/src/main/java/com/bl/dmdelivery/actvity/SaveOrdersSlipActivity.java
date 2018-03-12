package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrdersChangeListViewAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.OrdersChangeList;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SaveOrdersSlipActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mtxtDescSzie,mTxtRepcode,mTxtName,mTxtInv,mTxtAddress,
            mTxtMobilemsl,mTxtType,mTxtReturn,mTxtMobiledsm,
            mTxtLat,mTxtLog,mTxtCode,mTxtDesc,mTxtH,mTxtL,mTxtW,mTxtSum;
    private Button mBtnBack,mBtnUnpack,mBtnGPS,mBtnApprove,mBtnReject,mmBtnOk,mmBtnClose,mBtnMenu,mBtnSave,mBtnLoc,mBtnNotsave,mBtnclose;
    private ImageView mImageView;
    private ACProgressFlower mProgressDialog;

    private RecyclerView lvUnpack;
    private RecyclerView lvOrderList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapterOrdersList;
    private RecyclerView.Adapter mAdapterDeliveryRejectList;

    private ArrayList<Unpack> mListOrderData = new ArrayList<Unpack>();
    private ArrayList<OrdersChangeList> mOrdersChangeList = new ArrayList<OrdersChangeList>();
    private ArrayList<Reason> mDeliveryRejectList = new ArrayList<Reason>();

    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;
    SQLiteDatabase mDb;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
    private String sigInvMulti="";
    private String sigInvSingle="";

    private ListView lvDeliveryReject;
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
//            mBtnGPS= (Button) findViewById(R.id.btnGPS);
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
            mTxtSum = (TextView) findViewById(R.id.txtsum);
            mTxtSum.setText("จำนวนรายการที่เลือกจัดส่ง: 0 รายการ");

            //listview orders list
            lvOrderList = (RecyclerView)findViewById(R.id.lvOrderList);
            lvOrderList.setLayoutManager(new LinearLayoutManager(this));
            lvOrderList.setHasFixedSize(true);

//            // Create the arrays
//            sigInvlist = getResources().getStringArray(R.array.invList);
//
//            lvinv = (ListView) findViewById(R.id.lv);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sigInvlist);
//            lvinv.setAdapter(adapter);
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
            getInit();

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


            Intent intInv= getIntent();
            Bundle bdlInv = intInv.getExtras();

            if(bdlInv != null)
            {
                sigInvMulti =(String)bdlInv.get("inv");
            }

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

            mBtnUnpack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showUnpacklistDialog("");


                    //save val to variable
                    sigInvSingle = sigInvMulti;

                    //get user selected Unpack List
                    new getUnpackDataInAsync().execute();
                }
            });

//            mBtnGPS.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });

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


            lvOrderList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mOrdersChangeList.get(position).getTransNo().isEmpty()){
                        return;
                    }

                    //save val to variable
                    sigInvSingle="'" + mOrdersChangeList.get(position).getTransNo() + "'";

                    //get user selected Unpack List
                    new getUnpackDataInAsync().execute();
                }
            }));

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

    public void showUnpackDialog(int position)
    {
        //final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alert = DialogBuilder.create();
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_show_unpack, null, false);


        mTxtCode = (TextView) v.findViewById(R.id.txtCode);
        mTxtDesc = (TextView) v.findViewById(R.id.txtDesc);
        mImageView = (ImageView)v.findViewById(R.id.imageView3);
        mtxtDescSzie = (TextView)v.findViewById(R.id.txtDescSzie);
        mTxtH = (TextView)v.findViewById(R.id.txtHeight);
        mTxtL = (TextView)v.findViewById(R.id.txtLength);
        mTxtW = (TextView)v.findViewById(R.id.txtWidth);

        mTxtCode.setText(mListOrderData.get(position).getUnpack_code());
        mTxtDesc.setText(mListOrderData.get(position).getUnpack_desc());
        byte[] decodedByteArray = Base64.decode(mListOrderData.get(position).getUnpack_image().toString(), Base64.NO_WRAP);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        mImageView.setImageBitmap(decodedBitmap);

        mtxtDescSzie.setVisibility(View.INVISIBLE);
        mTxtH.setVisibility(View.INVISIBLE);
        mTxtW.setVisibility(View.INVISIBLE);
        mTxtL.setVisibility(View.INVISIBLE);

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
//        sigDeliveryRejectList = getResources().getStringArray(R.array.returncancellist);

        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersSlipActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_save_orders_return_cancel, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("เหตุผล/หมายเหตุ");

        lvDeliveryReject = (ListView) v.findViewById(R.id.lv);
        lvDeliveryReject.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mDeliveryRejectList.clear();
        mHelper = new DBHelper(getApplicationContext());
        mDeliveryRejectList = mHelper.getReasonListForCondition("'DELIVERY_REJECT'");

        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 0; i < mDeliveryRejectList.size();i++)
        {
            arrayList.add(mDeliveryRejectList.get(i).getReason_code() + " " + mDeliveryRejectList.get(i).getReason_desc());
        }


        if(arrayList.size() > 0)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,arrayList);
            lvDeliveryReject.setAdapter(adapter);

            //ถ้ามีข้อมูลบน ListView ให้เลือกรายการแรกเสมอ
            lvDeliveryReject.setItemChecked(0,true);
        }

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

//        // Set item click listener
//        lvDeliveryReject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String description = sigDeliveryRejectList[position];
//                Toast.makeText(SaveOrdersSlipActivity.this, description, Toast.LENGTH_SHORT).show();
//            }
//        });

        DialogBuilder.show();
    }


    public void showUnpacklistDialog()
    {
        //final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alert = DialogBuilder.create();
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_list_unpack, null, false);

        lvUnpack = (RecyclerView)v.findViewById(R.id.lv);
        lvUnpack.setLayoutManager(new LinearLayoutManager(this));
        lvUnpack.setHasFixedSize(true);
        lvUnpack.setAdapter(mAdapter);

        lvUnpack.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showUnpackDialog(position);
            }
        }));

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
            if(chkNetwork.isConnectionAvailable(getApplicationContext()))
            {
                if(chkNetwork.isWebserviceConnected(getApplicationContext()))
                {
                    //get user selected Order List
                    new getOrdersDataInAsync().execute();
                }
                else
                {
                    showMsgDialog(getResources().getString(R.string.error_webservice));
                }
            }else
            {
                showMsgDialog(getResources().getString(R.string.error_network));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load user selected Order List
    private class getOrdersDataInAsync extends AsyncTask<String, Void, PageResultHolder> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(SaveOrdersSlipActivity.this)
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
                mOrdersChangeList.clear();
                mHelper = new DBHelper(getApplicationContext());
                mOrdersChangeList = mHelper.getOrderChangeList(sigInvMulti);

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
                    mTxtSum.setText("จำนวนรายการที่เลือกจัดส่ง: 0 รายการ");
                    mProgressDialog.dismiss();
                    Toast.makeText(SaveOrdersSlipActivity.this,result.exception.toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            mProgressDialog.dismiss();
                            if(mOrdersChangeList.size()>0)
                            {
                                mAdapterOrdersList = new OrdersChangeListViewAdapter(getApplicationContext(),mOrdersChangeList);
                                lvOrderList.setAdapter(mAdapterOrdersList);

                                mTxtSum.setText("จำนวนรายการที่เลือกจัดส่ง: " + mOrdersChangeList.size() + " รายการ");
                            }else
                            {
                                mTxtSum.setText("จำนวนรายการที่เลือกจัดส่ง: 0 รายการ");
//                                Toast.makeText(SaveOrdersSlipActivity.this, getResources().getString(R.string.error_data_not_in_system), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //load user selected unpack List
    private class getUnpackDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           mProgressDialog = new ACProgressFlower.Builder(SaveOrdersSlipActivity.this)
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
                mHelper = new DBHelper(getApplicationContext());
                mListOrderData = mHelper.getUnpackListForInvCustom(sigInvSingle);

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
                    Toast.makeText(SaveOrdersSlipActivity.this,result.exception.toString(), Toast.LENGTH_SHORT).show();
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
//                                lvUnpack.setAdapter(mAdapter);

                                showUnpacklistDialog();
                            }else
                            {
//                                Toast.makeText(SaveOrdersSlipActivity.this,getResources().getString(R.string.error_data_not_in_system), Toast.LENGTH_SHORT).show();
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

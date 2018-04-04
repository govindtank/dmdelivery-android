package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SaveOrdersReturnActivity extends AppCompatActivity {

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


                //บันทึกข้อมูล รับไม่ได้
                mHelper = new DBHelper(getApplicationContext());
                mOrderReturnSaveData = new OrderReturn();
                mOrderReturnSaveData.setReturn_no(ref_return_no);
                mOrderReturnSaveData.setRep_code(ref_rep_code);
                mOrderReturnSaveData.setReturn_status("2");
                mOrderReturnSaveData.setReason_code(sigReson_code);
                mOrderReturnSaveData.setReturn_note(sigNote);
                mOrderReturnSaveData.setReturn_unit_real("0");
                mOrderReturnSaveData.setFullpathimage("");
                mOrderReturnSaveData.setLat("");
                mOrderReturnSaveData.setLon("");
                mOrderReturnSaveData.setSignature_timestamp(formattedDate);
                mHelper.updateOrderReturnDetails(mOrderReturnSaveData);


                //update order
                mHelper = new DBHelper(getApplicationContext());
                mHelper.updateOrdersStatus(mListOrder,"W");


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


}

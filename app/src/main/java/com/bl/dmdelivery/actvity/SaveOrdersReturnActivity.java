package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrderReturnDtlViewAdapter;
import com.bl.dmdelivery.adapter.OrderReturnViewAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.utility.TagUtils;

import java.util.ArrayList;

public class SaveOrdersReturnActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mTxtsum,mmTxtQty;
    private Button mBtnBack,mmBtnOk,mmBtnClose,mBtnPlus,mBtnDel,mBtnConfirm,mBtnCheckScan;
    private ImageView mmImvTitle;
    private EditText medtNote;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ArrayList<Reason> mReturnAcceptRejectList = new ArrayList<Reason>();
    private ArrayList<String> arrayListReason = new ArrayList<String>();

    private String[] sigReturnList;
    private String[] sigReturncancellist;

    private String mSelectReson = "";
    private Integer mSelectResonIndex = 0;
    private Intent myIntent=null;

    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;

    private String ref_rep_code;
    private String ref_return_no;

    private int intQTY_UINT_REAL= 0;
    private int intQTY_UINT= 0;
    private String sigNote="";



    private ArrayList<OrderReturn> mListOrderReturn = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListOrderReturnSaveData = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListOrderReturnGeOnResume = new ArrayList<OrderReturn>();
    private OrderReturn mOrderReturnSaveData = null;
    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

    private ListView lvReturnAcceptRejectList;
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

        if(!isResumeState)
        {
            //มาจากหน้า slip
            Toast toast = Toast.makeText(SaveOrdersReturnActivity.this, "onResume - Slip2", Toast.LENGTH_SHORT);
            toast.show();


            String sigBackToPage = sp.getString(TagUtils.PREF_BACK_TO_PAGE, "");

            if (sigBackToPage.toUpperCase()=="SAVE_TO_PAGE")
            {
                finish();
            }
            else
            {

            }
        }
        else
        {
            //มาจากหน้าใบคืน
            Toast toast = Toast.makeText(SaveOrdersReturnActivity.this, "onResume - returnDoc", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void bindWidget()
    {
        try{

            Intent ineGetIntent= getIntent();
            Bundle bdlGetExtras= ineGetIntent.getExtras();

            if(bdlGetExtras == null) {
                mOrderReturnSaveData = new OrderReturn();
                mListOrderReturnSaveData.clear();

                ref_return_no= null;
                ref_rep_code= null;
            } else {
                mOrderReturnSaveData = new OrderReturn();
                mOrderReturnSaveData =(OrderReturn)bdlGetExtras.get("data");

                mListOrderReturnSaveData.clear();
                mListOrderReturnSaveData = (ArrayList<OrderReturn>)bdlGetExtras.get("dataAll");


                ref_return_no= mOrderReturnSaveData.getReturn_no();
                ref_rep_code= mOrderReturnSaveData.getRep_code();
            }

            isResumeState = true;

            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
            mBtnCheckScan = (Button) findViewById(R.id.btnCheckScan);

            mTxtsum = (TextView) findViewById(R.id.txtsum);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);


            // Create the arrays
            sigReturnList = getResources().getStringArray(R.array.returnList);

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
            mReturnAcceptRejectList.clear();
            mHelper = new DBHelper(getApplicationContext());
            mReturnAcceptRejectList = mHelper.getReasonListForCondition("'RETURN_REJECT'");


            for(int i = 0; i < mReturnAcceptRejectList.size();i++)
            {
                arrayListReason.add(mReturnAcceptRejectList.get(i).getReason_code() + " " + mReturnAcceptRejectList.get(i).getReason_desc());
            }

            if(arrayListReason.size() > 0)
            {
                mSelectReson =  mReturnAcceptRejectList.get(0).getReason_desc();
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

                    isResumeState = false;

                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnSlipActivity.class);
                    myIntent.putExtra("datareturn", mOrderReturnSaveData);
                    myIntent.putExtra("datareturnAll", mListOrderReturnSaveData);
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


//    private boolean isSaveOrderReturnAllComplete(){
//        try{
//            mHelper = new DBHelper(getApplicationContext());
//            mListOrderReturnGeOnResume.clear();
//            mListOrderReturnGeOnResume = mHelper.getOrderReturnCriteria("'" + ref_return_no + "'");
//
//            for(int i=0;i < mListOrderReturnGeOnResume.size(); i++)
//            {
//                if (mListOrderReturnGeOnResume.get(i).getReturn_status() == "0")
//                {
//                    return false;
//                }
//            }
//            return true;
//        }
//        catch (Exception e)
//        {
//        }
//        return  false;
//    }


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
        else
        {
            if(intQTY_UINT_REAL == 0)
            {
                intQTY_UINT_REAL = intQTY_UINT;
            }
            else
            {
                intQTY_UINT_REAL = Integer.parseInt(sigQty_uint_real_final);
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

//                Toast.makeText(SaveOrdersReturnActivity.this, "intQTY_UINT_REAL : " + String.valueOf(intQTY_UINT_REAL) + " intQTY_UINT : " + String.valueOf(intQTY_UINT), Toast.LENGTH_SHORT).show();
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

//                Toast.makeText(SaveOrdersReturnActivity.this, "intQTY_UINT_REAL : " + String.valueOf(intQTY_UINT_REAL) + " intQTY_UINT : " + String.valueOf(intQTY_UINT), Toast.LENGTH_SHORT).show();
            }
        });

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();

                //update qty on row
                mHelper = new DBHelper(getApplicationContext());
                OrderReturn mOrderReturn = new OrderReturn();
                mOrderReturn.setRep_code(ref_rep_code);
                mOrderReturn.setReturn_no(ref_return_no);
                mOrderReturn.setFs_code(sigFs_code_final);
                mOrderReturn.setReturn_unit_real(mmTxtQty.getText().toString());
                mHelper.updateOrderReturnDtl(mOrderReturn);

                //call init
                getInit();
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
        View v = li.inflate(R.layout.dialog_reason_save_order, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnok);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);
        medtNote = (EditText) v.findViewById(R.id.edtNote);


        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText(getResources().getString(R.string.txt_text_reason_remark));
        //mmTxtMsg.setText(msg);
        mmBtnOk.setText(getResources().getString(R.string.btn_text_ok));

        lvReturnAcceptRejectList = (ListView) v.findViewById(R.id.lv);
        lvReturnAcceptRejectList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if(arrayListReason.size() > 0)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,arrayListReason);
            lvReturnAcceptRejectList.setAdapter(adapter);

            //ถ้ามีข้อมูลบน ListView ให้เลือกรายการแรกเสมอ
            if(mSelectResonIndex > 0)
            {
                lvReturnAcceptRejectList.setItemChecked(mSelectResonIndex,true);
            }
            else
            {
                lvReturnAcceptRejectList.setItemChecked(0,true);
            }
        }

        lvReturnAcceptRejectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = mReturnAcceptRejectList.get(position).getReason_desc();

                sigReson_code = mReturnAcceptRejectList.get(position).getReason_code();
                mSelectReson = description;
                mSelectResonIndex = position;

                //Toast.makeText(SaveOrdersApproveSlipActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });


        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //ถ้าบันทึก ใบคืนไม่ครบ ไปหน้าใบคืน
                if(medtNote != null){
                    sigNote = medtNote.getText().toString();
                }
                else
                {
                    sigNote = "";
                }

                //บันทึกข้อมูล รับไม่ได้
                mHelper = new DBHelper(getApplicationContext());
                mOrderReturnSaveData = new OrderReturn();

                mOrderReturnSaveData.setReturn_no(ref_return_no);
                mOrderReturnSaveData.setRep_code(ref_rep_code);
                mOrderReturnSaveData.setReturn_status("2");
                mOrderReturnSaveData.setReason_code(sigReson_code);
                mOrderReturnSaveData.setReturn_note(sigNote);
                mHelper.updateOrderReturnSlip(mOrderReturnSaveData);


                //รับได้
                finish();


                //loadData();
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


}

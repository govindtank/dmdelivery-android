package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
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
import com.bl.dmdelivery.utility.TagUtils;

import java.util.ArrayList;

public class SaveOrdersReturnActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mTxtsum,mmTxtQty;
    private Button mBtnBack,mmBtnOk,mmBtnClose,mBtnPlus,mBtnDel,mBtnConfirm;
    private ImageView mmImvTitle;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    //private ListView lv;
    private String[] sigReturnList;
    private String[] sigReturncancellist;

    private Intent myIntent=null;

    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;

    private String ref_rep_code;
    private String ref_return_no;

    private int intQTY_UINT_REAL= 0;
    private int intQTY_UINT= 0;


    private ArrayList<OrderReturn> mListOrderReturn = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListOrderReturnSavDate = new ArrayList<OrderReturn>();
    private OrderReturn mOrderReturnSaveData = null;
    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

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
            bindWidget();


            setWidgetControl();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void bindWidget()
    {
        try{

            Intent ineGetIntent= getIntent();
            Bundle bdlGetExtras= ineGetIntent.getExtras();

            if(bdlGetExtras == null) {
                mOrderReturnSaveData = new OrderReturn();
                mListOrderReturnSavDate.clear();

                ref_return_no= null;
                ref_rep_code= null;
            } else {
                mOrderReturnSaveData = new OrderReturn();
                mOrderReturnSaveData =(OrderReturn)bdlGetExtras.get("data");

                mListOrderReturnSavDate.clear();
                mListOrderReturnSavDate = (ArrayList<OrderReturn>)bdlGetExtras.get("dataAll");


                ref_return_no= mOrderReturnSaveData.getReturn_no();
                ref_rep_code= mOrderReturnSaveData.getRep_code();
            }


            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnConfirm = (Button) findViewById(R.id.btnConfirm);

            mTxtsum = (TextView) findViewById(R.id.txtsum);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return));

            // Create the arrays
            sigReturnList = getResources().getStringArray(R.array.returnList);

            lv = (RecyclerView) findViewById(R.id.lv);
            lv.setLayoutManager(new LinearLayoutManager(this));
            lv.setHasFixedSize(true);
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }



    private void setWidgetControl() {
        try{

            getInit();

            mTxtsum.setText("จำนวนรายการสินค้า : "+String.valueOf( mListOrderReturn.size()));

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                }
            });


            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnSlipActivity.class);
                    myIntent.putExtra("datareturn", mOrderReturnSaveData);
                    myIntent.putExtra("datareturnAll", mListOrderReturnSavDate);
                    startActivity(myIntent);
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

    public void showMsgQtyDialog(String sigFs_code,String sigQty_uint_real,String sigQty_uint,int selectedPosition)
    {
        intQTY_UINT_REAL=0;
        intQTY_UINT= 0;

        final String sigFs_code_final = sigFs_code;
        final String sigQty_uint_real_final = sigQty_uint_real;
        final String sigQty_uint_final = sigQty_uint;
        final int selectedPosition_Final = selectedPosition;

        if(sigQty_uint_real_final.isEmpty() || sigQty_uint_real_final.equals("") || sigQty_uint_real_final==null){
            intQTY_UINT_REAL = Integer.parseInt("0");
        }
        else
        {
            intQTY_UINT_REAL = Integer.parseInt(sigQty_uint_real_final);
        }

        if(sigQty_uint_final.isEmpty() || sigQty_uint_final.equals("") || sigQty_uint_final==null){
            intQTY_UINT = Integer.parseInt("0");
        }
        else
        {
            intQTY_UINT = Integer.parseInt(sigQty_uint_final);
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

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
    private Button mBtnBack,mmBtnOk,mmBtnClose,mBtnApprove,mBtnReject,mBtnPlus,mBtnDel;

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

            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                ref_return_no= null;
                ref_rep_code= null;
            } else {
                ref_return_no= extras.getString("REF_RETURN_NO");
                ref_rep_code= extras.getString("REP_CODE");
            }


            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnApprove = (Button) findViewById(R.id.btnApprove);
            mBtnReject = (Button) findViewById(R.id.btnReject);

            mTxtsum = (TextView) findViewById(R.id.txtsum);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return));

            // Create the arrays
            sigReturnList = getResources().getStringArray(R.array.returnList);

//            lv = (ListView) findViewById(R.id.lv);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sigReturnList);
//            lv.setAdapter(adapter);


            lv = (RecyclerView) findViewById(R.id.lv);
            lv.setLayoutManager(new LinearLayoutManager(this));
            lv.setHasFixedSize(true);
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


            mTxtsum.setText("จำนวนรายการสินค้า : "+String.valueOf( mListOrderReturn.size()));

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
//                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                    //set val transno on class order
                    ArrayList<Order> mOrderList= new ArrayList<Order>();
                    Order mOrders = new Order();
                    mOrders.setRep_code(ref_rep_code);
                    mOrderList.add(mOrders);

                    //send val on putextra
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                    myIntent.putExtra("data",mOrderList);
                    startActivity(myIntent);
                }
            });


            mBtnApprove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
//                    finish();
//                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnSlipActivity.class);
                    startActivity(myIntent);
                }
            });

            mBtnReject.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showMsgCancelSelectedSingleDialog();
                }
            });


            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    String retqty  =  mListOrderReturn.get(position).getReturn_unit();
                    showMsgQtyDialog(retqty);
                }
            }));




            // Set item click listener
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String description = sigReturnList[position];
////                    Toast.makeText(SaveOrdersReturnActivity.this, description, Toast.LENGTH_SHORT).show();
//
//
//                    showMsgQtyDialog();
//
//                }
//            });


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

    public void showMsgCancelSelectedSingleDialog()
    {
        // Create the arrays
        sigReturncancellist = getResources().getStringArray(R.array.returncancellist);

        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersReturnActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_save_orders_return_cancel, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("ยืนยันการยกเลิกคืนสืนค้า");


//        lv = (ListView) v.findViewById(R.id.lv);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,sigReturncancellist);
//        lv.setAdapter(adapter);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();

                finish();
                myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                startActivity(myIntent);
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        // Set item click listener
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String description = sigReturncancellist[position];
//                Toast.makeText(SaveOrdersReturnActivity.this, description, Toast.LENGTH_SHORT).show();
//            }
//        });

        DialogBuilder.show();
    }


    public void showMsgQtyDialog(String sigRetqty_uint)
    {
        intQTY_UINT_REAL=0;
        intQTY_UINT= 0;

        final String sigRetqty_uint_final=sigRetqty_uint;

        if(sigRetqty_uint_final.isEmpty() || sigRetqty_uint_final.equals("") || sigRetqty_uint_final==null){
            intQTY_UINT=Integer.parseInt("0");
        }
        else
        {
            intQTY_UINT=Integer.parseInt(sigRetqty_uint_final);
        }


        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersReturnActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_confirm_qty, null);
        DialogBuilder.setView(v);

        mmTxtQty = (TextView) v.findViewById(R.id.txtQty);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);

        mBtnPlus = (Button) v.findViewById(R.id.btnPlus);
        mBtnDel = (Button) v.findViewById(R.id.btnDel);

        mmTxtTitle.setText("แก้ไขจำนวนรับจริง");
        mmTxtQty.setText(String.valueOf(intQTY_UINT));


        mBtnPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if( intQTY_UINT_REAL > Integer.parseInt(mmTxtQty.getText().toString()) ){
                    return;
                }

                intQTY_UINT_REAL++;

                mmTxtQty.setText(String.valueOf(intQTY_UINT_REAL));
            }
        });

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(intQTY_UINT_REAL < Integer.parseInt(mmTxtQty.getText().toString()) ){
                    return;
                }

                if(intQTY_UINT_REAL <= 0){
                    return;
                }

                intQTY_UINT_REAL--;

                mmTxtQty.setText(String.valueOf(intQTY_UINT_REAL));
            }
        });

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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

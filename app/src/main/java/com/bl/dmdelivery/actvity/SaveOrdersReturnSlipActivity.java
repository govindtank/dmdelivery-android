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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;

public class SaveOrdersReturnSlipActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack,mmBtnOk,mmBtnClose,mBtnNote,mBtnSendGps,mBtnSendGpsNo;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ListView lv;
    private String[] sigReturnAcceptList;
    private Intent myIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_return_slip);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            bindWidget();

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
            mBtnNote = (Button) findViewById(R.id.btnNote);
            mBtnSendGps = (Button) findViewById(R.id.btnSendGps);
            mBtnSendGpsNo = (Button) findViewById(R.id.btnSendGpsNo);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return_slip));
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

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

            mBtnNote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showMsgReasonApproveSelectedSingleDialog();
                }
            });

            mBtnSendGps.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                    startActivity(myIntent);
                }
            });

            mBtnSendGpsNo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                    startActivity(myIntent);
                }
            });

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    public void showMsgReasonApproveSelectedSingleDialog()
    {
        // Create the arrays
        sigReturnAcceptList = getResources().getStringArray(R.array.returnaccept);

        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersReturnSlipActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_save_orders_return_cancel, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("เหตุผล/หมายเหตุ");


        lv = (ListView) v.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,sigReturnAcceptList);
        lv.setAdapter(adapter);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();

//                finish();
//
//                myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
//                startActivity(myIntent);
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        // Set item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = sigReturnAcceptList[position];
                Toast.makeText(SaveOrdersReturnSlipActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        DialogBuilder.show();


//        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersReturnSlipActivity.this).create();
//        LayoutInflater inflater = getLayoutInflater();
//        View v = (View) inflater.inflate(R.layout.dialog_save_orders_reason_approve, null);
//        DialogBuilder.setView(v);
//
//        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
//        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
//        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
//        mmTxtTitle.setText("ป้อนเหตุผลการบันทึกคืนสินค้า");
//
//        mmBtnOk.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                DialogBuilder.dismiss();
//
////                //ตรวจสอบก่อนว่ามีสินค้ารับคือนหรือไม่ ถ้ามีไปที่ใบรับคืน ถ้าไม่มีไปที่ หน้ารายการจัดส่งหลัก
////                finish();
////
////                myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
////                startActivity(myIntent);
////                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });
//
//        mmBtnClose.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                DialogBuilder.dismiss();
//            }
//        });
//
//        DialogBuilder.show();
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

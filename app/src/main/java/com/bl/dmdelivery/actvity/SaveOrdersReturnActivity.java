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
import com.bl.dmdelivery.utility.TagUtils;

public class SaveOrdersReturnActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack,mmBtnOk,mmBtnClose,mBtnApprove,mBtnReject;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ListView lv;
    private String[] sigReturnList;
    private String[] sigReturncancellist;

    private Intent myIntent=null;

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
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnApprove = (Button) findViewById(R.id.btnApprove);
            mBtnReject = (Button) findViewById(R.id.btnReject);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return));

            // Create the arrays
            sigReturnList = getResources().getStringArray(R.array.returnList);

            lv = (ListView) findViewById(R.id.lv);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sigReturnList);
            lv.setAdapter(adapter);
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


            // Set item click listener
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String description = sigReturnList[position];
//                    Toast.makeText(SaveOrdersReturnActivity.this, description, Toast.LENGTH_SHORT).show();


                    showMsgQtyDialog();

                }
            });


        } catch (Exception e) {
            showMsgDialog(e.toString());
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


        lv = (ListView) v.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,sigReturncancellist);
        lv.setAdapter(adapter);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();

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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = sigReturncancellist[position];
                Toast.makeText(SaveOrdersReturnActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        DialogBuilder.show();
    }


    public void showMsgQtyDialog()
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersReturnActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_confirm_qty, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("แก้ไขจำนวนรับจริง");

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

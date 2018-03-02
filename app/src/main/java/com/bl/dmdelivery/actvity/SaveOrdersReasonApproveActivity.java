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

import com.bl.dmdelivery.R;

public class SaveOrdersReasonApproveActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnOk,mBtnClose;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";


    private Intent myIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_orders_reason_approve);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        try {
//
//            bindWidget();
//
////            setDefaultFonts();
//
//            setWidgetControl();
//
//        } catch (Exception e) {
//            showMsgDialog(e.toString());
//        }
    }


    private void bindWidget()
    {
        try{
            //button
            mBtnOk = (Button) findViewById(R.id.btnOk);
            mBtnClose = (Button) findViewById(R.id.btnClose);

            mTxtHeader = (TextView) findViewById(R.id.txtHeader);

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

//            mBtnBack.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    finish();
//                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//                }
//            });



            mBtnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                }
            });


            mBtnClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
//                    finish();
//                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
//                    startActivity(myIntent);
                }
            });


//            // Set item click listener
//            lvReturnCancellist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String description = sigReturnCancellist[position];
//
//                    if(!description.isEmpty())   {
////                        finish();
////
////                        myIntent = new Intent(getApplicationContext(), SaveOrdersReturnActivity.class);
////                        startActivity(myIntent);
//                    }
//                }
//            });

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


}

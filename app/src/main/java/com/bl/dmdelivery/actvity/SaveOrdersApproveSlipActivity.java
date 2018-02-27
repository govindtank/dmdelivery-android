package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bl.dmdelivery.R;

public class SaveOrdersApproveSlipActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack,mmBtnOk,mmBtnClose;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private Intent myIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_approve_slip);

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
            mmBtnOk = (Button) findViewById(R.id.btnApprove);
            mmBtnClose = (Button) findViewById(R.id.btnReject);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_slip));
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

            mmBtnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();

                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            mmBtnClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();

                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

}

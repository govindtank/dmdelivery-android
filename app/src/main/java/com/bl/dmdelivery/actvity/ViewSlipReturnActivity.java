package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.utility.TagUtils;

public class ViewSlipReturnActivity extends AppCompatActivity {

    private OrderReturn mOrderReturnGetData = null;

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mTxtRefReturnNo,mTxtRepcode;
    private  Button mBtnBack;
    private ImageView mimvSlip;

//    private String sigDataFullPathimage = "";

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slip_return);
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

            if(bdlGetExtras == null)
            {
                mOrderReturnGetData = new OrderReturn();
            }
            else
            {
                mOrderReturnGetData = new OrderReturn();
                mOrderReturnGetData = (OrderReturn)bdlGetExtras.get("datareturn");
            }

            mBtnBack = (Button) findViewById(R.id.btnBack);
            mimvSlip = (ImageView) findViewById(R.id.imvSlip);

            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_slip_return_list));

            mTxtRefReturnNo = (TextView) findViewById(R.id.txtRefReturnNo);
            mTxtRepcode = (TextView) findViewById(R.id.txtRepcode);
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


    private void setWidgetControl() {
        try{
            mTxtRefReturnNo.setText(mOrderReturnGetData.getReturn_no());
            mTxtRepcode.setText(mOrderReturnGetData.getRep_code() + " - " + mOrderReturnGetData.getRep_name());

            if(mOrderReturnGetData.getFullpathimage().equals("") || mOrderReturnGetData.getFullpathimage().equals("null") || mOrderReturnGetData.getFullpathimage() == null){
                mimvSlip.setImageResource(R.mipmap.ic_noimg);
            }
            else
            {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/SLIPRETURN/" + mOrderReturnGetData.getFullpathimage();
                mimvSlip.setImageBitmap(BitmapFactory.decodeFile(mPath));
            }


            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                }
            });


        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

//    private void getData() {
//        try{
//
//
//        } catch (Exception e) {
//            showMsgDialog(e.toString());
//        }
//    }


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

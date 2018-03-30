package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Order;

import java.util.ArrayList;

public class ViewSlipActivity extends AppCompatActivity {

    private TextView mmTxtMsg,mTxtHeader,mmTxtTitle,txtRepcode,txtInvNo,txtAddress1,txtAddress2,txtMslTel,txtCarton;
    private Button mBtnOk,mmBtnClose,mBtnBack;
    private ImageView mmImvTitle,imvSlip;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
    private String returnflag = "";

    private Intent myIntent=null;

    ArrayList<Order> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slip);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bindWidget();

        setWidgetControl();


    }

    private void bindWidget()
    {
        try{


            mBtnBack = (Button) findViewById(R.id.btnBack);
            imvSlip = (ImageView) findViewById(R.id.imvSlip);

            //txtRepcode = (TextView) findViewById(R.id.txtRepcode);
            //txtInvNo = (TextView) findViewById(R.id.txtInvNo);
            //txtAddress1 = (TextView) findViewById(R.id.txtAddress1);
            //txtAddress2 = (TextView) findViewById(R.id.txtAddress2);
            //txtMslTel = (TextView) findViewById(R.id.txtMslTel);
            //txtgps = (TextView) findViewById(R.id.txtgps);

            //txtCarton = (TextView) findViewById(R.id.txtCarton);

            //mBtnNote = (Button) findViewById(R.id.btnNote);
            //mBtnSendGps = (Button) findViewById(R.id.btnSendGps);
            //mBtnSendGpsNo = (Button) findViewById(R.id.btnSendGpsNo);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_slip));

            getData();


        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void setWidgetControl() {
        try{


            if(order.get(0).getFullpathimage().equals("") || order.get(0).getFullpathimage().equals("null") || order.get(0).getFullpathimage() == null){
                imvSlip.setImageResource(R.mipmap.ic_noimg);
            }
            else
            {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/DMSLIP/" + order.get(0).getFullpathimage();
                imvSlip.setImageBitmap(BitmapFactory.decodeFile(mPath));
            }

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });


        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void getData() {
        try{


            Intent intInv= getIntent();
            Bundle intent = intInv.getExtras();

            if(intent != null)
            {

                order = (ArrayList<Order>) intent.get("data");
                //mOrder = new Order();
                //mOrder =(Order)intent.get("data");


                if(order.size() != 0) {

//                    txtRepcode.setText(setRepcodeFormat(order.get(0).getRep_code())+" "+order.get(0).getRep_name());
//                    txtInvNo.setText(order.get(0).getTransNo());
//                    //txtAddress1.setText(order.get(0).getAddress1());
//                    //txtAddress2.setText(order.get(0).getAddress2()+" "+order.get(0).getPostal());
//                    //txtMslTel.setText("โทร. "+order.get(0).getRep_telno());
//                    txtCarton.setText(order.get(0).getCont_desc());
//                    //txtgps.setText("GPS : 0,0");
//
//                    returnflag = order.get(0).getReturn_flag();



                }



            }




        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private String setRepcodeFormat(String repcode) {


        try {

            String repcodeformat = "";

            if(repcode.length() == 10)

            {
                repcodeformat = repcode.substring(0, 4)+"-"+repcode.substring(4, 9)+"-"+repcode.substring(9, 10);
            }
            else
            {
                repcodeformat = repcode;
            }

            return repcodeformat;


        } catch (Exception e)
        {
            return repcode;
            //showMsgDialog(e.toString());
        }

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

//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mmTxtMsg.setTypeface(tf);
//        mmTxtTitle.setTypeface(tf);
//        mmBtnClose.setTypeface(tf);

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

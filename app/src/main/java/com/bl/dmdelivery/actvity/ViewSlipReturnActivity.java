package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.utility.TagUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ViewSlipReturnActivity extends AppCompatActivity {

    private OrderReturn mOrderReturnGetData = null;

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mTxtRefReturnNo,mTxtRepcode,mmTxtMsg;
    private  Button mBtnBack,mmBtnClose;
    private ImageView mimvSlip,mmImvTitle;

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
//            mTxtRefReturnNo.setText(mOrderReturnGetData.getReturn_no());
//            mTxtRepcode.setText(mOrderReturnGetData.getRep_code() + " - " + mOrderReturnGetData.getRep_name());

            if(mOrderReturnGetData.getFullpathimage().equals("") || mOrderReturnGetData.getFullpathimage().equals("null") || mOrderReturnGetData.getFullpathimage() == null){
                mimvSlip.setImageResource(R.mipmap.ic_noimg);
            }
            else
            {
//                String mPathDMSLIPRETURN = Environment.getExternalStorageDirectory().toString() + "/DMSLIPRETURN/" + mOrderReturnGetData.getFullpathimage();
//                mimvSlip.setImageBitmap(BitmapFactory.decodeFile(mPath));


                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                java.util.Date currentLocalTime = cal.getTime();
                SimpleDateFormat date = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");
                date.setTimeZone(TimeZone.getTimeZone("GMT"));
                String localTime = date.format(currentLocalTime);
                localTime = localTime.replace(" ", "").replace("-", "");


                String sendtime = "";
                String mInputPath ="";
                String mInputPathProcess ="";

                sendtime = mOrderReturnGetData.getSendtoserver_timestamp();



                if(sendtime.equals("") || sendtime.equals("null") || sendtime==null) {
                    //ถ้ามีใน DMSLIPRETURN
                    mimvSlip.setImageBitmap(BitmapFactory.decodeFile(mInputPath));
                }
                else
                {
                    if (sendtime.length() > 8) {
                        localTime = sendtime.substring(0, 8);
                    }

                    mInputPath = Environment.getExternalStorageDirectory().toString() + "/DMSLIPRETURN/" + mOrderReturnGetData.getFullpathimage();
                    mInputPathProcess = Environment.getExternalStorageDirectory().toString() + "/DMRETURNPROCESSED/" + localTime + "/" + mOrderReturnGetData.getFullpathimage();

                    File dirInputDMSLIPRETURN = new File(mInputPath);
                    if (!dirInputDMSLIPRETURN.exists()) {
                        //ถ้าไม่มี DMSLIPRETURN ให้ไปหาที่ DMRETURNPROCESSED
                        File dirInputDMRETURNPROCESSED = new File(mInputPathProcess);
                        if (!dirInputDMRETURNPROCESSED.exists()) {
                            //ให้แสดงภาพว่าง
                            mimvSlip.setImageResource(R.mipmap.ic_noimg);
                        } else {
                            //ถ้ามีใน DMRETURNPROCESSED ให้แสดงภาพ
                            mimvSlip.setImageBitmap(BitmapFactory.decodeFile(mInputPathProcess));
                        }
                    } else {
                        //ถ้ามีใน DMSLIPRETURN
                        mimvSlip.setImageBitmap(BitmapFactory.decodeFile(mInputPath));
                    }
                }
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

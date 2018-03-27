package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.SaveOrderReasonViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.utility.TagUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class SaveOrdersReturnSlipActivity extends AppCompatActivity {

    private TextView mmTxtMsg,mTxtHeader,mmTxtTitle,mTxtInvNo,mTxtRepcode;
    private Button mBtnBack,mBtnCancelGPS,mBtnCancel,mBtnGPS,mBtnSaveGPS,mBtnSave,mBtnNew,mBtnNote,mmBtnOk,mmBtnClose;
    private ImageView mmImvTitle;
    private EditText medtNote;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ArrayList<Reason> mReturnAcceptRejectList = new ArrayList<Reason>();
    private ArrayList<OrderReturn> mListOrderReturnSavDate = new ArrayList<OrderReturn>();
    private OrderReturn mOrderReturnGetData = null;
    private OrderReturn mOrderReturnSaveData = null;
    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

    private CanvasViewSlipReturn mCanvasViewSlipReturn;


    private ArrayList<Reason> arrayListReason = new ArrayList<Reason>();
    private RecyclerView lvReturnAcceptRejectList;
    private RecyclerView.Adapter mReturnAcceptRejectListAdapter;
    private String mSelectReson = "";
    private Integer mSelectResonIndex = 0;
    private String  mSelect="0";

    String mInputPath = Environment.getExternalStorageDirectory().toString() + "/SLIPRETURN/";


    private String sigInvNo="";
    private String sigReturn_no="";
    private String sigRepcode="";
    private String sigRep_name="";
    private String sigReson_code="";
    private String sigNote="";
    private String backToPage="";

    public String batteryPercent = "0";
    private File mFile;
    private String mFileName;

    private String mLatitude = "0";
    private String mLongitude = "0";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

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

            sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

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
                mOrderReturnGetData = new OrderReturn();

                mListOrderReturnSavDate.clear();

                sigInvNo="";
                sigReturn_no="";
                sigRepcode="";
            } else {
                mOrderReturnGetData = new OrderReturn();
                mOrderReturnGetData =(OrderReturn)bdlGetExtras.get("datareturn");

                mListOrderReturnSavDate.clear();
                mListOrderReturnSavDate = (ArrayList<OrderReturn>)bdlGetExtras.get("datareturnAll");

                sigInvNo = mOrderReturnGetData.getReftrans_no();
                sigReturn_no = mOrderReturnGetData.getReturn_no();
                sigRepcode = mOrderReturnGetData.getRep_code();
                sigRep_name = mOrderReturnGetData.getRep_name();
            }

            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnCancelGPS = (Button) findViewById(R.id.btnCancelGPS);
            mBtnCancel = (Button) findViewById(R.id.btnCancel);
            mBtnGPS = (Button) findViewById(R.id.btnGPS);
            mBtnSaveGPS = (Button) findViewById(R.id.btnSaveGPS);
            mBtnSave = (Button) findViewById(R.id.btnSave);

            mBtnNew= (Button) findViewById(R.id.btnNew);
            mBtnNote = (Button) findViewById(R.id.btnNote);

            mBtnCancelGPS.setVisibility(View.INVISIBLE);
            mBtnCancel.setVisibility(View.INVISIBLE);
            mBtnGPS.setVisibility(View.INVISIBLE);
            mBtnSaveGPS.setVisibility(View.INVISIBLE);
            mBtnSave.setText("บันทึก");


            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return_slip));

            mTxtInvNo = (TextView) findViewById(R.id.txtInvNo);
            mTxtRepcode = (TextView) findViewById(R.id.txtRepcode);



            mCanvasViewSlipReturn = (CanvasViewSlipReturn) findViewById(R.id.signature_canvas);



            File dirInput = new File (mInputPath);
            if (!dirInput.exists())
            {
                dirInput.mkdirs();
            }


            getData();
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void getData() {
        try{

            arrayListReason.clear();
            mHelper = new DBHelper(getApplicationContext());
            arrayListReason = mHelper.getReasonListForCondition("'RETURN_ACCEPT'");


            if(arrayListReason.size() > 0)
            {
                mSelectReson =  arrayListReason.get(0).getReason_desc();
                mSelectResonIndex = 0;

                mCanvasViewSlipReturn.reason = mSelectReson;
                mCanvasViewSlipReturn.invalidate();
            }

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


    private void setWidgetControl() {
        try{

            mTxtInvNo.setText("เลขที่ใบรับคืน : " + sigReturn_no);
            mTxtRepcode.setText("รหัส-ชื่อสมาชิก : " + sigRepcode + " - " + sigRep_name);


            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    backToPage = "BACK_TO_PAGE";

                    editor = sp.edit();
                    editor.putString(TagUtils.PREF_BACK_TO_PAGE, backToPage);
                    editor.apply();

                    finish();
                }
            });

            mBtnNew.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    clearCanvas(view);
                }
            });


            mBtnNote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showMsgReasonApproveSelectedSingleDialog();
                }
            });


            mBtnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    backToPage = "SAVE_TO_PAGE";

                    editor = sp.edit();
                    editor.putString(TagUtils.PREF_BACK_TO_PAGE, backToPage);
                    editor.apply();

                    //ถ้าบันทึก ใบคืนไม่ครบ ไปหน้าใบคืน
                    if(medtNote != null){
                        sigNote = medtNote.getText().toString();
                    }
                    else
                    {
                        sigNote = "";
                    }

                    takeScreenshot();

                    //บันทึกข้อมูล รับได้
                    mHelper = new DBHelper(getApplicationContext());
                    mOrderReturnSaveData = new OrderReturn();

                    mOrderReturnSaveData.setReturn_no(sigReturn_no);
                    mOrderReturnSaveData.setRep_code(sigRepcode);
                    mOrderReturnSaveData.setReturn_status("1");
                    mOrderReturnSaveData.setReason_code(sigReson_code);
                    mOrderReturnSaveData.setReturn_note(sigNote);
                    mOrderReturnSaveData.setReturn_unit_real("0");
                    mOrderReturnSaveData.setFullpathimage("");
                    mHelper.updateOrderReturnSlip(mOrderReturnSaveData);

//
//                   //รับได้
//                    finish();
                }
            });

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    public void clearCanvas(View v) {
        mCanvasViewSlipReturn.totalDx = 0;
        mCanvasViewSlipReturn.totalDy  = 0;
        mCanvasViewSlipReturn.clearCanvas();
    }

    public void showMsgReasonApproveSelectedSingleDialog()
    {

        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_save_orders_return_cancel, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnok);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);
        medtNote = (EditText) v.findViewById(R.id.edtNote);

        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
        medtNote.setTypeface(tf);
        medtNote.setText(sigNote);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText(getResources().getString(R.string.txt_text_reason_remark));
        mmBtnOk.setText(getResources().getString(R.string.btn_text_ok));


        lvReturnAcceptRejectList = (RecyclerView) v.findViewById(R.id.lvacceptList);
        lvReturnAcceptRejectList.setLayoutManager(new LinearLayoutManager(this));
        lvReturnAcceptRejectList.setHasFixedSize(true);


        if(mSelectResonIndex > 0)
        {

            arrayListReason.get(mSelectResonIndex).setIsselect("1");
        }
        else
        {
            arrayListReason.get(0).setIsselect("1");
        }

        mReturnAcceptRejectListAdapter = new SaveOrderReasonViewAdapter(getApplicationContext(),arrayListReason);
        lvReturnAcceptRejectList.setAdapter(mReturnAcceptRejectListAdapter);



        lvReturnAcceptRejectList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                for(int i = arrayListReason.size()-1 ; i >= 0; i--)
                {
                    mSelect = arrayListReason.get(i).getIsselect().toString();

                    if(mSelect.equals("1"))
                    {
                        arrayListReason.get(i).setIsselect("0");
                    }
                }


                mSelect = arrayListReason.get(position).getIsselect();

                arrayListReason.get(position).setIsselect("1");

                String description = arrayListReason.get(position).getReason_desc();

                mSelectReson = description;

                mSelectResonIndex = position;

                mReturnAcceptRejectListAdapter.notifyDataSetChanged();

            }
        }));


        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //loadData();
                DialogBuilder.dismiss();

                sigNote = medtNote.getText().toString();
                mCanvasViewSlipReturn.reason = mSelectReson;
                mCanvasViewSlipReturn.note = sigNote;
                mCanvasViewSlipReturn.invalidate();
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
//        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
//        final AlertDialog alert = DialogBuilder.create();
//        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = li.inflate(R.layout.dialog_message, null, false);
//
//        mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
//
//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mTxtMsg.setTypeface(tf);
//        mTxtMsg.setText(msg);
//
//        DialogBuilder.setView(v);
//        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
//            }
//        });
//        DialogBuilder.show();

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


    private void takeScreenshot() {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        java.util.Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String localTime = date.format(currentLocalTime);
        localTime = localTime.replace(" ", "").replace("-", "");

        getBatteryPercentage();


      /*  NumberFormat nf = NumberFormat.getInstance();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lx = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String latlng = nf.format(lx.getLatitude()) + "," + nf.format(lx.getLongitude());*/

        String latlng = mLatitude+","+mLongitude;

        String truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");


        String fileName= "V" + getResources().getString(R.string.app_version_slip) + "_" + Build.SERIAL.trim() +  "-" + truckNo + "-" + mOrderReturnGetData.getReftrans_no() + "-" + mOrderReturnGetData.getRep_code() + "-" + localTime + "-" + latlng + "-" + getImeiNumber() + "-" + batteryPercent + ".jpg";


        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/SLIPRETURN/" + fileName;


            View v = (View) findViewById(R.id.lnlSlip);

            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            v.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            mFileName = fileName;
            mFile =imageFile;

            //processSlip(mPath);

            //openScreenshot(imageFile);

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }


    private void getBatteryPercentage() {
        try {
            Intent batteryIntent = getApplicationContext().registerReceiver(null,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int rawlevel = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int level = -1;
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
                batteryPercent = "P"+String.valueOf(level);
            }else
            {
                batteryPercent = "P"+String.valueOf(0);
            }

        } catch (Exception e)
        {
            showMsgDialog(e.toString());
        }
    }

    public String getImeiNumber() {
        String imeiNumber ="";
        try {
            TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber = telephony.getDeviceId();

        } catch (SecurityException e)
        {
            showMsgDialog(e.toString());
        }

        return imeiNumber;
    }


}

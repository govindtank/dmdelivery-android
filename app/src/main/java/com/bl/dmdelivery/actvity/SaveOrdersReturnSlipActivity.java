package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.utility.TagUtils;

import java.io.File;
import java.util.ArrayList;

public class SaveOrdersReturnSlipActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mTxtInvNo,mTxtRepcode;
    private Button mBtnBack,mBtnCancelGPS,mBtnCancel,mBtnGPS,mBtnSaveGPS,mBtnSave,mBtnNew,mBtnNote,mmBtnOk,mmBtnClose;
    private ImageView mmImvTitle;
    private EditText medtNote;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ArrayList<Reason> mReturnAcceptRejectList = new ArrayList<Reason>();
    private ArrayList<OrderReturn> mListOrderReturnSavDate = new ArrayList<OrderReturn>();
    ArrayList<String> arrayListReason = new ArrayList<String>();
    private OrderReturn mOrderReturnGetData = null;
    private OrderReturn mOrderReturnSaveData = null;

    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

    private ListView lvReturnAcceptRejectList;
//    private String[] sigReturnAcceptRejectList;
//    private Intent myIntent=null;

    private CanvasView customCanvas;
    private String mSelectReson = "";
    private Integer mSelectResonIndex = 0;

    String mInputPath = Environment.getExternalStorageDirectory().toString() + "/SLIPRETURN/";


    private String sigInvNo="";
    private String sigReturn_no="";
    private String sigRepcode="";
    private String sigRep_name="";
    private String sigReson_code="";
    private String sigNote="";
    private String backToPage="";

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



            customCanvas = (CanvasView) findViewById(R.id.signature_canvas);


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
            mReturnAcceptRejectList.clear();
            mHelper = new DBHelper(getApplicationContext());
            mReturnAcceptRejectList = mHelper.getReasonListForCondition("'RETURN_ACCEPT'");


            for(int i = 0; i < mReturnAcceptRejectList.size();i++)
            {
                arrayListReason.add(mReturnAcceptRejectList.get(i).getReason_code() + " " + mReturnAcceptRejectList.get(i).getReason_desc());
            }

            if(arrayListReason.size() > 0)
            {
                mSelectReson =  mReturnAcceptRejectList.get(0).getReason_desc();
                mSelectResonIndex = 0;

                customCanvas.gpstext="GPS : ";
                customCanvas.reason = mSelectReson;
                customCanvas.invalidate();
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

                    //บันทึกข้อมูล รับได้
                    mHelper = new DBHelper(getApplicationContext());
                    mOrderReturnSaveData = new OrderReturn();

                    mOrderReturnSaveData.setReturn_no(sigReturn_no);
                    mOrderReturnSaveData.setRep_code(sigRepcode);
                    mOrderReturnSaveData.setReturn_status("1");
                    mOrderReturnSaveData.setReason_code(sigReson_code);
                    mOrderReturnSaveData.setReturn_note(sigNote);
                    mHelper.updateOrderReturnSlip(mOrderReturnSaveData);


                   //รับได้
                    finish();
                }
            });

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    public void clearCanvas(View v) {
        customCanvas.totalDx = 0;
        customCanvas.totalDy  = 0;
        customCanvas.clearCanvas();
    }

    public void showMsgReasonApproveSelectedSingleDialog()
    {

        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_reason_save_order, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnok);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);
        medtNote = (EditText) v.findViewById(R.id.edtNote);


        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText(getResources().getString(R.string.txt_text_reason_remark));
        mmBtnOk.setText(getResources().getString(R.string.btn_text_ok));

        lvReturnAcceptRejectList = (ListView) v.findViewById(R.id.lv);
        lvReturnAcceptRejectList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if(arrayListReason.size() > 0)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,arrayListReason);
            lvReturnAcceptRejectList.setAdapter(adapter);

            //ถ้ามีข้อมูลบน ListView ให้เลือกรายการแรกเสมอ
            if(mSelectResonIndex > 0)
            {
                lvReturnAcceptRejectList.setItemChecked(mSelectResonIndex,true);
            }
            else
            {
                lvReturnAcceptRejectList.setItemChecked(0,true);
            }
        }

        lvReturnAcceptRejectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = mReturnAcceptRejectList.get(position).getReason_desc();

                sigReson_code = mReturnAcceptRejectList.get(position).getReason_code();
                mSelectReson = description;
                mSelectResonIndex = position;

                //Toast.makeText(SaveOrdersApproveSlipActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });


        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //loadData();
                DialogBuilder.dismiss();

                customCanvas.reason = mSelectReson;
                customCanvas.invalidate();
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

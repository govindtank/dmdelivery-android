package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Reason;

import java.util.ArrayList;

public class SaveOrdersApproveSlipActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack,mmBtnOk,mmBtnClose,btnCancelGPS,btnCancel,btnGPS,btnSaveGPS,btnSave,btnNew,btnNote;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ArrayList<Reason> mDeliveryAcceptList = new ArrayList<Reason>();

    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

    private ListView lv;
//    private String[] sigDeliverylist;

    private ListView lvDeliveryAcceptList;
    private Intent myIntent=null;

    private CanvasView customCanvas;

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
            btnCancelGPS = (Button) findViewById(R.id.btnCancelGPS);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnGPS = (Button) findViewById(R.id.btnGPS);
            btnSaveGPS = (Button) findViewById(R.id.btnSaveGPS);
            btnSave = (Button) findViewById(R.id.btnSave);

            btnNew = (Button) findViewById(R.id.btnNew);
            btnNote = (Button) findViewById(R.id.btnNote);

            //mBtnNote = (Button) findViewById(R.id.btnNote);
            //mBtnSendGps = (Button) findViewById(R.id.btnSendGps);
            //mBtnSendGpsNo = (Button) findViewById(R.id.btnSendGpsNo);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_slip));

            customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
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

            btnNew.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {


                    clearCanvas(view);

                }
            });

//            mBtnNote.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    showMsgReasonApproveSelectedSingleDialog();
//                }
//            });
//
//            mBtnSendGps.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    //ตรวจสอบก่อนว่ามีสินค้ารับคือนหรือไม่ ถ้ามีไปที่ใบรับคืน ถ้าไม่มีไปที่ หน้ารายการจัดส่งหลัก
//                    finish();
//
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                }
//            });
//
//            mBtnSendGpsNo.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    //ตรวจสอบก่อนว่ามีสินค้ารับคือนหรือไม่ ถ้ามีไปที่ใบรับคืน ถ้าไม่มีไปที่ หน้ารายการจัดส่งหลัก
//                    finish();
//
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                }
//            });

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

    public void showMsgReasonApproveSelectedSingleDialog()
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersApproveSlipActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_save_orders_return_cancel, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("เหตุผล/หมายเหตุ");

        lvDeliveryAcceptList = (ListView) v.findViewById(R.id.lv);
        lvDeliveryAcceptList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        mDeliveryAcceptList.clear();
        mHelper = new DBHelper(getApplicationContext());
        mDeliveryAcceptList = mHelper.getReasonListForCondition("'DELIVERY_ACCEPT'");

        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 0; i < mDeliveryAcceptList.size();i++)
        {
            arrayList.add(mDeliveryAcceptList.get(i).getReason_code() + " " + mDeliveryAcceptList.get(i).getReason_desc());
        }

        if(arrayList.size() > 0)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,arrayList);
            lvDeliveryAcceptList.setAdapter(adapter);

            //ถ้ามีข้อมูลบน ListView ให้เลือกรายการแรกเสมอ
            lvDeliveryAcceptList.setItemChecked(0,true);
        }

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

//        // Set item click listener
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String description = sigDeliverylist[position];
//                Toast.makeText(SaveOrdersApproveSlipActivity.this, description, Toast.LENGTH_SHORT).show();
//            }
//        });

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

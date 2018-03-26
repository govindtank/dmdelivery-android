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
import android.support.v7.widget.LinearLayoutManager;
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
import com.bl.dmdelivery.adapter.OrderReturnViewAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SaveOrdersReturnDocActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mTxtsum;
    private Button mBtnBack;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private Intent myIntent=null;
    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<OrderReturn> mListOrderReturn = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListOrderReturnGeOnResume = new ArrayList<OrderReturn>();
    private ArrayList<Order> mListOrder= new ArrayList<Order>();
    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

    private String sigMultiInv="";
    private boolean isResumeState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_return_doc);

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

    @Override
    public void onResume(){
        super.onResume();

        if(!isResumeState)
        {
//            //มาจากหน้า slip
//            Toast toast = Toast.makeText(SaveOrdersReturnDocActivity.this, "onResume - OK", Toast.LENGTH_SHORT);
//            toast.show();


            if(isSaveOrderReturnAllComplete()){
                //ถ้าบันทึกใบรับคืนครบให้ไปที่ หน้าจัดส่ง
                finish();
            }
            else
            {
                //ถ้าบันทึกใบคืนไม่ครบให้ refresh ข้อมูล
                getInit();
            }
        }
//        else
//        {
//            //มาจากหน้าใบคืน
//            Toast toast = Toast.makeText(SaveOrdersReturnDocActivity.this, "onResume - Slip1", Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }


    public void onBackPressed() {
//        finish();
//
//        myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
//        startActivity(myIntent);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private boolean isSaveOrderReturnAllComplete(){
        try{
            mListOrderReturnGeOnResume.clear();
            mHelper = new DBHelper(getApplicationContext());
            mListOrderReturnGeOnResume = mHelper.getOrdersReturnList(mListOrder);

            for(int i=0;i < mListOrderReturnGeOnResume.size(); i++)
            {
                if (mListOrderReturnGeOnResume.get(i).getReturn_status() == "0")
                {
                    return false;
                }
            }

            if(mListOrderReturnGeOnResume == null){ return  false;}
            if(mListOrderReturnGeOnResume.size() == 0){ return  false;}

            return true;
        }
        catch (Exception e)
        {
        }
        return  false;
    }


    private void bindWidget()
    {
        try{
            Intent ineGetIntent= getIntent();
            Bundle bdlGetExtras= ineGetIntent.getExtras();

            if(bdlGetExtras == null) {
                mListOrder = new ArrayList<Order>();
            } else {
                mListOrder = new ArrayList<Order>();
                mListOrder =(ArrayList<Order>)bdlGetExtras.get("data");
            }

            isResumeState = true;

            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnBack.setVisibility(View.INVISIBLE);
            mTxtsum = (TextView) findViewById(R.id.txtsum);
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return_list));

            lv = (RecyclerView) findViewById(R.id.lv);
            lv.setLayoutManager(new LinearLayoutManager(this));
            lv.setHasFixedSize(true);
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void setWidgetControl() {
        try{

            getInit();

            mTxtsum.setText("จำนวนใบรับคืน : "+String.valueOf( mListOrderReturn.size()));

            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    OrderReturn mOrderReturn = new OrderReturn();
                    mOrderReturn.setReftrans_no( mListOrderReturn.get(position).getReftrans_no());
                    mOrderReturn.setReturn_no( mListOrderReturn.get(position).getReturn_no());
                    mOrderReturn.setRep_code( mListOrderReturn.get(position).getRep_code());
                    mOrderReturn.setRep_name( mListOrderReturn.get(position).getRep_name());

                    isResumeState = false;

                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnActivity.class);
                    myIntent.putExtra("data", mOrderReturn);
                    myIntent.putExtra("dataAll", mListOrderReturn);
                    startActivity(myIntent);
                }
            }));

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void getInit() {

        try {
            mListOrderReturn.clear();
            mHelper = new DBHelper(getApplicationContext());
            mListOrderReturn = mHelper.getOrdersReturnList(mListOrder);

            if(mListOrderReturn != null)
            {
                if(mListOrderReturn.size() > 0){
                    mAdapter = new OrderReturnViewAdapter(getApplicationContext(),mListOrderReturn);
                    lv.setAdapter(mAdapter);
                }
            }else
            {
                showMsgDialog(getResources().getString(R.string.error_data_not_in_system));
            }

        } catch (Exception e) {
            e.printStackTrace();
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

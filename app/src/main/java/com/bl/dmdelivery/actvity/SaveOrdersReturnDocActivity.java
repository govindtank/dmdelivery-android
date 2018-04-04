package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
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
import com.bl.dmdelivery.utility.TagUtils;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SaveOrdersReturnDocActivity extends AppCompatActivity {

    private TextView mmTxtMsg,mTxtHeader,mmTxtTitle,mTxtsum;
    private Button mBtnBack,mmBtnClose;
    private ImageView mmImvTitle;
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

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

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
            sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

            bindWidget();

//            setDefaultFonts();

            setWidgetControl();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

//        private void setDefaultFonts() {
//        try {
//            Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//            mTxtHeader.setTypeface(tf);
////            mBtnBack.setTypeface(tf);
//
//        } catch (Exception e) {
//            showMsgDialog(e.toString());
//        }
//    }

    @Override
    public void onResume(){
        super.onResume();

        if(isResumeState)
        {
//            //มาจากหน้า slip
//            Toast toast = Toast.makeText(SaveOrdersReturnDocActivity.this, "onResume - OK", Toast.LENGTH_SHORT);
//            toast.show();

            String sigBackToPage = sp.getString(TagUtils.PREF_BACK_TO_PAGE, "");
            if (sigBackToPage.toUpperCase().equals("SAVE_TO_PAGE"))
            {
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
        }
//        else
//        {
//            //มาจากหน้าใบคืน
//            Toast toast = Toast.makeText(SaveOrdersReturnDocActivity.this, "onResume - Slip1", Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }

    @Override
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
//                Toast toast = Toast.makeText(SaveOrdersReturnDocActivity.this, "onResume - OK " + mListOrderReturnGeOnResume.get(i).getReturn_status(), Toast.LENGTH_SHORT);
//                toast.show();

                if (mListOrderReturnGeOnResume.get(i).getReturn_status().equals("0") || mListOrderReturnGeOnResume.get(i).getReturn_status().equals(""))
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

            isResumeState = false;

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

                    isResumeState = true;

                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnActivity.class);
                    myIntent.putExtra("data", mOrderReturn);
                    myIntent.putExtra("dataAll", mListOrderReturn);
                    myIntent.putExtra("dataInvUpdate", mListOrder);
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

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
    private ArrayList<Order> mListOrder= new ArrayList<Order>();
    private CheckNetwork chkNetwork = new CheckNetwork();
    DBHelper mHelper;

    private String sigMultiInv="";

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

    public void onBackPressed() {
//        finish();
//
//        myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
//        startActivity(myIntent);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void bindWidget()
    {
        try{
            //button
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
//            //get val Intent
//            Intent ineGetVals= getIntent();
//            Bundle bdlGetVals = ineGetVals.getExtras();
//
//            if(bdlGetVals != null)
//            {
//                mListOrder.clear();
//                mListOrder =(ArrayList<Order>)bdlGetVals.get("data");
//            }


//            Bundle extras = getIntent().getExtras();
            Intent ineGetIntent= getIntent();
            Bundle bdlGetExtras= ineGetIntent.getExtras();

            if(bdlGetExtras == null) {
                mListOrder = new ArrayList<Order>();
            } else {
                mListOrder = new ArrayList<Order>();
                mListOrder =(ArrayList<Order>)bdlGetExtras.get("data");
            }


            getInit();


            mTxtsum.setText("จำนวนใบรับคืน : "+String.valueOf( mListOrderReturn.size()));

            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    String rep_code =  mListOrderReturn.get(position).getRep_code();
                    String ref_return_no  =  mListOrderReturn.get(position).getReturn_no();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnActivity.class);
                    myIntent.putExtra("REP_CODE", rep_code);
                    myIntent.putExtra("REF_RETURN_NO", ref_return_no);
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
                //finish();
                //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

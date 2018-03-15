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
            //get val Intent
            Intent ineGetVals= getIntent();
            Bundle bdlGetVals = ineGetVals.getExtras();

            sigMultiInv="";
            if(bdlGetVals != null)
            {

                ArrayList<String> list = new ArrayList<String>();
//                list.add("1100499936");
//                list.add("1100499691");

                list =(ArrayList<String>)bdlGetVals.get("REF_TRANS_NO");
                if(list.size() > 0){
                    for(int i=0;i<list.size();i++){
                        String sigInv = list.get(i);
                        if(!sigInv.isEmpty()){
                            if(i==0)
                            {
                                sigMultiInv= "'" + sigInv + "'";
                            }
                            else
                            {
                                sigMultiInv = sigMultiInv + ",'" + sigInv + "'";
                            }
                        }
                    }
                }
            }


            getInit();


            mTxtsum.setText("จำนวนใบรับคืน : "+String.valueOf( mListOrderReturn.size()));

            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    String refno  =  mListOrderReturn.get(position).getReturn_no();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnActivity.class);
                    myIntent.putExtra("REF_RETURN_NO", refno);
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
            mListOrderReturn.clear();
            mListOrderReturn = mHelper.getOrderReturnCriteria(sigMultiInv);
//            mListOrderReturn = mHelper.getOrderReturn();

            if(mListOrderReturn.size()>0)
            {
                mAdapter = new OrderReturnViewAdapter(getApplicationContext(),mListOrderReturn);
                lv.setAdapter(mAdapter);
            }else
            {
                //finish();
                //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                showMsgDialog(getResources().getString(R.string.error_data_not_in_system));
            }



//            Order f = new Order();
//            f.setTransNo("1");
//            mListOrderData.add(f);
//
//            f = new Order();
//            f.setTransNo("2");
//            mListOrderData.add(f);
//
//            f = new Order();
//            f.setTransNo("3");
//            mListOrderData.add(f);
//
//            f = new Order();
//            f.setTransNo("4");
//            mListOrderData.add(f);
//
//            f = new Order();
//            f.setTransNo("5");
//            mListOrderData.add(f);
//
//            f = new Order();
//            f.setTransNo("6");
//            mListOrderData.add(f);
//
//            f = new Order();
//            f.setTransNo("7");
//            mListOrderData.add(f);
//
//            f = new Order();
//            f.setTransNo("8");
//            mListOrderData.add(f);


            //new getInitDataInAsync().execute();

           /* if(chkNetwork.isConnectionAvailable(getApplicationContext()))
            {

                if(chkNetwork.isWebserviceConnected(getApplicationContext()))
                {

                    new getOrderDataInAsync().execute();
                }
                else
                {

                    showMsgDialog(getResources().getString(R.string.error_webservice));

                }

            }else
            {

                showMsgDialog(getResources().getString(R.string.error_network));
            }*/

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

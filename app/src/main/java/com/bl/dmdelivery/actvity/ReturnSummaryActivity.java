package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrderReturnDtlViewAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.ReturnSummaryViewAdapter;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.OrderScanReq;
import com.bl.dmdelivery.utility.TagUtils;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class ReturnSummaryActivity extends AppCompatActivity {


    private ACProgressFlower mProgressDialog;
    private TextView mmTxtMsg,mTxtHeader,mmTxtTitle,mmTxtsum;
    private ImageView mmImvTitle;
    private Button mBtnBack,mmBtnClose;

    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;
    private DBHelper mHelper;

    private ArrayList<OrderReturn> mListOrderReturn = new ArrayList<OrderReturn>();
    private int itemsSumQty=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_summary);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


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
            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);

            //textbox
            mmTxtsum = (TextView) findViewById(R.id.txtsum);
            mmTxtsum.setText("แสดงรายการ: 0, จำนวนชิ้นรวม: 0");

            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_unpack_list));

            //listview
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

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();

                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                }
            });




//            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//
//                }
//            }));

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


    private void getInit() {

        try {

            new ReturnSummaryActivity.getInitDataInAsync().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int setSumItemQty(){
        try
        {
            itemsSumQty=0;

            if(mListOrderReturn.size()==0){
                return 0;
            }

            for(int i=0; i < mListOrderReturn.size();i++){
                if(!mListOrderReturn.get(i).getReturn_unit_real().isEmpty()){
                    itemsSumQty = itemsSumQty + Integer.parseInt(mListOrderReturn.get(i).getReturn_unit_real().toString());
                }
            }

            return  itemsSumQty;
        }catch (Exception e) {
            //e.printStackTrace();
            showMsgDialog(e.toString());
        }
        return  0;
    }


    private class getInitDataInAsync extends AsyncTask<String, Void, ReturnSummaryActivity.PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(ReturnSummaryActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();
        }

        @Override
        protected ReturnSummaryActivity.PageResultHolder doInBackground(String... params) {
            // TODO Auto-generated method stub
            ReturnSummaryActivity.PageResultHolder pageResultHolder = new ReturnSummaryActivity.PageResultHolder();
            //String xmlInput = params[0];
            try
            {
                itemsSumQty=0;

                mHelper = new DBHelper(getApplicationContext());
                mListOrderReturn.clear();
                mListOrderReturn = mHelper.getReturnSummary("ALL");
            }
            catch (Exception e)
            {
                pageResultHolder.content = "Exception : CheckOrderData";
                pageResultHolder.exception = e;
            }

            return pageResultHolder;
        }

        @Override
        protected void onPostExecute(final ReturnSummaryActivity.PageResultHolder result) {
            // TODO Auto-generated method stub

            try {

                if (result.exception != null) {
                    mProgressDialog.dismiss();
                    mmTxtsum.setText("แสดงรายการ: 0, จำนวนชิ้นรวม: 0");
                    showMsgDialog(result.exception.toString());
                }
                else
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms

                            mProgressDialog.dismiss();
                            if(mListOrderReturn.size()>0)
                            {
                                mmTxtsum.setText("แสดงรายการ: " + mListOrderReturn.size() + ", จำนวนชิ้นรวม: " + setSumItemQty());

                                mAdapter = new ReturnSummaryViewAdapter(getApplicationContext(),mListOrderReturn);
                                lv.setAdapter(mAdapter);
                            }else
                            {
                                mmTxtsum.setText("แสดงรายการ: 0, จำนวนชิ้นรวม: 0");
                                showMsgDialog(getResources().getString(R.string.error_data_not_in_system));
                            }

                        }
                    }, 200);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class PageResultHolder {
        private String content;
        private Exception exception;
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

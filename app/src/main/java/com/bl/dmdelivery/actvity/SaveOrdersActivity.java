package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrderAdapter;
import com.bl.dmdelivery.adapter.OrderSlipViewAdapter;
import com.bl.dmdelivery.adapter.RVListDeliveryBWAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.helper.GlobalObject;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderData;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SaveOrdersActivity extends AppCompatActivity {


    private ACProgressFlower mProgressDialog;
//    private TextView mTxtMsg,mTxtHeader;
//    private Button mBtnBack;

//    private ImageView mImvHeader;
    private TextView mTxtMsg,mTxtHeader;
    private Button mBtnBack,mBtnMenu,mBtnSaveOrders,mBtnSaveOrdersComplete,mBtnReturnList;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

//    private WebserviceHelper webHelper = new WebserviceHelper();
//    private CheckNetwork chkNetwork = new CheckNetwork();
    GlobalObject ogject = GlobalObject.getInstance();

    private ArrayList<Order> mListOrderData = new ArrayList<Order>();
    //private List<Order> mListOrder = new List<Order>();
    private String mFilter="0",mInvoiceno,mSelectall="0",mSelect="";

    private RecyclerView lv;
    private RecyclerView.Adapter mAdapter;
    private Integer PositionSelect = 0,mDelCount = 0,selectCount = 0;

    private Intent myIntent=null;

    private GestureManager mGestureManager;
    DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders);


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

//            getOrder();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

//
//    @Override
//    public void onResume() {
//        super.onResume();
//
////        if(ogject.getSign().toString().equals("1"))
////        {
////            selectCount = 0;
////            mListOrderData = ogject.getListCheckOrderData();
////
////
////            mAdapter = new RVListDeliveryBWAdapter(getApplicationContext(),mListOrderData);
////            lv.setAdapter(mAdapter);
////            mAdapter.notifyDataSetChanged();
////
////            setSelectCount();
////        }
//
//
//
//            selectCount = 0;
//            mListOrderData = ogject.getListCheckOrderData();
//
//
//            mAdapter = new RVListDeliveryBWAdapter(getApplicationContext(),mListOrderData);
//            lv.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
//    }

    private void bindWidget()
    {
        try{
//            mImvHeader = (ImageView) findViewById(R.id.imvHeader);
//            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
//            mBtnBack = (Button) findViewById(R.id.btnBack);
//            mBtnRefrach = (Button) findViewById(R.id.btnRefrach);
//            mBtnSelectall = (Button) findViewById(R.id.btnSelectall);
//            mBtnSign = (Button) findViewById(R.id.btnSign);
//
//            mTxtOrderCount = (TextView) findViewById(R.id.txtOrderCount);
//            mTxtCount = (TextView) findViewById(R.id.txtCount);


            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnMenu = (Button) findViewById(R.id.btnMenu);

            mBtnSaveOrders = (Button) findViewById(R.id.btnOrdersWait);
            mBtnSaveOrdersComplete = (Button) findViewById(R.id.btnOrdersComptete);
            mBtnReturnList = (Button) findViewById(R.id.btnReturnList);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_list));


            lv = (RecyclerView) findViewById(R.id.lv);
//          lv.setLayoutManager(new LinearLayoutManager(this));
//          lv.setHasFixedSize(true);




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
            getInit();

            final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            lv.setHasFixedSize(true);
            lv.setLayoutManager(manager);

            final OrderAdapter adapter = new OrderAdapter(getApplicationContext(), R.layout.list_row_save_order_item);
            adapter.setData(mListOrderData);

            lv.setAdapter(adapter);
            lv.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<Order>() {

                @Override
                public boolean onItemClick(final Order item, final int position) {
                    //Snackbar.make(view, "Click event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Click event on the " + position  + " position", Toast.LENGTH_SHORT);
                    toast.show();

                    myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    return true;
                }

                @Override
                public void onItemLongPress(final Order item, final int position) {
                    //Snackbar.make(view, "Long press event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Long press event on the " + position + " position", Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public boolean onDoubleTap(final Order item, final int position) {
                    //Snackbar.make(view, "Double tap event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Double tap event on the " + position + " position", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }


            }));

            mGestureManager = new GestureManager.Builder(lv)
                    .setSwipeEnabled(false)
                    .setLongPressDragEnabled(true)
                    .setSwipeFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                    .setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
                    .setManualDragEnabled(true)
                    .build();

            adapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<Order>() {
                @Override
                public void onItemRemoved(final Order item, final int position) {
                    //Snackbar.make(view, "Month removed from position " + position, Snackbar.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Month removed from position " + position, Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void onItemReorder(final Order item, final int fromPos, final int toPos) {
                    //Snackbar.make(view, "Month moved from position " + fromPos + " to " + toPos, Snackbar.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Month moved from position " + fromPos + " to " + toPos, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

            mBtnMenu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    myIntent = new Intent(getApplicationContext(), OthersMenuActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

//            mBtnSaveOrders.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                }
//            });

            mBtnSaveOrders.setText("รอส่งข้อมูล\n(8/8)");

            mBtnSaveOrdersComplete.setText("ส่งข้อมูลได้\n(0/8)");

            mBtnReturnList.setText("ใบรับคืน\n(0/2)");


            mBtnSaveOrdersComplete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersCompleteActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(0,0);

////                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            mBtnReturnList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnListActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(0,0);
////                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

//            lv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
//                    startActivity(myIntent);
////                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                }
//            }));

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }



//    private void getOrder() {
//
//        try {
////            if(chkNetwork.isConnectionAvailable(getApplicationContext()))
////            {
////
////                if(chkNetwork.isWebserviceConnected(getApplicationContext()))
////                {
////
////                    new getOrderDataInAsync().execute();
////                }
////                else
////                {
////
////                    showMsgDialog(getResources().getString(R.string.error_webservice));
////
////                }
////
////            }else
////            {
////
////                showMsgDialog(getResources().getString(R.string.error_network));
////            }
//
//
//            new getOrderDataInAsync().execute();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//        private class getOrderDataInAsync extends AsyncTask<String, Void, PageResultHolder>
//        {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
////                mProgressDialog = new ACProgressFlower.Builder(SaveOrdersActivity.this)
////                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
////                        .themeColor(getResources().getColor(R.color.colorBackground))
////                        //.text(getResources().getString(R.string.progress_loading))
////                        .fadeColor(Color.DKGRAY).build();
////                mProgressDialog.show();
//
//
//                showMsgDialog("onPreExecute");
//            }
//
//            @Override
//            protected PageResultHolder doInBackground(String... params) {
//
//
//
//                // TODO Auto-generated method stub
//                PageResultHolder pageResultHolder = new PageResultHolder();
//                try
//                {
////                    SoapObject responseorder = new SoapObject();
////                    responseorder = webHelper.getOrderIntransit(ogject.getDate().toString(),ogject.getTruck().toString(),"1");
////
////                    mListOrderData.clear();
////                    for(int i=0; i<responseorder.getPropertyCount();i++){
////
////                        SoapObject array = (SoapObject)responseorder.getProperty(i);
////
////                        OrderData item = new OrderData();
////                        item.setRepcode(array.getPrimitivePropertySafelyAsString("repcode"));
////                        item.setRepname(array.getPrimitivePropertySafelyAsString("repname"));
////                        item.setRepaddr1(array.getPrimitivePropertySafelyAsString("repaddr1"));
////                        item.setRepaddr2(array.getPrimitivePropertySafelyAsString("repaddr2"));
////                        item.setRepaddr3(array.getPrimitivePropertySafelyAsString("repaddr3"));
////                        item.setRepaddr4(array.getPrimitivePropertySafelyAsString("repaddr4"));
////                        item.setRepphone(array.getPrimitivePropertySafelyAsString("repphone"));
////                        item.setRepmobile(array.getPrimitivePropertySafelyAsString("repmobile"));
////                        item.setInvoiceno(array.getPrimitivePropertySafelyAsString("invoiceno"));
////                        item.setCarton(array.getPrimitivePropertySafelyAsString("carton"));
////                        item.setBag(array.getPrimitivePropertySafelyAsString("bag"));
////                        item.setOtrstatus(array.getPrimitivePropertySafelyAsString("otrstatus"));
////                        item.setOtrdeliverystatus(array.getPrimitivePropertySafelyAsString("otrdeliverystatus"));
////                        item.setSelect("0");
////                        mListOrderData.add(item);
////                    }
//
//
////
////                        mListOrderData.clear();
////                        OrderData item = new OrderData();
////
////                        item.setRepcode("0181149095");
////                        item.setRepname("คุณศะศิรา โชติปรีชารัตน์");
////                        item.setRepaddr1("500 ร้านยา B.C ราชประสงค์เภสัช เพลินจิต (ชั้น2 )OK");
////                        item.setRepaddr2("");
////                        item.setRepaddr3("แขวงวังใหม่ เขตปทุมวัน");
////                        item.setRepaddr4("กรุงเทพมหานคร 10330");
////                        item.setRepphone("");
////                        item.setRepmobile("0875989989");
////                        item.setInvoiceno("1100863742");
////                        item.setCarton("1");
////                        item.setBag("0");
////                        item.setOtrstatus("02");
////                        item.setOtrdeliverystatus("02");
////                        item.setSelect("0");
////                        mListOrderData.add(item);
////
////                        item.setRepcode("0025117963");
////                        item.setRepname("คุณพรประภา อรุณสิทธิวงศ์");
////                        item.setRepaddr1("444เสื้อผ้าเด็กห้องB059000 ชั้น3 ซ.3 มาบุญครองโซนโรงแรม");
////                        item.setRepaddr2("");
////                        item.setRepaddr3("แขวงวังใหม่ เขตปทุมวัน");
////                        item.setRepaddr4("กรุงเทพมหานคร 10330");
////                        item.setRepphone("");
////                        item.setRepmobile("0890363176");
////                        item.setInvoiceno("1100870630");
////                        item.setCarton("1");
////                        item.setBag("0");
////                        item.setOtrstatus("02");
////                        item.setOtrdeliverystatus("02");
////                        item.setSelect("0");
////                        mListOrderData.add(item);
//
//                } catch (Exception e) {
//                    pageResultHolder.content = "Exception : CheckOrderData";
//                    pageResultHolder.exception = e;
//                }
//
//                return pageResultHolder;
//            }
//
//            @Override
//            protected void onPostExecute(final PageResultHolder result) {
//                // TODO Auto-generated method stub
//
//                try {
//
//                    if (result.exception != null) {
//                        mProgressDialog.dismiss();
//                        showMsgDialog(result.exception.toString());
//                    }
//                    else
//                    {
//
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //Do something after 100ms
//                                mProgressDialog.dismiss();
//
//                                if(mListOrderData.size()>0)
//                                {
////                                    bindData();
//                                }else
//                                {
//                                    //finish();
//                                    showMsgDialog(getResources().getString(R.string.error_data_not_in_system));
//                                }
//
//                            }
//                        }, 200);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//    private class PageResultHolder {
//        private String content;
//        private Exception exception;
//    }
//
//    private void bindData() {
//
//        try {
//
////            setSelectCount();
////
////            mAdapter = new RVListDeliveryBWAdapter(getApplicationContext(),mListOrderData);
////            lv.setAdapter(mAdapter);
////
////
////            lv.addOnItemTouchListener(
////                    new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
////                        @Override
////                        public void onItemClick(View view, int position) {
////                            // TODO Handle item click
////                            //String fscode = mListPhysicalCount.get(position).getFscode().toString();
////
////                            PositionSelect = position;
////                            mInvoiceno = mListOrderData.get(position).getInvoiceno().toString();
////                            mSelect = mListOrderData.get(position).getSelect().toString();
////
////                            if(mSelect.equals("0"))
////                            {
////                                mListOrderData.get(position).setSelect("1");
////                                selectCount = selectCount+1;
////                            }else
////                            {
////                                mListOrderData.get(position).setSelect("0");
////
////                                if(!selectCount.equals(0))
////                                {
////                                    selectCount = selectCount-1;
////                                }
////                            }
////
////                            mAdapter.notifyDataSetChanged();
////                            setSelectCount();
////
////
////                     /*   Toast toast = Toast.makeText(ListDeliveryBWActivity.this, mInvoiceno, Toast.LENGTH_SHORT);
////                        toast.show();*/
////                        }
////                    })
////            );
//
//
//
//
//
////            setSelectCount();
//
//            mAdapter = new RVListDeliveryBWAdapter(getApplicationContext(),mListOrderData);
//            lv.setAdapter(mAdapter);
//
//
//            lv.addOnItemTouchListener(
//                    new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            // TODO Handle item click
//                            //String fscode = mListPhysicalCount.get(position).getFscode().toString();
//
//                            PositionSelect = position;
//                            mInvoiceno = mListOrderData.get(position).getInvoiceno().toString();
//                            mSelect = mListOrderData.get(position).getSelect().toString();
//
//                            if(mSelect.equals("0"))
//                            {
//                                mListOrderData.get(position).setSelect("1");
////                                selectCount = selectCount+1;
//                            }else
//                            {
//                                mListOrderData.get(position).setSelect("0");
//
////                                if(!selectCount.equals(0))
////                                {
////                                    selectCount = selectCount-1;
////                                }
//                            }
//
//                            mAdapter.notifyDataSetChanged();
////                            setSelectCount();
//
//                        }
//                    })
//            );
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showMsgDialog(e.toString());
//        }
//    }

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


    private void getInit() {

        try {

            mListOrderData.clear();

            mHelper = new DBHelper(getApplicationContext());
            mListOrderData.clear();
            mListOrderData = mHelper.getOrderWaitList();



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

    private class getInitDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(SaveOrdersActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();

        }

        @Override
        protected PageResultHolder doInBackground(String... params) {
            // TODO Auto-generated method stub
            PageResultHolder pageResultHolder = new PageResultHolder();
            //String xmlInput = params[0];
            try
            {
                mListOrderData.clear();

                Order f = new Order();
                f.setTransNo("1");
                mListOrderData.add(f);

                f = new Order();
                f.setTransNo("2");
                mListOrderData.add(f);

                f = new Order();
                f.setTransNo("3");
                mListOrderData.add(f);

                f = new Order();
                f.setTransNo("4");
                mListOrderData.add(f);

                f = new Order();
                f.setTransNo("5");
                mListOrderData.add(f);

                f = new Order();
                f.setTransNo("6");
                mListOrderData.add(f);

                f = new Order();
                f.setTransNo("7");
                mListOrderData.add(f);

                f = new Order();
                f.setTransNo("8");
                mListOrderData.add(f);



            } catch (Exception e) {
                pageResultHolder.content = "Exception : CheckOrderData";
                pageResultHolder.exception = e;
            }

            return pageResultHolder;
        }

        @Override
        protected void onPostExecute(final PageResultHolder result) {
            // TODO Auto-generated method stub

            try {

                if (result.exception != null) {
                    mProgressDialog.dismiss();
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

                            if(mListOrderData.size()>0)
                            {

                                //mAdapter = new OrderSlipViewAdapter(getApplicationContext(),mListOrderData);
                                //lv.setAdapter(mAdapter);


                                //ogject.setListCheckOrderData(mListOrderData);

                                //bindData();

                               /* mAdapter = new RVGetOrderAdapter(getApplicationContext(),mListOrderData);
                                lv.setAdapter(mAdapter);


                                mTxtCount.setText(mDelCount+"/"+String.valueOf(mListOrderData.size()));*/



                            }else
                            {
                                //finish();
                                //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                                showMsgDialog(getResources().getString(R.string.error_data_not_in_system));

                            }

                        }
                    }, 200);

                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });*/

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

//    protected List<OrderItem> getMonths() {
//        final List<MonthItem> monthList = new ArrayList<>();
//        //monthList.add(new MonthHeader("First quarter"));
//        monthList.add(new Month("JAN", R.drawable.january));
//        monthList.add(new Month("FEB", R.drawable.february));
//        monthList.add(new Month("MAR", R.drawable.march));
//        //monthList.add(new MonthHeader("Second quarter"));
//        monthList.add(new Month("APR", R.drawable.april));
//        monthList.add(new Month("MAY", R.drawable.may));
//        monthList.add(new Month("JUN", R.drawable.june));
//        //monthList.add(new MonthHeader("Third quarter"));
//        monthList.add(new Month("JUL", R.drawable.july));
//        monthList.add(new Month("AUG", R.drawable.august));
//        monthList.add(new Month("SEP", R.drawable.september));
//        //monthList.add(new MonthHeader("Fourth quarter"));
//        monthList.add(new Month("OCT", R.drawable.october));
//        monthList.add(new Month("NOV", R.drawable.november));
//        monthList.add(new Month("DEC", R.drawable.december));
//
//        return monthList;
//    }




}

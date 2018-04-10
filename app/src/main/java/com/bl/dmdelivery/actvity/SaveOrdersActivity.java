package com.bl.dmdelivery.actvity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AndroidException;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.MenuSaveOrderViewAdapter;
import com.bl.dmdelivery.adapter.OrderAdapter;
import com.bl.dmdelivery.adapter.OrderSlipViewAdapter;
import com.bl.dmdelivery.adapter.RVListDeliveryBWAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.helper.GlobalObject;
import com.bl.dmdelivery.helper.TinyDB;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.BaseResponse;
import com.bl.dmdelivery.model.Delivery;
import com.bl.dmdelivery.model.MenuSaveOrder;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderData;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SaveOrdersActivity extends AppCompatActivity {


    private ACProgressFlower mProgressDialog;
//    private TextView mTxtMsg,mTxtHeader;
//    private Button mBtnBack;

//    private ImageView mImvHeader;
    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mmTxtMsg;
    private Button mBtnBack,mBtnMenu,mBtnSaveOrders,mBtnSaveOrdersComplete,mBtnReturnList,mBtnClose,mmBtnClose;
    private ImageView mmImvTitle;
    private EditText mEdtSearchWord;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

//    private WebserviceHelper webHelper = new WebserviceHelper();
//    private CheckNetwork chkNetwork = new CheckNetwork();
    GlobalObject ogject = GlobalObject.getInstance();

    private ArrayList<Order> mListOrderDataALL = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataY = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataN = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataNN = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataSend = new ArrayList<Order>();
    private ArrayList<OrderReturn> mListReturnDataALL = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListReturnDataY = new ArrayList<OrderReturn>();
    private ArrayList<MenuSaveOrder> mListMenuData = new ArrayList<MenuSaveOrder>();

    private ArrayList<Order> mListOrderDataWaitSend = new ArrayList<Order>();

    private ArrayList<OrderReturn> mListOrderReturnWaitSend = new ArrayList<OrderReturn>();

    private ArrayList<Delivery.ReturnOrder> mListOrderReturnSend = new ArrayList<Delivery.ReturnOrder>();

    private ArrayList<OrderReturn> mListOrderReturnItemSend = new ArrayList<OrderReturn>();

    private ArrayList<Delivery.ReturnOrder.ReturnItem> mOrderReturnItemSend = new ArrayList<Delivery.ReturnOrder.ReturnItem>();

    private ArrayList<Order> mListSearchDataN = new ArrayList<Order>();

    //private List<Order> mListOrder = new List<Order>();
    private String mFilter="0",mInvoiceno,mSelectall="0",mSelect="0";

    private Integer mSelectIndex = 0;

    String sigTruckNo = "";
    String sigDeliveryDate = "";

    private RecyclerView lv,lvmenu;
    private RecyclerView.Adapter mAdapter,mMenuAdapter;
    private ListView lvGetMenu=null;
    private Integer PositionSelect = 0,mDelCount = 0,selectCount = 0;

    private Intent myIntent=null;

    private GestureManager mGestureManager;
    DBHelper mHelper;

    Order mOrder;

    private OrderAdapter adapter,oldadapter;

    private boolean isResumeState = false;


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    private boolean isSelectAll = false;

    public boolean timerEnable = true;

    public Timer t = new Timer();

    public TimerTask task;

    private String serverUrl;
    private Button mBtnScan;

    private String mIsScan = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);


        try {

            bindWidget();
//
            setDefaultFonts();

            setWidgetControl();

//            getOrder();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms

                    task = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ( timerEnable == true ) {
                                        //sendSlip();
                                        new sendDataInAsync().execute();
                                    }
                                }
                            });
                        }
                    };



                    t.scheduleAtFixedRate(task, 0, 20000);

                }
            }, 10000);



            //sendSlip();
            //new sendDataInAsync().execute();




        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        timerEnable = true;




        setHeader();


        if(!mIsScan.equals("1"))
        {
            mEdtSearchWord.setText("");
            adapter.clearData();
            adapter.setData(mListOrderDataN);
            adapter.notifyDataSetChanged();
        }

        /*adapter.clearData();
        adapter.setData(mListOrderDataN);
        adapter.notifyDataSetChanged();*/

//        String resumeOrderList = sp.getString(TagUtils.PREF_RESUME_ORDER_LIST, "");
//        String selectOrderPosition = sp.getString(TagUtils.PREF_SELECT_ORDER_POSITION, "-1");
//
//
//        if(resumeOrderList.equals(""))
//        {
//
//        }
//        else
//        {
//            if(Integer.parseInt(selectOrderPosition) > -1)
//            {
//                //mListOrderDataN.get(Integer.parseInt(selectOrderPosition)).setIsselect("1");
//
//                editor = sp.edit();
//                editor.putString(TagUtils.PREF_RESUME_ORDER_LIST, "");
//                editor.apply();
//
//                setHeader();
//
//                mHelper = new DBHelper(getApplicationContext());
////
//                mListOrderDataNN.clear();
//                mListOrderDataNN = mHelper.getOrderWaitList("N");
//
//
//                mListOrderDataN.clear();
//                mListOrderDataN = mListOrderDataNN;
//
////                for(int i=0;i < mListOrderDataN.size(); i++)
////                {
////                    //int retval = mListOrderDataN.indexOf(mListOrderDataNN.get(i).getTransNo());
////
////
////                    for(int ii=0;ii < mListOrderDataNN.size(); ii++) {
////
////                        if(mListOrderDataNN.get(ii).getTransNo().equals(mListOrderDataN.get(i).getTransNo()))
////                        {
////                            mListOrderDataN.get(i).setDelivery_status("W");
////
//////                            Order ordersend = new Order();
//////                            ordersend = mListOrderDataN.get(i);
//////                            mListOrderDataN.remove(ordersend);
////
////
////
////                            //mAdapter.notifyItemRangeChanged(i, mListOrderDataN.size());
//////                            mListOrderDataN.remove(i);
//////                            adapter.notifyItemRemoved(i);
//////                            lv.removeViewAt(i);
////                            //mListOrderDataN.remove(i);
////                            adapter.notifyItemRemoved(i);
////
////                        }
////
////                    }
////
////
////                }
//
//                //adapter.notifyDataSetChanged();
//                //lv.invalidate();
//
//                adapter.clearData();
//                adapter.setData(mListOrderDataN);
//                adapter.notifyDataSetChanged();
//                //lv.setAdapter(adapter);
//                //lv.scrollToPosition(mSelectIndex);
//
//                //setWidgetControl();
//
//                //lv.notify();
//
////                Toast toast = Toast.makeText(SaveOrdersActivity.this, "onResume - Slip", Toast.LENGTH_SHORT);
////                toast.show();
//
//
//            }
//            else
//            {
//
//
//            }
//
//
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        timerEnable = false;

    }

    @Override
    protected void onDestroy() {
        //t.cancel();
        //Toast.makeText(this, "Timer cancel ", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


    private void bindWidget()
    {
        try{
            sigTruckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
            sigDeliveryDate = sp.getString(TagUtils.PREF_DELIVERY_DATE, "");


          //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnMenu = (Button) findViewById(R.id.btnMenu);
            mBtnClose = (Button) findViewById(R.id.btnClose);
            mBtnScan = (Button) findViewById(R.id.btnScan);


            mBtnSaveOrders = (Button) findViewById(R.id.btnOrdersWait);
            mBtnSaveOrdersComplete = (Button) findViewById(R.id.btnOrdersComptete);
            mBtnReturnList = (Button) findViewById(R.id.btnReturnList);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);



            lv = (RecyclerView) findViewById(R.id.lv);

            mEdtSearchWord = (EditText) findViewById(R.id.txtSearch);
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


    private void setDefaultFonts() {
        try {
            Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
            mEdtSearchWord.setTypeface(tf);
//            mBtnBack.setTypeface(tf);

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void setWidgetControl() {
        try{


            getInit();


            final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            lv.setHasFixedSize(true);
            lv.setLayoutManager(manager);

            adapter = new OrderAdapter(getApplicationContext(), R.layout.list_row_save_order_item);
            adapter.setData(mListOrderDataN);

            lv.setAdapter(adapter);
            lv.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<Order>() {

                @Override
                public boolean onItemClick(final Order item, int position) {
//                    //Snackbar.make(view, "Click event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Click event on the " + item.getItemno() + " position", Toast.LENGTH_SHORT);
                    toast.show();
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
//                    myIntent.putExtra("inv", "'" + mListOrderData.get(position).getTransNo() + "'");
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                    //showMsgUserSelectedMenuDialog(mListOrderData.get(position).getTransNo(),position);

                    //showMsgDialogSelectedMenu(position);

                    showMsgDialogSelectedMenu(position);



                    return true;
                }



                @Override
                public void onItemLongPress(final Order item, final int position) {
                    //Snackbar.make(view, "Long press event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Long press event on the " + position + " position", Toast.LENGTH_SHORT);
//                    toast.show();

                    //adapter.notifyDataSetChanged();

                    //showMsgDialogSelectedMenu(position);


                }

                @Override
                public boolean onDoubleTap(final Order item, int position) {
                    //Snackbar.make(view, "Double tap event on the " + position + " position", Snackbar.LENGTH_SHORT).show();

                    if (position == -1)
                    {
                        return false;
                    }
                    else
                    {
                        isSelectAll = true;

                        mSelectIndex = position;

                        mSelect = mListOrderDataN.get(position).getIsselect().toString();

                        if(mSelect.equals("0"))
                        {
                            mListOrderDataN.get(position).setIsselect("1");

                        }else
                        {
                            mListOrderDataN.get(position).setIsselect("0");

                        }

                        adapter.clearData();
                        adapter.setData(mListOrderDataN);
                        adapter.notifyDataSetChanged();


                        return true;
                    }
//                    showMsgDialogSelectedMenu(position);

//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Double tap event on the " + mListOrderDataN.get(position).getTransNo() + " position", Toast.LENGTH_SHORT);
//                    toast.show();
//
//                    isSelectAll = true;
//
//                    mSelectIndex = position;
//
//
//                    adapter.clearData();
//                    adapter.setData(mListOrderDataN);
//                    adapter.notifyDataSetChanged();
//

//                    for(int i=0; i<=mListOrderDataN.size()-1; i++){
//
//                        if(mListOrderDataN.get(position).getTransNo().equals(mListOrderDataN.get(i).getTransNo()))
//                        {
//                            Toast toast = Toast.makeText(SaveOrdersActivity.this, "Double tap event on the " + mListOrderDataN.get(position).getTransNo() + " position", Toast.LENGTH_SHORT);
//                            toast.show();
//
//                            mSelect = mListOrderDataN.get(i).getIsselect().toString();
//
//                                if(mSelect.equals("0"))
//                                {
//                                    mListOrderDataN.get(position).setIsselect("1");
//
//                                }else
//                                {
//                                    mListOrderDataN.get(position).setIsselect("0");
//
//                                }
//                        }
//                    }


//                    mSelect = mListOrderDataN.get(position).getIsselect().toString();
//
//                    if(mSelect.equals("0"))
//                    {
//                        mListOrderDataN.get(position).setIsselect("1");
//
//                    }else
//                    {
//                        mListOrderDataN.get(position).setIsselect("0");
//
//                    }
//
//                    adapter.clearData();
//                    adapter.setData(mListOrderDataN);
//                    adapter.notifyDataSetChanged();
//

//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Double tap event on the " + mListOrderDataN.get(position).getTransNo() + " position", Toast.LENGTH_SHORT);
//                    toast.show();
//
//                    if(position > -1)
//                    {
//
//                        for(int i=0; i<=mListOrderDataN.size()-1; i++){
//
//                            if(mListOrderDataN.get(position).getTransNo().equals(mListOrderDataN.get(i).getTransNo()))
//                            {
//                                mSelect = mListOrderDataN.get(i).getIsselect();
//
//                                if(mSelect.equals("0"))
//                                {
//                                    //mListOrderDataN.get(i).setIsselect("1");
//
//                                }else
//                                {
//                                    //mListOrderDataN.get(i).setIsselect("0");
//
//
//                                }
//
//                                //mListOrderDataN.iterator();
//                            }
//
//                        }
//
////                        mSelect = mListOrderDataN.get(position).getIsselect();
////
////                        if(mSelect.equals("0"))
////                        {
////                            mListOrderDataN.get(position).setIsselect("1");
////
////                        }else
////                        {
////                            mListOrderDataN.get(position).setIsselect("0");
////
////
////                        }
//
//                        //adapter.clearData();
//                        //adapter.setData(mListOrderDataN);
//                        adapter.notifyDataSetChanged();
//
//                        Toast toast = Toast.makeText(SaveOrdersActivity.this, "Double tap event on the " + mListOrderDataN.get(position).getTransNo() + " position", Toast.LENGTH_SHORT);
//                        toast.show();
//                    }else
//                    {
//                        Toast toast = Toast.makeText(SaveOrdersActivity.this, "Double tap event Error position", Toast.LENGTH_SHORT);
//                        toast.show();
//                    }



                    //adapter.notifyDataSetChanged();

//                    mSelect = mListOrderDataN.get(position).getIsselect();
//
//                    if(mSelect.equals("0"))
//                    {
//                        mListOrderDataN.get(position).setIsselect("1");
//
//                    }else
//                    {
//                        mListOrderDataN.get(position).setIsselect("0");
//
//
//                    }

                    //adapter.setData(mListOrderDataN);
                    //adapter.notifyDataSetChanged();



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
//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Month removed from position " + position, Toast.LENGTH_SHORT);
//                    toast.show();
                }

                @Override
                public void onItemReorder(final Order item, final int fromPos, final int toPos) {
                    //Snackbar.make(view, "Month moved from position " + fromPos + " to " + toPos, Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "moved from position " + fromPos + " to " + toPos, Toast.LENGTH_SHORT);
//                    toast.show();


//                    Toast toast2 = Toast.makeText(SaveOrdersActivity.this,adapter.getItem(fromPos).getRep_name(), Toast.LENGTH_SHORT);
//                    toast2.show();



                    if (fromPos <= toPos) {
                        Collections.rotate(mListOrderDataN.subList(fromPos, toPos + 1), -1);
                    } else {
                        Collections.rotate(mListOrderDataN.subList(toPos, fromPos + 1), 1);
                    }


//                    oldadapter = new OrderAdapter(getApplicationContext(), R.layout.list_row_save_order_item);
//                    oldadapter = adapter;


                    adapter.notifyDataSetChanged();

                    mHelper = new DBHelper(getApplicationContext());
                    mHelper.update_Arrange_Items_That_users_Call(adapter);
                }
            });

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    //t.cancel();
                }
            });

            mBtnMenu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
//                    myIntent = new Intent(getApplicationContext(), OthersMenuActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    showMsgDialogMenu();
                }
            });


//            mBtnSaveOrders.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                }
//            });




            mBtnSaveOrdersComplete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersCompleteActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(0,0);
                    //t.cancel();

////                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            mBtnReturnList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnListActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(0,0);
                    //t.cancel();
////                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            mEdtSearchWord.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    String text = mEdtSearchWord.getText().toString().toLowerCase(Locale.getDefault());
                    filter(text);

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub
                }
            });

            mBtnScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mIsScan = "1";

                   /* IntentIntegrator integrator = new IntentIntegrator(SaveOrdersActivity.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                    integrator.setPrompt("Scan a barcode");
                    integrator.setCameraId(0);  // Use a specific camera of the device
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(true);
                    integrator.initiateScan();*/
                    new IntentIntegrator(SaveOrdersActivity.this).initiateScan();

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

    private class sendDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            mProgressDialog = new ACProgressFlower.Builder(MainMenuActivity.this)
//                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
//                    .text(getResources().getString(R.string.progress_loading))
//                    .themeColor(getResources().getColor(R.color.colorBackground))
//                    //.text(getResources().getString(R.string.progress_loading))
//                    .fadeColor(Color.DKGRAY).build();
//            mProgressDialog.show();

        }

        @Override
        protected PageResultHolder doInBackground(String... params) {
            // TODO Auto-generated method stub
            PageResultHolder pageResultHolder = new PageResultHolder();
            //String xmlInput = params[0];
            try
            {

                sendSlip();

                pageResultHolder.content = "";



            } catch (Exception e) {
                pageResultHolder.content = "Exception : NoData";
                pageResultHolder.exception = e;
            }

            return pageResultHolder;
        }

        @Override
        protected void onPostExecute(final PageResultHolder result) {
            // TODO Auto-generated method stub

            //final String msg = "";

            try {


                setHeader();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class PageResultHolder {
        private String content;
        private Exception exception;
    }


    public void sendSlip() {


        String inputPath = Environment.getExternalStorageDirectory().toString()+"/DMSLIP/";
        String outputPath = Environment.getExternalStorageDirectory().toString()+"/DMPROCESSED/";
        String inputPathReturn = Environment.getExternalStorageDirectory().toString()+"/DMSLIPRETURN/";
        String outputPathReturn = Environment.getExternalStorageDirectory().toString()+"/DMRETURNPROCESSED/";


        File f = new File(inputPath);
        File file[] = f.listFiles();


        try
        {

            mHelper = new DBHelper(getApplicationContext());


            mListOrderDataWaitSend.clear();
            mListOrderDataWaitSend = mHelper.getOrderWaitListToServer();


            serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/DeliveryOrders";



            for(int i=0; i<mListOrderDataWaitSend.size(); i++){


                Delivery delivery = new Delivery();

                delivery.setTruck_no(mListOrderDataWaitSend.get(i).getTruckNo());
                delivery.setDelivery_date(mListOrderDataWaitSend.get(i).getDelivery_date());
                delivery.setTrans_year(mListOrderDataWaitSend.get(i).getYear());
                delivery.setTrans_group(mListOrderDataWaitSend.get(i).getGroup());
                delivery.setTrans_no(mListOrderDataWaitSend.get(i).getTransNo());
                delivery.setRoute_seq(String.valueOf(mListOrderDataWaitSend.get(i).getItemno()));
                delivery.setRep_code(mListOrderDataWaitSend.get(i).getRep_code());
                delivery.setLat(mListOrderDataWaitSend.get(i).getLat());
                delivery.setLon(mListOrderDataWaitSend.get(i).getLon());
                delivery.setSignature_timestamp(mListOrderDataWaitSend.get(i).getSignature_timestamp());
                delivery.setReason_code(mListOrderDataWaitSend.get(i).getReason_code());
                delivery.setReason_note(mListOrderDataWaitSend.get(i).getReason_note());
                delivery.setDelivery_status(mListOrderDataWaitSend.get(i).getSend_status());
                delivery.setMobile_serial(mListOrderDataWaitSend.get(i).getMobile_serial());
                delivery.setMobile_emei(mListOrderDataWaitSend.get(i).getMobile_emei());
                delivery.setMobile_battery(mListOrderDataWaitSend.get(i).getMobile_battery());
                delivery.setApp_version(getResources().getString(R.string.app_version_slip));
                delivery.setImage_signature(getBytesFromBitmap(inputPath + mListOrderDataWaitSend.get(i).getFullpathimage()));

                mListOrderReturnWaitSend.clear();
                mListOrderReturnWaitSend = mHelper.getOrdersReturnListByRepCode(mListOrderDataWaitSend.get(i));


                if(mListOrderReturnWaitSend.size() > 0)
                {
                    Delivery.ReturnOrder returnOrderdelivery = new Delivery.ReturnOrder();
                    Delivery.ReturnOrder.ReturnItem orderReturnItem = new Delivery.ReturnOrder.ReturnItem();


                    for(int x=0; x<mListOrderReturnWaitSend.size(); x++){


                        returnOrderdelivery.setTrack_no(mListOrderReturnWaitSend.get(x).getTrack_no());
                        returnOrderdelivery.setDelivery_date(mListOrderDataWaitSend.get(i).getDelivery_date());
                        returnOrderdelivery.setReturn_no(mListOrderReturnWaitSend.get(x).getReturn_no());
                        returnOrderdelivery.setRep_code(mListOrderReturnWaitSend.get(x).getRep_code());
                        returnOrderdelivery.setReturn_status(mListOrderReturnWaitSend.get(x).getReturn_status());
                        returnOrderdelivery.setReturn_reason(mListOrderReturnWaitSend.get(x).getReason_code());
                        returnOrderdelivery.setReturn_notes(mListOrderReturnWaitSend.get(x).getReturn_note());
                        returnOrderdelivery.setLat(mListOrderReturnWaitSend.get(x).getLat());
                        returnOrderdelivery.setLon(mListOrderReturnWaitSend.get(x).getLon());
                        returnOrderdelivery.setSignature_timestamp(mListOrderReturnWaitSend.get(x).getSignature_timestamp());
                        //returnOrderdelivery.setSignature_image(getBytesFromBitmap(inputPathReturn + mListOrderReturnWaitSend.get(x).getFullpathimage()));


                        mListOrderReturnItemSend.clear();
                        mListOrderReturnItemSend = mHelper.getOrderReturnDtl(returnOrderdelivery.getReturn_no());


                        for(int y=0; y<mListOrderReturnItemSend.size(); y++){

                            orderReturnItem.setFscode(mListOrderReturnItemSend.get(y).getFs_code());
                            orderReturnItem.setFsdesc(mListOrderReturnItemSend.get(y).getFs_desc());
                            orderReturnItem.setReturn_request(mListOrderReturnItemSend.get(y).getReturn_unit());
                            orderReturnItem.setReturn_actual(mListOrderReturnItemSend.get(y).getReturn_unit_real());

                            mOrderReturnItemSend.add(orderReturnItem);

                        }

                        returnOrderdelivery.setReturn_item(mOrderReturnItemSend);

                    }

                    mListOrderReturnSend.add(returnOrderdelivery);

                }




                delivery.setReturn_order(mListOrderReturnSend);


                Gson gson = new Gson();
                String json = gson.toJson(delivery);
                String result = new WebServiceHelper().postServiceAPI(serverUrl,json);
                BaseResponse obj = gson.fromJson(result,BaseResponse.class);


                //Toast.makeText(this, "Process slip : " + obj.getResponseCode(), Toast.LENGTH_SHORT).show();

                if(obj.getResponseCode().equals("1"))
                {

                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
                    java.util.Date currentLocalTime = cal.getTime();
                    SimpleDateFormat date = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");
                    date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    String localTime = date.format(currentLocalTime);
                    localTime = localTime.replace(" ", "").replace("-", "");

                    mListOrderDataWaitSend.get(i).setDelivery_status("Y");
                    mListOrderDataWaitSend.get(i).setSendtoserver_timestamp(localTime);

                    boolean isRes = true;
                    mHelper = new DBHelper(getApplicationContext());
                    isRes = mHelper.updateOrderDeliveryStatusToServer(mListOrderDataWaitSend);


                    moveSlip(inputPath,mListOrderDataWaitSend.get(i).getFullpathimage(),outputPath);




                    //Toast.makeText(this, "Process slip : " + path + mListOrderDataWaitSend.get(i).getFullpathimage(), Toast.LENGTH_SHORT).show();
                }



                //Toast.makeText(this, "Process slip : " + getBytesFromBitmap(path + mListOrderDataWaitSend.get(i).getFullpathimage()), Toast.LENGTH_LONG).show();


            }



        } catch (Exception e) {

        }


    }


    public String getBytesFromBitmap(String filepath) {
        File imagefile = new File(filepath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);
        return encImage;
    }



    private void moveSlip(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void callPhone(String name,String tel) {

        try {

            if(!contactExists(tel))
            {
                //addContact
                addContact(name,tel);
            }



            Intent intent = new Intent(Intent.ACTION_CALL);
            //String[] tel1 = tel.split(",");
            if (tel.equals("")) {

            }
            else
            {
//                intent.setData(Uri.parse("tel:" + tel));
//                startActivity(intent);

                showMsgDialog("tel:" + tel);

            }


        } catch (SecurityException e) {
            // e.printStackTrace();
            showMsgDialog(e.toString());
        }

    }

    public boolean contactExists(String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = this.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }

    public void addContact(String name, String phone)
    {
        String fname = name;
        String fphone = phone;
        String mask = Character.toString((char)10)+":";

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, fname+mask).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fphone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
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

    public void showMsgDialogSelectedMenu(final int selectedPosition)
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_menu_order_save, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);

        lvmenu = (RecyclerView) v.findViewById(R.id.lvmenu);
        lvmenu.setLayoutManager(new LinearLayoutManager(this));
        lvmenu.setHasFixedSize(true);

        //mListMenuData

        mListMenuData.clear();

        MenuSaveOrder f1 = new MenuSaveOrder();
        f1.setMenuname("เซ็นรับสินค้า");
        f1.setMenuname_type("0");
        f1.setMenuname_mode("0");
        mListMenuData.add(f1);


        MenuSaveOrder f2 = new MenuSaveOrder();
        f2.setMenuname("กิจกรรม");
        f2.setMenuname_type("1");
        f2.setMenuname_mode("0");
        mListMenuData.add(f2);

//        MenuSaveOrder f3 = new MenuSaveOrder();
//        f3.setMenuname("เลือกทุกออเดอร์");
//        f3.setMenuname_type("3");
//        f3.setMenuname_mode("0");
//        mListMenuData.add(f3);


        final String[] telsMSL = mListOrderDataN.get(selectedPosition).getRep_telno().split(",");
        final String[] telsDSM = mListOrderDataN.get(selectedPosition).getDsm_telno().split(",");


        if(telsMSL.length>0)
        {

            if (telsMSL[0].toString().trim().equals(""))
            {
                //mLnlTel1.setVisibility(View.GONE);
                //mLnlTel2.setVisibility(View.GONE);
            }
            else
            {
                if(telsMSL.length>=2)
                {
                    MenuSaveOrder msl1 = new MenuSaveOrder();
                    msl1.setMenuname("MSL1 : "+ telsMSL[0]);
                    msl1.setMenuname_type("2");
                    msl1.setMenuname_mode("0");
                    mListMenuData.add(msl1);

                    MenuSaveOrder msl2 = new MenuSaveOrder();
                    msl2.setMenuname("MSL2 : "+ telsMSL[1]);
                    msl2.setMenuname_type("2");
                    msl2.setMenuname_mode("0");
                    mListMenuData.add(msl2);
                }
                else
                {


                    MenuSaveOrder msl1 = new MenuSaveOrder();
                    msl1.setMenuname("MSL : "+ telsMSL[0]);
                    msl1.setMenuname_type("2");
                    msl1.setMenuname_mode("0");
                    mListMenuData.add(msl1);

                }
            }

        }

        if(telsDSM.length>0)
        {

            if (telsDSM[0].toString().trim().equals(""))
            {
                //mLnlTel1.setVisibility(View.GONE);
                //mLnlTel2.setVisibility(View.GONE);
            }
            else
            {
                if(telsDSM.length>=2)
                {
                    MenuSaveOrder dsm1 = new MenuSaveOrder();
                    dsm1.setMenuname("DSM1 : "+ telsDSM[0]);
                    dsm1.setMenuname_type("2");
                    dsm1.setMenuname_mode("0");
                    mListMenuData.add(dsm1);

                    MenuSaveOrder dsm2 = new MenuSaveOrder();
                    dsm2.setMenuname("DSM2 : "+ telsDSM[1]);
                    dsm2.setMenuname_type("2");
                    dsm2.setMenuname_mode("0");
                    mListMenuData.add(dsm2);
                }
                else
                {


                    MenuSaveOrder dsm1 = new MenuSaveOrder();
                    dsm1.setMenuname("DSM : "+ telsDSM[0]);
                    dsm1.setMenuname_type("2");
                    dsm1.setMenuname_mode("0");
                    mListMenuData.add(dsm1);

                }
            }

        }



//        MenuSaveOrder f3 = new MenuSaveOrder();
//        f3.setMenuname("โทร MSL : 0983939393");
//        f3.setMenuname_type("2");
//        f3.setMenuname_mode("0");
//        mListMenuData.add(f3);

//        MenuSaveOrder f4 = new MenuSaveOrder();
//        f4.setMenuname("โทร DSM : 0874848949");
//        f4.setMenuname_type("2");
//        f4.setMenuname_mode("0");
//        mListMenuData.add(f4);


        mMenuAdapter = new MenuSaveOrderViewAdapter(getApplicationContext(),mListMenuData);
        lvmenu.setAdapter(mMenuAdapter);


        lvmenu.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                switch (mListMenuData.get(position).getMenuname_type()){
                    case "0":
                        //จัดส่งสินค้า

                        editor = sp.edit();
                        editor.putString(TagUtils.PREF_RESUME_ORDER_LIST, "1");
                        editor.putString(TagUtils.PREF_SELECT_ORDER_POSITION, String.valueOf(selectedPosition));
                        editor.apply();

                        DialogBuilder.dismiss();

                        mOrder = new Order();
                        mOrder.setRep_code(mListOrderDataN.get(selectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderDataN.get(selectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderDataN.get(selectedPosition).getTransNo());
                        mOrder.setAddress1(mListOrderDataN.get(selectedPosition).getAddress1());
                        mOrder.setAddress2(mListOrderDataN.get(selectedPosition).getAddress2());
                        mOrder.setPostal(mListOrderDataN.get(selectedPosition).getPostal());
                        mOrder.setRep_telno(mListOrderDataN.get(selectedPosition).getRep_telno());
                        mOrder.setReturn_flag(mListOrderDataN.get(selectedPosition).getReturn_flag());
                        mOrder.setCont_desc(mListOrderDataN.get(selectedPosition).getCont_desc());
                        mOrder.setDelivery_status("W");
                        ArrayList<Order> order = new ArrayList<Order>();
                        order.add(mOrder);

                        //TinyDB tinydb = new TinyDB(getApplicationContext());




                        myIntent = new Intent(getApplicationContext(), SaveOrdersApproveSlipActivity.class);
                        myIntent.putExtra("data",order);
                        startActivity(myIntent);
                        break;
                    case "1":
                        //กิจกรรมอื่นๆ
                        DialogBuilder.dismiss();

                        mOrder = new Order();
                        mOrder.setRep_code(mListOrderDataN.get(selectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderDataN.get(selectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderDataN.get(selectedPosition).getTransNo());
                        mOrder.setDelivery_date(sigDeliveryDate);
                        mOrder.setTruckNo(sigTruckNo);
                        mOrder.setRep_telno(mListOrderDataN.get(selectedPosition).getRep_telno());
                        mOrder.setDsm_telno(mListOrderDataN.get(selectedPosition).getDsm_telno());

                        myIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                        myIntent.putExtra("data",mOrder);
                        startActivity(myIntent);
                        break;

                    case "2":
                        //โทร
                        DialogBuilder.dismiss();

                        String repcode = mListOrderDataN.get(selectedPosition).getRep_code();


                        String[] Tels = mListMenuData.get(position).getMenuname().toString().trim().split(":");
                        String name = Tels[0].toString();
                        String phone = Tels[1].toString();


                        callPhone(name+setRepcodeFormat(repcode),phone);

                        break;

                    default:

                        DialogBuilder.dismiss();
                        showMsgDialog("default");


                }

            }
        }));

//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mmTxtMsg.setTypeface(tf);
//        mmTxtTitle.setTypeface(tf);
//        mmBtnClose.setTypeface(tf);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText("ออเดอร์ : "+mListOrderDataN.get(selectedPosition).getTransNo());
        //mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }

    private String setRepcodeFormat(String repcode) {


        try {

            String repcodeformat = "";

            if(repcode.length() == 10)

            {
                repcodeformat = repcode.substring(0, 4)+"-"+repcode.substring(4, 9)+"-"+repcode.substring(9, 10);
            }
            else
            {
                repcodeformat = repcode;
            }

            return repcodeformat;


        } catch (Exception e)
        {
            return repcode;
            //showMsgDialog(e.toString());
        }

    }

    public void showMsgDialogMenu()
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_menu_order_save, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);

        lvmenu = (RecyclerView) v.findViewById(R.id.lvmenu);
        lvmenu.setLayoutManager(new LinearLayoutManager(this));
        lvmenu.setHasFixedSize(true);

        //mListMenuData

        mListMenuData.clear();

        MenuSaveOrder f1 = new MenuSaveOrder();
        f1.setMenuname("เซ็นรับสินค้า");
        f1.setMenuname_type("0");
        f1.setMenuname_mode("0");
        mListMenuData.add(f1);


        MenuSaveOrder f2 = new MenuSaveOrder();
        f2.setMenuname("กิจกรรม");
        f2.setMenuname_type("1");
        f2.setMenuname_mode("0");
        mListMenuData.add(f2);


        String selectText ="";
        String selectType ="";

        if(isSelectAll == false)
        {
            selectText = getResources().getString(R.string.btn_text_select);
            selectType = "3";
        }
        else
        {
            selectText = getResources().getString(R.string.btn_text_noselect);
            selectType = "4";
        }


        MenuSaveOrder f3 = new MenuSaveOrder();
        f3.setMenuname(selectText);
        f3.setMenuname_type(selectType);
        f3.setMenuname_mode("0");
        mListMenuData.add(f3);



        mMenuAdapter = new MenuSaveOrderViewAdapter(getApplicationContext(),mListMenuData);
        lvmenu.setAdapter(mMenuAdapter);


        lvmenu.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                switch (mListMenuData.get(position).getMenuname_type()){
                    case "0":
                        //จัดส่งสินค้า
                        DialogBuilder.dismiss();

                        ArrayList<Order> order = new ArrayList<Order>();

                        for(int i=0; i<mListOrderDataN.size();i++){

                            if(mListOrderDataN.get(i).getIsselect().equals("1"))
                            {
                                mOrder = new Order();
                                mOrder.setRep_code(mListOrderDataN.get(i).getRep_code());
                                mOrder.setRep_name(mListOrderDataN.get(i).getRep_name());
                                mOrder.setTransNo(mListOrderDataN.get(i).getTransNo());
                                mOrder.setAddress1(mListOrderDataN.get(i).getAddress1());
                                mOrder.setAddress2(mListOrderDataN.get(i).getAddress2());
                                mOrder.setPostal(mListOrderDataN.get(i).getPostal());
                                mOrder.setRep_telno(mListOrderDataN.get(i).getRep_telno());
                                mOrder.setReturn_flag(mListOrderDataN.get(i).getReturn_flag());
                                mOrder.setCont_desc(mListOrderDataN.get(i).getCont_desc());
                                mOrder.setDelivery_status("W");

                                order.add(mOrder);
                            }


                        }

                        //TinyDB tinydb = new TinyDB(getApplicationContext());


                        if(order.size() > 0)
                        {
                            myIntent = new Intent(getApplicationContext(), SaveOrdersApproveSlipActivity.class);
                            myIntent.putExtra("data",order);
                            startActivity(myIntent);
                        }
                        else
                        {
                            showMsgDialog("กรุณาเลือกออเดอร์");
                        }


                        break;
                    case "1":
                        //กิจกรรมอื่นๆ
                        DialogBuilder.dismiss();

                        mOrder = new Order();
                        mOrder.setRep_code(mListOrderDataN.get(0).getRep_code());
                        mOrder.setRep_name(mListOrderDataN.get(0).getRep_name());
                        mOrder.setTransNo(mListOrderDataN.get(0).getTransNo());
                        mOrder.setDelivery_date(sigDeliveryDate);
                        mOrder.setTruckNo(sigTruckNo);
                        mOrder.setRep_telno(mListOrderDataN.get(0).getRep_telno());
                        mOrder.setDsm_telno(mListOrderDataN.get(0).getDsm_telno());

                        myIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                        myIntent.putExtra("data",mOrder);
                        startActivity(myIntent);
                        break;

                    case "2":
                        //โทร
                        DialogBuilder.dismiss();

                        showMsgDialog(mListMenuData.get(position).getMenuname());

                        break;

                    case "3":
                        //โทร
                        DialogBuilder.dismiss();


                        for(int i=0; i<mListOrderDataN.size();i++){

                            mListOrderDataN.get(i).setIsselect("1");
                        }

                        adapter.notifyDataSetChanged();

                        if(isSelectAll == true)
                        {
                            isSelectAll = false;
                        }else
                        {
                            isSelectAll = true;
                        }

                        break;

                    case "4":
                        //โทร
                        DialogBuilder.dismiss();

                        for(int i=0; i<mListOrderDataN.size();i++){

                            mListOrderDataN.get(i).setIsselect("0");

                        }

                        adapter.notifyDataSetChanged();

                        if(isSelectAll == true)
                        {
                            isSelectAll = false;
                        }else
                        {
                            isSelectAll = true;
                        }

                        break;

                    default:

                        DialogBuilder.dismiss();
                        showMsgDialog("default");


                }

            }
        }));

//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mmTxtMsg.setTypeface(tf);
//        mmTxtTitle.setTypeface(tf);
//        mmBtnClose.setTypeface(tf);

        mmImvTitle.setImageResource(R.mipmap.ic_launcher);
        mmTxtTitle.setText("เมนู");
        //mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }


    private void getInit() {

        try {




            mHelper = new DBHelper(getApplicationContext());


            mListOrderDataN.clear();
            mListOrderDataN = mHelper.getOrderWaitList("N");

            mListSearchDataN.clear();
            mListSearchDataN = mHelper.getOrderWaitList("N");


//            String text = mEdtSearchWord.getText().toString().toLowerCase(Locale.getDefault());
//            filter(text);


        } catch (Exception e) {
            e.printStackTrace();
            showMsgDialog(e.toString());
        }

    }

    private void setHeader() {

        try {


            mHelper = new DBHelper(getApplicationContext());

            mListOrderDataALL.clear();
            mListOrderDataALL = mHelper.getOrderWaitList("ALL");

            mListOrderDataY.clear();
            mListOrderDataY = mHelper.getOrderWaitList("SWY");

            mListOrderDataN.clear();
            mListOrderDataN = mHelper.getOrderWaitList("N");

            mListSearchDataN.clear();
            mListSearchDataN = mHelper.getOrderWaitList("N");

            mListOrderDataSend.clear();
            mListOrderDataSend = mHelper.getOrderWaitList("Y");

            mListReturnDataALL.clear();
            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            mListReturnDataY.clear();
            mListReturnDataY = mHelper.getOrdersReturnListSummary("Y");

            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_send_data)+" ("+mListOrderDataSend.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrders.setText("ยังไม่บันทึกผล\n("+mListOrderDataN.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrdersComplete.setText("บันทึกผลแล้ว\n("+mListOrderDataY.size()+"/"+mListOrderDataALL.size()+")");

            mBtnReturnList.setText("ใบรับคืน\n("+mListReturnDataY.size()+"/"+mListReturnDataALL.size()+")");


        } catch (Exception e) {
            e.printStackTrace();
            showMsgDialog(e.toString());
        }

    }

    private void filter(String charText) {

        mListOrderDataN.clear();
        if (charText.toLowerCase(Locale.getDefault()).equals("")) {
            mListOrderDataN.addAll(mListSearchDataN);
            mFilter = "0";
        }
        else
        {
            for (Order wp : mListSearchDataN)
            {
                if (wp.getTransNo().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mListOrderDataN.add(wp);
                }else
                {
                    if (wp.getRep_code().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        mListOrderDataN.add(wp);
                    }else
                    {
                        if (wp.getRep_name().toLowerCase(Locale.getDefault()).contains(charText))
                        {
                            mListOrderDataN.add(wp);
                        }else
                        {
                            if (wp.getAddress1().toLowerCase(Locale.getDefault()).contains(charText))
                            {
                                mListOrderDataN.add(wp);
                            }else
                            {
                                if (wp.getAddress2().toLowerCase(Locale.getDefault()).contains(charText))
                                {
                                    mListOrderDataN.add(wp);
                                }
                            }
                        }
                    }
                }
            }

            mFilter = "1";
        }

        adapter.clearData();
        adapter.setData(mListOrderDataN);
        adapter.notifyDataSetChanged();

    }


    private void removeData() {

        try {

            for(int i = mListOrderDataN.size()-1 ; i >= 0; i--)
            {

                mSelect = mListOrderDataN.get(i).getIsselect().toString();

                if(mSelect.equals("0"))
                {
                    //mListSearchConfirmOrder.remove(mListConfirmOrder.get(i));
                    mListOrderDataN.remove(mListOrderDataN.get(i));
                }

            }

            /*for(int i=0; i<mListConfirmOrder.size();i++){

                mOucode = mListConfirmOrder.get(i).getOucode().toString();
                mYear = mListConfirmOrder.get(i).getYear().toString();
                mGroup = mListConfirmOrder.get(i).getGroup().toString();
                mInv = mListConfirmOrder.get(i).getInv().toString();
                mMailgroup = mListConfirmOrder.get(i).getMailgroup().toString();
                mRepcode = mListConfirmOrder.get(i).getRepcode().toString();
                mRepname = mListConfirmOrder.get(i).getRepname().toString();
                mTel = mListConfirmOrder.get(i).getTel().toString();
                mSelect = mListConfirmOrder.get(i).getSelect().toString();


                if(mSelect.equals("1"))
                {
                    mListConfirmOrder.remove(this);
                }

            }*/

            mAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            int hasCameraPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//
//            List<String> permissions = new ArrayList<String>();
//
//            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//
//            }
//            if (!permissions.isEmpty()) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
//            }
//
//
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 111: {
//                for (int i = 0; i < permissions.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
//
//
//                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
//
//                    }
//                }
//            }
//            break;
//            default: {
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            }
//        }
//    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                mEdtSearchWord.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}

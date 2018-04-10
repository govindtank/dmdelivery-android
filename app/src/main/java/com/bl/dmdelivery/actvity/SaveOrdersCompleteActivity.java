package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.MenuSaveOrderViewAdapter;
import com.bl.dmdelivery.adapter.OrderAdapter;
import com.bl.dmdelivery.adapter.OrderCompleteAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.BaseResponse;
import com.bl.dmdelivery.model.Delivery;
import com.bl.dmdelivery.model.MenuSaveOrder;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;
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
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class SaveOrdersCompleteActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mmTxtMsg;
    private Button mBtnBack,mBtnMenu,mBtnSaveOrders,mBtnSaveOrdersComplete,mBtnReturnList,mBtnClose,mmBtnClose;
    private ImageView mmImvTitle;
    private EditText mEdtSearchWord;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    String sigTruckNo = "";
    String sigDeliveryDate = "";

    private Intent myIntent=null;

    private ArrayList<Order> mListOrderDataWaitSend = new ArrayList<Order>();

    private ArrayList<OrderReturn> mListOrderReturnWaitSend = new ArrayList<OrderReturn>();

    private ArrayList<Delivery.ReturnOrder> mListOrderReturnSend = new ArrayList<Delivery.ReturnOrder>();

    private ArrayList<OrderReturn> mListOrderReturnItemSend = new ArrayList<OrderReturn>();

    private ArrayList<Delivery.ReturnOrder.ReturnItem> mOrderReturnItemSend = new ArrayList<Delivery.ReturnOrder.ReturnItem>();

    //private ArrayList<Order> mListOrderData = new ArrayList<Order>();

    private ArrayList<Order> mListOrderDataALL = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataY = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataYY = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataN = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataNN = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataSend = new ArrayList<Order>();
    private ArrayList<OrderReturn> mListReturnDataALL = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListReturnDataY = new ArrayList<OrderReturn>();


    private ArrayList<MenuSaveOrder> mListMenuData = new ArrayList<MenuSaveOrder>();
    private RecyclerView lv,lvmenu;
    private RecyclerView.Adapter mAdapter,mMenuAdapter;
    private GestureManager mGestureManager;
    DBHelper mHelper;

    private OrderCompleteAdapter adapter;


    Order mOrder;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    public boolean timerEnable = true;

    public Timer t = new Timer();

    public TimerTask task;

    private String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_complete);


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

//            getOrder();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        timerEnable = true;

        mEdtSearchWord.setText("");

        setHeader();

        adapter.clearData();
        adapter.setData(mListOrderDataY);
        adapter.notifyDataSetChanged();

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
//                mListOrderDataNN = mHelper.getOrderWaitList("WY");
//
//                mListOrderDataY.clear();
//                mListOrderDataY = mListOrderDataNN;
//
////                for(int i=0;i < mListOrderDataY.size(); i++)
////                {
////                    //int retval = mListOrderDataN.indexOf(mListOrderDataNN.get(i).getTransNo());
////
////
////                    for(int ii=0;ii < mListOrderDataNN.size(); ii++) {
////
////                        if(mListOrderDataNN.get(ii).getTransNo().equals(mListOrderDataY.get(i).getTransNo()))
////                        {
////                            mListOrderDataY.get(i).setDelivery_status("W");
////
////                            //mListOrderDataN.remove(i);
////                            //mListOrderDataN.remove(i);
////
////                        }
////
////                    }
////
////
////                }
//
//                //adapter.notifyDataSetChanged();
//
//                adapter.clearData();
//                adapter.setData(mListOrderDataY);
//                adapter.notifyDataSetChanged();
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
            //mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_complete_list));

            lv = (RecyclerView) findViewById(R.id.lv);

            mBtnMenu.setVisibility(View.INVISIBLE);

            mEdtSearchWord = (EditText) findViewById(R.id.txtSearch);

//            lv = (RecyclerView) findViewById(R.id.lv);
//
//            lv.setLayoutManager(new LinearLayoutManager(this));
//            lv.setHasFixedSize(true);
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

            adapter = new OrderCompleteAdapter(getApplicationContext(), R.layout.list_row_save_order_item);
            adapter.setData(mListOrderDataY);

            lv.setAdapter(adapter);
            lv.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<Order>() {

                @Override
                public boolean onItemClick(final Order item, final int position) {
                    //Snackbar.make(view, "Click event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersCompleteActivity.this, "Click event on the " + position  + " position", Toast.LENGTH_SHORT);
//                    toast.show();

                    showMsgDialogSelectedMenu(position);

//                    myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    return true;
                }

                @Override
                public void onItemLongPress(final Order item, final int position) {
                    //Snackbar.make(view, "Long press event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersCompleteActivity.this, "Long press event on the " + position + " position", Toast.LENGTH_SHORT);
//                    toast.show();
                }

                @Override
                public boolean onDoubleTap(final Order item, final int position) {
                    //Snackbar.make(view, "Double tap event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersCompleteActivity.this, "Double tap event on the " + position + " position", Toast.LENGTH_SHORT);
//                    toast.show();
                    return true;
                }


            }));

            mGestureManager = new GestureManager.Builder(lv)
                    .setSwipeEnabled(false)
                    .setLongPressDragEnabled(false)
                    .setSwipeFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                    .setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
                    .setManualDragEnabled(false)
                    .build();

            adapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<Order>() {
                @Override
                public void onItemRemoved(final Order item, final int position) {
                    //Snackbar.make(view, "Month removed from position " + position, Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersCompleteActivity.this, "Month removed from position " + position, Toast.LENGTH_SHORT);
//                    toast.show();
                }

                @Override
                public void onItemReorder(final Order item, final int fromPos, final int toPos) {
                    //Snackbar.make(view, "Month moved from position " + fromPos + " to " + toPos, Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersCompleteActivity.this, "Month moved from position " + fromPos + " to " + toPos, Toast.LENGTH_SHORT);
//                    toast.show();
                }
            });

//            mBtnSaveOrders.setText("รอส่งข้อมูล\n(8/8)");
//
//            mBtnSaveOrdersComplete.setText("ส่งข้อมูลได้\n(0/8)");
//
//            mBtnReturnList.setText("ใบรับคืน\n(0/2)");

//            mBtnSaveOrders.setText("รอส่งข้อมูล\n("+mListOrderDataY.size()+"/"+mListOrderDataY.size()+")");
//
//            mBtnSaveOrdersComplete.setText("ส่งข้อมูลได้\n(0/"+mListOrderDataY.size()+")");
//
//            mBtnReturnList.setText("ใบรับคืน\n(0/0)");




            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

            mBtnSaveOrders.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
                    startActivity(myIntent);

                    overridePendingTransition(0,0);
                    //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            mBtnReturnList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersReturnListActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(0,0);
                    //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });


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

//        MenuSaveOrder f1 = new MenuSaveOrder();
//        f1.setMenuname("เซ็นรับสินค้า");
//        f1.setMenuname_type("0");
//        f1.setMenuname_mode("0");
//        mListMenuData.add(f1);


        MenuSaveOrder f2 = new MenuSaveOrder();
        f2.setMenuname("กิจกรรม");
        f2.setMenuname_type("1");
        f2.setMenuname_mode("0");
        mListMenuData.add(f2);



        mMenuAdapter = new MenuSaveOrderViewAdapter(getApplicationContext(),mListMenuData);
        lvmenu.setAdapter(mMenuAdapter);


        lvmenu.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                switch (mListMenuData.get(position).getMenuname_type()){
                    case "0":
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
                        //myIntent.putExtra("data",mOrder);
                        startActivity(myIntent);
                        break;


                    case "1":
                        //กิจกรรมอื่นๆ
                        DialogBuilder.dismiss();

//                        mOrder = new Order();
//                        mOrder.setRep_code(mListOrderDataN.get(0).getRep_code());
//                        mOrder.setRep_name(mListOrderDataN.get(0).getRep_name());
//                        mOrder.setTransNo(mListOrderDataN.get(0).getTransNo());
//                        mOrder.setDelivery_date(sigDeliveryDate);
//                        mOrder.setTruckNo(sigTruckNo);
//                        mOrder.setRep_telno(mListOrderDataN.get(0).getRep_telno());
//                        mOrder.setDsm_telno(mListOrderDataN.get(0).getDsm_telno());
//
//                        myIntent = new Intent(getApplicationContext(), WebViewActivity.class);
//                        myIntent.putExtra("data",mOrder);
//                        startActivity(myIntent);
                        break;

                    case "2":
                        //โทร
                        DialogBuilder.dismiss();

                        //showMsgDialog(mListMenuData.get(position).getMenuname());

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
        f1.setMenuname("ดูสลิปเซ็นรับสินค้า");
        f1.setMenuname_type("0");
        f1.setMenuname_mode("1");
        mListMenuData.add(f1);


        MenuSaveOrder f11 = new MenuSaveOrder();
        f11.setMenuname("แก้ไขการเซ็นรับสินค้า");
        f11.setMenuname_type("0");
        f11.setMenuname_mode("0");
        mListMenuData.add(f11);


        MenuSaveOrder f2 = new MenuSaveOrder();
        f2.setMenuname("กิจกรรม");
        f2.setMenuname_type("1");
        f2.setMenuname_mode("1");
        mListMenuData.add(f2);

        final String[] telsMSL = mListOrderDataY.get(selectedPosition).getRep_telno().split(",");
        final String[] telsDSM = mListOrderDataY.get(selectedPosition).getDsm_telno().split(",");


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

//        MenuSaveOrder f2 = new MenuSaveOrder();
//        f2.setMenuname("กิจกรรม");
//        f2.setMenuname_type("1");
//        f2.setMenuname_mode("1");
//        mListMenuData.add(f2);
//
//        MenuSaveOrder f3 = new MenuSaveOrder();
//        f3.setMenuname("โทร MSL : 0983939393");
//        f3.setMenuname_type("2");
//        f3.setMenuname_mode("1");
//        mListMenuData.add(f3);
//
//        MenuSaveOrder f4 = new MenuSaveOrder();
//        f4.setMenuname("โทร DSM : 0874848949");
//        f4.setMenuname_type("2");
//        f4.setMenuname_mode("1");
//        mListMenuData.add(f4);


        mMenuAdapter = new MenuSaveOrderViewAdapter(getApplicationContext(),mListMenuData);
        lvmenu.setAdapter(mMenuAdapter);


        lvmenu.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                switch (mListMenuData.get(position).getMenuname_type()){
                    case "0":
                        //จัดส่งสินค้า
                        DialogBuilder.dismiss();

                        mOrder = new Order();
                        mOrder.setRep_code(mListOrderDataY.get(selectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderDataY.get(selectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderDataY.get(selectedPosition).getTransNo());
                        mOrder.setAddress1(mListOrderDataY.get(selectedPosition).getAddress1());
                        mOrder.setAddress2(mListOrderDataY.get(selectedPosition).getAddress2());
                        mOrder.setPostal(mListOrderDataY.get(selectedPosition).getPostal());
                        mOrder.setRep_telno(mListOrderDataY.get(selectedPosition).getRep_telno());
                        mOrder.setReturn_flag(mListOrderDataY.get(selectedPosition).getReturn_flag());
                        mOrder.setCont_desc(mListOrderDataY.get(selectedPosition).getCont_desc());
                        mOrder.setFullpathimage(mListOrderDataY.get(selectedPosition).getFullpathimage());

                        if(mListMenuData.get(position).getMenuname_mode().equals("0"))
                        {
                            mOrder.setDelivery_status("W");
                        }else
                        {
                            mOrder.setDelivery_status(mListOrderDataY.get(selectedPosition).getDelivery_status());
                        }



                        ArrayList<Order> order = new ArrayList<Order>();
                        order.add(mOrder);

                        //TinyDB tinydb = new TinyDB(getApplicationContext());


                        if(mListMenuData.get(position).getMenuname_mode().equals("0"))
                        {

                            editor = sp.edit();
                            editor.putString(TagUtils.PREF_RESUME_ORDER_LIST, "1");
                            editor.putString(TagUtils.PREF_SELECT_ORDER_POSITION, String.valueOf(selectedPosition));
                            editor.apply();

                            myIntent = new Intent(getApplicationContext(), SaveOrdersApproveSlipActivity.class);
                            myIntent.putExtra("data",order);
                            startActivity(myIntent);
                        }else
                        {
                            myIntent = new Intent(getApplicationContext(), ViewSlipActivity.class);
                            myIntent.putExtra("data",order);
                            startActivity(myIntent);
                        }




                        break;
                    case "1":
                        //กิจกรรมอื่นๆ
                        DialogBuilder.dismiss();

                        mOrder = new Order();
                        mOrder.setRep_code(mListOrderDataY.get(selectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderDataY.get(selectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderDataY.get(selectedPosition).getTransNo());
                        mOrder.setDelivery_date(sigDeliveryDate);
                        mOrder.setTruckNo(sigTruckNo);
                        mOrder.setRep_telno(mListOrderDataY.get(selectedPosition).getRep_telno());
                        mOrder.setDsm_telno(mListOrderDataY.get(selectedPosition).getDsm_telno());

                        myIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                        myIntent.putExtra("data",mOrder);
                        startActivity(myIntent);
                        break;

                    case "2":
                        //โทร
                        DialogBuilder.dismiss();

                        String repcode = mListOrderDataY.get(selectedPosition).getRep_code();


                        String[] Tels = mListMenuData.get(position).getMenuname().toString().trim().split(":");
                        String name = Tels[0].toString();
                        String phone = Tels[1].toString();


                        callPhone(name+setRepcodeFormat(repcode),phone);

                        //showMsgDialog(mListMenuData.get(position).getMenuname());

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
        mmTxtTitle.setText("ออเดอร์ : "+mListOrderDataY.get(selectedPosition).getTransNo());
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

//            mListOrderDataY.clear();
//
//            mHelper = new DBHelper(getApplicationContext());
//            mListOrderDataY.clear();
//            mListOrderDataY = mHelper.getOrderWaitList("Y");



            mHelper = new DBHelper(getApplicationContext());


            mListOrderDataY.clear();
            mListOrderDataY = mHelper.getOrderWaitList("SWY");





        } catch (Exception e) {
            e.printStackTrace();
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

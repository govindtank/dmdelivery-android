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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.MenuSaveOrderViewAdapter;
import com.bl.dmdelivery.adapter.OrderAdapter;
import com.bl.dmdelivery.adapter.OrderCompleteAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.MenuSaveOrder;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.utility.TagUtils;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;

import java.util.ArrayList;

public class SaveOrdersCompleteActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mmTxtMsg;
    private Button mBtnBack,mBtnMenu,mBtnSaveOrders,mBtnSaveOrdersComplete,mBtnReturnList,mBtnClose,mmBtnClose;
    private ImageView mmImvTitle;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    String sigTruckNo = "";
    String sigDeliveryDate = "";

    private Intent myIntent=null;

    //private ArrayList<Order> mListOrderData = new ArrayList<Order>();

    private ArrayList<Order> mListOrderDataALL = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataY = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataYY = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataN = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataNN = new ArrayList<Order>();
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
//            setDefaultFonts();

            setWidgetControl();

//            getOrder();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    @Override
    public void onResume(){
        super.onResume();

//        if()
        String resumeOrderList = sp.getString(TagUtils.PREF_RESUME_ORDER_LIST, "");
        String selectOrderPosition = sp.getString(TagUtils.PREF_SELECT_ORDER_POSITION, "-1");


        if(resumeOrderList.equals(""))
        {

        }
        else
        {
            if(Integer.parseInt(selectOrderPosition) > -1)
            {
                //mListOrderDataN.get(Integer.parseInt(selectOrderPosition)).setIsselect("1");

                editor = sp.edit();
                editor.putString(TagUtils.PREF_RESUME_ORDER_LIST, "");
                editor.apply();

                setHeader();

                mHelper = new DBHelper(getApplicationContext());
//
                mListOrderDataNN.clear();
                mListOrderDataNN = mHelper.getOrderWaitList("WY");

                mListOrderDataY.clear();
                mListOrderDataY = mListOrderDataNN;

//                for(int i=0;i < mListOrderDataY.size(); i++)
//                {
//                    //int retval = mListOrderDataN.indexOf(mListOrderDataNN.get(i).getTransNo());
//
//
//                    for(int ii=0;ii < mListOrderDataNN.size(); ii++) {
//
//                        if(mListOrderDataNN.get(ii).getTransNo().equals(mListOrderDataY.get(i).getTransNo()))
//                        {
//                            mListOrderDataY.get(i).setDelivery_status("W");
//
//                            //mListOrderDataN.remove(i);
//                            //mListOrderDataN.remove(i);
//
//                        }
//
//                    }
//
//
//                }

                //adapter.notifyDataSetChanged();

                adapter.clearData();
                adapter.setData(mListOrderDataY);
                adapter.notifyDataSetChanged();

//                Toast toast = Toast.makeText(SaveOrdersActivity.this, "onResume - Slip", Toast.LENGTH_SHORT);
//                toast.show();


            }
            else
            {


            }


        }

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



//            lv = (RecyclerView) findViewById(R.id.lv);
//
//            lv.setLayoutManager(new LinearLayoutManager(this));
//            lv.setHasFixedSize(true);
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

            setHeader();


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
                    msl1.setMenuname("โทร MSL 1 : "+ telsMSL[0]);
                    msl1.setMenuname_type("2");
                    msl1.setMenuname_mode("0");
                    mListMenuData.add(msl1);

                    MenuSaveOrder msl2 = new MenuSaveOrder();
                    msl2.setMenuname("โทร MSL 2 : "+ telsMSL[1]);
                    msl2.setMenuname_type("2");
                    msl2.setMenuname_mode("0");
                    mListMenuData.add(msl2);
                }
                else
                {


                    MenuSaveOrder msl1 = new MenuSaveOrder();
                    msl1.setMenuname("โทร MSL : "+ telsMSL[0]);
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
                    dsm1.setMenuname("โทร DSM 1 : "+ telsDSM[0]);
                    dsm1.setMenuname_type("2");
                    dsm1.setMenuname_mode("0");
                    mListMenuData.add(dsm1);

                    MenuSaveOrder dsm2 = new MenuSaveOrder();
                    dsm2.setMenuname("โทร DSM 2 : "+ telsDSM[1]);
                    dsm2.setMenuname_type("2");
                    dsm2.setMenuname_mode("0");
                    mListMenuData.add(dsm2);
                }
                else
                {


                    MenuSaveOrder dsm1 = new MenuSaveOrder();
                    dsm1.setMenuname("โทร DSM : "+ telsDSM[0]);
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

                        showMsgDialog(mListMenuData.get(position).getMenuname());

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
            mListOrderDataY = mHelper.getOrderWaitList("WY");





        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setHeader() {

        try {


            mHelper = new DBHelper(getApplicationContext());

            mListOrderDataALL.clear();
            mListOrderDataALL = mHelper.getOrderWaitList("ALL");

            mListOrderDataYY.clear();
            mListOrderDataYY = mHelper.getOrderWaitList("WY");

            mListOrderDataN.clear();
            mListOrderDataN = mHelper.getOrderWaitList("N");

            mListReturnDataALL.clear();
            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            mListReturnDataY.clear();
            mListReturnDataY = mHelper.getOrdersReturnListSummary("Y");


            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_send_data)+" ("+mListOrderDataALL.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrders.setText("ยังไม่บันทึกผล\n("+mListOrderDataN.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrdersComplete.setText("บันทึกผลแล้ว\n("+mListOrderDataYY.size()+"/"+mListOrderDataALL.size()+")");

            mBtnReturnList.setText("ใบรับคืน\n("+mListReturnDataY.size()+"/"+mListReturnDataALL.size()+")");



        } catch (Exception e) {
            e.printStackTrace();
            showMsgDialog(e.toString());
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

package com.bl.dmdelivery.actvity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AndroidException;
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
import com.bl.dmdelivery.adapter.MenuSaveOrderViewAdapter;
import com.bl.dmdelivery.adapter.OrderAdapter;
import com.bl.dmdelivery.adapter.OrderSlipViewAdapter;
import com.bl.dmdelivery.adapter.RVListDeliveryBWAdapter;
import com.bl.dmdelivery.adapter.RecyclerItemClickListener;
import com.bl.dmdelivery.adapter.UnpackViewAdapter;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.helper.GlobalObject;
import com.bl.dmdelivery.helper.TinyDB;
import com.bl.dmdelivery.model.MenuSaveOrder;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderData;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.utility.TagUtils;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private OrderAdapter adapter;


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    private boolean isSelectAll = false;

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
                mListOrderDataNN = mHelper.getOrderWaitList("N");


                mListOrderDataN.clear();
                mListOrderDataN = mListOrderDataNN;

//                for(int i=0;i < mListOrderDataN.size(); i++)
//                {
//                    //int retval = mListOrderDataN.indexOf(mListOrderDataNN.get(i).getTransNo());
//
//
//                    for(int ii=0;ii < mListOrderDataNN.size(); ii++) {
//
//                        if(mListOrderDataNN.get(ii).getTransNo().equals(mListOrderDataN.get(i).getTransNo()))
//                        {
//                            mListOrderDataN.get(i).setDelivery_status("W");
//
////                            Order ordersend = new Order();
////                            ordersend = mListOrderDataN.get(i);
////                            mListOrderDataN.remove(ordersend);
//
//
//
//                            //mAdapter.notifyItemRangeChanged(i, mListOrderDataN.size());
////                            mListOrderDataN.remove(i);
////                            adapter.notifyItemRemoved(i);
////                            lv.removeViewAt(i);
//                            //mListOrderDataN.remove(i);
//                            adapter.notifyItemRemoved(i);
//
//                        }
//
//                    }
//
//
//                }

                //adapter.notifyDataSetChanged();
                //lv.invalidate();

                adapter.clearData();
                adapter.setData(mListOrderDataN);
                adapter.notifyDataSetChanged();
                //lv.scrollToPosition(selectedPositionNotifyDataSetChanged);

                //lv.notify();

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
            sigTruckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
            sigDeliveryDate = sp.getString(TagUtils.PREF_DELIVERY_DATE, "");


          //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnMenu = (Button) findViewById(R.id.btnMenu);
            mBtnClose = (Button) findViewById(R.id.btnClose);

            mBtnSaveOrders = (Button) findViewById(R.id.btnOrdersWait);
            mBtnSaveOrdersComplete = (Button) findViewById(R.id.btnOrdersComptete);
            mBtnReturnList = (Button) findViewById(R.id.btnReturnList);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);



            lv = (RecyclerView) findViewById(R.id.lv);
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

            adapter = new OrderAdapter(getApplicationContext(), R.layout.list_row_save_order_item);
            adapter.setData(mListOrderDataN);

            lv.setAdapter(adapter);
            lv.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<Order>() {

                @Override
                public boolean onItemClick(final Order item, int position) {
//                    //Snackbar.make(view, "Click event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Click event on the " + position  + " position", Toast.LENGTH_SHORT);
//                    toast.show();
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
                public boolean onDoubleTap(final Order item, final int position) {
                    //Snackbar.make(view, "Double tap event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Double tap event on the " + position + " position", Toast.LENGTH_SHORT);
//                    toast.show();

//                    showMsgDialogSelectedMenu(position);

                    isSelectAll = true;

                    mSelectIndex = position;

                    mSelect = mListOrderDataN.get(position).getIsselect();

                    if(mSelect.equals("0"))
                    {
                        mListOrderDataN.get(position).setIsselect("1");

                    }else
                    {
                        mListOrderDataN.get(position).setIsselect("0");


                    }

                    adapter.notifyDataSetChanged();

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


                    adapter.notifyDataSetChanged();
                    //lv.notify();
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
            mListOrderDataY = mHelper.getOrderWaitList("WY");

            mListOrderDataNN.clear();
            mListOrderDataNN = mHelper.getOrderWaitList("N");

            mListOrderDataSend.clear();
            mListOrderDataSend = mHelper.getOrderWaitList("Y");

            mListReturnDataALL.clear();
            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            mListReturnDataY.clear();
            mListReturnDataY = mHelper.getOrdersReturnListSummary("Y");

            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_send_data)+" ("+mListOrderDataSend.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrders.setText("ยังไม่บันทึกผล\n("+mListOrderDataNN.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrdersComplete.setText("บันทึกผลแล้ว\n("+mListOrderDataY.size()+"/"+mListOrderDataALL.size()+")");

            mBtnReturnList.setText("ใบรับคืน\n("+mListReturnDataY.size()+"/"+mListReturnDataALL.size()+")");


        } catch (Exception e) {
            e.printStackTrace();
            showMsgDialog(e.toString());
        }

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



    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);


                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
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

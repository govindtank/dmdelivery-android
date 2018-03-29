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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.MenuSaveOrderViewAdapter;
import com.bl.dmdelivery.adapter.OrderCompleteAdapter;
import com.bl.dmdelivery.adapter.OrderReturnListAdapter;
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

public class SaveOrdersReturnListActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack,mBtnMenu,mBtnSaveOrders,mBtnSaveOrdersComplete,mBtnReturnList,mmBtnClose;
    private ImageView mmImvTitle;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private Intent myIntent=null;

    //private ListView lv;
    private String[] sigReturnList;

    private ArrayList<MenuSaveOrder> mListMenuData = new ArrayList<MenuSaveOrder>();


    private ArrayList<Order> mListOrderDataALL = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataY = new ArrayList<Order>();
    private ArrayList<Order> mListOrderDataN = new ArrayList<Order>();
    private ArrayList<OrderReturn> mListReturnDataALL = new ArrayList<OrderReturn>();
    private ArrayList<OrderReturn> mListReturnDataY = new ArrayList<OrderReturn>();


    //private ArrayList<MenuSaveOrder> mListMenuData = new ArrayList<MenuSaveOrder>();
    private RecyclerView lv,lvmenu;
    private RecyclerView.Adapter mAdapter,mMenuAdapter;
    private GestureManager mGestureManager;
    DBHelper mHelper;

    OrderReturn mOrder;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private String sigTruckNo = "";
    private String sigDeliveryDate = "";
    private boolean isResumeState = false;

    private  OrderReturnListAdapter adapter = null;

    private  int selectedPositionNotifyDataSetChanged = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders_return_list);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

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

        if(isResumeState)
        {
//            //มาจากหน้า slip
//            Toast toast = Toast.makeText(SaveOrdersReturnListActivity.this, "onResume - OK= " + mListReturnDataALL.get(selectedPositionNotifyDataSetChanged).getReturn_status(), Toast.LENGTH_SHORT);
//            toast.show();

//
//            mListReturnDataALL.get(selectedPositionNotifyDataSetChanged).setReturn_status(mListReturnDataALL.get(selectedPositionNotifyDataSetChanged).getReturn_status());

            setHeader();

            mListReturnDataALL.clear();
            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            adapter.clearData();
            adapter.setData(mListReturnDataALL);
            adapter.notifyDataSetChanged();
            lv.scrollToPosition(selectedPositionNotifyDataSetChanged);

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

            mBtnSaveOrders = (Button) findViewById(R.id.btnOrdersWait);
            mBtnSaveOrdersComplete = (Button) findViewById(R.id.btnOrdersComptete);
            mBtnReturnList = (Button) findViewById(R.id.btnReturnList);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            //mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return_list));

            mBtnMenu.setVisibility(View.INVISIBLE);

            lv = (RecyclerView) findViewById(R.id.lv);


            isResumeState = false;
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

            adapter = new OrderReturnListAdapter(getApplicationContext(), R.layout.list_row_order_return_item);
            adapter.setData(mListReturnDataALL);

            lv.setAdapter(adapter);
            lv.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<OrderReturn>() {

                @Override
                public boolean onItemClick(final OrderReturn item, final int position) {
                    selectedPositionNotifyDataSetChanged = position;
                    showMsgDialogMenu(position);
                    return true;
                }

                @Override
                public void onItemLongPress(final OrderReturn item, final int position) {
                    //Snackbar.make(view, "Long press event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersReturnListActivity.this, "Long press event on the " + position + " position", Toast.LENGTH_SHORT);
//                    toast.show();
                }

                @Override
                public boolean onDoubleTap(final OrderReturn item, final int position) {
                    //Snackbar.make(view, "Double tap event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersReturnListActivity.this, "Double tap event on the " + position + " position", Toast.LENGTH_SHORT);
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

            adapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<OrderReturn>() {
                @Override
                public void onItemRemoved(final OrderReturn item, final int position) {
                    //Snackbar.make(view, "Month removed from position " + position, Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersReturnListActivity.this, "Month removed from position " + position, Toast.LENGTH_SHORT);
//                    toast.show();
                }

                @Override
                public void onItemReorder(final OrderReturn item, final int fromPos, final int toPos) {
                    //Snackbar.make(view, "Month moved from position " + fromPos + " to " + toPos, Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersReturnListActivity.this, "Month moved from position " + fromPos + " to " + toPos, Toast.LENGTH_SHORT);
//                    toast.show();
                }
            });





//            mBtnSaveOrders.setText("รอส่งข้อมูล\n(0/0)");
//
//            mBtnOrdersComptete.setText("ส่งข้อมูลได้\n(0/0)");
//
//            mBtnReturnList.setText("ใบรับคืน\n(0/"+mListReturnDataALL.size()+")");

            //mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_send_data)+" (0/0)");

//            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_send_data)+" ("+mListOrderDataALL.size()+"/"+mListOrderDataALL.size()+")");
//
//
//            mBtnSaveOrders.setText("ยังไม่บันทึกผล\n("+mListOrderDataN.size()+"/"+mListOrderDataALL.size()+")");
//
//            mBtnSaveOrdersComplete.setText("บันทึกผลแล้ว\n("+mListOrderDataY.size()+"/"+mListOrderDataALL.size()+")");
//
//            mBtnReturnList.setText("ใบรับคืน\n("+mListReturnDataY.size()+"/"+mListReturnDataALL.size()+")");



            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

//            mBtnMenu.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
////                    myIntent = new Intent(getApplicationContext(), OthersMenuActivity.class);
////                    startActivity(myIntent);
////                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//
//                    showMsgDialogMenu();
//                }
//            });

            mBtnSaveOrders.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(0,0);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            mBtnSaveOrdersComplete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    myIntent = new Intent(getApplicationContext(), SaveOrdersCompleteActivity.class);
                    startActivity(myIntent);

                    overridePendingTransition(0,0);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void getInit() {

        try {

            mHelper = new DBHelper(getApplicationContext());

//            mListOrderDataALL.clear();
//            mListOrderDataALL = mHelper.getOrderWaitList("ALL");
//
//            mListOrderDataY.clear();
//            mListOrderDataY = mHelper.getOrderWaitList("Y");
//
//            mListOrderDataN.clear();
//            mListOrderDataN = mHelper.getOrderWaitList("N");

            mListReturnDataALL.clear();
            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            mListReturnDataY.clear();
            mListReturnDataY = mHelper.getOrdersReturnListSummary("Y");


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
            mListOrderDataY = mHelper.getOrderWaitList("WY");

            mListOrderDataN.clear();
            mListOrderDataN = mHelper.getOrderWaitList("N");

            mListReturnDataALL.clear();
            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            mListReturnDataY.clear();
            mListReturnDataY = mHelper.getOrdersReturnListSummary("Y");


            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_send_data)+" ("+mListOrderDataALL.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrders.setText("ยังไม่บันทึกผล\n("+mListOrderDataN.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrdersComplete.setText("บันทึกผลแล้ว\n("+mListOrderDataY.size()+"/"+mListOrderDataALL.size()+")");

            mBtnReturnList.setText("ใบรับคืน\n("+mListReturnDataY.size()+"/"+mListReturnDataALL.size()+")");



        } catch (Exception e) {
            e.printStackTrace();
            showMsgDialog(e.toString());
        }

    }

    public void showMsgDialogMenu(final int selectedPosition)
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_menu_order_save, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnClose = (Button) v.findViewById(R.id.btClose);

        lvmenu = (RecyclerView) v.findViewById(R.id.lvmenu);
        lvmenu.setLayoutManager(new LinearLayoutManager(this));
        lvmenu.setHasFixedSize(true);

        mListMenuData.clear();



        if(mListReturnDataALL.get(selectedPosition).getReturn_status() == "รับคืนได้")
        {
            //รับคืนได้
            MenuSaveOrder f1 = new MenuSaveOrder();
            f1.setMenuname("ดูสลิปเซ็นรับคืนสินค้า");
            f1.setMenuname_type("0");
            f1.setMenuname_mode("1");
            mListMenuData.add(f1);


            MenuSaveOrder f2 = new MenuSaveOrder();
            f2.setMenuname("แก้ไขการเซ็นรับคืนสินค้า");
            f2.setMenuname_type("0");
            f2.setMenuname_mode("0");
            mListMenuData.add(f2);


            MenuSaveOrder f3 = new MenuSaveOrder();
            f3.setMenuname("กิจกรรม");
            f3.setMenuname_type("1");
            f3.setMenuname_mode("0");
            mListMenuData.add(f3);
        }
        else  if(mListReturnDataALL.get(selectedPosition).getReturn_status() == "รับคืนไม่ได้")
        {
            //รับคืนไม่ได้

            MenuSaveOrder f2 = new MenuSaveOrder();
            f2.setMenuname("แก้ไขการเซ็นรับคืนสินค้า");
            f2.setMenuname_type("0");
            f2.setMenuname_mode("0");
            mListMenuData.add(f2);


            MenuSaveOrder f3 = new MenuSaveOrder();
            f3.setMenuname("กิจกรรม");
            f3.setMenuname_type("1");
            f3.setMenuname_mode("0");
            mListMenuData.add(f3);
        }
        else
        {
            //ยังไม่รับคืน

            MenuSaveOrder f3 = new MenuSaveOrder();
            f3.setMenuname("กิจกรรม");
            f3.setMenuname_type("1");
            f3.setMenuname_mode("0");
            mListMenuData.add(f3);
        }


//        MenuSaveOrder f1 = new MenuSaveOrder();
//        f1.setMenuname("ดูสลิปเซ็นรับคืนสินค้า");
//        f1.setMenuname_type("0");
//        f1.setMenuname_mode("1");
//        mListMenuData.add(f1);
//
//
//        MenuSaveOrder f2 = new MenuSaveOrder();
//        f2.setMenuname("แก้ไขการเซ็นรับคืนสินค้า");
//        f2.setMenuname_type("0");
//        f2.setMenuname_mode("0");
//        mListMenuData.add(f2);
//
//
//        MenuSaveOrder f3 = new MenuSaveOrder();
//        f3.setMenuname("กิจกรรม");
//        f3.setMenuname_type("1");
//        f3.setMenuname_mode("0");
//        mListMenuData.add(f3);



        mMenuAdapter = new MenuSaveOrderViewAdapter(getApplicationContext(),mListMenuData);
        lvmenu.setAdapter(mMenuAdapter);


        lvmenu.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                switch (mListMenuData.get(position).getMenuname_type()){
                    case "0":
                        //ดูสลิปเซ็นต์รับคืน
                        DialogBuilder.dismiss();

                        ArrayList<Order> mOrders = null;
                        Order mOrder = null;
                        if(mListMenuData.get(position).getMenuname_mode() == "0")
                        {
                            //แก้ไขการเซ็นต์รับคืนสินค้า
                            isResumeState = true;

                            mOrders = new ArrayList<Order>();
                            mOrder = new Order();
                            mOrder.setTruckNo(mListReturnDataALL.get(selectedPosition).getReftrans_no());
                            mOrder.setRep_code(mListReturnDataALL.get(selectedPosition).getRep_code());
                            mOrders.add(mOrder);

                            myIntent = new Intent(getApplicationContext(), SaveOrdersReturnDocActivity.class);
                            myIntent.putExtra("data",mOrders);
                            startActivity(myIntent);
                        }
                        else if(mListMenuData.get(position).getMenuname_mode() == "1")
                        {
                            //ดูสลิปเซ็นต์รับคืน

                            isResumeState = true;

//                            Toast toast = Toast.makeText(SaveOrdersReturnListActivity.this, mListReturnDataALL.get(selectedPosition).getFullpathimage(), Toast.LENGTH_SHORT);
//                            toast.show();

                            OrderReturn  mOrderReturn = new OrderReturn();
                            mOrderReturn.setReftrans_no(mListReturnDataALL.get(selectedPosition).getReftrans_no());
                            mOrderReturn.setReturn_no(mListReturnDataALL.get(selectedPosition).getReturn_no());
                            mOrderReturn.setRep_code(mListReturnDataALL.get(selectedPosition).getRep_code());
                            mOrderReturn.setRep_name(mListReturnDataALL.get(selectedPosition).getRep_name());
                            mOrderReturn.setFullpathimage(mListReturnDataALL.get(selectedPosition).getFullpathimage());


                            myIntent = new Intent(getApplicationContext(), ViewSlipReturnActivity.class);
                            myIntent.putExtra("datareturn",mOrderReturn);
                            startActivity(myIntent);
                        }
                        break;
                    case "1":
                        //กิจกรรม
                        DialogBuilder.dismiss();

//                        isResumeState = true;

                        mOrder = new Order();
                        mOrder.setRep_code(mListReturnDataALL.get(selectedPosition).getRep_code());
                        mOrder.setRep_name(mListReturnDataALL.get(selectedPosition).getRep_name());
                        mOrder.setTransNo(mListReturnDataALL.get(selectedPosition).getReftrans_no());
                        mOrder.setDelivery_date(sigDeliveryDate);
                        mOrder.setTruckNo(sigTruckNo);
                        mOrder.setRep_telno("");
                        mOrder.setDsm_telno("");

                        myIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                        myIntent.putExtra("data",mOrder);
                        startActivity(myIntent);

                        break;
                    default:

//                        isResumeState = true;

                        DialogBuilder.dismiss();
                        showMsgDialog("default");
                        break;
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

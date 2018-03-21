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
import com.bl.dmdelivery.utility.TagUtils;
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
    private TextView mTxtMsg,mTxtHeader,mmTxtTitle,mmTxtMsg;
    private Button mBtnBack,mBtnMenu,mBtnSaveOrders,mBtnSaveOrdersComplete,mBtnReturnList,mBtnClose,mmBtnClose;
    private ImageView mmImvTitle;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

//    private WebserviceHelper webHelper = new WebserviceHelper();
//    private CheckNetwork chkNetwork = new CheckNetwork();
    GlobalObject ogject = GlobalObject.getInstance();

    private ArrayList<Order> mListOrderData = new ArrayList<Order>();
    private ArrayList<MenuSaveOrder> mListMenuData = new ArrayList<MenuSaveOrder>();
    //private List<Order> mListOrder = new List<Order>();
    private String mFilter="0",mInvoiceno,mSelectall="0",mSelect="";

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


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

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
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_list));


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

            final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            lv.setHasFixedSize(true);
            lv.setLayoutManager(manager);

            final OrderAdapter adapter = new OrderAdapter(getApplicationContext(), R.layout.list_row_save_order_item);
            adapter.setData(mListOrderData);

            lv.setAdapter(adapter);
            lv.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<Order>() {

                @Override
                public boolean onItemClick(final Order item, final int position) {
//                    //Snackbar.make(view, "Click event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersActivity.this, "Click event on the " + position  + " position", Toast.LENGTH_SHORT);
//                    toast.show();
//                    myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
//                    myIntent.putExtra("inv", "'" + mListOrderData.get(position).getTransNo() + "'");
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                    //showMsgUserSelectedMenuDialog(mListOrderData.get(position).getTransNo(),position);

                    showMsgDialogSelectedMenu(position);

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

            mBtnSaveOrders.setText("รอส่งข้อมูล\n("+mListOrderData.size()+"/"+mListOrderData.size()+")");

            mBtnSaveOrdersComplete.setText("ส่งข้อมูลได้\n(0/"+mListOrderData.size()+")");

            mBtnReturnList.setText("ใบรับคืน\n(0/0)");


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


//    public void showMsgDialog(String msg)
//    {
//        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
//        final AlertDialog alert = DialogBuilder.create();
//        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = li.inflate(R.layout.dialog_message, null, false);
//
//        mTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
//
//        Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//        mTxtMsg.setTypeface(tf);
//        mTxtMsg.setText(msg);
//
//        DialogBuilder.setView(v);
//        DialogBuilder.setNegativeButton(getResources().getString(R.string.btn_text_close), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
//            }
//        });
//        DialogBuilder.show();
//    }

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
        mListMenuData.add(f1);


        MenuSaveOrder f2 = new MenuSaveOrder();
        f2.setMenuname("กิจกรรม");
        f2.setMenuname_type("1");
        mListMenuData.add(f2);

        MenuSaveOrder f3 = new MenuSaveOrder();
        f3.setMenuname("โทร MSL : 0983939393");
        f3.setMenuname_type("2");
        mListMenuData.add(f3);

        MenuSaveOrder f4 = new MenuSaveOrder();
        f4.setMenuname("โทร DSM : 0874848949");
        f4.setMenuname_type("2");
        mListMenuData.add(f4);


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
                        mOrder.setRep_code(mListOrderData.get(selectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderData.get(selectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderData.get(selectedPosition).getTransNo());
                        mOrder.setAddress1(mListOrderData.get(selectedPosition).getAddress1());
                        mOrder.setAddress2(mListOrderData.get(selectedPosition).getAddress2());
                        mOrder.setPostal(mListOrderData.get(selectedPosition).getPostal());
                        mOrder.setRep_telno(mListOrderData.get(selectedPosition).getRep_telno());
                        mOrder.setReturn_flag(mListOrderData.get(selectedPosition).getReturn_flag());
                        mOrder.setCont_desc(mListOrderData.get(selectedPosition).getCont_desc());

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
                        mOrder.setRep_code(mListOrderData.get(selectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderData.get(selectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderData.get(selectedPosition).getTransNo());
                        mOrder.setDelivery_date(sigDeliveryDate);
                        mOrder.setTruckNo(sigTruckNo);
                        mOrder.setRep_telno(mListOrderData.get(selectedPosition).getRep_telno());
                        mOrder.setDsm_telno(mListOrderData.get(selectedPosition).getDsm_telno());

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
        mmTxtTitle.setText("ออเดอร์ : "+mListOrderData.get(selectedPosition).getTransNo());
        //mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }


    public void showMsgUserSelectedMenuDialog(String sigGetTransNo,int selectedPosition)
    {

        final String sigTransNo=sigGetTransNo;
        final int intSelectedPosition=selectedPosition;
        final AlertDialog DialogBuilder = new AlertDialog.Builder(SaveOrdersActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_call_telandact, null);
        DialogBuilder.setView(v);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmTxtTitle.setText("เลือกเมนู");
        mBtnClose = (Button) v.findViewById(R.id.btnClose);
        lvGetMenu = (ListView) v.findViewById(R.id.lv);

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("หน้าจอเซ็นรับ");
        arrayList.add("กิจกรรรม");
        arrayList.add("โทรหา MSL 1: 0800000000");
        arrayList.add("โทรหา MSL 2: 0800000000");
        arrayList.add("โทรหา DSM 1: 0800000000");
        arrayList.add("โทรหา DSM 2: 0800000000");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        lvGetMenu.setAdapter(adapter);


        mBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });


        // Set item click listener
        lvGetMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String  description    = (String) lvGetMenu.getItemAtPosition(position);

                switch (position){
                    case 0:
                        //จัดส่งสินค้า
                        DialogBuilder.dismiss();

                        mOrder = new Order();
                        mOrder.setRep_code(mListOrderData.get(intSelectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderData.get(intSelectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderData.get(intSelectedPosition).getTransNo());
                        mOrder.setAddress1(mListOrderData.get(intSelectedPosition).getAddress1());
                        mOrder.setAddress2(mListOrderData.get(intSelectedPosition).getAddress2());
                        mOrder.setPostal(mListOrderData.get(intSelectedPosition).getPostal());
                        mOrder.setRep_telno(mListOrderData.get(intSelectedPosition).getRep_telno());
                        mOrder.setReturn_flag(mListOrderData.get(intSelectedPosition).getReturn_flag());
                        mOrder.setCont_desc(mListOrderData.get(intSelectedPosition).getCont_desc());

                        ArrayList<Order> order = new ArrayList<Order>();
                        order.add(mOrder);

                        //TinyDB tinydb = new TinyDB(getApplicationContext());




                        myIntent = new Intent(getApplicationContext(), SaveOrdersApproveSlipActivity.class);
                        myIntent.putExtra("data",order);
                        startActivity(myIntent);
                        break;
                    case 1:
                        //กิจกรรมอื่นๆ
                        DialogBuilder.dismiss();

                        mOrder = new Order();
                        mOrder.setRep_code(mListOrderData.get(intSelectedPosition).getRep_code());
                        mOrder.setRep_name(mListOrderData.get(intSelectedPosition).getRep_name());
                        mOrder.setTransNo(mListOrderData.get(intSelectedPosition).getTransNo());
                        mOrder.setDelivery_date(sigDeliveryDate);
                        mOrder.setTruckNo(sigTruckNo);
                        mOrder.setRep_telno(mListOrderData.get(intSelectedPosition).getRep_telno());
                        mOrder.setDsm_telno(mListOrderData.get(intSelectedPosition).getDsm_telno());

                        myIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                        myIntent.putExtra("data",mOrder);
                        startActivity(myIntent);
                        break;
                }
            }
        });


//        lvGetMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                DialogBuilder.dismiss();
//
////                myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
////                startActivity(myIntent);
////                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });

        DialogBuilder.show();
    }





    private void getInit() {

        try {

            mListOrderData.clear();

            mHelper = new DBHelper(getApplicationContext());
            mListOrderData.clear();
            mListOrderData = mHelper.getOrderWaitList("ALL");



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

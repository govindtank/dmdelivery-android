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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.OrderCompleteAdapter;
import com.bl.dmdelivery.adapter.OrderReturnListAdapter;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.MenuSaveOrder;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;

import java.util.ArrayList;

public class SaveOrdersReturnListActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader;
    private Button mBtnBack,mBtnMenu,mBtnSaveOrders,mBtnSaveOrdersComplete,mBtnReturnList;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private Intent myIntent=null;

    //private ListView lv;
    private String[] sigReturnList;


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

            bindWidget();
//
//            setDefaultFonts();

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
            mBtnMenu = (Button) findViewById(R.id.btnMenu);

            mBtnSaveOrders = (Button) findViewById(R.id.btnOrdersWait);
            mBtnSaveOrdersComplete = (Button) findViewById(R.id.btnOrdersComptete);
            mBtnReturnList = (Button) findViewById(R.id.btnReturnList);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_return_list));

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

            final OrderReturnListAdapter adapter = new OrderReturnListAdapter(getApplicationContext(), R.layout.list_row_order_return_item);
            adapter.setData(mListReturnDataALL);

            lv.setAdapter(adapter);
            lv.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<OrderReturn>() {

                @Override
                public boolean onItemClick(final OrderReturn item, final int position) {
                    //Snackbar.make(view, "Click event on the " + position + " position", Snackbar.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(SaveOrdersCompleteActivity.this, "Click event on the " + position  + " position", Toast.LENGTH_SHORT);
//                    toast.show();

                    //showMsgDialogSelectedMenu(position);

//                    myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
//                    startActivity(myIntent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

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


            mBtnSaveOrders.setText("รอส่งข้อมูล\n("+mListOrderDataN.size()+"/"+mListOrderDataALL.size()+")");

            mBtnSaveOrdersComplete.setText("ส่งข้อมูลได้\n("+mListOrderDataY.size()+"/"+mListOrderDataALL.size()+")");

            mBtnReturnList.setText("ใบรับคืน\n("+mListReturnDataY.size()+"/"+mListReturnDataALL.size()+")");



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

//            mListReturnDataALL.clear();
//
//            mHelper = new DBHelper(getApplicationContext());
//            mListReturnDataALL.clear();
//            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            mHelper = new DBHelper(getApplicationContext());

            mListOrderDataALL.clear();
            mListOrderDataALL = mHelper.getOrderWaitList("ALL");

            mListOrderDataY.clear();
            mListOrderDataY = mHelper.getOrderWaitList("Y");

            mListOrderDataN.clear();
            mListOrderDataN = mHelper.getOrderWaitList("N");

            mListReturnDataALL.clear();
            mListReturnDataALL = mHelper.getOrdersReturnListSummary("ALL");

            mListReturnDataY.clear();
            mListReturnDataY = mHelper.getOrdersReturnListSummary("Y");


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

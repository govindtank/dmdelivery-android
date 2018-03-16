package com.bl.dmdelivery.actvity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.CustomGridViewAdapter;
import com.bl.dmdelivery.helper.CheckNetwork;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.LoadOrderResponse;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.OrderScan;
import com.bl.dmdelivery.model.OrderScanReq;
import com.bl.dmdelivery.model.OrderScanResponse;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.model.Unpack;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MainMenuActivity extends AppCompatActivity {

    private GridView menuGridView;
    private TextView mmTxtMsg,mmTxtTitle,mTxtTroukno;
    private Button mmBtnOk,mmBtnClose;
    private ImageView mmImvTitle;
    private static TextView mTxtDate;
    private Intent myIntent=null;
    private CheckNetwork chkNetwork = new CheckNetwork();
    private ACProgressFlower mProgressDialog;
    private String serverUrl;
    private OrderScanReq mLoadOrderReq;
    private LoadOrderResponse mListLoadOrder =new LoadOrderResponse();

    DBHelper mHelper;
    SQLiteDatabase mDb;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private ListView lv;
    private String[] sigDcList;

//    private String[] gridViewString = {
//            "Scan Order", "Save Order", "Load Contact", "Unpack", "Update Program", "Logout",
//
//    } ;

    private String[] gridViewString = {
            "แสกนสินค้าขึ้นรถ", "โหลดข้อมูล", "สินค้านอกกล่อง", "บันทึกผลการจัดส่ง", "โหลดเบอร์ติดต่อ", "อัพเดทโปรแกรม",
    } ;

//    private int[] gridViewImageId = {
//            R.mipmap.ic_barcodereaderfilled100, R.mipmap.ic_saveasfilled100, R.mipmap.ic_contactfilled100, R.mipmap.ic_formfilled100, R.mipmap.ic_downloadfromfilled100, R.mipmap.ic_padlockfilled100,
//
//    };


    private int[] gridViewImageId = {
            R.mipmap.ic_menu_barcode128, R.mipmap.ic_menu_downloaddata128, R.mipmap.ic_menu_unpack128, R.mipmap.ic_menu_saveorders128, R.mipmap.ic_menu_downloadtel128, R.mipmap.ic_menu_undateprogram128,

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

        bindWidget();

        setWidgetControl();
    }

    private void bindWidget() {

        try {
            menuGridView = (GridView)findViewById(R.id.grid_view_image_text);

            mTxtDate = (TextView) findViewById(R.id.txtdate);
            mTxtTroukno = (TextView) findViewById(R.id.txttruck);

//            //listview
//            lv = (ListView) findViewById(R.id.lv);
//
//            // Create the arrays
//            sigDcList = getResources().getStringArray(R.array.dcList);
//
//            // Create an array adapter
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sigDcList);
//            lv.setAdapter(adapter);


            String truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
            String deliveryDate = sp.getString(TagUtils.PREF_DELIVERY_DATE, "");

            if(deliveryDate.equals(""))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = sdf.format(new Date());
                mTxtDate.setText(currentDate);
            }
            else
            {
                mTxtDate.setText(deliveryDate);
            }


            mTxtTroukno.setText(truckNo);


            mLoadOrderReq = new OrderScanReq();
            mLoadOrderReq.setTruckNo(sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, ""));
            mLoadOrderReq.setDeliveryDate(sp.getString(TagUtils.PREF_DELIVERY_DATE, ""));


        } catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void setWidgetControl()
    {
        try {
            CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getApplicationContext(), gridViewString, gridViewImageId);
            menuGridView.setAdapter(adapterViewAndroid);
            menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int i, long id) {

//                    Toast.makeText(getApplicationContext(), "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();


                    switch (gridViewString[+i]){
                        case "แสกนสินค้าขึ้นรถ":
                            myIntent = new Intent(getApplicationContext(), ScanOrdersActivity.class);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case "โหลดข้อมูล":
                            showMsgConfirmDialog("ต้องการโหลดข้อมูล ใช่หรือไม่?");
                           /* mHelper = new DBHelper(getApplicationContext());
                            ArrayList<Unpack> unpacks = new ArrayList<Unpack>();
                            unpacks = mHelper.getUnpackList();*/
                            break;
                        case "สินค้านอกกล่อง":
                            myIntent = new Intent(getApplicationContext(), UnpackListActivity.class);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case "บันทึกผลการจัดส่ง":
                            myIntent = new Intent(getApplicationContext(), SaveOrdersActivity.class);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case "โหลดเบอร์ติดต่อ":
                            showMsgConfirmSelectedSingleDialog();
                            break;
                        case "อัพเดทโปรแกรม":
                            showMsgConfirmDialog("ต้องการอัพเดทโปรแกรม ใช่หรือไม่?");
                            break;
                    }

                }
            });

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

    public void showMsgConfirmDialog(String msg)
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_confirm, null, false);


        mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);

        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);

        mmTxtTitle.setText("ยืนยัน");
        mmTxtMsg.setText(msg);

        DialogBuilder.setView(v);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loadData();
                DialogBuilder.dismiss();
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        DialogBuilder.show();
    }

    public void showMsgConfirmSelectedSingleDialog()
    {
        // Create the arrays
        sigDcList = getResources().getStringArray(R.array.dcList);

        final AlertDialog DialogBuilder = new AlertDialog.Builder(MainMenuActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog_confirm_down_load_tel, null);
        DialogBuilder.setView(v);
//        DialogBuilder.setTitle("เลือกเงื่อนไขในการโหลดเบอร์ติดต่อ");


        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnOk = (Button) v.findViewById(R.id.btnOk);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);
        mmTxtTitle.setText("เลือกเงื่อนไขการโหลดเบอร์ติดต่อ");


        lv = (ListView) v.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,sigDcList);
        lv.setAdapter(adapter);

        mmBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        mmBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogBuilder.dismiss();
            }
        });

        // Set item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = sigDcList[position];
                Toast.makeText(MainMenuActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        DialogBuilder.show();
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(),R.style.DatePickerDialog, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // do some stuff for example write on log and update TextField on activity
            populateSetDate(year, month, day);
        }

        public void populateSetDate(int year, int month, int day) {

            //String a = mTxtDate.getText().toString();

            //String b = formatDate(year,month,day).toString();

            mTxtDate.setText(formatDate(year, month, day));

          /*  if(!mTxtDate.getText().equals(formatDate(year,month,day)))
            {
                mTxtDate.setText(formatDate(year,month,day));
            }*/
        }
    }

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(date);
    }

    private void loadData() {

        try {

            serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/LoadOrder";
            new loadDataDataInAsync().execute(serverUrl);


//            if(chkNetwork.isConnectionAvailable(getApplicationContext()))
//            {
//                if(chkNetwork.isWebserviceConnected(getApplicationContext()))
//                {
//
//                    serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/LoadOrder";
//                    new loadDataDataInAsync().execute(serverUrl);
//
//
//                }
//                else
//                {
//                    showMsgDialog(getResources().getString(R.string.error_webservice));
//
//                }
//
//            }else
//            {
//                //showDialog("ไม่สามารถเชื่อมต่อ Internet ได้ กรุณากรุณาตรวจสอบ!!!");
//                showMsgDialog(getResources().getString(R.string.error_network));
//            }

        } catch (Exception e) {
            //e.printStackTrace();
            showMsgDialog(e.toString());
        }

    }

    private class loadDataDataInAsync extends AsyncTask<String, Void, PageResultHolder>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(MainMenuActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .text(getResources().getString(R.string.progress_loading))
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

                mHelper = new DBHelper(getApplicationContext());
                mDb = mHelper.getWritableDatabase();
                mHelper.onUpgrade(mDb,0,0);

                //mDb.execSQL("DELETE FROM " + DBHelper.TableOrder + ";");

                Gson gson = new Gson();
                String json = gson.toJson(mLoadOrderReq);
                String result = new WebServiceHelper().postServiceAPI(params[0],json);
                Log.i("LoginResult", result.toString());

                //convert json to obj
                LoadOrderResponse obj = gson.fromJson(result,LoadOrderResponse.class);

                ArrayList<Order> orders = new ArrayList<Order>();
                ArrayList<OrderReturn> orderReturns = new ArrayList<OrderReturn>();
                ArrayList<Unpack> unpacks = new ArrayList<Unpack>();
                ArrayList<Reason> reasons = new ArrayList<Reason>();
               // mListLoadOrder. =new LoadOrderResponse();

                if (obj.getResponseCode().equals("1"))
                {
                    for(int i=0; i<obj.getOrder().size();i++){

                        Order f = new Order();
                        f.setOucode(obj.getOrder().get(i).getOucode().toString());
                        f.setYear(obj.getOrder().get(i).getYear().toString());
                        f.setGroup(obj.getOrder().get(i).getGroup().toString());
                        f.setTransNo(obj.getOrder().get(i).getTransNo().toString());
                        f.setTransdate(obj.getOrder().get(i).getTransdate().toString());
                        f.setRep_seq(obj.getOrder().get(i).getRep_seq().toString());
                        f.setRep_code(obj.getOrder().get(i).getRep_code().toString());
                        f.setRep_name(obj.getOrder().get(i).getRep_name().toString());
                        f.setRep_nickname(obj.getOrder().get(i).getRep_nickname().toString());
                        f.setAddress1(obj.getOrder().get(i).getAddress1().toString());
                        f.setAddress2(obj.getOrder().get(i).getAddress2().toString());
                        f.setTumbon(obj.getOrder().get(i).getTumbon().toString());
                        f.setAmphur(obj.getOrder().get(i).getAmphur().toString());
                        f.setProvince(obj.getOrder().get(i).getProvince().toString());
                        f.setPostal(obj.getOrder().get(i).getPostal().toString());
                        f.setRep_telno(obj.getOrder().get(i).getRep_telno().toString());
                        f.setDsm_name(obj.getOrder().get(i).getDsm_name().toString());
                        f.setDsm_telno(obj.getOrder().get(i).getDsm_telno().toString());
                        f.setLoc_code(obj.getOrder().get(i).getLoc_code().toString());
                        f.setTrans_campaign(obj.getOrder().get(i).getTrans_campaign().toString());
                        f.setOrd_campaign(obj.getOrder().get(i).getOrd_campaign().toString());
                        f.setOrd_type(obj.getOrder().get(i).getOrd_type().toString());
                        f.setDel_type(obj.getOrder().get(i).getDel_type().toString());
                        f.setOrd_flag_status(obj.getOrder().get(i).getOrd_flag_status().toString());
                        f.setReturn_flag(obj.getOrder().get(i).getReturn_flag().toString());
                        f.setUnpack_items(obj.getOrder().get(i).getUnpack_items().toString());
                        f.setOrder_flag_desc(obj.getOrder().get(i).getOrder_flag_desc().toString());
                        f.setDelivery_desc(obj.getOrder().get(i).getDelivery_desc().toString());
                        f.setOrdertype_desc(obj.getOrder().get(i).getOrdertype_desc().toString());
                        f.setCont_desc(obj.getOrder().get(i).getCont_desc().toString());
                        f.setItemno(obj.getOrder().get(i).getItemno());
                        orders.add(f);

                        mHelper.addOrders(f);

                    }

                    for(int i=0; i<obj.getOrderReturn().size();i++){

                        OrderReturn f = new OrderReturn();
                        f.setOu_code(obj.getOrderReturn().get(i).getOu_code().toString());
                        f.setReturn_no(obj.getOrderReturn().get(i).getReturn_no().toString());
                        f.setReturn_code(obj.getOrderReturn().get(i).getReturn_code().toString());
                        f.setReturn_type(obj.getOrderReturn().get(i).getReturn_type().toString());
                        f.setReftrans_no(obj.getOrderReturn().get(i).getReftrans_no().toString());
                        f.setReftrans_year(obj.getOrderReturn().get(i).getReftrans_year().toString());
                        f.setRep_code(obj.getOrderReturn().get(i).getRep_code().toString());
                        f.setRep_seq(obj.getOrderReturn().get(i).getRep_seq().toString());
                        f.setRep_name(obj.getOrderReturn().get(i).getRep_name().toString());
                        f.setReturn_seq(obj.getOrderReturn().get(i).getReturn_seq().toString());
                        f.setFs_code(obj.getOrderReturn().get(i).getFs_code().toString());
                        f.setFs_desc(obj.getOrderReturn().get(i).getFs_desc().toString());
                        f.setReturn_unit(obj.getOrderReturn().get(i).getReturn_unit().toString());
                        f.setReturn_remark(obj.getOrderReturn().get(i).getReturn_remark().toString());
                        orderReturns.add(f);

                        mHelper.addOrdersReturn(f);

                    }

                    for(int i=0; i<obj.getUnpack().size();i++){

                        Unpack f = new Unpack();
                        f.setTransno(obj.getUnpack().get(i).getTransno().toString());
                        f.setUnpack_code(obj.getUnpack().get(i).getUnpack_code().toString());
                        f.setUnpack_desc(obj.getUnpack().get(i).getUnpack_desc().toString());
                        f.setUnpack_qty(obj.getUnpack().get(i).getUnpack_qty().toString());
                        f.setUnpack_image(obj.getUnpack().get(i).getUnpack_image().toString());
                        unpacks.add(f);

                        mHelper.addUnpack(f);

                    }

                    for(int i=0; i<obj.getReason().size();i++){

                        Reason f = new Reason();
                        f.setReason_code(obj.getReason().get(i).getReason_code().toString());
                        f.setReason_desc(obj.getReason().get(i).getReason_desc().toString());
                        f.setReason_type(obj.getReason().get(i).getReason_type().toString());
                        reasons.add(f);

                        mHelper.addReason(f);

                    }

                }

                pageResultHolder.content = obj.getResponseCode();

               /* else
                {

                }*/

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



                if (result.exception != null) {
                    mProgressDialog.dismiss();
                    showMsgDialog(result.exception.toString());
                }
                else
                {

                    if (result.content.equals("1"))
                    {

                        result.content = getResources().getString(R.string.txt_text_update_success);
                    }
                    else
                    {
                        result.content = getResources().getString(R.string.error_data_not_in_system);
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms

                            mProgressDialog.dismiss();

                            showMsgDialog(result.content.toString());

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

            int hasCameraPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

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


}

package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bl.dmdelivery.utility.TagUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainMenuActivity extends AppCompatActivity {

    private GridView menuGridView;
    private TextView mmTxtMsg,mmTxtTitle,mTxtTroukno;
    private Button mmBtnOk,mmBtnClose;
    private ImageView mmImvTitle;
    private static TextView mTxtDate;
    private Intent myIntent=null;

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

       // mmTxtMsg = (TextView) v.findViewById(R.id.txtMsg);
        //mmImvTitle = (ImageView) v.findViewById(R.id.imvTitle);
        mmTxtTitle = (TextView) v.findViewById(R.id.txtTitle);
        mmBtnClose = (Button) v.findViewById(R.id.btnClose);

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
}

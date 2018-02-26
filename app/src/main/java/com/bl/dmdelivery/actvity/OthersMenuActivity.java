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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bl.dmdelivery.R;

public class OthersMenuActivity extends AppCompatActivity {


    private TextView mTxtMsg,mTxtHeader;
    private Button mBtnBack;
    private ListView lv;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private String[] sigOthers_menu;

    private Intent myIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_menu);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {

            bindWidget();

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

            lv = (ListView) findViewById(R.id.lv);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_saveorders_othermenu_list));

            // Create the arrays
            sigOthers_menu = getResources().getStringArray(R.array.others_menu);

            // Create an array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sigOthers_menu);
            lv.setAdapter(adapter);
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }


    private void setWidgetControl() {
        try{

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

            // Set item click listener
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String description = sigOthers_menu[position];

                    Toast.makeText(OthersMenuActivity.this, description, Toast.LENGTH_SHORT).show();

                    switch (description){
                        case "กิจกรรม":
                            break;
                        case "หน้าจอเซ็นต์รับ":
                            myIntent = new Intent(getApplicationContext(), SaveOrdersSlipActivity.class);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case "ยกเลิกรายการที่เลือก":
                            break;
                        case "พิกัดของสมาชิก maps.me":
                            break;
                        case "พิกัดของสมาชิก Google Maps":
                            break;
                        case "นำทาง":
                            break;
                        case "โทรหา DSM เบอร์ที่ 1":
                            break;
                        case "โทรหา DSM เบอร์ที่ 2":
                            break;
                        case "โทรหาสมาชิก เบอร์ที่ 1":
                            break;
                        case "โทรหาสมาชิก เบอร์ที่ 2":
                            break;
                        case "วางสาย":
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

package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bl.dmdelivery.R;

public class DownLoadTelActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader;
    private Button mBtnBack;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    private ListView lv;
    private String[] sigDcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_down_load_tel);

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

            //listview
            lv = (ListView) findViewById(R.id.lv);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_tel_list));

            // Create the arrays
            sigDcList = getResources().getStringArray(R.array.dcList);

            // Create an array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sigDcList);
            lv.setAdapter(adapter);
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

//    private void setDefaultFonts() {
//
//        try {
//            Typeface tf = Typeface.createFromAsset(getAssets(), defaultFonts);
//            mTxtHeader.setTypeface(tf);
//            mBtnBack.setTypeface(tf);
//
//        } catch (Exception e) {
//            showMsgDialog(e.toString());
//        }
//    }

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
                    String description = sigDcList[position];

                    Toast.makeText(DownLoadTelActivity.this, description, Toast.LENGTH_SHORT).show();
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

package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.adapter.CustomGridViewAdapter;

public class MainMenuActivity extends AppCompatActivity {

    private GridView menuGridView;
    private TextView mmTxtMsg,mmTxtTitle;
    private Button mmBtnClose;
    private ImageView mmImvTitle;

    private String[] gridViewString = {
            "Scan Order", "Save Order", "Load Contact", "Unpack", "Update Program", "Logout",

    } ;

    private int[] gridViewImageId = {
            R.mipmap.ic_barcodereaderfilled100, R.mipmap.ic_saveasfilled100, R.mipmap.ic_contactfilled100, R.mipmap.ic_formfilled100, R.mipmap.ic_downloadfromfilled100, R.mipmap.ic_padlockfilled100,

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

        bindWidget();

        setWidgetControl();
    }

    private void bindWidget() {

        try {
            menuGridView = (GridView)findViewById(R.id.grid_view_image_text);


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
                    Toast.makeText(getApplicationContext(), "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
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
}

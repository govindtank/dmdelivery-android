package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bl.dmdelivery.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private TextView mTxtLogotext,mmTxtMsg,mmTxtTitle;
    private EditText mEdtTroukno;
    private Button mBtnlogin,mmBtnClose;
    private ImageView mmImvTitle;
    private static TextView mTxtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bindWidget();

        setWidgetControl();


    }

    private void bindWidget() {

        try {

            mTxtLogotext = (TextView) findViewById(R.id.txtlogo);
            mEdtTroukno = (EditText) findViewById(R.id.edttruck);
            mBtnlogin = (Button) findViewById(R.id.btnlogin);
            mTxtLogotext.setText(getResources().getString(R.string.app_name_dtl));
            mTxtDate = (TextView) findViewById(R.id.txtdate);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());

            mTxtDate.setText(currentDate);

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

    private void setWidgetControl()
    {

        try {



            mTxtDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //showDialog();

                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            });



            mBtnlogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(myIntent);
                    //getWindow().setWindowAnimations(0);
                    overridePendingTransition(0,0);
                    //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

        } catch (Exception e) {

            showMsgDialog(e.toString());
        }


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

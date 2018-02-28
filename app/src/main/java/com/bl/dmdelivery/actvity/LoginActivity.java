package com.bl.dmdelivery.actvity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView mTxtLogotext,mmTxtMsg,mmTxtTitle;
    private EditText mEdtTroukno;
    private Button mBtnlogin,mmBtnClose;
    private ImageView mmImvTitle;
    private static TextView mTxtDate;
    private String serverUrl;
    private ACProgressFlower mProgressDialog;
    private Response resResponse;
    private String truckNo = "";
    private String deliveryDate = "";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

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

        sp = getSharedPreferences(TagUtils.DMDELIVERY_PREF, Context.MODE_PRIVATE);

        bindWidget();

        setWidgetControl();


    }

    private void bindWidget() {

        try {

            String truckNo = sp.getString(TagUtils.PREF_LOGIN_TRUCK_NO, "");
            //String deliveryDate = sp.getString(TagUtils.PREF_DELIVERY_DATE, "");


            mTxtLogotext = (TextView) findViewById(R.id.txtlogo);
            mEdtTroukno = (EditText) findViewById(R.id.edttruck);
            mBtnlogin = (Button) findViewById(R.id.btnlogin);
            mTxtLogotext.setText(getResources().getString(R.string.app_name_dtl));
            mTxtDate = (TextView) findViewById(R.id.txtdate);

//            if(deliveryDate.equals(""))
//            {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                String currentDate = sdf.format(new Date());
//                mTxtDate.setText(currentDate);
//            }
//            else
//            {
//                mTxtDate.setText(deliveryDate);
//            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());
            mTxtDate.setText(currentDate);


            mEdtTroukno.setText(truckNo);

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

                    //hideSoftKeyboard(LoginActivity.this);

                    //checkLogin();

                    setTruck();


                    //serverUrl = TagUtils.WEBSERVICEURI + "IsTruckActiveState/"+mEdtTroukno;
                    //new loginAsynTask().execute(serverUrl);

//                    finish();
//                    Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
//                    startActivity(myIntent);
                    //overridePendingTransition(0,0);



                    //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

        } catch (Exception e) {

            showMsgDialog(e.toString());
        }


    }

    private void setTruck() {

        try {

            mBtnlogin.setEnabled(false);

            hideSoftKeyboard(LoginActivity.this);

            if(!mEdtTroukno.getText().toString().trim().equals(""))
            {
                deliveryDate = mTxtDate.getText().toString().trim().toUpperCase();
                truckNo = mEdtTroukno.getText().toString().trim().toUpperCase();

                editor = sp.edit();
                editor.putString(TagUtils.PREF_DELIVERY_DATE, deliveryDate);
                editor.putString(TagUtils.PREF_LOGIN_TRUCK_NO, truckNo);
                editor.apply();



                mBtnlogin.setEnabled(true);

                finish();
                Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(myIntent);

            }else
            {
                mBtnlogin.setEnabled(true);
                showMsgDialog(getResources().getString(R.string.error_truck_no_empty));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkLogin() {

        try {

            mBtnlogin.setEnabled(false);

            hideSoftKeyboard(LoginActivity.this);

            if(!mEdtTroukno.getText().toString().trim().equals(""))
            {
                truckNo = mEdtTroukno.getText().toString().trim().toUpperCase();

                serverUrl = TagUtils.WEBSERVICEURI + "IsTruckActiveState/"+truckNo;
                new loginAsynTask().execute(serverUrl);

                mBtnlogin.setEnabled(true);

            }else
            {
                mBtnlogin.setEnabled(true);
                showMsgDialog(getResources().getString(R.string.error_truck_no_empty));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class loginAsynTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ACProgressFlower.Builder(LoginActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            PageResultHolder pageResultHolder = new PageResultHolder();
            //String xmlInput = params[0];
            try
            {

                String result = new WebServiceHelper().getServiceAPI(params[0]);
                Log.i("LoginResult", resResponse.body().toString());
                //pageResultHolder.content = result;
                return result;



            } catch (Exception e) {
                pageResultHolder.content = "Exception : CheckTruckNo";
                pageResultHolder.exception = e;
                return null;
            }

        }

        @Override
        protected void onPostExecute(final String result) {
            // TODO Auto-generated method stub

            try {

                if (result == null) {
                    mProgressDialog.dismiss();
                    showMsgDialog(result.toString());
                }
                else
                {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms

                            mProgressDialog.dismiss();


                            Gson gson = new Gson();

                            //String jsonData = resResponse.body().toString();
                            //JSONObject Jobject = new JSONObject(jsonData);

                            String json = gson.toJson(result);
                            Log.i("LoginResultGson",json);


//                            if(resResponse.isSuccessful())
//                            {
//                                Gson gson = new Gson();
//
//                                String jsonData = resResponse.body().toString();
//                                //JSONObject Jobject = new JSONObject(jsonData);
//
//                                String json = gson.toJson(jsonData);
//                                Log.i("LoginResultGson",json);
//                            }
//                            else
//                            {
//                                showMsgDialog(getResources().getString(R.string.error_webservice));
//                            }






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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        hideSoftKeyboard(LoginActivity.this);

        return false;
    }
    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}

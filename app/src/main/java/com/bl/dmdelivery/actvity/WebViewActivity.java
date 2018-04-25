package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.HttpAuthHandler;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.WebServiceHelper;
import com.bl.dmdelivery.model.BaseResponse;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.UrlWebsReq;
import com.bl.dmdelivery.utility.TagUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static com.bl.dmdelivery.utility.TagUtils.WEBSERVICEPASS;
import static com.bl.dmdelivery.utility.TagUtils.WEBSERVICEUSER;

public class WebViewActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack,mBtnMenu;
    private WebView mWebViewDisplay;
    private ProgressDialog prDialog;
    private ACProgressFlower mProgressDialog;
    private String serverUrl,contenttype = "0",webUrl="";

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {

            bindWidget();

            setWidgetControl();

        }
        catch (Exception e)
        {
            showMsgDialog(e.toString());
        }
    }


    private void bindWidget()
    {
        try{

            //button
            mBtnBack = (Button) findViewById(R.id.btnBack);
            mBtnMenu = (Button) findViewById(R.id.btnMenu);
            mWebViewDisplay = (WebView) findViewById(R.id.webviewDisplay);

            //textbox
            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            //mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_activity));
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void setWidgetControl() {
        try {

            mBtnMenu.setVisibility(View.INVISIBLE);


            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });




            Intent intent= getIntent();
            Bundle bdlInv = intent.getExtras();

            if(bdlInv != null)
            {

                String ct = intent.getStringExtra("contenttype");

                contenttype = ct;

                getUrl();



            }





        }
        catch (Exception e)
        {
            showMsgDialog(e.toString());
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(url)) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            //handler.proceed("bladmin", "P@ssw0rdblSD");
            handler.proceed(WEBSERVICEUSER,WEBSERVICEPASS);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            prDialog = new ProgressDialog(WebViewActivity.this);
//            prDialog.setMessage("Please wait ...");
//            prDialog.show();

            mProgressDialog = new ACProgressFlower.Builder(WebViewActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .text(getResources().getString(R.string.progress_loading))
                    .themeColor(getResources().getColor(R.color.colorBackground))
                    //.text(getResources().getString(R.string.progress_loading))
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            if(prDialog!=null){
//                prDialog.dismiss();
//            }

            if(mProgressDialog!=null){
                mProgressDialog.dismiss();
            }


        }
    }

    private void getUrl() {

        try {


            new getUrlAsync().execute();



        } catch (Exception e) {
            //e.printStackTrace();
            showMsgDialog(e.toString());
        }

    }

    private class getUrlAsync extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            int count;
            HttpURLConnection connection = null;
            InputStream input = null;
            OutputStream output = null;

            long total = 0;

            try
            {
                UrlWebsReq urlobj = new UrlWebsReq();
                urlobj.setURL_TYPE(contenttype);

                serverUrl = TagUtils.WEBSERVICEURI + "/DeliveryOrder/UrlWebs";
                Gson gson = new Gson();
                String json = gson.toJson(urlobj);
                String result = new WebServiceHelper().postServiceAPI(serverUrl,json);
                Log.i("Result", result.toString());

                //convert json to obj
                BaseResponse obj = gson.fromJson(result,BaseResponse.class);

                if(obj.getResponseCode().equals("1"))
                {
                    webUrl = obj.getResponseMessage().toString();
                }else
                {
                    webUrl = "0";
                }



            } catch (Exception e) {

                showMsgDialog(e.toString());

            }

            return total;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            mProgressDialog = new ACProgressFlower.Builder(WebViewActivity.this)
//                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
//                    .text(getResources().getString(R.string.progress_loading))
//                    .themeColor(getResources().getColor(R.color.colorBackground))
//                    //.text(getResources().getString(R.string.progress_loading))
//                    .fadeColor(Color.DKGRAY).build();
//            mProgressDialog.show();

        }


        protected void onPostExecute(final Long result) {


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms

                    //mProgressDialog.dismiss();

                    Order mOrder=null;
                    Intent intent= getIntent();
                    Bundle bdlInv = intent.getExtras();

                    mOrder=new Order();
                    mOrder =(Order)bdlInv.get("data");


                    switch(contenttype) {
                        case "WEB1":

                            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_activity));

                            if(mOrder !=null){



                                if(!webUrl.equals("0"))
                                {

                                    mWebViewDisplay.setWebViewClient(new MyWebViewClient());
                                    mWebViewDisplay.getSettings().setJavaScriptEnabled(true);
                                    mWebViewDisplay.getSettings().setDisplayZoomControls(true);
                                    mWebViewDisplay.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);



                                    //Other webview settings
                                    mWebViewDisplay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                                    mWebViewDisplay.setScrollbarFadingEnabled(false);
                                    mWebViewDisplay.loadUrl(webUrl+"?" +
                                            "rep_code=" + mOrder.getRep_code() +
                                            "&rep_name=" + mOrder.getRep_name() +
                                            "&inv=" + mOrder.getTransNo() +
                                            "&deliverydate=" + mOrder.getDelivery_date() +
                                            "&truck=" + mOrder.getTruckNo() +
                                            "&msltel=" + mOrder.getRep_telno() +
                                            "&dsmtel=" + mOrder.getDsm_telno());

                                }


                            }else
                            {

                                if(!webUrl.equals("0"))
                                {
                                    mWebViewDisplay.setWebViewClient(new MyWebViewClient());
                                    mWebViewDisplay.getSettings().setJavaScriptEnabled(true);
                                    mWebViewDisplay.getSettings().setDisplayZoomControls(true);
                                    mWebViewDisplay.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


                                    //Other webview settings
                                    mWebViewDisplay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                                    mWebViewDisplay.setScrollbarFadingEnabled(false);
                                    mWebViewDisplay.loadUrl(webUrl);
                                }

                            }

                            break;


                        case "WEB2":

                            mTxtHeader.setText(getResources().getString(R.string.menu_text_manual));

                            if(!webUrl.equals("0"))
                            {

                                mWebViewDisplay.setWebViewClient(new MyWebViewClient());
                                mWebViewDisplay.getSettings().setJavaScriptEnabled(true);
                                mWebViewDisplay.getSettings().setDisplayZoomControls(true);
                                mWebViewDisplay.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);



                                //Other webview settings
                                mWebViewDisplay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                                mWebViewDisplay.setScrollbarFadingEnabled(false);
                                mWebViewDisplay.loadUrl(webUrl);

                            }


                            break;


                        default:

                    }


                }
            }, 200);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebViewDisplay.canGoBack()) {
            mWebViewDisplay.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
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

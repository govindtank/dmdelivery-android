package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bl.dmdelivery.model.Order;

import java.util.StringTokenizer;

public class WebViewActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack,mBtnMenu;
    private WebView mWebViewDisplay;
    private ProgressDialog prDialog;

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
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_activity));
        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void setWidgetControl() {
        try {
            Order mOrder=null;
            Intent intInv= getIntent();
            Bundle bdlInv = intInv.getExtras();

            if(bdlInv != null)
            {
                mOrder=new Order();
                mOrder =(Order)bdlInv.get("data");
            }


            mBtnMenu.setVisibility(View.INVISIBLE);


            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });


            if(mOrder !=null){
                mWebViewDisplay.setWebViewClient(new MyWebViewClient());
                mWebViewDisplay.getSettings().setJavaScriptEnabled(true);
                mWebViewDisplay.getSettings().setDisplayZoomControls(true);
                mWebViewDisplay.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


                //Other webview settings
                mWebViewDisplay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                mWebViewDisplay.setScrollbarFadingEnabled(false);
                mWebViewDisplay.loadUrl("http://distributioncenter01.mistine.co.th:9090/dm_delivery_addin/index.php?" +
                                  "rep_code=" + mOrder.getRep_code() +
                                  "&rep_name=" + mOrder.getRep_name() +
                                  "&inv=" + mOrder.getTransNo() +
                                  "&deliverydate=" + mOrder.getDelivery_date() +
                                  "&truck=" + mOrder.getTruckNo() +
                                  "&msltel=" + mOrder.getRep_telno() +
                                  "&dsmtel=" + mOrder.getDsm_telno());
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
            handler.proceed("bladmin", "P@ssw0rdblSD");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(WebViewActivity.this);
            prDialog.setMessage("Please wait ...");
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(prDialog!=null){
                prDialog.dismiss();
            }
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

package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import java.util.StringTokenizer;

public class WebViewActivity extends AppCompatActivity {

    private TextView mTxtMsg,mTxtHeader,mmTxtTitle;
    private Button mBtnBack;
    private WebView mWebViewDisplay;


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

            String sigData="";
            Intent intInv= getIntent();
            Bundle bdlInv = intInv.getExtras();

            if(bdlInv != null)
            {
                sigData =(String)bdlInv.get("data");
            }

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });


            if(!sigData.isEmpty()) {
                StringTokenizer sigDataSpilt = new StringTokenizer(sigData, "|");
                      if(sigDataSpilt.countTokens() > 0) {
                          mWebViewDisplay.setWebViewClient(new MyWebViewClient());
                          mWebViewDisplay.getSettings().setJavaScriptEnabled(true);
                          mWebViewDisplay.getSettings().setDisplayZoomControls(true);

                          //Other webview settings
                          mWebViewDisplay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                          mWebViewDisplay.setScrollbarFadingEnabled(false);
                          mWebViewDisplay.loadUrl("http://distributioncenter01.mistine.co.th:9090/dm_delivery_addin/index.php?" +
                                  "rep_code=" + sigDataSpilt.nextToken() +
                                  "&rep_name=" + sigDataSpilt.nextToken() +
                                  "&inv=" + sigDataSpilt.nextToken() +
                                  "&deliverydate=" + sigDataSpilt.nextToken() +
                                  "&truck=" + sigDataSpilt.nextToken());

                      }
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

package com.bl.dmdelivery.actvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Order;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements GoogleMap.InfoWindowAdapter ,OnMapReadyCallback {

    private TextView mmTxtTitle,mmTxtMsg,mTxtHeader;
    private Button mmBtnClose,mBtnBack;
    private ImageView mmImvTitle;

    private GoogleMap map;

    private ArrayList<Order> mListOrderDataALL = new ArrayList<Order>();

    DBHelper mHelper;

    MapFragment mapFragment;

    private static final LatLng CHIANGMAI = new LatLng(18.701224, 98.789770);
    private static final LatLng PHUKET = new LatLng(7.966598, 98.359929);

    private static final LatLng CHONBURI = new LatLng(13.36114, 100.98467);
    private static final LatLng BANGSAEN_BEACH = new LatLng(13.29466, 100.90582);
    private static final LatLng KHAO_KHEOW_OPEN_ZOO = new LatLng(13.21498, 101.05598);
    private static final LatLng SIRACHA_TIGER_ZOO = new LatLng(13.14873, 101.01256);

    private static final LatLng KASETSART = new LatLng(13.85187, 100.56752);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_map);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {

            bindWidget();
//
//            setDefaultFonts();

            setWidgetControl();

//            getOrder();

        } catch (Exception e) {
            showMsgDialog(e.toString());
        }

    }

    private void bindWidget()
    {
        try{

            //mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

            mTxtHeader = (TextView) findViewById(R.id.txtHeader);
            mTxtHeader.setText(getResources().getString(R.string.txt_text_headder_check_map_msl));
            mBtnBack = (Button) findViewById(R.id.btnBack);

            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

    private void setWidgetControl()
    {
        try{


            mBtnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

        }
        catch (Exception e) {
            showMsgDialog(e.toString());
        }
    }

//    private void addMarker() {
//        map.addMarker(new MarkerOptions()
//                .position(CHONBURI)
//                .title(getString(R.string.error_network))
//                .snippet(getString(R.string.error_network))
//        );
//
//        map.addMarker(new MarkerOptions()
//                .position(BANGSAEN_BEACH)
//                .title(getString(R.string.error_network))
//                .snippet(getString(R.string.error_network))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//        );
//
//        map.addMarker(new MarkerOptions()
//                .position(KHAO_KHEOW_OPEN_ZOO)
//                .title(getString(R.string.error_network))
//                .snippet(getString(R.string.error_network))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.background_dialog))
//        );
//
//        map.addMarker(new MarkerOptions()
//                .position(SIRACHA_TIGER_ZOO)
//                .title(getString(R.string.error_network))
//                .snippet(getString(R.string.error_network))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//        );
//
//        map.setInfoWindowAdapter(this);
//    }

    @Override
    public View getInfoContents(Marker marker) {
        if (marker.getTitle().equals(getString(R.string.app_name))) {
            LayoutInflater inflater = getLayoutInflater();
            View infoWindow = inflater.inflate(R.layout.info_window, null);

            ImageView icon = (ImageView) infoWindow.findViewById(R.id.icon);
            icon.setImageResource(R.mipmap.ic_homeaddressfilled50);

            TextView tv = (TextView) infoWindow.findViewById(R.id.title);
            tv.setText(marker.getTitle());

            tv = (TextView) infoWindow.findViewById(R.id.snippet);
            tv.setText(marker.getSnippet());

            return infoWindow;
        } else {
            return null;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (marker.getTitle().equals(getString(R.string.app_name))) {
            LayoutInflater inflater = getLayoutInflater();
            View infoWindow = inflater.inflate(R.layout.info_window, null);

            LinearLayout layout = (LinearLayout) infoWindow.findViewById(R.id.layout);
            layout.setBackgroundResource(R.drawable.background_dialog);

            ImageView icon = (ImageView) infoWindow.findViewById(R.id.icon);
            icon.setImageResource(R.mipmap.ic_homeaddressfilled50);

            TextView tv = (TextView) infoWindow.findViewById(R.id.title);
            tv.setText(marker.getTitle());

            tv = (TextView) infoWindow.findViewById(R.id.snippet);
            tv.setText(marker.getSnippet());

            return infoWindow;
        } else {
            return null;
        }
    }

//    private void drawOnMap() {
//        PolylineOptions line = new PolylineOptions()
//                .add(
//                        new LatLng(13.85243, 100.56449),
//                        new LatLng(13.85197, 100.56539),
//                        new LatLng(13.85212, 100.56583),
//                        new LatLng(13.85278, 100.56621),
//                        new LatLng(13.85290, 100.56653),
//                        new LatLng(13.85287, 100.56803),
//                        new LatLng(13.85254, 100.56885),
//                        new LatLng(13.85254, 100.56906),
//                        new LatLng(13.85299, 100.56909),
//                        new LatLng(13.85303, 100.56980))
//                .width(3)
//                .color(Color.RED);
//        map.addPolyline(line);
//
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(13.85243, 100.56449))
//                .title(getString(R.string.error_network))
//        );
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(13.85303, 100.56980))
//                .title(getString(R.string.error_network))
//        );
//
//        PolygonOptions area = new PolygonOptions()
//                .add(
//                        new LatLng(13.85275, 100.56654),
//                        new LatLng(13.85054, 100.56651),
//                        new LatLng(13.85050, 100.56943),
//                        new LatLng(13.85245, 100.56950),
//                        new LatLng(13.85245, 100.56883),
//                        new LatLng(13.85279, 100.56799))
//                .strokeWidth(3)
//                .strokeColor(Color.BLUE)
//                .fillColor(Color.argb(64, 0, 0, 255));
//        map.addPolygon(area);
//    }

    @Override
    public void onMapReady(GoogleMap map) {
        Boolean match = true;
        LatLng pos = new LatLng(0,0);

        String latlng;
//        Bundle extras = getIntent().getExtras();
//        if(extras == null) {
//            latlng= "0,0";
//        } else {
//            latlng= extras.getString("latlng");
//        }

        //Toast.makeText(this, latlng, Toast.LENGTH_SHORT).show();

        //String[] slatlng = latlng.split(",");

        mHelper = new DBHelper(getApplicationContext());

        mListOrderDataALL.clear();
        mListOrderDataALL = mHelper.getOrderWaitList("ALL");


        for(int i=0; i<mListOrderDataALL.size();i++){


            if(!mListOrderDataALL.get(i).getMsllat().equals("") || !mListOrderDataALL.get(i).getMsllng().equals("") || !mListOrderDataALL.get(i).getMsllat().equals("null") || !mListOrderDataALL.get(i).getMsllng().equals("null"))
            {
                pos = new LatLng (Double.parseDouble(mListOrderDataALL.get(i).getMsllat()), Double.parseDouble(mListOrderDataALL.get(i).getMsllng()));

                map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(mListOrderDataALL.get(i).getRep_name())
                        .snippet(mListOrderDataALL.get(i).getMsllat()+","+mListOrderDataALL.get(i).getMsllng())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_homeaddressfilled50)));


                if ( match == true  ) {
                    CameraPosition cameraPos = new CameraPosition.Builder()
                            .target(pos)
                            .zoom(16)
                            .bearing(0)
                            .tilt(0)
                            .build();

                    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(focus_pos, 18));
                    //map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));

                    //map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
                }
            }


        }

    }


    public void showMsgDialog(String msg)
    {
        final AlertDialog DialogBuilder = new AlertDialog.Builder(this).create();
        DialogBuilder.setIcon(R.mipmap.ic_launcher);
        final LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_message, null, false);


        DialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

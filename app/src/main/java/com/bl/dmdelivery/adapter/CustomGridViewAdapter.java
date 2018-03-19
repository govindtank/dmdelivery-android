package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;

public class CustomGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    public CustomGridViewAdapter(Context context, String[] gridViewString, int[] gridViewImageId) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gridview_menu_item, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.menuText);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.menuImage);

            Typeface tf = Typeface.createFromAsset(gridViewAndroid.getContext().getAssets(), defaultFonts);

            textViewAndroid.setTypeface(tf);

            textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}

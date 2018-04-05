package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class UnpackListViewAdapter extends RecyclerView.Adapter<UnpackListViewAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<Unpack> mData;
    private int lastPosition = -1;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    public UnpackListViewAdapter(Context context, ArrayList<Unpack> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_unpack_list, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder,final int position) {

        try
        {

            Unpack f = mData.get(position);

            holder.mTxtFscode.setText(f.getUnpack_code().toString()+" x "+f.getUnpack_qty().toString());
            holder.mTxtFsName.setText(f.getUnpack_desc().toString());
            //holder.mTxtQty.setText(f.getUnpack_qty().toString());


            byte[] decodedByteArray = Base64.decode(f.getUnpack_image().toString(), Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            holder.mImage.setImageBitmap(decodedBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView mTxtFscode,mTxtFsName,mTxtQty;
        public ImageView mImage;

        public ViewHolder(View v) {
            super(v);

            this.mTxtFscode = (TextView) v.findViewById(R.id.txtFscode);
            this.mTxtFsName = (TextView) v.findViewById(R.id.txtFsname);
            //this.mTxtQty = (TextView) v.findViewById(R.id.txtQty);
            this.mImage = (ImageView)v.findViewById(R.id.imageView);

            Typeface tf = Typeface.createFromAsset(v.getContext().getAssets(), defaultFonts);

            mTxtFscode.setTypeface(tf);
            mTxtFsName.setTypeface(tf);
            //mTxtQty.setTypeface(tf);

        }
    }

    public void addToList(Unpack name, int position) {
        mData.add(position, name);
        notifyItemInserted(position);
    }


    private void removeItemFromList(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight)
    {
        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = bWidth;
        int nHeight = bHeight;

        if(nWidth > reqWidth)
        {
            int ratio = bWidth / reqWidth;
            if(ratio > 0)
            {
                nWidth = reqWidth;
                nHeight = bHeight / ratio;
            }
        }

        if(nHeight > reqHeight)
        {
            int ratio = bHeight / reqHeight;
            if(ratio > 0)
            {
                nHeight = reqHeight;
                nWidth = bWidth / ratio;
            }
        }

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);
    }

}

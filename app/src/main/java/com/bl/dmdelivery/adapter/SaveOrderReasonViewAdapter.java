package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.MenuSaveOrder;
import com.bl.dmdelivery.model.Reason;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class SaveOrderReasonViewAdapter extends RecyclerView.Adapter<SaveOrderReasonViewAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<Reason> mData;
    private int lastPosition = -1;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    public SaveOrderReasonViewAdapter(Context context, ArrayList<Reason> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_save_order_reason, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder,final int position) {

        try
        {

            Reason f = mData.get(position);

            holder.txtMenuName.setText(f.getReason_desc().toString());

            if(f.getReason_type().equals("DELIVERY_ACCEPT"))
            {

                if(f.getIsselect().equals("0"))
                {
                    holder.mImage.setImageResource(R.mipmap.ic_circlefilled50_g);
                }
                else
                {
                    holder.mImage.setImageResource(R.mipmap.ic_filledcirclefilled50_g);
                }

                holder.txtMenuName.setTextColor(ContextCompat.getColor(mContext, R.color.colorBackgroundGreen));

            }else
            {
                if(f.getIsselect().equals("0"))
                {
                    holder.mImage.setImageResource(R.mipmap.ic_circlefilled50_r);
                }
                else
                {
                    holder.mImage.setImageResource(R.mipmap.ic_filledcirclefilled50_r);
                }


                holder.txtMenuName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }


            //holder.mImage.setImageResource(R.mipmap.ic_signup48);

//
//            if(f.getIsselect().toString().equals("0"))
//            {
//                holder.mImage.setImageResource(R.mipmap.ic_signup48);
//            }
//            else
//            {
//                holder.mImage.setImageResource(R.mipmap.ic_word48);
//            }



//            byte[] decodedByteArray = Base64.decode(f.getUnpack_image().toString(), Base64.NO_WRAP);
//            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
//            holder.mImage.setImageBitmap(decodedBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtMenuName;
        public ImageView mImage;

        public ViewHolder(View v) {
            super(v);

            this.txtMenuName = (TextView) v.findViewById(R.id.txtMenuName);
            this.mImage = (ImageView)v.findViewById(R.id.imvMenu);

            Typeface tf = Typeface.createFromAsset(v.getContext().getAssets(), defaultFonts);

            txtMenuName.setTypeface(tf);


        }
    }

    public void addToList(Reason name, int position) {
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

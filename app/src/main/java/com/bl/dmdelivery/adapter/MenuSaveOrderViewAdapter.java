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
import com.bl.dmdelivery.model.MenuSaveOrder;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class MenuSaveOrderViewAdapter extends RecyclerView.Adapter<MenuSaveOrderViewAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<MenuSaveOrder> mData;
    private int lastPosition = -1;

    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

    public MenuSaveOrderViewAdapter(Context context, ArrayList<MenuSaveOrder> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_menu_save_order, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder,final int position) {

        try
        {

            MenuSaveOrder f = mData.get(position);

            holder.txtMenuName.setText(f.getMenuname().toString());

            switch(f.getMenuname_type().toString()) {
                case "0":
                    if(f.getMenuname_mode().toString().equals("0"))
                    {
                        holder.mImage.setImageResource(R.mipmap.ic_signup48);
                    }
                    else
                    {
                        holder.mImage.setImageResource(R.mipmap.ic_word48);
                    }

                    break;
                case "1":
                    holder.mImage.setImageResource(R.mipmap.ic_special48);
                    break;
                case "2":
                    holder.mImage.setImageResource(R.mipmap.ic_phone48);
                    break;
                case "3":
                    holder.mImage.setImageResource(R.mipmap.ic_checkallfilled50);
                    holder.txtMenuName.setText(mContext.getResources().getString(R.string.btn_text_select));
                    break;
                case "4":
                    holder.mImage.setImageResource(R.mipmap.ic_uncheckallfilled50);
                    holder.txtMenuName.setText(mContext.getResources().getString(R.string.btn_text_noselect));
                    break;

                default:
                    holder.mImage.setImageResource(R.mipmap.ic_launcher);
            }





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

    public void addToList(MenuSaveOrder name, int position) {
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

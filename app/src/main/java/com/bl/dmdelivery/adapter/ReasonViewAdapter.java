package com.bl.dmdelivery.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.DBHelper;
import com.bl.dmdelivery.model.Reason;

import java.util.ArrayList;

/**
 * Created by thorkait_t on 09/03/2561.
 */

public class ReasonViewAdapter  extends RecyclerView.Adapter<ReasonViewAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<Reason> mData;
    private int lastPosition = -1;
    private DBHelper mHelper;

    public ReasonViewAdapter(Context context, ArrayList<Reason> Data) {
        mContext = context;
        mData = Data;
    }


    @Override
    public ReasonViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_save_orders_return_cancel, parent, false);

        ReasonViewAdapter.ViewHolder ViewHolder = new ReasonViewAdapter.ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(final ReasonViewAdapter.ViewHolder holder, final int position) {

        try
        {
//            OrdersChangeList f = mData.get(position);
//
//            holder.mTxtInv.setText(isEmptyString(f.getTransNo()));
//            holder.mTxtIdName.setText(isEmptyString(f.getRep_code()) + " " + isEmptyString(f.getRep_name()));
//            holder.mTxtQty.setText(isEmptyString(f.getQty()));
//
//            if(!isEmptyString(f.getUnpack_items()).isEmpty()){
//                double dblUnpack_items = Double.valueOf(f.getUnpack_items());
//                //ถ่ามี unpack แสดงข้อความ "มีสินค้านอกกล่อง"
//                if(dblUnpack_items >= 1){
//                    holder.mTxtUnpackStatus.setText("มีสินค้านอกกล่อง");
//                }
//            }
//            else
//            {
//                //ถ่ามีไม่ unpack ไม่แสดงข้อความ
//                holder.mTxtUnpackStatus.setText("");
//            }


//getReasonListForCondition

            mData.clear();
            mHelper = new DBHelper(mContext);
            mData = mHelper.getReasonListForCondition("");




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String isEmptyString(String val){
        try{
            if(!val.isEmpty()){
                return val.toString();
            }
        }
        catch (Exception e)
        {
        }
        return "";
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ListView lv;

        public ViewHolder(View v) {
            super(v);
            this.lv = (ListView) v.findViewById(R.id.lv);
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

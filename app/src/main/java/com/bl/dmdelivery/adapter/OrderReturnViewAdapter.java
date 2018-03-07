package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class OrderReturnViewAdapter extends RecyclerView.Adapter<OrderReturnViewAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<OrderReturn> mData;
    private int lastPosition = -1;

    public OrderReturnViewAdapter(Context context, ArrayList<OrderReturn> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_order_return, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder,final int position) {

        try
        {

            OrderReturn f = mData.get(position);

            String refNo = "Ref No : "+String.valueOf(f.getReturn_no());
            holder.txtRefReturnNo.setText(refNo);

            String refInv = "Inv No : "+String.valueOf(f.getReftrans_no());
            holder.txtInvNo.setText(refInv);

            String refCode = "Ref Code :"+String.valueOf(f.getRep_code())+" - "+String.valueOf(f.getRep_name());
            holder.txtRefCode.setText(refCode);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtRefReturnNo,txtInvNo,txtRefCode;


        public ViewHolder(View v) {
            super(v);

            this.txtRefReturnNo = (TextView) v.findViewById(R.id.txtRefReturnNo);
            this.txtInvNo = (TextView) v.findViewById(R.id.txtInvNo);
            this.txtRefCode = (TextView) v.findViewById(R.id.txtRefCode);


        }
    }

    public void addToList(OrderReturn name, int position) {
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



}

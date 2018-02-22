package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.OrderScan;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 22/2/2561.
 */

public class OrderScanViewAdapter extends RecyclerView.Adapter<OrderScanViewAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<OrderScan> mData;
    private int lastPosition = -1;

    public OrderScanViewAdapter(Context context, ArrayList<OrderScan> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public OrderScanViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_checkscan, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try
        {

            OrderScan f = mData.get(position);

            holder.mItemNo.setText(f.getItem().toString());
            holder.mOrderNo.setText(f.getInvoiceNo());
            holder.mOrderscan.setText(f.getTotalScan().toString());
            holder.mOrderSum.setText(f.getTotalCanton().toString());
            holder.mOrderNotscan.setText(f.getTotalNotScan().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView mItemNo,mOrderNo,mOrderSum,mOrderscan,mOrderNotscan;

        public ViewHolder(View v) {
            super(v);

            this.mItemNo = (TextView) v.findViewById(R.id.txtNo);
            this.mOrderNo = (TextView) v.findViewById(R.id.txtOrder);
            this.mOrderSum = (TextView) v.findViewById(R.id.txtOrdersSum);
            this.mOrderscan = (TextView) v.findViewById(R.id.txtOrderscan);
            this.mOrderNotscan = (TextView) v.findViewById(R.id.txtOrderNotscan);

        }
    }

    public void addToList(OrderScan name, int position) {
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

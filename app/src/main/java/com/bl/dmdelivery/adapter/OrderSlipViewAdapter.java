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
import com.bl.dmdelivery.model.OrderData;

import java.util.ArrayList;

/**
 * Created by thundorn_j on 26/2/2561.
 */

public class OrderSlipViewAdapter  extends RecyclerView.Adapter<OrderSlipViewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderData> mData;
    private int lastPosition = -1;

    public OrderSlipViewAdapter(Context context, ArrayList<OrderData> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public OrderSlipViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_save_orders, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try
        {

            OrderData f = mData.get(position);

            holder.mItemNo.setText(f.getItemno().toString());
            holder.mInvno.setText(f.getInvoiceno().toString());
            holder.mInvType.setText(f.getInvoice_type().toString());
            holder.mRepcode.setText(f.getRepcode().toString());
            holder.mRepname.setText(f.getRepname().toString());
            holder.mMobilemsl.setText(f.getMobilemsl().toString());
            holder.mMobiledsm.setText(f.getMobiledsm().toString());
            holder.mReturn.setText(f.getInv_return().toString());
            holder.mCarton.setText(f.getCarton().toString());
            holder.mCampaign.setText(f.getCampaign().toString());
            holder.mAddress.setText(f.getAddress().toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView mItemNo,mInvno,mInvType,mCampaign,mCarton,mRepcode,mRepname,mAddress,mMobilemsl,mMobiledsm,mReturn;

        public ViewHolder(View v) {
            super(v);

            this.mItemNo = (TextView) v.findViewById(R.id.txtItemno);
            this.mInvno = (TextView) v.findViewById(R.id.txtInv);
            this.mInvType = (TextView) v.findViewById(R.id.txtTypeInv);
            this.mCampaign = (TextView) v.findViewById(R.id.txtCamp);
            this.mRepcode = (TextView) v.findViewById(R.id.txtRepcode);
            this.mRepname = (TextView) v.findViewById(R.id.txtRepname);
            this.mAddress = (TextView) v.findViewById(R.id.txtAddress1);
            this.mMobilemsl = (TextView) v.findViewById(R.id.txtMobilemsl);
            this.mMobiledsm = (TextView) v.findViewById(R.id.txtMobiledsm);
            this.mReturn = (TextView) v.findViewById(R.id.txtReturn);
            this.mCarton = (TextView)v.findViewById(R.id.txtCarton);

        }
    }

    public void addToList(OrderData name, int position) {
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

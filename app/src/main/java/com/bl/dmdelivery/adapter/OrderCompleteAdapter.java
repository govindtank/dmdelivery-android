package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.Order;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

public class OrderCompleteAdapter extends GestureAdapter<Order, GestureViewHolder> {

    private final Context mCtx;
    private final int mItemResId;

    public OrderCompleteAdapter(final Context ctx, @LayoutRes final int itemResId) {
        mCtx = ctx;
        mItemResId = itemResId;
    }

    @Override
    public GestureViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_save_order_complete_item, parent, false);
        return new OrderCompleteViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final GestureViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Order orderItem = getItem(position);

        final OrderCompleteViewHolder orderViewHolder = (OrderCompleteViewHolder) holder;
        final Order order = (Order) orderItem;
        orderViewHolder.mSeqText.setText(String.valueOf(order.getId()));
        orderViewHolder.txtInv.setText(String.valueOf(order.getTransNo()+" "+order.getDelivery_desc()+" "+order.getCont_desc()));
        orderViewHolder.txtRepcode.setText(String.valueOf(order.getRep_code()+" - "+order.getRep_name()));
        orderViewHolder.txtAddress.setText(String.valueOf(order.getAddress1()+" "+order.getAddress2()+" "+order.getPostal()));
        orderViewHolder.txtMslTel.setText("MSL:"+String.valueOf(order.getRep_telno()));
        orderViewHolder.txtDsmTel.setText("DSM:"+String.valueOf(order.getDsm_telno()));

    }

//    @Override
//    public int getItemViewType(final int position) {
//        return getItem(position).getType().ordinal();
//    }
}

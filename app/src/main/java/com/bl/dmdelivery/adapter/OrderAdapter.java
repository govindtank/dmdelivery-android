package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

public class OrderAdapter extends GestureAdapter<Order, GestureViewHolder> {

    private final Context mCtx;
    private final int mItemResId;

    public OrderAdapter(final Context ctx, @LayoutRes final int itemResId) {
        mCtx = ctx;
        mItemResId = itemResId;
    }

    @Override
    public GestureViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_save_order_item, parent, false);
        return new OrderViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final GestureViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Order orderItem = getItem(position);

        final OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        final Order order = (Order) orderItem;
        orderViewHolder.mMonthText.setText(String.valueOf(order.getTransNo()));
    }

//    @Override
//    public int getItemViewType(final int position) {
//        return getItem(position).getType().ordinal();
//    }
}

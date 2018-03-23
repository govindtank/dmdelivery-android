package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

public class OrderReturnListAdapter extends GestureAdapter<OrderReturn, GestureViewHolder> {

    private final Context mCtx;
    private final int mItemResId;

    public OrderReturnListAdapter(final Context ctx, @LayoutRes final int itemResId) {
        mCtx = ctx;
        mItemResId = itemResId;
    }

    @Override
    public GestureViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_order_return_item, parent, false);
        return new OrderReturnListViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final GestureViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final OrderReturn orderItem = getItem(position);

        final OrderReturnListViewHolder orderViewHolder = (OrderReturnListViewHolder) holder;
        final OrderReturn order = (OrderReturn) orderItem;
        orderViewHolder.mSeqText.setText(String.valueOf(position+1));
        orderViewHolder.txtReturnNo.setText(String.valueOf(order.getReturn_no()));
        orderViewHolder.txtRepcode.setText(setRepcodeFormat(String.valueOf(order.getRep_code()))+" - "+order.getRep_name());
        orderViewHolder.txtReturnList.setText("จำนวนสินค้าที่ต้องรับคืน "+String.valueOf(order.getReturn_unit())+" ชิ้น");
        orderViewHolder.txtReturnListRes.setText("จำนวนสินค้าที่รับคืนจริง "+String.valueOf(order.getReturn_unit_real())+" ชิ้น");
        orderViewHolder.txtStatus.setText(String.valueOf(order.getReturn_status()));


    }

    private String setRepcodeFormat(String repcode) {


        try {

            String repcodeformat = "";

            if(repcode.length() == 10)

            {
                repcodeformat = repcode.substring(0, 4)+"-"+repcode.substring(4, 9)+"-"+repcode.substring(9, 10);
            }
            else
            {
                repcodeformat = repcode;
            }

            return repcodeformat;


        } catch (Exception e)
        {
            return repcode;
            //showMsgDialog(e.toString());
        }

    }

//    @Override
//    public int getItemViewType(final int position) {
//        return getItem(position).getType().ordinal();
//    }
}

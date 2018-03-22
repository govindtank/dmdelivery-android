package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


        if(order.getIsselect().equals("0"))
        {
            orderViewHolder.imgCheck.setVisibility(View.GONE);
            orderViewHolder.txtInv.setText(String.valueOf(order.getTransNo())+" ");
        }else
        {
            orderViewHolder.imgCheck.setVisibility(View.VISIBLE);
            orderViewHolder.txtInv.setText(" "+String.valueOf(order.getTransNo())+" ");
        }



        orderViewHolder.mSeqText.setText(String.valueOf(position+1));
        //orderViewHolder.txtInv.setText(String.valueOf(order.getTransNo())+" ");

        if(order.getCont_desc().contains("+"))
        {
            String[] separated = order.getCont_desc().split("\\+");

            orderViewHolder.txtcarton.setText(" "+separated[0]);
            orderViewHolder.txtcartonb.setText(separated[1]);
        }
        else
        {
            if(order.getCont_desc().contains("C"))
            {


                orderViewHolder.txtcarton.setText(" "+order.getCont_desc());
                orderViewHolder.txtcartonb.setText("");
            }else
            {


                orderViewHolder.txtcarton.setText("");
                orderViewHolder.txtcartonb.setText(" "+order.getCont_desc());

            }

        }






        if(order.getUnpack_items().equals("0"))
        {
            orderViewHolder.txtUnpack.setText("");
        }else
        {
            orderViewHolder.txtUnpack.setText("(นอกกล่อง : "+String.valueOf(order.getUnpack_items())+" ชิ้น)");
        }


        if(order.getReturn_flag().equals(""))
        {
            orderViewHolder.txtReturn.setText("");
        }else
        {
            orderViewHolder.txtReturn.setText("("+String.valueOf(order.getReturn_flag())+")");
        }

        orderViewHolder.txtRepcode.setText(setRepcodeFormat(String.valueOf(order.getRep_code()))+" - "+order.getRep_name());
        orderViewHolder.txtAddress1.setText(String.valueOf(order.getAddress1()));
        orderViewHolder.txtAddress2.setText(String.valueOf(order.getAddress2()+" "+order.getPostal()));
        orderViewHolder.txtMslTel.setText("MSL:"+String.valueOf(order.getRep_telno()));
        orderViewHolder.txtDsmTel.setText("DSM:"+String.valueOf(order.getDsm_telno()));


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

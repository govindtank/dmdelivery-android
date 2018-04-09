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
        orderViewHolder.mSeqText.setText(String.valueOf(position+1));
        orderViewHolder.txtInv.setText(String.valueOf(order.getTransNo())+" ");



        if(order.getDelivery_status().equals("W"))
        {
            orderViewHolder.mSeqText.setBackgroundResource(R.color.colorBackgroundGrayButton);
        }else
        {
            orderViewHolder.mSeqText.setBackgroundResource(R.color.colorBackgroundGreen);
        }

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

        orderViewHolder.txtRepcode.setText(setRepcodeFormat(String.valueOf(order.getRep_code()))+" "+order.getRep_name());
        orderViewHolder.txtAddress1.setText(String.valueOf(order.getAddress1()));
        orderViewHolder.txtAddress2.setText(String.valueOf(order.getAddress2()+" "+order.getPostal()));
        orderViewHolder.txtMslTel.setText("MSL:"+String.valueOf(order.getRep_telno()));
        orderViewHolder.txtDsmTel.setText("DSM:"+String.valueOf(order.getDsm_telno()));
        //orderViewHolder.txtOrdertype.setText("DSM");
        String orderFlag = "";


        switch(order.getOrd_flag_status()) {
            case "N":
                orderFlag = "";
                break;
            case "F":
                orderFlag = "FIRST";
                break;
            case "L":
                orderFlag = "LATE";
                break;
            case "X":
                orderFlag = "FIRST";
                break;
            case "S":
                orderFlag = "LATE";
                break;
            case "T":
                orderFlag = "";
                break;
            case "LT":
                orderFlag = "LATE";
                break;
            default:
                orderFlag = "";
        }



        orderViewHolder.txtOrdertype.setText(orderFlag);



        switch(order.getSend_status()) {
            case "0":
                orderViewHolder.imgCheck.setVisibility(View.VISIBLE);
                orderViewHolder.imgCheck.setImageResource(R.mipmap.ic_notsend64);
            case "1":
                orderViewHolder.imgCheck.setVisibility(View.VISIBLE);
                orderViewHolder.imgCheck.setImageResource(R.mipmap.ic_send64);
                break;
            case "2":
                orderViewHolder.imgCheck.setVisibility(View.VISIBLE);
                orderViewHolder.imgCheck.setImageResource(R.mipmap.ic_notsend64);
                break;
            case "3":
                orderViewHolder.imgCheck.setVisibility(View.VISIBLE);
                orderViewHolder.imgCheck.setImageResource(R.mipmap.ic_send64);
                break;
            default:
                orderViewHolder.imgCheck.setVisibility(View.GONE);
        }


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

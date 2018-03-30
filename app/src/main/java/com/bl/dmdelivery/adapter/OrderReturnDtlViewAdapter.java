package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.OrderReturn;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class OrderReturnDtlViewAdapter extends RecyclerView.Adapter<OrderReturnDtlViewAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<OrderReturn> mData;
    private int lastPosition = -1;

    public OrderReturnDtlViewAdapter(Context context, ArrayList<OrderReturn> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_order_return_dtl, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder,final int position) {

        try
        {
            Typeface typeface = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PSL162pro-webfont.ttf");

            OrderReturn f = mData.get(position);

            String fscode = String.valueOf(f.getFs_code());
            holder.txtFscode.setText(fscode);
            holder.txtFscode.setTypeface(typeface);

            String fsname = String.valueOf(f.getFs_desc());
            holder.txtFsname.setText(fsname);
            holder.txtFsname.setTypeface(typeface);

            holder.txtRet.setTypeface(typeface);
            holder.txtReq.setTypeface(typeface);

            if(f.getReturn_unit().equals("") || f.getReturn_unit().equals("0") || f.getReturn_unit().isEmpty() || f.getReturn_unit()==null)
            {
                holder.txtRet.setText("0");
            }
            else
            {
                holder.txtRet.setText(String.valueOf(f.getReturn_unit()));
            }

            if(f.getReturn_unit_real().equals("") || f.getReturn_unit_real().isEmpty() || f.getReturn_unit_real()==null)
            {
                holder.txtReq.setText(String.valueOf(f.getReturn_unit()));
            }
            else if(f.getReturn_unit_real().equals("0"))
            {
                holder.txtReq.setText("0");
            }
            else
            {
                holder.txtReq.setText(String.valueOf(f.getReturn_unit_real()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtFscode,txtFsname,txtReq,txtRet;


        public ViewHolder(View v) {
            super(v);

            this.txtFscode = (TextView) v.findViewById(R.id.txtFscode);
            this.txtFsname = (TextView) v.findViewById(R.id.txtFsname);
            this.txtReq = (TextView) v.findViewById(R.id.txtReq);
            this.txtRet = (TextView) v.findViewById(R.id.txtRet);



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

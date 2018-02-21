package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.actvity.UpdateProgramActivity;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class UnpackViewAdapter  extends RecyclerView.Adapter<UnpackViewAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<Unpack> mData;
    private int lastPosition = -1;

    public UnpackViewAdapter(Context context, ArrayList<Unpack> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_unpack, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder,final int position) {

        try
        {

            Unpack f = mData.get(position);

            holder.mTxtFscode.setText(f.getFscode().toString());
            holder.mTxtFsName.setText(f.getFsname().toString());
            holder.mTxtQty.setText(f.getFsunit().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView mTxtFscode,mTxtFsName,mTxtQty;

        public ViewHolder(View v) {
            super(v);

            this.mTxtFscode = (TextView) v.findViewById(R.id.txtFscode);
            this.mTxtFsName = (TextView) v.findViewById(R.id.txtFsname);
            this.mTxtQty = (TextView) v.findViewById(R.id.txtQty);

        }
    }

    public void addToList(Unpack name, int position) {
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

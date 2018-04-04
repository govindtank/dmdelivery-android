package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

/**
 * Created by thundorn_j on 19/3/2561.
 */

public class UnpackDialogAdapter extends RecyclerView.Adapter<UnpackDialogAdapter.ViewHolder>  {


        private Context mContext;
        private ArrayList<Unpack> mData;
        private int lastPosition = -1;

        private String defaultFonts = "fonts/PSL162pro-webfont.ttf";

        public UnpackDialogAdapter(Context context, ArrayList<Unpack> Data) {

            mContext = context;
            mData = Data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_orders_unpack, parent, false);

            ViewHolder ViewHolder = new ViewHolder(view);
            return ViewHolder;
        }

        @Override
        public void onBindViewHolder( final ViewHolder holder,final int position) {

            try
            {

                Unpack f = mData.get(position);

                holder.mTxtInv.setText(f.getTransno().toString());
                holder.mTxtRepcode.setText(f.getRep_name().toString());
                holder.mTxtQty.setText(f.getUnpack_qty().toString());




            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public int getItemCount() {

            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            public TextView mTxtInv, mTxtRepcode,mTxtQty;

            public ViewHolder(View v) {
                super(v);

                this.mTxtInv = (TextView) v.findViewById(R.id.txtInvNo);
                this.mTxtRepcode = (TextView) v.findViewById(R.id.txtRefCode);
                this.mTxtQty = (TextView)v.findViewById(R.id.txtQty);

                Typeface tf = Typeface.createFromAsset(v.getContext().getAssets(), defaultFonts);

                mTxtInv.setTypeface(tf);
                mTxtRepcode.setTypeface(tf);
                mTxtQty.setTypeface(tf);

            }
        }
}

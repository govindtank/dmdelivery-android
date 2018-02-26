package com.bl.dmdelivery.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bl.dmdelivery.R;
import com.bl.dmdelivery.helper.GlobalObject;
import com.bl.dmdelivery.model.OrderData;

import java.util.ArrayList;

//import org.ksoap2.serialization.SoapObject;


/**
 * Created by nitisak_p on 20/03/2560.
 */
public class RVListDeliveryBWAdapter extends RecyclerView.Adapter<RVListDeliveryBWAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<OrderData> mData;
    private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
    private int lastPosition = -1;
    GlobalObject ogject = GlobalObject.getInstance();


    public RVListDeliveryBWAdapter(Context context, ArrayList<OrderData> Data) {

        mContext = context;
        mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_row_order_status, parent, false);

        ViewHolder ViewHolder = new ViewHolder(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder,final int position) {

        try
        {

            OrderData f = mData.get(position);

            //Truck t = mData.get(position);

            //holder.mTxtInv.setText(t.getTruck().toString());

            //holder.mTxtNo.setText(String.valueOf(position+1));
            holder.mTxtInv.setText(f.getInvoiceno().toString());
            holder.mTxtRepcode.setText(f.getRepcode().toString());
            holder.mTxtRepname.setText(f.getRepname().toString());
            //holder.mTxtAddress.setText(f.getRepaddr1().toString()+""+f.getRepaddr2().toString()+""+f.getRepaddr3().toString()+""+f.getRepaddr4().toString());
            //holder.mTxtTel.setText("Tel. : "+f.getRepphone().toString()+""+f.getRepmobile().toString());


/*            if(f.getOtrdeliverystatus().toString().equals("05"))
            {
                holder.mImbChecked.setVisibility(View.VISIBLE);
                holder.mImbChecked.setImageResource(R.mipmap.ic_checked60);

            }else
            {
                holder.mImbChecked.setVisibility(View.INVISIBLE);
                //holder.mImbChecked.setImageResource(R.mipmap.ic_truck64);
            }*/

            /*if(f.getSelect().toString().equals("0"))
            {
                holder.mImbChecked.setVisibility(View.INVISIBLE);
            }else
            {
                holder.mImbChecked.setVisibility(View.VISIBLE);
                holder.mImbChecked.setImageResource(R.mipmap.ic_doubletick55);
            }*/





          /*  holder.mTxtInv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItemFromList(position);
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView mTxtInv,mTxtRepcode,mTxtRepname,mTxtAddress,mTxtTel;
        public ImageView mImbChecked;


        public ViewHolder(View v) {
            super(v);

            this.mTxtInv = (TextView) v.findViewById(R.id.txtInv);
            this.mTxtRepcode = (TextView) v.findViewById(R.id.txtRepcode);
            this.mTxtRepname = (TextView) v.findViewById(R.id.txtRepname);
            this.mTxtAddress = (TextView) v.findViewById(R.id.txtAddress);
            this.mTxtTel = (TextView) v.findViewById(R.id.txtTelMSL);
            this.mImbChecked = (ImageView) v.findViewById(R.id.imbChecked);





            Typeface tf = Typeface.createFromAsset(v.getContext().getAssets(), defaultFonts);

            this.mTxtInv.setTypeface(tf);
            this.mTxtRepcode.setTypeface(tf);
            this.mTxtRepname.setTypeface(tf);
            this.mTxtAddress.setTypeface(tf);
            this.mTxtTel.setTypeface(tf);




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

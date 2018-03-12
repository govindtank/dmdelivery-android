package com.bl.dmdelivery.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.dmdelivery.R;
import com.thesurix.gesturerecycler.GestureViewHolder;


public class OrderViewHolder extends GestureViewHolder {

    private static final int SELECT_DURATION_IN_MS = 250;

//    @BindView(R.id.month_text)
//    TextView mMonthText;

//    @BindView(R.id.month_image)
//    ImageView mMonthPicture;

//    @BindView(R.id.mont_drag)
//    ImageView mItemDrag;

//    @Nullable
//    @BindView(R.id.foreground)
//    View mForegroundView;

//    @Nullable
//    @BindView(R.id.month_background_stub)
//    ViewStub mBackgroundView;

    public TextView mSeqText,txtRepcode,txtInv,txtAddress,txtMslTel,txtDsmTel;
    public Button btnTelActivity;
    public ImageView mMonthPicture,mItemDrag;
    public View mForegroundView;
    public ViewStub mBackgroundView;

    public OrderViewHolder(final View view) {
        super(view);

        this.mSeqText = (TextView) view.findViewById(R.id.seqTxt);
        this.txtRepcode = (TextView) view.findViewById(R.id.txtRepcode);
        this.txtInv = (TextView) view.findViewById(R.id.txtInv);
        this.txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        this.txtMslTel = (TextView) view.findViewById(R.id.txtMslTel);
        this.txtDsmTel = (TextView) view.findViewById(R.id.txtDsmTel);
//        this.btnTelActivity = (Button) view.findViewById(R.id.btnTelActivity);

        //this.mMonthPicture = (ImageView) view.findViewById(R.id.month_image);
        this.mItemDrag = (ImageView) view.findViewById(R.id.mont_drag);

        //this.mForegroundView = (View) view.findViewById(R.id.foreground);
        //this.mBackgroundView = (ViewStub) view.findViewById(R.id.month_background_stub);

        //ButterKnife.bind(this, view);

//        btnTelActivity.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//
//                    Toast toast = Toast.makeText(view.getContext(), "btnTelActivity Onclick ", Toast.LENGTH_SHORT);
//                    toast.show();
//
//                }
//            });
    }

    @Nullable
    @Override
    public View getDraggableView() {
        return mItemDrag;
    }

    @Override
    public View getForegroundView() {
        return mForegroundView == null ? super.getForegroundView() : mForegroundView;
    }

    @Nullable
    @Override
    public View getBackgroundView() {
        return mBackgroundView;
    }

    @Override
    public void onItemSelect() {
        final int textColorFrom = itemView.getContext().getResources().getColor(android.R.color.white);
        final int textColorTo = itemView.getContext().getResources().getColor(R.color.colorPrimary);
        final ValueAnimator textAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), textColorFrom, textColorTo);
        textAnimation.setDuration(SELECT_DURATION_IN_MS);
        textAnimation.addUpdateListener(getTextAnimatorListener(mSeqText, textAnimation));
        textAnimation.start();

        final int backgroundColorFrom = itemView.getContext().getResources().getColor(R.color.colorPrimary);
        final int backgroundColorTo = itemView.getContext().getResources().getColor(android.R.color.white);
        final ValueAnimator backgroundAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), backgroundColorFrom, backgroundColorTo);
        backgroundAnimation.setDuration(SELECT_DURATION_IN_MS);
        backgroundAnimation.addUpdateListener(getBackgroundAnimatorListener(mSeqText, backgroundAnimation));
        backgroundAnimation.start();
    }

    @Override
    public void onItemClear() {
        final int textColorFrom = itemView.getContext().getResources().getColor(R.color.colorPrimary);
        final int textColorTo = itemView.getContext().getResources().getColor(android.R.color.white);
        final ValueAnimator textAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), textColorFrom, textColorTo);
        textAnimation.setDuration(SELECT_DURATION_IN_MS);
        textAnimation.addUpdateListener(getTextAnimatorListener(mSeqText, textAnimation));
        textAnimation.start();

        final int backgroundColorFrom = itemView.getContext().getResources().getColor(android.R.color.white);
        final int backgroundColorTo = itemView.getContext().getResources().getColor(R.color.colorPrimary);
        final ValueAnimator backgroundAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), backgroundColorFrom, backgroundColorTo);
        backgroundAnimation.setDuration(SELECT_DURATION_IN_MS);
        backgroundAnimation.addUpdateListener(getBackgroundAnimatorListener(mSeqText, backgroundAnimation));
        backgroundAnimation.start();
    }

    @Override
    public boolean canDrag() {
        return true;
    }

    @Override
    public boolean canSwipe() {
        return true;
    }

    private ValueAnimator.AnimatorUpdateListener getBackgroundAnimatorListener(final TextView view, final ValueAnimator animator) {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        };
    }

    private ValueAnimator.AnimatorUpdateListener getTextAnimatorListener(final TextView view, final ValueAnimator animator) {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                view.setTextColor((int) animator.getAnimatedValue());
            }
        };
    }
}

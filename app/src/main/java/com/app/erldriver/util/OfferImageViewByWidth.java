package com.app.erldriver.util;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


public class OfferImageViewByWidth extends AppCompatImageView {

    public OfferImageViewByWidth(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OfferImageViewByWidth(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width * 0.6));
    }

}

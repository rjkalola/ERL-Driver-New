package com.app.erldriver.util;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


public class SquareImageViewByHeight extends AppCompatImageView {

    public SquareImageViewByHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageViewByHeight(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }

}

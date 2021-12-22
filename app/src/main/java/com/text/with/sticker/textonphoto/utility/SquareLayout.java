package com.text.with.sticker.textonphoto.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;




public class SquareLayout extends LinearLayout {
    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        getMeasuredHeight();
        int widthMeasureSpec2 = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec2, widthMeasureSpec2);
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = width;
        setLayoutParams(params);
        setMeasuredDimension(width, width);
        super.onLayout(changed, l, t, r, t + width);
    }
}

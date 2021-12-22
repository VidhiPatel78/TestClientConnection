package com.text.with.sticker.textonphoto.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.text.with.sticker.textonphoto.R;


@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context ctx, AttributeSet attrs) {
        String assetName;
        TypedArray typedAttr = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        if (typedAttr.hasValue(0) && (assetName = typedAttr.getString(0)) != null && assetName.length() > 0) {
            if (assetName.equals("Header")) {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), "DroidSans.ttf"));
            } else {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), "DroidSans.ttf"));
            }
        }
        if (typedAttr.hasValue(1)) {
            setTextColor(typedAttr.getColor(1, Color.argb(0, 0, 0, 0)));
        }
        typedAttr.recycle();
    }
}

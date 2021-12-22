package com.text.with.sticker.textonphoto.sticker;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class CustomImageViewBook extends StickerView {
    private Bitmap bitmap = null;
    private boolean hide;
    private CallbackListener listener = null;
    private ImageView tv_main;

    public interface CallbackListener {
        void onDeleteCallback(View view);

        void onTouchCallback(View view);
    }

    public void setCallbackListener(CallbackListener l) {
        this.listener = l;
    }

    public CustomImageViewBook(Context context) {
        super(context);
    }

    public CustomImageViewBook(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageViewBook(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    @Override // com.textonphoto.customqoutescreator.sticker.StickerView
    public View getMainView() {
        ImageView imageView = this.tv_main;
        if (imageView != null) {
            return imageView;
        }
        this.tv_main = new ImageView(getContext());
        setFocusable(true);
        MultiTouchListener multiTouchListener = new MultiTouchListener();
        multiTouchListener.setOnTouchCallbackListener(new MultiTouchListener.TouchCallbackListener() {
            /* class com.textonphoto.customqoutescreator.sticker.CustomImageViewBook.AnonymousClass1 */

            @Override // com.textonphoto.customqoutescreator.sticker.MultiTouchListener.TouchCallbackListener
            public void onTouchCallback(View v) {
                CustomImageViewBook.this.setControlItemsHidden(false);
                CustomImageViewBook.this.bringToFront();
            }
        });
        setOnTouchListener(multiTouchListener);
        setControlItemsHidden(true);
        return this.tv_main;
    }

    public boolean gethide() {
        return this.hide;
    }

    public void setImageResource(int imageResource) {
        this.tv_main.setImageResource(imageResource);
    }

    public void setImageUri(Uri uri) {
        this.tv_main.setImageURI(uri);
    }

    public Bitmap getImageBitmap() {
        if (this.bitmap == null) {
            setControlItemsHidden(true);
            this.tv_main.setDrawingCacheEnabled(true);
            this.bitmap = this.tv_main.getDrawingCache();
        }
        if (this.bitmap.isRecycled()) {
            try {
                ContentResolver contentResolver = getContext().getContentResolver();
                this.bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse("file://" + getTag()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.bitmap;
    }

    public void setImageBitmap(Bitmap bitmap2) {
        this.tv_main.setImageBitmap(bitmap2);
    }
}

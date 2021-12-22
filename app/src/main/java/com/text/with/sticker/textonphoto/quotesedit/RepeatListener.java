package com.text.with.sticker.textonphoto.quotesedit;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class RepeatListener implements View.OnTouchListener {
    private final View.OnClickListener clickListener;
    private View downView;
    private ImageView guideline;
    private Handler handler = new Handler();
    private Runnable handlerRunnable = new Runnable() {
        /* class com.textonphoto.customqoutescreator.quotesedit.RepeatListener.AnonymousClass1 */

        public void run() {
            RepeatListener.this.handler.postDelayed(this, (long) RepeatListener.this.normalInterval);
            RepeatListener.this.clickListener.onClick(RepeatListener.this.downView);
        }
    };
    private int initialInterval;
    private final int normalInterval;

    public RepeatListener(int initialInterval2, int normalInterval2, View.OnClickListener clickListener2) {
        if (clickListener2 == null) {
            throw new IllegalArgumentException("null runnable");
        } else if (initialInterval2 < 0 || normalInterval2 < 0) {
            throw new IllegalArgumentException("negative interval");
        } else {
            this.initialInterval = initialInterval2;
            this.normalInterval = normalInterval2;
            this.clickListener = clickListener2;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.handler.removeCallbacks(this.handlerRunnable);
                this.handler.postDelayed(this.handlerRunnable, (long) this.initialInterval);
                this.downView = view;
                view.setPressed(true);
                this.clickListener.onClick(view);
                return true;
            case 1:
            case 3:
                this.handler.removeCallbacks(this.handlerRunnable);
                this.downView.setPressed(false);
                this.downView = null;
                return true;
            case 2:
            default:
                return false;
        }
    }
}

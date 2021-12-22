package com.text.with.sticker.textonphoto.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.text.with.sticker.textonphoto.R;


public abstract class StickerView extends FrameLayout {
    private static final int BUTTON_SIZE_DP = 30;
    private static final int SELF_SIZE_DP = 100;
    public static final String TAG = "com.knef.stickerView";
    private double centerX;
    private double centerY;
    private BorderView iv_border;
    private ImageView iv_delete;
    private ImageView iv_move;
    private ImageView iv_rotate;
    private ImageView iv_scale;
    private OnTouchListener mTouchListener = new OnTouchListener() {
        /* class com.textonphoto.customqoutescreator.sticker.StickerView.AnonymousClass1 */

        public boolean onTouch(View view, MotionEvent event) {
            StickerView.this.bringToFront();
            if (!view.getTag().equals("DraggableViewGroup")) {
                if (!view.getTag().equals("iv_move")) {
                    if (!view.getTag().equals("iv_rotate") && view.getTag().equals("iv_scale")) {
                        switch (event.getAction()) {
                            case 0:
                                StickerView.this.move_orgX = event.getRawX();
                                StickerView.this.rlX = ((View) view.getParent()).getX();
                                break;
                            case 2:
                                float offsetX = event.getRawX() - StickerView.this.move_orgX;
                                ((View) view.getParent()).setX(StickerView.this.rlX);
                                ((View) view.getParent()).getLayoutParams().width = (int) (((float) ((View) view.getParent()).getLayoutParams().width) + offsetX);
                                ((View) view.getParent()).getLayoutParams().height = (int) (((float) ((View) view.getParent()).getLayoutParams().height) + offsetX);
                                ((View) view.getParent()).postInvalidate();
                                ((View) view.getParent()).requestLayout();
                                StickerView.this.move_orgX = event.getRawX();
                                break;
                        }
                    }
                    switch (event.getAction()) {
                        case 0:
                            StickerView.this.centerX = (double) ((((float) ((View) view.getParent()).getWidth()) / 2.0f) + ((View) view.getParent()).getX() + ((View) ((View) view.getParent()).getParent()).getX());
                            int result = 0;
                            int resourceId = StickerView.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                            if (resourceId > 0) {
                                result = StickerView.this.getResources().getDimensionPixelSize(resourceId);
                            }
                            double statusBarHeight = (double) result;
                            StickerView stickerView = StickerView.this;
                            double y = (double) (((View) ((View) view.getParent()).getParent()).getY() + ((View) view.getParent()).getY());
                            Double.isNaN(y);
                            Double.isNaN(statusBarHeight);
                            double height = (double) (((float) ((View) view.getParent()).getHeight()) / 2.0f);
                            Double.isNaN(height);
                            stickerView.centerY = y + statusBarHeight + height;
                            break;
                        case 2:
                            double rawY = (double) event.getRawY();
                            double d = StickerView.this.centerY;
                            Double.isNaN(rawY);
                            double d2 = rawY - d;
                            double rawX = (double) event.getRawX();
                            double d3 = StickerView.this.centerX;
                            Double.isNaN(rawX);
                            ((View) view.getParent()).setRotation(((float) ((Math.atan2(d2, rawX - d3) * 180.0d) / 3.141592653589793d)) - 180.0f);
                            ((View) view.getParent()).postInvalidate();
                            ((View) view.getParent()).requestLayout();
                            break;
                    }
                }
                switch (event.getAction()) {
                    case 0:
                        Log.v(StickerView.TAG, "sticker view action down");
                        StickerView.this.move_orgX = event.getRawX();
                        StickerView.this.move_orgY = event.getRawY();
                        break;
                    case 1:
                        Log.v(StickerView.TAG, "sticker view action up");
                        break;
                    case 2:
                        Log.v(StickerView.TAG, "sticker view action move");
                        float offsetY = event.getRawY() - StickerView.this.move_orgY;
                        StickerView stickerView2 = StickerView.this;
                        stickerView2.setX(stickerView2.getX() + (event.getRawX() - StickerView.this.move_orgX));
                        StickerView stickerView3 = StickerView.this;
                        stickerView3.setY(stickerView3.getY() + offsetY);
                        StickerView.this.move_orgX = event.getRawX();
                        StickerView.this.move_orgY = event.getRawY();
                        break;
                }
            }
            switch (event.getAction()) {
                case 0:
                    Log.v(StickerView.TAG, "sticker view action down");
                    StickerView.this.move_orgX = event.getRawX();
                    StickerView.this.move_orgY = event.getRawY();
                    return true;
                case 1:
                    Log.v(StickerView.TAG, "sticker view action up");
                    return true;
                case 2:
                    Log.v(StickerView.TAG, "sticker view action move");
                    Float offsetY2 = Float.valueOf(event.getRawY() - StickerView.this.move_orgY);
                    StickerView stickerView4 = StickerView.this;
                    stickerView4.setX(stickerView4.getX() + (event.getRawX() - StickerView.this.move_orgX));
                    StickerView stickerView5 = StickerView.this;
                    stickerView5.setY(stickerView5.getY() + offsetY2.floatValue());
                    StickerView.this.move_orgX = event.getRawX();
                    StickerView.this.move_orgY = event.getRawY();
                    return true;
                default:
                    return true;
            }
        }
    };
    private float move_orgX = -1.0f;
    private float move_orgY = -1.0f;
    float rlX;
    private float rotate_newX = -1.0f;
    private float rotate_newY = -1.0f;
    private float rotate_orgX = -1.0f;
    private float rotate_orgY = -1.0f;
    private double scale_orgHeight = -1.0d;
    private double scale_orgWidth = -1.0d;
    private float scale_orgX = -1.0f;
    private float scale_orgY = -1.0f;
    private float this_orgX = -1.0f;
    private float this_orgY = -1.0f;

    /* access modifiers changed from: protected */
    public abstract View getMainView();

    /* access modifiers changed from: private */
    public class BorderView extends View {
        public BorderView(Context context) {
            super(context);
        }

        public BorderView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BorderView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            LayoutParams params = (LayoutParams) getLayoutParams();
            Drawable d = getResources().getDrawable(R.drawable.border);
            d.setBounds(getLeft() - params.leftMargin, getTop() - params.topMargin, getRight() - params.rightMargin, getBottom() - params.bottomMargin);
            d.draw(canvas);
        }
    }

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.iv_border = new BorderView(context);
        this.iv_scale = new ImageView(context);
        this.iv_move = new ImageView(context);
        this.iv_delete = new ImageView(context);
        this.iv_rotate = new ImageView(context);
        this.iv_scale.setImageResource(R.drawable.scale);
        this.iv_move.setImageResource(R.drawable.move);
        this.iv_delete.setImageResource(R.drawable.remove);
        this.iv_rotate.setImageResource(R.drawable.rotate);
        setTag("DraggableViewGroup");
        this.iv_border.setTag("iv_border");
        this.iv_scale.setTag("iv_scale");
        this.iv_move.setTag("iv_move");
        this.iv_delete.setTag("iv_delete");
        this.iv_rotate.setTag("iv_rotate");
        int size = convertDpToPixel(100.0f, getContext()) + ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        Log.e("margin is", "" + size);
        LayoutParams this_params = new LayoutParams(size, size);
        this_params.gravity = 17;
        LayoutParams iv_main_params = new LayoutParams(-1, -1);
        iv_main_params.setMargins(30, 30, 30, 30);
        LayoutParams iv_border_params = new LayoutParams(-1, -1);
        iv_border_params.setMargins(0, 0, 0, 0);
        LayoutParams iv_scale_params = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        iv_scale_params.gravity = 85;
        iv_scale_params.setMargins(20, 20, 20, 20);
        LayoutParams iv_move_params = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        iv_move_params.gravity = 83;
        iv_move_params.setMargins(20, 20, 20, 20);
        LayoutParams iv_delete_params = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        iv_delete_params.gravity = 53;
        iv_delete_params.setMargins(20, 20, 20, 20);
        LayoutParams iv_flip_params = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        iv_flip_params.gravity = 51;
        iv_flip_params.setMargins(20, 20, 20, 20);
        setLayoutParams(this_params);
        addView(getMainView(), iv_main_params);
        addView(this.iv_border, iv_border_params);
        addView(this.iv_scale, iv_scale_params);
        addView(this.iv_move, iv_move_params);
        addView(this.iv_delete, iv_delete_params);
        addView(this.iv_rotate, iv_flip_params);
        this.iv_move.setOnTouchListener(this.mTouchListener);
        this.iv_rotate.setOnTouchListener(this.mTouchListener);
        this.iv_scale.setOnTouchListener(this.mTouchListener);
        this.iv_delete.setOnClickListener(new OnClickListener() {
            /* class com.textonphoto.customqoutescreator.sticker.StickerView.AnonymousClass2 */

            public void onClick(View view) {
                if (StickerView.this.getParent() != null) {
                    ((ViewGroup) StickerView.this.getParent()).removeView(StickerView.this);
                }
            }
        });
        this.iv_rotate.setOnClickListener(new OnClickListener() {
            /* class com.textonphoto.customqoutescreator.sticker.StickerView.AnonymousClass3 */

            public void onClick(View view) {
                float f = -180.0f;
                Log.v(StickerView.TAG, "flip the view");
                StickerView.this.bringToFront();
                View mainView = StickerView.this.getMainView();
                if (mainView.getRotationY() == -180.0f) {
                    f = 0.0f;
                }
                mainView.setRotationY(f);
                mainView.invalidate();
                StickerView.this.requestLayout();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2.0d) + Math.pow(x2 - x1, 2.0d));
    }

    private float[] getRelativePos(float absX, float absY) {
        return new float[]{absX - ((View) getParent()).getX(), absY - ((View) getParent()).getY()};
    }

    @SuppressLint("WrongConstant")
    public void setControlItemsHidden(boolean isHidden) {
        if (isHidden) {
            this.iv_border.setVisibility(4);
            this.iv_scale.setVisibility(4);
            this.iv_move.setVisibility(4);
            this.iv_delete.setVisibility(4);
            this.iv_rotate.setVisibility(4);
            return;
        }
        this.iv_border.setVisibility(0);
        this.iv_scale.setVisibility(0);
        this.iv_move.setVisibility(0);
        this.iv_delete.setVisibility(0);
        this.iv_rotate.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void onScaling(boolean scaleUp) {
    }

    /* access modifiers changed from: protected */
    public void onRotating() {
    }

    private static int convertDpToPixel(float dp, Context context) {
        return (int) ((((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * dp);
    }
}

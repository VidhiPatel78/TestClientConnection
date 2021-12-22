package com.text.with.sticker.textonphoto.quotesedit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;


import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.activity.AddTextQuotesActivity;


public class ResizableImageview extends RelativeLayout {
    private static final int SELF_SIZE_DP = 30;
    public static final String TAG = "ResizableImageview";
    int alphaProg = 255;
    double angle = 0.0d;
    int baseh;
    int basew;
    int basex;
    int basey;
    private ImageView border_iv;
    float cX = 0.0f;
    float cY = 0.0f;
    private double centerX;
    private double centerY;
    private Context context;
    double dAngle = 0.0d;
    private ImageView delete_iv;
    private String drawableId;
    private ImageView flip_iv;
    private int he;
    float heightMain;
    int hueProg = 1;
    private boolean isBorderVisible = false;
    private boolean isColorFilterEnable = false;
    public boolean isMultiTouchEnabled = true;
    private boolean isSticker = true;
    private TouchEventListener listener = null;
    private OnTouchListener mTouchListener = new OnTouchListener() {
        /* class com.textonphoto.customqoutescreator.quotesedit.ResizableImageview.AnonymousClass1 */

        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    Rect rect = new Rect();
                    ((View) view.getParent()).getGlobalVisibleRect(rect);
                    ResizableImageview.this.cX = rect.exactCenterX();
                    ResizableImageview.this.cY = rect.exactCenterY();
                    ResizableImageview.this.vAngle = (double) ((View) view.getParent()).getRotation();
                    ResizableImageview resizableImageview = ResizableImageview.this;
                    resizableImageview.tAngle = (Math.atan2((double) (resizableImageview.cY - event.getRawY()), (double) (ResizableImageview.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                    ResizableImageview resizableImageview2 = ResizableImageview.this;
                    resizableImageview2.dAngle = resizableImageview2.vAngle - ResizableImageview.this.tAngle;
                    return true;
                case 1:
                default:
                    return true;
                case 2:
                    ResizableImageview resizableImageview3 = ResizableImageview.this;
                    resizableImageview3.angle = (Math.atan2((double) (resizableImageview3.cY - event.getRawY()), (double) (ResizableImageview.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                    float rotation = (float) (ResizableImageview.this.angle + ResizableImageview.this.dAngle);
                    if (rotation > -5.0f && rotation < 5.0f) {
                        rotation = 0.0f;
                    }
                    Log.i("rotation", "" + (ResizableImageview.this.angle + ResizableImageview.this.dAngle));
                    ((View) view.getParent()).setRotation(rotation);
                    return true;
            }
        }
    };
    private OnTouchListener mTouchListener1 = new OnTouchListener() {
        /* class com.textonphoto.customqoutescreator.quotesedit.ResizableImageview.AnonymousClass2 */

        public boolean onTouch(View view, MotionEvent event) {
            ResizableImageview rl = (ResizableImageview) view.getParent();
            int j = (int) event.getRawX();
            int i = (int) event.getRawY();
            LayoutParams layoutParams = (LayoutParams) ResizableImageview.this.getLayoutParams();
            switch (event.getAction()) {
                case 0:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    ResizableImageview.this.invalidate();
                    ResizableImageview.this.basex = j;
                    ResizableImageview.this.basey = i;
                    ResizableImageview resizableImageview = ResizableImageview.this;
                    resizableImageview.basew = resizableImageview.getWidth();
                    ResizableImageview resizableImageview2 = ResizableImageview.this;
                    resizableImageview2.baseh = resizableImageview2.getHeight();
                    ResizableImageview.this.getLocationOnScreen(new int[2]);
                    ResizableImageview.this.margl = layoutParams.leftMargin;
                    ResizableImageview.this.margt = layoutParams.topMargin;
                    break;
                case 1:
                    ResizableImageview resizableImageview3 = ResizableImageview.this;
                    resizableImageview3.wi = resizableImageview3.getLayoutParams().width;
                    ResizableImageview resizableImageview4 = ResizableImageview.this;
                    resizableImageview4.he = resizableImageview4.getLayoutParams().height;
                    ResizableImageview resizableImageview5 = ResizableImageview.this;
                    resizableImageview5.margl = ((LayoutParams) resizableImageview5.getLayoutParams()).leftMargin;
                    ResizableImageview resizableImageview6 = ResizableImageview.this;
                    resizableImageview6.margt = ((LayoutParams) resizableImageview6.getLayoutParams()).topMargin;
                    break;
                case 2:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    float f2 = (float) Math.toDegrees(Math.atan2((double) (i - ResizableImageview.this.basey), (double) (j - ResizableImageview.this.basex)));
                    float f1 = f2;
                    if (f2 < 0.0f) {
                        f1 = f2 + 360.0f;
                    }
                    int j2 = j - ResizableImageview.this.basex;
                    int k = i - ResizableImageview.this.basey;
                    int i2 = (int) (Math.sqrt((double) ((j2 * j2) + (k * k))) * Math.cos(Math.toRadians((double) (f1 - ResizableImageview.this.getRotation()))));
                    int j3 = (int) (Math.sqrt((double) ((i2 * i2) + (k * k))) * Math.sin(Math.toRadians((double) (f1 - ResizableImageview.this.getRotation()))));
                    int k2 = (i2 * 2) + ResizableImageview.this.basew;
                    int m = (j3 * 2) + ResizableImageview.this.baseh;
                    if (k2 > ResizableImageview.this.s) {
                        layoutParams.width = k2;
                        layoutParams.leftMargin = ResizableImageview.this.margl - i2;
                    }
                    if (m > ResizableImageview.this.s) {
                        layoutParams.height = m;
                        layoutParams.topMargin = ResizableImageview.this.margt - j3;
                    }
                    ResizableImageview.this.setLayoutParams(layoutParams);
                    ResizableImageview.this.performLongClick();
                    break;
            }
            return true;
        }
    };
    private OnTouchListener mTouchListener2 = new OnTouchListener() {
        /* class com.textonphoto.customqoutescreator.quotesedit.ResizableImageview.AnonymousClass3 */

        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    ResizableImageview.this.scale_orgX = event.getRawX();
                    ResizableImageview.this.scale_orgY = event.getRawY();
                    ResizableImageview.this.this_orgX = ((View) view.getParent()).getX();
                    ResizableImageview.this.this_orgY = ((View) view.getParent()).getY();
                    return true;
                case 1:
                    ResizableImageview resizableImageview = ResizableImageview.this;
                    resizableImageview.wi = resizableImageview.getLayoutParams().width;
                    ResizableImageview resizableImageview2 = ResizableImageview.this;
                    resizableImageview2.he = resizableImageview2.getLayoutParams().height;
                    return true;
                case 2:
                    float offsetX = event.getRawX() - ResizableImageview.this.scale_orgX;
                    float offsetY = event.getRawY() - ResizableImageview.this.scale_orgY;
                    ((View) view.getParent()).setX(ResizableImageview.this.this_orgX);
                    ((View) view.getParent()).setY(ResizableImageview.this.this_orgY);
                    ((View) view.getParent()).getLayoutParams().width = (int) (((float) ((View) view.getParent()).getLayoutParams().width) + offsetX);
                    ((View) view.getParent()).getLayoutParams().height = (int) (((float) ((View) view.getParent()).getLayoutParams().height) + offsetY);
                    ((View) view.getParent()).postInvalidate();
                    ((View) view.getParent()).requestLayout();
                    ResizableImageview.this.scale_orgX = event.getRawX();
                    ResizableImageview.this.scale_orgY = event.getRawY();
                    return true;
                default:
                    return true;
            }
        }
    };
    public ImageView main_iv;
    int margl;
    int margt;
    private ImageView move_iv;
    private float rotation;
    private int s;
    private ImageView scale_iv;
    private float scale_orgX = -1.0f;
    private float scale_orgY = -1.0f;
    private String stickerType;
    double tAngle = 0.0d;
    private float this_orgX = -1.0f;
    private float this_orgY = -1.0f;
    double vAngle = 0.0d;
    float view_width;
    private int wi;
    float widthMain;
    private float yRotation;
    Animation zoomInScale;
    Animation zoomOutScale;

    public interface TouchEventListener {
        void onDelete(View view);
    }

    public ResizableImageview setOnTouchCallbackListener(TouchEventListener l) {
        this.listener = l;
        return this;
    }

    public ResizableImageview(Context context2) {
        super(context2);
        init(context2);
    }

    public ResizableImageview(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        init(context2);
    }

    public ResizableImageview(Context context2, AttributeSet attrs, int defStyleAttr) {
        super(context2, attrs, defStyleAttr);
        init(context2);
    }

    public void setMainLayoutWH(float wMLay, float hMLay) {
        this.widthMain = wMLay;
        this.heightMain = hMLay;
    }

    public float getMainWidth() {
        return this.widthMain;
    }

    public float getMainHeight() {
        return this.heightMain;
    }

    public void init(Context ctx) {
        this.context = ctx;
        this.main_iv = new ImageView(this.context);
        this.scale_iv = new ImageView(this.context);
        this.border_iv = new ImageView(this.context);
        this.flip_iv = new ImageView(this.context);
        this.delete_iv = new ImageView(this.context);
        this.move_iv = new ImageView(this.context);
        this.s = dpToPx(this.context, 25);
        this.wi = dpToPx(this.context, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.he = dpToPx(this.context, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.view_width = (float) dpToPx(this.context, 25);
        this.scale_iv.setImageResource(R.drawable.scale);
        this.border_iv.setImageResource(R.drawable.border);
        this.flip_iv.setImageResource(R.drawable.flip);
        this.delete_iv.setImageResource(R.drawable.remove);
        this.move_iv.setImageResource(R.drawable.rotate);
        LayoutParams lp = new LayoutParams(this.wi, this.he);
        LayoutParams mlp = new LayoutParams(-1, -1);
        mlp.setMargins(10, 10, 10, 10);
        mlp.addRule(17);
        int i = this.s;
        LayoutParams slp = new LayoutParams(i, i);
        slp.addRule(12);
        slp.addRule(11);
        slp.setMargins(10, 10, 10, 10);
        int i2 = this.s;
        LayoutParams flp = new LayoutParams(i2, i2);
        flp.addRule(10);
        flp.addRule(9);
        flp.setMargins(10, 10, 10, 10);
        int i3 = this.s;
        LayoutParams dlp = new LayoutParams(i3, i3);
        dlp.addRule(10);
        dlp.addRule(11);
        dlp.setMargins(10, 10, 10, 10);
        int i4 = this.s;
        LayoutParams mlp1 = new LayoutParams(i4, i4);
        mlp1.addRule(12);
        mlp1.addRule(9);
        mlp1.setMargins(10, 10, 10, 10);
        LayoutParams blp = new LayoutParams(-1, -1);
        setLayoutParams(lp);
        setBackgroundResource(R.drawable.border_gray);
        addView(this.main_iv);
        this.main_iv.setLayoutParams(mlp);
        this.main_iv.setTag("main_iv");
        addView(this.border_iv);
        this.border_iv.setLayoutParams(blp);
        this.border_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        this.border_iv.setTag("border_iv");
        addView(this.flip_iv);
        this.flip_iv.setLayoutParams(flp);
        this.flip_iv.setOnClickListener(new OnClickListener() {
            /* class com.textonphoto.customqoutescreator.quotesedit.ResizableImageview.AnonymousClass4 */

            public void onClick(View v) {
                float f = -180.0f;
                ImageView imageView = ResizableImageview.this.main_iv;
                if (ResizableImageview.this.main_iv.getRotationY() == -180.0f) {
                    f = 0.0f;
                }
                imageView.setRotationY(f);
                ResizableImageview.this.main_iv.invalidate();
                ResizableImageview.this.requestLayout();
            }
        });
        addView(this.delete_iv);
        this.delete_iv.setLayoutParams(dlp);
        this.delete_iv.setOnClickListener(new OnClickListener() {
            /* class com.textonphoto.customqoutescreator.quotesedit.ResizableImageview.AnonymousClass5 */

            public void onClick(View v) {
                final ViewGroup parent = (ViewGroup) ResizableImageview.this.getParent();
                ResizableImageview.this.zoomInScale.setAnimationListener(new Animation.AnimationListener() {
                    /* class com.textonphoto.customqoutescreator.quotesedit.ResizableImageview.AnonymousClass5.AnonymousClass1 */

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        parent.removeView(ResizableImageview.this);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                ResizableImageview.this.main_iv.startAnimation(ResizableImageview.this.zoomInScale);
                ResizableImageview.this.setBorderVisibility(false);
                if (ResizableImageview.this.listener != null) {
                    ResizableImageview.this.listener.onDelete(ResizableImageview.this);
                }
            }
        });
        addView(this.move_iv);
        this.move_iv.setLayoutParams(mlp1);
        this.move_iv.setOnTouchListener(this.mTouchListener);
        this.rotation = getRotation();
        addView(this.scale_iv);
        this.scale_iv.setLayoutParams(slp);
        this.scale_iv.setOnTouchListener(this.mTouchListener1);
        this.scale_iv.setTag("scale_iv");
        this.isMultiTouchEnabled = setDefaultTouchListener(true);
        this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_out);
        this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_in);
    }

    public boolean setDefaultTouchListener(boolean enable) {
        if (enable) {
            setOnTouchListener(new MultiTouchListener().enableRotation(true).setOnTouchCallbackListener((AddTextQuotesActivity) this.context));
            setOnTouchCallbackListener((AddTextQuotesActivity) this.context);
            return true;
        }
        setOnTouchListener(null);
        return false;
    }

    @SuppressLint("WrongConstant")
    public void setBorderVisibility(boolean ch) {
        this.isBorderVisible = ch;
        if (!ch) {
            this.border_iv.setVisibility(8);
            this.scale_iv.setVisibility(8);
            this.flip_iv.setVisibility(8);
            this.delete_iv.setVisibility(8);
            this.move_iv.setVisibility(8);
            setBackgroundResource(0);
        } else if (this.border_iv.getVisibility() != 0) {
            this.border_iv.setVisibility(0);
            this.scale_iv.setVisibility(0);
            this.move_iv.setVisibility(0);
            if (this.isSticker) {
                this.flip_iv.setVisibility(0);
            }
            this.delete_iv.setVisibility(0);
            setBackgroundResource(R.drawable.border_gray);
        }
    }

    public boolean getBorderVisbilty() {
        return this.isBorderVisible;
    }

    public void setBgDrawable(String redId) {
        this.main_iv.setImageResource(getResources().getIdentifier(redId, "drawable", this.context.getPackageName()));
        this.drawableId = redId;
    }

    @SuppressLint("WrongConstant")
    public void setComponentInfo(ComponentInfo ci) {
        this.wi = ci.getWIDTH();
        this.he = ci.getHEIGHT();
        this.drawableId = ci.getRES_ID();
        this.rotation = ci.getROTATION();
        this.yRotation = ci.getY_ROTATION();
        this.hueProg = ci.getHUE();
        this.alphaProg = ci.getOPACITY();
        setBgDrawable(this.drawableId);
        setRotation(this.rotation);
        this.margl = ci.getLEFT();
        this.margt = ci.getTOP();
        Log.i("margintop", "" + ci.getPOS_X());
        Log.i("margintop", "" + ci.getPOS_Y());
        this.flip_iv.setVisibility(0);
        this.isSticker = true;
        String type = ci.getTYPE();
        this.stickerType = type;
        if (type.equals("COLOR")) {
            setColorFilter(this.hueProg);
        } else {
            setHueProg(this.hueProg);
        }
        settransparency(this.alphaProg);
        if (ci.getTYPE() == "SHAPE") {
            this.flip_iv.setVisibility(8);
            this.isSticker = false;
        }
        if (ci.getTYPE() == "STICKER") {
            this.flip_iv.setVisibility(0);
            this.isSticker = true;
        }
        this.main_iv.setRotationY(this.yRotation);
        ((LayoutParams) getLayoutParams()).leftMargin = this.margl;
        ((LayoutParams) getLayoutParams()).topMargin = this.margt;
        getLayoutParams().width = this.wi;
        getLayoutParams().height = this.he;
        setX(ci.getPOS_X() + ((float) (this.margl * -1)));
        setY(ci.getPOS_Y() + ((float) (this.margt * -1)));
        this.main_iv.startAnimation(this.zoomOutScale);
    }

    public void optimize(float wr, float hr) {
        setX(getX() * wr);
        setY(getY() * hr);
        getLayoutParams().width = (int) (((float) this.wi) * wr);
        getLayoutParams().height = (int) (((float) this.he) * hr);
    }

    public ComponentInfo getComponentInfo() {
        ComponentInfo ci = new ComponentInfo();
        ci.setPOS_X(getX());
        ci.setPOS_Y(getY());
        ci.setWIDTH(this.wi);
        ci.setHEIGHT(this.he);
        ci.setRES_ID(this.drawableId);
        ci.setROTATION(getRotation());
        ci.setY_ROTATION(this.main_iv.getRotationY());
        ci.setTOP(((LayoutParams) getLayoutParams()).topMargin);
        ci.setLEFT(((LayoutParams) getLayoutParams()).leftMargin);
        ci.setHUE(this.hueProg);
        ci.setTYPE(this.stickerType);
        ci.setOPACITY(this.alphaProg);
        Log.i("margintop", "" + getX());
        Log.i("margintop", "" + getY());
        Log.i("margintop", "" + this.margt);
        Log.i("margintopL", "" + this.margl);
        return ci;
    }

    public void incrX() {
        setX(getX() + 1.0f);
    }

    public void decX() {
        setX(getX() - 1.0f);
    }

    public void incrY() {
        setY(getY() + 1.0f);
    }

    public void decY() {
        setY(getY() - 1.0f);
    }

    public int dpToPx(Context c, int dp) {
        c.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) dp));
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2.0d) + Math.pow(x2 - x1, 2.0d));
    }

    public void enableColorFilter(boolean b) {
        this.isColorFilterEnable = b;
    }

    public void setHueProg(int hueProg2) {
        this.hueProg = hueProg2;
        if (hueProg2 == 0) {
            this.main_iv.setColorFilter(-1);
        } else if (hueProg2 == 100) {
            this.main_iv.setColorFilter(ViewCompat.MEASURED_STATE_MASK);
        } else {
            this.main_iv.setColorFilter(ColorFilterGenerator.adjustHue((float) hueProg2));
        }
    }

    public void setColorFilter(int hueProg2) {
        this.hueProg = hueProg2;
        this.main_iv.setColorFilter(hueProg2);
    }

    public void settransparency(int alphaProg2) {
        try {
            this.main_iv.setImageAlpha(alphaProg2);
            this.alphaProg = alphaProg2;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

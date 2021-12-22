package com.text.with.sticker.textonphoto.scale;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;


import com.text.with.sticker.textonphoto.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SubsamplingScaleImageView extends View {
    public static final int EASE_IN_OUT_QUAD = 2;
    public static final int EASE_OUT_QUAD = 1;
    private static final int MESSAGE_LONG_CLICK = 1;
    public static final int ORIENTATION_0 = 0;
    public static final int ORIENTATION_180 = 180;
    public static final int ORIENTATION_270 = 270;
    public static final int ORIENTATION_90 = 90;
    public static final int ORIENTATION_USE_EXIF = -1;
    public static final int ORIGIN_ANIM = 1;
    public static final int ORIGIN_DOUBLE_TAP_ZOOM = 4;
    public static final int ORIGIN_FLING = 3;
    public static final int ORIGIN_TOUCH = 2;
    public static final int PAN_LIMIT_CENTER = 3;
    public static final int PAN_LIMIT_INSIDE = 1;
    public static final int PAN_LIMIT_OUTSIDE = 2;
    public static final int SCALE_TYPE_CENTER_CROP = 2;
    public static final int SCALE_TYPE_CENTER_INSIDE = 1;
    public static final int SCALE_TYPE_CUSTOM = 3;
    public static final int SCALE_TYPE_START = 4;
    private static final String TAG = SubsamplingScaleImageView.class.getSimpleName();
    public static final int TILE_SIZE_AUTO = Integer.MAX_VALUE;
    private static final List<Integer> VALID_EASING_STYLES = Arrays.asList(2, 1);
    private static final List<Integer> VALID_ORIENTATIONS = Arrays.asList(0, 90, Integer.valueOf((int) ORIENTATION_180), Integer.valueOf((int) ORIENTATION_270), -1);
    private static final List<Integer> VALID_PAN_LIMITS = Arrays.asList(1, 2, 3);
    private static final List<Integer> VALID_SCALE_TYPES = Arrays.asList(2, 1, 3, 4);
    private static final List<Integer> VALID_ZOOM_STYLES = Arrays.asList(1, 2, 3);
    public static final int ZOOM_FOCUS_CENTER = 2;
    public static final int ZOOM_FOCUS_CENTER_IMMEDIATE = 3;
    public static final int ZOOM_FOCUS_FIXED = 1;
    private static Bitmap.Config preferredBitmapConfig;
    private Anim anim;
    private Bitmap bitmap;
    private DecoderFactory<? extends ImageDecoder> bitmapDecoderFactory;
    private boolean bitmapIsCached;
    private boolean bitmapIsPreview;
    private Paint bitmapPaint;
    private boolean debug;
    private Paint debugLinePaint;
    private Paint debugTextPaint;
    private ImageRegionDecoder decoder;
    private final ReadWriteLock decoderLock;
    private final float density;
    private GestureDetector detector;
    private int doubleTapZoomDuration;
    private float doubleTapZoomScale;
    private int doubleTapZoomStyle;
    private final float[] dstArray;
    private boolean eagerLoadingEnabled;
    private Executor executor;
    private int fullImageSampleSize;
    private final Handler handler;
    private boolean imageLoadedSent;
    private boolean isPanning;
    private boolean isQuickScaling;
    private boolean isZooming;
    private Matrix matrix;
    private float maxScale;
    private int maxTileHeight;
    private int maxTileWidth;
    private int maxTouchCount;
    private float minScale;
    private int minimumScaleType;
    private int minimumTileDpi;
    private OnImageEventListener onImageEventListener;
    private OnLongClickListener onLongClickListener;
    private OnStateChangedListener onStateChangedListener;
    private int orientation;
    private Rect pRegion;
    private boolean panEnabled;
    private int panLimit;
    private Float pendingScale;
    private boolean quickScaleEnabled;
    private float quickScaleLastDistance;
    private boolean quickScaleMoved;
    private PointF quickScaleSCenter;
    private final float quickScaleThreshold;
    private PointF quickScaleVLastPoint;
    private PointF quickScaleVStart;
    private boolean readySent;
    private DecoderFactory<? extends ImageRegionDecoder> regionDecoderFactory;
    private int sHeight;
    private int sOrientation;
    private PointF sPendingCenter;
    private RectF sRect;
    private Rect sRegion;
    private PointF sRequestedCenter;
    private int sWidth;
    private ScaleAndTranslate satTemp;
    private float scale;
    private float scaleStart;
    private GestureDetector singleDetector;
    private final float[] srcArray;
    private Paint tileBgPaint;
    private Map<Integer, List<Tile>> tileMap;
    private Uri uri;
    private PointF vCenterStart;
    private float vDistStart;
    private PointF vTranslate;
    private PointF vTranslateBefore;
    private PointF vTranslateStart;
    private boolean zoomEnabled;

    public interface OnAnimationEventListener {
        void onComplete();

        void onInterruptedByNewAnim();

        void onInterruptedByUser();
    }

    public interface OnImageEventListener {
        void onImageLoadError(Exception exc);

        void onImageLoaded();

        void onPreviewLoadError(Exception exc);

        void onPreviewReleased();

        void onReady();

        void onTileLoadError(Exception exc);
    }

    public interface OnStateChangedListener {
        void onCenterChanged(PointF pointF, int i);

        void onScaleChanged(float f, int i);
    }

    public SubsamplingScaleImageView(Context context, AttributeSet attr) {
        super(context, attr);
        int resId;
        String assetName;
        this.orientation = 0;
        this.maxScale = 2.0f;
        this.minScale = minScale();
        this.minimumTileDpi = -1;
        this.panLimit = 1;
        this.minimumScaleType = 1;
        this.maxTileWidth = Integer.MAX_VALUE;
        this.maxTileHeight = Integer.MAX_VALUE;
        this.executor = AsyncTask.THREAD_POOL_EXECUTOR;
        this.eagerLoadingEnabled = true;
        this.panEnabled = true;
        this.zoomEnabled = true;
        this.quickScaleEnabled = true;
        this.doubleTapZoomScale = 1.0f;
        this.doubleTapZoomStyle = 1;
        this.doubleTapZoomDuration = 500;
        this.decoderLock = new ReentrantReadWriteLock(true);
        this.bitmapDecoderFactory = new CompatDecoderFactory(SkiaImageDecoder.class);
        this.regionDecoderFactory = new CompatDecoderFactory(SkiaImageRegionDecoder.class);
        this.srcArray = new float[8];
        this.dstArray = new float[8];
        this.density = getResources().getDisplayMetrics().density;
        setMinimumDpi(160);
        setDoubleTapZoomDpi(160);
        setMinimumTileDpi(320);
        setGestureDetector(context);
        this.handler = new Handler(new Handler.Callback() {
            /* class com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.AnonymousClass1 */

            public boolean handleMessage(Message message) {
                if (message.what == 1 && SubsamplingScaleImageView.this.onLongClickListener != null) {
                    SubsamplingScaleImageView.this.maxTouchCount = 0;
                    SubsamplingScaleImageView subsamplingScaleImageView = SubsamplingScaleImageView.this;
                    SubsamplingScaleImageView.super.setOnLongClickListener(subsamplingScaleImageView.onLongClickListener);
                    SubsamplingScaleImageView.this.performLongClick();
                    SubsamplingScaleImageView.super.setOnLongClickListener(null);
                }
                return true;
            }
        });
        if (attr != null) {
            TypedArray typedAttr = getContext().obtainStyledAttributes(attr, R.styleable.SubsamplingScaleImageView);
            if (typedAttr.hasValue(R.styleable.SubsamplingScaleImageView_assetName)) {
                 assetName = typedAttr.getString(R.styleable.SubsamplingScaleImageView_assetName);
                if (assetName != null && assetName.length() > 0) {
                    setImage(ImageSource.asset(assetName).tilingEnabled());
                }
            }
            if (typedAttr.hasValue(R.styleable.SubsamplingScaleImageView_src)) {
                 resId = typedAttr.getResourceId(R.styleable.SubsamplingScaleImageView_src, 0);
                if (resId > 0) {
                    setImage(ImageSource.resource(resId).tilingEnabled());
                }
            }
            if (typedAttr.hasValue(R.styleable.SubsamplingScaleImageView_panEnabled)) {
                setPanEnabled(typedAttr.getBoolean(R.styleable.SubsamplingScaleImageView_panEnabled, true));
            }
            if (typedAttr.hasValue(R.styleable.SubsamplingScaleImageView_zoomEnabled)) {
                setZoomEnabled(typedAttr.getBoolean(R.styleable.SubsamplingScaleImageView_zoomEnabled, true));
            }
            if (typedAttr.hasValue(R.styleable.SubsamplingScaleImageView_quickScaleEnabled)) {
                setQuickScaleEnabled(typedAttr.getBoolean(R.styleable.SubsamplingScaleImageView_quickScaleEnabled, true));
            }
            if (typedAttr.hasValue(R.styleable.SubsamplingScaleImageView_tileBackgroundColor)) {
                setTileBackgroundColor(typedAttr.getColor(R.styleable.SubsamplingScaleImageView_tileBackgroundColor, Color.argb(0, 0, 0, 0)));
            }
            typedAttr.recycle();
        }
        quickScaleThreshold = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
    }

    public SubsamplingScaleImageView(Context context) {
        this(context, null);
    }

    public static Bitmap.Config getPreferredBitmapConfig() {
        return preferredBitmapConfig;
    }

    public static void setPreferredBitmapConfig(Bitmap.Config preferredBitmapConfig2) {
        preferredBitmapConfig = preferredBitmapConfig2;
    }

    public final void setOrientation(int orientation2) {
        if (VALID_ORIENTATIONS.contains(Integer.valueOf(orientation2))) {
            this.orientation = orientation2;
            reset(false);
            invalidate();
            requestLayout();
            return;
        }
        throw new IllegalArgumentException("Invalid orientation: " + orientation2);
    }

    public final void setImage(ImageSource imageSource) {
        setImage(imageSource, null, null);
    }

    public final void setImage(ImageSource imageSource, ImageViewState state) {
        setImage(imageSource, null, state);
    }

    public final void setImage(ImageSource imageSource, ImageSource previewSource) {
        setImage(imageSource, previewSource, null);
    }

    public final void setImage(ImageSource imageSource, ImageSource previewSource, ImageViewState state) {
        if (imageSource != null) {
            reset(true);
            if (state != null) {
                restoreState(state);
            }
            if (previewSource != null) {
                if (imageSource.getBitmap() != null) {
                    throw new IllegalArgumentException("Preview image cannot be used when a bitmap is provided for the main image");
                } else if (imageSource.getSWidth() <= 0 || imageSource.getSHeight() <= 0) {
                    throw new IllegalArgumentException("Preview image cannot be used unless dimensions are provided for the main image");
                } else {
                    this.sWidth = imageSource.getSWidth();
                    this.sHeight = imageSource.getSHeight();
                    this.pRegion = previewSource.getSRegion();
                    if (previewSource.getBitmap() != null) {
                        this.bitmapIsCached = previewSource.isCached();
                        onPreviewLoaded(previewSource.getBitmap());
                    } else {
                        Uri uri2 = previewSource.getUri();
                        if (uri2 == null && previewSource.getResource() != null) {
                            uri2 = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + previewSource.getResource());
                        }
                        execute(new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, uri2, true));
                    }
                }
            }
            if (imageSource.getBitmap() != null && imageSource.getSRegion() != null) {
                onImageLoaded(Bitmap.createBitmap(imageSource.getBitmap(), imageSource.getSRegion().left, imageSource.getSRegion().top, imageSource.getSRegion().width(), imageSource.getSRegion().height()), 0, false);
            } else if (imageSource.getBitmap() != null) {
                onImageLoaded(imageSource.getBitmap(), 0, imageSource.isCached());
            } else {
                this.sRegion = imageSource.getSRegion();
                Uri uri3 = imageSource.getUri();
                this.uri = uri3;
                if (uri3 == null && imageSource.getResource() != null) {
                    this.uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + imageSource.getResource());
                }
                if (imageSource.getTile() || this.sRegion != null) {
                    execute(new TilesInitTask(this, getContext(), this.regionDecoderFactory, this.uri));
                } else {
                    execute(new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, this.uri, false));
                }
            }
        } else {
            throw new NullPointerException("imageSource must not be null");
        }
    }

    /* JADX INFO: finally extract failed */
    private void reset(boolean newImage) {
        OnImageEventListener onImageEventListener2;
        debug("reset newImage=" + newImage, new Object[0]);
        this.scale = 0.0f;
        this.scaleStart = 0.0f;
        this.vTranslate = null;
        this.vTranslateStart = null;
        this.vTranslateBefore = null;
        this.pendingScale = Float.valueOf(0.0f);
        this.sPendingCenter = null;
        this.sRequestedCenter = null;
        this.isZooming = false;
        this.isPanning = false;
        this.isQuickScaling = false;
        this.maxTouchCount = 0;
        this.fullImageSampleSize = 0;
        this.vCenterStart = null;
        this.vDistStart = 0.0f;
        this.quickScaleLastDistance = 0.0f;
        this.quickScaleMoved = false;
        this.quickScaleSCenter = null;
        this.quickScaleVLastPoint = null;
        this.quickScaleVStart = null;
        this.anim = null;
        this.satTemp = null;
        this.matrix = null;
        this.sRect = null;
        if (newImage) {
            this.uri = null;
            this.decoderLock.writeLock().lock();
            try {
                ImageRegionDecoder imageRegionDecoder = this.decoder;
                if (imageRegionDecoder != null) {
                    imageRegionDecoder.recycle();
                    this.decoder = null;
                }
                this.decoderLock.writeLock().unlock();
                Bitmap bitmap2 = this.bitmap;
                if (bitmap2 != null && !this.bitmapIsCached) {
                    bitmap2.recycle();
                }
                if (!(this.bitmap == null || !this.bitmapIsCached || (onImageEventListener2 = this.onImageEventListener) == null)) {
                    onImageEventListener2.onPreviewReleased();
                }
                this.sWidth = 0;
                this.sHeight = 0;
                this.sOrientation = 0;
                this.sRegion = null;
                this.pRegion = null;
                this.readySent = false;
                this.imageLoadedSent = false;
                this.bitmap = null;
                this.bitmapIsPreview = false;
                this.bitmapIsCached = false;
            } catch (Throwable th) {
                this.decoderLock.writeLock().unlock();
                throw th;
            }
        }
        Map<Integer, List<Tile>> map = this.tileMap;
        if (map != null) {
            for (Map.Entry<Integer, List<Tile>> tileMapEntry : map.entrySet()) {
                for (Tile tile : tileMapEntry.getValue()) {
                    tile.visible = false;
                    if (tile.bitmap != null) {
                        tile.bitmap.recycle();
                        tile.bitmap = null;
                    }
                }
            }
            this.tileMap = null;
        }
        setGestureDetector(getContext());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setGestureDetector(final Context context) {
        this.detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            /* class com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.AnonymousClass2 */

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (!SubsamplingScaleImageView.this.panEnabled || !SubsamplingScaleImageView.this.readySent || SubsamplingScaleImageView.this.vTranslate == null || e1 == null || e2 == null || ((Math.abs(e1.getX() - e2.getX()) <= 50.0f && Math.abs(e1.getY() - e2.getY()) <= 50.0f) || ((Math.abs(velocityX) <= 500.0f && Math.abs(velocityY) <= 500.0f) || SubsamplingScaleImageView.this.isZooming))) {
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
                PointF vTranslateEnd = new PointF(SubsamplingScaleImageView.this.vTranslate.x + (velocityX * 0.25f), SubsamplingScaleImageView.this.vTranslate.y + (0.25f * velocityY));
                new AnimationBuilder(new PointF((((float) (SubsamplingScaleImageView.this.getWidth() / 2)) - vTranslateEnd.x) / SubsamplingScaleImageView.this.scale, (((float) (SubsamplingScaleImageView.this.getHeight() / 2)) - vTranslateEnd.y) / SubsamplingScaleImageView.this.scale)).withEasing(1).withPanLimited(false).withOrigin(3).start();
                return true;
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                SubsamplingScaleImageView.this.performClick();
                return true;
            }

            public boolean onDoubleTap(MotionEvent e) {
                if (!SubsamplingScaleImageView.this.zoomEnabled || !SubsamplingScaleImageView.this.readySent || SubsamplingScaleImageView.this.vTranslate == null) {
                    return super.onDoubleTapEvent(e);
                }
                SubsamplingScaleImageView.this.setGestureDetector(context);
                if (SubsamplingScaleImageView.this.quickScaleEnabled) {
                    SubsamplingScaleImageView.this.vCenterStart = new PointF(e.getX(), e.getY());
                    SubsamplingScaleImageView.this.vTranslateStart = new PointF(SubsamplingScaleImageView.this.vTranslate.x, SubsamplingScaleImageView.this.vTranslate.y);
                    SubsamplingScaleImageView subsamplingScaleImageView = SubsamplingScaleImageView.this;
                    subsamplingScaleImageView.scaleStart = subsamplingScaleImageView.scale;
                    SubsamplingScaleImageView.this.isQuickScaling = true;
                    SubsamplingScaleImageView.this.isZooming = true;
                    SubsamplingScaleImageView.this.quickScaleLastDistance = -1.0f;
                    SubsamplingScaleImageView subsamplingScaleImageView2 = SubsamplingScaleImageView.this;
                    subsamplingScaleImageView2.quickScaleSCenter = subsamplingScaleImageView2.viewToSourceCoord(subsamplingScaleImageView2.vCenterStart);
                    SubsamplingScaleImageView.this.quickScaleVStart = new PointF(e.getX(), e.getY());
                    SubsamplingScaleImageView.this.quickScaleVLastPoint = new PointF(SubsamplingScaleImageView.this.quickScaleSCenter.x, SubsamplingScaleImageView.this.quickScaleSCenter.y);
                    SubsamplingScaleImageView.this.quickScaleMoved = false;
                    return false;
                }
                SubsamplingScaleImageView subsamplingScaleImageView3 = SubsamplingScaleImageView.this;
                subsamplingScaleImageView3.doubleTapZoom(subsamplingScaleImageView3.viewToSourceCoord(new PointF(e.getX(), e.getY())), new PointF(e.getX(), e.getY()));
                return true;
            }
        });
        this.singleDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            /* class com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.AnonymousClass3 */

            public boolean onSingleTapConfirmed(MotionEvent e) {
                SubsamplingScaleImageView.this.performClick();
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        debug("onSizeChanged %dx%d -> %dx%d", Integer.valueOf(oldw), Integer.valueOf(oldh), Integer.valueOf(w), Integer.valueOf(h));
        PointF sCenter = getCenter();
        if (this.readySent && sCenter != null) {
            this.anim = null;
            this.pendingScale = Float.valueOf(this.scale);
            this.sPendingCenter = sCenter;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        boolean resizeHeight = true;
        boolean resizeWidth = widthSpecMode != 1073741824;
        if (heightSpecMode == 1073741824) {
            resizeHeight = false;
        }
        int width = parentWidth;
        int height = parentHeight;
        if (this.sWidth > 0 && this.sHeight > 0) {
            if (resizeWidth && resizeHeight) {
                width = sWidth();
                height = sHeight();
            } else if (resizeHeight) {
                double sHeight2 = (double) sHeight();
                double sWidth2 = (double) sWidth();
                Double.isNaN(sHeight2);
                Double.isNaN(sWidth2);
                double d = sHeight2 / sWidth2;
                double d2 = (double) width;
                Double.isNaN(d2);
                height = (int) (d * d2);
            } else if (resizeWidth) {
                double sWidth3 = (double) sWidth();
                double sHeight3 = (double) sHeight();
                Double.isNaN(sWidth3);
                Double.isNaN(sHeight3);
                double d3 = sWidth3 / sHeight3;
                double d4 = (double) height;
                Double.isNaN(d4);
                width = (int) (d3 * d4);
            }
        }
        setMeasuredDimension(Math.max(width, getSuggestedMinimumWidth()), Math.max(height, getSuggestedMinimumHeight()));
    }

    public boolean onTouchEvent(MotionEvent event) {
        GestureDetector gestureDetector;
        Anim anim2 = this.anim;
        if (anim2 == null || anim2.interruptible) {
            Anim anim3 = this.anim;
            if (!(anim3 == null || anim3.listener == null)) {
                try {
                    this.anim.listener.onInterruptedByUser();
                } catch (Exception e) {
                    Log.w(TAG, "Error thrown by animation listener", e);
                }
            }
            this.anim = null;
            if (this.vTranslate == null) {
                GestureDetector gestureDetector2 = this.singleDetector;
                if (gestureDetector2 != null) {
                    gestureDetector2.onTouchEvent(event);
                }
                return true;
            } else if (this.isQuickScaling || ((gestureDetector = this.detector) != null && !gestureDetector.onTouchEvent(event))) {
                if (this.vTranslateStart == null) {
                    this.vTranslateStart = new PointF(0.0f, 0.0f);
                }
                if (this.vTranslateBefore == null) {
                    this.vTranslateBefore = new PointF(0.0f, 0.0f);
                }
                if (this.vCenterStart == null) {
                    this.vCenterStart = new PointF(0.0f, 0.0f);
                }
                float scaleBefore = this.scale;
                this.vTranslateBefore.set(this.vTranslate);
                boolean handled = onTouchEventInternal(event);
                sendStateChanged(scaleBefore, this.vTranslateBefore, 2);
                if (handled || super.onTouchEvent(event)) {
                    return true;
                }
                return false;
            } else {
                this.isZooming = false;
                this.isPanning = false;
                this.maxTouchCount = 0;
                return true;
            }
        } else {
            requestDisallowInterceptTouchEvent(true);
            return true;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0120, code lost:
        if ((r19.scale * ((float) sWidth())) >= ((float) getWidth())) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x02a1, code lost:
        if ((r19.scale * ((float) sWidth())) >= ((float) getWidth())) goto L_0x02a3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0417  */
    /* JADX WARNING: Removed duplicated region for block: B:170:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean onTouchEventInternal(MotionEvent r20) {
        /*
        // Method dump skipped, instructions count: 1354
        */
        throw new UnsupportedOperationException("Method not decompiled: com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.onTouchEventInternal(android.view.MotionEvent):boolean");
    }

    private void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void doubleTapZoom(PointF sCenter, PointF vFocus) {
        if (!this.panEnabled) {
            PointF pointF = this.sRequestedCenter;
            if (pointF != null) {
                sCenter.x = pointF.x;
                sCenter.y = this.sRequestedCenter.y;
            } else {
                sCenter.x = (float) (sWidth() / 2);
                sCenter.y = (float) (sHeight() / 2);
            }
        }
        float doubleTapZoomScale2 = Math.min(this.maxScale, this.doubleTapZoomScale);
        float f = this.scale;
        double d = (double) doubleTapZoomScale2;
        Double.isNaN(d);
        boolean zoomIn = ((double) f) <= d * 0.9d || f == this.minScale;
        float targetScale = zoomIn ? doubleTapZoomScale2 : minScale();
        int i = this.doubleTapZoomStyle;
        if (i == 3) {
            setScaleAndCenter(targetScale, sCenter);
        } else if (i == 2 || !zoomIn || !this.panEnabled) {
            new AnimationBuilder(targetScale, sCenter).withInterruptible(false).withDuration((long) this.doubleTapZoomDuration).withOrigin(4).start();
        } else if (i == 1) {
            new AnimationBuilder(targetScale, sCenter, vFocus).withInterruptible(false).withDuration((long) this.doubleTapZoomDuration).withOrigin(4).start();
        }
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        super.onDraw(canvas);
        createPaints();
        if (!(this.sWidth == 0 || this.sHeight == 0 || getWidth() == 0 || getHeight() == 0)) {
            if (this.tileMap == null && this.decoder != null) {
                initialiseBaseLayer(getMaxBitmapDimensions(canvas));
            }
            if (checkReady()) {
                preDraw();
                Anim anim2 = this.anim;
                if (!(anim2 == null || anim2.vFocusStart == null)) {
                    float scaleBefore = this.scale;
                    if (this.vTranslateBefore == null) {
                        this.vTranslateBefore = new PointF(0.0f, 0.0f);
                    }
                    this.vTranslateBefore.set(this.vTranslate);
                    long scaleElapsed = System.currentTimeMillis() - this.anim.time;
                    boolean finished = scaleElapsed > this.anim.duration;
                    long scaleElapsed2 = Math.min(scaleElapsed, this.anim.duration);
                    this.scale = ease(this.anim.easing, scaleElapsed2, this.anim.scaleStart, this.anim.scaleEnd - this.anim.scaleStart, this.anim.duration);
                    float vFocusNowX = ease(this.anim.easing, scaleElapsed2, this.anim.vFocusStart.x, this.anim.vFocusEnd.x - this.anim.vFocusStart.x, this.anim.duration);
                    float vFocusNowY = ease(this.anim.easing, scaleElapsed2, this.anim.vFocusStart.y, this.anim.vFocusEnd.y - this.anim.vFocusStart.y, this.anim.duration);
                    this.vTranslate.x -= sourceToViewX(this.anim.sCenterEnd.x) - vFocusNowX;
                    this.vTranslate.y -= sourceToViewY(this.anim.sCenterEnd.y) - vFocusNowY;
                    fitToBounds(finished || this.anim.scaleStart == this.anim.scaleEnd);
                    sendStateChanged(scaleBefore, this.vTranslateBefore, this.anim.origin);
                    refreshRequiredTiles(finished);
                    if (finished) {
                        if (this.anim.listener != null) {
                            try {
                                this.anim.listener.onComplete();
                            } catch (Exception e) {
                                Log.w(TAG, "Error thrown by animation listener", e);
                            }
                        }
                        this.anim = null;
                    }
                    invalidate();
                }
                if (this.tileMap == null || !isBaseLayerReady()) {
                    i2 = 35;
                    i = 15;
                    Bitmap bitmap2 = this.bitmap;
                    if (bitmap2 != null) {
                        float xScale = this.scale;
                        float yScale = this.scale;
                        if (this.bitmapIsPreview) {
                            xScale = this.scale * (((float) this.sWidth) / ((float) bitmap2.getWidth()));
                            yScale = this.scale * (((float) this.sHeight) / ((float) this.bitmap.getHeight()));
                        }
                        if (this.matrix == null) {
                            this.matrix = new Matrix();
                        }
                        this.matrix.reset();
                        this.matrix.postScale(xScale, yScale);
                        this.matrix.postRotate((float) getRequiredRotation());
                        this.matrix.postTranslate(this.vTranslate.x, this.vTranslate.y);
                        if (getRequiredRotation() == 180) {
                            Matrix matrix2 = this.matrix;
                            float f = this.scale;
                            matrix2.postTranslate(((float) this.sWidth) * f, f * ((float) this.sHeight));
                        } else if (getRequiredRotation() == 90) {
                            this.matrix.postTranslate(this.scale * ((float) this.sHeight), 0.0f);
                        } else if (getRequiredRotation() == 270) {
                            this.matrix.postTranslate(0.0f, this.scale * ((float) this.sWidth));
                        }
                        if (this.tileBgPaint != null) {
                            if (this.sRect == null) {
                                this.sRect = new RectF();
                            }
                            this.sRect.set(0.0f, 0.0f, (float) (this.bitmapIsPreview ? this.bitmap.getWidth() : this.sWidth), (float) (this.bitmapIsPreview ? this.bitmap.getHeight() : this.sHeight));
                            this.matrix.mapRect(this.sRect);
                            canvas.drawRect(this.sRect, this.tileBgPaint);
                        }
                        canvas.drawBitmap(this.bitmap, this.matrix, this.bitmapPaint);
                    }
                } else {
                    int sampleSize = Math.min(this.fullImageSampleSize, calculateInSampleSize(this.scale));
                    boolean hasMissingTiles = false;
                    for (Map.Entry<Integer, List<Tile>> tileMapEntry : this.tileMap.entrySet()) {
                        if (tileMapEntry.getKey().intValue() == sampleSize) {
                            for (Tile tile : tileMapEntry.getValue()) {
                                if (tile.visible && (tile.loading || tile.bitmap == null)) {
                                    hasMissingTiles = true;
                                }
                            }
                        }
                    }
                    for (Map.Entry<Integer, List<Tile>> tileMapEntry2 : this.tileMap.entrySet()) {
                        if (tileMapEntry2.getKey().intValue() == sampleSize || hasMissingTiles) {
                            for (Tile tile2 : tileMapEntry2.getValue()) {
                                sourceToViewRect(tile2.sRect, tile2.vRect);
                                if (!tile2.loading && tile2.bitmap != null) {
                                    if (this.tileBgPaint != null) {
                                        canvas.drawRect(tile2.vRect, this.tileBgPaint);
                                    }
                                    if (this.matrix == null) {
                                        this.matrix = new Matrix();
                                    }
                                    this.matrix.reset();
                                    setMatrixArray(this.srcArray, 0.0f, 0.0f, (float) tile2.bitmap.getWidth(), 0.0f, (float) tile2.bitmap.getWidth(), (float) tile2.bitmap.getHeight(), 0.0f, (float) tile2.bitmap.getHeight());
                                    if (getRequiredRotation() == 0) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.left, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.bottom);
                                    } else if (getRequiredRotation() == 90) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.right, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.top);
                                    } else if (getRequiredRotation() == 180) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.right, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.top);
                                    } else if (getRequiredRotation() == 270) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.left, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.bottom);
                                    }
                                    this.matrix.setPolyToPoly(this.srcArray, 0, this.dstArray, 0, 4);
                                    canvas.drawBitmap(tile2.bitmap, this.matrix, this.bitmapPaint);
                                    if (this.debug) {
                                        canvas.drawRect(tile2.vRect, this.debugLinePaint);
                                    }
                                } else if (tile2.loading && this.debug) {
                                    canvas.drawText("LOADING", (float) (tile2.vRect.left + px(5)), (float) (tile2.vRect.top + px(35)), this.debugTextPaint);
                                }
                                if (tile2.visible && this.debug) {
                                    canvas.drawText("ISS " + tile2.sampleSize + " RECT " + tile2.sRect.top + "," + tile2.sRect.left + "," + tile2.sRect.bottom + "," + tile2.sRect.right, (float) (tile2.vRect.left + px(5)), (float) (tile2.vRect.top + px(15)), this.debugTextPaint);
                                }
                            }
                        }
                    }
                    i2 = 35;
                    i = 15;
                }
                if (this.debug) {
                    canvas.drawText("Scale: " + String.format(Locale.ENGLISH, "%.2f", Float.valueOf(this.scale)) + " (" + String.format(Locale.ENGLISH, "%.2f", Float.valueOf(minScale())) + " - " + String.format(Locale.ENGLISH, "%.2f", Float.valueOf(this.maxScale)) + ")", (float) px(5), (float) px(i), this.debugTextPaint);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Translate: ");
                    sb.append(String.format(Locale.ENGLISH, "%.2f", Float.valueOf(this.vTranslate.x)));
                    sb.append(":");
                    sb.append(String.format(Locale.ENGLISH, "%.2f", Float.valueOf(this.vTranslate.y)));
                    canvas.drawText(sb.toString(), (float) px(5), (float) px(30), this.debugTextPaint);
                    PointF center = getCenter();
                    canvas.drawText("Source center: " + String.format(Locale.ENGLISH, "%.2f", Float.valueOf(center.x)) + ":" + String.format(Locale.ENGLISH, "%.2f", Float.valueOf(center.y)), (float) px(5), (float) px(45), this.debugTextPaint);
                    Anim anim3 = this.anim;
                    if (anim3 != null) {
                        PointF vCenterStart2 = sourceToViewCoord(anim3.sCenterStart);
                        PointF vCenterEndRequested = sourceToViewCoord(this.anim.sCenterEndRequested);
                        PointF vCenterEnd = sourceToViewCoord(this.anim.sCenterEnd);
                        canvas.drawCircle(vCenterStart2.x, vCenterStart2.y, (float) px(10), this.debugLinePaint);
                        this.debugLinePaint.setColor(SupportMenu.CATEGORY_MASK);
                        canvas.drawCircle(vCenterEndRequested.x, vCenterEndRequested.y, (float) px(20), this.debugLinePaint);
                        this.debugLinePaint.setColor(-16776961);
                        canvas.drawCircle(vCenterEnd.x, vCenterEnd.y, (float) px(25), this.debugLinePaint);
                        this.debugLinePaint.setColor(-16711681);
                        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) px(30), this.debugLinePaint);
                    }
                    if (this.vCenterStart != null) {
                        this.debugLinePaint.setColor(SupportMenu.CATEGORY_MASK);
                        canvas.drawCircle(this.vCenterStart.x, this.vCenterStart.y, (float) px(20), this.debugLinePaint);
                    }
                    if (this.quickScaleSCenter != null) {
                        this.debugLinePaint.setColor(-16776961);
                        canvas.drawCircle(sourceToViewX(this.quickScaleSCenter.x), sourceToViewY(this.quickScaleSCenter.y), (float) px(i2), this.debugLinePaint);
                    }
                    if (this.quickScaleVStart != null && this.isQuickScaling) {
                        this.debugLinePaint.setColor(-16711681);
                        canvas.drawCircle(this.quickScaleVStart.x, this.quickScaleVStart.y, (float) px(30), this.debugLinePaint);
                    }
                    this.debugLinePaint.setColor(-65281);
                }
            }
        }
    }

    private void setMatrixArray(float[] array, float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7) {
        array[0] = f0;
        array[1] = f1;
        array[2] = f2;
        array[3] = f3;
        array[4] = f4;
        array[5] = f5;
        array[6] = f6;
        array[7] = f7;
    }

    private boolean isBaseLayerReady() {
        if (!(this.bitmap == null || this.bitmapIsPreview)) {
            return true;
        }
        Map<Integer, List<Tile>> map = this.tileMap;
        if (map == null) {
            return false;
        }
        boolean baseLayerReady = true;
        for (Map.Entry<Integer, List<Tile>> tileMapEntry : map.entrySet()) {
            if (tileMapEntry.getKey().intValue() == this.fullImageSampleSize) {
                for (Tile tile : tileMapEntry.getValue()) {
                    if (tile.loading || tile.bitmap == null) {
                        baseLayerReady = false;
                    }
                }
            }
        }
        return baseLayerReady;
    }

    private boolean checkReady() {
        boolean ready = getWidth() > 0 && getHeight() > 0 && this.sWidth > 0 && this.sHeight > 0 && (this.bitmap != null || isBaseLayerReady());
        if (!this.readySent && ready) {
            preDraw();
            this.readySent = true;
            onReady();
            OnImageEventListener onImageEventListener2 = this.onImageEventListener;
            if (onImageEventListener2 != null) {
                onImageEventListener2.onReady();
            }
        }
        return ready;
    }

    private boolean checkImageLoaded() {
        boolean imageLoaded = isBaseLayerReady();
        if (!this.imageLoadedSent && imageLoaded) {
            preDraw();
            this.imageLoadedSent = true;
            onImageLoaded();
            OnImageEventListener onImageEventListener2 = this.onImageEventListener;
            if (onImageEventListener2 != null) {
                onImageEventListener2.onImageLoaded();
            }
        }
        return imageLoaded;
    }

    private void createPaints() {
        if (this.bitmapPaint == null) {
            Paint paint = new Paint();
            this.bitmapPaint = paint;
            paint.setAntiAlias(true);
            this.bitmapPaint.setFilterBitmap(true);
            this.bitmapPaint.setDither(true);
        }
        if ((this.debugTextPaint == null || this.debugLinePaint == null) && this.debug) {
            Paint paint2 = new Paint();
            this.debugTextPaint = paint2;
            paint2.setTextSize((float) px(12));
            this.debugTextPaint.setColor(-65281);
            this.debugTextPaint.setStyle(Paint.Style.FILL);
            Paint paint3 = new Paint();
            this.debugLinePaint = paint3;
            paint3.setColor(-65281);
            this.debugLinePaint.setStyle(Paint.Style.STROKE);
            this.debugLinePaint.setStrokeWidth((float) px(1));
        }
    }

    private synchronized void initialiseBaseLayer(Point maxTileDimensions) {
        debug("initialiseBaseLayer maxTileDimensions=%dx%d", Integer.valueOf(maxTileDimensions.x), Integer.valueOf(maxTileDimensions.y));
        ScaleAndTranslate scaleAndTranslate = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f));
        this.satTemp = scaleAndTranslate;
        fitToBounds(true, scaleAndTranslate);
        int calculateInSampleSize = calculateInSampleSize(this.satTemp.scale);
        this.fullImageSampleSize = calculateInSampleSize;
        if (calculateInSampleSize > 1) {
            this.fullImageSampleSize = calculateInSampleSize / 2;
        }
        if (this.fullImageSampleSize != 1 || this.sRegion != null || sWidth() >= maxTileDimensions.x || sHeight() >= maxTileDimensions.y) {
            initialiseTileMap(maxTileDimensions);
            for (Tile baseTile : this.tileMap.get(Integer.valueOf(this.fullImageSampleSize))) {
                execute(new TileLoadTask(this, this.decoder, baseTile));
            }
            refreshRequiredTiles(true);
        } else {
            this.decoder.recycle();
            this.decoder = null;
            execute(new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, this.uri, false));
        }
    }

    private void refreshRequiredTiles(boolean load) {
        if (!(this.decoder == null || this.tileMap == null)) {
            int sampleSize = Math.min(this.fullImageSampleSize, calculateInSampleSize(this.scale));
            for (Map.Entry<Integer, List<Tile>> tileMapEntry : this.tileMap.entrySet()) {
                for (Tile tile : tileMapEntry.getValue()) {
                    if (tile.sampleSize < sampleSize || (tile.sampleSize > sampleSize && tile.sampleSize != this.fullImageSampleSize)) {
                        tile.visible = false;
                        if (tile.bitmap != null) {
                            tile.bitmap.recycle();
                            tile.bitmap = null;
                        }
                    }
                    if (tile.sampleSize == sampleSize) {
                        if (tileVisible(tile)) {
                            tile.visible = true;
                            if (!tile.loading && tile.bitmap == null && load) {
                                execute(new TileLoadTask(this, this.decoder, tile));
                            }
                        } else if (tile.sampleSize != this.fullImageSampleSize) {
                            tile.visible = false;
                            if (tile.bitmap != null) {
                                tile.bitmap.recycle();
                                tile.bitmap = null;
                            }
                        }
                    } else if (tile.sampleSize == this.fullImageSampleSize) {
                        tile.visible = true;
                    }
                }
            }
        }
    }

    private boolean tileVisible(Tile tile) {
        return viewToSourceX(0.0f) <= ((float) tile.sRect.right) && ((float) tile.sRect.left) <= viewToSourceX((float) getWidth()) && viewToSourceY(0.0f) <= ((float) tile.sRect.bottom) && ((float) tile.sRect.top) <= viewToSourceY((float) getHeight());
    }

    private void preDraw() {
        Float f;
        if (getWidth() != 0 && getHeight() != 0 && this.sWidth > 0 && this.sHeight > 0) {
            if (!(this.sPendingCenter == null || (f = this.pendingScale) == null)) {
                this.scale = f.floatValue();
                if (this.vTranslate == null) {
                    this.vTranslate = new PointF();
                }
                this.vTranslate.x = ((float) (getWidth() / 2)) - (this.scale * this.sPendingCenter.x);
                this.vTranslate.y = ((float) (getHeight() / 2)) - (this.scale * this.sPendingCenter.y);
                this.sPendingCenter = null;
                this.pendingScale = null;
                fitToBounds(true);
                refreshRequiredTiles(true);
            }
            fitToBounds(false);
        }
    }

    private int calculateInSampleSize(float scale2) {
        if (this.minimumTileDpi > 0) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            scale2 *= ((float) this.minimumTileDpi) / ((metrics.xdpi + metrics.ydpi) / 2.0f);
        }
        int reqWidth = (int) (((float) sWidth()) * scale2);
        int reqHeight = (int) (((float) sHeight()) * scale2);
        int inSampleSize = 1;
        if (reqWidth == 0 || reqHeight == 0) {
            return 32;
        }
        if (sHeight() > reqHeight || sWidth() > reqWidth) {
            int heightRatio = Math.round(((float) sHeight()) / ((float) reqHeight));
            int widthRatio = Math.round(((float) sWidth()) / ((float) reqWidth));
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        int power = 1;
        while (power * 2 < inSampleSize) {
            power *= 2;
        }
        return power;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void fitToBounds(boolean center, ScaleAndTranslate sat) {
        float maxTx;
        float maxTy;
        if (this.panLimit == 2 && isReady()) {
            center = false;
        }
        PointF vTranslate2 = sat.vTranslate;
        float scale2 = limitedScale(sat.scale);
        float scaleWidth = ((float) sWidth()) * scale2;
        float scaleHeight = ((float) sHeight()) * scale2;
        if (this.panLimit == 3 && isReady()) {
            vTranslate2.x = Math.max(vTranslate2.x, ((float) (getWidth() / 2)) - scaleWidth);
            vTranslate2.y = Math.max(vTranslate2.y, ((float) (getHeight() / 2)) - scaleHeight);
        } else if (center) {
            vTranslate2.x = Math.max(vTranslate2.x, ((float) getWidth()) - scaleWidth);
            vTranslate2.y = Math.max(vTranslate2.y, ((float) getHeight()) - scaleHeight);
        } else {
            vTranslate2.x = Math.max(vTranslate2.x, -scaleWidth);
            vTranslate2.y = Math.max(vTranslate2.y, -scaleHeight);
        }
        float yPaddingRatio = 0.5f;
        float xPaddingRatio = (getPaddingLeft() > 0 || getPaddingRight() > 0) ? ((float) getPaddingLeft()) / ((float) (getPaddingLeft() + getPaddingRight())) : 0.5f;
        if (getPaddingTop() > 0 || getPaddingBottom() > 0) {
            yPaddingRatio = ((float) getPaddingTop()) / ((float) (getPaddingTop() + getPaddingBottom()));
        }
        if (this.panLimit == 3 && isReady()) {
            maxTx = (float) Math.max(0, getWidth() / 2);
            maxTy = (float) Math.max(0, getHeight() / 2);
        } else if (center) {
            maxTx = Math.max(0.0f, (((float) getWidth()) - scaleWidth) * xPaddingRatio);
            maxTy = Math.max(0.0f, (((float) getHeight()) - scaleHeight) * yPaddingRatio);
        } else {
            maxTx = (float) Math.max(0, getWidth());
            maxTy = (float) Math.max(0, getHeight());
        }
        vTranslate2.x = Math.min(vTranslate2.x, maxTx);
        vTranslate2.y = Math.min(vTranslate2.y, maxTy);
        sat.scale = scale2;
    }

    private void fitToBounds(boolean center) {
        boolean init = false;
        if (this.vTranslate == null) {
            init = true;
            this.vTranslate = new PointF(0.0f, 0.0f);
        }
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f));
        }
        this.satTemp.scale = this.scale;
        this.satTemp.vTranslate.set(this.vTranslate);
        fitToBounds(center, this.satTemp);
        this.scale = this.satTemp.scale;
        this.vTranslate.set(this.satTemp.vTranslate);
        if (init && this.minimumScaleType != 4) {
            this.vTranslate.set(vTranslateForSCenter((float) (sWidth() / 2), (float) (sHeight() / 2), this.scale));
        }
    }





    /*private void initialiseTileMap(Point maxTileDimensions) {
        Point point = maxTileDimensions;
        int i = 1;
        debug("initialiseTileMap maxTileDimensions=%dx%d", Integer.valueOf(point.x), Integer.valueOf(point.y));
        this.tileMap = new LinkedHashMap();
        int sampleSize = this.fullImageSampleSize;
        int xTiles = 1;
        int yTiles = 1;
        while (true) {
            int sTileWidth = sWidth() / xTiles;
            int sTileHeight = sHeight() / yTiles;
            int subTileWidth = sTileWidth / sampleSize;
            int subTileHeight = sTileHeight / sampleSize;
            while (true) {
                if (subTileWidth + xTiles + i <= point.x) {
                    double width = (double) getWidth();
                    Double.isNaN(width);
                    if (((double) subTileWidth) <= width * 1.25d || sampleSize >= this.fullImageSampleSize) {
                    }
                }
                xTiles++;
                sTileWidth = sWidth() / xTiles;
                subTileWidth = sTileWidth / sampleSize;
                point = maxTileDimensions;
                i = 1;
            }
            while (true) {
                if (subTileHeight + yTiles + i <= point.y) {
                    double height = (double) getHeight();
                    Double.isNaN(height);
                    if (((double) subTileHeight) <= height * 1.25d || sampleSize >= this.fullImageSampleSize) {
                        List<Tile> tileGrid = new ArrayList<>(xTiles * yTiles);
                        int x = 0;
                    }
                }
                yTiles++;
                sTileHeight = sHeight() / yTiles;
                subTileHeight = sTileHeight / sampleSize;
                point = maxTileDimensions;
                sTileWidth = sTileWidth;
                i = 1;
            }
            List<Tile> tileGrid2 = new ArrayList<>(xTiles * yTiles);
            int x2 = 0;
            while (x2 < xTiles) {
                int y = 0;
                while (y < yTiles) {
                    Tile tile = new Tile();
                    tile.sampleSize = sampleSize;
                    tile.visible = sampleSize == this.fullImageSampleSize;
                    tile.sRect = new Rect(x2 * sTileWidth, y * sTileHeight, x2 == xTiles + -1 ? sWidth() : (x2 + 1) * sTileWidth, y == yTiles + -1 ? sHeight() : (y + 1) * sTileHeight);
                    tile.vRect = new Rect(0, 0, 0, 0);
                    tile.fileSRect = new Rect(tile.sRect);
                    tileGrid2.add(tile);
                    y++;
                    sTileWidth = sTileWidth;
                }
                x2++;
            }
            this.tileMap.put(Integer.valueOf(sampleSize), tileGrid2);
            if (sampleSize != 1) {
                sampleSize /= 2;
                point = maxTileDimensions;
                i = 1;
            } else {
                return;
            }
        }
    }
*/

    private void initialiseTileMap(Point maxTileDimensions) {
        debug("initialiseTileMap maxTileDimensions=%dx%d", maxTileDimensions.x, maxTileDimensions.y);
        this.tileMap = new LinkedHashMap<>();
        int sampleSize = fullImageSampleSize;
        int xTiles = 1;
        int yTiles = 1;
        while (true) {
            int sTileWidth = sWidth()/xTiles;
            int sTileHeight = sHeight()/yTiles;
            int subTileWidth = sTileWidth/sampleSize;
            int subTileHeight = sTileHeight/sampleSize;
            while (subTileWidth + xTiles + 1 > maxTileDimensions.x || (subTileWidth > getWidth() * 1.25 && sampleSize < fullImageSampleSize)) {
                xTiles += 1;
                sTileWidth = sWidth()/xTiles;
                subTileWidth = sTileWidth/sampleSize;
            }
            while (subTileHeight + yTiles + 1 > maxTileDimensions.y || (subTileHeight > getHeight() * 1.25 && sampleSize < fullImageSampleSize)) {
                yTiles += 1;
                sTileHeight = sHeight()/yTiles;
                subTileHeight = sTileHeight/sampleSize;
            }
            List<Tile> tileGrid = new ArrayList<>(xTiles * yTiles);
            for (int x = 0; x < xTiles; x++) {
                for (int y = 0; y < yTiles; y++) {
                    Tile tile = new Tile();
                    tile.sampleSize = sampleSize;
                    tile.visible = sampleSize == fullImageSampleSize;
                    tile.sRect = new Rect(
                            x * sTileWidth,
                            y * sTileHeight,
                            x == xTiles - 1 ? sWidth() : (x + 1) * sTileWidth,
                            y == yTiles - 1 ? sHeight() : (y + 1) * sTileHeight
                    );
                    tile.vRect = new Rect(0, 0, 0, 0);
                    tile.fileSRect = new Rect(tile.sRect);
                    tileGrid.add(tile);
                }
            }
            tileMap.put(sampleSize, tileGrid);
            if (sampleSize == 1) {
                break;
            } else {
                sampleSize /= 2;
            }
        }
    }

    /* access modifiers changed from: private */
    public static class TilesInitTask extends AsyncTask<Void, Void, int[]> {
        private final WeakReference<Context> contextRef;
        private ImageRegionDecoder decoder;
        private final WeakReference<DecoderFactory<? extends ImageRegionDecoder>> decoderFactoryRef;
        private Exception exception;
        private final Uri source;
        private final WeakReference<SubsamplingScaleImageView> viewRef;

        TilesInitTask(SubsamplingScaleImageView view, Context context, DecoderFactory<? extends ImageRegionDecoder> decoderFactory, Uri source2) {
            this.viewRef = new WeakReference<>(view);
            this.contextRef = new WeakReference<>(context);
            this.decoderFactoryRef = new WeakReference<>(decoderFactory);
            this.source = source2;
        }

        /* access modifiers changed from: protected */
        public int[] doInBackground(Void... params) {
            try {
                String sourceUri = this.source.toString();
                Context context = this.contextRef.get();
                DecoderFactory<? extends ImageRegionDecoder> decoderFactory = this.decoderFactoryRef.get();
                SubsamplingScaleImageView view = this.viewRef.get();
                if (context == null || decoderFactory == null || view == null) {
                    return null;
                }
                view.debug("TilesInitTask.doInBackground", new Object[0]);
                ImageRegionDecoder imageRegionDecoder = (ImageRegionDecoder) decoderFactory.make();
                this.decoder = imageRegionDecoder;
                Point dimensions = imageRegionDecoder.init(context, this.source);
                int sWidth = dimensions.x;
                int sHeight = dimensions.y;
                int exifOrientation = view.getExifOrientation(context, sourceUri);
                if (view.sRegion != null) {
                    view.sRegion.left = Math.max(0, view.sRegion.left);
                    view.sRegion.top = Math.max(0, view.sRegion.top);
                    view.sRegion.right = Math.min(sWidth, view.sRegion.right);
                    view.sRegion.bottom = Math.min(sHeight, view.sRegion.bottom);
                    sWidth = view.sRegion.width();
                    sHeight = view.sRegion.height();
                }
                return new int[]{sWidth, sHeight, exifOrientation};
            } catch (Exception e) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to initialise bitmap decoder", e);
                this.exception = e;
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(int[] xyo) {
            SubsamplingScaleImageView view = this.viewRef.get();
            if (view != null) {
                ImageRegionDecoder imageRegionDecoder = this.decoder;
                if (imageRegionDecoder != null && xyo != null && xyo.length == 3) {
                    view.onTilesInited(imageRegionDecoder, xyo[0], xyo[1], xyo[2]);
                } else if (this.exception != null && view.onImageEventListener != null) {
                    view.onImageEventListener.onImageLoadError(this.exception);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private synchronized void onTilesInited(ImageRegionDecoder decoder2, int sWidth2, int sHeight2, int sOrientation2) {
        int i;
        int i2;
        int i3;
        debug("onTilesInited sWidth=%d, sHeight=%d, sOrientation=%d", Integer.valueOf(sWidth2), Integer.valueOf(sHeight2), Integer.valueOf(this.orientation));
        int i4 = this.sWidth;
        if (i4 > 0 && (i3 = this.sHeight) > 0 && !(i4 == sWidth2 && i3 == sHeight2)) {
            reset(false);
            Bitmap bitmap2 = this.bitmap;
            if (bitmap2 != null) {
                if (!this.bitmapIsCached) {
                    bitmap2.recycle();
                }
                this.bitmap = null;
                OnImageEventListener onImageEventListener2 = this.onImageEventListener;
                if (onImageEventListener2 != null && this.bitmapIsCached) {
                    onImageEventListener2.onPreviewReleased();
                }
                this.bitmapIsPreview = false;
                this.bitmapIsCached = false;
            }
        }
        this.decoder = decoder2;
        this.sWidth = sWidth2;
        this.sHeight = sHeight2;
        this.sOrientation = sOrientation2;
        checkReady();
        if (!checkImageLoaded() && (i = this.maxTileWidth) > 0 && i != Integer.MAX_VALUE && (i2 = this.maxTileHeight) > 0 && i2 != Integer.MAX_VALUE && getWidth() > 0 && getHeight() > 0) {
            initialiseBaseLayer(new Point(this.maxTileWidth, this.maxTileHeight));
        }
        invalidate();
        requestLayout();
    }

    /* access modifiers changed from: private */
    public static class TileLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<ImageRegionDecoder> decoderRef;
        private Exception exception;
        private final WeakReference<Tile> tileRef;
        private final WeakReference<SubsamplingScaleImageView> viewRef;

        TileLoadTask(SubsamplingScaleImageView view, ImageRegionDecoder decoder, Tile tile) {
            this.viewRef = new WeakReference<>(view);
            this.decoderRef = new WeakReference<>(decoder);
            this.tileRef = new WeakReference<>(tile);
            tile.loading = true;
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(Void... params) {
            try {
                SubsamplingScaleImageView view = this.viewRef.get();
                ImageRegionDecoder decoder = this.decoderRef.get();
                Tile tile = this.tileRef.get();
                if (decoder != null && tile != null && view != null && decoder.isReady() && tile.visible) {
                    view.debug("TileLoadTask.doInBackground, tile.sRect=%s, tile.sampleSize=%d", new Object[]{tile.sRect, Integer.valueOf(tile.sampleSize)});
                    view.decoderLock.readLock().lock();
                    try {
                        if (decoder.isReady()) {
                            view.fileSRect(tile.sRect, tile.fileSRect);
                            if (view.sRegion != null) {
                                tile.fileSRect.offset(view.sRegion.left, view.sRegion.top);
                            }
                            return decoder.decodeRegion(tile.fileSRect, tile.sampleSize);
                        }
                        tile.loading = false;
                        view.decoderLock.readLock().unlock();
                        return null;
                    } finally {
                        view.decoderLock.readLock().unlock();
                    }
                } else if (tile == null) {
                    return null;
                } else {
                    tile.loading = false;
                    return null;
                }
            } catch (Exception e) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to decode tile", e);
                this.exception = e;
                return null;
            } catch (OutOfMemoryError e2) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to decode tile - OutOfMemoryError", e2);
                this.exception = new RuntimeException(e2);
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            Tile tile = this.tileRef.get();
            if (subsamplingScaleImageView != null && tile != null) {
                if (bitmap != null) {
                    tile.bitmap = bitmap;
                    tile.loading = false;
                    subsamplingScaleImageView.onTileLoaded();
                } else if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    subsamplingScaleImageView.onImageEventListener.onTileLoadError(this.exception);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private synchronized void onTileLoaded() {
        Bitmap bitmap2;
        debug("onTileLoaded", new Object[0]);
        checkReady();
        checkImageLoaded();
        if (isBaseLayerReady() && (bitmap2 = this.bitmap) != null) {
            if (!this.bitmapIsCached) {
                bitmap2.recycle();
            }
            this.bitmap = null;
            OnImageEventListener onImageEventListener2 = this.onImageEventListener;
            if (onImageEventListener2 != null && this.bitmapIsCached) {
                onImageEventListener2.onPreviewReleased();
            }
            this.bitmapIsPreview = false;
            this.bitmapIsCached = false;
        }
        invalidate();
    }

    /* access modifiers changed from: private */
    public static class BitmapLoadTask extends AsyncTask<Void, Void, Integer> {
        private Bitmap bitmap;
        private final WeakReference<Context> contextRef;
        private final WeakReference<DecoderFactory<? extends ImageDecoder>> decoderFactoryRef;
        private Exception exception;
        private final boolean preview;
        private final Uri source;
        private final WeakReference<SubsamplingScaleImageView> viewRef;

        BitmapLoadTask(SubsamplingScaleImageView view, Context context, DecoderFactory<? extends ImageDecoder> decoderFactory, Uri source2, boolean preview2) {
            this.viewRef = new WeakReference<>(view);
            this.contextRef = new WeakReference<>(context);
            this.decoderFactoryRef = new WeakReference<>(decoderFactory);
            this.source = source2;
            this.preview = preview2;
        }

        /* access modifiers changed from: protected */
        public Integer doInBackground(Void... params) {
            try {
                String sourceUri = this.source.toString();
                Context context = this.contextRef.get();
                DecoderFactory<? extends ImageDecoder> decoderFactory = this.decoderFactoryRef.get();
                SubsamplingScaleImageView view = this.viewRef.get();
                if (context == null || decoderFactory == null || view == null) {
                    return null;
                }
                view.debug("BitmapLoadTask.doInBackground", new Object[0]);
                this.bitmap = ((ImageDecoder) decoderFactory.make()).decode(context, this.source);
                return Integer.valueOf(view.getExifOrientation(context, sourceUri));
            } catch (Exception e) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to load bitmap", e);
                this.exception = e;
                return null;
            } catch (OutOfMemoryError e2) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to load bitmap - OutOfMemoryError", e2);
                this.exception = new RuntimeException(e2);
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Integer orientation) {
            SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            if (subsamplingScaleImageView != null) {
                Bitmap bitmap2 = this.bitmap;
                if (bitmap2 == null || orientation == null) {
                    if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                        if (this.preview) {
                            subsamplingScaleImageView.onImageEventListener.onPreviewLoadError(this.exception);
                        } else {
                            subsamplingScaleImageView.onImageEventListener.onImageLoadError(this.exception);
                        }
                    }
                } else if (this.preview) {
                    subsamplingScaleImageView.onPreviewLoaded(bitmap2);
                } else {
                    subsamplingScaleImageView.onImageLoaded(bitmap2, orientation.intValue(), false);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private synchronized void onPreviewLoaded(Bitmap previewBitmap) {
        debug("onPreviewLoaded", new Object[0]);
        if (this.bitmap == null) {
            if (!this.imageLoadedSent) {
                Rect rect = this.pRegion;
                if (rect != null) {
                    this.bitmap = Bitmap.createBitmap(previewBitmap, rect.left, this.pRegion.top, this.pRegion.width(), this.pRegion.height());
                } else {
                    this.bitmap = previewBitmap;
                }
                this.bitmapIsPreview = true;
                if (checkReady()) {
                    invalidate();
                    requestLayout();
                }
                return;
            }
        }
        previewBitmap.recycle();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private synchronized void onImageLoaded(Bitmap bitmap2, int sOrientation2, boolean bitmapIsCached2) {
        OnImageEventListener onImageEventListener2;
        debug("onImageLoaded", new Object[0]);
        int i = this.sWidth;
        if (i > 0 && this.sHeight > 0 && !(i == bitmap2.getWidth() && this.sHeight == bitmap2.getHeight())) {
            reset(false);
        }
        Bitmap bitmap3 = this.bitmap;
        if (bitmap3 != null && !this.bitmapIsCached) {
            bitmap3.recycle();
        }
        if (!(this.bitmap == null || !this.bitmapIsCached || (onImageEventListener2 = this.onImageEventListener) == null)) {
            onImageEventListener2.onPreviewReleased();
        }
        this.bitmapIsPreview = false;
        this.bitmapIsCached = bitmapIsCached2;
        this.bitmap = bitmap2;
        this.sWidth = bitmap2.getWidth();
        this.sHeight = bitmap2.getHeight();
        this.sOrientation = sOrientation2;
        boolean ready = checkReady();
        boolean imageLoaded = checkImageLoaded();
        if (ready || imageLoaded) {
            invalidate();
            requestLayout();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getExifOrientation(Context context, String sourceUri) {
        int exifOrientation = 0;
        if (sourceUri.startsWith("content")) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(Uri.parse(sourceUri), new String[]{"orientation"}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int orientation2 = cursor.getInt(0);
                    if (!VALID_ORIENTATIONS.contains(Integer.valueOf(orientation2)) || orientation2 == -1) {
                        String str = TAG;
                        Log.w(str, "Unsupported orientation: " + orientation2);
                    } else {
                        exifOrientation = orientation2;
                    }
                }
                if (cursor == null) {
                    return exifOrientation;
                }
            } catch (Exception e) {
                Log.w(TAG, "Could not get orientation of image from media store");
                if (0 == 0) {
                    return 0;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    cursor.close();
                }
                throw th;
            }
            cursor.close();
            return exifOrientation;
        } else if (!sourceUri.startsWith("file:///") || sourceUri.startsWith("file:///android_asset/")) {
            return 0;
        } else {
            try {
                int orientationAttr = new ExifInterface(sourceUri.substring("file:///".length() - 1)).getAttributeInt("Orientation", 1);
                if (orientationAttr != 1) {
                    if (orientationAttr != 0) {
                        if (orientationAttr == 6) {
                            return 90;
                        }
                        if (orientationAttr == 3) {
                            return ORIENTATION_180;
                        }
                        if (orientationAttr == 8) {
                            return ORIENTATION_270;
                        }
                        String str2 = TAG;
                        Log.w(str2, "Unsupported EXIF orientation: " + orientationAttr);
                        return 0;
                    }
                }
                return 0;
            } catch (Exception e2) {
                Log.w(TAG, "Could not get EXIF orientation of image");
                return 0;
            }
        }
    }

    private void execute(AsyncTask<Void, Void, ?> asyncTask) {
        asyncTask.executeOnExecutor(this.executor, new Void[0]);
    }

    /* access modifiers changed from: private */
    public static class Tile {
        private Bitmap bitmap;
        private Rect fileSRect;
        private boolean loading;
        private Rect sRect;
        private int sampleSize;
        private Rect vRect;
        private boolean visible;

        private Tile() {
        }
    }

    /* access modifiers changed from: private */
    public static class Anim {
        private long duration;
        private int easing;
        private boolean interruptible;
        private OnAnimationEventListener listener;
        private int origin;
        private PointF sCenterEnd;
        private PointF sCenterEndRequested;
        private PointF sCenterStart;
        private float scaleEnd;
        private float scaleStart;
        private long time;
        private PointF vFocusEnd;
        private PointF vFocusStart;

        private Anim() {
            this.duration = 500;
            this.interruptible = true;
            this.easing = 2;
            this.origin = 1;
            this.time = System.currentTimeMillis();
        }
    }

    /* access modifiers changed from: private */
    public static class ScaleAndTranslate {
        private float scale;
        private final PointF vTranslate;

        private ScaleAndTranslate(float scale2, PointF vTranslate2) {
            this.scale = scale2;
            this.vTranslate = vTranslate2;
        }
    }

    private void restoreState(ImageViewState state) {
        if (state != null && VALID_ORIENTATIONS.contains(Integer.valueOf(state.getOrientation()))) {
            this.orientation = state.getOrientation();
            this.pendingScale = Float.valueOf(state.getScale());
            this.sPendingCenter = state.getCenter();
            invalidate();
        }
    }

    public void setMaxTileSize(int maxPixels) {
        this.maxTileWidth = maxPixels;
        this.maxTileHeight = maxPixels;
    }

    public void setMaxTileSize(int maxPixelsX, int maxPixelsY) {
        this.maxTileWidth = maxPixelsX;
        this.maxTileHeight = maxPixelsY;
    }

    private Point getMaxBitmapDimensions(Canvas canvas) {
        return new Point(Math.min(canvas.getMaximumBitmapWidth(), this.maxTileWidth), Math.min(canvas.getMaximumBitmapHeight(), this.maxTileHeight));
    }

    private int sWidth() {
        int rotation = getRequiredRotation();
        if (rotation == 90 || rotation == 270) {
            return this.sHeight;
        }
        return this.sWidth;
    }

    private int sHeight() {
        int rotation = getRequiredRotation();
        if (rotation == 90 || rotation == 270) {
            return this.sWidth;
        }
        return this.sHeight;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void fileSRect(Rect sRect2, Rect target) {
        if (getRequiredRotation() == 0) {
            target.set(sRect2);
        } else if (getRequiredRotation() == 90) {
            target.set(sRect2.top, this.sHeight - sRect2.right, sRect2.bottom, this.sHeight - sRect2.left);
        } else if (getRequiredRotation() == 180) {
            target.set(this.sWidth - sRect2.right, this.sHeight - sRect2.bottom, this.sWidth - sRect2.left, this.sHeight - sRect2.top);
        } else {
            target.set(this.sWidth - sRect2.bottom, sRect2.left, this.sWidth - sRect2.top, sRect2.right);
        }
    }

    private int getRequiredRotation() {
        int i = this.orientation;
        if (i == -1) {
            return this.sOrientation;
        }
        return i;
    }

    private float distance(float x0, float x1, float y0, float y1) {
        float x = x0 - x1;
        float y = y0 - y1;
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    public void recycle() {
        reset(true);
        this.bitmapPaint = null;
        this.debugTextPaint = null;
        this.debugLinePaint = null;
        this.tileBgPaint = null;
    }

    private float viewToSourceX(float vx) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (vx - pointF.x) / this.scale;
    }

    private float viewToSourceY(float vy) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (vy - pointF.y) / this.scale;
    }

    public void viewToFileRect(Rect vRect, Rect fRect) {
        if (this.vTranslate != null && this.readySent) {
            fRect.set((int) viewToSourceX((float) vRect.left), (int) viewToSourceY((float) vRect.top), (int) viewToSourceX((float) vRect.right), (int) viewToSourceY((float) vRect.bottom));
            fileSRect(fRect, fRect);
            fRect.set(Math.max(0, fRect.left), Math.max(0, fRect.top), Math.min(this.sWidth, fRect.right), Math.min(this.sHeight, fRect.bottom));
            Rect rect = this.sRegion;
            if (rect != null) {
                fRect.offset(rect.left, this.sRegion.top);
            }
        }
    }

    public void visibleFileRect(Rect fRect) {
        if (this.vTranslate != null && this.readySent) {
            fRect.set(0, 0, getWidth(), getHeight());
            viewToFileRect(fRect, fRect);
        }
    }

    public final PointF viewToSourceCoord(PointF vxy) {
        return viewToSourceCoord(vxy.x, vxy.y, new PointF());
    }

    public final PointF viewToSourceCoord(float vx, float vy) {
        return viewToSourceCoord(vx, vy, new PointF());
    }

    public final PointF viewToSourceCoord(PointF vxy, PointF sTarget) {
        return viewToSourceCoord(vxy.x, vxy.y, sTarget);
    }

    public final PointF viewToSourceCoord(float vx, float vy, PointF sTarget) {
        if (this.vTranslate == null) {
            return null;
        }
        sTarget.set(viewToSourceX(vx), viewToSourceY(vy));
        return sTarget;
    }

    private float sourceToViewX(float sx) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (this.scale * sx) + pointF.x;
    }

    private float sourceToViewY(float sy) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (this.scale * sy) + pointF.y;
    }

    public final PointF sourceToViewCoord(PointF sxy) {
        return sourceToViewCoord(sxy.x, sxy.y, new PointF());
    }

    public final PointF sourceToViewCoord(float sx, float sy) {
        return sourceToViewCoord(sx, sy, new PointF());
    }

    public final PointF sourceToViewCoord(PointF sxy, PointF vTarget) {
        return sourceToViewCoord(sxy.x, sxy.y, vTarget);
    }

    public final PointF sourceToViewCoord(float sx, float sy, PointF vTarget) {
        if (this.vTranslate == null) {
            return null;
        }
        vTarget.set(sourceToViewX(sx), sourceToViewY(sy));
        return vTarget;
    }

    private void sourceToViewRect(Rect sRect2, Rect vTarget) {
        vTarget.set((int) sourceToViewX((float) sRect2.left), (int) sourceToViewY((float) sRect2.top), (int) sourceToViewX((float) sRect2.right), (int) sourceToViewY((float) sRect2.bottom));
    }

    private PointF vTranslateForSCenter(float sCenterX, float sCenterY, float scale2) {
        int vxCenter = getPaddingLeft() + (((getWidth() - getPaddingRight()) - getPaddingLeft()) / 2);
        int vyCenter = getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2);
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f));
        }
        this.satTemp.scale = scale2;
        this.satTemp.vTranslate.set(((float) vxCenter) - (sCenterX * scale2), ((float) vyCenter) - (sCenterY * scale2));
        fitToBounds(true, this.satTemp);
        return this.satTemp.vTranslate;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private PointF limitedSCenter(float sCenterX, float sCenterY, float scale2, PointF sTarget) {
        PointF vTranslate2 = vTranslateForSCenter(sCenterX, sCenterY, scale2);
        sTarget.set((((float) (getPaddingLeft() + (((getWidth() - getPaddingRight()) - getPaddingLeft()) / 2))) - vTranslate2.x) / scale2, (((float) (getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2))) - vTranslate2.y) / scale2);
        return sTarget;
    }

    private float minScale() {
        int vPadding = getPaddingBottom() + getPaddingTop();
        int hPadding = getPaddingLeft() + getPaddingRight();
        int i = this.minimumScaleType;
        if (i == 2 || i == 4) {
            return Math.max(((float) (getWidth() - hPadding)) / ((float) sWidth()), ((float) (getHeight() - vPadding)) / ((float) sHeight()));
        }
        if (i == 3) {
            float f = this.minScale;
            if (f > 0.0f) {
                return f;
            }
        }
        return Math.min(((float) (getWidth() - hPadding)) / ((float) sWidth()), ((float) (getHeight() - vPadding)) / ((float) sHeight()));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private float limitedScale(float targetScale) {
        return Math.min(this.maxScale, Math.max(minScale(), targetScale));
    }

    private float ease(int type, long time, float from, float change, long duration) {
        switch (type) {
            case 1:
                return easeOutQuad(time, from, change, duration);
            case 2:
                return easeInOutQuad(time, from, change, duration);
            default:
                throw new IllegalStateException("Unexpected easing type: " + type);
        }
    }

    private float easeOutQuad(long time, float from, float change, long duration) {
        float progress = ((float) time) / ((float) duration);
        return ((-change) * progress * (progress - 2.0f)) + from;
    }

    private float easeInOutQuad(long time, float from, float change, long duration) {
        float timeF = ((float) time) / (((float) duration) / 2.0f);
        if (timeF < 1.0f) {
            return ((change / 2.0f) * timeF * timeF) + from;
        }
        float timeF2 = timeF - 1.0f;
        return (((-change) / 2.0f) * (((timeF2 - 2.0f) * timeF2) - 1.0f)) + from;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void debug(String message, Object... args) {
        if (this.debug) {
            Log.d(TAG, String.format(message, args));
        }
    }

    private int px(int px) {
        return (int) (this.density * ((float) px));
    }

    public final void setRegionDecoderClass(Class<? extends ImageRegionDecoder> regionDecoderClass) {
        if (regionDecoderClass != null) {
            this.regionDecoderFactory = new CompatDecoderFactory(regionDecoderClass);
            return;
        }
        throw new IllegalArgumentException("Decoder class cannot be set to null");
    }

    public final void setRegionDecoderFactory(DecoderFactory<? extends ImageRegionDecoder> regionDecoderFactory2) {
        if (regionDecoderFactory2 != null) {
            this.regionDecoderFactory = regionDecoderFactory2;
            return;
        }
        throw new IllegalArgumentException("Decoder factory cannot be set to null");
    }

    public final void setBitmapDecoderClass(Class<? extends ImageDecoder> bitmapDecoderClass) {
        if (bitmapDecoderClass != null) {
            this.bitmapDecoderFactory = new CompatDecoderFactory(bitmapDecoderClass);
            return;
        }
        throw new IllegalArgumentException("Decoder class cannot be set to null");
    }

    public final void setBitmapDecoderFactory(DecoderFactory<? extends ImageDecoder> bitmapDecoderFactory2) {
        if (bitmapDecoderFactory2 != null) {
            this.bitmapDecoderFactory = bitmapDecoderFactory2;
            return;
        }
        throw new IllegalArgumentException("Decoder factory cannot be set to null");
    }

    public final void getPanRemaining(RectF vTarget) {
        if (isReady()) {
            float scaleWidth = this.scale * ((float) sWidth());
            float scaleHeight = this.scale * ((float) sHeight());
            int i = this.panLimit;
            if (i == 3) {
                vTarget.top = Math.max(0.0f, -(this.vTranslate.y - ((float) (getHeight() / 2))));
                vTarget.left = Math.max(0.0f, -(this.vTranslate.x - ((float) (getWidth() / 2))));
                vTarget.bottom = Math.max(0.0f, this.vTranslate.y - (((float) (getHeight() / 2)) - scaleHeight));
                vTarget.right = Math.max(0.0f, this.vTranslate.x - (((float) (getWidth() / 2)) - scaleWidth));
            } else if (i == 2) {
                vTarget.top = Math.max(0.0f, -(this.vTranslate.y - ((float) getHeight())));
                vTarget.left = Math.max(0.0f, -(this.vTranslate.x - ((float) getWidth())));
                vTarget.bottom = Math.max(0.0f, this.vTranslate.y + scaleHeight);
                vTarget.right = Math.max(0.0f, this.vTranslate.x + scaleWidth);
            } else {
                vTarget.top = Math.max(0.0f, -this.vTranslate.y);
                vTarget.left = Math.max(0.0f, -this.vTranslate.x);
                vTarget.bottom = Math.max(0.0f, (this.vTranslate.y + scaleHeight) - ((float) getHeight()));
                vTarget.right = Math.max(0.0f, (this.vTranslate.x + scaleWidth) - ((float) getWidth()));
            }
        }
    }

    public final void setPanLimit(int panLimit2) {
        if (VALID_PAN_LIMITS.contains(Integer.valueOf(panLimit2))) {
            this.panLimit = panLimit2;
            if (isReady()) {
                fitToBounds(true);
                invalidate();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Invalid pan limit: " + panLimit2);
    }

    public final void setMinimumScaleType(int scaleType) {
        if (VALID_SCALE_TYPES.contains(Integer.valueOf(scaleType))) {
            this.minimumScaleType = scaleType;
            if (isReady()) {
                fitToBounds(true);
                invalidate();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Invalid scale type: " + scaleType);
    }

    public final void setMaxScale(float maxScale2) {
        this.maxScale = maxScale2;
    }

    public final void setMinScale(float minScale2) {
        this.minScale = minScale2;
    }

    public final void setMinimumDpi(int dpi) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        setMaxScale(((metrics.xdpi + metrics.ydpi) / 2.0f) / ((float) dpi));
    }

    public final void setMaximumDpi(int dpi) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        setMinScale(((metrics.xdpi + metrics.ydpi) / 2.0f) / ((float) dpi));
    }

    public float getMaxScale() {
        return this.maxScale;
    }

    public final float getMinScale() {
        return minScale();
    }

    public void setMinimumTileDpi(int minimumTileDpi2) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.minimumTileDpi = (int) Math.min((metrics.xdpi + metrics.ydpi) / 2.0f, (float) minimumTileDpi2);
        if (isReady()) {
            reset(false);
            invalidate();
        }
    }

    public final PointF getCenter() {
        return viewToSourceCoord((float) (getWidth() / 2), (float) (getHeight() / 2));
    }

    public final float getScale() {
        return this.scale;
    }

    public final void setScaleAndCenter(float scale2, PointF sCenter) {
        this.anim = null;
        this.pendingScale = Float.valueOf(scale2);
        this.sPendingCenter = sCenter;
        this.sRequestedCenter = sCenter;
        invalidate();
    }

    public final void resetScaleAndCenter() {
        this.anim = null;
        this.pendingScale = Float.valueOf(limitedScale(0.0f));
        if (isReady()) {
            this.sPendingCenter = new PointF((float) (sWidth() / 2), (float) (sHeight() / 2));
        } else {
            this.sPendingCenter = new PointF(0.0f, 0.0f);
        }
        invalidate();
    }

    public final boolean isReady() {
        return this.readySent;
    }

    /* access modifiers changed from: protected */
    public void onReady() {
    }

    public final boolean isImageLoaded() {
        return this.imageLoadedSent;
    }

    /* access modifiers changed from: protected */
    public void onImageLoaded() {
    }

    public final int getSWidth() {
        return this.sWidth;
    }

    public final int getSHeight() {
        return this.sHeight;
    }

    public final int getOrientation() {
        return this.orientation;
    }

    public final int getAppliedOrientation() {
        return getRequiredRotation();
    }

    public final ImageViewState getState() {
        if (this.vTranslate == null || this.sWidth <= 0 || this.sHeight <= 0) {
            return null;
        }
        return new ImageViewState(getScale(), getCenter(), getOrientation());
    }

    public final boolean isZoomEnabled() {
        return this.zoomEnabled;
    }

    public final void setZoomEnabled(boolean zoomEnabled2) {
        this.zoomEnabled = zoomEnabled2;
    }

    public final boolean isQuickScaleEnabled() {
        return this.quickScaleEnabled;
    }

    public final void setQuickScaleEnabled(boolean quickScaleEnabled2) {
        this.quickScaleEnabled = quickScaleEnabled2;
    }

    public final boolean isPanEnabled() {
        return this.panEnabled;
    }

    public final void setPanEnabled(boolean panEnabled2) {
        PointF pointF;
        this.panEnabled = panEnabled2;
        if (!panEnabled2 && (pointF = this.vTranslate) != null) {
            pointF.x = ((float) (getWidth() / 2)) - (this.scale * ((float) (sWidth() / 2)));
            this.vTranslate.y = ((float) (getHeight() / 2)) - (this.scale * ((float) (sHeight() / 2)));
            if (isReady()) {
                refreshRequiredTiles(true);
                invalidate();
            }
        }
    }

    public final void setTileBackgroundColor(int tileBgColor) {
        if (Color.alpha(tileBgColor) == 0) {
            this.tileBgPaint = null;
        } else {
            Paint paint = new Paint();
            this.tileBgPaint = paint;
            paint.setStyle(Paint.Style.FILL);
            this.tileBgPaint.setColor(tileBgColor);
        }
        invalidate();
    }

    public final void setDoubleTapZoomScale(float doubleTapZoomScale2) {
        this.doubleTapZoomScale = doubleTapZoomScale2;
    }

    public final void setDoubleTapZoomDpi(int dpi) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        setDoubleTapZoomScale(((metrics.xdpi + metrics.ydpi) / 2.0f) / ((float) dpi));
    }

    public final void setDoubleTapZoomStyle(int doubleTapZoomStyle2) {
        if (VALID_ZOOM_STYLES.contains(Integer.valueOf(doubleTapZoomStyle2))) {
            this.doubleTapZoomStyle = doubleTapZoomStyle2;
            return;
        }
        throw new IllegalArgumentException("Invalid zoom style: " + doubleTapZoomStyle2);
    }

    public final void setDoubleTapZoomDuration(int durationMs) {
        this.doubleTapZoomDuration = Math.max(0, durationMs);
    }

    public void setExecutor(Executor executor2) {
        if (executor2 != null) {
            this.executor = executor2;
            return;
        }
        throw new NullPointerException("Executor must not be null");
    }

    public void setEagerLoadingEnabled(boolean eagerLoadingEnabled2) {
        this.eagerLoadingEnabled = eagerLoadingEnabled2;
    }

    public final void setDebug(boolean debug2) {
        this.debug = debug2;
    }

    public boolean hasImage() {
        return (this.uri == null && this.bitmap == null) ? false : true;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener2) {
        this.onLongClickListener = onLongClickListener2;
    }

    public void setOnImageEventListener(OnImageEventListener onImageEventListener2) {
        this.onImageEventListener = onImageEventListener2;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener2) {
        this.onStateChangedListener = onStateChangedListener2;
    }

    private void sendStateChanged(float oldScale, PointF oldVTranslate, int origin) {
        OnStateChangedListener onStateChangedListener2 = this.onStateChangedListener;
        if (onStateChangedListener2 != null) {
            float f = this.scale;
            if (f != oldScale) {
                onStateChangedListener2.onScaleChanged(f, origin);
            }
        }
        if (this.onStateChangedListener != null && !this.vTranslate.equals(oldVTranslate)) {
            this.onStateChangedListener.onCenterChanged(getCenter(), origin);
        }
    }

    public AnimationBuilder animateCenter(PointF sCenter) {
        if (!isReady()) {
            return null;
        }
        return new AnimationBuilder(sCenter);
    }

    public AnimationBuilder animateScale(float scale2) {
        if (!isReady()) {
            return null;
        }
        return new AnimationBuilder(scale2);
    }

    public AnimationBuilder animateScaleAndCenter(float scale2, PointF sCenter) {
        if (!isReady()) {
            return null;
        }
        return new AnimationBuilder(scale2, sCenter);
    }

    public final class AnimationBuilder {
        private long duration;
        private int easing;
        private boolean interruptible;
        private OnAnimationEventListener listener;
        private int origin;
        private boolean panLimited;
        private final PointF targetSCenter;
        private final float targetScale;
        private final PointF vFocus;

        private AnimationBuilder(PointF sCenter) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = SubsamplingScaleImageView.this.scale;
            this.targetSCenter = sCenter;
            this.vFocus = null;
        }

        private AnimationBuilder(float scale) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = scale;
            this.targetSCenter = SubsamplingScaleImageView.this.getCenter();
            this.vFocus = null;
        }

        private AnimationBuilder(float scale, PointF sCenter) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = scale;
            this.targetSCenter = sCenter;
            this.vFocus = null;
        }

        private AnimationBuilder(float scale, PointF sCenter, PointF vFocus2) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = scale;
            this.targetSCenter = sCenter;
            this.vFocus = vFocus2;
        }

        public AnimationBuilder withDuration(long duration2) {
            this.duration = duration2;
            return this;
        }

        public AnimationBuilder withInterruptible(boolean interruptible2) {
            this.interruptible = interruptible2;
            return this;
        }

        public AnimationBuilder withEasing(int easing2) {
            if (SubsamplingScaleImageView.VALID_EASING_STYLES.contains(Integer.valueOf(easing2))) {
                this.easing = easing2;
                return this;
            }
            throw new IllegalArgumentException("Unknown easing type: " + easing2);
        }

        public AnimationBuilder withOnAnimationEventListener(OnAnimationEventListener listener2) {
            this.listener = listener2;
            return this;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private AnimationBuilder withPanLimited(boolean panLimited2) {
            this.panLimited = panLimited2;
            return this;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private AnimationBuilder withOrigin(int origin2) {
            this.origin = origin2;
            return this;
        }

        public void start() {
            if (!(SubsamplingScaleImageView.this.anim == null || SubsamplingScaleImageView.this.anim.listener == null)) {
                try {
                    SubsamplingScaleImageView.this.anim.listener.onInterruptedByNewAnim();
                } catch (Exception e) {
                    Log.w(SubsamplingScaleImageView.TAG, "Error thrown by animation listener", e);
                }
            }
            int vxCenter = SubsamplingScaleImageView.this.getPaddingLeft() + (((SubsamplingScaleImageView.this.getWidth() - SubsamplingScaleImageView.this.getPaddingRight()) - SubsamplingScaleImageView.this.getPaddingLeft()) / 2);
            int vyCenter = SubsamplingScaleImageView.this.getPaddingTop() + (((SubsamplingScaleImageView.this.getHeight() - SubsamplingScaleImageView.this.getPaddingBottom()) - SubsamplingScaleImageView.this.getPaddingTop()) / 2);
            float targetScale2 = SubsamplingScaleImageView.this.limitedScale(this.targetScale);
            PointF targetSCenter2 = this.panLimited ? SubsamplingScaleImageView.this.limitedSCenter(this.targetSCenter.x, this.targetSCenter.y, targetScale2, new PointF()) : this.targetSCenter;
            SubsamplingScaleImageView.this.anim = new Anim();
            SubsamplingScaleImageView.this.anim.scaleStart = SubsamplingScaleImageView.this.scale;
            SubsamplingScaleImageView.this.anim.scaleEnd = targetScale2;
            SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
            SubsamplingScaleImageView.this.anim.sCenterEndRequested = targetSCenter2;
            SubsamplingScaleImageView.this.anim.sCenterStart = SubsamplingScaleImageView.this.getCenter();
            SubsamplingScaleImageView.this.anim.sCenterEnd = targetSCenter2;
            SubsamplingScaleImageView.this.anim.vFocusStart = SubsamplingScaleImageView.this.sourceToViewCoord(targetSCenter2);
            SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF((float) vxCenter, (float) vyCenter);
            SubsamplingScaleImageView.this.anim.duration = this.duration;
            SubsamplingScaleImageView.this.anim.interruptible = this.interruptible;
            SubsamplingScaleImageView.this.anim.easing = this.easing;
            SubsamplingScaleImageView.this.anim.origin = this.origin;
            SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
            SubsamplingScaleImageView.this.anim.listener = this.listener;
            PointF pointF = this.vFocus;
            if (pointF != null) {
                float vTranslateXEnd = pointF.x - (SubsamplingScaleImageView.this.anim.sCenterStart.x * targetScale2);
                float vTranslateYEnd = this.vFocus.y - (SubsamplingScaleImageView.this.anim.sCenterStart.y * targetScale2);
                ScaleAndTranslate satEnd = new ScaleAndTranslate(targetScale2, new PointF(vTranslateXEnd, vTranslateYEnd));
                SubsamplingScaleImageView.this.fitToBounds(true, satEnd);
                SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF(this.vFocus.x + (satEnd.vTranslate.x - vTranslateXEnd), this.vFocus.y + (satEnd.vTranslate.y - vTranslateYEnd));
            }
            SubsamplingScaleImageView.this.invalidate();
        }
    }

    public static class DefaultOnAnimationEventListener implements OnAnimationEventListener {
        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnAnimationEventListener
        public void onComplete() {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnAnimationEventListener
        public void onInterruptedByUser() {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnAnimationEventListener
        public void onInterruptedByNewAnim() {
        }
    }

    public static class DefaultOnImageEventListener implements OnImageEventListener {
        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnImageEventListener
        public void onReady() {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnImageEventListener
        public void onImageLoaded() {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnImageEventListener
        public void onPreviewLoadError(Exception e) {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnImageEventListener
        public void onImageLoadError(Exception e) {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnImageEventListener
        public void onTileLoadError(Exception e) {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnImageEventListener
        public void onPreviewReleased() {
        }
    }

    public static class DefaultOnStateChangedListener implements OnStateChangedListener {
        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnStateChangedListener
        public void onCenterChanged(PointF newCenter, int origin) {
        }

        @Override // com.textonphoto.customqoutescreator.scale.SubsamplingScaleImageView.OnStateChangedListener
        public void onScaleChanged(float newScale, int origin) {
        }
    }
}

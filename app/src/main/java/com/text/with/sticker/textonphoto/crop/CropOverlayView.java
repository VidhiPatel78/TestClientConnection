package com.text.with.sticker.textonphoto.crop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class CropOverlayView extends View {
    private static final float DEFAULT_CORNER_EXTENSION_DP;
    private static final float DEFAULT_CORNER_LENGTH_DP = 20.0f;
    private static final float DEFAULT_CORNER_OFFSET_DP;
    private static final float DEFAULT_CORNER_THICKNESS_DP;
    private static final float DEFAULT_LINE_THICKNESS_DP;
    private static final float DEFAULT_SHOW_GUIDELINES_LIMIT = 100.0f;
    private static final int GUIDELINES_OFF = 0;
    private static final int GUIDELINES_ON = 2;
    private static final int GUIDELINES_ON_TOUCH = 1;
    private static final int SNAP_RADIUS_DP = 6;
    private boolean initializedCropWindow = false;
    private int mAspectRatioX = 1;
    private int mAspectRatioY = 1;
    private Paint mBackgroundPaint;
    private Rect mBitmapRect;
    private Paint mBorderPaint;
    private float mCornerExtension;
    private float mCornerLength;
    private float mCornerOffset;
    private Paint mCornerPaint;
    private boolean mFixAspectRatio = false;
    private Paint mGuidelinePaint;
    private int mGuidelines;
    private float mHandleRadius;
    private Handle mPressedHandle;
    private float mSnapRadius;
    private float mTargetAspectRatio = (((float) 1) / ((float) 1));
    private Pair<Float, Float> mTouchOffset;

    static {
        float lineThickness = PaintUtil.getLineThickness();
        DEFAULT_LINE_THICKNESS_DP = lineThickness;
        float cornerThickness = PaintUtil.getCornerThickness();
        DEFAULT_CORNER_THICKNESS_DP = cornerThickness;
        float f = (cornerThickness / 2.0f) - (lineThickness / 2.0f);
        DEFAULT_CORNER_OFFSET_DP = f;
        DEFAULT_CORNER_EXTENSION_DP = (cornerThickness / 2.0f) + f;
    }

    public CropOverlayView(Context context) {
        super(context);
        init(context);
    }

    public CropOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        initCropWindow(this.mBitmapRect);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas, this.mBitmapRect);
        if (showGuidelines()) {
            int i = this.mGuidelines;
            if (i == 2) {
                drawRuleOfThirdsGuidelines(canvas);
            } else if (i == 1 && this.mPressedHandle != null) {
                drawRuleOfThirdsGuidelines(canvas);
            }
        }
        canvas.drawRect(Edge.LEFT.getCoordinate(), Edge.TOP.getCoordinate(), Edge.RIGHT.getCoordinate(), Edge.BOTTOM.getCoordinate(), this.mBorderPaint);
        drawCorners(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                onActionDown(event.getX(), event.getY());
                return true;
            case 1:
            case 3:
                getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp();
                return true;
            case 2:
                onActionMove(event.getX(), event.getY());
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            default:
                return false;
        }
    }

    public void setBitmapRect(Rect bitmapRect) {
        this.mBitmapRect = bitmapRect;
        initCropWindow(bitmapRect);
    }

    public void resetCropOverlayView() {
        if (this.initializedCropWindow) {
            initCropWindow(this.mBitmapRect);
            invalidate();
        }
    }

    public void setGuidelines(int guidelines) {
        if (guidelines < 0 || guidelines > 2) {
            throw new IllegalArgumentException("Guideline value must be set between 0 and 2. See documentation.");
        }
        this.mGuidelines = guidelines;
        if (this.initializedCropWindow) {
            initCropWindow(this.mBitmapRect);
            invalidate();
        }
    }

    public void setFixedAspectRatio(boolean fixAspectRatio) {
        this.mFixAspectRatio = fixAspectRatio;
        if (this.initializedCropWindow) {
            initCropWindow(this.mBitmapRect);
            invalidate();
        }
    }

    public void setAspectRatioX(int aspectRatioX) {
        if (aspectRatioX > 0) {
            this.mAspectRatioX = aspectRatioX;
            this.mTargetAspectRatio = ((float) aspectRatioX) / ((float) this.mAspectRatioY);
            if (this.initializedCropWindow) {
                initCropWindow(this.mBitmapRect);
                invalidate();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
    }

    public void setAspectRatioY(int aspectRatioY) {
        if (aspectRatioY > 0) {
            this.mAspectRatioY = aspectRatioY;
            this.mTargetAspectRatio = ((float) this.mAspectRatioX) / ((float) aspectRatioY);
            if (this.initializedCropWindow) {
                initCropWindow(this.mBitmapRect);
                invalidate();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
    }

    public void setInitialAttributeValues(int guidelines, boolean fixAspectRatio, int aspectRatioX, int aspectRatioY) {
        if (guidelines < 0 || guidelines > 2) {
            throw new IllegalArgumentException("Guideline value must be set between 0 and 2. See documentation.");
        }
        this.mGuidelines = guidelines;
        this.mFixAspectRatio = fixAspectRatio;
        if (aspectRatioX > 0) {
            this.mAspectRatioX = aspectRatioX;
            this.mTargetAspectRatio = ((float) aspectRatioX) / ((float) this.mAspectRatioY);
            if (aspectRatioY > 0) {
                this.mAspectRatioY = aspectRatioY;
                this.mTargetAspectRatio = ((float) aspectRatioX) / ((float) aspectRatioY);
                return;
            }
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        }
        throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
    }

    @SuppressLint("WrongConstant")
    private void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.mHandleRadius = HandleUtil.getTargetRadius(context);
        this.mSnapRadius = TypedValue.applyDimension(1, 6.0f, displayMetrics);
        this.mBorderPaint = PaintUtil.newBorderPaint(context);
        this.mGuidelinePaint = PaintUtil.newGuidelinePaint();
        this.mBackgroundPaint = PaintUtil.newBackgroundPaint(context);
        this.mCornerPaint = PaintUtil.newCornerPaint(context);
        this.mCornerOffset = TypedValue.applyDimension(1, DEFAULT_CORNER_OFFSET_DP, displayMetrics);
        this.mCornerExtension = TypedValue.applyDimension(1, DEFAULT_CORNER_EXTENSION_DP, displayMetrics);
        this.mCornerLength = TypedValue.applyDimension(1, DEFAULT_CORNER_LENGTH_DP, displayMetrics);
        this.mGuidelines = 1;
    }

    private void initCropWindow(Rect bitmapRect) {
        if (!this.initializedCropWindow) {
            this.initializedCropWindow = true;
        }
        if (!this.mFixAspectRatio) {
            float horizontalPadding = ((float) bitmapRect.width()) * 0.1f;
            float verticalPadding = ((float) bitmapRect.height()) * 0.1f;
            Edge.LEFT.setCoordinate(((float) bitmapRect.left) + horizontalPadding);
            Edge.TOP.setCoordinate(((float) bitmapRect.top) + verticalPadding);
            Edge.RIGHT.setCoordinate(((float) bitmapRect.right) - horizontalPadding);
            Edge.BOTTOM.setCoordinate(((float) bitmapRect.bottom) - verticalPadding);
        } else if (AspectRatioUtil.calculateAspectRatio(bitmapRect) > this.mTargetAspectRatio) {
            Edge.TOP.setCoordinate((float) bitmapRect.top);
            Edge.BOTTOM.setCoordinate((float) bitmapRect.bottom);
            float centerX = ((float) getWidth()) / 2.0f;
            float cropWidth = Math.max(40.0f, AspectRatioUtil.calculateWidth(Edge.TOP.getCoordinate(), Edge.BOTTOM.getCoordinate(), this.mTargetAspectRatio));
            if (cropWidth == 40.0f) {
                this.mTargetAspectRatio = 40.0f / (Edge.BOTTOM.getCoordinate() - Edge.TOP.getCoordinate());
            }
            float halfCropWidth = cropWidth / 2.0f;
            Edge.LEFT.setCoordinate(centerX - halfCropWidth);
            Edge.RIGHT.setCoordinate(centerX + halfCropWidth);
        } else {
            Edge.LEFT.setCoordinate((float) bitmapRect.left);
            Edge.RIGHT.setCoordinate((float) bitmapRect.right);
            float centerY = ((float) getHeight()) / 2.0f;
            float cropHeight = Math.max(40.0f, AspectRatioUtil.calculateHeight(Edge.LEFT.getCoordinate(), Edge.RIGHT.getCoordinate(), this.mTargetAspectRatio));
            if (cropHeight == 40.0f) {
                this.mTargetAspectRatio = (Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate()) / 40.0f;
            }
            float halfCropHeight = cropHeight / 2.0f;
            Edge.TOP.setCoordinate(centerY - halfCropHeight);
            Edge.BOTTOM.setCoordinate(centerY + halfCropHeight);
        }
    }

    public static boolean showGuidelines() {
        if (Math.abs(Edge.LEFT.getCoordinate() - Edge.RIGHT.getCoordinate()) < DEFAULT_SHOW_GUIDELINES_LIMIT || Math.abs(Edge.TOP.getCoordinate() - Edge.BOTTOM.getCoordinate()) < DEFAULT_SHOW_GUIDELINES_LIMIT) {
            return false;
        }
        return true;
    }

    private void drawRuleOfThirdsGuidelines(Canvas canvas) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        float oneThirdCropWidth = Edge.getWidth() / 3.0f;
        float x1 = left + oneThirdCropWidth;
        Canvas canvas2 = canvas;
        float f = top;
        float f2 = bottom;
        canvas2.drawLine(x1, f, x1, f2, this.mGuidelinePaint);
        float x2 = right - oneThirdCropWidth;
        canvas2.drawLine(x2, f, x2, f2, this.mGuidelinePaint);
        float oneThirdCropHeight = Edge.getHeight() / 3.0f;
        float y1 = top + oneThirdCropHeight;
        Canvas canvas3 = canvas;
        float f3 = left;
        float f4 = right;
        canvas3.drawLine(f3, y1, f4, y1, this.mGuidelinePaint);
        float y2 = bottom - oneThirdCropHeight;
        canvas3.drawLine(f3, y2, f4, y2, this.mGuidelinePaint);
    }

    private void drawBackground(Canvas canvas, Rect bitmapRect) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        Canvas canvas2 = canvas;
        canvas2.drawRect((float) bitmapRect.left, (float) bitmapRect.top, (float) bitmapRect.right, top, this.mBackgroundPaint);
        canvas2.drawRect((float) bitmapRect.left, bottom, (float) bitmapRect.right, (float) bitmapRect.bottom, this.mBackgroundPaint);
        canvas.drawRect((float) bitmapRect.left, top, left, bottom, this.mBackgroundPaint);
        canvas.drawRect(right, top, (float) bitmapRect.right, bottom, this.mBackgroundPaint);
    }

    private void drawCorners(Canvas canvas) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        float f = this.mCornerOffset;
        Canvas canvas2 = canvas;
        canvas2.drawLine(left - f, top - this.mCornerExtension, left - f, top + this.mCornerLength, this.mCornerPaint);
        float f2 = this.mCornerOffset;
        canvas.drawLine(left, top - f2, left + this.mCornerLength, top - f2, this.mCornerPaint);
        float f3 = this.mCornerOffset;
        canvas2.drawLine(right + f3, top - this.mCornerExtension, right + f3, top + this.mCornerLength, this.mCornerPaint);
        float f4 = this.mCornerOffset;
        canvas2.drawLine(right, top - f4, right - this.mCornerLength, top - f4, this.mCornerPaint);
        float f5 = this.mCornerOffset;
        canvas2.drawLine(left - f5, bottom + this.mCornerExtension, left - f5, bottom - this.mCornerLength, this.mCornerPaint);
        float f6 = this.mCornerOffset;
        canvas.drawLine(left, bottom + f6, left + this.mCornerLength, bottom + f6, this.mCornerPaint);
        float f7 = this.mCornerOffset;
        canvas2.drawLine(right + f7, bottom + this.mCornerExtension, right + f7, bottom - this.mCornerLength, this.mCornerPaint);
        float f8 = this.mCornerOffset;
        canvas2.drawLine(right, bottom + f8, right - this.mCornerLength, bottom + f8, this.mCornerPaint);
    }

    private void onActionDown(float x, float y) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        Handle pressedHandle = HandleUtil.getPressedHandle(x, y, left, top, right, bottom, this.mHandleRadius);
        this.mPressedHandle = pressedHandle;
        if (pressedHandle != null) {
            this.mTouchOffset = HandleUtil.getOffset(pressedHandle, x, y, left, top, right, bottom);
            invalidate();
        }
    }

    private void onActionUp() {
        if (this.mPressedHandle != null) {
            this.mPressedHandle = null;
            invalidate();
        }
    }

    private void onActionMove(float x, float y) {
        if (this.mPressedHandle != null) {
            float x2 = x + ((Float) this.mTouchOffset.first).floatValue();
            float y2 = y + ((Float) this.mTouchOffset.second).floatValue();
            if (this.mFixAspectRatio) {
                this.mPressedHandle.updateCropWindow(x2, y2, this.mTargetAspectRatio, this.mBitmapRect, this.mSnapRadius);
            } else {
                this.mPressedHandle.updateCropWindow(x2, y2, this.mBitmapRect, this.mSnapRadius);
            }
            invalidate();
        }
    }
}

package com.text.with.sticker.textonphoto.quotesedit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MultiTouchListener implements View.OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    Bitmap bitmap = null;
    boolean bt = false;
    boolean checkstickerWH = false;
    private boolean disContinueHandleTransparecy = true;
    GestureDetector gd = null;
    public boolean isRotateEnabled = true;
    public boolean isRotationEnabled = false;
    public boolean isScaleEnabled = true;
    public boolean isTranslateEnabled = true;
    public boolean isZoomEnabled = false;
    private TouchCallbackListener listener = null;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;
    private TouchMoveListener tmListener = null;

    public interface TouchCallbackListener {
        void onTouchCallback(View view);

        void onTouchMoveCallback(View view);

        void onTouchUpCallback(View view);
    }

    public interface TouchMoveListener {
        boolean onTouchDown(View view, MotionEvent motionEvent);

        void onTouchMove(View view, MotionEvent motionEvent);
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }

        @Override // com.textonphoto.customqoutescreator.quotesedit.ScaleGestureDetector.OnScaleGestureListener, com.textonphoto.customqoutescreator.quotesedit.ScaleGestureDetector.SimpleOnScaleGestureListener
        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            this.mPivotX = detector.getFocusX();
            this.mPivotY = detector.getFocusY();
            this.mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        @Override // com.textonphoto.customqoutescreator.quotesedit.ScaleGestureDetector.OnScaleGestureListener, com.textonphoto.customqoutescreator.quotesedit.ScaleGestureDetector.SimpleOnScaleGestureListener
        public boolean onScale(View view, ScaleGestureDetector detector) {
            float angle;
            float angle2;
            float f = 0.0f;
            TransformInfo info = new TransformInfo();
            info.deltaScale = MultiTouchListener.this.isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            if (MultiTouchListener.this.isRotateEnabled) {
                angle = Vector2D.getAngle(this.mPrevSpanVector, detector.getCurrentSpanVector());
            } else {
                angle = 0.0f;
            }
            info.deltaAngle = angle;
            if (MultiTouchListener.this.isTranslateEnabled) {
                angle2 = detector.getFocusX() - this.mPivotX;
            } else {
                angle2 = 0.0f;
            }
            info.deltaX = angle2;
            if (MultiTouchListener.this.isTranslateEnabled) {
                f = detector.getFocusY() - this.mPivotY;
            }
            info.deltaY = f;
            info.pivotX = this.mPivotX;
            info.pivotY = this.mPivotY;
            info.minimumScale = MultiTouchListener.this.minimumScale;
            info.maximumScale = MultiTouchListener.this.maximumScale;
            MultiTouchListener.this.move(view, info);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    public MultiTouchListener setGestureListener(GestureDetector gd2) {
        this.gd = gd2;
        return this;
    }

    public MultiTouchListener setOnTouchCallbackListener(TouchCallbackListener l) {
        this.listener = l;
        return this;
    }

    public MultiTouchListener setOnTouchMoveListener(TouchMoveListener l) {
        this.tmListener = l;
        return this;
    }

    public MultiTouchListener enableRotation(boolean b) {
        this.isRotationEnabled = b;
        return this;
    }

    public MultiTouchListener enableZoom(boolean b) {
        this.isZoomEnabled = b;
        return this;
    }

    public MultiTouchListener setMinScale(float f) {
        this.minimumScale = f;
        return this;
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            return degrees - 360.0f;
        }
        if (degrees < -180.0f) {
            return 360.0f + degrees;
        }
        return degrees;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void move(View view, TransformInfo info) {
        if (this.isZoomEnabled) {
            computeRenderOffset(view, info.pivotX, info.pivotY);
            adjustTranslation(view, info.deltaX, info.deltaY);
            float scale = Math.max(info.minimumScale, Math.min(info.maximumScale, view.getScaleX() * info.deltaScale));
            view.setScaleX(scale);
            view.setScaleY(scale);
        }
        if (this.isRotationEnabled) {
            float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
            Log.i("testing", "Rotation : " + rotation);
            view.setRotation(rotation);
        }
    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() != pivotX || view.getPivotY() != pivotY) {
            float[] prevPoint = {0.0f, 0.0f};
            view.getMatrix().mapPoints(prevPoint);
            view.setPivotX(pivotX);
            view.setPivotY(pivotY);
            float[] currPoint = {0.0f, 0.0f};
            view.getMatrix().mapPoints(currPoint);
            float offsetY = currPoint[1] - prevPoint[1];
            view.setTranslationX(view.getTranslationX() - (currPoint[0] - prevPoint[0]));
            view.setTranslationY(view.getTranslationY() - offsetY);
        }
    }

    public boolean handleTransparency(View view, MotionEvent event) {
        TouchCallbackListener touchCallbackListener;
        try {
            Log.i("MOVE_TESTs", "touch test: " + view.getWidth() + " / " + ((ResizableImageview) view).getMainWidth());
            boolean b = true;
            boolean isSmaller = ((float) view.getWidth()) < ((ResizableImageview) view).getMainWidth() && ((float) view.getHeight()) < ((ResizableImageview) view).getMainHeight();
            if (isSmaller && ((ResizableImageview) view).getBorderVisbilty()) {
                return false;
            }
            if (event.getAction() == 0 && !isSmaller && (touchCallbackListener = this.listener) != null) {
                touchCallbackListener.onTouchMoveCallback(view);
            }
            if (event.getAction() == 2 && this.bt) {
                return true;
            }
            if (event.getAction() != 1 || !this.bt) {
                int[] posXY = new int[2];
                view.getLocationOnScreen(posXY);
                float r = view.getRotation();
                Matrix mat = new Matrix();
                mat.postRotate(-r);
                float[] point = {(float) ((int) (event.getRawX() - ((float) posXY[0]))), (float) ((int) (event.getRawY() - ((float) posXY[1])))};
                mat.mapPoints(point);
                int rx = (int) point[0];
                int ry = (int) point[1];
                if (event.getAction() == 0) {
                    this.bt = false;
                    boolean borderVisbilty = ((ResizableImageview) view).getBorderVisbilty();
                    if (borderVisbilty) {
                        ((ResizableImageview) view).setBorderVisibility(false);
                    }
                    this.bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                    view.draw(new Canvas(this.bitmap));
                    if (borderVisbilty) {
                        ((ResizableImageview) view).setBorderVisibility(true);
                    }
                    rx = (int) (((float) rx) * (((float) this.bitmap.getWidth()) / (((float) this.bitmap.getWidth()) * view.getScaleX())));
                    ry = (int) (((float) ry) * (((float) this.bitmap.getHeight()) / (((float) this.bitmap.getHeight()) * view.getScaleX())));
                }
                if (rx >= 0 && ry >= 0 && rx <= this.bitmap.getWidth()) {
                    if (ry <= this.bitmap.getHeight()) {
                        if (this.bitmap.getPixel(rx, ry) != 0) {
                            b = false;
                        }
                        if (event.getAction() != 0) {
                            return b;
                        }
                        this.bt = b;
                        if (b) {
                            if (!isSmaller) {
                                ((ResizableImageview) view).setBorderVisibility(false);
                                return b;
                            }
                        }
                        return b;
                    }
                }
                return false;
            }
            this.bt = false;
            Bitmap bitmap2 = this.bitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        this.mScaleGestureDetector.onTouchEvent(view, event);
        RelativeLayout rl = (RelativeLayout) view.getParent();
        Log.e("aaa", "aaa");
        int newPointerIndex = 0;
        if ((view instanceof ResizableImageview) && !((ResizableImageview) view).getBorderVisbilty() && this.disContinueHandleTransparecy) {
            Log.e("bbbb", "bbbb1");
            this.disContinueHandleTransparecy = false;
        }
        if (!this.isTranslateEnabled) {
            Log.e("bbbb", "bbbb3");
            return true;
        }
        int action = event.getAction();
        switch (event.getActionMasked() & action) {
            case 0:
                Log.e("aaa", "aaa0");
                if (rl != null) {
                    rl.requestDisallowInterceptTouchEvent(true);
                }
                TouchCallbackListener touchCallbackListener = this.listener;
                if (touchCallbackListener != null) {
                    touchCallbackListener.onTouchCallback(view);
                }
                view.bringToFront();
                if (view instanceof ResizableImageview) {
                    ((ResizableImageview) view).setBorderVisibility(true);
                }
                this.mPrevX = event.getX();
                this.mPrevY = event.getY();
                this.mActivePointerId = event.getPointerId(0);
                break;
            case 1:
                Log.e("aaa", "aaa1");
                this.mActivePointerId = -1;
                this.disContinueHandleTransparecy = true;
                TouchCallbackListener touchCallbackListener2 = this.listener;
                if (touchCallbackListener2 != null) {
                    touchCallbackListener2.onTouchUpCallback(view);
                }
                float rotation = view.getRotation();
                if (Math.abs(90.0f - Math.abs(rotation)) <= 5.0f) {
                    if (rotation > 0.0f) {
                        rotation = 90.0f;
                    } else {
                        rotation = -90.0f;
                    }
                }
                if (Math.abs(0.0f - Math.abs(rotation)) <= 5.0f) {
                    if (rotation > 0.0f) {
                        rotation = 0.0f;
                    } else {
                        rotation = -0.0f;
                    }
                }
                if (Math.abs(180.0f - Math.abs(rotation)) <= 5.0f) {
                    if (rotation > 0.0f) {
                        rotation = 180.0f;
                    } else {
                        rotation = -180.0f;
                    }
                }
                view.setRotation(rotation);
                Log.i("testing", "Final Rotation : " + rotation);
                break;
            case 2:
                Log.e("aaa", "aaa2");
                if (rl != null) {
                    rl.requestDisallowInterceptTouchEvent(true);
                }
                int pointerIndex = event.findPointerIndex(this.mActivePointerId);
                if (pointerIndex != -1) {
                    float currX = event.getX(pointerIndex);
                    float currY = event.getY(pointerIndex);
                    if (!this.mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - this.mPrevX, currY - this.mPrevY);
                        break;
                    }
                }
                break;
            case 3:
                Log.e("aaa", "aaa3");
                this.mActivePointerId = -1;
                break;
            case 6:
                Log.e("aaa", "aaa6");
                int pointerIndex2 = (65280 & action) >> 8;
                if (event.getPointerId(pointerIndex2) == this.mActivePointerId) {
                    if (pointerIndex2 == 0) {
                        newPointerIndex = 1;
                    }
                    this.mPrevX = event.getX(newPointerIndex);
                    this.mPrevY = event.getY(newPointerIndex);
                    this.mActivePointerId = event.getPointerId(newPointerIndex);
                    break;
                }
                break;
        }
        return true;
    }
}

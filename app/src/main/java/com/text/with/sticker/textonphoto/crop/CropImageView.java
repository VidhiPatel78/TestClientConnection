package com.text.with.sticker.textonphoto.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.text.with.sticker.textonphoto.R;


public class CropImageView extends FrameLayout {
    public static final int DEFAULT_ASPECT_RATIO_X = 1;
    public static final int DEFAULT_ASPECT_RATIO_Y = 1;
    public static final boolean DEFAULT_FIXED_ASPECT_RATIO = false;
    public static final int DEFAULT_GUIDELINES = 1;
    private static final int DEFAULT_IMAGE_RESOURCE = 0;
    private static final String DEGREES_ROTATED = "DEGREES_ROTATED";
    private static final Rect EMPTY_RECT = new Rect();
    private int mAspectRatioX = 1;
    private int mAspectRatioY = 1;
    private Bitmap mBitmap;
    private CropOverlayView mCropOverlayView;
    private int mDegreesRotated = 0;
    private boolean mFixAspectRatio = false;
    private int mGuidelines = 1;
    private int mImageResource = 0;
    private ImageView mImageView;
    private int mLayoutHeight;
    private int mLayoutWidth;

    public CropImageView(Context context) {
        super(context);
        init(context);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt(DEGREES_ROTATED, this.mDegreesRotated);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (this.mBitmap != null) {
                int i = bundle.getInt(DEGREES_ROTATED);
                this.mDegreesRotated = i;
                int tempDegrees = this.mDegreesRotated;
                rotateImage(i);
                this.mDegreesRotated = tempDegrees;
            }
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            this.mCropOverlayView.setBitmapRect(ImageViewUtil.getBitmapRectCenterInside(bitmap, this));
        } else {
            this.mCropOverlayView.setBitmapRect(EMPTY_RECT);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight;
        int desiredWidth;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (this.mBitmap != null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (heightSize == 0) {
                heightSize = this.mBitmap.getHeight();
            }
            double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
            double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;
            if (widthSize < this.mBitmap.getWidth()) {
                double d = (double) widthSize;
                double width = (double) this.mBitmap.getWidth();
                Double.isNaN(d);
                Double.isNaN(width);
                viewToBitmapWidthRatio = d / width;
            }
            if (heightSize < this.mBitmap.getHeight()) {
                double d2 = (double) heightSize;
                double height = (double) this.mBitmap.getHeight();
                Double.isNaN(d2);
                Double.isNaN(height);
                viewToBitmapHeightRatio = d2 / height;
            }
            if (viewToBitmapWidthRatio == Double.POSITIVE_INFINITY && viewToBitmapHeightRatio == Double.POSITIVE_INFINITY) {
                desiredWidth = this.mBitmap.getWidth();
                desiredHeight = this.mBitmap.getHeight();
            } else if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                desiredWidth = widthSize;
                double height2 = (double) this.mBitmap.getHeight();
                Double.isNaN(height2);
                desiredHeight = (int) (height2 * viewToBitmapWidthRatio);
            } else {
                desiredHeight = heightSize;
                double width2 = (double) this.mBitmap.getWidth();
                Double.isNaN(width2);
                desiredWidth = (int) (width2 * viewToBitmapHeightRatio);
            }
            int width3 = getOnMeasureSpec(widthMode, widthSize, desiredWidth);
            int height3 = getOnMeasureSpec(heightMode, heightSize, desiredHeight);
            this.mLayoutWidth = width3;
            this.mLayoutHeight = height3;
            int i = widthMode;
            int i2 = heightMode;
            this.mCropOverlayView.setBitmapRect(ImageViewUtil.getBitmapRectCenterInside(this.mBitmap.getWidth(), this.mBitmap.getHeight(), this.mLayoutWidth, this.mLayoutHeight));
            setMeasuredDimension(this.mLayoutWidth, this.mLayoutHeight);
            return;
        }
        int i3 = heightMode;
        this.mCropOverlayView.setBitmapRect(EMPTY_RECT);
        setMeasuredDimension(widthSize, heightSize);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.mLayoutWidth > 0 && this.mLayoutHeight > 0) {
            ViewGroup.LayoutParams origparams = getLayoutParams();
            origparams.width = this.mLayoutWidth;
            origparams.height = this.mLayoutHeight;
            setLayoutParams(origparams);
        }
    }

    public int getImageResource() {
        return this.mImageResource;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mImageView.setImageBitmap(bitmap);
        CropOverlayView cropOverlayView = this.mCropOverlayView;
        if (cropOverlayView != null) {
            cropOverlayView.resetCropOverlayView();
        }
    }

    public void setImageBitmap(Bitmap bitmap, ExifInterface exif) {
        int rotate;
        if (bitmap == null) {
            return;
        }
        if (exif == null) {
            setImageBitmap(bitmap);
            return;
        }
        Matrix matrix = new Matrix();
        switch (exif.getAttributeInt("Orientation", 1)) {
            case 3:
                rotate = 180;
                break;
            case 6:
                rotate = 90;
                break;
            case 8:
                rotate = 270;
                break;
            default:
                rotate = -1;
                break;
        }
        if (rotate == -1) {
            setImageBitmap(bitmap);
            return;
        }
        matrix.postRotate((float) rotate);
        setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
        bitmap.recycle();
    }

    public void setImageResource(int resId) {
        if (resId != 0) {
            setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
        }
    }

    public Bitmap getCroppedImage() {
        Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(this.mBitmap, this.mImageView);
        float scaleFactorWidth = ((float) this.mBitmap.getWidth()) / ((float) displayedImageRect.width());
        float scaleFactorHeight = ((float) this.mBitmap.getHeight()) / ((float) displayedImageRect.height());
        return Bitmap.createBitmap(this.mBitmap, (int) ((Edge.LEFT.getCoordinate() - ((float) displayedImageRect.left)) * scaleFactorWidth), (int) ((Edge.TOP.getCoordinate() - ((float) displayedImageRect.top)) * scaleFactorHeight), (int) (Edge.getWidth() * scaleFactorWidth), (int) (Edge.getHeight() * scaleFactorHeight));
    }

    public RectF getActualCropRect() {
        Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(this.mBitmap, this.mImageView);
        float scaleFactorWidth = ((float) this.mBitmap.getWidth()) / ((float) displayedImageRect.width());
        float scaleFactorHeight = ((float) this.mBitmap.getHeight()) / ((float) displayedImageRect.height());
        float displayedCropLeft = Edge.LEFT.getCoordinate() - ((float) displayedImageRect.left);
        float actualCropLeft = displayedCropLeft * scaleFactorWidth;
        float actualCropTop = (Edge.TOP.getCoordinate() - ((float) displayedImageRect.top)) * scaleFactorHeight;
        return new RectF(Math.max(0.0f, actualCropLeft), Math.max(0.0f, actualCropTop), Math.min((float) this.mBitmap.getWidth(), (Edge.getWidth() * scaleFactorWidth) + actualCropLeft), Math.min((float) this.mBitmap.getHeight(), (Edge.getHeight() * scaleFactorHeight) + actualCropTop));
    }

    public void setFixedAspectRatio(boolean fixAspectRatio) {
        this.mCropOverlayView.setFixedAspectRatio(fixAspectRatio);
    }

    public void setGuidelines(int guidelines) {
        this.mCropOverlayView.setGuidelines(guidelines);
    }

    public void setAspectRatio(int aspectRatioX, int aspectRatioY) {
        this.mAspectRatioX = aspectRatioX;
        this.mCropOverlayView.setAspectRatioX(aspectRatioX);
        this.mAspectRatioY = aspectRatioY;
        this.mCropOverlayView.setAspectRatioY(aspectRatioY);
    }

    public void rotateImage(int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degrees);
        Bitmap bitmap = this.mBitmap;
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), this.mBitmap.getHeight(), matrix, true);
        this.mBitmap = createBitmap;
        setImageBitmap(createBitmap);
        int i = this.mDegreesRotated + degrees;
        this.mDegreesRotated = i;
        this.mDegreesRotated = i % 360;
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.crop_image_view, this, true);
        this.mImageView = (ImageView) v.findViewById(R.id.ImageView_image);
        setImageResource(this.mImageResource);
        CropOverlayView cropOverlayView = (CropOverlayView) v.findViewById(R.id.CropOverlayView);
        this.mCropOverlayView = cropOverlayView;
        cropOverlayView.setInitialAttributeValues(this.mGuidelines, this.mFixAspectRatio, this.mAspectRatioX, this.mAspectRatioY);
    }

    private static int getOnMeasureSpec(int measureSpecMode, int measureSpecSize, int desiredSize) {
        if (measureSpecMode == 1073741824) {
            return measureSpecSize;
        }
        if (measureSpecMode == Integer.MIN_VALUE) {
            return Math.min(desiredSize, measureSpecSize);
        }
        return desiredSize;
    }
}

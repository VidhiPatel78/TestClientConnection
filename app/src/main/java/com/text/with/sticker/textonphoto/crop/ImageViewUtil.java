package com.text.with.sticker.textonphoto.crop;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

public class ImageViewUtil {
    public static Rect getBitmapRectCenterInside(Bitmap bitmap, View view) {
        return getBitmapRectCenterInsideHelper(bitmap.getWidth(), bitmap.getHeight(), view.getWidth(), view.getHeight());
    }

    public static Rect getBitmapRectCenterInside(int bitmapWidth, int bitmapHeight, int viewWidth, int viewHeight) {
        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
    }

    private static Rect getBitmapRectCenterInsideHelper(int bitmapWidth, int bitmapHeight, int viewWidth, int viewHeight) {
        double resultWidth;
        double resultHeight;
        int resultY;
        int resultX;
        int i = bitmapWidth;
        int i2 = bitmapHeight;
        int i3 = viewWidth;
        int i4 = viewHeight;
        double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
        double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;
        if (i3 < i) {
            double d = (double) i3;
            double d2 = (double) i;
            Double.isNaN(d);
            Double.isNaN(d2);
            viewToBitmapWidthRatio = d / d2;
        }
        if (i4 < i2) {
            double d3 = (double) i4;
            double d4 = (double) i2;
            Double.isNaN(d3);
            Double.isNaN(d4);
            viewToBitmapHeightRatio = d3 / d4;
        }
        if (viewToBitmapWidthRatio == Double.POSITIVE_INFINITY && viewToBitmapHeightRatio == Double.POSITIVE_INFINITY) {
            resultHeight = (double) i2;
            resultWidth = (double) i;
        } else if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
            resultWidth = (double) i3;
            double d5 = (double) i2;
            Double.isNaN(d5);
            Double.isNaN(resultWidth);
            double d6 = (double) i;
            Double.isNaN(d6);
            resultHeight = (d5 * resultWidth) / d6;
        } else {
            resultHeight = (double) i4;
            double d7 = (double) i;
            Double.isNaN(d7);
            Double.isNaN(resultHeight);
            double d8 = (double) i2;
            Double.isNaN(d8);
            resultWidth = (d7 * resultHeight) / d8;
        }
        if (resultWidth == ((double) i3)) {
            resultX = 0;
            double d9 = (double) i4;
            Double.isNaN(d9);
            resultY = (int) Math.round((d9 - resultHeight) / 2.0d);
        } else if (resultHeight == ((double) i4)) {
            double d10 = (double) i3;
            Double.isNaN(d10);
            resultX = (int) Math.round((d10 - resultWidth) / 2.0d);
            resultY = 0;
        } else {
            double d11 = (double) i3;
            Double.isNaN(d11);
            resultX = (int) Math.round((d11 - resultWidth) / 2.0d);
            double d12 = (double) i4;
            Double.isNaN(d12);
            resultY = (int) Math.round((d12 - resultHeight) / 2.0d);
        }
        return new Rect(resultX, resultY, ((int) Math.ceil(resultWidth)) + resultX, ((int) Math.ceil(resultHeight)) + resultY);
    }
}

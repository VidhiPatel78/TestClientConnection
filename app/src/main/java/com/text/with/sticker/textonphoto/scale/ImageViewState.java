package com.text.with.sticker.textonphoto.scale;

import android.graphics.PointF;

import java.io.Serializable;

public class ImageViewState implements Serializable {
    private float centerX;
    private float centerY;
    private int orientation;
    private float scale;

    public ImageViewState(float scale2, PointF center, int orientation2) {
        this.scale = scale2;
        this.centerX = center.x;
        this.centerY = center.y;
        this.orientation = orientation2;
    }

    public float getScale() {
        return this.scale;
    }

    public PointF getCenter() {
        return new PointF(this.centerX, this.centerY);
    }

    public int getOrientation() {
        return this.orientation;
    }
}

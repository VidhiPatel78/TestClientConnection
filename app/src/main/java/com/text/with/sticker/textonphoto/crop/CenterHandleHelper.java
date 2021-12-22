package com.text.with.sticker.textonphoto.crop;

import android.graphics.Rect;

class CenterHandleHelper extends HandleHelper {
    CenterHandleHelper() {
        super((Edge) null, (Edge) null);
    }

    /* access modifiers changed from: package-private */
    public void updateCropWindow(float x, float y, Rect imageRect, float snapRadius) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float offsetX = x - ((left + Edge.RIGHT.getCoordinate()) / 2.0f);
        float offsetY = y - ((Edge.BOTTOM.getCoordinate() + top) / 2.0f);
        Edge.LEFT.offset(offsetX);
        Edge.TOP.offset(offsetY);
        Edge.RIGHT.offset(offsetX);
        Edge.BOTTOM.offset(offsetY);
        if (Edge.LEFT.isOutsideMargin(imageRect, snapRadius)) {
            Edge.RIGHT.offset(Edge.LEFT.snapToRect(imageRect));
        } else if (Edge.RIGHT.isOutsideMargin(imageRect, snapRadius)) {
            Edge.LEFT.offset(Edge.RIGHT.snapToRect(imageRect));
        }
        if (Edge.TOP.isOutsideMargin(imageRect, snapRadius)) {
            Edge.BOTTOM.offset(Edge.TOP.snapToRect(imageRect));
        } else if (Edge.BOTTOM.isOutsideMargin(imageRect, snapRadius)) {
            Edge.TOP.offset(Edge.BOTTOM.snapToRect(imageRect));
        }
    }

    /* access modifiers changed from: package-private */
    public void updateCropWindow(float x, float y, float targetAspectRatio, Rect imageRect, float snapRadius) {
        updateCropWindow(x, y, imageRect, snapRadius);
    }
}

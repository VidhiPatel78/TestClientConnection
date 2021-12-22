package com.text.with.sticker.textonphoto.crop;

import android.graphics.Rect;

class HorizontalHandleHelper extends HandleHelper {
    private Edge mEdge;

    HorizontalHandleHelper(Edge edge) {
        super(edge, (Edge) null);
        this.mEdge = edge;
    }

    /* access modifiers changed from: package-private */
    public void updateCropWindow(float x, float y, float targetAspectRatio, Rect imageRect, float snapRadius) {
        this.mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float halfDifference = (AspectRatioUtil.calculateWidth(top, Edge.BOTTOM.getCoordinate(), targetAspectRatio) - (right - left)) / 2.0f;
        Edge.LEFT.setCoordinate(left - halfDifference);
        Edge.RIGHT.setCoordinate(right + halfDifference);
        if (Edge.LEFT.isOutsideMargin(imageRect, snapRadius) && !this.mEdge.isNewRectangleOutOfBounds(Edge.LEFT, imageRect, targetAspectRatio)) {
            Edge.RIGHT.offset(-Edge.LEFT.snapToRect(imageRect));
            this.mEdge.adjustCoordinate(targetAspectRatio);
        }
        if (Edge.RIGHT.isOutsideMargin(imageRect, snapRadius) && !this.mEdge.isNewRectangleOutOfBounds(Edge.RIGHT, imageRect, targetAspectRatio)) {
            Edge.LEFT.offset(-Edge.RIGHT.snapToRect(imageRect));
            this.mEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}

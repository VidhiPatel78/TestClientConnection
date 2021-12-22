package com.text.with.sticker.textonphoto.crop;

import android.graphics.Rect;

class CornerHandleHelper extends HandleHelper {
    CornerHandleHelper(Edge horizontalEdge, Edge verticalEdge) {
        super(horizontalEdge, verticalEdge);
    }

    /* access modifiers changed from: package-private */
    public void updateCropWindow(float x, float y, float targetAspectRatio, Rect imageRect, float snapRadius) {
        EdgePair activeEdges = getActiveEdges(x, y, targetAspectRatio);
        Edge primaryEdge = activeEdges.primary;
        Edge secondaryEdge = activeEdges.secondary;
        primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);
        secondaryEdge.adjustCoordinate(targetAspectRatio);
        if (secondaryEdge.isOutsideMargin(imageRect, snapRadius)) {
            secondaryEdge.snapToRect(imageRect);
            primaryEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}

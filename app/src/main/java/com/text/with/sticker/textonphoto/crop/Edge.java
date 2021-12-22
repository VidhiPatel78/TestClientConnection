package com.text.with.sticker.textonphoto.crop;

import android.graphics.Rect;
import android.view.View;

public enum Edge {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM;
    
    public static final int MIN_CROP_LENGTH_PX = 40;
    private float mCoordinate;

    static class AnonymousClass1 {
        static int[] $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge;

        AnonymousClass1() {
        }

        static {
            $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge = null;
            int[] iArr = new int[Edge.values().length];
            $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge = iArr;
            try {
                iArr[Edge.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[Edge.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[Edge.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[Edge.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public void setCoordinate(float coordinate) {
        this.mCoordinate = coordinate;
    }

    public void offset(float distance) {
        this.mCoordinate += distance;
    }

    public float getCoordinate() {
        return this.mCoordinate;
    }

    public void adjustCoordinate(float x, float y, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                this.mCoordinate = adjustLeft(x, imageRect, imageSnapRadius, aspectRatio);
                return;
            case 2:
                this.mCoordinate = adjustTop(y, imageRect, imageSnapRadius, aspectRatio);
                return;
            case 3:
                this.mCoordinate = adjustRight(x, imageRect, imageSnapRadius, aspectRatio);
                return;
            case 4:
                this.mCoordinate = adjustBottom(y, imageRect, imageSnapRadius, aspectRatio);
                return;
            default:
                return;
        }
    }

    public void adjustCoordinate(float aspectRatio) {
        float left = LEFT.getCoordinate();
        float top = TOP.getCoordinate();
        float right = RIGHT.getCoordinate();
        float bottom = BOTTOM.getCoordinate();
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                this.mCoordinate = AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio);
                return;
            case 2:
                this.mCoordinate = AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio);
                return;
            case 3:
                this.mCoordinate = AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio);
                return;
            case 4:
                this.mCoordinate = AspectRatioUtil.calculateBottom(left, top, right, aspectRatio);
                return;
            default:
                return;
        }
    }

    public boolean isNewRectangleOutOfBounds(Edge edge, Rect imageRect, float aspectRatio) {
        float offset = edge.snapOffset(imageRect);
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                Edge edge2 = TOP;
                if (edge.equals(edge2)) {
                    float top = (float) imageRect.top;
                    float bottom = BOTTOM.getCoordinate() - offset;
                    float right = RIGHT.getCoordinate();
                    return isOutOfBounds(top, AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio), bottom, right, imageRect);
                } else if (!edge.equals(BOTTOM)) {
                    return true;
                } else {
                    float bottom2 = (float) imageRect.bottom;
                    float top2 = edge2.getCoordinate() - offset;
                    float right2 = RIGHT.getCoordinate();
                    return isOutOfBounds(top2, AspectRatioUtil.calculateLeft(top2, right2, bottom2, aspectRatio), bottom2, right2, imageRect);
                }
            case 2:
                Edge edge3 = LEFT;
                if (edge.equals(edge3)) {
                    float left = (float) imageRect.left;
                    float right3 = RIGHT.getCoordinate() - offset;
                    float bottom3 = BOTTOM.getCoordinate();
                    return isOutOfBounds(AspectRatioUtil.calculateTop(left, right3, bottom3, aspectRatio), left, bottom3, right3, imageRect);
                } else if (!edge.equals(RIGHT)) {
                    return true;
                } else {
                    float right4 = (float) imageRect.right;
                    float left2 = edge3.getCoordinate() - offset;
                    float bottom4 = BOTTOM.getCoordinate();
                    return isOutOfBounds(AspectRatioUtil.calculateTop(left2, right4, bottom4, aspectRatio), left2, bottom4, right4, imageRect);
                }
            case 3:
                Edge edge4 = TOP;
                if (edge.equals(edge4)) {
                    float top3 = (float) imageRect.top;
                    float bottom5 = BOTTOM.getCoordinate() - offset;
                    float left3 = LEFT.getCoordinate();
                    return isOutOfBounds(top3, left3, bottom5, AspectRatioUtil.calculateRight(left3, top3, bottom5, aspectRatio), imageRect);
                } else if (!edge.equals(BOTTOM)) {
                    return true;
                } else {
                    float bottom6 = (float) imageRect.bottom;
                    float top4 = edge4.getCoordinate() - offset;
                    float left4 = LEFT.getCoordinate();
                    return isOutOfBounds(top4, left4, bottom6, AspectRatioUtil.calculateRight(left4, top4, bottom6, aspectRatio), imageRect);
                }
            case 4:
                Edge edge5 = LEFT;
                if (edge.equals(edge5)) {
                    float left5 = (float) imageRect.left;
                    float right5 = RIGHT.getCoordinate() - offset;
                    float top5 = TOP.getCoordinate();
                    return isOutOfBounds(top5, left5, AspectRatioUtil.calculateBottom(left5, top5, right5, aspectRatio), right5, imageRect);
                } else if (!edge.equals(RIGHT)) {
                    return true;
                } else {
                    float right6 = (float) imageRect.right;
                    float left6 = edge5.getCoordinate() - offset;
                    float top6 = TOP.getCoordinate();
                    return isOutOfBounds(top6, left6, AspectRatioUtil.calculateBottom(left6, top6, right6, aspectRatio), right6, imageRect);
                }
            default:
                return true;
        }
    }

    private boolean isOutOfBounds(float top, float left, float bottom, float right, Rect imageRect) {
        return top < ((float) imageRect.top) || left < ((float) imageRect.left) || bottom > ((float) imageRect.bottom) || right > ((float) imageRect.right);
    }

    public float snapToRect(Rect imageRect) {
        float oldCoordinate = this.mCoordinate;
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                this.mCoordinate = (float) imageRect.left;
                break;
            case 2:
                this.mCoordinate = (float) imageRect.top;
                break;
            case 3:
                this.mCoordinate = (float) imageRect.right;
                break;
            case 4:
                this.mCoordinate = (float) imageRect.bottom;
                break;
        }
        return this.mCoordinate - oldCoordinate;
    }

    public float snapOffset(Rect imageRect) {
        float oldCoordinate = this.mCoordinate;
        float newCoordinate = oldCoordinate;
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                newCoordinate = (float) imageRect.left;
                break;
            case 2:
                newCoordinate = (float) imageRect.top;
                break;
            case 3:
                newCoordinate = (float) imageRect.right;
                break;
            case 4:
                newCoordinate = (float) imageRect.bottom;
                break;
        }
        return newCoordinate - oldCoordinate;
    }

    public void snapToView(View view) {
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                this.mCoordinate = 0.0f;
                return;
            case 2:
                this.mCoordinate = 0.0f;
                return;
            case 3:
                this.mCoordinate = (float) view.getWidth();
                return;
            case 4:
                this.mCoordinate = (float) view.getHeight();
                return;
            default:
                return;
        }
    }

    public static float getWidth() {
        return RIGHT.getCoordinate() - LEFT.getCoordinate();
    }

    public static float getHeight() {
        return BOTTOM.getCoordinate() - TOP.getCoordinate();
    }

    public boolean isOutsideMargin(Rect rect, float margin) {
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                if (this.mCoordinate - ((float) rect.left) < margin) {
                    return true;
                }
                return false;
            case 2:
                if (this.mCoordinate - ((float) rect.top) < margin) {
                    return true;
                }
                return false;
            case 3:
                if (((float) rect.right) - this.mCoordinate < margin) {
                    return true;
                }
                return false;
            case 4:
                return ((float) rect.bottom) - this.mCoordinate < margin;
            default:
                return false;
        }
    }

    public boolean isOutsideFrame(Rect rect) {
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Edge[ordinal()]) {
            case 1:
                if (((double) (this.mCoordinate - ((float) rect.left))) < 0.0d) {
                    return true;
                }
                return false;
            case 2:
                if (((double) (this.mCoordinate - ((float) rect.top))) < 0.0d) {
                    return true;
                }
                return false;
            case 3:
                if (((double) (((float) rect.right) - this.mCoordinate)) < 0.0d) {
                    return true;
                }
                return false;
            case 4:
                return ((double) (((float) rect.bottom) - this.mCoordinate)) < 0.0d;
            default:
                return false;
        }
    }

    private static float adjustLeft(float x, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultX = x;
        if (x - ((float) imageRect.left) < imageSnapRadius) {
            return (float) imageRect.left;
        }
        float resultXHoriz = Float.POSITIVE_INFINITY;
        float resultXVert = Float.POSITIVE_INFINITY;
        Edge edge = RIGHT;
        if (x >= edge.getCoordinate() - 40.0f) {
            resultXHoriz = edge.getCoordinate() - 40.0f;
        }
        if ((edge.getCoordinate() - x) / aspectRatio <= 40.0f) {
            resultXVert = edge.getCoordinate() - (40.0f * aspectRatio);
        }
        return Math.min(resultX, Math.min(resultXHoriz, resultXVert));
    }

    private static float adjustRight(float x, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultX = x;
        if (((float) imageRect.right) - x < imageSnapRadius) {
            return (float) imageRect.right;
        }
        float resultXHoriz = Float.NEGATIVE_INFINITY;
        float resultXVert = Float.NEGATIVE_INFINITY;
        Edge edge = LEFT;
        if (x <= edge.getCoordinate() + 40.0f) {
            resultXHoriz = edge.getCoordinate() + 40.0f;
        }
        if ((x - edge.getCoordinate()) / aspectRatio <= 40.0f) {
            resultXVert = edge.getCoordinate() + (40.0f * aspectRatio);
        }
        return Math.max(resultX, Math.max(resultXHoriz, resultXVert));
    }

    private static float adjustTop(float y, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultY = y;
        if (y - ((float) imageRect.top) < imageSnapRadius) {
            return (float) imageRect.top;
        }
        float resultYVert = Float.POSITIVE_INFINITY;
        float resultYHoriz = Float.POSITIVE_INFINITY;
        Edge edge = BOTTOM;
        if (y >= edge.getCoordinate() - 40.0f) {
            resultYHoriz = edge.getCoordinate() - 40.0f;
        }
        if ((edge.getCoordinate() - y) * aspectRatio <= 40.0f) {
            resultYVert = edge.getCoordinate() - (40.0f / aspectRatio);
        }
        return Math.min(resultY, Math.min(resultYHoriz, resultYVert));
    }

    private static float adjustBottom(float y, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultY = y;
        if (((float) imageRect.bottom) - y < imageSnapRadius) {
            return (float) imageRect.bottom;
        }
        float resultYVert = Float.NEGATIVE_INFINITY;
        float resultYHoriz = Float.NEGATIVE_INFINITY;
        Edge edge = TOP;
        if (y <= edge.getCoordinate() + 40.0f) {
            resultYVert = edge.getCoordinate() + 40.0f;
        }
        if ((y - edge.getCoordinate()) * aspectRatio <= 40.0f) {
            resultYHoriz = edge.getCoordinate() + (40.0f / aspectRatio);
        }
        return Math.max(resultY, Math.max(resultYHoriz, resultYVert));
    }
}

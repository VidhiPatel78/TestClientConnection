package com.text.with.sticker.textonphoto.crop;

import android.content.Context;
import android.util.Pair;
import android.util.TypedValue;

public class HandleUtil {
    private static final int TARGET_RADIUS_DP = 24;

    static class AnonymousClass1 {
        static final int[] $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle;

        AnonymousClass1() {
        }

        static {
            int[] iArr = new int[Handle.values().length];
            $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle = iArr;
            try {
                iArr[Handle.TOP_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.TOP_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.BOTTOM_LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.BOTTOM_RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.LEFT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.TOP.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.RIGHT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.BOTTOM.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[Handle.CENTER.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public static float getTargetRadius(Context context) {
        return TypedValue.applyDimension(1, 24.0f, context.getResources().getDisplayMetrics());
    }

    public static Handle getPressedHandle(float x, float y, float left, float top, float right, float bottom, float targetRadius) {
        if (isInCornerTargetZone(x, y, left, top, targetRadius)) {
            return Handle.TOP_LEFT;
        }
        if (isInCornerTargetZone(x, y, right, top, targetRadius)) {
            return Handle.TOP_RIGHT;
        }
        if (isInCornerTargetZone(x, y, left, bottom, targetRadius)) {
            return Handle.BOTTOM_LEFT;
        }
        if (isInCornerTargetZone(x, y, right, bottom, targetRadius)) {
            return Handle.BOTTOM_RIGHT;
        }
        if (isInCenterTargetZone(x, y, left, top, right, bottom) && focusCenter()) {
            return Handle.CENTER;
        }
        if (isInHorizontalTargetZone(x, y, left, right, top, targetRadius)) {
            return Handle.TOP;
        }
        if (isInHorizontalTargetZone(x, y, left, right, bottom, targetRadius)) {
            return Handle.BOTTOM;
        }
        if (isInVerticalTargetZone(x, y, left, top, bottom, targetRadius)) {
            return Handle.LEFT;
        }
        if (isInVerticalTargetZone(x, y, right, top, bottom, targetRadius)) {
            return Handle.RIGHT;
        }
        if (!isInCenterTargetZone(x, y, left, top, right, bottom) || focusCenter()) {
            return null;
        }
        return Handle.CENTER;
    }

    public static Pair<Float, Float> getOffset(Handle handle, float x, float y, float left, float top, float right, float bottom) {
        if (handle == null) {
            return null;
        }
        float touchOffsetX = 0.0f;
        float touchOffsetY = 0.0f;
        switch (AnonymousClass1.$SwitchMap$com$SimplyEntertaining$coolquotes$crop$Handle[handle.ordinal()]) {
            case 1:
                touchOffsetX = left - x;
                touchOffsetY = top - y;
                break;
            case 2:
                touchOffsetX = right - x;
                touchOffsetY = top - y;
                break;
            case 3:
                touchOffsetX = left - x;
                touchOffsetY = bottom - y;
                break;
            case 4:
                touchOffsetX = right - x;
                touchOffsetY = bottom - y;
                break;
            case 5:
                touchOffsetX = left - x;
                touchOffsetY = 0.0f;
                break;
            case 6:
                touchOffsetX = 0.0f;
                touchOffsetY = top - y;
                break;
            case 7:
                touchOffsetX = right - x;
                touchOffsetY = 0.0f;
                break;
            case 8:
                touchOffsetX = 0.0f;
                touchOffsetY = bottom - y;
                break;
            case 9:
                touchOffsetX = ((right + left) / 2.0f) - x;
                touchOffsetY = ((top + bottom) / 2.0f) - y;
                break;
        }
        return new Pair<>(Float.valueOf(touchOffsetX), Float.valueOf(touchOffsetY));
    }

    private static boolean isInCornerTargetZone(float x, float y, float handleX, float handleY, float targetRadius) {
        if (Math.abs(x - handleX) > targetRadius || Math.abs(y - handleY) > targetRadius) {
            return false;
        }
        return true;
    }

    private static boolean isInHorizontalTargetZone(float x, float y, float handleXStart, float handleXEnd, float handleY, float targetRadius) {
        if (x <= handleXStart || x >= handleXEnd || Math.abs(y - handleY) > targetRadius) {
            return false;
        }
        return true;
    }

    private static boolean isInVerticalTargetZone(float x, float y, float handleX, float handleYStart, float handleYEnd, float targetRadius) {
        if (Math.abs(x - handleX) > targetRadius || y <= handleYStart || y >= handleYEnd) {
            return false;
        }
        return true;
    }

    private static boolean isInCenterTargetZone(float x, float y, float left, float top, float right, float bottom) {
        if (x <= left || x >= right || y <= top || y >= bottom) {
            return false;
        }
        return true;
    }

    private static boolean focusCenter() {
        return !CropOverlayView.showGuidelines();
    }
}

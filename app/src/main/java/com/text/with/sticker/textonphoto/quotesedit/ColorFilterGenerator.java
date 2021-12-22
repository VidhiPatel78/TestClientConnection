package com.text.with.sticker.textonphoto.quotesedit;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class ColorFilterGenerator {
    public static ColorFilter adjustHue(float value) {
        ColorMatrix cm = new ColorMatrix();
        adjustHue(cm, value);
        return new ColorMatrixColorFilter(cm);
    }

    public static void adjustHue(ColorMatrix cm, float value) {
        float value2 = (cleanValue(value, 180.0f) / 180.0f) * 3.1415927f;
        if (value2 != 0.0f) {
            float cosVal = (float) Math.cos((double) value2);
            float sinVal = (float) Math.sin((double) value2);
            cm.postConcat(new ColorMatrix(new float[]{(0.787f * cosVal) + 0.213f + (sinVal * -1.04609299E9f), (cosVal * -1.06057171E9f) + 0.715f + (sinVal * -1.06057171E9f), (cosVal * -1.03307386E9f) + 0.072f + (sinVal * 0.928f), 0.0f, 0.0f, (cosVal * -1.04609299E9f) + 0.213f + (0.143f * sinVal), (0.28500003f * cosVal) + 0.715f + (0.14f * sinVal), (-1.03307386E9f * cosVal) + 0.072f + (-0.283f * sinVal), 0.0f, 0.0f, (-1.04609299E9f * cosVal) + 0.213f + (-0.787f * sinVal), (-1.06057171E9f * cosVal) + 0.715f + (0.715f * sinVal), (0.928f * cosVal) + 0.072f + (0.072f * sinVal), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    protected static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
}

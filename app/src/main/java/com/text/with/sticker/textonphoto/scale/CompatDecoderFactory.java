package com.text.with.sticker.textonphoto.scale;

import android.graphics.Bitmap;

import java.lang.reflect.InvocationTargetException;

public class CompatDecoderFactory<T> implements DecoderFactory<T> {
    private Bitmap.Config bitmapConfig;
    private Class<? extends T> clazz;

    public CompatDecoderFactory(Class<? extends T> clazz2) {
        this(clazz2, null);
    }

    public CompatDecoderFactory(Class<? extends T> clazz2, Bitmap.Config bitmapConfig2) {
        this.clazz = clazz2;
        this.bitmapConfig = bitmapConfig2;
    }

    @Override // com.textonphoto.customqoutescreator.scale.DecoderFactory
    public T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (this.bitmapConfig == null) {
            return (T) this.clazz.newInstance();
        }
        return (T) this.clazz.getConstructor(Bitmap.Config.class).newInstance(this.bitmapConfig);
    }
}

package com.text.with.sticker.textonphoto.scale;

import java.lang.reflect.InvocationTargetException;

public interface DecoderFactory<T> {
    T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;
}

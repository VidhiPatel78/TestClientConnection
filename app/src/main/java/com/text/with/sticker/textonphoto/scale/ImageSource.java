package com.text.with.sticker.textonphoto.scale;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;

import com.bumptech.glide.load.Key;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class ImageSource {
    static final String ASSET_SCHEME = "file:///android_asset/";
    static final String FILE_SCHEME = "file:///";
    private final Bitmap bitmap;
    private boolean cached;
    private final Integer resource;
    private int sHeight;
    private Rect sRegion;
    private int sWidth;
    private boolean tile;
    private final Uri uri;

    private ImageSource(Bitmap bitmap2, boolean cached2) {
        this.bitmap = bitmap2;
        this.uri = null;
        this.resource = null;
        this.tile = false;
        this.sWidth = bitmap2.getWidth();
        this.sHeight = bitmap2.getHeight();
        this.cached = cached2;
    }

    private ImageSource(Uri uri2) {
        String uriString = uri2.toString();
        if (uriString.startsWith(FILE_SCHEME) && !new File(uriString.substring(FILE_SCHEME.length() - 1)).exists()) {
            try {
                uri2 = Uri.parse(URLDecoder.decode(uriString, Key.STRING_CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
            }
        }
        this.bitmap = null;
        this.uri = uri2;
        this.resource = null;
        this.tile = true;
    }

    private ImageSource(int resource2) {
        this.bitmap = null;
        this.uri = null;
        this.resource = Integer.valueOf(resource2);
        this.tile = true;
    }

    public static ImageSource resource(int resId) {
        return new ImageSource(resId);
    }

    public static ImageSource asset(String assetName) {
        if (assetName != null) {
            return uri(ASSET_SCHEME + assetName);
        }
        throw new NullPointerException("Asset name must not be null");
    }

    public static ImageSource uri(String uri2) {
        if (uri2 != null) {
            if (!uri2.contains("://")) {
                if (uri2.startsWith("/")) {
                    uri2 = uri2.substring(1);
                }
                uri2 = FILE_SCHEME + uri2;
            }
            return new ImageSource(Uri.parse(uri2));
        }
        throw new NullPointerException("Uri must not be null");
    }

    public static ImageSource uri(Uri uri2) {
        if (uri2 != null) {
            return new ImageSource(uri2);
        }
        throw new NullPointerException("Uri must not be null");
    }

    public static ImageSource bitmap(Bitmap bitmap2) {
        if (bitmap2 != null) {
            return new ImageSource(bitmap2, false);
        }
        throw new NullPointerException("Bitmap must not be null");
    }

    public static ImageSource cachedBitmap(Bitmap bitmap2) {
        if (bitmap2 != null) {
            return new ImageSource(bitmap2, true);
        }
        throw new NullPointerException("Bitmap must not be null");
    }

    public ImageSource tilingEnabled() {
        return tiling(true);
    }

    public ImageSource tilingDisabled() {
        return tiling(false);
    }

    public ImageSource tiling(boolean tile2) {
        this.tile = tile2;
        return this;
    }

    public ImageSource region(Rect sRegion2) {
        this.sRegion = sRegion2;
        setInvariants();
        return this;
    }

    public ImageSource dimensions(int sWidth2, int sHeight2) {
        if (this.bitmap == null) {
            this.sWidth = sWidth2;
            this.sHeight = sHeight2;
        }
        setInvariants();
        return this;
    }

    private void setInvariants() {
        Rect rect = this.sRegion;
        if (rect != null) {
            this.tile = true;
            this.sWidth = rect.width();
            this.sHeight = this.sRegion.height();
        }
    }

    /* access modifiers changed from: protected */
    public final Uri getUri() {
        return this.uri;
    }

    /* access modifiers changed from: protected */
    public final Bitmap getBitmap() {
        return this.bitmap;
    }

    /* access modifiers changed from: protected */
    public final Integer getResource() {
        return this.resource;
    }

    /* access modifiers changed from: protected */
    public final boolean getTile() {
        return this.tile;
    }

    /* access modifiers changed from: protected */
    public final int getSWidth() {
        return this.sWidth;
    }

    /* access modifiers changed from: protected */
    public final int getSHeight() {
        return this.sHeight;
    }

    /* access modifiers changed from: protected */
    public final Rect getSRegion() {
        return this.sRegion;
    }

    /* access modifiers changed from: protected */
    public final boolean isCached() {
        return this.cached;
    }
}

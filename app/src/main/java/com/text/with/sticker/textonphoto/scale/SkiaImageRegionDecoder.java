package com.text.with.sticker.textonphoto.scale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SkiaImageRegionDecoder implements ImageRegionDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private final Bitmap.Config bitmapConfig;
    private BitmapRegionDecoder decoder;
    private final ReadWriteLock decoderLock;

    public SkiaImageRegionDecoder() {
        this(null);
    }

    public SkiaImageRegionDecoder(Bitmap.Config bitmapConfig2) {
        this.decoderLock = new ReentrantReadWriteLock(true);
        Bitmap.Config globalBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (bitmapConfig2 != null) {
            this.bitmapConfig = bitmapConfig2;
        } else if (globalBitmapConfig != null) {
            this.bitmapConfig = globalBitmapConfig;
        } else {
            this.bitmapConfig = Bitmap.Config.RGB_565;
        }
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public Point init(Context context, Uri uri) throws Exception {
        Resources res;
        String uriString = uri.toString();
        if (uriString.startsWith(RESOURCE_PREFIX)) {
            String packageName = uri.getAuthority();
            if (context.getPackageName().equals(packageName)) {
                res = context.getResources();
            } else {
                res = context.getPackageManager().getResourcesForApplication(packageName);
            }
            int id = 0;
            List<String> segments = uri.getPathSegments();
            int size = segments.size();
            if (size == 2 && segments.get(0).equals("drawable")) {
                id = res.getIdentifier(segments.get(1), "drawable", packageName);
            } else if (size == 1 && TextUtils.isDigitsOnly(segments.get(0))) {
                try {
                    id = Integer.parseInt(segments.get(0));
                } catch (NumberFormatException e) {
                }
            }
            this.decoder = BitmapRegionDecoder.newInstance(context.getResources().openRawResource(id), false);
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            this.decoder = BitmapRegionDecoder.newInstance(context.getAssets().open(uriString.substring(ASSET_PREFIX.length()), 1), false);
        } else if (uriString.startsWith(FILE_PREFIX)) {
            this.decoder = BitmapRegionDecoder.newInstance(uriString.substring(FILE_PREFIX.length()), false);
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                this.decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e3) {
                    }
                }
            }
        }
        return new Point(this.decoder.getWidth(), this.decoder.getHeight());
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public Bitmap decodeRegion(Rect sRect, int sampleSize) {
        getDecodeLock().lock();
        try {
            BitmapRegionDecoder bitmapRegionDecoder = this.decoder;
            if (bitmapRegionDecoder == null || bitmapRegionDecoder.isRecycled()) {
                throw new IllegalStateException("Cannot decode region after decoder has been recycled");
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            options.inPreferredConfig = this.bitmapConfig;
            Bitmap bitmap = this.decoder.decodeRegion(sRect, options);
            if (bitmap != null) {
                return bitmap;
            }
            throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
        } finally {
            getDecodeLock().unlock();
        }
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public synchronized boolean isReady() {
        BitmapRegionDecoder bitmapRegionDecoder;
        bitmapRegionDecoder = this.decoder;
        return bitmapRegionDecoder != null && !bitmapRegionDecoder.isRecycled();
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public synchronized void recycle() {
        this.decoderLock.writeLock().lock();
        try {
            this.decoder.recycle();
            this.decoder = null;
        } finally {
            this.decoderLock.writeLock().unlock();
        }
    }

    private Lock getDecodeLock() {
        if (Build.VERSION.SDK_INT < 21) {
            return this.decoderLock.writeLock();
        }
        return this.decoderLock.readLock();
    }
}

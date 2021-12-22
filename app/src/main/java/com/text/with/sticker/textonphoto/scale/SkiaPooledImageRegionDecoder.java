package com.text.with.sticker.textonphoto.scale;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;

import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

public class SkiaPooledImageRegionDecoder implements ImageRegionDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private static final String TAG = SkiaPooledImageRegionDecoder.class.getSimpleName();
    private static boolean debug = false;
    private final Bitmap.Config bitmapConfig;
    private Context context;
    private final ReadWriteLock decoderLock;
    private DecoderPool decoderPool;
    private long fileLength;
    private Point imageDimensions;
    private final AtomicBoolean lazyInited;
    private Uri uri;

    /* access modifiers changed from: private */
    public static class DecoderPool {
        private final Semaphore available;
        private Map<BitmapRegionDecoder, Boolean> decoders;

        private DecoderPool() {
            this.available = new Semaphore(0, true);
            this.decoders = new ConcurrentHashMap();
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private synchronized boolean isEmpty() {
            return this.decoders.isEmpty();
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private synchronized int size() {
            return this.decoders.size();
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private BitmapRegionDecoder acquire() {
            this.available.acquireUninterruptibly();
            return getNextAvailable();
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void release(BitmapRegionDecoder decoder) {
            if (markAsUnused(decoder)) {
                this.available.release();
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private synchronized void add(BitmapRegionDecoder decoder) {
            this.decoders.put(decoder, false);
            this.available.release();
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private synchronized void recycle() {
            while (!this.decoders.isEmpty()) {
                BitmapRegionDecoder decoder = acquire();
                decoder.recycle();
                this.decoders.remove(decoder);
            }
        }

        private synchronized BitmapRegionDecoder getNextAvailable() {
            Iterator<Map.Entry<BitmapRegionDecoder, Boolean>> it = this.decoders.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<BitmapRegionDecoder, Boolean> entry = it.next();
                if (!entry.getValue().booleanValue()) {
                    entry.setValue(true);
                    entry.getKey();
                    break;
                }
            }
            return null;
        }

        private synchronized boolean markAsUnused(BitmapRegionDecoder decoder) {
            for (Map.Entry<BitmapRegionDecoder, Boolean> entry : this.decoders.entrySet()) {
                if (decoder == entry.getKey() && entry.getValue().booleanValue()) {
                    entry.setValue(false);
                }
            }
            return false;
        }
    }

    public SkiaPooledImageRegionDecoder() {
        this(null);
    }

    public SkiaPooledImageRegionDecoder(Bitmap.Config bitmapConfig2) {
        this.decoderPool = new DecoderPool();
        this.decoderLock = new ReentrantReadWriteLock(true);
        this.fileLength = Long.MAX_VALUE;
        this.imageDimensions = new Point(0, 0);
        this.lazyInited = new AtomicBoolean(false);
        Bitmap.Config globalBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (bitmapConfig2 != null) {
            this.bitmapConfig = bitmapConfig2;
        } else if (globalBitmapConfig != null) {
            this.bitmapConfig = globalBitmapConfig;
        } else {
            this.bitmapConfig = Bitmap.Config.RGB_565;
        }
    }

    public static void setDebug(boolean debug2) {
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public Point init(Context context2, Uri uri2) throws Exception {
        this.context = context2;
        this.uri = uri2;
        initialiseDecoder();
        return this.imageDimensions;
    }

    private void lazyInit() {
        if (this.lazyInited.compareAndSet(false, true) && this.fileLength < Long.MAX_VALUE) {
            debug("Starting lazy init of additional decoders");
            new Thread() {
                /* class com.textonphoto.customqoutescreator.scale.SkiaPooledImageRegionDecoder.AnonymousClass1 */

                public void run() {
                    while (SkiaPooledImageRegionDecoder.this.decoderPool != null) {
                        SkiaPooledImageRegionDecoder skiaPooledImageRegionDecoder = SkiaPooledImageRegionDecoder.this;
                        if (skiaPooledImageRegionDecoder.allowAdditionalDecoder(skiaPooledImageRegionDecoder.decoderPool.size(), SkiaPooledImageRegionDecoder.this.fileLength)) {
                            try {
                                if (SkiaPooledImageRegionDecoder.this.decoderPool != null) {
                                    long start = System.currentTimeMillis();
                                    SkiaPooledImageRegionDecoder.this.debug("Starting decoder");
                                    SkiaPooledImageRegionDecoder.this.initialiseDecoder();
                                    SkiaPooledImageRegionDecoder skiaPooledImageRegionDecoder2 = SkiaPooledImageRegionDecoder.this;
                                    skiaPooledImageRegionDecoder2.debug("Started decoder, took " + (System.currentTimeMillis() - start) + "ms");
                                }
                            } catch (Exception e) {
                                SkiaPooledImageRegionDecoder skiaPooledImageRegionDecoder3 = SkiaPooledImageRegionDecoder.this;
                                skiaPooledImageRegionDecoder3.debug("Failed to start decoder: " + e.getMessage());
                            }
                        } else {
                            return;
                        }
                    }
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initialiseDecoder() throws Exception {
        Resources res;
        BitmapRegionDecoder decoder = null;
        String uriString = this.uri.toString();
        long fileLength2 = Long.MAX_VALUE;
        if (uriString.startsWith(RESOURCE_PREFIX)) {
            String packageName = this.uri.getAuthority();
            if (this.context.getPackageName().equals(packageName)) {
                res = this.context.getResources();
            } else {
                res = this.context.getPackageManager().getResourcesForApplication(packageName);
            }
            int id = 0;
            List<String> segments = this.uri.getPathSegments();
            int size = segments.size();
            if (size == 2 && segments.get(0).equals("drawable")) {
                id = res.getIdentifier(segments.get(1), "drawable", packageName);
            } else if (size == 1 && TextUtils.isDigitsOnly(segments.get(0))) {
                try {
                    id = Integer.parseInt(segments.get(0));
                } catch (NumberFormatException e) {
                }
            }
            try {
                fileLength2 = this.context.getResources().openRawResourceFd(id).getLength();
            } catch (Exception e2) {
            }
            decoder = BitmapRegionDecoder.newInstance(this.context.getResources().openRawResource(id), false);
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            String assetName = uriString.substring(ASSET_PREFIX.length());
            try {
                fileLength2 = this.context.getAssets().openFd(assetName).getLength();
            } catch (Exception e3) {
            }
            decoder = BitmapRegionDecoder.newInstance(this.context.getAssets().open(assetName, 1), false);
        } else if (uriString.startsWith(FILE_PREFIX)) {
            decoder = BitmapRegionDecoder.newInstance(uriString.substring(FILE_PREFIX.length()), false);
            try {
                File file = new File(uriString);
                if (file.exists()) {
                    fileLength2 = file.length();
                }
            } catch (Exception e4) {
            }
        } else {
            InputStream inputStream = null;
            try {
                ContentResolver contentResolver = this.context.getContentResolver();
                inputStream = contentResolver.openInputStream(this.uri);
                decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                try {
                    AssetFileDescriptor descriptor = contentResolver.openAssetFileDescriptor(this.uri, "r");
                    if (descriptor != null) {
                        fileLength2 = descriptor.getLength();
                    }
                } catch (Exception e5) {
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e6) {
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e7) {
                    }
                }
            }
        }
        this.fileLength = fileLength2;
        this.imageDimensions.set(decoder.getWidth(), decoder.getHeight());
        this.decoderLock.writeLock().lock();
        try {
            DecoderPool decoderPool2 = this.decoderPool;
            if (decoderPool2 != null) {
                decoderPool2.add(decoder);
            }
            this.decoderLock.writeLock().unlock();
        } catch (Throwable th2) {
            this.decoderLock.writeLock().unlock();
        }
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public Bitmap decodeRegion(Rect sRect, int sampleSize) {
        debug("Decode region " + sRect + " on thread " + Thread.currentThread().getName());
        if (sRect.width() < this.imageDimensions.x || sRect.height() < this.imageDimensions.y) {
            lazyInit();
        }
        this.decoderLock.readLock().lock();
        try {
            DecoderPool decoderPool2 = this.decoderPool;
            if (decoderPool2 != null) {
                BitmapRegionDecoder decoder = decoderPool2.acquire();
                if (decoder != null && !decoder.isRecycled()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = sampleSize;
                    options.inPreferredConfig = this.bitmapConfig;
                    Bitmap bitmap = decoder.decodeRegion(sRect, options);
                    if (bitmap != null) {
                        if (decoder != null) {
                            this.decoderPool.release(decoder);
                        }
                        this.decoderLock.readLock().unlock();
                        return bitmap;
                    }
                    throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
                } else if (decoder != null) {
                    this.decoderPool.release(decoder);
                }
            }
            throw new IllegalStateException("Cannot decode region after decoder has been recycled");
        } catch (Throwable th) {
            this.decoderLock.readLock().unlock();
            return null;
        }
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public synchronized boolean isReady() {
        DecoderPool decoderPool2;
        decoderPool2 = this.decoderPool;
        return decoderPool2 != null && !decoderPool2.isEmpty();
    }

    @Override // com.textonphoto.customqoutescreator.scale.ImageRegionDecoder
    public synchronized void recycle() {
        Throwable th;
        this.decoderLock.writeLock().lock();
        try {
            DecoderPool decoderPool2 = this.decoderPool;
            if (decoderPool2 != null) {
                try {
                    decoderPool2.recycle();
                    this.decoderPool = null;
                    this.context = null;
                    this.uri = null;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            this.decoderLock.writeLock().unlock();
        } catch (Throwable th3) {
            th = th3;
            this.decoderLock.writeLock().unlock();
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean allowAdditionalDecoder(int numberOfDecoders, long fileLength2) {
        if (numberOfDecoders >= 4) {
            debug("No additional decoders allowed, reached hard limit (4)");
            return false;
        } else if (((long) numberOfDecoders) * fileLength2 > 20971520) {
            debug("No additional encoders allowed, reached hard memory limit (20Mb)");
            return false;
        } else if (numberOfDecoders >= getNumberOfCores()) {
            debug("No additional encoders allowed, limited by CPU cores (" + getNumberOfCores() + ")");
            return false;
        } else if (isLowMemory()) {
            debug("No additional encoders allowed, memory is low");
            return false;
        } else {
            debug("Additional decoder allowed, current count is " + numberOfDecoders + ", estimated native memory " + ((((long) numberOfDecoders) * fileLength2) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "Mb");
            return true;
        }
    }

    private int getNumberOfCores() {
        if (Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }
        return getNumCoresOldPhones();
    }

    private int getNumCoresOldPhones() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                /* class com.textonphoto.customqoutescreator.scale.SkiaPooledImageRegionDecoder.AnonymousClass2 */

                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]+", pathname.getName());
                }
            }).length;
        } catch (Exception e) {
            return 1;
        }
    }

    private boolean isLowMemory() {
        @SuppressLint("WrongConstant") ActivityManager activityManager = (ActivityManager) this.context.getSystemService("activity");
        if (activityManager == null) {
            return true;
        }
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void debug(String message) {
        if (debug) {
            Log.d(TAG, message);
        }
    }
}

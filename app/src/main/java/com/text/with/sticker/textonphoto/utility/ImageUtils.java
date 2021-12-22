package com.text.with.sticker.textonphoto.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.WindowManager;


import com.text.with.sticker.textonphoto.quotesedit.CustomTypefaceSpan;

import java.io.FileDescriptor;
import java.io.IOException;

public class ImageUtils {
    public static Bitmap getBitmapFromUri(Context context, Uri uri, float screenWidth, float screenHeight) throws IOException {
        OutOfMemoryError e;
        float screenWidth2;
        int rotation;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, bfo);
            BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
            if (screenWidth <= screenHeight) {
                screenWidth2 = screenHeight;
            } else {
                screenWidth2 = screenWidth;
            }
            int maxDim = (int) screenWidth2;
            try {
                optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, optsDownSample);
                Matrix m = new Matrix();
                if (image.getWidth() > maxDim || image.getHeight() > maxDim) {
                    BitmapFactory.Options optsScale = getResampling(image.getWidth(), image.getHeight(), maxDim);
                    m.postScale(((float) optsScale.outWidth) / ((float) image.getWidth()), ((float) optsScale.outHeight) / ((float) image.getHeight()));
                }
                String pathInput = getRealPathFromURI(uri, context);
                if (new Integer(Build.VERSION.SDK).intValue() > 4 && (rotation = ExifUtils.getExifRotation(pathInput)) != 0) {
                    m.postRotate((float) rotation);
                }
                Bitmap image2 = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);
                parcelFileDescriptor.close();
                return image2;
            } catch (OutOfMemoryError e2) {
                e = e2;
                e.printStackTrace();
                return null;
            }
        } catch (OutOfMemoryError e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context, int maxDim) throws IOException {
        try {
            return resampleImage(getRealPathFromURI(uri, context), maxDim);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context) throws IOException {
        String pathInput = getRealPathFromURI(uri, context);
        try {
            @SuppressLint("WrongConstant") Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int i = size.y;
            return resampleImage(pathInput, width);
        } catch (Exception e) {
            e.printStackTrace();
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(pathInput));
        }
    }

    public static Bitmap resampleImage(String path, int maxDim) throws Exception {
        int rotation;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bfo);
        BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
        optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);
        Bitmap bmpt = BitmapFactory.decodeFile(path, optsDownSample);
        Matrix m = new Matrix();
        if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
            BitmapFactory.Options optsScale = getResampling(bmpt.getWidth(), bmpt.getHeight(), maxDim);
            m.postScale(((float) optsScale.outWidth) / ((float) bmpt.getWidth()), ((float) optsScale.outHeight) / ((float) bmpt.getHeight()));
        }
        if (new Integer(Build.VERSION.SDK).intValue() > 4 && (rotation = ExifUtils.getExifRotation(path)) != 0) {
            m.postRotate((float) rotation);
        }
        return Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(), bmpt.getHeight(), m, true);
    }

    public static BitmapFactory.Options getResampling(int cx, int cy, int max) {
        float scaleVal;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        if (cx > cy) {
            scaleVal = ((float) max) / ((float) cx);
        } else if (cy > cx) {
            scaleVal = ((float) max) / ((float) cy);
        } else {
            scaleVal = ((float) max) / ((float) cx);
        }
        bfo.outWidth = (int) ((((float) cx) * scaleVal) + 0.5f);
        bfo.outHeight = (int) ((((float) cy) * scaleVal) + 0.5f);
        return bfo;
    }

    public static int getClosestResampleSize(int cx, int cy, int maxDim) {
        int max = Math.max(cx, cy);
        int resample = 1;
        while (true) {
            if (resample >= Integer.MAX_VALUE) {
                break;
            } else if (resample * maxDim > max) {
                resample--;
                break;
            } else {
                resample++;
            }
        }
        if (resample > 0) {
            return resample;
        }
        return 1;
    }

    public static BitmapFactory.Options getBitmapDims(String path) throws Exception {
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bfo);
        return bfo;
    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                return contentURI.getPath();
            }
            cursor.moveToFirst();
            @SuppressLint("Range") String result = cursor.getString(cursor.getColumnIndex("_data"));
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return contentURI.toString();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bit, int width, int height) {
        float he;
        float wd;
        if (bit == null) {
            return null;
        }
        float wr = (float) width;
        float hr = (float) height;
        float wd2 = (float) bit.getWidth();
        float he2 = (float) bit.getHeight();
        float rat1 = wd2 / he2;
        float rat2 = he2 / wd2;
        if (wd2 > wr) {
            if (wr * rat2 > hr) {
                he = hr;
                wd = he * rat1;
            } else {
                wd = wr;
                he = wd * rat2;
            }
        } else if (he2 > hr) {
            he = hr;
            wd = he * rat1;
            if (wd > wr) {
                wd = wr;
                he = wd * rat2;
            }
        } else if (rat1 > 0.75f) {
            if (wr * rat2 > hr) {
                he = hr;
                wd = he * rat1;
            } else {
                wd = wr;
                he = wd * rat2;
            }
        } else if (rat2 > 1.5f) {
            he = hr;
            wd = he * rat1;
            if (wd > wr) {
                wd = wr;
                he = wd * rat2;
            }
        } else if (wr * rat2 > hr) {
            he = hr;
            wd = he * rat1;
        } else {
            wd = wr;
            he = wd * rat2;
        }
        return Bitmap.createScaledBitmap(bit, (int) wd, (int) he, false);
    }

    public static int dpToPx(Context c, int dp) {
        c.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) dp));
    }

    public static float pxToDp(Context context, float px) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static Bitmap bitmapmasking(Bitmap original, Bitmap mask) {
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawBitmap(original, 0.0f, 0.0f, (Paint) null);
        mCanvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return result;
    }

    public static Bitmap getThumbnail(Bitmap bitmap, int width, int height) {
        Bitmap b;
        Bitmap b2 = bitmap.copy(bitmap.getConfig(), true);
        int w = b2.getWidth();
        int h = b2.getHeight();
        if (h > w) {
            b = cropCenterBitmap(b2, w, w);
        } else {
            b = cropCenterBitmap(b2, h, h);
        }
        return Bitmap.createScaledBitmap(b, width, height, true);
    }

    public static CharSequence getSpannableString(Context ctx, Typeface ttf, int stringId) {
        SpannableString spannableString = new SpannableString(ctx.getResources().getString(stringId));
        spannableString.setSpan(new CustomTypefaceSpan(ttf), 0, ctx.getResources().getString(stringId).length(), 0);
        SpannableStringBuilder builder = new SpannableStringBuilder().append((CharSequence) spannableString);
        return builder.subSequence(0, builder.length());
    }

    public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        if (width < w && height < h) {
            return src;
        }
        int x = 0;
        int y = 0;
        if (width > w) {
            x = (width - w) / 2;
        }
        if (height > h) {
            y = (height - h) / 2;
        }
        int cw = w;
        int ch = h;
        if (w > width) {
            cw = width;
        }
        if (h > height) {
            ch = height;
        }
        return Bitmap.createBitmap(src, x, y, cw, ch);
    }

    public static Bitmap mergelogo(Bitmap bitmap, Bitmap logo) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        float wr = (float) bitmap.getWidth();
        float hr = (float) bitmap.getHeight();
        float wd = (float) logo.getWidth();
        float he = (float) logo.getHeight();
        float rat1 = wd / he;
        float rat2 = he / wd;
        if (wd > wr) {
            logo = Bitmap.createScaledBitmap(logo, (int) wr, (int) (wr * rat2), false);
        } else if (he > hr) {
            logo = Bitmap.createScaledBitmap(logo, (int) (hr * rat1), (int) hr, false);
        }
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(logo, (float) (bitmap.getWidth() - logo.getWidth()), (float) (bitmap.getHeight() - logo.getHeight()), (Paint) null);
        return result;
    }

    public static Bitmap CropBitmapTransparency(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                if (((sourceBitmap.getPixel(x, y) >> 24) & 255) > 0) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
        if (maxX < minX || maxY < minY) {
            return null;
        }
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        if (reqWidth <= 0) {
            reqWidth = 1;
        }
        if (reqHeight <= 0) {
            reqHeight = 1;
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}

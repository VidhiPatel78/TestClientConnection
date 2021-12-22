package com.text.with.sticker.textonphoto.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.text.with.sticker.textonphoto.R;

import java.io.File;
import java.io.IOException;

public class Constants {
    public static String[] backgroundCataDrawable = {"love", "getwellsoon", "specialdays", "sympathy", "wedding", "motivation", "congratulations", "funny", "friendship", "birthday", "religiousevent", "family", "thankyou", "justbecause", "emoji", "famouspeople"};

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

    public static boolean deleteFile(Context context, String filePath) {
        boolean bl = false;
        try {
            File file = new File(Uri.parse(filePath).getPath());
            bl = file.delete();
            if (file.exists()) {
                try {
                    bl = file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    bl = context.getApplicationContext().deleteFile(file.getName());
                }
            }
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e2) {
            Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
        return bl;
    }

    public static int getStart(String str, int focus) {
        for (int i = focus; i > 0; i--) {
            try {
                if (focus != str.length()) {
                    Log.e("tag is", "" + i);
                    if (Character.isWhitespace(str.charAt(i))) {
                        return i + 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static int getEnd(String str, int focus) {
        int i = focus;
        while (i <= str.length()) {
            try {
                if (Character.isWhitespace(str.charAt(i)) || str.length() == i) {
                    return i;
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str.length();
    }

    public static int dpToPx(Context c, int dp) {
        c.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) dp));
    }

    public static Typeface getTextTypeface(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "DroidSans.ttf");
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

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static void usePremimum_dialog(Activity activity) {
       /* final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.failed_ads_dialog);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        btn_ok.setTypeface(getTextTypeface(activity));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            *//* class com.textonphoto.customqoutescreator.utility.Constants.AnonymousClass1 *//*

            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();*/
    }
}

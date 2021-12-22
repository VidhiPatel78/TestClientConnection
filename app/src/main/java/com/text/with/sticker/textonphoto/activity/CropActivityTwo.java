package com.text.with.sticker.textonphoto.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.crop.CropImageView;
import com.text.with.sticker.textonphoto.interfacelistner.OnGetImgBackgndSelect;
import com.text.with.sticker.textonphoto.utility.Constants;
import com.text.with.sticker.textonphoto.utility.ImageUtils;

import java.io.IOException;

public class CropActivityTwo extends Activity {
    public static Bitmap bitmapImage;
    Animation bottomDown;
    Animation bottomUp;
    CropImageView cropimage;
    Button custom;
    Button done;
    RelativeLayout footer;
    OnGetImgBackgndSelect getSticker;
    RelativeLayout header;
    SharedPreferences preferences;
    Button ratio1;
    Button ratio10;
    Button ratio11;
    Button ratio12;
    Button ratio13;
    Button ratio14;
    Button ratio15;
    Button ratio2;
    Button ratio3;
    Button ratio4;
    Button ratio5;
    Button ratio6;
    Button ratio7;
    Button ratio8;
    Button ratio9;
    RelativeLayout rel;
    float screenHeight;
    float screenWidth;
    Button square;
    String value;

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_crop);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);
        this.screenWidth = (float) dimension.widthPixels;
        this.screenHeight = (float) (dimension.heightPixels - Constants.dpToPx(this, 50));
        this.getSticker = AddTextQuotesActivity.c;
        this.header = (RelativeLayout) findViewById(R.id.header);
        this.rel = (RelativeLayout) findViewById(R.id.rel);
        this.footer = (RelativeLayout) findViewById(R.id.footer);
        this.cropimage = (CropImageView) findViewById(R.id.cropimage);
        this.done = (Button) findViewById(R.id.done);
        this.custom = (Button) findViewById(R.id.cutom);
        this.square = (Button) findViewById(R.id.square);
        this.ratio1 = (Button) findViewById(R.id.ratio1);
        this.ratio2 = (Button) findViewById(R.id.ratio2);
        this.ratio3 = (Button) findViewById(R.id.ratio3);
        this.ratio4 = (Button) findViewById(R.id.ratio4);
        this.ratio5 = (Button) findViewById(R.id.ratio5);
        this.ratio6 = (Button) findViewById(R.id.ratio6);
        this.ratio7 = (Button) findViewById(R.id.ratio7);
        this.ratio8 = (Button) findViewById(R.id.ratio8);
        this.ratio9 = (Button) findViewById(R.id.ratio9);
        this.ratio10 = (Button) findViewById(R.id.ratio10);
        this.ratio11 = (Button) findViewById(R.id.ratio11);
        this.ratio12 = (Button) findViewById(R.id.ratio12);
        this.ratio13 = (Button) findViewById(R.id.ratio13);
        this.ratio14 = (Button) findViewById(R.id.ratio14);
        this.ratio15 = (Button) findViewById(R.id.ratio15);
        this.bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        this.bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String string = extra.getString("value");
            this.value = string;
            if (string.equals("image")) {
                this.cropimage.setFixedAspectRatio(true);
                this.cropimage.setAspectRatio(1, 1);
                this.footer.setVisibility(8);
            } else if (this.value.equals("sticker")) {
                this.footer.setVisibility(0);
            }
            try {
                Bitmap bitmap = ImageUtils.getBitmapFromUri(this, getIntent().getData(), this.screenWidth, this.screenHeight);
                if (this.screenWidth > ((float) bitmap.getWidth()) && this.screenHeight > ((float) bitmap.getHeight())) {
                    float f = this.screenWidth;
                    bitmap = ImageUtils.resizeBitmap(bitmap, (int) f, (int) f);
                }
                this.cropimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
            }
        }
        findViewById(R.id.btn_bck).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.finish();
            }
        });
        this.done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.bitmapImage = CropActivityTwo.this.cropimage.getCroppedImage();
                if (CropActivityTwo.this.value.equals("image")) {
                    CropActivityTwo.this.getSticker.ongetImageBitmap(CropActivityTwo.this.value);
                    CropActivityTwo.this.finish();
                }
            }
        });
        this.custom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(false);
            }
        });
        this.square.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(1, 1);
            }
        });
        this.ratio1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(1, 2);
            }
        });
        this.ratio2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(2, 1);
            }
        });
        this.ratio3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(2, 3);
            }
        });
        this.ratio4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(3, 2);
            }
        });
        this.ratio5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(3, 4);
            }
        });
        this.ratio6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(3, 5);
            }
        });
        this.ratio7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(4, 3);
            }
        });
        this.ratio8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(4, 5);
            }
        });
        this.ratio9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(4, 7);
            }
        });
        this.ratio10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(5, 3);
            }
        });
        this.ratio11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(5, 4);
            }
        });
        this.ratio12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(5, 6);
            }
        });
        this.ratio13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(5, 7);
            }
        });
        this.ratio14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(9, 16);
            }
        });
        this.ratio15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropActivityTwo.this.cropimage.setFixedAspectRatio(true);
                CropActivityTwo.this.cropimage.setAspectRatio(16, 9);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.preferences.getBoolean("isAdsDisabled", false);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.preferences.getBoolean("isAdsDisabled", false);
    }

    private boolean isNetworkAvailable() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

package com.text.with.sticker.textonphoto.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.text.with.sticker.textonphoto.R;

import java.io.File;
import java.io.PrintStream;

public class SaveActivity extends AppCompatActivity {
    Button back;
    Button fb;
    RelativeLayout fbll;
    Button insta;
    RelativeLayout install;
    private ImageView mImageView;
    Uri myUri;
    String path;
    Button save;
    Button share;
    RelativeLayout sharell;
    Button what;
    RelativeLayout whatll;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.save_activity);
        getSupportActionBar().hide();
        this.fb = (Button) findViewById(R.id.facebook);
        this.insta = (Button) findViewById(R.id.insta);
        this.what = (Button) findViewById(R.id.whatsup);
        this.share = (Button) findViewById(R.id.share);
        this.save = (Button) findViewById(R.id.done);
        this.back = (Button) findViewById(R.id.back);
        this.fbll = (RelativeLayout) findViewById(R.id.facebookll);
        this.install = (RelativeLayout) findViewById(R.id.install);
        this.whatll = (RelativeLayout) findViewById(R.id.whatsupll);
        this.sharell = (RelativeLayout) findViewById(R.id.sharell);
        this.mImageView = (ImageView) findViewById(R.id.mainImageView);
        Intent in = getIntent();
        new BitmapFactory.Options().inPreferredConfig = Bitmap.Config.ARGB_8888;
        String stringExtra = in.getStringExtra("uri");
        this.path = stringExtra;
        Log.e("path", stringExtra);
        this.mImageView.setImageURI(Uri.parse(this.path));
        this.myUri = Uri.parse(this.path);
        sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + this.path)));
        this.save.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        Log.i("TAG", "touched down");
                        return false;
                    case 1:
                        Log.i("TAG", "touched up");
                        SaveActivity.this.saveImageBtnClicked();
                        return false;
                    case 2:
                        Log.i("TAG", "moving: (16842924, 16842925)");
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.back.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        Log.i("TAG", "touched down");
                        return false;
                    case 1:
                        Log.i("TAG", "touched up");
                        SaveActivity.this.backk();
                        return false;
                    case 2:
                        Log.i("TAG", "moving: (16842924, 16842925)");
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.fb.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        Log.i("TAG", "touched down");
                        SaveActivity.this.fb.setBackgroundResource(R.drawable.fb1);
                        return false;
                    case 1:
                        Log.i("TAG", "touched up");
                        SaveActivity.this.facebook();
                        SaveActivity.this.fb.setBackgroundResource(R.drawable.fb);
                        return false;
                    case 2:
                        Log.i("TAG", "moving: (16842924, 16842925)");
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.what.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        Log.i("TAG", "touched down");
                        SaveActivity.this.what.setBackgroundResource(R.drawable.what1);
                        return false;
                    case 1:
                        Log.i("TAG", "touched up");
                        SaveActivity.this.whatsup();
                        SaveActivity.this.what.setBackgroundResource(R.drawable.what);
                        return false;
                    case 2:
                        Log.i("TAG", "moving: (16842924, 16842925)");
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.insta.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        Log.i("TAG", "touched down");
                        SaveActivity.this.insta.setBackgroundResource(R.drawable.insta1);
                        return false;
                    case 1:
                        Log.i("TAG", "touched up");
                        SaveActivity.this.insta.setBackgroundResource(R.drawable.insta);
                        SaveActivity.this.instagram();
                        return false;
                    case 2:
                        Log.i("TAG", "moving: (16842924, 16842925)");
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.share.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        Log.i("TAG", "touched down");
                        SaveActivity.this.share.setBackgroundResource(R.drawable.share1);
                        return false;
                    case 1:
                        Log.i("TAG", "touched up");
                        SaveActivity.this.share();
                        SaveActivity.this.share.setBackgroundResource(R.drawable.share);
                        return false;
                    case 2:
                        Log.i("TAG", "moving: (16842924, 16842925)");
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    public void saveImageBtnClicked() {
        sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + this.path)));
        Toast.makeText(this, "save image", Toast.LENGTH_SHORT).show();
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void backk() {
        File fdelete = new File(this.myUri.getPath());
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                PrintStream printStream = System.out;
                printStream.println("file Deleted :" + this.myUri.getPath());
            } else {
                PrintStream printStream2 = System.out;
                printStream2.println("file not Deleted :" + this.myUri.getPath());
            }
        }
        finish();
    }

    @SuppressLint("WrongConstant")
    public void facebook() {
        Uri data = FileProvider.getUriForFile(this, "com.textonphoto.customqoutescreator.fileprovider", new File(this.path));
        grantUriPermission(getPackageName(), data, 1);
        Intent sendIntent = new Intent("android.intent.action.SEND");
        sendIntent.setType("image/*");
        sendIntent.putExtra("android.intent.extra.SUBJECT", "Image");
        sendIntent.setPackage("com.facebook.katana");
        sendIntent.putExtra("android.intent.extra.STREAM", data);
        sendIntent.addFlags(1);
        sendIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.app_name));
        startActivity(Intent.createChooser(sendIntent, "Share Image:"));
    }

    @SuppressLint("WrongConstant")
    public void instagram() {
        Uri data = FileProvider.getUriForFile(this, "com.textonphoto.customqoutescreator.fileprovider", new File(this.path));
        grantUriPermission(getPackageName(), data, 1);
        Intent sendIntent = new Intent("android.intent.action.SEND");
        sendIntent.setType("image/*");
        sendIntent.putExtra("android.intent.extra.SUBJECT", "Image");
        sendIntent.setPackage("com.instagram.android");
        sendIntent.putExtra("android.intent.extra.STREAM", data);
        sendIntent.addFlags(1);
        sendIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.app_name));
        startActivity(Intent.createChooser(sendIntent, "Share Image:"));
    }

    @SuppressLint("WrongConstant")
    public void whatsup() {
        Uri data = FileProvider.getUriForFile(this, "com.textonphoto.customqoutescreator.fileprovider", new File(this.path));
        grantUriPermission(getPackageName(), data, 1);
        Intent sendIntent = new Intent("android.intent.action.SEND");
        sendIntent.setType("image/*");
        sendIntent.putExtra("android.intent.extra.SUBJECT", "Image");
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra("android.intent.extra.STREAM", data);
        sendIntent.addFlags(1);
        sendIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.app_name));
        startActivity(Intent.createChooser(sendIntent, "Share Image:"));
    }

    @SuppressLint("WrongConstant")
    public void share() {
        Uri data = FileProvider.getUriForFile(this, "com.textonphoto.customqoutescreator.fileprovider", new File(this.path));
        grantUriPermission(getPackageName(), data, 1);
        Intent sendIntent = new Intent("android.intent.action.SEND");
        sendIntent.setType("image/*");
        sendIntent.putExtra("android.intent.extra.SUBJECT", "Image");
        sendIntent.putExtra("android.intent.extra.STREAM", data);
        sendIntent.addFlags(1);
        sendIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.app_name));
        startActivity(Intent.createChooser(sendIntent, "Share Image:"));
    }
}

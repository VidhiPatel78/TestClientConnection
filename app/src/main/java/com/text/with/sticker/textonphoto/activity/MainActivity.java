package com.text.with.sticker.textonphoto.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.text.with.sticker.textonphoto.APPUtility;
import com.text.with.sticker.textonphoto.BuildConfig;
import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.adapter.RecyclerCategoryAdapter;
import com.text.with.sticker.textonphoto.db.DatabaseHandler;
import com.text.with.sticker.textonphoto.db.Quotes;
import com.text.with.sticker.textonphoto.db.QuotesSelect;
import com.text.with.sticker.textonphoto.quotesedit.PictureConstant;
import com.text.with.sticker.textonphoto.utility.CategoryInfo;
import com.text.with.sticker.textonphoto.utility.Constants;


import java.util.ArrayList;
/*import yuku.ambilwarna.BuildConfig;*/

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 100;
    private static final int REQUEST_PERMISSION = 101;
    public static int height1;
    public static float ratio;
    public static int width;
    ImageButton btn_Search;
    ImageButton btn_clear;
    ImageButton btn_quoteofday;
    ArrayList<CategoryInfo> categoryInfoList = new ArrayList<>();
    EditText edittext;
    private boolean isOpenFisrtTime = false;
    RecyclerView mRecyclerView;
    boolean monthlyBoolean = false;
    SharedPreferences preferences;
    ProgressBar progressBar;
    LoadingCategoryAsync quotesListAsync;
    private BroadcastReceiver removewatermark_update = new BroadcastReceiver() {
        /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            MainActivity.this.monthlyBoolean = false;
            MainActivity.this.yearlyBoolean = false;
        }
    };
    boolean yearlyBoolean = false;

    public class LoadingCategoryAsync extends AsyncTask<String, Void, String> {
        public LoadingCategoryAsync() {
        }

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public void onPreExecute() {
            MainActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            try {
                MainActivity.this.categoryInfoList.clear();
                DatabaseHandler dh = DatabaseHandler.getDbHandler(MainActivity.this);
                MainActivity.this.callDB(dh);
                MainActivity.this.categoryInfoList = dh.getCategooryListDes();
                dh.close();
                return "yes";
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "yes";
            }
        }

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public void onPostExecute(String result) {
            MainActivity.this.progressBar.setVisibility(8);
            if (MainActivity.this.categoryInfoList.size() != 0) {
                MainActivity mainActivity = MainActivity.this;
                RecyclerCategoryAdapter categoryAdapter = new RecyclerCategoryAdapter(mainActivity, mainActivity.categoryInfoList);
                categoryAdapter.setHasStableIds(true);
                MainActivity.this.mRecyclerView.setAdapter(categoryAdapter);
            }
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        registerReceiver(this.removewatermark_update, new IntentFilter("Remove_Watermark"));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        int i = size.y;
        height1 = i;
        ratio = ((float) width) / ((float) i);
        this.btn_quoteofday = (ImageButton) findViewById(R.id.btn_options);
        this.btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        this.btn_Search = (ImageButton) findViewById(R.id.btn_Search);
        this.edittext = (EditText) findViewById(R.id.edittext);
        getWindow().setSoftInputMode(2);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        this.mRecyclerView.setHasFixedSize(true);
        if (Build.VERSION.SDK_INT < 23 || (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
            LoadingCategoryAsync loadingCategoryAsync = new LoadingCategoryAsync();
            this.quotesListAsync = loadingCategoryAsync;
            loadingCategoryAsync.execute(new String[0]);
        } else {
            permissionDialog();
        }
        this.edittext.addTextChangedListener(new TextWatcher() {
            /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass2 */

            @SuppressLint("WrongConstant")
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainActivity.this.edittext.length() > 0) {
                    MainActivity.this.btn_clear.setVisibility(0);
                    MainActivity.this.btn_Search.setBackgroundResource(R.drawable.search);
                    return;
                }
                MainActivity.this.btn_clear.setVisibility(4);
                MainActivity.this.btn_Search.setBackgroundResource(R.drawable.search_light);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (MainActivity.this.edittext.length() > 0) {
                    MainActivity.this.btn_clear.setVisibility(0);
                    MainActivity.this.btn_Search.setBackgroundResource(R.drawable.search);
                    return;
                }
                MainActivity.this.btn_clear.setVisibility(4);
                MainActivity.this.btn_Search.setBackgroundResource(R.drawable.search_light);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void viewLikedQuotes(View view) {
        switch (view.getId()) {
            case R.id.btn_Search:
                if (this.edittext.getText().toString().equals("")) {
                    this.edittext.setError(getResources().getString(R.string.str_empaty));
                    return;
                }
                Intent searchquotesActivity = new Intent(this, QuotesListActivity.class);
                searchquotesActivity.putExtra("categoryId", 0);
                searchquotesActivity.putExtra("categoryName", "");
                searchquotesActivity.putExtra("searchString", this.edittext.getText().toString());
                startActivity(searchquotesActivity);
                return;
            case R.id.btn_clear:
                this.btn_clear.setVisibility(4);
                this.btn_Search.setBackgroundResource(R.drawable.search_light);
                this.edittext.setText("");
                return;

            case R.id.btn_options:
                PopupMenu popup = new PopupMenu(this, this.btn_quoteofday);
                popup.getMenuInflater().inflate(R.menu.poupup_menu_option, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass3 */

                    @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.imageHelp:
                                try {
                                    Intent ii = new Intent("android.intent.action.SEND");
                                    ii.setType("text/plain");
                                    ii.putExtra("android.intent.extra.TEXT", "\n Photo frame\n\n" + "https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "\n\n");
                                    MainActivity.this.startActivity(Intent.createChooser(ii, "choose one"));
                                } catch (Exception e) {
                                }
                                return true;
                            case R.id.imageMore:
                                Intent i1 = new Intent("android.intent.action.VIEW");
                                i1.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Elvee Infotech"));
                                MainActivity.this.startActivity(i1);
                                return true;
                            case R.id.imagePrivacy:
                                Intent i = new Intent("android.intent.action.VIEW");
                                i.setData(Uri.parse(APPUtility.PrivacyPolicy));
                                MainActivity.this.startActivity(i);
                                return true;
                            case R.id.imageRate:
                                Intent i2 = new Intent("android.intent.action.VIEW");
                                i2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName()));
                                MainActivity.this.startActivity(i2);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popup.show();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    @Override // androidx.fragment.app.FragmentActivity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (Build.VERSION.SDK_INT < 23 || (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
                LoadingCategoryAsync loadingCategoryAsync = new LoadingCategoryAsync();
                this.quotesListAsync = loadingCategoryAsync;
                loadingCategoryAsync.execute(new String[0]);
                return;
            }
            permissionDialog();
        }
    }

    @SuppressLint("WrongConstant")
    public void permissionDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.permissionsdialog);
        dialog.setTitle(getResources().getString(R.string.permission).toString());
        dialog.setCancelable(false);
        ((Button) dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass4 */

            @SuppressLint("NewApi")
            public void onClick(View v) {
                MainActivity.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
                dialog.dismiss();
            }
        });
        if (this.isOpenFisrtTime) {
            Button setting = (Button) dialog.findViewById(R.id.settings);
            setting.setVisibility(0);
            setting.setOnClickListener(new View.OnClickListener() {
                /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass5 */

                public void onClick(View v) {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                    intent.addFlags(268435456);
                    MainActivity.this.startActivityForResult(intent, 101);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    @SuppressLint({"WrongConstant", "MissingSuperCall"})
    @Override // androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback, androidx.fragment.app.FragmentActivity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == 0) {
                if (Build.VERSION.SDK_INT < 23 || (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
                    LoadingCategoryAsync loadingCategoryAsync = new LoadingCategoryAsync();
                    this.quotesListAsync = loadingCategoryAsync;
                    loadingCategoryAsync.execute(new String[0]);
                    return;
                }
                this.isOpenFisrtTime = true;
                permissionDialog();
            } else if (Build.VERSION.SDK_INT < 23 || (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
                LoadingCategoryAsync loadingCategoryAsync2 = new LoadingCategoryAsync();
                this.quotesListAsync = loadingCategoryAsync2;
                loadingCategoryAsync2.execute(new String[0]);
            } else {
                this.isOpenFisrtTime = true;
                permissionDialog();
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.removewatermark_update);
        try {
            System.gc();
            Runtime.getRuntime().gc();
        } catch (OutOfMemoryError e3) {
            e3.printStackTrace();
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            new Thread(new Runnable() {
                /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass6 */

                public void run() {
                    try {
                        Glide.get(MainActivity.this).clearDiskCache();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Glide.get(this).clearMemory();
            LoadingCategoryAsync loadingCategoryAsync = this.quotesListAsync;
            if (loadingCategoryAsync != null) {
                if (loadingCategoryAsync.getStatus() == AsyncTask.Status.PENDING) {
                    this.quotesListAsync.cancel(true);
                }
                if (this.quotesListAsync.getStatus() == AsyncTask.Status.RUNNING) {
                    this.quotesListAsync.cancel(true);
                }
            }
            this.mRecyclerView = null;
        } catch (OutOfMemoryError e32) {
            e32.printStackTrace();
        } catch (Exception e42) {
            e42.printStackTrace();
        }
        Constants.freeMemory();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void callDB(DatabaseHandler db) {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", 0);
        SharedPreferences.Editor editor = prefs.edit();
        if (prefs.getString("name", "No name defined").equals("No name defined")) {
            String text = getResources().getString(R.string.tab_write);
            db.addQuotes(new Quotes("t1", 1, 53, String.valueOf(PictureConstant.getNewWidth(710.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(896.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 41.0f) + "", "-16514044", "windsong.ttf", "3", "3", "3", "-12303292", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t2", 2, 49, String.valueOf(PictureConstant.getNewWidth(630.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(1000.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 40.0f) + "", "-10081008", "Walkway_Bold.ttf", "1", "1", "1", "-1", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t3", 3, 49, String.valueOf(PictureConstant.getNewWidth(628.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(786.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 24.0f) + "", "-15064270", "Sofia-Regular.otf", "1", "1", "1", "-1", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t4", 4, 49, String.valueOf(PictureConstant.getNewWidth(559.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(833.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 36.0f) + "", "-13824", "Walkway_Bold.ttf", "4.5", "4.5", "4.5", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t5", 5, 49, String.valueOf(PictureConstant.getNewWidth(933.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(534.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 22.0f) + "", "-1", "Beyond Wonderland.ttf", "3", "3", "3", "-12303292", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t6", 6, 49, String.valueOf(PictureConstant.getNewWidth(929.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(516.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 31.0f) + "", "-3546350", "lesser concern.ttf", "3", "3", "3", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t7", 7, 49, String.valueOf(PictureConstant.getNewWidth(770.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(774.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 37.0f) + "", "-12574976", "windsong.ttf", "1", "1", "1", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t8", 8, 49, String.valueOf(PictureConstant.getNewWidth(890.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(504.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 24.0f) + "", "-2228371", "Advertising Script Bold Trial.ttf", "3.5", "3.5", "3.5", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t9", 9, 49, String.valueOf(PictureConstant.getNewWidth(816.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(736.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 34.0f) + "", "-1", "ROD.TTF", "5.5", "5.5", "5.5", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t10", 10, 49, String.valueOf(PictureConstant.getNewWidth(859.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(491.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 26.0f) + "", "-69405", "CalliGravity.ttf", "2.5", "2.5", "2.5", "-15647417", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t11", 11, 49, String.valueOf(PictureConstant.getNewWidth(947.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(427.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 26.0f) + "", "-16750266", "Walkway_Bold.ttf", "0", "0", "0", "0", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t12", 12, 49, String.valueOf(PictureConstant.getNewWidth(836.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(548.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 28.0f) + "", "-2381", "Beyond Wonderland.ttf", "3", "3", "3", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t13", 13, 49, String.valueOf(PictureConstant.getNewWidth(1040.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(565.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 27.0f) + "", "-5934779", "Cosmic Love.ttf", "1.5", "1.5", "1.5", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t14", 14, 49, String.valueOf(PictureConstant.getNewWidth(934.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(499.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 36.0f) + "", "-836536", "ARDECODE.ttf", "2", "2", "2", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t15", 15, 49, String.valueOf(PictureConstant.getNewWidth(919.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(513.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 19.0f) + "", "-1974091", "DejaVuSans_Bold.ttf", "3", "3", "3", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t16", 16, 49, String.valueOf(PictureConstant.getNewWidth(405.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(892.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 31.0f) + "", "-261", "Advertising Script Monoline Trial.ttf", "2", "2", "2", "-1", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t17", 17, 49, String.valueOf(PictureConstant.getNewWidth(460.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(843.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 31.0f) + "", "-1", "squealer.ttf", "2", "2", "2", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t18", 18, 53, String.valueOf(PictureConstant.getNewWidth(456.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(852.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 25.0f) + "", "-2816", "segoe.ttf", "3", "3", "3", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t19", 19, 53, String.valueOf(PictureConstant.getNewWidth(552.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(748.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 33.0f) + "", "-917886", "Aladin_Regular.ttf", "2.0", "2.0", "2.0", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t20", 20, 49, String.valueOf(PictureConstant.getNewWidth(486.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(850.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 32.0f) + "", "-2720513", "lesser concern.ttf", "3", "3", "3", "-16777216", "null", "no", "no", "no", "no", ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 313, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t21", 21, 49, String.valueOf(PictureConstant.getNewWidth(902.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(569.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 27.0f) + "", "-7914994", "Advertising Script Monoline Trial.ttf", "0.0", "0.0", "0.0", "0", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t22", 22, 53, String.valueOf(PictureConstant.getNewWidth(652.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(748.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 29.0f) + "", "-256", "ROD.TTF", "2.5", "2.5", "2.5", "-10813440", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t23", 23, 49, String.valueOf(PictureConstant.getNewWidth(584.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(760.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 34.0f) + "", "-146432", "ARDECODE.ttf", "2", "2", "2", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t24", 24, 53, String.valueOf(PictureConstant.getNewWidth(445.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(707.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 30.0f) + "", "-1860755", "Walkway_Bold.ttf", "2.5", "2.5", "2.5", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t25", 25, 49, String.valueOf(PictureConstant.getNewWidth(442.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(741.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 32.0f) + "", "-8895187", "QUIGLEYW.TTF", "0", "0", "0", "06", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t26", 26, 49, String.valueOf(PictureConstant.getNewWidth(1007.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(446.0f)), text, PictureConstant.getNewSize(getApplicationContext(), 23.0f) + "", "-14143143", "Walkway_Bold.ttf", "2", "2", "2", "-16777216", "null", "no", "no", "no", "no", 0, 0, 0, "", "", "false", 0, "", 0, "false", 0));
            db.addQuotes(new Quotes("t99", 99, 49, String.valueOf(PictureConstant.getNewWidth(560.0f)) + "-" + String.valueOf(PictureConstant.getNewHeight(581.0f)), "Congratulations for always evolving and inspiring others", PictureConstant.getNewSize(getApplicationContext(), 18.0f) + "", "-16711936", "Advertising Script Bold Trial.ttf", "3.5", "3.5", "3.5", "-16777216", "null", "no", "no", "no", "no", 297, 313, 0, "", "", "false", 0, "", 0, "false", 0));
            setSeletedData(db);
            editor.putString("name", "insertQuotes");
            editor.commit();
        }
    }

    public void setSeletedData(DatabaseHandler db) {
        db.addQuoteSelect(new QuotesSelect(1, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 41.0f) + "", "-7590912", "windsong.ttf", "2.0", "2.0", "2.0", "-9359358", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(2, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 41.0f) + "", "-11330816", "Capture_it.ttf", "3.0", "3.0", "3.0", "-1", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(3, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 25.0f) + "", "-1", "Sofia-Regular.otf", "3.0", "3.0", "3.0", "-12303292", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(4, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 36.0f) + "", "-1", "Beyond Wonderland.ttf", "4.5", "4.5", "4.5", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(5, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 23.0f) + "", "-256", "ufonts_com_ck_scratchy_box.ttf", "3.0", "3.0", "3.0", "-12303292", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(6, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 38.0f) + "", "-1", "lesser concern.ttf", "4.5", "4.5", "4.5", "-16777216", "null", "true", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(7, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 37.0f) + "", "-12574976", "windsong.ttf", BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, "-16777216", "null", "false", "false", "true", "false"));
        db.addQuoteSelect(new QuotesSelect(8, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 24.0f) + "", "-2608", "Advertising Script Bold Trial.ttf", "3.0", "3.0", "3.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(9, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 35.0f) + "", "-1628", "Capture_it.ttf", "5.5", "5.5", "5.5", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(10, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 34.0f) + "", "-256", "CalliGravity.ttf", "2.5", "2.5", "2.5", "-15647417", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(11, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 26.0f) + "", "-16777216", "Aspergit.otf", "0.0", "0.0", "0.0", "0", "null", "true", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(12, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 28.0f) + "", "-1", "ufonts_com_ck_scratchy_box.ttf", "3.0", "3.0", "3.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(13, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 41.0f) + "", "-5934779", "lesser concern shadow.ttf", "1.5", "1.5", "1.5", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(14, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 37.0f) + "", "-9239546", "Adobe Caslon Pro Italic.ttf", "2.0", "2.0", "2.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(15, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 38.0f) + "", "-8114411", "majalla.ttf", "2.5", "2.5", "2.5", "-1", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(16, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 31.0f) + "", "-1850263", "Advertising Script Bold Trial.ttf", "0.0", "0.0", "0.0", "0", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(17, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 31.0f) + "", "-16316665", "squealer.ttf", "5.0", "5.0", "5.0", "-1", "null", "true", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(18, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 31.0f) + "", "-16711727", "Constantia Italic.ttf", "3.0", "3.0", "3.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(19, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 30.0f) + "", "-10223667", "Capture_it.ttf", "2.0", "2.0", "2.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(20, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 32.0f) + "", "-1", "lesser concern shadow.ttf", "3.0", "3.0", "3.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(21, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 39.0f) + "", "-11587554", "ARDECODE.ttf", "0.0", "0.0", "0.0", "0", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(22, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 30.0f) + "", "-16777216", "Capture_it.ttf", "0.0", "0.0", "0.0", "0", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(23, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 34.0f) + "", "-8109049", "aparaji.ttf", "2.0", "2.0", "2.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(24, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 30.0f) + "", "-16777216", "Walkway_Bold.ttf", "2.5", "2.5", "2.5", "-16777216", "null", "false", "false", "false", "true"));
        db.addQuoteSelect(new QuotesSelect(25, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 23.0f) + "", "-14744825", "squealer.ttf", "0.0", "0.0", "0.0", "0", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(26, 1, "15", "24", PictureConstant.getNewSize(getApplicationContext(), 23.0f) + "", "-15703697", "DejaVuSans_Bold.ttf", "0.0", "0.0", "0.0", "0", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(99, 1, "20", "26", PictureConstant.getNewSize(getApplicationContext(), 20.0f) + "", "-256", "Advertising Script Bold Trial.ttf", "3.0", "3.0", "3.0", "-16777216", "null", "false", "false", "false", "false"));
        db.addQuoteSelect(new QuotesSelect(99, 2, "9", "17", PictureConstant.getNewSize(getApplicationContext(), 20.0f) + "", "-16777216", "Adobe Caslon Pro Italic.ttf", "3.0", "3.0", "3.0", "-16777216", "null", "false", "false", "false", "false"));
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onResume() {
        super.onResume();
        this.preferences.getBoolean("isAdsDisabled", false);
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onPause() {
        super.onPause();
        this.preferences.getBoolean("isAdsDisabled", false);
    }

    @Override // androidx.activity.ComponentActivity
    public void onBackPressed() {
        exitAlertDialog();
    }

    public void exitAlertDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exitalert_dialog);
        dialog.setCancelable(true);
        ((Button) dialog.findViewById(R.id.btn_no)).setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass7 */

            public void onClick(View v) {
                MainActivity.this.finishAffinity();
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.activity.MainActivity.AnonymousClass8 */

            public void onClick(View v) {
                MainActivity.this.finish();
                System.exit(0);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean isNetworkAvailable() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

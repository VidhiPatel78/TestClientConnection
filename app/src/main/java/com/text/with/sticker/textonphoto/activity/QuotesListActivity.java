package com.text.with.sticker.textonphoto.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.adapter.RecyclerQuotesListAdapter;
import com.text.with.sticker.textonphoto.db.DatabaseHandler;
import com.text.with.sticker.textonphoto.utility.Constants;
import com.text.with.sticker.textonphoto.utility.CustomTextView;
import com.text.with.sticker.textonphoto.utility.QuotesInfo;


import java.util.ArrayList;

public class QuotesListActivity extends AppCompatActivity {
    public static String[] pathsOfFileUri = new String[1];
    int categoryId = -1;
    RecyclerView mRecyclerView;
    SharedPreferences preferences;
    ProgressBar progressBar;
    ArrayList<QuotesInfo> quotesInfoList = new ArrayList<>();
    LoadingQuotesListAsync quotesListAsync;
    RecyclerQuotesListAdapter quotesdapter;
    String searchString;

    public class LoadingQuotesListAsync extends AsyncTask<String, Void, String> {
        public LoadingQuotesListAsync() {
        }

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public void onPreExecute() {
            QuotesListActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            try {
                QuotesListActivity.this.quotesInfoList.clear();
                DatabaseHandler dh = DatabaseHandler.getDbHandler(QuotesListActivity.this);
                if (QuotesListActivity.this.searchString.equals("")) {
                    QuotesListActivity quotesListActivity = QuotesListActivity.this;
                    quotesListActivity.quotesInfoList = dh.getQuotesList(quotesListActivity.categoryId, "");
                } else {
                    QuotesListActivity quotesListActivity2 = QuotesListActivity.this;
                    quotesListActivity2.quotesInfoList = dh.getQuotesList(quotesListActivity2.categoryId, QuotesListActivity.this.searchString);
                }
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
            QuotesListActivity.this.progressBar.setVisibility(8);
            if (QuotesListActivity.this.quotesInfoList.size() != 0) {
                QuotesListActivity quotesListActivity = QuotesListActivity.this;
                QuotesListActivity quotesListActivity2 = QuotesListActivity.this;
                quotesListActivity.quotesdapter = new RecyclerQuotesListAdapter(quotesListActivity2, quotesListActivity2.quotesInfoList);
                QuotesListActivity.this.quotesdapter.setHasStableIds(true);
                QuotesListActivity.this.mRecyclerView.setAdapter(QuotesListActivity.this.quotesdapter);
                return;
            }
            ((CustomTextView) QuotesListActivity.this.findViewById(R.id.tvEmpty)).setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_quotes_list);
        getSupportActionBar().hide();
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            this.searchString = extra.getString("searchString");
            this.categoryId = extra.getInt("categoryId");
            String categoryName = extra.getString("categoryName");
            if (this.searchString.equals("")) {
                ((CustomTextView) findViewById(R.id.headertext)).setText(categoryName);
            } else {
                ((CustomTextView) findViewById(R.id.headertext)).setText(this.searchString);
                if (this.searchString.length() > 26) {
                    ((CustomTextView) findViewById(R.id.txtDot)).setVisibility(0);
                }
            }
        } else {
            finish();
        }
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.mRecyclerView.setHasFixedSize(true);
        pathsOfFileUri[0] = "";
        LoadingQuotesListAsync loadingQuotesListAsync = new LoadingQuotesListAsync();
        this.quotesListAsync = loadingQuotesListAsync;
        loadingQuotesListAsync.execute(new String[0]);
    }

    public void viewLikedQuotes(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onResume() {
        super.onResume();
        if (!this.preferences.getBoolean("isAdsDisabled", false)) {
            this.preferences.getBoolean("isAdsDisabled", false);
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onPause() {
        super.onPause();
        this.preferences.getBoolean("isAdsDisabled", false);
    }

    @Override // androidx.activity.ComponentActivity
    public void onBackPressed() {
        super.onBackPressed();
        String[] strArr = pathsOfFileUri;
        if (strArr.length > 0) {
            for (String deleteFile : strArr) {
                Constants.deleteFile(this, deleteFile);
            }
        }
    }

    private boolean isNetworkAvailable() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        super.onDestroy();
        try {
            new Thread(new Runnable() {
                /* class com.textonphoto.customqoutescreator.activity.QuotesListActivity.AnonymousClass1 */

                public void run() {
                    try {
                        Glide.get(QuotesListActivity.this).clearDiskCache();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Glide.get(this).clearMemory();
            LoadingQuotesListAsync loadingQuotesListAsync = this.quotesListAsync;
            if (loadingQuotesListAsync != null) {
                if (loadingQuotesListAsync.getStatus() == AsyncTask.Status.PENDING) {
                    this.quotesListAsync.cancel(true);
                }
                if (this.quotesListAsync.getStatus() == AsyncTask.Status.RUNNING) {
                    this.quotesListAsync.cancel(true);
                }
            }
            this.mRecyclerView = null;
            this.quotesdapter = null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Constants.freeMemory();
    }
}

package com.text.with.sticker.textonphoto.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.adapter.StickerViewPagerAdapter;
import com.text.with.sticker.textonphoto.interfacelistner.OnGetStickerReworded;
import com.text.with.sticker.textonphoto.utility.Constants;

public class StickerList extends AppCompatActivity implements TabLayout.OnTabSelectedListener, OnGetStickerReworded {
    StickerViewPagerAdapter _adapter;
    ImageView back;
    int categoryPosition;
    TextView header_txt;
    RelativeLayout lay_MainTabbar;
    SharedPreferences preferences;
    String str1;
    String str2;
    String str3;
    TabLayout tabs;
    ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.sticker_list);
        getSupportActionBar().hide();
        init();
    }

    private void init() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.categoryPosition = getIntent().getIntExtra("position", 0);
        this.lay_MainTabbar = (RelativeLayout) findViewById(R.id.lay_MainTabbar);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this._adapter = new StickerViewPagerAdapter(this, getSupportFragmentManager(), this.categoryPosition);
        this.tabs = (TabLayout) findViewById(R.id.result_tabs);
        this.viewPager.setAdapter(this._adapter);
        this.tabs.setupWithViewPager(this.viewPager);
        this.viewPager.setCurrentItem(0);
        this.header_txt = (TextView) findViewById(R.id.txt_appname);
        this.back = (ImageView) findViewById(R.id.btn_bck);
        this.tabs.addOnTabSelectedListener((TabLayout.OnTabSelectedListener) this);
        ((ImageView) findViewById(R.id.btn_bck)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StickerList.this.onBackPressed();
            }
        });
    }

    public void onTabSelected(TabLayout.Tab tab) {
    }

    public void onTabUnselected(TabLayout.Tab tab) {
    }

    public void onTabReselected(TabLayout.Tab tab) {
    }

    public void ongetStickerReworded(String type, String categoryBG, String imgType) {
        this.str1 = type;
        this.str2 = categoryBG;
        this.str3 = imgType;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.failed_ads_dialog);
        dialog.setCancelable(false);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        btn_ok.setTypeface(Constants.getTextTypeface(this));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ViewPager viewPager2 = this.viewPager;
        if (viewPager2 != null && viewPager2.getChildCount() != 0 && this._adapter.currentFragment(this.viewPager.getCurrentItem()) != null) {
            this._adapter.currentFragment(this.viewPager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.preferences.getBoolean("isAdsDisabled", false);
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.preferences.getBoolean("isAdsDisabled", false);
        super.onPause();
    }

    public void onDestroy() {
        this.preferences.getBoolean("isAdsDisabled", false);
        super.onDestroy();
        try {
            this.lay_MainTabbar = null;
            this.header_txt = null;
            this.back = null;
            this._adapter = null;
            this.viewPager = null;
            this.tabs = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Constants.freeMemory();
    }
}

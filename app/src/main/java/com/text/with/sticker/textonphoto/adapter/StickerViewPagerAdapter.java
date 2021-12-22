package com.text.with.sticker.textonphoto.adapter;



import android.content.Context;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.activity.StickerList;
import com.text.with.sticker.textonphoto.fragment.FragmentSticker;

import java.util.ArrayList;

public class StickerViewPagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES;
    private Context _context;
    int categoryPosition;
    ArrayList<Fragment> fragments = new ArrayList<>();

    public StickerViewPagerAdapter(Context context, FragmentManager fm, int categoryPosition2) {
        super(fm);
        this._context = context;
        this.categoryPosition = categoryPosition2;
        this.TITLES = context.getResources().getStringArray(R.array.sticker_category);
        Fragment f = new Fragment();
        for (int i = 0; i < this.TITLES.length; i++) {
            this.fragments.add(f);
        }
    }


    public androidx.fragment.app.Fragment getItem(int position) {
        Fragment f = new FragmentSticker();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position + 1);
        f.setArguments(bundle);
        this.fragments.set(position, f);
        return f;
    }

    public CharSequence getPageTitle(int position) {
        return this.TITLES[position];
    }

    public int getCount() {
        return this.TITLES.length;
    }

    public Fragment currentFragment(int position) {
        return this.fragments.get(position);
    }
}

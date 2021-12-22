package com.text.with.sticker.textonphoto.adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.fragment.FragmentBGList;
import com.text.with.sticker.textonphoto.utility.Constants;

import java.util.ArrayList;

public class BackgroundViewPagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES;
    private Context _context;
    int categoryPosition;
    String categoryQuote;
    ArrayList<Fragment> fragments = new ArrayList<>();
    String hasAuthor;
    String quote;

    public BackgroundViewPagerAdapter(Context context, FragmentManager fm, int categoryPosition2, String quote2, String hasAuthor2, String categoryQuote2) {
        super(fm);
        this._context = context;
        this.categoryPosition = categoryPosition2;
        this.quote = quote2;
        this.hasAuthor = hasAuthor2;
        this.categoryQuote = categoryQuote2;
        this.TITLES = context.getResources().getStringArray(R.array.listOfChoosePicItem);
        this.fragments.add(new Fragment());
    }


    public Fragment getItem(int position) {
        Fragment f = new FragmentBGList();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position + 1);
        bundle.putString("quote_edit", this.quote);
        bundle.putString("hasAuthor", this.hasAuthor);
        bundle.putString("categoryQuote", this.categoryQuote);
        bundle.putString("categoryBG", this.TITLES[position].toString());
        f.setArguments(bundle);
        this.fragments.set(position, f);
        return f;
    }

    public CharSequence getPageTitle(int position) {
        return this.TITLES[position];
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(this._context).inflate(R.layout.custom_tab_lay, (ViewGroup) null);
        ImageView iv = (ImageView) v.findViewById(R.id.img_background);
        ((TextView) v.findViewById(R.id.tab_text)).setText(getPageTitle(position));
        if (position == this.categoryPosition) {
            v.setBackgroundColor(this._context.getResources().getColor(R.color.white));
        }
        iv.setBackground(this._context.getResources().getDrawable(this._context.getResources().getIdentifier(Constants.backgroundCataDrawable[position], "drawable", this._context.getPackageName())));
        return v;
    }

    public int getCount() {
        return this.TITLES.length;
    }

    public Fragment currentFragment(int position) {
        return this.fragments.get(position);
    }
}

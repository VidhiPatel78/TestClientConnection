package com.text.with.sticker.textonphoto.fragment;

import android.app.Dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.adapter.FrameAdapterBG;
import com.text.with.sticker.textonphoto.interfacelistner.OnGetBackgroundReworded;
import com.text.with.sticker.textonphoto.utility.Constants;
import com.text.with.sticker.textonphoto.utility.CustomTextView;



public class FragmentBGList extends Fragment {
    String[] TITLES;
    FrameAdapterBG adapter;
    String categoryBG;
    String categoryQuote;
    boolean ch = false;
    int currentPosition;
    OnGetBackgroundReworded getBackgroundReworded;
    GridView gridView;
    String hasAuthor;
    boolean monthlyBoolean = false;
    int pos = 0;
    int positionIs;
    SharedPreferences preferences;
    String quote;
    private BroadcastReceiver removewatermark_update = new BroadcastReceiver() {
        /* class com.textonphoto.customqoutescreator.fragment.FragmentBGList.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            if (!FragmentBGList.this.preferences.getBoolean("isAdsDisabled", false)) {
                FragmentBGList.this.ch = false;
            } else if (FragmentBGList.this.ch) {
                Intent intent1 = new Intent();
                intent1.putExtra("background", FragmentBGList.this.resourceArray[FragmentBGList.this.currentPosition]);
                intent1.putExtra("categoryBG", FragmentBGList.this.categoryBG);
                intent1.putExtra("categoryQuote", FragmentBGList.this.categoryQuote);
                FragmentBGList.this.getActivity().setResult(-1, intent1);
                FragmentBGList.this.getActivity().finish();
                FragmentBGList.this.ch = false;
            }
            FragmentBGList.this.monthlyBoolean = false;
            FragmentBGList.this.yearlyBoolean = false;
        }
    };
    String[] resourceArray;
    boolean yearlyBoolean = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bg_list, container, false);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.TITLES = getResources().getStringArray(R.array.listOfManageQuotesItem);
        this.positionIs = getArguments().getInt("position");
        this.quote = getArguments().getString("quote_edit");
        this.hasAuthor = getArguments().getString("hasAuthor");
        this.categoryBG = getArguments().getString("categoryBG");
        this.categoryQuote = getArguments().getString("categoryQuote");
        this.resourceArray = setImageArray(this.positionIs);
        getActivity().registerReceiver(this.removewatermark_update, new IntentFilter("Remove_Watermark_Background"));
        this.getBackgroundReworded = (OnGetBackgroundReworded) getActivity();
        this.gridView = (GridView) view.findViewById(R.id.gridview);
        FrameAdapterBG frameAdapterBG = new FrameAdapterBG(getActivity(), this.resourceArray);
        this.adapter = frameAdapterBG;
        this.gridView.setAdapter((ListAdapter) frameAdapterBG);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class com.textonphoto.customqoutescreator.fragment.FragmentBGList.AnonymousClass2 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("background", FragmentBGList.this.resourceArray[position]);
                intent.putExtra("categoryBG", FragmentBGList.this.categoryBG);
                intent.putExtra("categoryQuote", FragmentBGList.this.categoryQuote);
                FragmentBGList.this.getActivity().setResult(-1, intent);
                FragmentBGList.this.getActivity().finish();
            }
        });
        return view;
    }

    public String[] setImageArray(int category2) {
        switch (category2) {
            case 1:
                this.pos = 1;
                this.resourceArray = getResources().getStringArray(R.array.back);
                break;
        }
        return this.resourceArray;
    }

    public void onDestroyView() {
        super.onDestroyView();
        try {
            new Thread(new Runnable() {
                /* class com.textonphoto.customqoutescreator.fragment.FragmentBGList.AnonymousClass3 */

                public void run() {
                    try {
                        Glide.get(FragmentBGList.this.getActivity()).clearDiskCache();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Glide.get(getActivity()).clearMemory();
            this.gridView = null;
            this.resourceArray = null;
            this.getBackgroundReworded = null;
            this.adapter.holder.root = null;
            this.adapter.holder.mThumbnail = null;
            this.adapter.holder.img_lock = null;
            this.adapter = null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Constants.freeMemory();
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(this.removewatermark_update);
        } catch (Exception e) {
        }
        try {
            this.gridView = null;
            this.resourceArray = null;
            this.getBackgroundReworded = null;
            this.adapter.holder.root = null;
            this.adapter.holder.mThumbnail = null;
            this.adapter.holder.img_lock = null;
            this.adapter = null;
        } catch (Exception e9) {
            e9.printStackTrace();
        }
        Constants.freeMemory();
    }

    private void usePremimum_dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(R.layout.use_pre_bckgnd_dialog);
        dialog.setCancelable(false);
        ((CustomTextView) dialog.findViewById(R.id.headerText)).setText(getActivity().getResources().getString(R.string.txtHederUse1));
        ((CustomTextView) dialog.findViewById(R.id.use_msg)).setText(getActivity().getResources().getString(R.string.txtUse1));
        Button no_thanks = (Button) dialog.findViewById(R.id.no_thanks);
        no_thanks.setTypeface(Constants.getTextTypeface(getActivity()));
        no_thanks.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.fragment.FragmentBGList.AnonymousClass4 */

            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnUse = (Button) dialog.findViewById(R.id.btnUse);
        btnUse.setTypeface(Constants.getTextTypeface(getActivity()));
        btnUse.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.fragment.FragmentBGList.AnonymousClass5 */

            public void onClick(View v) {
                FragmentBGList.this.getBackgroundReworded.ongetBackgroundReworded(FragmentBGList.this.resourceArray[FragmentBGList.this.currentPosition], FragmentBGList.this.categoryBG, FragmentBGList.this.categoryQuote);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.monthlyBoolean) {
            this.monthlyBoolean = false;
        }
        if (this.yearlyBoolean) {
            this.yearlyBoolean = false;
        }
    }
}

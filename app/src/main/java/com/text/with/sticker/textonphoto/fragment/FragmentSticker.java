package com.text.with.sticker.textonphoto.fragment;

import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.text.with.sticker.textonphoto.adapter.FrameAdapterSticker;
import com.text.with.sticker.textonphoto.interfacelistner.OnGetStickerReworded;
import com.text.with.sticker.textonphoto.utility.Constants;
import com.text.with.sticker.textonphoto.utility.CustomTextView;

public class FragmentSticker extends Fragment {
    FrameAdapterSticker adapter;
    String categoryName = "aaa";
    boolean ch = false;
    int currentPosition;
    OnGetStickerReworded getStkrReworded;
    GridView gridView;
    boolean monthlyBoolean = false;
    int pos = 0;
    SharedPreferences preferences;
    String[] resourceArray;
    boolean yearlyBoolean = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sticker_list, container, false);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.resourceArray = setImageArray(getArguments().getInt("position"));
        this.getStkrReworded = (OnGetStickerReworded) getActivity();
        this.gridView = (GridView) view.findViewById(R.id.gridview);
        FrameAdapterSticker frameAdapterSticker = new FrameAdapterSticker(getActivity(), this.resourceArray);
        this.adapter = frameAdapterSticker;
        this.gridView.setAdapter((ListAdapter) frameAdapterSticker);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class com.text.with.sticker.textonphoto.fragment.FragmentSticker.AnonymousClass1 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("sticker", FragmentSticker.this.resourceArray[position]);
                intent.putExtra("stickerCategory", FragmentSticker.this.categoryName);
                Log.e("stickerCategory", "" + FragmentSticker.this.categoryName);
                intent.putExtra("stickerType", "COLOR");
                FragmentSticker.this.getActivity().setResult(-1, intent);
                FragmentSticker.this.getActivity().finish();
            }
        });
        return view;
    }

    public String[] setImageArray(int category2) {
        switch (category2) {
            case 1:
                this.pos = 1;
                this.resourceArray = getResources().getStringArray(R.array.decoration);
                this.categoryName = "Decoration";
                break;
            case 2:
                this.pos = 2;
                this.resourceArray = getResources().getStringArray(R.array.ornaments);
                this.categoryName = "Ornaments";
                break;
            case 3:
                this.pos = 3;
                this.resourceArray = getResources().getStringArray(R.array.love);
                this.categoryName = "Love";
                break;
            case 4:
                this.pos = 4;
                this.resourceArray = getResources().getStringArray(R.array.emoji);
                this.categoryName = "Emoji";
                break;
            case 5:
                this.pos = 5;
                this.resourceArray = getResources().getStringArray(R.array.friends);
                this.categoryName = "Friendship";
                break;
            case 6:
                this.pos = 6;
                this.resourceArray = getResources().getStringArray(R.array.birthday);
                this.categoryName = "Birthday";
                break;
            case 7:
                this.pos = 7;
                this.resourceArray = getResources().getStringArray(R.array.wedding);
                this.categoryName = "Wedding";
                break;
            case 8:
                this.pos = 8;
                this.resourceArray = getResources().getStringArray(R.array.anniversary);
                this.categoryName = "Anniversay";
                break;
            case 9:
                this.pos = 9;
                this.resourceArray = getResources().getStringArray(R.array.cartoon);
                this.categoryName = "Cartoon";
                break;
            case 10:
                this.pos = 10;
                this.resourceArray = getResources().getStringArray(R.array.festival);
                this.categoryName = "Festival";
                break;
            case 11:
                this.pos = 11;
                this.resourceArray = getResources().getStringArray(R.array.food);
                this.categoryName = "Food";
                break;
        }
        return this.resourceArray;
    }

    public void onDestroyView() {
        super.onDestroyView();
        try {
            new Thread(new Runnable() {
                /* class com.text.with.sticker.textonphoto.fragment.FragmentSticker.AnonymousClass2 */

                public void run() {
                    try {
                        Glide.get(FragmentSticker.this.getActivity()).clearDiskCache();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Glide.get(getActivity()).clearMemory();
            this.gridView = null;
            this.resourceArray = null;
            this.getStkrReworded = null;
            if (this.adapter.holder.root != null) {
                this.adapter.holder.root = null;
                this.adapter.holder.mThumbnail = null;
                this.adapter.holder.img_lock = null;
            }
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
            this.gridView = null;
            this.resourceArray = null;
            this.getStkrReworded = null;
            if (this.adapter.holder.root != null) {
                this.adapter.holder.root = null;
                this.adapter.holder.mThumbnail = null;
                this.adapter.holder.img_lock = null;
            }
            this.adapter = null;
        } catch (Exception e9) {
            e9.printStackTrace();
        }
        Constants.freeMemory();
    }

    private void usePremimum_dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.use_pre_bckgnd_dialog);
        dialog.setCancelable(false);
        ((CustomTextView) dialog.findViewById(R.id.headerText)).setText(getActivity().getResources().getString(R.string.txtHederUse2));
        ((CustomTextView) dialog.findViewById(R.id.use_msg)).setText(getActivity().getResources().getString(R.string.txtUse2));
        Button no_thanks = (Button) dialog.findViewById(R.id.no_thanks);
        no_thanks.setTypeface(Constants.getTextTypeface(getActivity()));
        no_thanks.setOnClickListener(new View.OnClickListener() {
            /* class com.text.with.sticker.textonphoto.fragment.FragmentSticker.AnonymousClass3 */

            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnUse = (Button) dialog.findViewById(R.id.btnUse);
        btnUse.setTypeface(Constants.getTextTypeface(getActivity()));
        btnUse.setOnClickListener(new View.OnClickListener() {
            /* class com.text.with.sticker.textonphoto.fragment.FragmentSticker.AnonymousClass4 */

            public void onClick(View v) {
                FragmentSticker.this.getStkrReworded.ongetStickerReworded(FragmentSticker.this.resourceArray[FragmentSticker.this.currentPosition], FragmentSticker.this.categoryName, "HUE");
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

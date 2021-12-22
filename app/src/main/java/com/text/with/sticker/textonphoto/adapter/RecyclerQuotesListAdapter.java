package com.text.with.sticker.textonphoto.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.activity.AddTextQuotesActivity;
import com.text.with.sticker.textonphoto.activity.QuotesListActivity;
import com.text.with.sticker.textonphoto.activity.SaveActivity;
import com.text.with.sticker.textonphoto.db.DatabaseHandler;
import com.text.with.sticker.textonphoto.utility.CustomTextView;
import com.text.with.sticker.textonphoto.utility.FontTextView;
import com.text.with.sticker.textonphoto.utility.QuotesInfo;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class RecyclerQuotesListAdapter extends RecyclerView.Adapter<RecyclerQuotesListAdapter.ViewHolder> {
    Context context;
    String filename = "";
    String[] imagesColorArr = {"e8", "e13", "f7", "f13", "fa8", "#ffffff", "fa11", "fn15", "j15", "#1ab4f5", "#fff500", "#fce6bb", "#ff5858", "#00ff4e", "#bd4ef7", "l3", "m11", "m18", "#ff8c10", "#03fdf2", "#fe44a0", "#a2ec89", "m19", "sm9", "t2", "wd17"};
    String[] likeArray;
    int[] noOfIndex;
    SharedPreferences preferences;
    ArrayList<QuotesInfo> quotesInfoList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        ToggleButton imgLike;
        RelativeLayout lay_Copy;
        RelativeLayout lay_Edit;
        FrameLayout lay_Imgframe;
        RelativeLayout lay_Like;
        RelativeLayout lay_Save;
        RelativeLayout lay_Share;
        CustomTextView tvLike;
        FontTextView tvQuotes;
        View view1;

        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.cardview);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            this.view1 = view.findViewById(R.id.view);
            this.tvQuotes = (FontTextView) view.findViewById(R.id.tvQuotes);
            this.lay_Imgframe = (FrameLayout) view.findViewById(R.id.lay_Imgframe);
            this.lay_Like = (RelativeLayout) view.findViewById(R.id.lay_Like);
            this.lay_Edit = (RelativeLayout) view.findViewById(R.id.lay_Edit);
            this.lay_Save = (RelativeLayout) view.findViewById(R.id.lay_Save);
            this.lay_Copy = (RelativeLayout) view.findViewById(R.id.lay_Copy);
            this.lay_Share = (RelativeLayout) view.findViewById(R.id.lay_Share);
            this.imgLike = (ToggleButton) view.findViewById(R.id.imgLike);
            this.tvLike = (CustomTextView) view.findViewById(R.id.tvLike);
        }
    }

    public RecyclerQuotesListAdapter(Context context2, ArrayList<QuotesInfo> quotesInfoList2) {
        this.context = context2;
        this.quotesInfoList = quotesInfoList2;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context2);
        this.likeArray = new String[quotesInfoList2.size()];
        this.noOfIndex = new int[quotesInfoList2.size()];
        for (int i = 0; i < quotesInfoList2.size(); i++) {
            this.likeArray[i] = quotesInfoList2.get(i).getQUOTES_LIKED();
            this.noOfIndex[i] = -1;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int position) {
        return (long) position;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.quotesInfoList.size();
    }

    public void onBindViewHolder(final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.tvQuotes.setText(this.quotesInfoList.get(position).getQUOTES_TEXT());
        if (this.likeArray[position].equals("Like")) {
            viewHolder.imgLike.setBackgroundResource(R.drawable.like_no);
            viewHolder.imgLike.setChecked(false);
            viewHolder.tvLike.setText(this.quotesInfoList.get(position).getQUOTES_LIKED());
        } else {
            viewHolder.imgLike.setBackgroundResource(R.drawable.like_yes);
            viewHolder.imgLike.setChecked(true);
            viewHolder.tvLike.setText(this.quotesInfoList.get(position).getQUOTES_LIKED());
        }
        changeBackground(viewHolder.imageView, viewHolder.view1, viewHolder.tvQuotes, this.noOfIndex[position]);
        viewHolder.lay_Imgframe.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass1 */

            public void onClick(View v) {
                RecyclerQuotesListAdapter.this.noOfIndex[position] = new Random().nextInt(RecyclerQuotesListAdapter.this.imagesColorArr.length);
                RecyclerQuotesListAdapter.this.changeBackground(viewHolder.imageView, viewHolder.view1, viewHolder.tvQuotes, RecyclerQuotesListAdapter.this.noOfIndex[position]);
            }
        });
        this.noOfIndex[position] = new Random().nextInt(this.imagesColorArr.length);
        changeBackground(viewHolder.imageView, viewHolder.view1, viewHolder.tvQuotes, this.noOfIndex[position]);
        viewHolder.lay_Edit.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass2 */

            public void onClick(View v) {
                Intent quotesListActivity = new Intent(RecyclerQuotesListAdapter.this.context, AddTextQuotesActivity.class);
                quotesListActivity.putExtra("modeOfChosingPic", "chooserActivity");
                quotesListActivity.putExtra("quote_edit", RecyclerQuotesListAdapter.this.quotesInfoList.get(position).getQUOTES_TEXT());
                quotesListActivity.putExtra("positn", "main");
                int index = RecyclerQuotesListAdapter.this.noOfIndex[position];
                if (index != -1) {
                    if (index == 9 || index == 10 || index == 11 || index == 12 || index == 13 || index == 14 || index == 18 || index == 19 || index == 20 || index == 21) {
                        quotesListActivity.putExtra("profile", "color");
                    } else if (index == 5) {
                        quotesListActivity.putExtra("profile", "color");
                    } else {
                        quotesListActivity.putExtra("profile", "bg");
                    }
                    quotesListActivity.putExtra("background", RecyclerQuotesListAdapter.this.imagesColorArr[index]);
                } else {
                    quotesListActivity.putExtra("profile", "color");
                    quotesListActivity.putExtra("background", "#000000");
                }
                RecyclerQuotesListAdapter.this.context.startActivity(quotesListActivity);
            }
        });
        viewHolder.lay_Like.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass3 */

            public void onClick(View v) {
                if (viewHolder.imgLike.isChecked()) {
                    viewHolder.imgLike.setBackgroundResource(R.drawable.like_no);
                    RecyclerQuotesListAdapter recyclerQuotesListAdapter = RecyclerQuotesListAdapter.this;
                    recyclerQuotesListAdapter.setQuotesLiked(recyclerQuotesListAdapter.quotesInfoList.get(position).getQUOTES_ID(), "Like");
                    viewHolder.imgLike.setChecked(false);
                    viewHolder.tvLike.setText("Like");
                    RecyclerQuotesListAdapter.this.likeArray[position] = "Like";
                    return;
                }
                viewHolder.imgLike.setBackgroundResource(R.drawable.like_yes);
                RecyclerQuotesListAdapter recyclerQuotesListAdapter2 = RecyclerQuotesListAdapter.this;
                recyclerQuotesListAdapter2.setQuotesLiked(recyclerQuotesListAdapter2.quotesInfoList.get(position).getQUOTES_ID(), "Liked");
                viewHolder.imgLike.setChecked(true);
                viewHolder.tvLike.setText("Liked");
                RecyclerQuotesListAdapter.this.likeArray[position] = "Liked";
            }
        });
        viewHolder.lay_Save.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass4 */

            public void onClick(View v) {
                RecyclerQuotesListAdapter.this.saveImageInGallery(viewHolder.lay_Imgframe, false, position);
            }
        });
        viewHolder.lay_Copy.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass5 */

            @SuppressLint({"WrongConstant", "ShowToast"})
            public void onClick(View v) {
                ((ClipboardManager) RecyclerQuotesListAdapter.this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", RecyclerQuotesListAdapter.this.quotesInfoList.get(position).getQUOTES_TEXT()));
                Toast.makeText(RecyclerQuotesListAdapter.this.context, RecyclerQuotesListAdapter.this.context.getResources().getString(R.string.txtCopy), 1).show();
            }
        });
        viewHolder.lay_Share.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass6 */

            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(RecyclerQuotesListAdapter.this.context, viewHolder.lay_Share);
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass6.AnonymousClass1 */

                    @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.imageShare:
                                RecyclerQuotesListAdapter.this.saveImageInGallery(viewHolder.lay_Imgframe, true, position);
                                return true;
                            case R.id.textShare:
                                RecyclerQuotesListAdapter.this.shareImage("", RecyclerQuotesListAdapter.this.quotesInfoList.get(position).getQUOTES_TEXT(), false);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        return new ViewHolder(LayoutInflater.from(arg0.getContext()).inflate(R.layout.row_layout_quotes, arg0, false));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void changeBackground(ImageView imageView, View view1, TextView tvQuotes, int index) {
        Integer valueOf = Integer.valueOf((int) R.drawable.white);
        if (index == -1) {
            Glide.with(this.context).load(valueOf).into(imageView);
            imageView.setColorFilter(Color.parseColor("#000000"));
            view1.setBackgroundColor(Color.parseColor("#00000000"));
            tvQuotes.setTextColor(-1);
        } else if (index == 9 || index == 10 || index == 11 || index == 12 || index == 13 || index == 14 || index == 18 || index == 19 || index == 20 || index == 21) {
            Glide.with(this.context).load(valueOf).into(imageView);
            imageView.setColorFilter(Color.parseColor(this.imagesColorArr[index]));
            view1.setBackgroundColor(Color.parseColor("#00000000"));
            tvQuotes.setTextColor(-1);
        } else if (index == 5) {
            Glide.with(this.context).load(valueOf).into(imageView);
            imageView.setColorFilter(Color.parseColor(this.imagesColorArr[index]));
            view1.setBackgroundColor(Color.parseColor("#00000000"));
            tvQuotes.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        } else {
            imageView.setColorFilter((ColorFilter) null);
            Glide.with(this.context).load(Integer.valueOf(this.context.getResources().getIdentifier(this.imagesColorArr[index], "drawable", this.context.getPackageName()))).into(imageView);
            view1.setBackgroundColor(Color.parseColor("#65000000"));
            tvQuotes.setTextColor(-1);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setQuotesLiked(int quotesId, String liked) {
        DatabaseHandler dh = DatabaseHandler.getDbHandler(this.context);
        dh.updateQuotesLikedStatus(quotesId, liked);
        dh.close();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void saveImageInGallery(FrameLayout lay_Imgframe, boolean checkSaveShare, int position) {
        saveBitmap(viewToBitmap(lay_Imgframe), true, checkSaveShare, position);
    }

    private Bitmap viewToBitmap(View frameLayout) {
        try {
            Bitmap b = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
            frameLayout.draw(new Canvas(b));
            return b;
        } finally {
            frameLayout.destroyDrawingCache();
        }
    }

    private void saveBitmap(final Bitmap bitmap, final boolean inPNG, final boolean checkSaveShare, final int position) {
        final ProgressDialog pd = new ProgressDialog(this.context);
        pd.setMessage(this.context.getResources().getString(R.string.plzwait));
        pd.setCancelable(false);
        pd.show();
        new Thread(new Runnable() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass7 */

            @SuppressLint("WrongConstant")
            public void run() {
                String photoFile;
                try {
                    File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/Text On Photo");
                    if (pictureFileDir.exists() || pictureFileDir.mkdirs()) {
                        String photoFile2 = "text_on_photo_" + System.currentTimeMillis();
                        if (inPNG) {
                            photoFile = photoFile2 + ".png";
                        } else {
                            photoFile = photoFile2 + ".jpg";
                        }
                        if (QuotesListActivity.pathsOfFileUri[0].equals("")) {
                            RecyclerQuotesListAdapter.this.filename = pictureFileDir.getPath() + File.separator + photoFile;
                        } else {
                            RecyclerQuotesListAdapter.this.filename = QuotesListActivity.pathsOfFileUri[0];
                        }
                        File pictureFile = new File(RecyclerQuotesListAdapter.this.filename);
                        try {
                            if (!pictureFile.exists()) {
                                pictureFile.createNewFile();
                            }
                            FileOutputStream ostream = new FileOutputStream(pictureFile);
                            if (inPNG) {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            } else {
                                Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                                Canvas canvas = new Canvas(newBitmap);
                                canvas.drawColor(-1);
                                canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                newBitmap.recycle();
                            }
                            ostream.flush();
                            ostream.close();
                            bitmap.recycle();
                            RecyclerQuotesListAdapter.this.context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(pictureFile)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Thread.sleep(1000);
                        pd.dismiss();
                        return;
                    }
                    Log.d("", "Can't create directory to save image.");
                    Toast.makeText(RecyclerQuotesListAdapter.this.context, RecyclerQuotesListAdapter.this.context.getResources().getString(R.string.create_dir_err),  1).show();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerQuotesListAdapter.AnonymousClass8 */

            @SuppressLint("WrongConstant")
            public void onDismiss(DialogInterface dialog) {
                if (checkSaveShare && !RecyclerQuotesListAdapter.this.filename.equals("")) {
                    RecyclerQuotesListAdapter recyclerQuotesListAdapter = RecyclerQuotesListAdapter.this;
                    recyclerQuotesListAdapter.shareImage(recyclerQuotesListAdapter.filename, "", checkSaveShare);
                } else if (RecyclerQuotesListAdapter.this.filename.equals("")) {
                    Toast.makeText(RecyclerQuotesListAdapter.this.context, RecyclerQuotesListAdapter.this.context.getResources().getString(R.string.error).toString(), 0).show();
                } else {
                    Intent quotesListActivity = new Intent(RecyclerQuotesListAdapter.this.context, SaveActivity.class);
                    quotesListActivity.putExtra("uri", RecyclerQuotesListAdapter.this.filename);
                    quotesListActivity.putExtra("way", "AdapterQuote");
                    quotesListActivity.putExtra("quote_edit", RecyclerQuotesListAdapter.this.quotesInfoList.get(position).getQUOTES_TEXT());
                    int index = RecyclerQuotesListAdapter.this.noOfIndex[position];
                    if (index != -1) {
                        if (index == 9 || index == 10 || index == 11 || index == 12 || index == 13 || index == 14 || index == 18 || index == 19 || index == 20 || index == 21) {
                            quotesListActivity.putExtra("profile", "color");
                            quotesListActivity.putExtra("txtColor", "white");
                        } else if (index == 5) {
                            quotesListActivity.putExtra("profile", "color");
                            quotesListActivity.putExtra("txtColor", "black");
                        } else {
                            quotesListActivity.putExtra("profile", "bg");
                            quotesListActivity.putExtra("txtColor", "white");
                        }
                        quotesListActivity.putExtra("background", RecyclerQuotesListAdapter.this.imagesColorArr[index]);
                    } else {
                        quotesListActivity.putExtra("profile", "color");
                        quotesListActivity.putExtra("background", "#000000");
                        quotesListActivity.putExtra("txtColor", "white");
                    }
                    RecyclerQuotesListAdapter.this.context.startActivity(quotesListActivity);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void shareImage(String filePath, String strQuotes, boolean deleteIs) {
        String sAux;
        File file = null;
        if (!filePath.equals("")) {
            file = new File(Uri.parse(filePath).getPath());
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.SUBJECT", this.context.getResources().getString(R.string.app_name));
        if (filePath.equals("")) {
            sAux = strQuotes;
        } else {
            sAux = this.context.getResources().getString(R.string.sharetext) + " " + this.context.getResources().getString(R.string.app_name) + ". " + this.context.getResources().getString(R.string.sharetext1) + "https://play.google.com/store/apps/details?id=" + this.context.getPackageName();
        }
        intent.putExtra("android.intent.extra.TEXT", sAux);
        if (!filePath.equals("")) {
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
        }
        Context context2 = this.context;
        context2.startActivity(Intent.createChooser(intent, context2.getString(R.string.share_via).toString()));
        if (!filePath.equals("") && deleteIs) {
            QuotesListActivity.pathsOfFileUri[0] = filePath;
        }
    }
}

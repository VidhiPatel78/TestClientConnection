package com.text.with.sticker.textonphoto.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.text.Selection;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.text.with.sticker.textonphoto.APPUtility;
import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.activity.GPUImageFilterTools;
import com.text.with.sticker.textonphoto.adapter.StyleAdepter;
import com.text.with.sticker.textonphoto.colorpicker.LineColorPicker;
import com.text.with.sticker.textonphoto.colorpicker.OnColorChangedListener;
import com.text.with.sticker.textonphoto.db.DatabaseHandler;
import com.text.with.sticker.textonphoto.db.Quotes;
import com.text.with.sticker.textonphoto.db.QuotesSelect;
import com.text.with.sticker.textonphoto.interfacelistner.GetColorListener;
import com.text.with.sticker.textonphoto.interfacelistner.GetTemplateListener;
import com.text.with.sticker.textonphoto.interfacelistner.OnGetImgBackgndSelect;
import com.text.with.sticker.textonphoto.quotesedit.ComponentInfo;
import com.text.with.sticker.textonphoto.quotesedit.CustomShaderSpan;
import com.text.with.sticker.textonphoto.quotesedit.CustomShadowSpan;
import com.text.with.sticker.textonphoto.quotesedit.CustomTypefaceSpan;
import com.text.with.sticker.textonphoto.quotesedit.MultiTouchListener;
import com.text.with.sticker.textonphoto.quotesedit.PictureConstant;
import com.text.with.sticker.textonphoto.quotesedit.RepeatListener;
import com.text.with.sticker.textonphoto.quotesedit.ResizableImageview;
import com.text.with.sticker.textonphoto.quotesedit.SelectedTextData;
import com.text.with.sticker.textonphoto.utility.Constants;
import com.text.with.sticker.textonphoto.utility.CustomTextView;
import com.text.with.sticker.textonphoto.utility.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddTextQuotesActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, GetColorListener, GetTemplateListener, OnGetImgBackgndSelect, MultiTouchListener.TouchCallbackListener, ResizableImageview.TouchEventListener {
    private static final int EDIT_QUOTE_RESULT = 901;

    private static final int GET_STICKER = 906;


    private static final int WRITE_QUOTE_RESULT = 902;
    public static Bitmap bitmapOriginal;
    public static AddTextQuotesActivity c;
    public static boolean isTextTouchListener = true;
    int a;
    RelativeLayout alledit_ll;
    double angle = 0.0d;
    ArrayList<SelectedTextData> arrayfortv = new ArrayList<>();
    int b;
    ImageView back_arrow_add_quotes;
    int backgroundColor = -1;
    Bitmap bb;
    Bitmap bitRel;
    Bitmap bitmap;


    Button bold;
    ImageView btn_down;
    ImageView btn_left;
    ImageView btn_right;
    ImageView btn_top;
    SpannableStringBuilder builder;
    float cX = 0.0f;
    float cY = 0.0f;
    private String categoryBG = "";
    private String categoryQuote = "";
    private String categorySticker = "";
    boolean ch = false;
    int chang = 3;
    CustomTextView clr_opacity_txt;
    RelativeLayout cntrls_stkr_lay;
    RelativeLayout cntrls_stkrclr_lay;
    /* access modifiers changed from: private */
    public int color;
    LineColorPicker colorPicker;
    ImageView color_;
    CustomTextView color_punch;
    RelativeLayout color_rel;
    CustomTextView color_txt;
    RelativeLayout complete_img;
    CustomTextView contrl_txt;
    Button controll_btn_stckr;
    RelativeLayout controller_sticker;
    int count;
    double dAngle = 0.0d;
    DatabaseHandler db;
    DatabaseHandler dbHelper;
    String defaultText = "Tap on word to highlight";
    int defaultcolor_all = -1;
    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @SuppressLint("WrongConstant")
        public void onClick(View v) {
            AddTextQuotesActivity.this.selectFocus = true;
            AddTextQuotesActivity.this.ft1 = "";
            AddTextQuotesActivity.this.shr1 = "null";
            View view = AddTextQuotesActivity.this.getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) AddTextQuotesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            ((View) v.getParent()).setVisibility(8);
            ((TextView) ((ViewGroup) v.getParent()).getChildAt(0)).setText("Ultimate Textgram text for demo");
            if (AddTextQuotesActivity.this.textView.getPaintFlags() == 1289 || AddTextQuotesActivity.this.textView.getPaintFlags() == 1305 || AddTextQuotesActivity.this.textView.getPaintFlags() == 1297) {
                AddTextQuotesActivity.this.textView.setPaintFlags(AddTextQuotesActivity.this.textView.getPaintFlags() & -9);
                AddTextQuotesActivity.this.textView.setPaintFlags(AddTextQuotesActivity.this.textView.getPaintFlags() & -17);
            }
            if (AddTextQuotesActivity.this.alledit_ll.getVisibility() == 0) {
                AddTextQuotesActivity.this.menu_ll.setVisibility(0);
                AddTextQuotesActivity.this.alledit_ll.setVisibility(8);
              //  AddTextQuotesActivity.this.sb_effectsfilter.setVisibility(0);



            }
            if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                AddTextQuotesActivity.this.arrayfortv.clear();
            }
            if (AddTextQuotesActivity.this.rl.getVisibility() == 0) {
                AddTextQuotesActivity.this.text_tv.clearFocus();
                Selection.removeSelection(AddTextQuotesActivity.this.text_tv.getText());
                AddTextQuotesActivity.this.text_tv.setCursorVisible(false);
                AddTextQuotesActivity.this.fortext.removeView(AddTextQuotesActivity.this.rl);
            }
        }
    };
    ImageView delete_iv;
    Button done_add_quotes;
    String drawableName;
    Button duplicate;
    boolean ed = false;
    private View.OnClickListener editTextClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String quote = "";
            if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                AddTextQuotesActivity addTextQuotesActivity = AddTextQuotesActivity.this;
                quote = addTextQuotesActivity.setSelectionAll1(addTextQuotesActivity.text_tv);
            }
            AddTextQuotesActivity.this.selectFocus = false;
            AddTextQuotesActivity.this.storedArray.clear();
            Intent intent = new Intent(AddTextQuotesActivity.this, EditQuoteActivity.class);
            intent.putExtra("quote", quote);
            AddTextQuotesActivity.this.startActivityForResult(intent, AddTextQuotesActivity.EDIT_QUOTE_RESULT);
        }
    };
    CustomTextView edit_ivTxt;
    CustomTextView effect_txt;
    int end;
    int existingcolor = -1;
    private File f = null;
    String filename;
    GPUImageFilterTools.FilterType filterType = GPUImageFilterTools.FilterType.GAUSSIAN_BLUR;
    boolean flag = false;
    boolean flag2 = false;
    boolean flgPunch;
    CustomTextView font_punch;
    RelativeLayout font_rel;
    CustomTextView font_txt;
    ImageView fonts;

    CustomTextView format_punch;
    CustomTextView format_txt;
    LinearLayout formatall_type;
    RelativeLayout fortext;
    String ft1 = "";
    boolean ftrue = true;
    Button g1;
    Button g2;
    Button g3;
    GestureDetector gd;
    GPUImageView gpuImageview;
    SeekBar hue_seekbar;
    ImageView img_color_punch;
    ImageView img_color_txt;
    ImageView img_font_punch;
    ImageView img_font_txt;
    ImageView img_format_punch;
    ImageView img_format_txt;
    ImageView img_opacity;
    ImageView img_shader_punch;
    ImageView img_shader_txt;
    ImageView img_shadow_punch;
    ImageView img_shadow_txt;
    String intent_quote;
    boolean isBlurLayVisible = true;
    boolean isStickerLayVisible = true;
    boolean isUpadted = false;
    boolean isfrst = true;
    Button italic;
    RelativeLayout la_color;
    RelativeLayout la_fonts;
    RelativeLayout la_shader;
    RelativeLayout la_shadow;
    RelativeLayout la_size;
    RelativeLayout lay_colorfilter;
    RelativeLayout lay_hue;
    ImageView list_of_brnd;
    ImageView list_of_sticker;

    GPUImageFilter mFilter;
    GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {
            AddTextQuotesActivity.this.selectFocus = true;
            if (!view.getTag().equals("move_iv")) {
                if (!view.getTag().equals("scale_iv") && view.getTag().equals("rotate_iv")) {
                    Log.e("11111", "sticker view action move");
                    switch (event.getAction()) {
                        case 0:
                            AddTextQuotesActivity.this.hideEditRel();
                            Rect rect = new Rect();
                            ((View) view.getParent()).getGlobalVisibleRect(rect);
                            AddTextQuotesActivity.this.cX = rect.exactCenterX();
                            AddTextQuotesActivity.this.cY = rect.exactCenterY();
                            AddTextQuotesActivity.this.vAngle = (double) ((View) view.getParent()).getRotation();
                            AddTextQuotesActivity addTextQuotesActivity = AddTextQuotesActivity.this;
                            addTextQuotesActivity.tAngle = (Math.atan2((double) (addTextQuotesActivity.cY - event.getRawY()), (double) (AddTextQuotesActivity.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                            AddTextQuotesActivity addTextQuotesActivity2 = AddTextQuotesActivity.this;
                            addTextQuotesActivity2.dAngle = addTextQuotesActivity2.vAngle - AddTextQuotesActivity.this.tAngle;
                            break;
                        case 1:
                            AddTextQuotesActivity.this.checkandShowEditRel();
                            break;
                        case 2:
                            AddTextQuotesActivity addTextQuotesActivity3 = AddTextQuotesActivity.this;
                            addTextQuotesActivity3.angle = (Math.atan2((double) (addTextQuotesActivity3.cY - event.getRawY()), (double) (AddTextQuotesActivity.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                            float rotation = (float) (AddTextQuotesActivity.this.angle + AddTextQuotesActivity.this.dAngle);
                            if (rotation > -5.0f && rotation < 5.0f) {
                                rotation = 0.0f;
                            }
                            Log.i("rotation", "" + (AddTextQuotesActivity.this.angle + AddTextQuotesActivity.this.dAngle));
                            ((View) view.getParent()).setRotation(rotation);
                            break;
                    }
                }
                Log.e("22222", "sticker view action move");
                switch (event.getAction()) {
                    case 0:
                        view.bringToFront();
                        AddTextQuotesActivity.this.hideEditRel();
                        AddTextQuotesActivity.this.move_orgX = event.getRawX();
                        AddTextQuotesActivity.this.move_orgY = event.getRawY();
                        AddTextQuotesActivity.this.rlX = ((View) view.getParent()).getX();
                        AddTextQuotesActivity.this.rlY = ((View) view.getParent()).getY();
                        break;
                    case 1:
                        AddTextQuotesActivity.this.checkandShowEditRel();
                        break;
                    case 2:
                        Log.e("scale_iv", "sticker view action move");
                        float offsetX = event.getRawX() - AddTextQuotesActivity.this.move_orgX;
                        float offsetY = event.getRawY() - AddTextQuotesActivity.this.move_orgY;
                        ((View) view.getParent()).setX(AddTextQuotesActivity.this.rlX);
                        ((View) view.getParent()).setY(AddTextQuotesActivity.this.rlY);
                        int nw_width = (int) (((float) ((View) view.getParent()).getLayoutParams().width) + offsetX);
                        int nw_Height = (int) (((float) ((View) view.getParent()).getLayoutParams().height) + offsetY);
                        if (nw_width > AddTextQuotesActivity.this.fortext.getWidth()) {
                            nw_width = AddTextQuotesActivity.this.fortext.getWidth();
                        }
                        if (nw_Height > AddTextQuotesActivity.this.fortext.getHeight()) {
                            nw_Height = AddTextQuotesActivity.this.fortext.getHeight();
                        }
                        AddTextQuotesActivity.this.move_orgX = event.getRawX();
                        AddTextQuotesActivity.this.move_orgY = event.getRawY();
                        if (((float) nw_width) >= AddTextQuotesActivity.this.view_width && ((float) nw_Height) >= AddTextQuotesActivity.this.view_height) {
                            ((View) view.getParent()).getLayoutParams().width = nw_width;
                            ((View) view.getParent()).getLayoutParams().height = nw_Height;
                            ((View) view.getParent()).postInvalidate();
                            ((View) view.getParent()).requestLayout();
                            AddTextQuotesActivity.this.updateTextSizeonScale(Math.abs(nw_width), Math.abs(nw_Height));
                            break;
                        } else {
                            return true;
                        }
                }
            }
            Log.e("333333", "sticker view action move");
            switch (event.getAction()) {
                case 0:
                    AddTextQuotesActivity.this.move_orgX = event.getRawX();
                    AddTextQuotesActivity.this.move_orgY = event.getRawY();
                    break;
                case 1:
                    AddTextQuotesActivity.this.checkandShowEditRel();
                    break;
                case 2:
                    Float offsetY2 = Float.valueOf(event.getRawY() - AddTextQuotesActivity.this.move_orgY);
                    ((View) view.getParent()).setX(((View) view.getParent()).getX() + (event.getRawX() - AddTextQuotesActivity.this.move_orgX));
                    ((View) view.getParent()).setY(((View) view.getParent()).getY() + offsetY2.floatValue());
                    AddTextQuotesActivity.this.move_orgX = event.getRawX();
                    AddTextQuotesActivity.this.move_orgY = event.getRawY();
                    break;
            }
            return true;
        }
    };
    int max = 0;
    LinearLayout menu_ll;
    int min = 0;
    String modeClrSelection = "textclr";
    String modeOfChosingPic;
    String modefont = "";
    String modefontselection = "textfont";
    String modeformatselection = "textformat";
    String modeshaderselection = "textshader";
    String modeshadowselection = "textshadow";
    ImageView move_iv;
    float move_orgX = -1.0f;
    float move_orgY = -1.0f;
    int oldlength = 0;
    String[] pallete = {"#ffffff", "#cccccc", "#999999", "#666666", "#000000", "#ffd700", "#daa520", "#b8860b", "#b8860b", "#ccff66", "#adff2f", "#00ff00", "#32cd32", "#3cb371", "#99cccc", "#66cccc", "#339999", "#669999", "#006666", "#ffcccc", "#ff9999", "#ff6666", "#ff3333", "#cc0033"};
    RelativeLayout.LayoutParams params_rl;
    CustomTextView picture_txt;
    String pos;
    SharedPreferences preferences;
    float r;
    float rat1;
    RelativeLayout re_addnewText;
    RelativeLayout re_sticker;

    RecyclerView recyclerView;

    RelativeLayout rl;
    float rlX;
    float rlY;
    ImageView rotate_iv;

  //  SeekBar sb_effectsfilter;

    String sc_hght1 = "200";
    String sc_wdth1 = "200";
    ImageView scale_iv;
    float screenHeight;
    float screenWidth;


    Button sd_color;
    boolean seekflag = true;
    boolean selectFocus = true;
    boolean selecttemplate = true;
    Button sh;
    Button sh1;
    Button sh10;
    Button sh2;
    Button sh3;
    Button sh4;
    Button sh5;
    Button sh6;
    Button sh7;
    Button sh8;
    Button sh9;
    ImageView shader;
    CustomTextView shader_punch;
    RelativeLayout shader_rel;
    CustomTextView shader_txt;
    ImageView shadow;
    CustomTextView shadow_punch;
    RelativeLayout shadow_rel;
    SeekBar shadow_seekbar;
    CustomTextView shadow_txt;
    int shadowcolor = ViewCompat.MEASURED_STATE_MASK;
    String shr1 = "null";
    ImageView size;
    String sizeSt1 = "20";
    SpannableString spannableString;
    int start;
    CustomTextView sticker_txt;
    ArrayList<SelectedTextData> storedArray = new ArrayList<>();
    Button strike;

    double tAngle = 0.0d;
    RelativeLayout tab_clrs_stkr;
    RelativeLayout tab_cntrl_stkr;
    RelativeLayout tab_font_punch;
    RelativeLayout tab_font_txt;
    RelativeLayout tab_format_punch;
    RelativeLayout tab_format_txt;
    RelativeLayout tab_punch;
    RelativeLayout tab_shader_punch;
    RelativeLayout tab_shader_txt;
    RelativeLayout tab_shadow_punch;
    RelativeLayout tab_shadow_txt;
    RelativeLayout tab_text;
    int temp_postn = 1;
    String template = "t1";
    int textSizeOffset = 5;
    private View.OnTouchListener textTouchListener = new View.OnTouchListener() {
        @SuppressLint("WrongConstant")
        public boolean onTouch(View view, MotionEvent event) {
            AddTextQuotesActivity.this.removeImageViewControll();
            AddTextQuotesActivity.this.textView.setLongClickable(false);
            if (Build.VERSION.SDK_INT >= 21) {
                AddTextQuotesActivity.this.textView.setShowSoftInputOnFocus(false);
            }
            AddTextQuotesActivity.this.setDefault();
            if (AddTextQuotesActivity.this.rl.getChildAt(1).getVisibility() == 0) {
                if (AddTextQuotesActivity.this.flag) {
                    AddTextQuotesActivity.this.textView.setCursorVisible(false);
                } else {
                    AddTextQuotesActivity.this.textView.setCursorVisible(false);
                    AddTextQuotesActivity.this.flag = true;
                }
            }
            switch (event.getAction()) {
                case 0:
                    ((View) view.getParent()).bringToFront();
                    AddTextQuotesActivity.this.ed = false;
                    if (AddTextQuotesActivity.this.textView != null) {
                        int noch = ((ViewGroup) AddTextQuotesActivity.this.textView.getParent()).getChildCount();
                        Log.e("22222", "xxxx" + noch);
                        for (int i = 1; i < noch; i++) {
                            ((ViewGroup) AddTextQuotesActivity.this.textView.getParent()).getChildAt(i).setVisibility(4);
                        }
                    }
                    int noch1 = ((ViewGroup) view.getParent()).getChildCount();
                    for (int i2 = 1; i2 < noch1; i2++) {
                        Log.e("33333", "xxxx" + noch1);
                        View child = ((ViewGroup) view.getParent()).getChildAt(i2);
                        if (i2 != 3) {
                            child.setVisibility(0);
                        }
                    }
                    AddTextQuotesActivity.this.checkandShowEditRel();
                    AddTextQuotesActivity.this.textView = (EditText) view;
                    AddTextQuotesActivity.this.flgPunch = true;
                    AddTextQuotesActivity.this.move_orgX = event.getRawX();
                    AddTextQuotesActivity.this.move_orgY = event.getRawY();
                    break;
                case 1:
                    AddTextQuotesActivity.this.checkandShowEditRel();
                    if (AddTextQuotesActivity.this.flgPunch) {
                        AddTextQuotesActivity.this.textView.setCursorVisible(false);
                        break;
                    } else {
                        AddTextQuotesActivity.this.textView.setCursorVisible(false);
                        break;
                    }
                case 2:
                    AddTextQuotesActivity.this.flgPunch = false;
                    if (AddTextQuotesActivity.this.alledit_ll.getVisibility() == 0) {
                        AddTextQuotesActivity.this.menu_ll.setVisibility(0);
                        AddTextQuotesActivity.this.alledit_ll.setVisibility(8);
                    }
                    AddTextQuotesActivity.this.textView.setCursorVisible(false);
                    float offsetY = event.getRawY() - AddTextQuotesActivity.this.move_orgY;
                    ((View) view.getParent()).setX(((View) view.getParent()).getX() + (event.getRawX() - AddTextQuotesActivity.this.move_orgX));
                    ((View) view.getParent()).setY(((View) view.getParent()).getY() + offsetY);
                    AddTextQuotesActivity.this.move_orgX = event.getRawX();
                    AddTextQuotesActivity.this.move_orgY = event.getRawY();
                    break;
            }
            return false;
        }
    };
    EditText textView = null;
    String text_angle_1 = "0";
    EditText text_tv;
    RelativeLayout top_option;
    SeekBar transparency_seekbar;
    Typeface ttD;
    Typeface ttf1;
    Typeface ttf10;
    Typeface ttf11;
    Typeface ttf12;
    Typeface ttf13;
    Typeface ttf14;
    Typeface ttf15;
    Typeface ttf16;
    Typeface ttf17;
    Typeface ttf18;
    Typeface ttf19;
    Typeface ttf2;
    Typeface ttf20;
    Typeface ttf21;
    Typeface ttf22;
    Typeface ttf23;
    Typeface ttf24;
    Typeface ttf25;
    Typeface ttf26;
    Typeface ttf27;
    Typeface ttf28;
    Typeface ttf29;
    Typeface ttf3;
    Typeface ttf4;
    Typeface ttf5;
    Typeface ttf6;
    Typeface ttf7;
    Typeface ttf8;
    Typeface ttf9;
    Button underline;
    double vAngle = 0.0d;
    float view_height;
    float view_width;
    String x1 = "0.0f";
    String y1 = "0.0f";

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main1);
        initialize();
        c = this;
        this.db = new DatabaseHandler(this);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.preferences = defaultSharedPreferences;
        defaultSharedPreferences.getBoolean("isAdsDisabled", false);
        DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);
        this.screenWidth = (float) dimension.widthPixels;
        this.screenHeight = (float) (dimension.heightPixels - Constants.dpToPx(this, 98));
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String string = extra.getString("positn");
            this.pos = string;
            if (string.equals("main")) {
                this.re_addnewText.setVisibility(0);
                temp_Referesh();
            }
        }
        this.intent_quote = getIntent().getStringExtra("quote_edit");
        this.modeOfChosingPic = getIntent().getStringExtra("modeOfChosingPic");
        String profile = getIntent().getStringExtra("profile");
        String mDrawableName = getIntent().getStringExtra("background");
        if (this.modeOfChosingPic.equals("chooserActivity")) {
            this.drawableName = mDrawableName;
            if (profile.equals("color")) {
                this.backgroundColor = Color.parseColor(mDrawableName);
                Bitmap createBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
                this.bitmap = createBitmap;
                createBitmap.eraseColor(this.backgroundColor);
            } else {
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(mDrawableName, "drawable", getPackageName()));
                int width = bitmap1.getWidth();
                int height = bitmap1.getHeight();
                if (width < height) {
                    height = width;
                } else {
                    width = height;
                }
                this.bitmap = Constants.cropCenterBitmap(bitmap1, width, height);
            }
            Bitmap bitmap12 = this.bitmap;
            float f2 = this.screenWidth;
            Bitmap resizeBitmap = ImageUtils.resizeBitmap(bitmap12, (int) f2, (int) f2);
            this.bitmap = resizeBitmap;
            setImageInBackgrounds(resizeBitmap);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_error), Toast.LENGTH_SHORT).show();
            finish();
        }
        applyGusianBlur();
        //addNormalTextTemplateMethod(this.intent_quote);
        this.shadow_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int shadow_color;
            float shadow_dx;
            float shadow_dy;
            float shadow_radius;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float shadow = (float) (progress / 2);
                if (fromUser) {
                    AddTextQuotesActivity.this.selectFocus = true;
                    AddTextQuotesActivity.this.fstCallMethod();
                    if (this.shadow_color == 0) {
                        this.shadow_color = ViewCompat.MEASURED_STATE_MASK;
                    }
                    if (AddTextQuotesActivity.this.modeshadowselection.equals("textshadow")) {
                        AddTextQuotesActivity.this.textView.setShadowLayer(shadow, shadow, shadow, this.shadow_color);
                    } else {
                        AddTextQuotesActivity.this.setShadowonSelected(shadow, shadow, shadow, this.shadow_color);
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                    AddTextQuotesActivity.this.text_tv.getSelectionStart();
                    AddTextQuotesActivity.this.text_tv.getSelectionEnd();
                    if (!AddTextQuotesActivity.this.modeshadowselection.equals("punchshadow")) {
                        this.shadow_color = AddTextQuotesActivity.this.text_tv.getShadowColor();
                        this.shadow_dx = AddTextQuotesActivity.this.text_tv.getShadowDx();
                        this.shadow_dy = AddTextQuotesActivity.this.text_tv.getShadowDy();
                        this.shadow_radius = AddTextQuotesActivity.this.text_tv.getShadowRadius();
                    } else if (AddTextQuotesActivity.this.arrayfortv.size() > 0) {
                        SelectedTextData d = AddTextQuotesActivity.this.arrayfortv.get(0);
                        this.shadow_color = d.getText_shadowcolor();
                        this.shadow_dx = d.getText_shadowdx();
                        this.shadow_dy = d.getText_shadowdy();
                        this.shadow_radius = d.getText_shadowradius();
                    }
                }
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.g1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.textView.setGravity(3);
                AddTextQuotesActivity.this.bold.setBackgroundResource(R.drawable.bold);
                AddTextQuotesActivity.this.italic.setBackgroundResource(R.drawable.italic);
                AddTextQuotesActivity.this.underline.setBackgroundResource(R.drawable.underline);
                AddTextQuotesActivity.this.strike.setBackgroundResource(R.drawable.text);
                AddTextQuotesActivity.this.g1.setBackgroundResource(R.drawable.left_align_text1);
                AddTextQuotesActivity.this.g2.setBackgroundResource(R.drawable.center_align_text);
                AddTextQuotesActivity.this.g3.setBackgroundResource(R.drawable.right_align_text);
            }
        });
        this.g2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.textView.setGravity(1);
                AddTextQuotesActivity.this.bold.setBackgroundResource(R.drawable.bold);
                AddTextQuotesActivity.this.italic.setBackgroundResource(R.drawable.italic);
                AddTextQuotesActivity.this.underline.setBackgroundResource(R.drawable.underline);
                AddTextQuotesActivity.this.strike.setBackgroundResource(R.drawable.text);
                AddTextQuotesActivity.this.g1.setBackgroundResource(R.drawable.left_align_text);
                AddTextQuotesActivity.this.g2.setBackgroundResource(R.drawable.center_align_text1);
                AddTextQuotesActivity.this.g3.setBackgroundResource(R.drawable.right_align_text);
            }
        });
        this.g3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.textView.setGravity(5);
                AddTextQuotesActivity.this.bold.setBackgroundResource(R.drawable.bold);
                AddTextQuotesActivity.this.italic.setBackgroundResource(R.drawable.italic);
                AddTextQuotesActivity.this.underline.setBackgroundResource(R.drawable.underline);
                AddTextQuotesActivity.this.strike.setBackgroundResource(R.drawable.text);
                AddTextQuotesActivity.this.g1.setBackgroundResource(R.drawable.left_align_text);
                AddTextQuotesActivity.this.g2.setBackgroundResource(R.drawable.center_align_text);
                AddTextQuotesActivity.this.g3.setBackgroundResource(R.drawable.right_align_text1);
            }
        });
        this.sd_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                if (AddTextQuotesActivity.this.modeshadowselection.equals("textshadow")) {
                    AddTextQuotesActivity addTextQuotesActivity = AddTextQuotesActivity.this;
                    addTextQuotesActivity.shadowcolor = addTextQuotesActivity.textView.getShadowColor();
                } else {
                    ArrayList<SelectedTextData> alstd = null;
                    if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                        alstd = AddTextQuotesActivity.this.arrayfortv;
                    }
                    if (alstd.size() > 0) {
                        AddTextQuotesActivity.this.shadowcolor = alstd.get(0).getText_shadowcolor();
                    }
                }
                AddTextQuotesActivity addTextQuotesActivity2 = AddTextQuotesActivity.this;
                new AmbilWarnaDialog(addTextQuotesActivity2, addTextQuotesActivity2.shadowcolor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        AddTextQuotesActivity.this.fstCallMethod();
                        if (AddTextQuotesActivity.this.modeshadowselection.equals("textshadow")) {
                            AddTextQuotesActivity.this.textView.setShadowLayer(AddTextQuotesActivity.this.textView.getShadowRadius(), AddTextQuotesActivity.this.textView.getShadowDx(), AddTextQuotesActivity.this.textView.getShadowDy(), color);
                            AddTextQuotesActivity.this.shadowcolor = color;
                            return;
                        }
                        ArrayList<SelectedTextData> alstd = null;
                        if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                            alstd = AddTextQuotesActivity.this.arrayfortv;
                        }
                        if (alstd.size() > 0) {
                            AddTextQuotesActivity.this.setShadowonSelected(alstd.get(alstd.size() - 1).getText_shadowradius(), alstd.get(alstd.size() - 1).getText_shadowdx(), alstd.get(alstd.size() - 1).getText_shadowdy(), color);
                            AddTextQuotesActivity.this.shadowcolor = color;
                        }
                    }

                    public void onCancel(AmbilWarnaDialog dialog) {
                    }
                }).show();
            }
        });
        this.sh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.shr1 = "null";
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader((Shader) null);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("null");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh1), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs1");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs1");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh2), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs2");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs2");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh3), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs3");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs3");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh4), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs4");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs4");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh5), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs5");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs5");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh6), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs6");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs6");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh7), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs7");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs7");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh8), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs8");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs8");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh9), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs9");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs9");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.sh10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                Shader shader = new BitmapShader(BitmapFactory.decodeResource(AddTextQuotesActivity.this.getResources(), R.drawable.sh10), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeshaderselection.equals("textshader")) {
                    AddTextQuotesActivity.this.textView.setLayerType(1, (Paint) null);
                    AddTextQuotesActivity.this.textView.getPaint().setShader(shader);
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.shaderTag("shs10");
                } else {
                    AddTextQuotesActivity.this.setShaderonSelected("shs10");
                }
                AddTextQuotesActivity.this.updateColors();
            }
        });
        this.bold.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeformatselection.equals("textformat")) {
                    if (AddTextQuotesActivity.this.textView.getTypeface().getStyle() == 1) {
                        AddTextQuotesActivity.this.textView.setTypeface(Typeface.create(AddTextQuotesActivity.this.textView.getTypeface(), 1), 0);
                    } else if (AddTextQuotesActivity.this.textView.getTypeface().getStyle() == 3) {
                        AddTextQuotesActivity.this.textView.setTypeface(Typeface.create(AddTextQuotesActivity.this.textView.getTypeface(), 0), 0);
                        AddTextQuotesActivity.this.textView.setTypeface(AddTextQuotesActivity.this.textView.getTypeface(), AddTextQuotesActivity.this.textView.getTypeface().getStyle() | 2);
                    } else {
                        AddTextQuotesActivity.this.textView.setTypeface(AddTextQuotesActivity.this.textView.getTypeface(), 1 | AddTextQuotesActivity.this.textView.getTypeface().getStyle());
                    }
                    AddTextQuotesActivity.this.textView.invalidate();
                    AddTextQuotesActivity.this.textView.requestLayout();
                    AddTextQuotesActivity.this.defaultsetup();
                    AddTextQuotesActivity.this.checkboldItalic();
                    AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
                    return;
                }
                if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                    for (int i = 0; i < AddTextQuotesActivity.this.arrayfortv.size(); i++) {
                        SelectedTextData d = AddTextQuotesActivity.this.arrayfortv.get(i);
                        if (d.isText_bold()) {
                            d.setText_bold(false);
                        } else {
                            d.setText_bold(true);
                        }
                        AddTextQuotesActivity.this.arrayfortv.set(i, d);
                    }
                }
                AddTextQuotesActivity.this.defaultsetup();
                AddTextQuotesActivity.this.checkboldItalic();
                AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
            }
        });
        this.italic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeformatselection.equals("textformat")) {
                    if (AddTextQuotesActivity.this.textView.getTypeface().getStyle() == 2) {
                        AddTextQuotesActivity.this.textView.setTypeface(Typeface.create(AddTextQuotesActivity.this.textView.getTypeface(), 0), 0);
                    } else if (AddTextQuotesActivity.this.textView.getTypeface().getStyle() == 3) {
                        AddTextQuotesActivity.this.textView.setTypeface(Typeface.create(AddTextQuotesActivity.this.textView.getTypeface(), 1), 0);
                        AddTextQuotesActivity.this.textView.setTypeface(AddTextQuotesActivity.this.textView.getTypeface(), 1 | AddTextQuotesActivity.this.textView.getTypeface().getStyle());
                    } else {
                        AddTextQuotesActivity.this.textView.setTypeface(AddTextQuotesActivity.this.textView.getTypeface(), AddTextQuotesActivity.this.textView.getTypeface().getStyle() | 2);
                    }
                    AddTextQuotesActivity.this.defaultsetup();
                    AddTextQuotesActivity.this.checkboldItalic();
                    AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
                    return;
                }
                if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                    for (int i = 0; i < AddTextQuotesActivity.this.arrayfortv.size(); i++) {
                        SelectedTextData d = AddTextQuotesActivity.this.arrayfortv.get(i);
                        if (d.isText_italic()) {
                            d.setText_italic(false);
                        } else {
                            d.setText_italic(true);
                        }
                        AddTextQuotesActivity.this.arrayfortv.set(i, d);
                    }
                }
                AddTextQuotesActivity.this.defaultsetup();
                AddTextQuotesActivity.this.checkboldItalic();
                AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
            }
        });
        this.underline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeformatselection.equals("textformat")) {
                    if ((AddTextQuotesActivity.this.textView.getPaintFlags() & 8) == 8) {
                        AddTextQuotesActivity.this.textView.setPaintFlags(AddTextQuotesActivity.this.textView.getPaintFlags() & -9);
                    } else {
                        AddTextQuotesActivity.this.textView.setPaintFlags(8 | AddTextQuotesActivity.this.textView.getPaintFlags());
                    }
                    AddTextQuotesActivity.this.defaultsetup();
                    AddTextQuotesActivity.this.checkboldItalic();
                    AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
                    return;
                }
                if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                    for (int i = 0; i < AddTextQuotesActivity.this.arrayfortv.size(); i++) {
                        SelectedTextData d = AddTextQuotesActivity.this.arrayfortv.get(i);
                        if (d.isText_underline()) {
                            d.setText_underline(false);
                        } else {
                            d.setText_underline(true);
                        }
                        AddTextQuotesActivity.this.arrayfortv.set(i, d);
                    }
                }
                AddTextQuotesActivity.this.defaultsetup();
                AddTextQuotesActivity.this.checkboldItalic();
                AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
            }
        });
        this.strike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                AddTextQuotesActivity.this.fstCallMethod();
                if (AddTextQuotesActivity.this.modeformatselection.equals("textformat")) {
                    if ((AddTextQuotesActivity.this.textView.getPaintFlags() & 16) == 16) {
                        AddTextQuotesActivity.this.textView.setPaintFlags(AddTextQuotesActivity.this.textView.getPaintFlags() & -17);
                    } else {
                        AddTextQuotesActivity.this.textView.setPaintFlags(16 | AddTextQuotesActivity.this.textView.getPaintFlags());
                    }
                    AddTextQuotesActivity.this.defaultsetup();
                    AddTextQuotesActivity.this.checkboldItalic();
                    AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
                    return;
                }
                if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                    for (int i = 0; i < AddTextQuotesActivity.this.arrayfortv.size(); i++) {
                        SelectedTextData d = AddTextQuotesActivity.this.arrayfortv.get(i);
                        if (d.isText_strike()) {
                            d.setText_strike(false);
                        } else {
                            d.setText_strike(true);
                        }
                        AddTextQuotesActivity.this.arrayfortv.set(i, d);
                    }
                }
                AddTextQuotesActivity.this.defaultsetup();
                AddTextQuotesActivity.this.checkboldItalic();
                AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.min, AddTextQuotesActivity.this.max);
            }
        });
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                Log.e("GestureDetector", "onSingleTapUp");
                if (!AddTextQuotesActivity.this.flag) {
                    AddTextQuotesActivity.this.textView.setCursorVisible(false);
                }
                if (!AddTextQuotesActivity.this.flag || !AddTextQuotesActivity.this.flag2) {
                    if (AddTextQuotesActivity.this.selectFocus) {
                        Selection.removeSelection(AddTextQuotesActivity.this.textView.getText());
                        AddTextQuotesActivity.this.textView.setTextIsSelectable(true);
                        AddTextQuotesActivity.this.textView.setSelectAllOnFocus(false);
                        AddTextQuotesActivity.this.textView.setCursorVisible(false);
                        if (AddTextQuotesActivity.this.ftrue) {
                            AddTextQuotesActivity.this.ftrue = false;
                        }
                        AddTextQuotesActivity.this.checkandShowEditRel();
                    } else {
                        AddTextQuotesActivity.this.textView.setSelectAllOnFocus(false);
                        AddTextQuotesActivity.this.textView.setCursorVisible(false);
                    }
                }
                return false;
            }

            public void onShowPress(MotionEvent e) {
                Log.e("GestureDetector", "onShowPress");
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e("GestureDetector", "onScroll");
                return false;
            }

            public void onLongPress(MotionEvent e) {
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.e("GestureDetector", "onFling");
                return false;
            }

            public boolean onDown(MotionEvent e) {
                Log.e("GestureDetector", "onDown");
                return false;
            }
        });
        this.gd = gestureDetector;
        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            public boolean onDoubleTap(MotionEvent e) {
                Log.e("GestureDetector", "setOnDoubleTapListener");
                AddTextQuotesActivity.this.textView.clearFocus();
                return false;
            }

            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.e("GestureDetector", "onDoubleTapEvent");
                AddTextQuotesActivity.this.textView.clearFocus();
                return false;
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e("GestureDetector", "onSingleTapConfirmed");
                if (!AddTextQuotesActivity.this.flag) {
                    AddTextQuotesActivity.this.textView.setCursorVisible(false);
                }
                if ((!AddTextQuotesActivity.this.flag || !AddTextQuotesActivity.this.flag2) && AddTextQuotesActivity.this.selectFocus) {
                    AddTextQuotesActivity.this.seekflag = false;
                    AddTextQuotesActivity.this.start = 0;
                    AddTextQuotesActivity.this.end = 0;
                    int selectionStart = AddTextQuotesActivity.this.textView.getSelectionStart();
                    int start_txt = Constants.getStart(AddTextQuotesActivity.this.textView.getText().toString(), AddTextQuotesActivity.this.textView.getSelectionStart());
                    int end_txt = Constants.getEnd(AddTextQuotesActivity.this.textView.getText().toString(), AddTextQuotesActivity.this.textView.getSelectionEnd());
                    Log.e("end_txt", "" + start_txt + ", " + end_txt);
                    if (start_txt != end_txt) {
                        AddTextQuotesActivity.this.textView.setSelection(start_txt, end_txt);
                        if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                            float floatValue = Float.valueOf(AddTextQuotesActivity.this.text_tv.getTextSize()).floatValue();
                            AddTextQuotesActivity addTextQuotesActivity = AddTextQuotesActivity.this;
                            addTextQuotesActivity.start = addTextQuotesActivity.text_tv.getSelectionStart();
                            if (AddTextQuotesActivity.this.start < 0) {
                                AddTextQuotesActivity.this.start = 0;
                            }
                            AddTextQuotesActivity addTextQuotesActivity2 = AddTextQuotesActivity.this;
                            addTextQuotesActivity2.end = addTextQuotesActivity2.text_tv.getSelectionEnd();
                            if (AddTextQuotesActivity.this.end < 0) {
                                AddTextQuotesActivity.this.end = 0;
                            }
                            if (!(AddTextQuotesActivity.this.start == 0 && AddTextQuotesActivity.this.end == AddTextQuotesActivity.this.text_tv.length())) {
                                AddTextQuotesActivity addTextQuotesActivity3 = AddTextQuotesActivity.this;
                                addTextQuotesActivity3.setPunch(addTextQuotesActivity3.arrayfortv, AddTextQuotesActivity.this.start, AddTextQuotesActivity.this.end);
                            }
                            for (int i = 0; i < AddTextQuotesActivity.this.arrayfortv.size(); i++) {
                                Log.e("while", "yess" + AddTextQuotesActivity.this.arrayfortv.size());
                                if (AddTextQuotesActivity.this.arrayfortv.get(i).getStart() == AddTextQuotesActivity.this.start && AddTextQuotesActivity.this.arrayfortv.get(i).getEnd() == AddTextQuotesActivity.this.end) {
                                    AddTextQuotesActivity.this.arrayfortv.get(i).getText_size();
                                }
                            }
                        }
                        AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.start, AddTextQuotesActivity.this.end);
                    } else {
                        AddTextQuotesActivity.this.textView.setCursorVisible(false);
                        if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv")) {
                            AddTextQuotesActivity addTextQuotesActivity4 = AddTextQuotesActivity.this;
                            addTextQuotesActivity4.setSelectionAll(addTextQuotesActivity4.text_tv);
                        }
                    }
                }
                AddTextQuotesActivity.this.textView.clearFocus();
                Selection.removeSelection(AddTextQuotesActivity.this.textView.getText());
                AddTextQuotesActivity.this.a = 0;
                AddTextQuotesActivity.this.b = 0;
                AddTextQuotesActivity.this.start = 0;
                AddTextQuotesActivity.this.end = 0;
                AddTextQuotesActivity.this.min = 0;
                AddTextQuotesActivity.this.max = 0;
                AddTextQuotesActivity addTextQuotesActivity5 = AddTextQuotesActivity.this;
                addTextQuotesActivity5.updateTextSizeonScale(addTextQuotesActivity5.rl.getWidth(), AddTextQuotesActivity.this.rl.getHeight());
                Log.e("xxxxxx", "yess");
                return false;
            }
        });
        Log.e("vvvvvv", "yess");
        Button button = this.underline;
        button.setPaintFlags(button.getPaintFlags() | 8);
        Button button2 = this.strike;
        button2.setPaintFlags(button2.getPaintFlags() | 16);



        this.text_tv.post(new Runnable() {
            public void run() {
                int noch1 = ((ViewGroup) AddTextQuotesActivity.this.text_tv.getParent()).getChildCount();
                for (int i = 1; i < noch1; i++) {
                    Log.e("for", "yess");
                    View child = ((ViewGroup) AddTextQuotesActivity.this.text_tv.getParent()).getChildAt(i);
                    if (i != 3) {
                        child.setVisibility(0);
                    }
                }
                AddTextQuotesActivity.this.checkandShowEditRel();
            }
        });
    }


    /* access modifiers changed from: private */
    public void updatePositionSticker(String incr) {
        int childCount = this.fortext.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.fortext.getChildAt(i);
            if ((view instanceof ResizableImageview) && ((ResizableImageview) view).getBorderVisbilty()) {
                if (incr.equals("incrX")) {
                    ((ResizableImageview) view).incrX();
                }
                if (incr.equals("decX")) {
                    ((ResizableImageview) view).decX();
                }
                if (incr.equals("incrY")) {
                    ((ResizableImageview) view).incrY();
                }
                if (incr.equals("decY")) {
                    ((ResizableImageview) view).decY();
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void applyGusianBlur() {
        GPUImageFilter createFilterForType = GPUImageFilterTools.createFilterForType(getApplicationContext(), GPUImageFilterTools.FilterType.GAUSSIAN_BLUR);
        this.mFilter = createFilterForType;
        this.gpuImageview.setFilter(createFilterForType);
        this.gpuImageview.setVisibility(0);
        this.mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(this.mFilter);
       // this.sb_effectsfilter.setProgress(25);
        GPUImageFilterTools.FilterAdjuster filterAdjuster = this.mFilterAdjuster;
        if (filterAdjuster != null) {
            filterAdjuster.adjust(25);
        }
        this.gpuImageview.requestRender();
    }

    /* access modifiers changed from: private */
    public void setPunch(ArrayList<SelectedTextData> arr, int start2, int end2) {
        boolean bl = true;
        if (arr.size() > 0) {
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getStart() == start2 || arr.get(i).getEnd() == end2) {
                    arr.remove(i);
                    defaultsetup();
                    bl = false;
                }
            }
            if (bl && start2 != end2) {
                setSelect_ChangeformArray(arr, start2, end2);
            }
        } else if (this.pos.equals("main")) {
            int _postn = Integer.parseInt(this.template.replace("t", ""));
            if (start2 != end2) {
                setSelect_Chang(arr, start2, end2, _postn);
            }
        } else if (start2 != end2) {
            mainAddMethod1();
        }
    }

    private void temp_Referesh() {
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", 0).edit();
        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", 0);
        if (prefs.getInt("temp", 0) > 26) {
            this.temp_postn = 1;
            editor.putInt("temp", 1 + 1);
            editor.commit();
        } else if (prefs.getInt("temp", 0) == 0) {
            this.temp_postn = 1;
            editor.putInt("temp", 1 + 1);
            editor.commit();
        } else {
            int i = prefs.getInt("temp", 0);
            this.temp_postn = i;
            editor.putInt("temp", i + 1);
            editor.commit();
        }
    }

    /* access modifiers changed from: protected */
    public void fstCallMethod() {
        this.min = 0;
        this.max = this.textView.getText().length();
        this.textView.setCursorVisible(false);
        if (this.textView.isFocused()) {
            int selStart = this.textView.getSelectionStart();
            int selEnd = this.textView.getSelectionEnd();
            this.min = Math.max(0, Math.min(selStart, selEnd));
            this.max = Math.max(0, Math.max(selStart, selEnd));
        }
        if (this.min != 0 || this.max != this.textView.length()) {
            mainAddMethod();
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void callFous() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.textView.getWindowToken(), 0);
    }

    @SuppressLint("WrongConstant")
    private void visibleBorder(RelativeLayout rl2) {
        int noch = rl2.getChildCount();
        for (int i = 1; i < noch; i++) {
            if (i != 3) {
                rl2.getChildAt(i).setVisibility(0);
            }
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void addNormalTextTemplateMethod(String quotTxt) {
        String text;
        float sh_rdus;
        float sh_y;
        float sh_x;
        int i;
        int text_color;
        Shader sadr = null;
        int text_color2 = this.defaultcolor_all;


        this.ed = false;
        this.flag = false;
        this.flag2 = false;
       // EditText editText = this.textView;
        EditText editText = new EditText(AddTextQuotesActivity.this);
      //  if (editText != null) {
            editText.setCursorVisible(false);
            this.textView.setSelectAllOnFocus(false);
       // }
        Log.e("temp_postn is", "" + this.temp_postn);
        List<Quotes> quotesw = this.db.getQuotesAllRowValue(this.temp_postn);
        if (quotesw.size() != 0) {
            int i2 = 0;
            while (i2 < quotesw.size()) {
                Log.e("sizeoflist", "" + i2);
                Log.e("sizeoflist", "" + quotesw.size());
                this.template = quotesw.get(i2).get_template();
                if (quotTxt.equals("")) {
                    text = "";
                } else {
                    text = quotTxt;
                }
                int grvty = quotesw.get(i2).get_gravity();
                int text_color3 = Integer.parseInt(quotesw.get(i2).get_color());
                String fontt = quotesw.get(i2).get_font();
                Typeface font_type = Typeface.createFromAsset(getAssets(), fontt);
                List<QuotesSelect> dbsetPunch = this.db.getSelectAllRowValue(this.temp_postn);
                Typeface font_typePunch = Typeface.createFromAsset(getAssets(), dbsetPunch.get(i2).get_font());
                String shadow_dx = quotesw.get(i2).get_shadow_dx();
                String shadow_dy = quotesw.get(i2).get_shadow_dy();
                String shadow_radius = quotesw.get(i2).get_shadow_radius();
                String shadow_color = quotesw.get(i2).get_shadow_color();
                String shaderr = quotesw.get(i2).get_shader();
                float sh_x2 = Float.parseFloat(shadow_dx);
                int parseFloat = (int) Float.parseFloat(quotesw.get(i2).get_size());
                float sh_y2 = Float.parseFloat(shadow_dy);
                List<QuotesSelect> list = dbsetPunch;
                float sh_rdus2 = Float.parseFloat(shadow_radius);
                Typeface typeface = font_typePunch;
                int sh_color = Integer.parseInt(shadow_color);
                String str = shadow_dx;
                int grvty2 = grvty;
                String[] parts = quotesw.get(i2).get_scale().split("-");
                int width = (int) Float.parseFloat(parts[0]);
                Shader sadr2 = sadr;
                int height = (int) Float.parseFloat(parts[1]);
                int pos_x = quotesw.get(i2).getPos_x();
                int pos_y = quotesw.get(i2).getPos_y();
                List<Quotes> quotesw2 = quotesw;
                int rotation = quotesw.get(i2).getRotation();
                String[] strArr = parts;
                if (!shaderr.equals("null")) {
                    i = i2;
                    sh_x = sh_x2;
                    sh_y = sh_y2;
                    sh_rdus = sh_rdus2;
                    new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(shaderr, "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                } else {
                    i = i2;
                    sh_x = sh_x2;
                    sh_y = sh_y2;
                    sh_rdus = sh_rdus2;
                }
                float mainLayWidth = (float) (bitmapOriginal.getWidth() / 2);
                float mainLayHeight = (float) (bitmapOriginal.getHeight() / 2);
                if (this.rl.getVisibility() == 0 || text.equals("")) {
                    float f2 = sh_x;
                    float f3 = sh_rdus;
                    text_color = text_color3;
                    float sh_rdus3 = sh_y;
                    String str2 = shaderr;
                } else {
                    this.ft1 = fontt;
                    this.rl.getLayoutParams().width = width;
                    this.rl.getLayoutParams().height = height;
                    this.rl.setX(mainLayWidth - ((float) (width / 2)));
                    this.rl.setY(mainLayHeight - ((float) (height / 2)));
                    this.rl.setRotation((float) rotation);
                    this.rl.postInvalidate();
                    this.rl.requestLayout();
                    this.text_tv.setText(text);
                    this.text_tv.setTypeface(font_type);
                    this.text_tv.setTextColor(text_color3);
                    float sh_x3 = sh_x;
                    text_color = text_color3;
                    float sh_y3 = sh_y;
                    String str3 = shaderr;
                    float sh_rdus4 = sh_rdus;
                    this.text_tv.setShadowLayer(sh_x3, sh_y3, sh_rdus4, sh_color);
                    float f4 = sh_y3;
                    this.text_tv.getPaint().setShader(sadr2);
                    int grvty3 = grvty2;
                    if (grvty3 == 49) {
                        float f5 = sh_rdus4;
                        this.text_tv.setGravity(1);
                    } else {
                        if (grvty3 == 51) {
                            this.text_tv.setGravity(3);
                        } else if (grvty3 == 53) {
                            this.text_tv.setGravity(5);
                        }
                    }
                    hideChilds(this.rl);
                    EditText editText2 = this.text_tv;
                    this.textView = editText2;
                    setObserver(editText2);
                    this.rl.setVisibility(0);
                    this.rl.bringToFront();
                    this.storedArray.clear();
                    this.arrayfortv.clear();
                    if (this.chang == 0) {
                        this.chang = 4;
                    }
                    int i3 = grvty3;
                    setChangPunch(this.arrayfortv, text, 4, 1);
                    updateTextSizeonScale(width, height);
                    this.rl.postInvalidate();
                    this.rl.requestLayout();
                }
                this.selectFocus = true;
                this.textView.clearFocus();
                this.textView.setSelectAllOnFocus(false);
                this.textView.setCursorVisible(false);
                this.oldlength = this.textView.length();
                i2 = i + 1;
                sadr = sadr2;
                quotesw = quotesw2;
                text_color2 = text_color;
            }
            int i4 = text_color2;
            List<Quotes> list2 = quotesw;
            int i5 = i2;
            return;
        }
        int i6 = text_color2;
        List<Quotes> list3 = quotesw;
    }

    private void setChangPunch(ArrayList<SelectedTextData> arr, String quotTxt, int chang2, int selectedId) {
        int i;
        int i2;
        int i3;
        int i4;
        ArrayList<SelectedTextData> arrayList = arr;
        String str = quotTxt;
        int i5 = chang2;
        int i6 = selectedId;
        List<QuotesSelect> quotesw = this.db.getSelectAllRowValue(this.temp_postn);
        for (int j = 0; j < quotesw.size(); j++) {
            if (quotesw.get(j).get_text_id() == i6) {
                String[] split = str.split(" ");
                ArrayList arrayList2 = new ArrayList();
                int space_pos = str.indexOf(" ");
                arrayList2.add(Integer.valueOf(space_pos));
                while (true) {
                    if (space_pos >= quotTxt.length()) {
                        int i7 = space_pos;
                        break;
                    }
                    space_pos = str.indexOf(" ", space_pos + 1);
                    if (space_pos == -1) {
                        int i8 = space_pos;
                        break;
                    }
                    arrayList2.add(Integer.valueOf(space_pos));
                }
                int i9 = 1;
                if (i5 == 1) {
                    int i10 = 0;
                    while (i10 < split.length) {
                        if (i10 == 0) {
                            punchStartCh(arrayList, str, split[i10], i6);
                            i4 = i10;
                        } else if (i10 % 2 == 0) {
                            i4 = i10;
                            punchMiddle(arr, quotTxt, split[i10], ((Integer) arrayList2.get(i10 - 1)).intValue(), selectedId);
                        } else {
                            i4 = i10;
                        }
                        i10 = i4 + 1;
                    }
                    int i11 = i10;
                } else if (i5 == 2) {
                    int i12 = 0;
                    while (i12 < split.length) {
                        if (i12 % 2 == i9) {
                            i3 = i12;
                            punchMiddle(arr, quotTxt, split[i12], ((Integer) arrayList2.get(i12 - 1)).intValue(), selectedId);
                        } else {
                            i3 = i12;
                        }
                        i12 = i3 + 1;
                        i9 = 1;
                    }
                    int i13 = i12;
                } else if (i5 == 3) {
                    int i14 = 0;
                    while (i14 < split.length) {
                        if (i14 == 0) {
                            punchStartCh(arrayList, str, split[i14], i6);
                            i2 = i14;
                        } else if (i14 % 3 == 0) {
                            i2 = i14;
                            punchMiddle(arr, quotTxt, split[i14], ((Integer) arrayList2.get(i14 - 1)).intValue(), selectedId);
                        } else {
                            i2 = i14;
                        }
                        i14 = i2 + 1;
                    }
                    int i15 = i14;
                } else if (i5 == 4) {
                    int i16 = 0;
                    while (i16 < split.length) {
                        if (i16 == 0) {
                            punchStartCh(arrayList, str, split[i16], i6);
                            i = i16;
                        } else if (i16 % 5 == 0) {
                            i = i16;
                            punchMiddle(arr, quotTxt, split[i16], ((Integer) arrayList2.get(i16 - 1)).intValue(), selectedId);
                        } else {
                            i = i16;
                        }
                        i16 = i + 1;
                    }
                    int i17 = i16;
                }
            }
        }
    }

    private void punchMiddle(ArrayList<SelectedTextData> arr, String quotTxt, String string, int spacepos, int textId) {
        Matcher matcher = Pattern.compile("\\s\\S*" + string.replaceAll("\\)", "").replaceAll("\\(", "")).matcher(quotTxt);
        if (matcher.find(spacepos)) {
            setAddQuoteSelect(arr, matcher.start() + 1, matcher.end(), textId);
        }
    }

    private void punchStartCh(ArrayList<SelectedTextData> arr, String quotTxt, String string, int textId) {
        Matcher matcher = Pattern.compile(string.replaceAll("\\)", "").replaceAll("\\(", "")).matcher(quotTxt);
        if (matcher.find()) {
            setAddQuoteSelect(arr, matcher.start(), matcher.end(), textId);
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void hideChilds(RelativeLayout rl2) {
        int noch = rl2.getChildCount();
        for (int i = 1; i < noch; i++) {
            rl2.getChildAt(i).setVisibility(4);
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void UpdateAddTextMethod() {
        int i = this.defaultcolor_all;
        this.shr1 = "null";


        List<Quotes> quotesw = this.db.getQuotesAllRowValue(this.temp_postn);
        this.template = quotesw.get(0).get_template();
        int text_color = Integer.parseInt(quotesw.get(0).get_color());
        String fontt = quotesw.get(0).get_font();
        int grvty = quotesw.get(0).get_gravity();
        Typeface font_type = Typeface.createFromAsset(getAssets(), fontt);
        List<QuotesSelect> dbsetPunch = this.db.getSelectAllRowValue(this.temp_postn);
        Typeface font_typePunch = Typeface.createFromAsset(getAssets(), dbsetPunch.get(0).get_font());
        String shadow_dx = quotesw.get(0).get_shadow_dx();
        String shadow_dy = quotesw.get(0).get_shadow_dy();
        String shadow_radius = quotesw.get(0).get_shadow_radius();
        String shadow_color = quotesw.get(0).get_shadow_color();
        String shaderr = quotesw.get(0).get_shader();
        Object obj = "";
        float sh_x = Float.parseFloat(shadow_dx);
        int parseFloat = (int) Float.parseFloat(quotesw.get(0).get_size());
        float sh_y = Float.parseFloat(shadow_dy);
        List<QuotesSelect> list = dbsetPunch;
        float sh_rdus = Float.parseFloat(shadow_radius);
        Typeface typeface = font_typePunch;
        int sh_color = Integer.parseInt(shadow_color);
        String str = shadow_dx;
        List<Quotes> list2 = quotesw;
        String[] parts = quotesw.get(0).get_scale().split("-");
        int parseFloat2 = (int) Float.parseFloat(parts[0]);
        int height = (int) Float.parseFloat(parts[1]);
        if (!shaderr.equals("null")) {
            String[] strArr = parts;
            int i2 = height;
            String str2 = shadow_dy;
            String str3 = shadow_radius;
            new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(shaderr, "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        } else {
            int i3 = height;
            String str4 = shadow_dy;
            String str5 = shadow_radius;
        }
        if (this.textView.getTag().equals("text_tv")) {
            this.ft1 = fontt;
            String text2 = String.valueOf(this.text_tv.getText());
            RelativeLayout relativeLayout = this.rl;
            relativeLayout.setRotation(relativeLayout.getRotation());
            this.rl.postInvalidate();
            this.rl.requestLayout();
            this.text_tv.setText(text2);
            this.text_tv.setTypeface(font_type);
            this.text_tv.setTextColor(text_color);
            this.text_tv.setShadowLayer(sh_x, sh_y, sh_rdus, sh_color);
            this.text_tv.getPaint().setShader((Shader) null);
            if (grvty == 49) {
                this.text_tv.setGravity(1);
            } else if (grvty == 51) {
                this.text_tv.setGravity(3);
            } else if (grvty == 53) {
                this.text_tv.setGravity(5);
            }
            EditText editText = this.text_tv;
            this.textView = editText;
            setObserver(editText);
            setSelect1(this.arrayfortv, String.valueOf(this.text_tv.getText()));
            return;
        }
        Toast.makeText(this, "no text", 0).show();
    }

    private void setSelect(ArrayList<SelectedTextData> arr) {
        List<QuotesSelect> quotesw = this.db.getSelectAllRowValue(this.temp_postn);
        for (int i = 0; i < quotesw.size(); i++) {
            SelectedTextData std = new SelectedTextData();
            std.setStart(Integer.parseInt(quotesw.get(i).get_start()));
            std.setEnd(Integer.parseInt(quotesw.get(i).get_end()));
            if (this.textView.getTag().equals("text_tv")) {
                std.setText_size((int) PictureConstant.convertPixelsToDp((float) (((int) Float.valueOf(this.text_tv.getTextSize()).floatValue()) + 5), this));
            }
            std.setText_color(Integer.parseInt(quotesw.get(i).get_color()));
            std.setText_ttf(quotesw.get(i).get_font());
            std.setText_shadowdx(Float.parseFloat(quotesw.get(i).get_shadow_dx()));
            std.setText_shadowdy(Float.parseFloat(quotesw.get(i).get_shadow_dy()));
            std.setText_shadowradius(Float.parseFloat(quotesw.get(i).get_shadow_radius()));
            std.setText_shadowcolor(Integer.parseInt(quotesw.get(i).get_shadow_color()));
            std.setText_shader(quotesw.get(i).get_shader());
            std.setText_bold(Boolean.valueOf(quotesw.get(i).get_textbold()).booleanValue());
            std.setText_italic(Boolean.valueOf(quotesw.get(i).get_text_italic()).booleanValue());
            std.setText_underline(Boolean.valueOf(quotesw.get(i).get_text_underline()).booleanValue());
            std.setText_strike(Boolean.valueOf(quotesw.get(i).get_text_strik()).booleanValue());
            arr.add(std);
        }
        defaultsetup();
    }

    private void setAddQuoteSelect(ArrayList<SelectedTextData> arr, int start2, int end2, int selectedTextId) {
        int pos2 = selectedTextId - 1;
        List<QuotesSelect> quotesw = this.db.getSelectAllRowValue(this.temp_postn);
        if (quotesw.size() > 0) {
            SelectedTextData std = new SelectedTextData();
            std.setStart(start2);
            std.setEnd(end2);
            if (this.textView.getTag().equals("text_tv")) {
                std.setText_size((int) PictureConstant.convertPixelsToDp((float) (((int) Float.valueOf(this.text_tv.getTextSize()).floatValue()) + 5), this));
            }
            if (this.storedArray.size() > 0) {
                std.setText_color(this.storedArray.get(0).getText_color());
                std.setText_ttf(this.storedArray.get(0).getText_ttf());
                std.setText_shadowdx(this.storedArray.get(0).getText_shadowdx());
                std.setText_shadowdy(this.storedArray.get(0).getText_shadowdy());
                std.setText_shadowradius(this.storedArray.get(0).getText_shadowradius());
                std.setText_shadowcolor(this.storedArray.get(0).getText_shadowcolor());
                std.setText_shader(this.storedArray.get(0).getText_shader());
                std.setText_bold(this.storedArray.get(0).isText_bold());
                std.setText_italic(this.storedArray.get(0).isText_italic());
                std.setText_underline(this.storedArray.get(0).isText_underline());
                std.setText_strike(this.storedArray.get(0).isText_strike());
                arr.add(std);
            } else {
                std.setText_color(Integer.parseInt(quotesw.get(pos2).get_color()));
                std.setText_ttf(quotesw.get(pos2).get_font());
                std.setText_shadowdx(Float.parseFloat(quotesw.get(pos2).get_shadow_dx()));
                std.setText_shadowdy(Float.parseFloat(quotesw.get(pos2).get_shadow_dy()));
                std.setText_shadowradius(Float.parseFloat(quotesw.get(pos2).get_shadow_radius()));
                std.setText_shadowcolor(Integer.parseInt(quotesw.get(pos2).get_shadow_color()));
                std.setText_shader(quotesw.get(pos2).get_shader());
                std.setText_bold(Boolean.valueOf(quotesw.get(pos2).get_textbold()).booleanValue());
                std.setText_italic(Boolean.valueOf(quotesw.get(pos2).get_text_italic()).booleanValue());
                std.setText_underline(Boolean.valueOf(quotesw.get(pos2).get_text_underline()).booleanValue());
                std.setText_strike(Boolean.valueOf(quotesw.get(pos2).get_text_strik()).booleanValue());
                arr.add(std);
            }
        }
        Log.e("timeend1", "" + System.currentTimeMillis());
    }

    private void setSelect1(ArrayList<SelectedTextData> arr, String text) {
        if (text.equals(this.defaultText)) {
            arr.clear();
            setSelect(arr);
            return;
        }
        List<QuotesSelect> quotesw = this.db.getSelectAllRowValue(this.temp_postn);
        if (quotesw.size() > 0 && arr.size() > 0) {
            for (int j = 0; j < arr.size(); j++) {
                SelectedTextData std = new SelectedTextData();
                std.setStart(arr.get(j).getStart());
                std.setEnd(arr.get(j).getEnd());
                Resources resources = getResources();
                if (this.textView.getTag().equals("text_tv")) {
                    std.setText_size((int) PictureConstant.convertPixelsToDp((float) (((int) Float.valueOf(this.text_tv.getTextSize()).floatValue()) + 5), this));
                }
                std.setText_color(Integer.parseInt(quotesw.get(0).get_color()));
                std.setText_ttf(quotesw.get(0).get_font());
                Log.e("aaaaacc", "" + quotesw.get(0));
                try {
                    std.setText_shadowdx(Float.valueOf(Float.parseFloat(quotesw.get(0).get_shadow_dx())).floatValue());
                    std.setText_shadowdy(Float.parseFloat(quotesw.get(0).get_shadow_dy()));
                    std.setText_shadowradius(Float.parseFloat(quotesw.get(0).get_shadow_radius()));
                    std.setText_shadowcolor(Integer.parseInt(quotesw.get(0).get_shadow_color()));
                    std.setText_shader(quotesw.get(0).get_shader());
                    std.setText_bold(Boolean.valueOf(quotesw.get(0).get_textbold()).booleanValue());
                    std.setText_italic(Boolean.valueOf(quotesw.get(0).get_text_italic()).booleanValue());
                    std.setText_underline(Boolean.valueOf(quotesw.get(0).get_text_underline()).booleanValue());
                    std.setText_strike(Boolean.valueOf(quotesw.get(0).get_text_strik()).booleanValue());
                    arr.set(j, std);
                } catch (Exception e) {
                }
            }
        }
        updateTextSizeonScale(this.rl.getWidth(), this.rl.getHeight());
    }

    private void setSelect_Chang(ArrayList<SelectedTextData> arr, int start2, int end2, int postn) {
        List<QuotesSelect> quotesw = this.db.getSelectAllRowValue(postn);
        SelectedTextData std = new SelectedTextData();
        std.setStart(start2);
        std.setEnd(end2);
        if (this.textView.getTag().equals("text_tv")) {
            std.setText_size((int) PictureConstant.convertPixelsToDp((float) (((int) Float.valueOf(this.text_tv.getTextSize()).floatValue()) + 5), this));
        }
        std.setText_color(Integer.parseInt(quotesw.get(0).get_color()));
        std.setText_ttf(quotesw.get(0).get_font());
        std.setText_shadowdx(Float.parseFloat(quotesw.get(0).get_shadow_dx()));
        std.setText_shadowdy(Float.parseFloat(quotesw.get(0).get_shadow_dy()));
        std.setText_shadowradius(Float.parseFloat(quotesw.get(0).get_shadow_radius()));
        std.setText_shadowcolor(Integer.parseInt(quotesw.get(0).get_shadow_color()));
        std.setText_shader(quotesw.get(0).get_shader());
        std.setText_bold(Boolean.valueOf(quotesw.get(0).get_textbold()).booleanValue());
        std.setText_italic(Boolean.valueOf(quotesw.get(0).get_text_italic()).booleanValue());
        std.setText_underline(Boolean.valueOf(quotesw.get(0).get_text_underline()).booleanValue());
        std.setText_strike(Boolean.valueOf(quotesw.get(0).get_text_strik()).booleanValue());
        arr.add(std);
        defaultsetup();
    }

    private void setSelect_ChangeformArray(ArrayList<SelectedTextData> arr, int start2, int end2) {
        SelectedTextData std = new SelectedTextData();
        std.setStart(start2);
        std.setEnd(end2);
        std.setText_size(arr.get(arr.size() - 1).getText_size());
        std.setText_color(arr.get(arr.size() - 1).getText_color());
        std.setText_ttf(arr.get(arr.size() - 1).getText_ttf());
        std.setText_shadowdx(arr.get(arr.size() - 1).getText_shadowdx());
        std.setText_shadowdy(arr.get(arr.size() - 1).getText_shadowdy());
        std.setText_shadowradius(arr.get(arr.size() - 1).getText_shadowradius());
        std.setText_shadowcolor(arr.get(arr.size() - 1).getText_shadowcolor());
        std.setText_shader(arr.get(arr.size() - 1).getText_shader());
        std.setText_bold(arr.get(arr.size() - 1).isText_bold());
        std.setText_italic(arr.get(arr.size() - 1).isText_italic());
        std.setText_underline(arr.get(arr.size() - 1).isText_underline());
        std.setText_strike(arr.get(arr.size() - 1).isText_strike());
        arr.add(std);
        defaultsetup();
    }

    /* access modifiers changed from: private */
    public void setSelectionAll(EditText text_tv2) {
        Selection.removeSelection(text_tv2.getText());
        callFous();
        text_tv2.setSelectAllOnFocus(true);
        text_tv2.setTextIsSelectable(true);
        text_tv2.setCursorVisible(true);
        text_tv2.setSelection(0, text_tv2.getText().length());
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void checkandShowEditRel() {
        if (this.isfrst) {
            if (this.alledit_ll.getVisibility() != 0) {
                this.menu_ll.setVisibility(8);
                this.alledit_ll.setVisibility(0);
             //   this.sb_effectsfilter.setVisibility(8);

                this.controll_btn_stckr.setVisibility(8);
                this.controller_sticker.setVisibility(8);

                this.top_option.setVisibility(8);
                this.color_rel.setVisibility(4);
                this.shadow_rel.setVisibility(4);
                this.shader_rel.setVisibility(4);
                this.font_rel.setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.for_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.font_txt)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                ((TextView) findViewById(R.id.co_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shw_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shdr_txt)).setTextColor(-1);

                this.fonts.setBackgroundResource(R.drawable.fonts);
                this.size.setBackgroundResource(R.drawable.size1);
                this.color_.setBackgroundResource(R.drawable.color_1);
                this.shadow.setBackgroundResource(R.drawable.shadow1);
                this.shader.setBackgroundResource(R.drawable.shader_1);

                this.la_size.setBackgroundResource(R.drawable.gradient);
                this.la_fonts.setBackgroundResource(R.drawable.gradient);
                this.la_color.setBackgroundResource(R.drawable.gradient);
                this.la_shadow.setBackgroundResource(R.drawable.gradient);
                this.la_shader.setBackgroundResource(R.drawable.gradient);

                this.isfrst = false;
            }
        } else if (this.alledit_ll.getVisibility() != 0) {
            this.menu_ll.setVisibility(8);
            this.alledit_ll.setVisibility(0);
            //this.sb_effectsfilter.setVisibility(8);




            this.controll_btn_stckr.setVisibility(8);
            this.controller_sticker.setVisibility(8);

        }
    }

    private int comapreSizes1(int textSize, int punchSize, ArrayList<SelectedTextData> alstd) {
        TextPaint textpaintNormal = new TextPaint();
        TextPaint textPaintPunch = new TextPaint();
        Typeface punch = Typeface.createFromAsset(getAssets(), alstd.get(0).getText_ttf());
        textpaintNormal.setTextSize((float) textSize);
        textpaintNormal.setTypeface(this.textView.getTypeface());
        textPaintPunch.setTextSize(1.0f);
        textPaintPunch.setTypeface(punch);
        int normalHeight = new StaticLayout("Ay", textpaintNormal, 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true).getHeight();
        double d = (double) normalHeight;
        double d2 = (double) normalHeight;
        Double.isNaN(d2);
        Double.isNaN(d);
        RectF rectF = new RectF(0.0f, 0.0f, 100.0f, (float) ((int) (d + (d2 * 0.1d))));
        Typeface typeface = punch;
        RectF rectF2 = rectF;
        TextPaint textPaint = textPaintPunch;
        TextPaint textPaint2 = textpaintNormal;
        return getOptimumTextSize1(2, 400, "Ay", rectF2, textPaintPunch, this.text_tv.getGravity());
    }

    private void updatePunchSize(SpannableString spannableString2, ArrayList<SelectedTextData> arrayfortv2) {
        for (int i = 0; i < arrayfortv2.size(); i++) {
            if (arrayfortv2.get(i).getEnd() <= this.textView.getText().length()) {
                try {
                    spannableString2.setSpan(new TextAppearanceSpan((String) null, 0, (int) (((float) arrayfortv2.get(i).getText_size()) * getResources().getDisplayMetrics().density), (ColorStateList) null, (ColorStateList) null), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        SpannableStringBuilder builder2 = new SpannableStringBuilder().append(spannableString2);
        this.textView.setText(builder2.subSequence(0, builder2.length()));
        updateColors();
    }

    private void updateColorBarText(int currentTextColor) {
        if (this.shr1.equals("null")) {
            Drawable background = getResources().getDrawable(R.drawable.square_bk);
            int primaryColor = currentTextColor;
            this.existingcolor = primaryColor;
            background.setColorFilter(primaryColor, PorterDuff.Mode.DARKEN);
            this.img_format_txt.setBackgroundDrawable(background);
            this.img_font_txt.setBackgroundDrawable(background);
            this.img_shadow_txt.setBackgroundDrawable(background);
            this.img_shader_txt.setBackgroundDrawable(background);
            this.img_color_txt.setBackgroundDrawable(background);
            return;
        }
        Drawable background2 = getResources().getDrawable(getResources().getIdentifier(this.shr1, "drawable", getPackageName()));
        this.img_format_txt.setBackgroundDrawable(background2);
        this.img_font_txt.setBackgroundDrawable(background2);
        this.img_shadow_txt.setBackgroundDrawable(background2);
        this.img_shader_txt.setBackgroundDrawable(background2);
        this.img_color_txt.setBackgroundDrawable(background2);
    }

    private void updateCircleBarsPunch(int currentPunchColor) {
        if (this.arrayfortv.get(0).getText_shader().equals("null")) {
            Drawable background = getResources().getDrawable(R.drawable.square_bk1);
            int primaryColor = currentPunchColor;
            this.existingcolor = primaryColor;
            background.setColorFilter(primaryColor, PorterDuff.Mode.DARKEN);
            this.img_format_punch.setBackgroundDrawable(background);
            this.img_font_punch.setBackgroundDrawable(background);
            this.img_shadow_punch.setBackgroundDrawable(background);
            this.img_shader_punch.setBackgroundDrawable(background);
            this.img_color_punch.setBackgroundDrawable(background);
            return;
        }
        Drawable background2 = getResources().getDrawable(getResources().getIdentifier(this.arrayfortv.get(0).getText_shader(), "drawable", getPackageName()));
        this.img_format_punch.setBackgroundDrawable(background2);
        this.img_font_punch.setBackgroundDrawable(background2);
        this.img_shadow_punch.setBackgroundDrawable(background2);
        this.img_shader_punch.setBackgroundDrawable(background2);
        this.img_color_punch.setBackgroundDrawable(background2);
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void hideEditRel() {
        if (this.alledit_ll.getVisibility() == 0) {
            this.menu_ll.setVisibility(0);
            this.alledit_ll.setVisibility(8);
        //    this.sb_effectsfilter.setVisibility(0);



        }
    }

    public void defaultsetup() {
        Typeface ttq;
        updateColors();
        SpannableString spannableString2 = new SpannableString(this.textView.getText().toString());
        ArrayList<SelectedTextData> alstd = null;
        if (this.textView.getTag().equals("text_tv")) {
            alstd = this.arrayfortv;
        }
        for (int i = 0; i < alstd.size(); i++) {
            if (alstd.get(i).getEnd() <= this.textView.getText().length()) {
                try {
                    spannableString2.setSpan(new TextAppearanceSpan((String) null, 0, (int) (((float) alstd.get(i).getText_size()) * getResources().getDisplayMetrics().density), (ColorStateList) null, (ColorStateList) null), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    spannableString2.setSpan(new ForegroundColorSpan(alstd.get(i).getText_color()), alstd.get(i).getStart(), alstd.get(i).getEnd(), 33);
                    if (alstd.get(i).getText_ttf().equals("")) {
                        ttq = this.textView.getTypeface();
                    } else {
                        ttq = Typeface.createFromAsset(getAssets(), alstd.get(i).getText_ttf());
                    }
                    spannableString2.setSpan(new CustomTypefaceSpan(ttq), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    spannableString2.setSpan(new CustomShadowSpan(alstd.get(i).getText_shadowradius(), alstd.get(i).getText_shadowdx(), alstd.get(i).getText_shadowdy(), alstd.get(i).getText_shadowcolor()), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    Shader sadr = null;
                    if (!alstd.get(i).getText_shader().equals("null")) {
                        sadr = new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(alstd.get(i).getText_shader(), "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    }
                    spannableString2.setSpan(new CustomShaderSpan(sadr), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    new StyleSpan(1);
                    StyleSpan styleSpan = new StyleSpan(2);
                    if (alstd.get(i).isText_bold()) {
                        spannableString2.setSpan(new StyleSpan(1), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    } else {
                        spannableString2.removeSpan(styleSpan);
                    }
                    if (alstd.get(i).isText_italic()) {
                        spannableString2.setSpan(new StyleSpan(2), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    } else {
                        spannableString2.removeSpan(styleSpan);
                    }
                    if (alstd.get(i).isText_underline()) {
                        spannableString2.setSpan(new UnderlineSpan(), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    } else {
                        spannableString2.removeSpan(new UnderlineSpan());
                    }
                    if (alstd.get(i).isText_strike()) {
                        spannableString2.setSpan(new StrikethroughSpan(), alstd.get(i).getStart(), alstd.get(i).getEnd(), 0);
                    } else {
                        spannableString2.removeSpan(new StrikethroughSpan());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        SpannableStringBuilder builder2 = new SpannableStringBuilder().append(spannableString2);
        this.textView.setText(builder2.subSequence(0, builder2.length()));
    }

    /* access modifiers changed from: private */
    public void updateColors() {
        updateColorBarText(this.textView.getCurrentTextColor());
        if (this.arrayfortv.size() > 0) {
            updateCircleBarsPunch(this.arrayfortv.get(0).getText_color());
        }
    }

    public void setFontonSelected(String ttf) {
        Typeface ttq;
        SpannableString spannableString2 = new SpannableString(this.textView.getText().toString());
        ArrayList<SelectedTextData> alstd = null;
        if (this.textView.getTag().equals("text_tv")) {
            for (int i = 0; i < this.arrayfortv.size(); i++) {
                SelectedTextData d = this.arrayfortv.get(i);
                d.setText_ttf(ttf);
                this.arrayfortv.set(i, d);
                alstd = this.arrayfortv;
            }
        }
        if (alstd != null) {
            for (int i2 = 0; i2 < alstd.size(); i2++) {
                if (alstd.get(i2).getEnd() <= this.textView.getText().length()) {
                    try {
                        spannableString2.setSpan(new TextAppearanceSpan((String) null, 0, (int) (((float) alstd.get(i2).getText_size()) * getResources().getDisplayMetrics().density), (ColorStateList) null, (ColorStateList) null), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        spannableString2.setSpan(new ForegroundColorSpan(alstd.get(i2).getText_color()), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 33);
                        if (alstd.get(i2).getText_ttf().equals("")) {
                            ttq = this.textView.getTypeface();
                        } else {
                            ttq = Typeface.createFromAsset(getAssets(), alstd.get(i2).getText_ttf());
                        }
                        spannableString2.setSpan(new CustomTypefaceSpan(ttq), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        spannableString2.setSpan(new CustomShadowSpan(alstd.get(i2).getText_shadowradius(), alstd.get(i2).getText_shadowdx(), alstd.get(i2).getText_shadowdy(), alstd.get(i2).getText_shadowcolor()), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        if (!alstd.get(i2).getText_shader().equals("null")) {
                            new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(alstd.get(i2).getText_shader(), "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                        }
                        spannableString2.setSpan(new CustomShaderSpan((Shader) null), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        if (alstd.get(i2).isText_bold()) {
                            spannableString2.setSpan(new StyleSpan(1), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_italic()) {
                            spannableString2.setSpan(new StyleSpan(2), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_underline()) {
                            spannableString2.setSpan(new UnderlineSpan(), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_strike()) {
                            spannableString2.setSpan(new StrikethroughSpan(), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            SpannableStringBuilder builder2 = new SpannableStringBuilder().append(spannableString2);
            this.textView.setText(builder2.subSequence(0, builder2.length()));
            int normaltextSize = (int) this.textView.getTextSize();
            int comapreSizes1 = comapreSizes1(normaltextSize, (int) (((float) alstd.get(0).getText_size()) * getResources().getDisplayMetrics().density), alstd) - normaltextSize;
            this.textSizeOffset = comapreSizes1;
            if (comapreSizes1 < 0) {
                this.textSizeOffset = 5;
            }
            Log.e("textSizeOffset", "" + this.textSizeOffset);
            updateTextSizeonScale(this.rl.getWidth(), this.rl.getHeight());
        }
    }

    public void setColoronSelected(int color2) {
        Typeface ttq;
        Log.e("coloredText", "" + this.textView.getText().toString());
        SpannableString spannableString2 = new SpannableString(this.textView.getText().toString());
        ArrayList<SelectedTextData> alstd = null;
        if (this.textView.getTag().equals("text_tv") && this.arrayfortv.size() > 0) {
            for (int i = 0; i < this.arrayfortv.size(); i++) {
                SelectedTextData d = this.arrayfortv.get(i);
                d.setText_color(color2);
                this.arrayfortv.set(i, d);
                alstd = this.arrayfortv;
                Log.e("coloredText", "" + alstd.get(i).toString());
            }
        }
        if (alstd != null) {
            for (int i2 = 0; i2 < alstd.size(); i2++) {
                if (alstd.get(i2).getEnd() <= this.textView.getText().length()) {
                    try {
                        spannableString2.setSpan(new TextAppearanceSpan((String) null, 0, (int) (((float) alstd.get(i2).getText_size()) * getResources().getDisplayMetrics().density), (ColorStateList) null, (ColorStateList) null), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        spannableString2.setSpan(new ForegroundColorSpan(alstd.get(i2).getText_color()), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 33);
                        if (alstd.get(i2).getText_ttf().equals("")) {
                            ttq = this.textView.getTypeface();
                        } else {
                            ttq = Typeface.createFromAsset(getAssets(), alstd.get(i2).getText_ttf());
                        }
                        spannableString2.setSpan(new CustomTypefaceSpan(ttq), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        spannableString2.setSpan(new CustomShadowSpan(alstd.get(i2).getText_shadowradius(), alstd.get(i2).getText_shadowdx(), alstd.get(i2).getText_shadowdy(), alstd.get(i2).getText_shadowcolor()), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        Shader sadr = null;
                        if (!alstd.get(i2).getText_shader().equals("null")) {
                            sadr = new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(alstd.get(i2).getText_shader(), "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                        }
                        spannableString2.setSpan(new CustomShaderSpan(sadr), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        if (alstd.get(i2).isText_bold()) {
                            spannableString2.setSpan(new StyleSpan(1), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_italic()) {
                            spannableString2.setSpan(new StyleSpan(2), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_underline()) {
                            spannableString2.setSpan(new UnderlineSpan(), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_strike()) {
                            spannableString2.setSpan(new StrikethroughSpan(), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        SpannableStringBuilder builder2 = new SpannableStringBuilder().append(spannableString2);
        this.textView.setText(builder2.subSequence(0, builder2.length()));
    }

    public void setShadowonSelected(float radius, float dx, float dy, int color2) {
        ArrayList<SelectedTextData> alstd;
        Typeface ttq;
        SpannableString spannableString2 = new SpannableString(this.textView.getText().toString());
        ArrayList<SelectedTextData> alstd2 = null;
        if (this.textView.getTag().equals("text_tv")) {
            for (int i = 0; i < this.arrayfortv.size(); i++) {
                SelectedTextData d = this.arrayfortv.get(i);
                d.setText_shadowradius(radius);
                d.setText_shadowdx(dx);
                d.setText_shadowdy(dy);
                d.setText_shadowcolor(color2);
                this.arrayfortv.set(i, d);
                alstd2 = this.arrayfortv;
            }
            float f2 = radius;
            float f3 = dx;
            float f4 = dy;
            int i2 = color2;
            alstd = alstd2;
        } else {
            float f5 = radius;
            float f6 = dx;
            float f7 = dy;
            int i3 = color2;
            alstd = null;
        }
        int i4 = 0;
        if (alstd != null) {
            int i5 = 0;
            while (i5 < alstd.size()) {
                if (alstd.get(i5).getEnd() <= this.textView.getText().length()) {
                    try {
                        spannableString2.setSpan(new TextAppearanceSpan((String) null, 0, (int) (((float) alstd.get(i5).getText_size()) * getResources().getDisplayMetrics().density), (ColorStateList) null, (ColorStateList) null), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), i4);
                        spannableString2.setSpan(new ForegroundColorSpan(alstd.get(i5).getText_color()), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), 33);
                        if (alstd.get(i5).getText_ttf().equals("")) {
                            ttq = this.textView.getTypeface();
                        } else {
                            ttq = Typeface.createFromAsset(getAssets(), alstd.get(i5).getText_ttf());
                        }
                        spannableString2.setSpan(new CustomTypefaceSpan(ttq), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), i4);
                        spannableString2.setSpan(new CustomShadowSpan(alstd.get(i5).getText_shadowradius(), alstd.get(i5).getText_shadowdx(), alstd.get(i5).getText_shadowdy(), alstd.get(i5).getText_shadowcolor()), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), i4);
                        Shader sadr = null;
                        if (!alstd.get(i5).getText_shader().equals("null")) {
                            sadr = new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(alstd.get(i5).getText_shader(), "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                        }
                        spannableString2.setSpan(new CustomShaderSpan(sadr), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), 0);
                        if (alstd.get(i5).isText_bold()) {
                            spannableString2.setSpan(new StyleSpan(1), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), 0);
                        }
                        if (alstd.get(i5).isText_italic()) {
                            spannableString2.setSpan(new StyleSpan(2), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), 0);
                        }
                        if (alstd.get(i5).isText_underline()) {
                            spannableString2.setSpan(new UnderlineSpan(), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), 0);
                        }
                        if (alstd.get(i5).isText_strike()) {
                            spannableString2.setSpan(new StrikethroughSpan(), alstd.get(i5).getStart(), alstd.get(i5).getEnd(), 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                i5++;
                i4 = 0;
            }
        }
        SpannableStringBuilder builder2 = new SpannableStringBuilder().append(spannableString2);
        this.textView.setText(builder2.subSequence(0, builder2.length()));
    }

    public void setShaderonSelected(String shader2) {
        Typeface ttq;
        SpannableString spannableString2 = new SpannableString(this.textView.getText().toString());
        ArrayList<SelectedTextData> alstd = null;
        if (this.textView.getTag().equals("text_tv")) {
            for (int i = 0; i < this.arrayfortv.size(); i++) {
                SelectedTextData d = this.arrayfortv.get(i);
                d.setText_shader(shader2);
                this.arrayfortv.set(i, d);
                alstd = this.arrayfortv;
            }
        }
        if (alstd != null) {
            for (int i2 = 0; i2 < alstd.size(); i2++) {
                if (alstd.get(i2).getEnd() <= this.textView.getText().length()) {
                    try {
                        spannableString2.setSpan(new TextAppearanceSpan((String) null, 0, (int) (((float) alstd.get(i2).getText_size()) * getResources().getDisplayMetrics().density), (ColorStateList) null, (ColorStateList) null), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        spannableString2.setSpan(new ForegroundColorSpan(alstd.get(i2).getText_color()), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 33);
                        if (alstd.get(i2).getText_ttf().equals("")) {
                            ttq = this.textView.getTypeface();
                        } else {
                            ttq = Typeface.createFromAsset(getAssets(), alstd.get(i2).getText_ttf());
                        }
                        spannableString2.setSpan(new CustomTypefaceSpan(ttq), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        spannableString2.setSpan(new CustomShadowSpan(alstd.get(i2).getText_shadowradius(), alstd.get(i2).getText_shadowdx(), alstd.get(i2).getText_shadowdy(), alstd.get(i2).getText_shadowcolor()), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        Shader sadr = null;
                        if (!alstd.get(i2).getText_shader().equals("null")) {
                            sadr = new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(alstd.get(i2).getText_shader(), "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                        }
                        spannableString2.setSpan(new CustomShaderSpan(sadr), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        if (alstd.get(i2).isText_bold()) {
                            spannableString2.setSpan(new StyleSpan(1), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_italic()) {
                            spannableString2.setSpan(new StyleSpan(2), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_underline()) {
                            spannableString2.setSpan(new UnderlineSpan(), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                        if (alstd.get(i2).isText_strike()) {
                            spannableString2.setSpan(new StrikethroughSpan(), alstd.get(i2).getStart(), alstd.get(i2).getEnd(), 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!shader2.equals("null")) {
            new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(shader2, "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        }
        SpannableStringBuilder builder2 = new SpannableStringBuilder().append(spannableString2);
        this.textView.setText(builder2.subSequence(0, builder2.length()));
    }

    @SuppressLint("WrongConstant")
    private void initialize() {
        this.dbHelper = new DatabaseHandler(this);
        this.view_width = (float) Constants.dpToPx(getApplicationContext(), 40);
        this.view_height = (float) Constants.dpToPx(getApplication(), 100);

        this.img_opacity = (ImageView) findViewById(R.id.img_opacity);
        this.back_arrow_add_quotes = (ImageView) findViewById(R.id.back_arrow_add_quotes);

        this.complete_img = (RelativeLayout) findViewById(R.id.complete_img);
        this.top_option = (RelativeLayout) findViewById(R.id.top_option);
        this.done_add_quotes = (Button) findViewById(R.id.done_add_quotes);
        this.controller_sticker = (RelativeLayout) findViewById(R.id.controller_sticker);
        this.controll_btn_stckr = (Button) findViewById(R.id.control_btn_stkr);

        this.btn_left = (ImageView) findViewById(R.id.left);
        this.btn_top = (ImageView) findViewById(R.id.top);
        this.btn_right = (ImageView) findViewById(R.id.right);
        this.btn_down = (ImageView) findViewById(R.id.down);
        Button button = (Button) findViewById(R.id.duplicate);
        this.duplicate = button;
        button.setTypeface(Constants.getTextTypeface(this));
        this.tab_cntrl_stkr = (RelativeLayout) findViewById(R.id.controlls_stkr);
        this.tab_clrs_stkr = (RelativeLayout) findViewById(R.id.clr_opacity);
        this.tab_cntrl_stkr.setOnClickListener(this);
        this.tab_clrs_stkr.setOnClickListener(this);
        this.cntrls_stkr_lay = (RelativeLayout) findViewById(R.id.controlls);
        this.cntrls_stkrclr_lay = (RelativeLayout) findViewById(R.id.clr_stkr);
        this.picture_txt = (CustomTextView) findViewById(R.id.footer_text3);
        this.sticker_txt = (CustomTextView) findViewById(R.id.footer_text4);
        this.effect_txt = (CustomTextView) findViewById(R.id.footer_text5);
        this.picture_txt.setTextColor(-1);
        this.sticker_txt.setTextColor(-1);
        this.effect_txt.setTextColor(-1);
        this.tab_text = (RelativeLayout) findViewById(R.id.tabtxtrel);
        this.tab_punch = (RelativeLayout) findViewById(R.id.tabpunchrel);
        this.tab_shadow_punch = (RelativeLayout) findViewById(R.id.tabshadowpunch);
        this.tab_shadow_txt = (RelativeLayout) findViewById(R.id.tabShadowtxt);
        this.tab_shader_punch = (RelativeLayout) findViewById(R.id.tabshaderpunch);
        this.tab_shader_txt = (RelativeLayout) findViewById(R.id.tabshadertxt);
        this.tab_font_punch = (RelativeLayout) findViewById(R.id.tabfontpunch);
        this.tab_font_txt = (RelativeLayout) findViewById(R.id.tabfonttxt);
        this.tab_format_txt = (RelativeLayout) findViewById(R.id.tabformattxt);
        this.tab_format_punch = (RelativeLayout) findViewById(R.id.tabformatpunch);
        this.gpuImageview = (GPUImageView) findViewById(R.id.gpuimage);
        this.done_add_quotes.setOnClickListener(this);
        this.back_arrow_add_quotes.setOnClickListener(this);
        this.tab_text.setOnClickListener(this);
        this.tab_punch.setOnClickListener(this);
        this.tab_shadow_punch.setOnClickListener(this);
        this.tab_shadow_txt.setOnClickListener(this);
        this.tab_shader_punch.setOnClickListener(this);
        this.tab_shader_txt.setOnClickListener(this);
        this.tab_font_punch.setOnClickListener(this);
        this.tab_font_txt.setOnClickListener(this);
        this.tab_format_txt.setOnClickListener(this);
        this.tab_format_punch.setOnClickListener(this);
        this.img_format_txt = (ImageView) findViewById(R.id.img_format_txt);
        this.img_format_punch = (ImageView) findViewById(R.id.img_format_punch);
        this.img_font_txt = (ImageView) findViewById(R.id.img_font_txt);
        this.img_font_punch = (ImageView) findViewById(R.id.img_font_punch);
        this.img_shadow_txt = (ImageView) findViewById(R.id.img_shadow_txt);
        this.img_shadow_punch = (ImageView) findViewById(R.id.img_shadow_punch);
        this.img_shader_txt = (ImageView) findViewById(R.id.img_shader_txt);
        this.img_shader_punch = (ImageView) findViewById(R.id.img_shader_punch);
        this.img_color_txt = (ImageView) findViewById(R.id.img_color_txt);
        this.img_color_punch = (ImageView) findViewById(R.id.img_color_punch);
        this.format_txt = (CustomTextView) findViewById(R.id.txt_format);
        this.format_punch = (CustomTextView) findViewById(R.id.punch_format);
        this.font_txt = (CustomTextView) findViewById(R.id.txt_font);
        this.font_punch = (CustomTextView) findViewById(R.id.punch_font);
        this.shadow_txt = (CustomTextView) findViewById(R.id.txt_shadow);
        this.shadow_punch = (CustomTextView) findViewById(R.id.punch_shadow);
        this.shader_txt = (CustomTextView) findViewById(R.id.txt_shader);
        this.shader_punch = (CustomTextView) findViewById(R.id.punch_shader);
        this.color_txt = (CustomTextView) findViewById(R.id.txt_clr);
        this.color_punch = (CustomTextView) findViewById(R.id.txt_punch);
        this.contrl_txt = (CustomTextView) findViewById(R.id.controls_txt);
        this.clr_opacity_txt = (CustomTextView) findViewById(R.id.clr_opacity_txt);
        this.rl = (RelativeLayout) findViewById(R.id.rl);
        this.format_punch.setTextColor(-1);
        this.font_punch.setTextColor(-1);
        this.color_punch.setTextColor(-1);
        this.shadow_punch.setTextColor(-1);
        this.shader_punch.setTextColor(-1);
        this.lay_hue = (RelativeLayout) findViewById(R.id.lay_hue);
        this.lay_colorfilter = (RelativeLayout) findViewById(R.id.lay_colorfilter);
        EditText editText = (EditText) findViewById(R.id.text);
        this.text_tv = editText;
        editText.setTag("text_tv");
        isTextTouchListener = setDefaultTouchListener(true);
        ImageView imageView = (ImageView) findViewById(R.id.rotate);
        this.rotate_iv = imageView;
        imageView.setTag("rotate_iv");
        this.rotate_iv.setOnTouchListener(this.mTouchListener);
        ImageView imageView2 = (ImageView) findViewById(R.id.delete);
        this.delete_iv = imageView2;
        imageView2.setOnClickListener(this.deleteClickListener);
        CustomTextView customTextView = (CustomTextView) findViewById(R.id.edit);
        this.edit_ivTxt = customTextView;
        customTextView.setOnClickListener(this.editTextClickListener);
        ImageView imageView3 = (ImageView) findViewById(R.id.move);
        this.move_iv = imageView3;
        imageView3.setTag("move_iv");
        this.move_iv.setOnTouchListener(this.mTouchListener);
        ImageView imageView4 = (ImageView) findViewById(R.id.scale);
        this.scale_iv = imageView4;
        imageView4.setTag("scale_iv");
        this.scale_iv.setOnTouchListener(this.mTouchListener);
        Button fnt1 = (Button) findViewById(R.id.fnt1);
        Button fnt2 = (Button) findViewById(R.id.fnt2);
        Button fnt3 = (Button) findViewById(R.id.fnt3);
        Button fnt15 = (Button) findViewById(R.id.fnt15);
        Button fnt16 = (Button) findViewById(R.id.fnt16);
        Button fnt17 = (Button) findViewById(R.id.fnt17);
        Button fnt18 = (Button) findViewById(R.id.fnt18);
        Button fnt19 = (Button) findViewById(R.id.fnt19);
        Button fnt20 = (Button) findViewById(R.id.fnt20);
        Button fnt21 = (Button) findViewById(R.id.fnt21);
        Button fnt22 = (Button) findViewById(R.id.fnt22);
        Button fnt23 = (Button) findViewById(R.id.fnt23);
        Button fnt24 = (Button) findViewById(R.id.fnt24);
        Button fnt25 = (Button) findViewById(R.id.fnt25);
        Button fnt26 = (Button) findViewById(R.id.fnt26);
        Button fnt27 = (Button) findViewById(R.id.fnt27);
        Button fnt28 = (Button) findViewById(R.id.fnt28);
        Button fnt29 = (Button) findViewById(R.id.fnt29);
        this.g1 = (Button) findViewById(R.id.g1);
        this.g2 = (Button) findViewById(R.id.g2);
        this.g3 = (Button) findViewById(R.id.g3);
        this.sd_color = (Button) findViewById(R.id.sd_color);
        this.sh = (Button) findViewById(R.id.sh);
        this.sh1 = (Button) findViewById(R.id.sh1);
        this.sh2 = (Button) findViewById(R.id.sh2);
        this.sh3 = (Button) findViewById(R.id.sh3);
        this.sh4 = (Button) findViewById(R.id.sh4);
        this.sh5 = (Button) findViewById(R.id.sh5);
        this.sh6 = (Button) findViewById(R.id.sh6);
        this.sh7 = (Button) findViewById(R.id.sh7);
        this.sh8 = (Button) findViewById(R.id.sh8);
        this.sh9 = (Button) findViewById(R.id.sh9);
        this.sh10 = (Button) findViewById(R.id.sh10);
        this.bold = (Button) findViewById(R.id.bold);
        this.italic = (Button) findViewById(R.id.italic);
        this.underline = (Button) findViewById(R.id.underline);
        this.strike = (Button) findViewById(R.id.strike);
        this.ttD = Typeface.createFromAsset(getAssets(), "DroidSans.ttf");
        this.ttf1 = Typeface.createFromAsset(getAssets(), "majalla.ttf");
        this.ttf2 = Typeface.createFromAsset(getAssets(), "MVBOLI.TTF");
        this.ttf3 = Typeface.createFromAsset(getAssets(), "PortLligatSans-Regular.ttf");
        this.ttf4 = Typeface.createFromAsset(getAssets(), "ROD.TTF");
        this.ttf5 = Typeface.createFromAsset(getAssets(), "Aspergit.otf");
        this.ttf6 = Typeface.createFromAsset(getAssets(), "windsong.ttf");
        this.ttf7 = Typeface.createFromAsset(getAssets(), "Walkway_Bold.ttf");
        this.ttf8 = Typeface.createFromAsset(getAssets(), "Sofia-Regular.otf");
        this.ttf9 = Typeface.createFromAsset(getAssets(), "segoe.ttf");
        this.ttf10 = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");
        this.ttf11 = Typeface.createFromAsset(getAssets(), "Advertising Script Bold Trial.ttf");
        this.ttf12 = Typeface.createFromAsset(getAssets(), "Advertising Script Monoline Trial.ttf");
        this.ttf13 = Typeface.createFromAsset(getAssets(), "Beyond Wonderland.ttf");
        this.ttf14 = Typeface.createFromAsset(getAssets(), "CalliGravity.ttf");
        this.ttf15 = Typeface.createFromAsset(getAssets(), "Cosmic Love.ttf");
        this.ttf16 = Typeface.createFromAsset(getAssets(), "lesser concern shadow.ttf");
        this.ttf17 = Typeface.createFromAsset(getAssets(), "lesser concern.ttf");
        this.ttf18 = Typeface.createFromAsset(getAssets(), "Queen of Heaven.ttf");
        this.ttf19 = Typeface.createFromAsset(getAssets(), "QUIGLEYW.TTF");
        this.ttf20 = Typeface.createFromAsset(getAssets(), "squealer embossed.ttf");
        this.ttf21 = Typeface.createFromAsset(getAssets(), "squealer.ttf");
        this.ttf22 = Typeface.createFromAsset(getAssets(), "still time.ttf");
        this.ttf23 = Typeface.createFromAsset(getAssets(), "Constantia Italic.ttf");
        this.ttf24 = Typeface.createFromAsset(getAssets(), "DejaVuSans_Bold.ttf");
        this.ttf25 = Typeface.createFromAsset(getAssets(), "Aladin_Regular.ttf");
        this.ttf26 = Typeface.createFromAsset(getAssets(), "Adobe Caslon Pro Italic.ttf");
        this.ttf27 = Typeface.createFromAsset(getAssets(), "aparaji.ttf");
        this.ttf28 = Typeface.createFromAsset(getAssets(), "ARDECODE.ttf");
        this.ttf29 = Typeface.createFromAsset(getAssets(), "ufonts_com_ck_scratchy_box.ttf");
        fnt1.setTypeface(this.ttf1);
        fnt2.setTypeface(this.ttf7);
        fnt3.setTypeface(this.ttf3);
        ((Button) findViewById(R.id.fnt4)).setTypeface(this.ttf4);
        ((Button) findViewById(R.id.fnt5)).setTypeface(this.ttf5);
        ((Button) findViewById(R.id.fnt6)).setTypeface(this.ttf6);
        ((Button) findViewById(R.id.fnt7)).setTypeface(this.ttf2);
        ((Button) findViewById(R.id.fnt8)).setTypeface(this.ttf8);
        ((Button) findViewById(R.id.fnt9)).setTypeface(this.ttf9);
        ((Button) findViewById(R.id.fnt10)).setTypeface(this.ttf10);
        ((Button) findViewById(R.id.fnt11)).setTypeface(this.ttf11);
        ((Button) findViewById(R.id.fnt12)).setTypeface(this.ttf12);
        ((Button) findViewById(R.id.fnt13)).setTypeface(this.ttf13);
        ((Button) findViewById(R.id.fnt14)).setTypeface(this.ttf14);
        fnt15.setTypeface(this.ttf15);
        fnt16.setTypeface(this.ttf16);
        fnt17.setTypeface(this.ttf17);
        fnt18.setTypeface(this.ttf18);
        fnt19.setTypeface(this.ttf19);
        fnt20.setTypeface(this.ttf20);
        fnt21.setTypeface(this.ttf21);
        fnt22.setTypeface(this.ttf22);
        fnt23.setTypeface(this.ttf23);
        fnt24.setTypeface(this.ttf24);
        fnt25.setTypeface(this.ttf25);
        fnt26.setTypeface(this.ttf26);
        fnt27.setTypeface(this.ttf27);
        fnt28.setTypeface(this.ttf28);
        fnt29.setTypeface(this.ttf29);


        this.fortext = (RelativeLayout) findViewById(R.id.fortext);

        this.alledit_ll = (RelativeLayout) findViewById(R.id.alledit_ll);
        this.menu_ll = (LinearLayout) findViewById(R.id.menu_ll);
        this.formatall_type = (LinearLayout) findViewById(R.id.format_alltype);




        this.hue_seekbar = (SeekBar) findViewById(R.id.hue_seekBar);
        this.colorPicker = (LineColorPicker) findViewById(R.id.picker);
        this.la_size = (RelativeLayout) findViewById(R.id.la_size);
        this.la_fonts = (RelativeLayout) findViewById(R.id.la_fonts);
        this.la_color = (RelativeLayout) findViewById(R.id.la_color);
        this.la_shadow = (RelativeLayout) findViewById(R.id.la_shadow);
        this.la_shader = (RelativeLayout) findViewById(R.id.la_shader);

        this.font_rel = (RelativeLayout) findViewById(R.id.font_rel);
        this.color_rel = (RelativeLayout) findViewById(R.id.color_rel);
        this.shadow_rel = (RelativeLayout) findViewById(R.id.shadow_rel);
        this.shader_rel = (RelativeLayout) findViewById(R.id.shader_rel);

        this.list_of_brnd = (ImageView) findViewById(R.id.list_of_brnd);
        this.list_of_sticker = (ImageView) findViewById(R.id.list_of_sticker);

        this.size = (ImageView) findViewById(R.id.size);
        this.fonts = (ImageView) findViewById(R.id.fonts);
        this.color_ = (ImageView) findViewById(R.id.color);
        this.shadow = (ImageView) findViewById(R.id.shadow);
        this.shader = (ImageView) findViewById(R.id.shader);

        this.re_addnewText = (RelativeLayout) findViewById(R.id.re_addnewText);
        this.re_sticker = (RelativeLayout) findViewById(R.id.re_sticker);
        this.hue_seekbar.setProgress(1);
        SeekBar seekBar = (SeekBar) findViewById(R.id.trans_seekBar);
        this.transparency_seekbar = seekBar;
        seekBar.setProgress(255);
        this.transparency_seekbar.setOnSeekBarChangeListener(this);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.shadow_seekBar);
        this.shadow_seekbar = seekBar2;
        seekBar2.setProgress(1);
        this.shadow_seekbar.setOnSeekBarChangeListener(this);
       // this.sb_effectsfilter.setProgress(25);
        this.re_addnewText.setOnClickListener(this);
        this.re_sticker.setOnClickListener(this);
        //this.sb_effectsfilter.setOnSeekBarChangeListener(this);

       // this.hue_seekbar.setOnSeekBarChangeListener(this);
        this.img_opacity.setImageAlpha(25);

        this.img_opacity.setVisibility(0);
        int[] colors = new int[this.pallete.length];
        int i = 0;
        while (true) {
            Button fnt12 = fnt1;
            if (i < colors.length) {
                colors[i] = Color.parseColor(this.pallete[i]);
                i++;
                fnt1 = fnt12;
            } else {
                this.colorPicker.setColors(colors);
                this.colorPicker.setSelectedColor(colors[8]);
                this.color = this.colorPicker.getColor();
                this.colorPicker.setOnColorChangedListener(new OnColorChangedListener() {
                    public void onColorChanged(int c) {
                        int unused = AddTextQuotesActivity.this.color = c;
                        int childCount1 = AddTextQuotesActivity.this.fortext.getChildCount();
                        Log.e("childCount1", "" + childCount1);
                        for (int i = 0; i < childCount1; i++) {
                            View view1 = AddTextQuotesActivity.this.fortext.getChildAt(i);
                            if ((view1 instanceof ResizableImageview) && ((ResizableImageview) view1).getBorderVisbilty()) {
                                Log.e("color", "" + AddTextQuotesActivity.this.color);
                                Log.e("setColorFilter", "" + i);
                                ((ResizableImageview) view1).setColorFilter(AddTextQuotesActivity.this.color);
                            }
                        }
                    }
                });
                int[] iArr = colors;
                Button button2 = fnt2;
                Button button3 = fnt3;
                this.btn_left.setOnTouchListener(new RepeatListener(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 100, new View.OnClickListener() {
                    public void onClick(View view) {
                        AddTextQuotesActivity.this.updatePositionSticker("decX");
                    }
                }));
                this.btn_right.setOnTouchListener(new RepeatListener(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 100, new View.OnClickListener() {
                    public void onClick(View view) {
                        AddTextQuotesActivity.this.updatePositionSticker("incrX");
                    }
                }));
                this.btn_top.setOnTouchListener(new RepeatListener(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 100, new View.OnClickListener() {
                    public void onClick(View view) {
                        AddTextQuotesActivity.this.updatePositionSticker("decY");
                    }
                }));
                this.btn_down.setOnTouchListener(new RepeatListener(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 100, new View.OnClickListener() {
                    public void onClick(View view) {
                        AddTextQuotesActivity.this.updatePositionSticker("incrY");
                    }
                }));
                this.controller_sticker.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    }
                });
                this.formatall_type.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    }
                });
                this.controll_btn_stckr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (AddTextQuotesActivity.this.isStickerLayVisible) {
                            AddTextQuotesActivity.this.controll_btn_stckr.setBackgroundResource(R.drawable.slide_up);
                            AddTextQuotesActivity.this.controller_sticker.setVisibility(8);
                            AddTextQuotesActivity.this.isStickerLayVisible = false;
                            return;
                        }
                        AddTextQuotesActivity.this.controll_btn_stckr.setBackgroundResource(R.drawable.slide_down);
                        AddTextQuotesActivity.this.controller_sticker.setVisibility(0);
                        AddTextQuotesActivity.this.isStickerLayVisible = true;
                    }
                });
                this.duplicate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int childCount = AddTextQuotesActivity.this.fortext.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View view = AddTextQuotesActivity.this.fortext.getChildAt(i);
                            if ((view instanceof ResizableImageview) && ((ResizableImageview) view).getBorderVisbilty()) {
                                ResizableImageview riv = new ResizableImageview(AddTextQuotesActivity.this);
                                riv.setComponentInfo(((ResizableImageview) view).getComponentInfo());
                                AddTextQuotesActivity.this.fortext.addView(riv);
                                ((ResizableImageview) view).setBorderVisibility(false);
                                riv.setMainLayoutWH((float) AddTextQuotesActivity.this.fortext.getWidth(), (float) AddTextQuotesActivity.this.fortext.getHeight());
                                riv.setOnTouchListener(new MultiTouchListener().enableRotation(true).setOnTouchCallbackListener(AddTextQuotesActivity.this));
                                riv.setOnTouchCallbackListener(AddTextQuotesActivity.this);
                                riv.setBorderVisibility(true);
                            }
                        }
                    }
                });

                this.gpuImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        AddTextQuotesActivity.this.isStickerLayVisible = true;
                        /*if (AddTextQuotesActivity.this.isBlurLayVisible) {
                            AddTextQuotesActivity.this.blur_lay_cntrl.setVisibility(0);
                            AddTextQuotesActivity.this.blur_lay_cntrl.setBackgroundResource(R.drawable.slide_down);

                         //   AddTextQuotesActivity.this.sb_effectsfilter.setVisibility(0);

                        } else {
                            AddTextQuotesActivity.this.blur_lay_cntrl.setVisibility(0);
                            AddTextQuotesActivity.this.blur_lay_cntrl.setBackgroundResource(R.drawable.slide_up);

                          //  AddTextQuotesActivity.this.sb_effectsfilter.setVisibility(8);

                        }*/
                        AddTextQuotesActivity.this.controll_btn_stckr.setVisibility(8);
                        AddTextQuotesActivity.this.controller_sticker.setVisibility(8);
                        AddTextQuotesActivity.this.removeImageViewControll();
                        AddTextQuotesActivity.this.fortext.post(new Runnable() {
                            public void run() {
                                PictureConstant.removeTextViewControll(AddTextQuotesActivity.this.fortext);
                            }
                        });
                        AddTextQuotesActivity.this.selectFocus = true;
                        AddTextQuotesActivity.this.setDefault();
                        AddTextQuotesActivity.this.flag2 = false;
                        if (AddTextQuotesActivity.this.textView != null) {
                            if (AddTextQuotesActivity.this.textView.getTag().equals("text_tv") && AddTextQuotesActivity.this.textView.getText().toString().equals("")) {
                                AddTextQuotesActivity.this.text_tv.setText(AddTextQuotesActivity.this.getResources().getString(R.string.tab_write));
                            }
                            AddTextQuotesActivity.this.textView.clearFocus();
                            Selection.removeSelection(AddTextQuotesActivity.this.textView.getText());
                            if (AddTextQuotesActivity.this.rl.getVisibility() == 0) {
                                Selection.removeSelection(AddTextQuotesActivity.this.text_tv.getText());
                                AddTextQuotesActivity addTextQuotesActivity = AddTextQuotesActivity.this;
                                addTextQuotesActivity.hideChilds(addTextQuotesActivity.rl);
                            }
                        }
                        if (AddTextQuotesActivity.this.alledit_ll.getVisibility() == 0) {
                            AddTextQuotesActivity.this.menu_ll.setVisibility(0);
                            AddTextQuotesActivity.this.alledit_ll.setVisibility(8);
                           // AddTextQuotesActivity.this.sb_effectsfilter.setVisibility(0);



                        }
                        View view = AddTextQuotesActivity.this.getCurrentFocus();
                        if (view != null) {
                            ((InputMethodManager) AddTextQuotesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                });
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public String setSelectionAll1(EditText text_tv2) {
        Selection.removeSelection(text_tv2.getText());
        text_tv2.setCursorVisible(false);
        int max2 = text_tv2.getText().length();
        text_tv2.setSelection(max2, max2);
        return text_tv2.getText().toString();
    }

    /* access modifiers changed from: private */
    public void updateTextSizeonScale(int width, int height) {
        int suggestedSize;
        int rectRight = width;
        int rectBottom = height - Constants.dpToPx(this, 75);
        if (rectRight > 0 && rectBottom > 0) {
            RectF boundsFloat = new RectF(0.0f, 0.0f, (float) rectRight, (float) rectBottom);
            int gravity = this.textView.getGravity();
            TextPaint textPaint = this.textView.getPaint();
            this.spannableString = new SpannableString(this.textView.getText().toString());
            if (this.arrayfortv.size() > 0) {
                int suggestedSize2 = this.arrayfortv.get(0).getText_size();
                Log.e("perfectpunchSize2", "" + (((float) suggestedSize2) * getResources().getDisplayMetrics().density));
                suggestedSize = suggestedSize2;
            } else {
                suggestedSize = (int) this.textView.getTextSize();
            }
            SpannableStringBuilder createSpannableString = createSpannableString(this.arrayfortv, suggestedSize, this.spannableString, true);
            this.builder = createSpannableString;
            this.textView.setTextSize(0, (float) (getOptimumTextSize(5, 400, createSpannableString, boundsFloat, textPaint, gravity) - this.textSizeOffset));
            int k = (int) PictureConstant.convertPixelsToDp((float) (this.textSizeOffset + ((int) Float.valueOf(this.textView.getTextSize()).floatValue())), this);
            Log.e("perfectpunchSize3", "" + (((float) k) * getResources().getDisplayMetrics().density));
            if (this.textView.getTag().equals("text_tv")) {
                for (int i = 0; i < this.arrayfortv.size(); i++) {
                    this.arrayfortv.get(i).setText_size(k);
                }
            }
            updatePunchSize(this.spannableString, this.arrayfortv);
        }
    }

    @SuppressLint("WrongConstant")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_add_quotes:
                this.selectFocus = true;
                onBackPressed();
                return;
            case R.id.c1:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(SupportMenu.CATEGORY_MASK);
                    this.existingcolor = SupportMenu.CATEGORY_MASK;
                    Log.e("coloredText1", "" + this.textView.getText().toString());
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(SupportMenu.CATEGORY_MASK);
                    this.existingcolor = SupportMenu.CATEGORY_MASK;
                }
                updateColors();
                return;
            case R.id.c11:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(-1702802);
                    this.existingcolor = -1702802;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(-1702802);
                    this.existingcolor = -1702802;
                }
                updateColors();
                return;
            case R.id.c12:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(-223198);
                    this.existingcolor = -223198;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(-223198);
                    this.existingcolor = -223198;
                }
                updateColors();
                return;
            case R.id.c13:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(-5366888);
                    this.existingcolor = -5366888;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(-5366888);
                    this.existingcolor = -5366888;
                }
                updateColors();
                return;
            case R.id.c14:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(-16238219);
                    this.existingcolor = -16238219;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(-16238219);
                    this.existingcolor = -16238219;
                }
                updateColors();
                return;
            case R.id.c2:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(-16711936);
                    this.existingcolor = -16711936;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(-16711936);
                    this.existingcolor = -16711936;
                }
                updateColors();
                return;
            case R.id.c3:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(-16776961);
                    this.existingcolor = -16776961;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(-16776961);
                    this.existingcolor = -16776961;
                }
                updateColors();
                return;
            case R.id.c4:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(InputDeviceCompat.SOURCE_ANY);
                    this.existingcolor = InputDeviceCompat.SOURCE_ANY;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(InputDeviceCompat.SOURCE_ANY);
                    this.existingcolor = InputDeviceCompat.SOURCE_ANY;
                }
                updateColors();
                return;
            case R.id.c5:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(-2697256);
                    this.existingcolor = -2697256;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(-2697256);
                    this.existingcolor = -2697256;
                }
                updateColors();
                return;
            case R.id.c6:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    this.existingcolor = ViewCompat.MEASURED_STATE_MASK;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(ViewCompat.MEASURED_STATE_MASK);
                    this.existingcolor = ViewCompat.MEASURED_STATE_MASK;
                }
                updateColors();
                return;
            case R.id.clr_opacity:
                this.contrl_txt.setTextColor(-1);
                this.clr_opacity_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.tab_cntrl_stkr.setBackgroundResource(R.drawable.gradient);
                this.tab_clrs_stkr.setBackgroundColor(-1);
                this.cntrls_stkr_lay.setVisibility(8);
                this.cntrls_stkrclr_lay.setVisibility(0);
                return;
            case R.id.colorpicker:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modeClrSelection.equals("textclr")) {
                    this.existingcolor = this.textView.getCurrentTextColor();
                } else if (this.arrayfortv.size() > 0) {
                    this.existingcolor = this.arrayfortv.get(0).getText_color();
                }
                new AmbilWarnaDialog(this, this.existingcolor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        if (AddTextQuotesActivity.this.modeClrSelection.equals("textclr")) {
                            AddTextQuotesActivity.this.shr1 = "null";
                            AddTextQuotesActivity.this.textView.getPaint().setShader((Shader) null);
                            AddTextQuotesActivity.this.textView.postInvalidate();
                            AddTextQuotesActivity.this.textView.requestLayout();
                            AddTextQuotesActivity.this.textView.setTextColor(color);
                            AddTextQuotesActivity.this.existingcolor = color;
                        } else {
                            AddTextQuotesActivity.this.setShaderonSelected("null");
                            AddTextQuotesActivity.this.setColoronSelected(color);
                            AddTextQuotesActivity.this.existingcolor = color;
                        }
                        AddTextQuotesActivity.this.updateColors();
                    }

                    public void onCancel(AmbilWarnaDialog dialog) {
                        if (AddTextQuotesActivity.this.b == 0 || AddTextQuotesActivity.this.b == -1) {
                            AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.start, AddTextQuotesActivity.this.end);
                        } else {
                            AddTextQuotesActivity.this.textView.setSelection(AddTextQuotesActivity.this.a, AddTextQuotesActivity.this.b);
                        }
                    }
                }).show();
                return;
            case R.id.colorpicker1:
                new AmbilWarnaDialog(this, this.color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    public void onOk(AmbilWarnaDialog dialog, int colorSelect) {
                        int unused = AddTextQuotesActivity.this.color = colorSelect;
                        int childCount1 = AddTextQuotesActivity.this.fortext.getChildCount();
                        for (int i = 0; i < childCount1; i++) {
                            View view1 = AddTextQuotesActivity.this.fortext.getChildAt(i);
                            if ((view1 instanceof ResizableImageview) && ((ResizableImageview) view1).getBorderVisbilty()) {
                                ((ResizableImageview) view1).setColorFilter(AddTextQuotesActivity.this.color);
                            }
                        }
                    }

                    public void onCancel(AmbilWarnaDialog dialog) {
                    }
                }).show();
                return;
            case R.id.controlls_stkr:
                this.tab_cntrl_stkr.setBackgroundColor(-1);
                this.tab_clrs_stkr.setBackgroundResource(R.drawable.gradient);
                this.contrl_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.clr_opacity_txt.setTextColor(-1);
                this.cntrls_stkr_lay.setVisibility(0);
                this.cntrls_stkrclr_lay.setVisibility(8);
                return;
            case R.id.done_add_quotes:
                this.selectFocus = true;
                EditText editText = this.textView;
                if (editText != null) {
                    Selection.removeSelection(editText.getText());
                }
                removeImageViewControll();
                this.controll_btn_stckr.setVisibility(8);
                this.controller_sticker.setVisibility(8);
                EditText editText2 = this.textView;
                if (editText2 != null) {
                    editText2.setCursorVisible(false);
                    int noch = ((ViewGroup) this.textView.getParent()).getChildCount();
                    for (int i = 1; i < noch; i++) {
                        ((ViewGroup) this.textView.getParent()).getChildAt(i).setVisibility(4);
                    }
                }
                try {
                    this.complete_img.setDrawingCacheEnabled(true);
                    this.bitRel = Bitmap.createBitmap(this.complete_img.getDrawingCache());
                    this.complete_img.setDrawingCacheEnabled(false);
                    try {
                        this.bb = Bitmap.createBitmap(this.gpuImageview.capture());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitRel_logo = mergeBitmap(this.bb, this.bitRel);
                    bitmapOriginal = bitRel_logo;
                    this.bitRel = bitRel_logo;
                    saveBitmap(true);
                    return;
                } catch (OutOfMemoryError e2) {
                    e2.printStackTrace();
                    return;
                } catch (Exception e3) {
                    e3.printStackTrace();
                    return;
                }
            case R.id.dropcolor_picker:
                this.selectFocus = true;
                fstCallMethod();
                hideChilds(this.rl);
                createDropColorImg("text");
                return;
            case R.id.dropcolor_picker1:
                createDropColorImg("sticker");
                return;

            case R.id.fnt:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttD, 0);
                    fontTag("DroidSans.ttf");
                    return;
                }
                setFontonSelected("DroidSans.ttf");
                return;
            case R.id.fnt1:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf1, 0);
                    fontTag("majalla.ttf");
                    return;
                }
                setFontonSelected("majalla.ttf");
                return;
            case R.id.fnt10:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf10, 0);
                    fontTag("Capture_it.ttf");
                    return;
                }
                setFontonSelected("Capture_it.ttf");
                return;
            case R.id.fnt11:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf11, 0);
                    fontTag("Advertising Script Bold Trial.ttf");
                    return;
                }
                setFontonSelected("Advertising Script Bold Trial.ttf");
                return;
            case R.id.fnt12:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf12, 0);
                    fontTag("Advertising Script Monoline Trial.ttf");
                    return;
                }
                setFontonSelected("Advertising Script Monoline Trial.ttf");
                return;
            case R.id.fnt13:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf13, 0);
                    fontTag("Beyond Wonderland.ttf");
                    return;
                }
                setFontonSelected("Beyond Wonderland.ttf");
                return;
            case R.id.fnt14:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf14, 0);
                    fontTag("CalliGravity.ttf");
                    return;
                }
                setFontonSelected("CalliGravity.ttf");
                return;
            case R.id.fnt15:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf15, 0);
                    fontTag("Cosmic Love.ttf");
                    return;
                }
                setFontonSelected("Cosmic Love.ttf");
                return;
            case R.id.fnt16:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf16, 0);
                    fontTag("lesser concern shadow.ttf");
                    return;
                }
                setFontonSelected("lesser concern shadow.ttf");
                return;
            case R.id.fnt17:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf17, 0);
                    fontTag("lesser concern.ttf");
                    return;
                }
                setFontonSelected("lesser concern.ttf");
                return;
            case R.id.fnt18:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf18, 0);
                    fontTag("Queen of Heaven.ttf");
                    return;
                }
                setFontonSelected("Queen of Heaven.ttf");
                return;
            case R.id.fnt19:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf19, 0);
                    fontTag("QUIGLEYW.TTF");
                    return;
                }
                setFontonSelected("QUIGLEYW.TTF");
                return;
            case R.id.fnt2:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf7, 0);
                    fontTag("Walkway_Bold.ttf");
                    return;
                }
                setFontonSelected("Walkway_Bold.ttf");
                return;
            case R.id.fnt20:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf20, 0);
                    fontTag("squealer embossed.ttf");
                    return;
                }
                setFontonSelected("squealer embossed.ttf");
                return;
            case R.id.fnt21:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf21, 0);
                    fontTag("squealer.ttf");
                    return;
                }
                setFontonSelected("squealer.ttf");
                return;
            case R.id.fnt22:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf22, 0);
                    fontTag("still time.ttf");
                    return;
                }
                setFontonSelected("still time.ttf");
                return;
            case R.id.fnt23:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf23, 0);
                    fontTag("Constantia Italic.ttf");
                    return;
                }
                setFontonSelected("Constantia Italic.ttf");
                return;
            case R.id.fnt24:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf24, 0);
                    fontTag("DejaVuSans_Bold.ttf");
                    return;
                }
                setFontonSelected("DejaVuSans_Bold.ttf");
                return;
            case R.id.fnt25:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf25, 0);
                    fontTag("Aladin_Regular.ttf");
                    return;
                }
                setFontonSelected("Aladin_Regular.ttf");
                return;
            case R.id.fnt26:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf26, 0);
                    fontTag("Adobe Caslon Pro Italic.ttf");
                    return;
                }
                setFontonSelected("Adobe Caslon Pro Italic.ttf");
                return;
            case R.id.fnt27:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf27, 0);
                    fontTag("aparaji.ttf");
                    return;
                }
                setFontonSelected("aparaji.ttf");
                return;
            case R.id.fnt28:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf28, 0);
                    fontTag("ARDECODE.ttf");
                    return;
                }
                setFontonSelected("ARDECODE.ttf");
                return;
            case R.id.fnt29:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf29, 0);
                    fontTag("ufonts_com_ck_scratchy_box.ttf");
                    return;
                }
                setFontonSelected("ufonts_com_ck_scratchy_box.ttf");
                return;
            case R.id.fnt3:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf3, 0);
                    fontTag("PortLligatSans-Regular.ttf");
                    return;
                }
                setFontonSelected("PortLligatSans-Regular.ttf");
                return;
            case R.id.fnt4:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf4, 0);
                    fontTag("ROD.TTF");
                    return;
                }
                setFontonSelected("ROD.TTF");
                return;
            case R.id.fnt5:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf5, 0);
                    fontTag("Aspergit.otf");
                    return;
                }
                setFontonSelected("Aspergit.otf");
                return;
            case R.id.fnt6:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf6, 0);
                    fontTag("windsong.ttf");
                    return;
                }
                setFontonSelected("windsong.ttf");
                return;
            case R.id.fnt7:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf2, 0);
                    fontTag("MVBOLI.TTF");
                    return;
                }
                setFontonSelected("MVBOLI.TTF");
                return;
            case R.id.fnt8:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf8, 0);
                    fontTag("Sofia-Regular.otf");
                    return;
                }
                setFontonSelected("Sofia-Regular.otf");
                return;
            case R.id.fnt9:
                this.selectFocus = true;
                fstCallMethod();
                if (this.modefontselection.equals("textfont")) {
                    this.textView.setTypeface(this.ttf9, 0);
                    fontTag("segoe.ttf");
                    return;
                }
                setFontonSelected("segoe.ttf");
                return;
            case R.id.la_color:
                this.selectFocus = true;
                this.top_option.setVisibility(8);
                this.la_color.setBackgroundColor(-1);
                this.la_fonts.setBackgroundResource(R.drawable.gradient);
                this.la_size.setBackgroundResource(R.drawable.gradient);
                this.la_shadow.setBackgroundResource(R.drawable.gradient);
                this.la_shader.setBackgroundResource(R.drawable.gradient);


                this.fonts.setBackgroundResource(R.drawable.fonts1);
                this.size.setBackgroundResource(R.drawable.size1);
                this.color_.setBackgroundResource(R.drawable.color);
                this.shadow.setBackgroundResource(R.drawable.shadow1);
                this.shader.setBackgroundResource(R.drawable.shader_1);

                ((TextView) findViewById(R.id.for_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.font_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.co_txt)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                ((TextView) findViewById(R.id.shw_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shdr_txt)).setTextColor(-1);
                this.font_rel.setVisibility(4);
                this.color_rel.setVisibility(0);
                this.shadow_rel.setVisibility(4);
                this.shader_rel.setVisibility(4);

                return;
            case R.id.la_fonts:
                this.modefont = "fonts";
                this.selectFocus = true;
                this.top_option.setVisibility(8);
                this.la_fonts.setBackgroundColor(-1);
                this.la_size.setBackgroundResource(R.drawable.gradient);
                this.la_color.setBackgroundResource(R.drawable.gradient);
                this.la_shadow.setBackgroundResource(R.drawable.gradient);
                this.la_shader.setBackgroundResource(R.drawable.gradient);


                this.fonts.setBackgroundResource(R.drawable.fonts);
                this.size.setBackgroundResource(R.drawable.size1);
                this.color_.setBackgroundResource(R.drawable.color_1);
                this.shadow.setBackgroundResource(R.drawable.shadow1);
                this.shader.setBackgroundResource(R.drawable.shader_1);

                ((TextView) findViewById(R.id.for_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.font_txt)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                ((TextView) findViewById(R.id.co_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shw_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shdr_txt)).setTextColor(-1);
                this.font_rel.setVisibility(0);
                this.color_rel.setVisibility(4);
                this.shadow_rel.setVisibility(4);
                this.shader_rel.setVisibility(4);

                return;
            case R.id.la_shader:
                addNormalTextTemplateMethod("Add Your Text Here");
                this.selectFocus = true;
                this.top_option.setVisibility(View.INVISIBLE);
                this.la_shader.setBackgroundColor(-1);
                this.la_fonts.setBackgroundResource(R.drawable.gradient);
                this.la_color.setBackgroundResource(R.drawable.gradient);
                this.la_shadow.setBackgroundResource(R.drawable.gradient);
                this.la_size.setBackgroundResource(R.drawable.gradient);


                this.fonts.setBackgroundResource(R.drawable.fonts1);
                this.size.setBackgroundResource(R.drawable.size1);
                this.color_.setBackgroundResource(R.drawable.color_1);
                this.shadow.setBackgroundResource(R.drawable.shadow1);
                this.shader.setBackgroundResource(R.drawable.shader);

                ((TextView) findViewById(R.id.for_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.font_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.co_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shw_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shdr_txt)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.font_rel.setVisibility(View.INVISIBLE);
                this.color_rel.setVisibility(View.INVISIBLE);
                this.shadow_rel.setVisibility(View.INVISIBLE);
                this.shader_rel.setVisibility(View.VISIBLE);

                return;
            case R.id.la_shadow:
                this.selectFocus = true;
                this.top_option.setVisibility(8);
                this.la_shadow.setBackgroundColor(-1);
                this.la_fonts.setBackgroundResource(R.drawable.gradient);
                this.la_color.setBackgroundResource(R.drawable.gradient);
                this.la_size.setBackgroundResource(R.drawable.gradient);
                this.la_shader.setBackgroundResource(R.drawable.gradient);


                this.fonts.setBackgroundResource(R.drawable.fonts1);
                this.size.setBackgroundResource(R.drawable.size1);
                this.color_.setBackgroundResource(R.drawable.color_1);
                this.shadow.setBackgroundResource(R.drawable.shadow);
                this.shader.setBackgroundResource(R.drawable.shader_1);

                ((TextView) findViewById(R.id.for_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.font_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.co_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shw_txt)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                ((TextView) findViewById(R.id.shdr_txt)).setTextColor(-1);
                this.font_rel.setVisibility(4);
                this.color_rel.setVisibility(4);
                this.shadow_rel.setVisibility(0);
                this.shader_rel.setVisibility(4);

                if (this.modeshadowselection.equals("textshadow")) {
                    this.shadow_seekbar.setProgress((int) (this.textView.getShadowRadius() * 2.0f));
                    return;
                } else if (this.arrayfortv.size() > 0) {
                    this.shadow_seekbar.setProgress((int) (this.arrayfortv.get(0).getText_shadowradius() * 2.0f));
                    return;
                } else {
                    return;
                }
            case R.id.la_size:
                this.modefont = "formatting";
                this.selectFocus = true;
                this.top_option.setVisibility(0);
                this.la_size.setBackgroundColor(-1);
                this.la_fonts.setBackgroundResource(R.drawable.gradient);
                this.la_color.setBackgroundResource(R.drawable.gradient);
                this.la_shadow.setBackgroundResource(R.drawable.gradient);
                this.la_shader.setBackgroundResource(R.drawable.gradient);


                this.fonts.setBackgroundResource(R.drawable.fonts1);
                this.size.setBackgroundResource(R.drawable.size);
                this.color_.setBackgroundResource(R.drawable.color_1);
                this.shadow.setBackgroundResource(R.drawable.shadow1);
                this.shader.setBackgroundResource(R.drawable.shader_1);

                ((TextView) findViewById(R.id.for_txt)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                ((TextView) findViewById(R.id.font_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.co_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shw_txt)).setTextColor(-1);
                ((TextView) findViewById(R.id.shdr_txt)).setTextColor(-1);
                this.font_rel.setVisibility(4);
                this.color_rel.setVisibility(4);
                this.shadow_rel.setVisibility(4);
                this.shader_rel.setVisibility(4);

                checkboldItalic();
                return;
            case R.id.re_addnewText:
                removeImageViewControll();
                addNormalTextTemplateMethod("Hello");
              /*  this.sb_effectsfilter.setVisibility(8);
                this.sb_opctyfilter.setVisibility(8);
                this.blur_lay_cntrl.setVisibility(8);

               // this.scroll_of_all_effects.setVisibility(8);
                this.re_sticker.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
             //   this.rel_effects.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
              //  if (this.scroll_of_all_backgrounds.getVisibility() != 0) {
                    this.picture_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    this.sticker_txt.setTextColor(-1);
                    this.effect_txt.setTextColor(-1);
                    this.re_addnewText.setBackgroundColor(-1);
               //     this.footer_effects_image.setBackgroundResource(R.drawable.effects_image1);
                    this.list_of_brnd.setBackgroundResource(R.drawable.backrunds);
                    this.list_of_sticker.setBackgroundResource(R.drawable.stickerr1);
                    this.scroll_all.setVisibility(8);
                //    this.scroll_of_all_backgrounds.setVisibility(0);
                    return;*/
               // }
                setDefault();
                return;
            case R.id.re_sticker:
              // this.sb_effectsfilter.setVisibility(8);



                this.controll_btn_stckr.setVisibility(8);
                this.controller_sticker.setVisibility(8);


                setDefault();
                removeImageViewControll();
                int position = getStickerPositionFromCategory(this.categorySticker);
                Intent intentSticker = new Intent(this, StickerList.class);
                intentSticker.putExtra("position", position);
                startActivityForResult(intentSticker, GET_STICKER);
                return;

            case R.id.rel_effects:
                removeImageViewControll();






                return;
            case R.id.tabShadowtxt:
                this.tab_shadow_punch.setBackgroundResource(R.drawable.gradient);
                this.tab_shadow_txt.setBackgroundColor(-1);
                this.modeshadowselection = "textshadow";
                this.shadow_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.shadow_punch.setTextColor(-1);
                this.shadow_seekbar.setProgress((int) (this.textView.getShadowRadius() * 2.0f));
                return;
            case R.id.tabfontpunch:
                this.tab_font_punch.setBackgroundColor(-1);
                this.tab_font_txt.setBackgroundResource(R.drawable.gradient);
                this.modefontselection = "punchfont";
                this.font_txt.setTextColor(-1);
                this.font_punch.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                return;
            case R.id.tabfonttxt:
                this.tab_font_punch.setBackgroundResource(R.drawable.gradient);
                this.tab_font_txt.setBackgroundColor(-1);
                this.modefontselection = "textfont";
                this.font_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.font_punch.setTextColor(-1);
                return;
            case R.id.tabformatpunch:
                this.tab_format_punch.setBackgroundColor(-1);
                this.tab_format_txt.setBackgroundResource(R.drawable.gradient);
                this.modeformatselection = "punchformat";
                this.format_txt.setTextColor(-1);
                this.format_punch.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                checkboldItalic();
                return;
            case R.id.tabformattxt:
                this.tab_format_punch.setBackgroundResource(R.drawable.gradient);
                this.tab_format_txt.setBackgroundColor(-1);
                this.modeformatselection = "textformat";
                this.format_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.format_punch.setTextColor(-1);
                checkboldItalic();
                return;
            case R.id.tabpunchrel:
                this.tab_punch.setBackgroundColor(-1);
                this.tab_text.setBackgroundResource(R.drawable.gradient);
                this.modeClrSelection = "punchclr";
                this.color_punch.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.color_txt.setTextColor(-1);
                return;
            case R.id.tabshaderpunch:
                this.tab_shader_punch.setBackgroundColor(-1);
                this.tab_shader_txt.setBackgroundResource(R.drawable.gradient);
                this.modeshaderselection = "punchshader";
                this.shader_txt.setTextColor(-1);
                this.shader_punch.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                return;
            case R.id.tabshadertxt:
                this.tab_shader_punch.setBackgroundResource(R.drawable.gradient);
                this.tab_shader_txt.setBackgroundColor(-1);
                this.modeshaderselection = "textshader";
                this.shader_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.shader_punch.setTextColor(-1);
                return;
            case R.id.tabshadowpunch:
                this.tab_shadow_punch.setBackgroundColor(-1);
                this.tab_shadow_txt.setBackgroundResource(R.drawable.gradient);
                this.modeshadowselection = "punchshadow";
                this.shadow_punch.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.shadow_txt.setTextColor(-1);
                if (this.arrayfortv.size() > 0) {
                    this.shadow_seekbar.setProgress((int) (this.arrayfortv.get(0).getText_shadowradius() * 2.0f));
                    return;
                }
                return;
            case R.id.tabtxtrel:
                this.tab_punch.setBackgroundResource(R.drawable.gradient);
                this.tab_text.setBackgroundColor(-1);
                this.modeClrSelection = "textclr";
                this.color_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.color_punch.setTextColor(-1);
                return;
            default:
                return;
        }
    }

    private int getStickerPositionFromCategory(String category) {
        String[] TITLES = getResources().getStringArray(R.array.sticker_category);
        for (int i = 0; i < TITLES.length; i++) {
            if (category.equals(TITLES[i])) {
                return i;
            }
        }
        return 0;
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void checkboldItalic() {
        this.bold.setBackgroundResource(R.drawable.bold);
        this.italic.setBackgroundResource(R.drawable.italic);
        this.underline.setBackgroundResource(R.drawable.underline);
        this.strike.setBackgroundResource(R.drawable.text);
        if (this.textView.getGravity() == 17) {
            this.g1.setBackgroundResource(R.drawable.left_align_text);
            this.g2.setBackgroundResource(R.drawable.center_align_text1);
            this.g3.setBackgroundResource(R.drawable.right_align_text);
        } else if (this.textView.getGravity() == 49) {
            this.g1.setBackgroundResource(R.drawable.left_align_text);
            this.g2.setBackgroundResource(R.drawable.center_align_text1);
            this.g3.setBackgroundResource(R.drawable.right_align_text);
        } else if (this.textView.getGravity() == 51) {
            this.g1.setBackgroundResource(R.drawable.left_align_text1);
            this.g2.setBackgroundResource(R.drawable.center_align_text);
            this.g3.setBackgroundResource(R.drawable.right_align_text);
        } else if (this.textView.getGravity() == 53) {
            this.g1.setBackgroundResource(R.drawable.left_align_text);
            this.g2.setBackgroundResource(R.drawable.center_align_text);
            this.g3.setBackgroundResource(R.drawable.right_align_text1);
        }
        if (this.modeformatselection.equals("textformat")) {
            if (this.textView.getTypeface().getStyle() == 1) {
                this.bold.setBackgroundResource(R.drawable.bold1);
            }
            if (this.textView.getTypeface().getStyle() == 3) {
                this.bold.setBackgroundResource(R.drawable.bold1);
                this.italic.setBackgroundResource(R.drawable.italic1);
            }
            if (this.textView.getPaintFlags() == 1289 || this.textView.getPaintFlags() == 1305) {
                this.underline.setBackgroundResource(R.drawable.underline1);
            }
            if (this.textView.getPaintFlags() == 1297 || this.textView.getPaintFlags() == 1305) {
                this.strike.setBackgroundResource(R.drawable.text1);
            }
        } else if (this.arrayfortv.size() > 0) {
            if (this.arrayfortv.get(0).isText_bold()) {
                this.bold.setBackgroundResource(R.drawable.bold1);
            }
            if (this.arrayfortv.get(0).isText_italic()) {
                this.italic.setBackgroundResource(R.drawable.italic1);
            }
            if (this.arrayfortv.get(0).isText_underline()) {
                this.underline.setBackgroundResource(R.drawable.underline1);
            }
            if (this.arrayfortv.get(0).isText_strike()) {
                this.strike.setBackgroundResource(R.drawable.text1);
            }
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void setDefault() {
        this.picture_txt.setTextColor(-1);
        this.sticker_txt.setTextColor(-1);
        this.effect_txt.setTextColor(-1);
        this.list_of_brnd.setBackgroundResource(R.drawable.backrund1);
        this.list_of_sticker.setBackgroundResource(R.drawable.stickerr1);


        this.re_addnewText.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.re_sticker.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);

    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri;
        super.onActivityResult(requestCode, resultCode, data);
        this.selectFocus = true;
        if (resultCode == -1) {
            if (data != null || requestCode == GET_STICKER) {

              /*  if (requestCode == SEL_BACKGROUND_FROM_GALLERY && (selectedImageUri = data.getData()) != null) {
                    Intent _main = new Intent(this, CropActivityTwo.class);
                    _main.putExtra("value", "image");
                    _main.setData(selectedImageUri);
                    startActivity(_main);
                }*/

                if (requestCode == EDIT_QUOTE_RESULT) {
                    checkandShowEditRel();
                    this.selectFocus = true;
                    this.storedArray = new ArrayList<>();
                    String quoted_txt = data.getStringExtra("quote_edit");
                    hideChilds(this.rl);
                    if (this.textView.getTag().equals("text_tv")) {
                        EditText editText = this.text_tv;
                        editText.setText("" + quoted_txt);
                        this.storedArray = new ArrayList<>(this.arrayfortv);
                        this.arrayfortv.clear();
                        setChangPunch(this.arrayfortv, quoted_txt, 4, 1);
                        updateTextSizeonScale(this.rl.getWidth(), this.rl.getHeight());
                        visibleBorder(this.rl);
                    }
                }

                if (requestCode == WRITE_QUOTE_RESULT) {
                    String stringExtra = data.getStringExtra("quote_edit");
                    String stringExtra2 = data.getStringExtra("hasAuthor");
                    this.categoryQuote = data.getStringExtra("categoryQuote");


                 //   this.sb_effectsfilter.setVisibility(0);




                }
                if (requestCode == GET_STICKER) {
                    String mDrawableName2 = data.getStringExtra("sticker");
                    String stickerType = data.getStringExtra("stickerType");
                    this.categorySticker = data.getStringExtra("stickerCategory");
                    Log.e("TAG", "onActivityResult: " + mDrawableName2 + "/////" + stickerType + "....."+ categorySticker);
                    addSticker(mDrawableName2, stickerType);
                    return;
                }
                return;
            }
            new AlertDialog.Builder(this, 16974126).setMessage(ImageUtils.getSpannableString(this, Typeface.DEFAULT, R.string.picUpImg)).setPositiveButton(ImageUtils.getSpannableString(this, Typeface.DEFAULT, R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
        }
    }

    @SuppressLint("WrongConstant")
    private void addSticker(String resId, String stickerType) {
        String str = resId;
        String str2 = stickerType;
        if (str2.equals("COLOR")) {
            this.lay_colorfilter.setVisibility(0);
            this.lay_hue.setVisibility(8);
            this.colorPicker.setSelectedColor(Color.parseColor("#ffffff"));
        } else {
            this.lay_colorfilter.setVisibility(8);
            this.lay_hue.setVisibility(0);
            this.hue_seekbar.setProgress(1);
        }
        this.transparency_seekbar.setProgress(255);
        int stckrwidth = Constants.dpToPx(this, 140);
        ComponentInfo ci = new ComponentInfo();
        ci.setPOS_X((float) ((bitmapOriginal.getWidth() / 2) - (stckrwidth / 2)));
        ci.setPOS_Y((float) ((bitmapOriginal.getHeight() / 2) - (Constants.dpToPx(this, 140) / 2)));
        ci.setWIDTH(stckrwidth);
        ci.setHEIGHT(stckrwidth);
        ci.setROTATION(0.0f);
        Log.e("resId", "" + str);
        Log.e("stickerType", "" + str2);
        ci.setRES_ID(str);
        ci.setTYPE(str2);
        ci.setHUE(1);
        ci.setOPACITY(255);
        ResizableImageview riv = new ResizableImageview(this);
        riv.setComponentInfo(ci);
        this.fortext.addView(riv);
        riv.setMainLayoutWH((float) this.fortext.getWidth(), (float) this.fortext.getHeight());
        riv.setOnTouchListener(new MultiTouchListener().enableRotation(true).setOnTouchCallbackListener(this));
        riv.setOnTouchCallbackListener(this);
        riv.setBorderVisibility(true);
     //   this.sb_effectsfilter.setVisibility(4);



        if (this.isStickerLayVisible) {
            this.controll_btn_stckr.setVisibility(0);
            this.controller_sticker.setVisibility(0);
            this.controll_btn_stckr.setBackgroundResource(R.drawable.slide_down);
        } else {
            this.controll_btn_stckr.setVisibility(0);
            this.controller_sticker.setVisibility(8);
            this.controll_btn_stckr.setBackgroundResource(R.drawable.slide_up);
        }
        if (str2.equals("COLOR")) {
            this.lay_colorfilter.setVisibility(0);
            this.lay_hue.setVisibility(8);
            this.colorPicker.setSelectedColor(Color.parseColor("#ffffff"));
        } else {
            this.lay_colorfilter.setVisibility(8);
            this.lay_hue.setVisibility(0);
            this.hue_seekbar.setProgress(ci.getHUE());
        }
        this.transparency_seekbar.setProgress(ci.getOPACITY());
    }

    @SuppressLint("WrongConstant")
    public void removeImageViewControll() {
        int childCount = this.fortext.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.fortext.getChildAt(i);
            if (view instanceof ResizableImageview) {
                ((ResizableImageview) view).setBorderVisibility(false);
            }
        }
        if (this.controller_sticker.getVisibility() == 0) {
            this.controll_btn_stckr.setVisibility(8);
            this.controller_sticker.setVisibility(8);
        }
    }

    private void fontTag(String fontt) {
        if (this.textView.getTag().equals("text_tv")) {
            this.ft1 = fontt;
        }
        int normaltextSize = (int) this.textView.getTextSize();
        if (this.arrayfortv.size() > 0) {
            int comapreSizes1 = comapreSizes1(normaltextSize, (int) (((float) this.arrayfortv.get(0).getText_size()) * getResources().getDisplayMetrics().density), this.arrayfortv) - normaltextSize;
            this.textSizeOffset = comapreSizes1;
            if (comapreSizes1 < 0) {
                this.textSizeOffset = 5;
            }
        }
        updateTextSizeonScale(this.rl.getWidth(), this.rl.getHeight());
    }

    /* access modifiers changed from: private */
    public void shaderTag(String shaderr) {
        if (this.textView.getTag().equals("text_tv")) {
            this.shr1 = shaderr;
        }
    }


    public Bitmap mergeBitmap(Bitmap orig, Bitmap ontop) {
        float x;
        Bitmap bit = Bitmap.createBitmap(ontop.getWidth(), ontop.getHeight(), ontop.getConfig());
        if (orig.getWidth() < ontop.getWidth()) {
            x = (float) ((ontop.getWidth() - orig.getWidth()) / 2);
        } else {
            x = 0.0f;
        }
        Canvas canvas = new Canvas(bit);
        canvas.drawBitmap(orig, x, 0.0f, (Paint) null);
        canvas.drawBitmap(ontop, 0.0f, 0.0f, (Paint) null);
        return bit;
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void onResume() {
        super.onResume();

    }

    @SuppressLint("WrongConstant")
    public void onBackPressed() {
        if (this.alledit_ll.getVisibility() == 0) {
            this.alledit_ll.setVisibility(8);
          //  this.sb_effectsfilter.setVisibility(0);


            this.menu_ll.setVisibility(0);
            EditText editText = this.textView;
            if (editText != null) {
                Selection.removeSelection(editText.getText());
                hideChilds(this.rl);
                return;
            }
            return;
        }
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pageexit_dialog);
        ((Button) dialog.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddTextQuotesActivity.this.selectFocus = true;
                dialog.dismiss();
                AddTextQuotesActivity.this.finish();
            }
        });
        dialog.show();
    }

    @SuppressLint("WrongConstant")
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.hue_seekBar:
                int childCount1 = this.fortext.getChildCount();
                for (int i = 0; i < childCount1; i++) {
                    View view1 = this.fortext.getChildAt(i);
                    if ((view1 instanceof ResizableImageview) && ((ResizableImageview) view1).getBorderVisbilty()) {
                        ((ResizableImageview) view1).setHueProg(progress);
                    }
                }
                return;
           /* case R.id.sb_effects:
                GPUImageFilterTools.FilterAdjuster filterAdjuster = this.mFilterAdjuster;
                if (filterAdjuster != null) {
                    filterAdjuster.adjust(progress);
                }
                this.gpuImageview.requestRender();
                return;*/
         /*   case R.id.sb_effectsfilter:

                GPUImageFilter filter1 = GPUImageFilterTools.createFilterForType(this, this.filterType);
                GPUImageFilter filter2 = GPUImageFilterTools.createFilterForType(this, GPUImageFilterTools.FilterType.GAUSSIAN_BLUR);

                new GPUImageFilterTools.FilterAdjuster(filter2).adjust(progress);
                GPUImageFilterGroup group = new GPUImageFilterGroup();
                group.addFilter(filter1);
                group.addFilter(filter2);
                this.gpuImageview.setFilter(group);
                this.gpuImageview.requestRender();
                return;
            case R.id.sb_opctyfilter:
                try {
                    this.img_opacity.setImageAlpha(progress);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }*/
            case R.id.trans_seekBar:
                int childCount2 = this.fortext.getChildCount();
                for (int i2 = 0; i2 < childCount2; i2++) {
                    View view12 = this.fortext.getChildAt(i2);
                    if ((view12 instanceof ResizableImageview) && ((ResizableImageview) view12).getBorderVisbilty()) {
                        ((ResizableImageview) view12).settransparency(progress);
                    }
                }
                return;
            default:
                return;
        }
    }

    @SuppressLint("WrongConstant")
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
//            case R.id.sb_effectsfilter:
//
//                this.selectFocus = true;
//                return;
            default:
                return;
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @SuppressLint("WrongConstant")
    private void mainAddMethod() {
        this.min = 0;
        this.max = this.textView.getText().length();
        if (this.textView.isFocused()) {
            this.a = this.textView.getSelectionStart();
            this.b = this.textView.getSelectionEnd();
            int selStart = this.textView.getSelectionStart();
            int selEnd = this.textView.getSelectionEnd();
            this.min = Math.max(0, Math.min(selStart, selEnd));
            int max2 = Math.max(0, Math.max(selStart, selEnd));
            this.max = max2;
            if (this.min != max2) {
                SelectedTextData std = new SelectedTextData();
                std.setStart(this.min);
                std.setEnd(this.max);
                std.setText_size((int) (this.textView.getTextSize() / getResources().getDisplayMetrics().density));
                std.setText_color(this.textView.getCurrentTextColor());
                String fft = "";
                String shdrt = "";
                if (this.textView.getTag().equals("text_tv")) {
                    fft = this.ft1;
                }
                if (this.textView.getTag().equals("text_tv")) {
                    shdrt = this.shr1;
                }
                std.setText_ttf(fft);
                std.setText_shadowdx(this.textView.getShadowDx());
                std.setText_shadowdy(this.textView.getShadowDy());
                std.setText_shadowradius(this.textView.getShadowRadius());
                std.setText_shadowcolor(this.textView.getShadowColor());
                std.setText_shader(shdrt);
                std.setText_bold(false);
                std.setText_italic(false);
                std.setText_underline(false);
                std.setText_strike(false);
                if (this.textView.getTag().equals("text_tv")) {
                    boolean datapresent = false;
                    if (this.arrayfortv.size() > 0) {
                        for (int i = 0; i < this.arrayfortv.size(); i++) {
                            if (this.arrayfortv.get(i).getStart() == this.min && this.arrayfortv.get(i).getEnd() == this.max) {
                                datapresent = true;
                            }
                        }
                    }
                    if (!datapresent) {
                        this.arrayfortv.add(std);
                    }
                }
            }
            if (this.alledit_ll.getVisibility() != 0) {
                this.menu_ll.setVisibility(8);
                this.alledit_ll.setVisibility(0);
              //  this.sb_effectsfilter.setVisibility(8);





            }
            defaultsetup();
        }
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("WrongConstant")
    private void mainAddMethod1() {
        this.min = 0;
        this.max = this.textView.getText().length();
        if (this.textView.isFocused()) {
            this.a = this.textView.getSelectionStart();
            this.b = this.textView.getSelectionEnd();
            int selStart = this.textView.getSelectionStart();
            int selEnd = this.textView.getSelectionEnd();
            this.min = Math.max(0, Math.min(selStart, selEnd));
            this.max = Math.max(0, Math.max(selStart, selEnd));
            SelectedTextData std = new SelectedTextData();
            std.setStart(this.min);
            std.setEnd(this.max);
            std.setText_size((int) ((this.textView.getTextSize() + 5.0f) / getResources().getDisplayMetrics().density));
            std.setText_color(ViewCompat.MEASURED_STATE_MASK);
            String fft = "";
            String shdrt = "";
            if (this.textView.getTag().equals("text_tv")) {
                fft = this.ft1;
            }
            if (this.textView.getTag().equals("text_tv")) {
                shdrt = this.shr1;
            }
            std.setText_ttf(fft);
            std.setText_shadowdx(this.textView.getShadowDx());
            std.setText_shadowdy(this.textView.getShadowDy());
            std.setText_shadowradius(this.textView.getShadowRadius());
            std.setText_shadowcolor(this.textView.getShadowColor());
            std.setText_shader(shdrt);
            std.setText_bold(false);
            std.setText_italic(false);
            std.setText_underline(false);
            std.setText_strike(false);
            if (this.textView.getTag().equals("text_tv")) {
                boolean datapresent = false;
                if (this.arrayfortv.size() > 0) {
                    for (int i = 0; i < this.arrayfortv.size(); i++) {
                        if (this.arrayfortv.get(i).getStart() == this.min && this.arrayfortv.get(i).getEnd() == this.max) {
                            datapresent = true;
                        }
                    }
                }
                if (!datapresent) {
                    this.arrayfortv.add(std);
                }
            }
            if (this.alledit_ll.getVisibility() != 0) {
                this.menu_ll.setVisibility(8);
                this.alledit_ll.setVisibility(0);
            //   this.sb_effectsfilter.setVisibility(8);





            }
            defaultsetup();
        }
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("WrongConstant")
    public void ontemplate(int position) {
        Log.e("templte_pos", "" + position);
        this.temp_postn = position;
        this.selecttemplate = false;
        if (this.rl.getVisibility() == 0) {
            UpdateAddTextMethod();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_edit), 0).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        freeMemory();
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            this.back_arrow_add_quotes = null;
            this.f = null;
            this.params_rl = null;
            this.done_add_quotes = null;
            this.db = null;
            Bitmap bitmap2 = this.bitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.bitmap = null;
            }
            Bitmap bitmap3 = this.bitRel;
            if (bitmap3 != null) {
                bitmap3.recycle();
                this.bitRel = null;
            }
            Bitmap bitmap4 = this.bb;
            if (bitmap4 != null) {
                bitmap4.recycle();
                this.bb = null;
            }
            Bitmap bitmap5 = bitmapOriginal;
            if (bitmap5 != null) {
                bitmap5.recycle();
                bitmapOriginal = null;
            }
            this.filename = null;
            this.complete_img = null;
            c = null;
            this.gpuImageview = null;
            this.top_option = null;
            this.re_addnewText = null;
            this.re_sticker = null;


            this.tab_text = null;
            this.tab_punch = null;
            this.tab_shadow_txt = null;
            this.tab_shadow_punch = null;
            this.tab_shader_txt = null;
            this.tab_shader_punch = null;
            this.tab_font_txt = null;
            this.tab_font_punch = null;
            this.tab_format_txt = null;
            this.tab_format_punch = null;
            this.lay_hue = null;
            this.lay_colorfilter = null;
            this.cntrls_stkr_lay = null;
            this.cntrls_stkrclr_lay = null;
            this.tab_cntrl_stkr = null;
            this.tab_clrs_stkr = null;
            this.fortext = null;
            this.rl = null;
            this.alledit_ll = null;
            this.text_tv = null;
            this.scale_iv = null;
            this.move_iv = null;
            this.rotate_iv = null;
            this.delete_iv = null;
            this.controll_btn_stckr = null;
            this.edit_ivTxt = null;
            this.controller_sticker = null;
            this.btn_left = null;
            this.btn_right = null;
            this.btn_top = null;
            this.btn_down = null;
            this.duplicate = null;

            this.list_of_brnd = null;
            this.size = null;
            this.list_of_sticker = null;

            this.fonts = null;
            this.color_ = null;
            this.shadow = null;
            this.img_opacity = null;
            this.shader = null;

            this.menu_ll = null;
            this.formatall_type = null;


            this.mFilterAdjuster = null;
            this.la_size = null;
            this.la_fonts = null;
            this.la_color = null;
            this.la_shadow = null;
            this.la_shader = null;

            this.font_rel = null;
            this.color_rel = null;
            this.shadow_rel = null;
            this.shader_rel = null;

            this.temp_postn = 1;
            this.g1 = null;
            this.g2 = null;
            this.g3 = null;
            this.sd_color = null;
            this.sh = null;
            this.sh1 = null;
            this.sh2 = null;
            this.sh3 = null;
            this.sh4 = null;
            this.sh5 = null;
            this.sh6 = null;
            this.sh7 = null;
            this.sh8 = null;
            this.sh9 = null;
            this.sh10 = null;
            this.bold = null;
            this.italic = null;
            this.underline = null;
            this.strike = null;
            this.textView = null;

          //  this.sb_effectsfilter = null;

            this.hue_seekbar = null;
            this.transparency_seekbar = null;
            this.shadow_seekbar = null;
            this.colorPicker = null;
            this.arrayfortv = null;
            this.storedArray = null;
            this.mFilter = null;
            this.ttD = null;
            this.ttf1 = null;
            this.ttf2 = null;
            this.ttf3 = null;
            this.ttf4 = null;
            this.ttf5 = null;
            this.ttf6 = null;
            this.ttf7 = null;
            this.ttf8 = null;
            this.ttf9 = null;
            this.ttf10 = null;
            this.ttf11 = null;
            this.ttf12 = null;
            this.ttf13 = null;
            this.ttf14 = null;
            this.ttf15 = null;
            this.ttf16 = null;
            this.ttf17 = null;
            this.ttf18 = null;
            this.ttf19 = null;
            this.ttf20 = null;
            this.ttf21 = null;
            this.ttf22 = null;
            this.ttf23 = null;
            this.ttf24 = null;
            this.ttf25 = null;
            this.ttf26 = null;
            this.ttf27 = null;
            this.ttf28 = null;
            this.ttf29 = null;
            this.gd = null;


            this.dbHelper = null;
            this.spannableString = null;
            this.builder = null;
            this.preferences = null;
            this.filterType = null;
            this.img_format_txt = null;
            this.img_format_punch = null;
            this.img_font_txt = null;
            this.img_font_punch = null;
            this.img_shadow_txt = null;
            this.img_shadow_punch = null;
            this.img_shader_txt = null;
            this.img_shader_punch = null;
            this.img_color_txt = null;
            this.img_color_punch = null;
            this.format_txt = null;
            this.format_punch = null;
            this.font_txt = null;
            this.font_punch = null;
            this.shadow_txt = null;
            this.shadow_punch = null;
            this.shader_txt = null;
            this.shader_punch = null;
            this.color_txt = null;
            this.color_punch = null;
            this.contrl_txt = null;
            this.clr_opacity_txt = null;
            this.picture_txt = null;
            this.sticker_txt = null;
            this.effect_txt = null;
            this.pallete = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        freeMemory();
    }

    private void freeMemory() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Glide.get(AddTextQuotesActivity.this).clearDiskCache();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Glide.get(this).clearMemory();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Constants.freeMemory();
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void setImageInBackgrounds(Bitmap selectBitmap) {
        this.complete_img.getLayoutParams().height = selectBitmap.getHeight();
        this.complete_img.getLayoutParams().width = selectBitmap.getWidth();
        this.complete_img.postInvalidate();
        this.complete_img.requestLayout();
        bitmapOriginal = selectBitmap;
        this.gpuImageview.setImage(selectBitmap);
        this.gpuImageview.setVisibility(0);
    }

    private void saveBitmap(final boolean inPNG) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.plzwait));
        pd.setCancelable(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                String photoFile;
                try {
                    File pictureFileDir = new File(APPUtility.getAppDir(), AddTextQuotesActivity.this.getResources().getString(R.string.app_name));
                    if (!pictureFileDir.exists()) {
                        if (!pictureFileDir.mkdirs()) {
                            Log.d("", "Can't create directory to save image.");
                            Toast.makeText(AddTextQuotesActivity.this.getApplicationContext(), AddTextQuotesActivity.this.getResources().getString(R.string.create_dir_err), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    String photoFile2 = "text_On_photo" + System.currentTimeMillis();
                    if (inPNG) {
                        photoFile = photoFile2 + ".png";
                    } else {
                        photoFile = photoFile2 + ".jpg";
                    }
                    AddTextQuotesActivity.this.filename = pictureFileDir.getPath() + File.separator + photoFile;
                    File pictureFile = new File(AddTextQuotesActivity.this.filename);
                    try {
                        if (!pictureFile.exists()) {
                            pictureFile.createNewFile();
                        }
                        FileOutputStream ostream = new FileOutputStream(pictureFile);
                        if (inPNG) {
                            AddTextQuotesActivity.this.bitRel.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                        } else {
                            Bitmap newBitmap = Bitmap.createBitmap(AddTextQuotesActivity.this.bitRel.getWidth(), AddTextQuotesActivity.this.bitRel.getHeight(), AddTextQuotesActivity.this.bitRel.getConfig());
                            Canvas canvas = new Canvas(newBitmap);
                            canvas.drawColor(-1);
                            canvas.drawBitmap(AddTextQuotesActivity.this.bitRel, 0.0f, 0.0f, (Paint) null);
                            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            newBitmap.recycle();
                        }
                        ostream.flush();
                        ostream.close();
                        AddTextQuotesActivity.this.isUpadted = true;
                        AddTextQuotesActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(pictureFile)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(1000);
                    pd.dismiss();
                } catch (Exception e2) {
                }
            }
        }).start();
        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (AddTextQuotesActivity.this.isUpadted) {
                    Intent intent = new Intent(AddTextQuotesActivity.this, SaveActivity.class);
                    intent.putExtra("uri", AddTextQuotesActivity.this.filename);
                    intent.putExtra("way", "AddQuote");
                    AddTextQuotesActivity.this.startActivity(intent);
                    AddTextQuotesActivity.this.preferences.getBoolean("isAdsDisabled", false);
                }
            }
        });
    }

    public void onColor(int position, String type) {
        if (position != 0) {
            if (type.equals("text")) {
                visibleBorder(this.rl);
                if (this.modeClrSelection.equals("textclr")) {
                    this.shr1 = "null";
                    this.textView.getPaint().setShader((Shader) null);
                    this.textView.postInvalidate();
                    this.textView.requestLayout();
                    this.textView.setTextColor(position);
                    this.existingcolor = position;
                } else {
                    setShaderonSelected("null");
                    setColoronSelected(position);
                    this.existingcolor = position;
                }
                updateColors();
                return;
            }
            this.color = position;
            int childCount1 = this.fortext.getChildCount();
            for (int i = 0; i < childCount1; i++) {
                View view1 = this.fortext.getChildAt(i);
                if ((view1 instanceof ResizableImageview) && ((ResizableImageview) view1).getBorderVisbilty()) {
                    ((ResizableImageview) view1).setColorFilter(this.color);
                }
            }
        } else if (type.equals("text")) {
            visibleBorder(this.rl);
        }
    }

    @SuppressLint("WrongConstant")
    public void onTouchCallback(View v) {
        removeImageViewControll();
        this.effect_txt.setTextColor(-1);
        this.sticker_txt.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.re_sticker.setBackgroundColor(-1);

        this.list_of_brnd.setBackgroundResource(R.drawable.backrund1);
        this.list_of_sticker.setBackgroundResource(R.drawable.stickers);
        EditText editText = this.textView;
        if (editText != null) {
            int noch = ((ViewGroup) editText.getParent()).getChildCount();
            for (int i = 1; i < noch; i++) {
                ((ViewGroup) this.textView.getParent()).getChildAt(i).setVisibility(4);
            }
        }
        this.controll_btn_stckr.setVisibility(8);
        this.controller_sticker.setVisibility(8);
        if (this.alledit_ll.getVisibility() == 0) {
            this.menu_ll.setVisibility(0);
            this.alledit_ll.setVisibility(8);
         //   this.sb_effectsfilter.setVisibility(0);


        }

    }

    @SuppressLint("WrongConstant")
    public void onTouchUpCallback(View v) {
        //this.sb_effectsfilter.setVisibility(4);


        if (this.isStickerLayVisible) {
            this.controll_btn_stckr.setVisibility(0);
            this.controller_sticker.setVisibility(0);
            this.controll_btn_stckr.setBackgroundResource(R.drawable.slide_down);
        } else {
            this.controll_btn_stckr.setVisibility(0);
            this.controller_sticker.setVisibility(8);
            this.controll_btn_stckr.setBackgroundResource(R.drawable.slide_up);
        }
        ComponentInfo ci = ((ResizableImageview) v).getComponentInfo();
        String type = ci.getTYPE();
        int alphaProg = ci.getOPACITY();
        if (type.equals("COLOR")) {
            this.lay_colorfilter.setVisibility(0);
            this.lay_hue.setVisibility(8);
            this.colorPicker.setSelectedColor(Color.parseColor("#ffffff"));
        } else {
            this.lay_colorfilter.setVisibility(8);
            this.lay_hue.setVisibility(0);
            this.hue_seekbar.setProgress(ci.getHUE());
        }
        this.transparency_seekbar.setProgress(alphaProg);
    }

    public void onTouchMoveCallback(View v) {
    }

    public void setObserver(final EditText edTxt) {
        edTxt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (AddTextQuotesActivity.this.isKeyboardShown(edTxt.getRootView())) {
                    AddTextQuotesActivity.this.textView.setCursorVisible(false);
                    AddTextQuotesActivity.this.ch = true;
                } else if (AddTextQuotesActivity.this.ch) {
                    edTxt.setShowSoftInputOnFocus(false);
                    AddTextQuotesActivity.this.gpuImageview.performClick();
                    AddTextQuotesActivity.this.ch = false;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isKeyboardShown(View rootView) {
        Rect r2 = new Rect();
        rootView.getWindowVisibleDisplayFrame(r2);
        return ((float) (rootView.getBottom() - r2.bottom)) > rootView.getResources().getDisplayMetrics().density * 128.0f;
    }

    private void setSelectTextDataOneAtaTime(ArrayList<SelectedTextData> arra, String dlft, EditText editView, int txtID) {
        List<QuotesSelect> quotesw = this.db.getSelectAllRowValue(this.temp_postn);
        for (int i = 0; i < quotesw.size(); i++) {
            if (quotesw.get(i).get_text_id() == txtID) {
                SelectedTextData std = new SelectedTextData();
                std.setStart(Integer.parseInt(quotesw.get(i).get_start()));
                std.setEnd(Integer.parseInt(quotesw.get(i).get_end()));
                Resources resources = getResources();
                if (dlft.equals("default")) {
                    std.setText_size((int) PictureConstant.convertPixelsToDp((float) (((int) Float.parseFloat(quotesw.get(i).get_size())) + ((int) Float.valueOf(editView.getTextSize()).floatValue())), this));
                } else {
                    std.setText_size((int) Float.parseFloat(quotesw.get(i).get_size()));
                }
                std.setText_color(Integer.parseInt(quotesw.get(i).get_color()));
                std.setText_ttf(quotesw.get(i).get_font());
                std.setText_shadowdx(Float.parseFloat(quotesw.get(i).get_shadow_dx()));
                std.setText_shadowdy(Float.parseFloat(quotesw.get(i).get_shadow_dy()));
                std.setText_shadowradius(Float.parseFloat(quotesw.get(i).get_shadow_radius()));
                std.setText_shadowcolor(Integer.parseInt(quotesw.get(i).get_shadow_color()));
                std.setText_shader(quotesw.get(i).get_shader());
                std.setText_bold(Boolean.valueOf(quotesw.get(i).get_textbold()).booleanValue());
                std.setText_italic(Boolean.valueOf(quotesw.get(i).get_text_italic()).booleanValue());
                std.setText_underline(Boolean.valueOf(quotesw.get(i).get_text_underline()).booleanValue());
                std.setText_strike(Boolean.valueOf(quotesw.get(i).get_text_strik()).booleanValue());
                arra.add(std);
            }
        }
        defaultsetup();
    }

    private int getOptimumTextSize(int start2, int end2, SpannableStringBuilder text, RectF availableSpace, TextPaint textPaint, int gravity) {
        return binarySearch(start2, end2, text, availableSpace, textPaint, gravity);
    }

    private int binarySearch(int start2, int end2, SpannableStringBuilder text, RectF availableSpace, TextPaint textPaint, int gravity) {
        int lastBest = start2;
        int lo = start2;
        int hi = end2 - 1;
        while (lo <= hi) {
            int i = (lo + hi) >>> 1;
            int midValCmp = onTestSize(i, text, availableSpace, textPaint, gravity);
            if (midValCmp < 0) {
                lastBest = lo;
                lo = i + 1;
            } else if (midValCmp <= 0) {
                return i;
            } else {
                hi = i - 1;
                lastBest = hi;
            }
        }
        return lastBest;
    }

    public int onTestSize(int suggestedSize, SpannableStringBuilder text, RectF availableSpace, TextPaint textPaint, int gravity) {
        int i = suggestedSize;
        int i2 = gravity;
        textPaint.setTextSize((float) i);
        RectF textRect = new RectF();
        float _widthLimit = availableSpace.width();
        StaticLayout layout = null;
        SpannableStringBuilder builder2 = createSpannableString(this.arrayfortv, i, this.spannableString, false);
        if (i2 == 17) {
            layout = new StaticLayout(builder2, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        } else if (i2 == 49) {
            layout = new StaticLayout(builder2, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        } else if (i2 == 51) {
            layout = new StaticLayout(builder2, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        } else if (i2 == 53) {
            layout = new StaticLayout(builder2, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0.0f, true);
        }
        textRect.bottom = (float) layout.getHeight();
        textRect.right = _widthLimit;
        textRect.offsetTo(0.0f, 0.0f);
        if (availableSpace.contains(textRect)) {
            return -1;
        }
        return 1;
    }

    private int getOptimumTextSize1(int start2, int end2, String text, RectF availableSpace, TextPaint textPaint, int gravity) {
        return binarySearch1(start2, end2, text, availableSpace, textPaint, gravity);
    }

    private int binarySearch1(int start2, int end2, String text, RectF availableSpace, TextPaint textPaint, int gravity) {
        int lastBest = start2;
        int lo = start2;
        int hi = end2 - 1;
        while (lo <= hi) {
            int i = (lo + hi) >>> 1;
            int midValCmp = onTestSize1(i, text, availableSpace, textPaint, gravity);
            if (midValCmp < 0) {
                lastBest = lo;
                lo = i + 1;
            } else if (midValCmp <= 0) {
                return i;
            } else {
                hi = i - 1;
                lastBest = hi;
            }
        }
        return lastBest;
    }

    public int onTestSize1(int suggestedSize, String text, RectF availableSpace, TextPaint textPaint, int gravity) {
        int i = gravity;
        textPaint.setTextSize((float) suggestedSize);
        RectF textRect = new RectF();
        float _widthLimit = availableSpace.width();
        StaticLayout layout = null;
        if (i == 17) {
            layout = new StaticLayout(text, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        } else if (i == 49) {
            layout = new StaticLayout(text, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        } else if (i == 51) {
            layout = new StaticLayout(text, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        } else if (i == 53) {
            layout = new StaticLayout(text, textPaint, (int) _widthLimit, Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0.0f, true);
        }
        textRect.bottom = (float) layout.getHeight();
        textRect.right = _widthLimit;
        textRect.offsetTo(0.0f, 0.0f);
        if (availableSpace.contains(textRect)) {
            return -1;
        }
        return 1;
    }

    public SpannableStringBuilder createSpannableString(ArrayList<SelectedTextData> arrayfortv2, int textSize, SpannableString spannableString2, boolean isFirst) {
        Typeface ttq;
        if (arrayfortv2.size() > 0) {
            for (int i = 0; i < arrayfortv2.size(); i++) {
                if (arrayfortv2.get(i).getEnd() <= this.textView.getText().length()) {
                    try {
                        spannableString2.setSpan(new TextAppearanceSpan((String) null, 0, textSize, (ColorStateList) null, (ColorStateList) null), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                        if (isFirst) {
                            if (arrayfortv2.get(i).getText_ttf().equals("")) {
                                ttq = this.textView.getTypeface();
                            } else {
                                ttq = Typeface.createFromAsset(getAssets(), arrayfortv2.get(i).getText_ttf());
                            }
                            spannableString2.setSpan(new CustomTypefaceSpan(ttq), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                            spannableString2.setSpan(new ForegroundColorSpan(arrayfortv2.get(i).getText_color()), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 33);
                            spannableString2.setSpan(new CustomShadowSpan(arrayfortv2.get(i).getText_shadowradius(), arrayfortv2.get(i).getText_shadowdx(), arrayfortv2.get(i).getText_shadowdy(), arrayfortv2.get(i).getText_shadowcolor()), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                            Shader sadr = null;
                            if (!arrayfortv2.get(i).getText_shader().equals("null")) {
                                sadr = new BitmapShader(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(arrayfortv2.get(i).getText_shader(), "drawable", getPackageName())), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                            }
                            spannableString2.setSpan(new CustomShaderSpan(sadr), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                            if (arrayfortv2.get(i).isText_bold()) {
                                spannableString2.setSpan(new StyleSpan(1), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                            } else {
                                spannableString2.removeSpan(new StyleSpan(1));
                            }
                            if (arrayfortv2.get(i).isText_italic()) {
                                spannableString2.setSpan(new StyleSpan(2), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                            } else {
                                spannableString2.removeSpan(new StyleSpan(2));
                            }
                            if (arrayfortv2.get(i).isText_underline()) {
                                spannableString2.setSpan(new UnderlineSpan(), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                            } else {
                                spannableString2.removeSpan(new UnderlineSpan());
                            }
                            if (arrayfortv2.get(i).isText_strike()) {
                                spannableString2.setSpan(new StrikethroughSpan(), arrayfortv2.get(i).getStart(), arrayfortv2.get(i).getEnd(), 0);
                            } else {
                                spannableString2.removeSpan(new StrikethroughSpan());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new SpannableStringBuilder().append(spannableString2);
    }

    @SuppressLint("WrongConstant")
    public void onDelete(View v) {
        this.controll_btn_stckr.setVisibility(8);
        this.controller_sticker.setVisibility(8);
    }

    public boolean setDefaultTouchListener(boolean enable) {
        if (enable) {
            this.text_tv.setClickable(true);
            this.text_tv.setFocusable(true);
            this.text_tv.requestFocus();
            this.text_tv.setEnabled(true);
            this.text_tv.setTextIsSelectable(true);
            this.text_tv.setCursorVisible(false);
            this.text_tv.setOnTouchListener(this.textTouchListener);
            this.selectFocus = true;
            this.ed = false;
            this.flag = false;
            this.flag2 = false;
            EditText editText = this.text_tv;
            this.textView = editText;
            setObserver(editText);
            this.textView.setSelectAllOnFocus(false);
            this.textView.setTextIsSelectable(true);
            this.textView.setCursorVisible(false);
            return true;
        }
        this.text_tv.setClickable(false);
        this.text_tv.setEnabled(false);
        this.text_tv.setOnTouchListener((View.OnTouchListener) null);
        this.selectFocus = false;
        return false;
    }

    public void createDropColorImg(final String type) {
        final ProgressDialog ringProgressDialog1 = ProgressDialog.show(this, "", getString(R.string.plzwait), true);
        ringProgressDialog1.setCancelable(false);
        View view = null;
        if (type.equals("sticker")) {
            int childCount1 = this.fortext.getChildCount();
            for (int i = 0; i < childCount1; i++) {
                View view1 = this.fortext.getChildAt(i);
                if ((view1 instanceof ResizableImageview) && ((ResizableImageview) view1).getBorderVisbilty()) {
                    view = view1;
                    ((ResizableImageview) view1).setBorderVisibility(false);
                }
            }
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    AddTextQuotesActivity.this.complete_img.setDrawingCacheEnabled(true);
                    AddTextQuotesActivity addTextQuotesActivity = AddTextQuotesActivity.this;
                    addTextQuotesActivity.bitRel = Bitmap.createBitmap(addTextQuotesActivity.complete_img.getDrawingCache());
                    AddTextQuotesActivity.this.complete_img.setDrawingCacheEnabled(false);
                    try {
                        AddTextQuotesActivity addTextQuotesActivity2 = AddTextQuotesActivity.this;
                        addTextQuotesActivity2.bb = Bitmap.createBitmap(addTextQuotesActivity2.gpuImageview.capture());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AddTextQuotesActivity addTextQuotesActivity3 = AddTextQuotesActivity.this;
                    AddTextQuotesActivity.bitmapOriginal = addTextQuotesActivity3.mergeBitmap(addTextQuotesActivity3.bb, AddTextQuotesActivity.this.bitRel);
                    Thread.sleep(1000);
                } catch (Exception e2) {
                }
                ringProgressDialog1.dismiss();
            }
        }).start();
        final View finalView = view;
        ringProgressDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (type.equals("sticker")) {
                    ((ResizableImageview) finalView).setBorderVisibility(true);
                }
                Intent ii = new Intent(AddTextQuotesActivity.this, PickColorImageActivity.class);
                ii.putExtra("type", type);
                AddTextQuotesActivity.this.startActivity(ii);
            }
        });
    }

    public void ongetImageBitmap(String imgType) {
        if (imgType.equals("image")) {
            this.drawableName = "";
            Bitmap bitmap2 = CropActivityTwo.bitmapImage;
            float f2 = this.screenWidth;
            Bitmap resizeBitmap = ImageUtils.resizeBitmap(bitmap2, (int) f2, (int) f2);
            this.bitmap = resizeBitmap;
            setImageInBackgrounds(resizeBitmap);
            CropActivityTwo.bitmapImage.recycle();
        }
    }

    private int getPositionFromCategory(String category) {
        String[] TITLES = getResources().getStringArray(R.array.listOfManageQuotesItem);
        for (int i = 0; i < TITLES.length; i++) {
            if (category.equals(TITLES[i])) {
                return i;
            }
        }
        return 0;
    }
}

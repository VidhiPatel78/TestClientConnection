package com.text.with.sticker.textonphoto.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.text.with.sticker.textonphoto.R;


public class EditQuoteActivity extends Activity {
    View.OnClickListener cancelClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditQuoteActivity.this.finish();
        }
    };
    ImageView cancel_edit;
    View.OnClickListener doneCLickListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditQuoteActivity.this.hidesoftinput();
            String edited_quote = EditQuoteActivity.this.quote_edittext.getText().toString();
            if (edited_quote.isEmpty()) {
                EditQuoteActivity.this.quote_edittext.setError(EditQuoteActivity.this.getResources().getString(R.string.error_edit));
            }
            if (edited_quote.length() > 500) {
                EditQuoteActivity.this.quote_edittext.setError(EditQuoteActivity.this.getResources().getString(R.string.max_limit_quote));
            } else if (!EditQuoteActivity.this.isEdited) {
                EditQuoteActivity.this.finish();
            } else if (edited_quote.isEmpty()) {
                EditQuoteActivity.this.quote_edittext.setError(EditQuoteActivity.this.getResources().getString(R.string.error_edit));
            } else {
                Intent intent = new Intent();
                intent.putExtra("quote_edit", edited_quote);
                EditQuoteActivity.this.setResult(-1, intent);
                EditQuoteActivity.this.finish();
            }
        }
    };
    ImageView done_edit;
    /* access modifiers changed from: private */
    public boolean isEdited = false;
    SharedPreferences preferences;
    EditText quote_edittext;
    String quotedText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_quotes);
        init();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.quote_edittext.requestFocus();
        getWindow().setSoftInputMode(5);
    }

    private void init() {
        this.cancel_edit = (ImageView) findViewById(R.id.cancel_edit);
        this.done_edit = (ImageView) findViewById(R.id.done_edit);
        this.quote_edittext = (EditText) findViewById(R.id.quote_edit_text);
        this.done_edit.setOnClickListener(this.doneCLickListener);
        this.cancel_edit.setOnClickListener(this.cancelClickListener);
        String stringExtra = getIntent().getStringExtra("quote");
        this.quotedText = stringExtra;
        if (!stringExtra.equals("")) {
            EditText editText = this.quote_edittext;
            editText.setText("" + this.quotedText);
        }
        this.quote_edittext.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    boolean unused = EditQuoteActivity.this.isEdited = true;
                    if (s.length() >= 500) {
                        EditQuoteActivity.this.quote_edittext.setError(EditQuoteActivity.this.getResources().getString(R.string.max_limit_quote));
                    } else {
                        EditQuoteActivity.this.quote_edittext.setError((CharSequence) null);
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        ((RelativeLayout) findViewById(R.id.lay_touch)).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                EditQuoteActivity.this.hidesoftinput();
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void hidesoftinput() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.preferences.getBoolean("isAdsDisabled", false);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.preferences.getBoolean("isAdsDisabled", false);
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

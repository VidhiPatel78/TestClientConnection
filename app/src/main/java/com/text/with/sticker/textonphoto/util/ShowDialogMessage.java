package com.text.with.sticker.textonphoto.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import com.text.with.sticker.textonphoto.R;


public class ShowDialogMessage {
    public void showMessage(Context context, String title, String msg) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= 21) {
            builder = new AlertDialog.Builder(context);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title).setMessage(msg).setPositiveButton("17039379", new DialogInterface.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.util.ShowDialogMessage.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int which) {
            }
        }).setIcon(R.mipmap.ic_launcher).show();
    }
}

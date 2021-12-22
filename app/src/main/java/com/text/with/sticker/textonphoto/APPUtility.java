package com.text.with.sticker.textonphoto;

import android.os.Environment;

import java.io.File;

public class APPUtility {
    public static final String Account_name = "Elvee Infotech";
    public static final String PrivacyPolicy = "https://www.google.com/";

    public static File getAppDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }
}

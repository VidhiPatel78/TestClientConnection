package com.text.with.sticker.textonphoto.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.text.with.sticker.textonphoto.utility.CategoryInfo;
import com.text.with.sticker.textonphoto.utility.QuotesInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String BLUR = "BLUR";
    private static final String CATEGORY = "CATEGORY";
    private static final String CATEGORY_DISPLAY_SEQ = "CATEGORY_DISPLAY_SEQ";
    private static final String CATEGORY_DRAWABLE = "CATEGORY_DRAWABLE";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String CATEGORY_STATUS = "CATEGORY_STATUS";
    private static final String COLLUM_ID = "COLLUM_ID";
    private static final String COLOR = "COLOR";
    private static final String COMP_ID = "COMP_ID";
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORY_ID INTEGER PRIMARY KEY,CATEGORY_NAME TEXT,CATEGORY_STATUS TEXT,CATEGORY_DRAWABLE TEXT,CATEGORY_DISPLAY_SEQ TEXT)";
    private static final String CREATE_TABLE_QUOTES = "CREATE TABLE IF NOT EXISTS QUOTES(QUOTES_ID INTEGER PRIMARY KEY,CATEGORY_ID TEXT,QUOTES_TEXT TEXT,QUOTES_LIKED TEXT,QUOTES_UTP TEXT)";
    private static final String CREATE_TEXT_NORMAL_TABLE = "CREATE TABLE IF NOT EXISTS TABLE_TEXT_NORMAL(ID INTEGER PRIMARY KEY,TEMPLATE TEXT,COLLUM_ID TEXT,GRAVITY TEXT,SCALE TEXT,TEXT_VALUE TEXT,SIZE TEXT,COLOR TEXT,FONT TEXT,SHADOW_DX_ TEXT,SHADOW_DY TEXT,SHADOW_RADIOUS TEXT,SHADOW_COLOR TEXT,SHADER TEXT,TEXT_BOLD TEXT,TEXT_ITALIC TEXT,TEXT_UNDERLINE TEXT,TEXT_STRIKE TEXT,POS_X TEXT,POS_Y TEXT,ROTATION TEXT,USER_IMAGE TEXT,LOGO_IMAGE TEXT,HAS_AUTHOR TEXT,BLUR TEXT,FILTER_TYPE TEXT,FILTER_PERCENTAGE TEXT,IS_CROPPED TEXT,TEXT_ORDER TEXT)";
    private static final String CREATE_TEXT_PUNCH_TABLE = "CREATE TABLE IF NOT EXISTS TABLE_TEXT_PUNCH(ID INTEGER PRIMARY KEY,COLLUM_ID TEXT,TEXT_ID TEXT,START TEXT,END TEXT,SIZE TEXT,COLOR TEXT,FONT TEXT,SHADOW_DX_ TEXT,SHADOW_DY TEXT,SHADOW_RADIOUS TEXT,SHADOW_COLOR TEXT,SHADER TEXT,TEXT_BOLD TEXT,TEXT_ITALIC TEXT,TEXT_UNDERLINE TEXT,TEXT_STRIKE TEXT)";
    private static final String DATABASE_NAME = "BESTQUOTES_DB";
    private static String DATABASE_PATH = "";
    private static final int DATABASE_VERSION = 1;
    private static final String END = "END";
    private static final String FILTER_PERCENTAGE = "FILTER_PERCENTAGE";
    private static final String FILTER_TYPE = "FILTER_TYPE";
    private static final String FONT = "FONT";
    private static final String GRAVITY = "GRAVITY";
    private static final String HAS_AUTHOR = "HAS_AUTHOR";
    private static final String HEIGHT = "HEIGHT";
    private static final String HUE = "HUE";
    private static final String ID = "ID";
    private static final String IS_CROPPED = "IS_CROPPED";
    private static final String LEFT = "LEFT";
    private static final String LOGO_IMAGE = "LOGO_IMAGE";
    private static final String OPACITY = "OPACITY";
    private static final String ORDER = "ORDER";
    private static final String POS_X = "POS_X";
    private static final String POS_Y = "POS_Y";
    private static final String QUOTES = "QUOTES";
    private static final String QUOTES_ID = "QUOTES_ID";
    private static final String QUOTES_LIKED = "QUOTES_LIKED";
    private static final String QUOTES_TEXT = "QUOTES_TEXT";
    private static final String QUOTES_UTP = "QUOTES_UTP";
    private static final String RES_ID = "RES_ID";
    private static final String ROTATION = "ROTATION";
    private static final String SCALE = "SCALE";
    private static final String SHADER = "SHADER";
    private static final String SHADOW_COLOR = "SHADOW_COLOR";
    private static final String SHADOW_DX_ = "SHADOW_DX_";
    private static final String SHADOW_DY = "SHADOW_DY";
    private static final String SHADOW_RADIOUS = "SHADOW_RADIOUS";
    private static final String SIZE = "SIZE";
    private static final String START = "START";
    private static final String TABLE_TEXT_NORMAL = "TABLE_TEXT_NORMAL";
    private static final String TABLE_TEXT_PUNCH = "TABLE_TEXT_PUNCH";
    private static final String TEMPLATE = "TEMPLATE";
    private static final String TEXT_BOLD = "TEXT_BOLD";
    private static final String TEXT_ID = "TEXT_ID";
    private static final String TEXT_ITALIC = "TEXT_ITALIC";
    private static final String TEXT_ORDER = "TEXT_ORDER";
    private static final String TEXT_STRIKE = "TEXT_STRIKE";
    private static final String TEXT_UNDERLINE = "TEXT_UNDERLINE";
    private static final String TEXT_VALUE = "TEXT_VALUE";
    private static final String TOP = "TOP";
    private static final String TYPE = "TYPE";
    private static final String Top_ID = "Top_ID";
    private static final String USER_IMAGE = "USER_IMAGE";
    private static final String WIDHT = "WIDHT";
    private static final String X = "X";
    private static final String Y = "Y";
    private static final String Y_ROTATION = "Y_ROTATION";
    Context mContext;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_PATH, (SQLiteDatabase.CursorFactory) null, 1);
        this.mContext = context;
    }

    public static DatabaseHandler getDbHandler(Context context) {
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        return new DatabaseHandler(context);
    }

    public void onCreate(SQLiteDatabase db) {
        if (createDBStructure(db)) {
            try {
                String dbFilePath = copyShippedDataBase();
                if (dbFilePath != null) {
                    CopyTempTablesToLocalDB(db, dbFilePath);
                    db_delete(dbFilePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            } catch (Error e3) {
                e3.printStackTrace();
            }
        }
        Log.i("testing", "Database Created");
    }

    private boolean createDBStructure(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_QUOTES);
        db.execSQL(CREATE_TEXT_NORMAL_TABLE);
        db.execSQL(CREATE_TEXT_PUNCH_TABLE);
        return true;
    }

    private String copyShippedDataBase() throws IOException {
        try {
            InputStream myInput = this.mContext.getAssets().open("BESTQUOTES_DB.sqlite");
            String path = this.mContext.getDatabasePath("Temp.db").getPath();
            File dbFile = new File(path);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }
            OutputStream myOutput = new FileOutputStream(dbFile);
            byte[] buffer = new byte[2048];
            while (true) {
                int length = myInput.read(buffer);
                if (length > 0) {
                    myOutput.write(buffer, 0, length);
                } else {
                    myOutput.flush();
                    myOutput.close();
                    myInput.close();
                    return path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean CopyTempTablesToLocalDB(SQLiteDatabase db, String dbFilePath) {
        SQLException e2;
        int i = 1;
        try {
            db.setTransactionSuccessful();
            db.endTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("ATTACH DATABASE '");
            try {
                sb.append(dbFilePath);
                sb.append("' AS TEMP1");
                db.execSQL(sb.toString());
                db.beginTransaction();
                String[] strArr = null;
                try {
                    Cursor cursorCategory = db.rawQuery("SELECT  * FROM TEMP1. CATEGORY ORDER BY CATEGORY_ID ASC;", null);
                    if (cursorCategory == null || cursorCategory.getCount() <= 0 || !cursorCategory.moveToFirst()) {
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.execSQL("DETACH DATABASE TEMP1;");
                        db.beginTransaction();
                        return false;
                    }
                    while (true) {
                        CategoryInfo values = new CategoryInfo();
                        values.setCATEGORY_ID(cursorCategory.getInt(0));
                        values.setCATEGORY_NAME(cursorCategory.getString(i));
                        int i2 = 2;
                        values.setCATEGORY_STATUS(cursorCategory.getString(2));
                        values.setCATEGORY_DRAWABLE(cursorCategory.getString(3));
                        values.setCATEGORY_DISPLAY_SEQ(cursorCategory.getInt(4));
                        long newTempId = insertCategoryRowData(values, db);
                        Log.e("category name", "" + cursorCategory.getString(i));
                        Cursor cursorText = db.rawQuery("SELECT * FROM TEMP1.QUOTES WHERE CATEGORY_ID='" + values.getCATEGORY_ID() + "'", strArr);
                        if (cursorText != null && cursorText.getCount() > 0 && cursorText.moveToFirst()) {
                            while (true) {
                                QuotesInfo valuesTextInfo = new QuotesInfo();
                                valuesTextInfo.setQUOTES_ID(cursorText.getInt(0));
                                valuesTextInfo.setCATEGORY_ID(cursorText.getInt(i));
                                valuesTextInfo.setQUOTES_TEXT(cursorText.getString(i2));
                                valuesTextInfo.setQUOTES_LIKED(cursorText.getString(3));
                                valuesTextInfo.setQUOTES_UTP(cursorText.getString(4));
                                valuesTextInfo.setCATEGORY_ID((int) newTempId);
                                insertQuotesRowData(valuesTextInfo, db);
                                Log.e("QUOTES_LIKED name", "" + cursorText.getString(3));
                                if (!cursorText.moveToNext()) {
                                    break;
                                }
                                i = 1;
                                i2 = 2;
                            }
                        }
                        if (!cursorCategory.moveToNext()) {
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.execSQL("DETACH DATABASE TEMP1;");
                            db.beginTransaction();
                            return false;
                        }
                        i = 1;
                        strArr = null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return true;
                }
            } catch (SQLException e3) {
                e2 = e3;
                Log.e("RK Error", "Couldnt Attach DB" + e2.toString());
                e2.printStackTrace();
                return true;
            }
        } catch (SQLException e4) {
            e2 = e4;
            Log.e("RK Error", "Couldnt Attach DB" + e2.toString());
            e2.printStackTrace();
            return true;
        }
    }

    public void db_delete(String dbFilePath) {
        File file = new File(dbFilePath);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertCategoryRowData(CategoryInfo tInfo, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, tInfo.getCATEGORY_NAME());
        values.put(CATEGORY_STATUS, tInfo.getCATEGORY_STATUS());
        values.put(CATEGORY_DRAWABLE, tInfo.getCATEGORY_DRAWABLE());
        values.put(CATEGORY_DISPLAY_SEQ, Integer.valueOf(tInfo.getCATEGORY_DISPLAY_SEQ()));
        Log.i("testing", "Before insertion ");
        long insert = db.insert(CATEGORY, null, values);
        Log.i("testing", "ID " + insert);
        Log.i("testing", "cate Name " + tInfo.getCATEGORY_NAME());
        Log.i("testing", "status " + tInfo.getCATEGORY_STATUS());
        return insert;
    }

    public void insertQuotesRowData(QuotesInfo tInfo, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY_ID, Integer.valueOf(tInfo.getCATEGORY_ID()));
        values.put(QUOTES_TEXT, tInfo.getQUOTES_TEXT());
        values.put(QUOTES_LIKED, tInfo.getQUOTES_LIKED());
        values.put(QUOTES_UTP, tInfo.getQUOTES_UTP());
        Log.i("testing", "Before QUOTES insertion ");
        Log.i("testing", "QUOTES ID " + db.insert(QUOTES, null, values));
    }

    public ArrayList<CategoryInfo> getCategooryListDes() {
        ArrayList<CategoryInfo> categoryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM CATEGORY", null);
        if (cursor == null || cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            db.close();
            Log.e("quotesInfoList size", "" + categoryList.size());
            return categoryList;
        }
        do {
            CategoryInfo values = new CategoryInfo();
            values.setCATEGORY_ID(cursor.getInt(0));
            values.setCATEGORY_NAME(cursor.getString(1));
            values.setCATEGORY_STATUS(cursor.getString(2));
            values.setCATEGORY_DRAWABLE(cursor.getString(3));
            values.setCATEGORY_DISPLAY_SEQ(cursor.getInt(4));
            Log.e("category name", "" + cursor.getString(3));
            categoryList.add(values);
        } while (cursor.moveToNext());
        db.close();
        Log.e("quotesInfoList size", "" + categoryList.size());
        return categoryList;
    }

    public ArrayList<QuotesInfo> getQuotesList(int categoryId, String strSearch) {
        String str;
        ArrayList<QuotesInfo> quotesyList = new ArrayList<>();
        if (strSearch.equals("")) {
            str = "SELECT * FROM QUOTES WHERE CATEGORY_ID='" + categoryId + "'";
        } else {
            str = "";
            String[] splited = strSearch.split("\\s+");
            for (int k = 0; k < splited.length; k++) {
                str = k == 0 ? "SELECT * FROM QUOTES WHERE ( QUOTES_TEXT like '% " + splited[k] + "%' OR " + QUOTES_TEXT + " like '" + splited[k] + "%' )" : "SELECT * FROM QUOTES WHERE AND ( QUOTES_TEXT like '% " + splited[k] + "%' OR " + QUOTES_TEXT + " like '" + splited[k] + "%' )";
            }
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(str, null);
        if (cursor == null || cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            db.close();
            Log.e("quotesyList size", "" + quotesyList.size());
            return quotesyList;
        }
        do {
            QuotesInfo values = new QuotesInfo();
            values.setQUOTES_ID(cursor.getInt(0));
            values.setCATEGORY_ID(cursor.getInt(1));
            values.setQUOTES_TEXT(cursor.getString(2));
            values.setQUOTES_LIKED(cursor.getString(3));
            values.setQUOTES_UTP(cursor.getString(4));
            quotesyList.add(values);
        } while (cursor.moveToNext());
        db.close();
        Log.e("quotesyList size", "" + quotesyList.size());
        return quotesyList;
    }

    public ArrayList<QuotesInfo> getLikedQuotesList(int quote_Id) {
        String query;
        ArrayList<QuotesInfo> quotesyList = new ArrayList<>();
        if (quote_Id == 0) {
            query = "SELECT * FROM QUOTES WHERE QUOTES_LIKED='Liked' order  by datetime(QUOTES_UTP) DESC";
        } else {
            query = "SELECT * FROM QUOTES WHERE QUOTES_ID='" + quote_Id + "'";
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null || cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            db.close();
            Log.e("quotesyList size", "" + quotesyList.size());
            return quotesyList;
        }
        do {
            QuotesInfo values = new QuotesInfo();
            values.setQUOTES_ID(cursor.getInt(0));
            values.setCATEGORY_ID(cursor.getInt(1));
            values.setQUOTES_TEXT(cursor.getString(2));
            values.setQUOTES_LIKED(cursor.getString(3));
            values.setQUOTES_UTP(cursor.getString(4));
            quotesyList.add(values);
        } while (cursor.moveToNext());
        db.close();
        Log.e("quotesyList size", "" + quotesyList.size());
        return quotesyList;
    }

    public boolean updateQuotesLikedStatus(int quotesId, String type) {
        String strDate;
        try {
            if (type.equals("Liked")) {
                String strDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                Log.e("strDate is", "" + strDate2);
                strDate = "UPDATE QUOTES SET QUOTES_LIKED='Liked' , QUOTES_UTP='" + strDate2 + "' WHERE " + QUOTES_ID + "='" + quotesId + "'";
            } else {
                strDate = "UPDATE QUOTES SET QUOTES_LIKED='Like' WHERE QUOTES_ID='" + quotesId + "'";
            }
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(strDate);
            db.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addQuotes(Quotes quotes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEMPLATE, quotes.get_template());
        values.put(COLLUM_ID, Integer.valueOf(quotes.get_collum_id()));
        values.put(GRAVITY, Integer.valueOf(quotes.get_gravity()));
        values.put(SCALE, quotes.get_scale());
        values.put(TEXT_VALUE, quotes.get_text());
        values.put(SIZE, quotes.get_size());
        values.put(COLOR, quotes.get_color());
        values.put(FONT, quotes.get_font());
        values.put(SHADOW_DX_, quotes.get_shadow_dx());
        values.put(SHADOW_DY, quotes.get_shadow_dy());
        values.put(SHADOW_RADIOUS, quotes.get_shadow_radius());
        values.put(SHADOW_COLOR, quotes.get_shadow_color());
        values.put(SHADER, quotes.get_shader());
        values.put(TEXT_BOLD, quotes.get_textbold());
        values.put(TEXT_ITALIC, quotes.get_text_italic());
        values.put(TEXT_UNDERLINE, quotes.get_text_underline());
        values.put(TEXT_STRIKE, quotes.get_text_strik());
        values.put(POS_X, Integer.valueOf(quotes.getPos_x()));
        values.put(POS_Y, Integer.valueOf(quotes.getPos_y()));
        values.put(ROTATION, Integer.valueOf(quotes.getRotation()));
        values.put(USER_IMAGE, quotes.get_user_image());
        values.put(LOGO_IMAGE, quotes.get_logo_image());
        values.put(HAS_AUTHOR, quotes.getHasAuthor());
        values.put(BLUR, Integer.valueOf(quotes.getBlur()));
        values.put(FILTER_TYPE, quotes.getFilter());
        values.put(FILTER_PERCENTAGE, Integer.valueOf(quotes.getFilter_percentage()));
        values.put(IS_CROPPED, quotes.getIsCropped());
        values.put(TEXT_ORDER, Integer.valueOf(quotes.getOrder()));
        db.insert(TABLE_TEXT_NORMAL, null, values);
        db.close();
    }

    public List<Quotes> getQuotesAllRowValue(int id) {
        List<Quotes> quotesList = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("SELECT * FROM TABLE_TEXT_NORMAL WHERE COLLUM_ID ='" + id + "'", null);
        if (cursor.moveToFirst()) {
            do {
                Quotes quotes = new Quotes();
                quotes.setID(Integer.parseInt(cursor.getString(0)));
                quotes.set_template(cursor.getString(1));
                quotes.set_collum_id(cursor.getInt(2));
                quotes.set_gravity(cursor.getInt(3));
                quotes.set_scale(cursor.getString(4));
                quotes.set_text(cursor.getString(5));
                quotes.set_size(cursor.getString(6));
                quotes.set_color(cursor.getString(7));
                quotes.set_font(cursor.getString(8));
                quotes.set_shadow_dx(cursor.getString(9));
                quotes.set_shadow_dy(cursor.getString(10));
                quotes.set_shadow_radius(cursor.getString(11));
                quotes.set_shadow_color(cursor.getString(12));
                quotes.set_shader(cursor.getString(13));
                quotes.set_textbold(cursor.getString(14));
                quotes.set_text_italic(cursor.getString(15));
                quotes.set_text_underline(cursor.getString(16));
                quotes.set_text_strik(cursor.getString(17));
                quotes.setPos_x(Integer.parseInt(cursor.getString(18)));
                quotes.setPos_y(Integer.parseInt(cursor.getString(19)));
                quotes.setRotation(Integer.parseInt(cursor.getString(20)));
                quotes.set_user_image(cursor.getString(21));
                quotes.set_logo_image(cursor.getString(22));
                quotes.setHasAuthor(cursor.getString(23));
                quotes.setBlur(Integer.parseInt(cursor.getString(24)));
                quotes.setFilter(cursor.getString(25));
                quotes.setFilter_percentage(Integer.parseInt(cursor.getString(26)));
                quotes.setIsCropped(cursor.getString(27));
                quotes.setOrder(Integer.parseInt(cursor.getString(28)));
                quotesList.add(quotes);
            } while (cursor.moveToNext());
        }
        return quotesList;
    }

    public void addQuoteSelect(QuotesSelect quotes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLLUM_ID, Integer.valueOf(quotes.get_collum_id()));
        values.put(TEXT_ID, Integer.valueOf(quotes.get_text_id()));
        values.put(START, quotes.get_start());
        values.put(END, quotes.get_end());
        values.put(SIZE, quotes.get_size());
        values.put(COLOR, quotes.get_color());
        values.put(FONT, quotes.get_font());
        values.put(SHADOW_DX_, quotes.get_shadow_dx());
        values.put(SHADOW_DY, quotes.get_shadow_dy());
        values.put(SHADOW_RADIOUS, quotes.get_shadow_radius());
        values.put(SHADOW_COLOR, quotes.get_shadow_color());
        values.put(SHADER, quotes.get_shader());
        values.put(TEXT_BOLD, quotes.get_textbold());
        values.put(TEXT_ITALIC, quotes.get_text_italic());
        values.put(TEXT_UNDERLINE, quotes.get_text_underline());
        values.put(TEXT_STRIKE, quotes.get_text_strik());
        db.insert(TABLE_TEXT_PUNCH, null, values);
        db.close();
    }

    public List<QuotesSelect> getSelectAllRowValue(int id) {
        List<QuotesSelect> quotesList = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("SELECT * FROM TABLE_TEXT_PUNCH WHERE COLLUM_ID =" + id + ";", null);
        if (cursor.moveToFirst()) {
            do {
                QuotesSelect quotes = new QuotesSelect();
                quotes.setID(Integer.parseInt(cursor.getString(0)));
                quotes.set_collum_id(cursor.getInt(1));
                quotes.set_text_id(cursor.getInt(2));
                quotes.set_start(cursor.getString(3));
                quotes.set_end(cursor.getString(4));
                quotes.set_size(cursor.getString(5));
                quotes.set_color(cursor.getString(6));
                quotes.set_font(cursor.getString(7));
                quotes.set_shadow_dx(cursor.getString(8));
                quotes.set_shadow_dy(cursor.getString(9));
                quotes.set_shadow_radius(cursor.getString(10));
                quotes.set_shadow_color(cursor.getString(11));
                quotes.set_shader(cursor.getString(12));
                quotes.set_textbold(cursor.getString(13));
                quotes.set_text_italic(cursor.getString(14));
                quotes.set_text_underline(cursor.getString(15));
                quotes.set_text_strik(cursor.getString(16));
                quotesList.add(quotes);
            } while (cursor.moveToNext());
        }
        return quotesList;
    }
}

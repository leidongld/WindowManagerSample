package com.example.leidong.windowmanagersample.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by leidong on 2017/5/3.
 */

public class ItemsDBHelper extends SQLiteOpenHelper {
    //数据库版本
    private static final int DATABASE_VERSION = 3;
    //数据库名称
    private static final String DATABASE_NAME = "listview_items";
    //表名称
    private static final String TABLE_NAME = "listview_items";
    //创建表的语句
    private static final String CREATE_DATABASE_TABLE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + ItemColumn._ID + " integer PRIMARY KEY AUTOINCREMENT,"
            + ItemColumn.ITEMNAME + " text"
            + ItemColumn.ITEMDETAIL + " text"
            + ")";

    public ItemsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public ItemsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

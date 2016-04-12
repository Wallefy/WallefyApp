package com.example.dpene.wallefy.model.datasources;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.dpene.wallefy.model.DbHelper;

public abstract class DataSource {

    protected SQLiteDatabase database;
    protected DbHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

}

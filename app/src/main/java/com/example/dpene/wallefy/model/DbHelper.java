package com.example.dpene.wallefy.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dpene.wallefy.model.queries.CreateTableQueries;
import com.example.dpene.wallefy.model.utils.Constants;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance;

    private DbHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    public static DbHelper getInstance(Context context){
        if(instance == null){
            instance = new DbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
//            db.execSQL("PRAGMA foreign_keys = ON");
            db.execSQL(CreateTableQueries.CREATE_TABLE_ACCOUNT_TYPES);
            db.execSQL(CreateTableQueries.CREATE_TABLE_CATEGORIES);
            db.execSQL(CreateTableQueries.CREATE_TABLE_USERS);
            db.execSQL(CreateTableQueries.CREATE_TABLE_HISTORY);

        } catch (SQLiteException e) {
            Log.e("SQL ERROR", "UNABLE TO CREATE TABLES" + e);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

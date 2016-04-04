package com.example.dpene.wallefy.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dpene.wallefy.model.queries.Queries;
import com.example.dpene.wallefy.model.utils.Constants;

/**
 * Created by Petkata on 4.4.2016 г..
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(Queries.CREATE_TABLE_ACCOUNT_TYPES);
            db.execSQL(Queries.CREATE_TABLE_CATEGORIES);
            db.execSQL(Queries.CREATE_TABLE_USERS);
            db.execSQL(Queries.CREATE_TABLE_HISTORY);


        } catch (SQLiteException e) {
            Log.e("SQL ERROR", "UNABLE TO CREATE TABLES" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            TODO what is best here ?
    }
}

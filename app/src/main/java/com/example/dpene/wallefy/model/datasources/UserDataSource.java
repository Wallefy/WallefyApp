package com.example.dpene.wallefy.model.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.utils.Constants;


public class UserDataSource extends DataSource implements IUserDao {

    public UserDataSource(Context context) {
        super(context);
    }

    @Override
    public long loginUser(String userEmail, String password) {

        String[] selArgs = {userEmail,password};
        Cursor cursor = database.rawQuery("select user_id from users where user_email = ? and user_password = ?",selArgs);
        if (cursor.moveToFirst()) {
            long userId = cursor.getLong(0);
            cursor.close();
            return userId;
        }
        cursor.close();
        return 0;
    }

    @Override
    public long registerUser(String userEmail, String userName, String password) {

        if (checkForExisting(Constants.TABLE_USERS, Constants.USER_EMAIL, userEmail)) {
            return 0;
        }

        ContentValues values = new ContentValues();
        values.put(Constants.USER_NAME, userName);
        values.put(Constants.USER_PASSWORD, password);
        values.put(Constants.USER_EMAIL, userEmail);
        long insertId = database.insert(Constants.TABLE_USERS, null, values);
        if (insertId < 0) {
            return -2;
        }

        return insertId;

    }

    public boolean checkForExisting(String table, String column, String selectionArg) {
        String[] columns = {column};
        String[] selectionArgs = {selectionArg};
        Cursor cursor = database.query(table, columns, column + " =? ", selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editUsername(long userId, String newUsername) {
        return false;
    }

    @Override
    public boolean editEmail(long userId, String newEmail) {
        return false;
    }

    @Override
    public boolean editPassword(long userId, String newPassword) {
        return false;
    }

    @Override
    public boolean deleteUser(String email) {
        return false;
    }
}